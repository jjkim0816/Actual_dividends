package com.zerobase.dividends.scrapper;

import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.ScrapedResult;

public interface Scraper {
    CompanyDto scrapCompanyByTicker(String ticker);

    ScrapedResult scrap(CompanyDto companyDto);
}
