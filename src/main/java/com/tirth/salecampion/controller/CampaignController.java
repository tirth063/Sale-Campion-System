package com.tirth.salecampion.controller;

import com.tirth.salecampion.model.Campaign;
import com.tirth.salecampion.model.dtos.CampaignCreateDTO;
import com.tirth.salecampion.model.CampaignStatus;
import com.tirth.salecampion.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping("/active")
    public ResponseEntity<List<Campaign>> getActiveCampaigns() {
        return ResponseEntity.ok(campaignService.getActiveCampaigns());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Campaign>> getExpiredCampaigns() {
        return ResponseEntity.ok(campaignService.getExpiredCampaigns());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Campaign>> getUpcomingCampaigns() {
        return ResponseEntity.ok(campaignService.getUpcomingCampaigns());
    }

    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@Valid @RequestBody CampaignCreateDTO campaignCreateDTO) {
        Campaign created = campaignService.createCampaign(campaignCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateCampaignStatus(@PathVariable Long id, @RequestParam CampaignStatus status) {
        campaignService.updateCampaignStatus(id, status);
        return ResponseEntity.ok("Campaign status updated successfully");
    }
}
