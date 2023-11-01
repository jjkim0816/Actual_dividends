package com.zerobase.dividends.service;

import com.zerobase.dividends.exception.impl.NoCompanyException;
import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.ScrapedResult;
import com.zerobase.dividends.persist.CompanyRepository;
import com.zerobase.dividends.persist.DividendRepository;
import com.zerobase.dividends.persist.entity.CompanyEntity;
import com.zerobase.dividends.persist.entity.DividendEntity;
import com.zerobase.dividends.scrapper.Scraper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final Trie<String, String> trie;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    public CompanyDto save(String ticker) {
        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }

        return storeCompanyAndDividend(ticker);
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        // ticker 를 기준으로 회사를 스크래핑
        CompanyDto companyDto = yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(companyDto)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        // 해당 회사가 존재할 경우, 회사 배당금 정보를 스크래핑 한다.
        ScrapedResult result = yahooFinanceScraper.scrap(companyDto);

        // 스크래핑 결과
        CompanyEntity companyEntity = companyRepository.save(CompanyEntity
                        .builder()
                        .ticker(companyDto.getTicker())
                        .name(companyDto.getName())
                        .build());

        List<DividendEntity> dividendEntityList = result.getDividends().stream()
                .map(e -> DividendEntity.builder()
                        .companyId(companyEntity.getId())
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build())
                .collect(Collectors.toList());

        this.dividendRepository.saveAll(dividendEntityList);

        return companyDto;
    }

    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<CompanyEntity> companies = this.companyRepository
                .findByNameStartingWithIgnoreCase(keyword, limit);
        return companies.stream().map(CompanyEntity::getName)
                .collect(Collectors.toList());
    }

    public Page<CompanyEntity> findAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> autocomplete(String keyword) {
        return this.trie.prefixMap(keyword).keySet()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    public String deleteCompany(String ticker) {
        CompanyEntity company = this.companyRepository.findByTicker(ticker)
            .orElseThrow(NoCompanyException::new);

        this.dividendRepository.deleteAllByCompanyId(company.getId());
        this.companyRepository.delete(company);

        this.deleteAutocompleteKeyword(company.getName());

        return company.getName();
    }
}
