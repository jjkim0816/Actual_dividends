package com.zerobase.dividends.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {
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
	public ResponseEntity<?> addCompany() {
		return null;
	}

	@DeleteMapping
	public ResponseEntity<?> deleteCompany() {
		return null;
	}
}
