package com.tirth.salecampion.controller;

import com.tirth.salecampion.model.PriceHistory;
import com.tirth.salecampion.model.Product;
import com.tirth.salecampion.model.dtos.ProductDTO;
import com.tirth.salecampion.scheduler.CampaignScheduler;
import com.tirth.salecampion.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    public CampaignScheduler schedulerTasks;

    public final ProductService ps;

    ProductController( ProductService ps ) {
        this.ps = ps;
    }

    @PostMapping
    public void   saveAll(@RequestBody List<Product> products){
         ps.saveAll(products);
    }

    @GetMapping
    public Page<Product> getProducts(@RequestParam int page, @RequestParam int pageSize) {
        return ps.getAll(page-1, pageSize);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable long id) {
        return ps.findProductById(id);
    }

    @GetMapping("/history/{id}")
    public List<PriceHistory> getPriceHistory(@PathVariable int id) {
        return ps.getPriceHistory(id);
    }

    @PostMapping("/start")
    public void callSc(){
        schedulerTasks.processCampaigns();
    }


}
