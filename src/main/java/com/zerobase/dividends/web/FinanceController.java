package com.zerobase.dividends.web;

import com.zerobase.dividends.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/finance")
@RequiredArgsConstructor
public class FinanceController {
	private final FinanceService financeService;

	@GetMapping("/dividend/{companyName}")
	public ResponseEntity<?> searchFinance(
		@PathVariable(name = "companyName") String companyName
	) {
		return ResponseEntity.ok(
			financeService.findDividendByCompanyName(companyName)
		);
	}
}
