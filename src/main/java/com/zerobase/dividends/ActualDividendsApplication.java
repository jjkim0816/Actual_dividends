package com.zerobase.dividends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ActualDividendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActualDividendsApplication.class, args);
		
//		Scraper scraper = new YahooFinanceScraper();
//		ScrapedResult result = scraper.scrap(CompanyDto.builder()
//				.ticker("COKE")
//				.build());
//
//		CompanyDto result = scraper.scrapCompanyByTicker("COKE");
//
//		System.out.println(result);
	}

}
