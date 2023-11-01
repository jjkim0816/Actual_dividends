package com.zerobase.dividends.scheduler;

import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.ScrapedResult;
import com.zerobase.dividends.model.constants.CacheKey;
import com.zerobase.dividends.persist.CompanyRepository;
import com.zerobase.dividends.persist.DividendRepository;
import com.zerobase.dividends.persist.entity.CompanyEntity;
import com.zerobase.dividends.persist.entity.DividendEntity;
import com.zerobase.dividends.scrapper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableCaching
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

//    @Scheduled(fixedDelay = 1000)
//    public void test1() throws InterruptedException {
//        Thread.sleep(10000);
//        System.out.println(Thread.currentThread().getName() + " -> 테스트 1 :" +
//            LocalDateTime.now());
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    public void test2() throws InterruptedException {
//        System.out.println(Thread.currentThread().getName() + " -> 테스트 2 :" +
//            LocalDateTime.now());
//    }

    // 일정 주기마다 수행
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("start scraping");
        // 저장된 회사 목록 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity company : companies) {
            log.info("Scraping scheduler is started -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                CompanyDto.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());

            scrapedResult.getDividends().stream()
                .map(e -> DividendEntity.builder()
                    .companyId(company.getId())
                    .date(e.getDate())
                    .dividend(e.getDividend())
                    .build())
                .forEach(e -> {
                    boolean exists = this.dividendRepository
                        .existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                    // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
                    if (!exists) {
                        this.dividendRepository.save(e);
                    }
                });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정리
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
