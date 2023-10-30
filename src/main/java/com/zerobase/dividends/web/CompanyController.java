package com.zerobase.dividends.web;

import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/company")
@RequiredArgsConstructor
public class CompanyController {
	private final CompanyService companyService;

	@GetMapping("/auto-complete")
	public ResponseEntity<?> autoComplete(
		@RequestParam(name = "keyword") String keyword
	) {
		return null;
	}
	
	@GetMapping
	public ResponseEntity<?> searchCompany() {
		return null;
	}

	@PostMapping
	public ResponseEntity<CompanyDto> addCompany(
		@RequestBody CompanyDto request
	) {
		String ticker = request.getTicker().trim();
		if(ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}
		CompanyDto companyDto = this.companyService.save(ticker);
		return ResponseEntity.ok(companyDto);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteCompany() {
		return null;
	}
}
