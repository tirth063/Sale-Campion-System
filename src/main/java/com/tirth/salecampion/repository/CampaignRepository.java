package com.tirth.salecampion.repository;

import com.tirth.salecampion.model.Campaign;
import com.tirth.salecampion.model.CampaignStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    @Query("SELECT c FROM Campaign c WHERE c.startDate <= :currentDate AND c.endDate >= :currentDate")
    List<Campaign> findActiveCampaigns(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT c FROM Campaign c WHERE c.endDate < :currentDate")
    List<Campaign> findExpiredCampaigns(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT c FROM Campaign c WHERE c.startDate > :date")
    List<Campaign> findUpcomingCampaigns(@Param("date") LocalDate date);

    @Query("SELECT c FROM Campaign c WHERE c.startDate <= :endDate AND c.endDate >= :startDate")
    List<Campaign> findActiveCampaignsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Campaign c SET c.status = :status WHERE c.id = :campaignId")
    void updateCampaignStatus(@Param("campaignId") Long campaignId, @Param("status") CampaignStatus status);
}

