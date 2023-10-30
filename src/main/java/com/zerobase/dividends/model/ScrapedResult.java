package com.zerobase.dividends.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScrapedResult {
	private CompanyDto company;
	private List<DividendDto> dividendEntities;

	public ScrapedResult() {
		this.dividendEntities = new ArrayList<>();
	}
}
