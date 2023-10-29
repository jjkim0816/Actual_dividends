package com.zerobase.dividends.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/finance")
public class FinanceController {
	@GetMapping("/dividend/{companyName}")
	public ResponseEntity<?> searchFinance(
		@PathVariable(name = "companyName") String companyName
	) {
		return null;
	}
}
