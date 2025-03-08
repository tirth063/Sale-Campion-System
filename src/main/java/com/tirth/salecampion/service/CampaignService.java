package com.tirth.salecampion.service;

import com.tirth.salecampion.model.Campaign;
import com.tirth.salecampion.model.dtos.CampaignCreateDTO;
import com.tirth.salecampion.model.CampaignDiscount;
import com.tirth.salecampion.model.CampaignStatus;
import com.tirth.salecampion.repository.CampaignRepository;
import com.tirth.salecampion.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Retrieves all active campaigns for the current date.
     *
     * @return List of active campaigns
     */
    public List<Campaign> getActiveCampaigns() {
        return campaignRepository.findActiveCampaigns(LocalDate.now());
    }

    public List<Campaign> getExpiredCampaigns() {
        return campaignRepository.findExpiredCampaigns(LocalDate.now());
    }

    public List<Campaign> getUpcomingCampaigns() {
        return campaignRepository.findUpcomingCampaigns(LocalDate.now());
    }

    public Campaign createCampaign(CampaignCreateDTO campaignCreateDTO) {
        Campaign campaign = new Campaign();
        campaign.setTitle(campaignCreateDTO.getTitle());
        campaign.setStartDate(campaignCreateDTO.getStartDate());
        campaign.setEndDate(campaignCreateDTO.getEndDate());
        campaign.setStatus(CampaignStatus.DRAFT);

        List<CampaignDiscount> discounts = campaignCreateDTO.getDiscounts().stream().map(discountDTO -> {
            CampaignDiscount discount = new CampaignDiscount();
            discount.setProductId(discountDTO.getProductId());
            discount.setDiscount(discountDTO.getDiscount());
            discount.setCampaign(campaign);
            return discount;
        }).collect(Collectors.toList());

        campaign.setCampaignDiscounts(discounts);
        Campaign createdCampaign = campaignRepository.save(campaign);
        notifyAdmin(createdCampaign);
        return createdCampaign;
    }

    public void updateCampaignStatus(Long campaignId, CampaignStatus status) {
        campaignRepository.updateCampaignStatus(campaignId, status);
    }

    private void notifyAdmin(Campaign campaign) {
        emailService.sendEmail(
                "admin@example.com",
                "New Campaign Created",
                "A new campaign titled " + campaign.getTitle() + " has been created with start date: " + campaign.getStartDate()
        );
    }



}
