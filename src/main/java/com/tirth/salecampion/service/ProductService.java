package com.tirth.salecampion.service;

import com.tirth.salecampion.model.*;
import com.tirth.salecampion.model.dtos.ProductDTO;
import com.tirth.salecampion.model.dtos.ProductPageResponse;
import com.tirth.salecampion.repository.CampaignRepository;
import com.tirth.salecampion.repository.PriceHistoryRepository;
import com.tirth.salecampion.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {
    private final CampaignRepository campaignRepository;
    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    @Autowired
    public ProductService(CampaignRepository campaignRepository, ProductRepository productRepository, PriceHistoryRepository priceHistoryRepository) {
        this.campaignRepository = campaignRepository;
        this.productRepository = productRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }


    public ProductPageResponse getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findAllWithPrice(pageable);

        ProductPageResponse response = new ProductPageResponse();
        response.setProducts(productsPage.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()));
        response.setPageNumber(productsPage.getNumber() + 1);
        response.setPageSize(productsPage.getSize());
        response.setTotalElements(productsPage.getTotalElements());
        response.setTotalPages(productsPage.getTotalPages());
        response.setLast(productsPage.isLast());

        return response;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setMrp(product.getMrp());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setDiscount(product.getDiscount());
        dto.setInventory(product.getInventory());
        return dto;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Product not found with ID: " + id));
        return convertToDTO(product);
    }

    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    public Page<Product> getAll(int pgNo, int pgSize) {
        Pageable pages = PageRequest.of(pgNo, pgSize);
        return productRepository.findAll(pages);
    }

    public Product findById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<PriceHistory> getPriceHistory(long productId) {
        return priceHistoryRepository.findByProductId(productId);
    }

    public void updateProductPrices(List<Campaign> activeCampaigns) {
        for (Campaign campaign : activeCampaigns) {
            for (CampaignDiscount discount : campaign.getCampaignDiscounts()) {
                Long productId = discount.getProductId();
                productRepository.findById(productId).ifPresent(product -> {
                    // Save price history
                    priceHistoryRepository.savePriceHistory(product.getId(), product.getCurrentPrice());

                    // Calculate new price
                    BigDecimal discountAmount = product.getMrp().multiply(BigDecimal.valueOf(discount.getDiscount())).divide(BigDecimal.valueOf(100));
                    BigDecimal newPrice = product.getMrp().subtract(discountAmount);

                    // Update product price
                    productRepository.updateProductPrice(productId, newPrice, discount.getDiscount());
                });
            }

            // Mark campaign as COMPLETED after processing
            campaignRepository.updateCampaignStatus(campaign.getId(), CampaignStatus.COMPLETED);
        }
    }

    public void resetPricesForExpiredCampaigns(List<Campaign> expiredCampaigns) {
        List<Long> productIds = expiredCampaigns.stream()
                .flatMap(campaign -> campaign.getCampaignDiscounts().stream())
                .map(CampaignDiscount::getProductId)
                .collect(Collectors.toList());

        if (!productIds.isEmpty()) {
            productRepository.resetPricesForProducts(productIds);
        }

        for (Campaign campaign : expiredCampaigns) {
            // Mark campaign as EXPIRED
            campaignRepository.updateCampaignStatus(campaign.getId(), CampaignStatus.EXPIRED);
        }
    }
}