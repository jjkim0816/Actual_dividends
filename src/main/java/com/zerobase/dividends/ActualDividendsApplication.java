package com.zerobase.dividends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.ScrapedResult;
import com.zerobase.dividends.scrapper.YahooFinancelScraper;

//@SpringBootApplication
public class ActualDividendsApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ActualDividendsApplication.class, args);
		
		YahooFinancelScraper scraper = new YahooFinancelScraper();
		ScrapedResult result = scraper.scrap(CompanyDto.builder()
				.ticker("COKE")
				.build());
		
		System.out.println(result);
	}

}
