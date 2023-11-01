package com.zerobase.dividends.service;

import com.zerobase.dividends.exception.impl.NoCompanyException;
import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.DividendDto;
import com.zerobase.dividends.model.ScrapedResult;
import com.zerobase.dividends.model.constants.CacheKey;
import com.zerobase.dividends.persist.CompanyRepository;
import com.zerobase.dividends.persist.DividendRepository;
import com.zerobase.dividends.persist.entity.CompanyEntity;
import com.zerobase.dividends.persist.entity.DividendEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 요청이 자주 들어오는가?
    // 자주 변경되는 데이터 인가?
    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult findDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
            .orElseThrow(NoCompanyException::new);

        // 2. 조회 된 회사 ID로 배당금을 조회 한다.
        List<DividendEntity> dividendEntities = this.dividendRepository
                .findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<DividendDto> dividendDtos = dividendEntities.stream()
                .map(e -> DividendDto.builder()
                        .date(e.getDate())
                        .dividend(e.getDividend())
                        .build())
                .collect(Collectors.toList());

       return ScrapedResult.builder()
                .company(CompanyDto.builder()
                        .ticker(company.getTicker())
                        .name(company.getName())
                        .build())
                .dividends(dividendDtos)
                .build();
    }
}
