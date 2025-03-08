package com.tirth.salecampion.scheduler;

import com.tirth.salecampion.model.Campaign;
import com.tirth.salecampion.repository.CampaignRepository;
import com.tirth.salecampion.service.EmailService;
import com.tirth.salecampion.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class CampaignScheduler {

    private final CampaignRepository campaignRepository;
    private final ProductService productService;
    private final EmailService emailService;

    @Autowired
    public CampaignScheduler(CampaignRepository campaignRepository, ProductService productService, EmailService emailService) {
        this.campaignRepository = campaignRepository;
        this.productService = productService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "${app.scheduler.campaign-update-cron}")
    public void processCampaigns() {
        log.info("Starting campaign processing");

        try {
            LocalDate today = LocalDate.now();

            // Process active campaigns
            List<Campaign> activeCampaigns = campaignRepository.findActiveCampaigns(today);
            productService.updateProductPrices(activeCampaigns);

            // Process expired campaigns
            List<Campaign> expiredCampaigns = campaignRepository.findExpiredCampaigns(today);
            productService.resetPricesForExpiredCampaigns(expiredCampaigns);

            log.info("Campaign processing completed: {} active, {} expired campaigns processed.",
                    activeCampaigns.size(), expiredCampaigns.size());
        } catch (Exception e) {
            log.error("Error during campaign processing", e);
            emailService.sendEmail("peojectManager@gmail.com", "Error during campaign processing", e.getMessage());
        }
    }
}
