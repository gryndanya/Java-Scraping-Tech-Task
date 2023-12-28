package com.example.javascrapingtechnicaltask.controller;

import com.example.javascrapingtechnicaltask.model.JobItem;
import com.example.javascrapingtechnicaltask.service.ScraperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ScraperController {

    private final ScraperService scraperService;

    public ScraperController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @GetMapping("/scrape/{jobFunction}")
    public ResponseEntity<List<JobItem>> scrapeJobs(@PathVariable String jobFunction) throws IOException {
        List<JobItem> JobItemList = scraperService.scrapeJobs(jobFunction);
        return new ResponseEntity<>(JobItemList, HttpStatus.OK);
    }
}
