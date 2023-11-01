package com.zerobase.dividends.web;

import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.constants.CacheKey;
import com.zerobase.dividends.persist.entity.CompanyEntity;
import com.zerobase.dividends.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/company")
@RequiredArgsConstructor
public class CompanyController {
	private final CompanyService companyService;
	private final CacheManager redisCacheManager;

	@GetMapping("/auto-complete")
	public ResponseEntity<List<String>> autoComplete(
		@RequestParam(name = "keyword") String keyword
	) {
//		List<String> result = this.companyService.autocomplete(keyword);
		List<String> result = this.companyService.getCompanyNamesByKeyword(
			keyword
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping
	@PreAuthorize("hasRole('READ')")
	public ResponseEntity<Page<CompanyEntity>> searchCompany(
		final Pageable pageable
	) {
		Page<CompanyEntity> companies = companyService.findAllCompany(pageable);
		return ResponseEntity.ok(companies);
	}

	/**
	 * 회사 배당금 정보 추가
	 */
	@PostMapping
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<CompanyDto> addCompany(
		@RequestBody CompanyDto request
	) {
		String ticker = request.getTicker().trim();
		if(ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		CompanyDto companyDto = this.companyService.save(ticker);
		this.companyService.addAutocompleteKeyword(companyDto.getName());
		return ResponseEntity.ok(companyDto);
	}

	@DeleteMapping("/{ticker}")
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> deleteCompany(
		@PathVariable String ticker
	) {
		String companyName = this.companyService.deleteCompany(ticker);

		clearFinanceCache(companyName);

		return ResponseEntity.ok().body(companyName);
	}

	private void clearFinanceCache(String companyName) {
		Objects.requireNonNull(this.redisCacheManager.getCache(CacheKey.KEY_FINANCE)).evict(companyName);
	}
}
