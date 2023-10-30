package com.zerobase.dividends.scrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zerobase.dividends.model.CompanyDto;
import com.zerobase.dividends.model.DividendDto;
import com.zerobase.dividends.model.ScrapedResult;
import com.zerobase.dividends.model.constants.Month;

public class YahooFinancelScraper {
	private	static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
	private static final long START_TIME = 86400;

	public ScrapedResult scrap(CompanyDto company) {
		ScrapedResult scrapResult = new ScrapedResult();
		scrapResult.setCompany(company);
		long endTime = System.currentTimeMillis() / 1000;

		try {
			String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, endTime);
			Connection conn = Jsoup.connect(url);
			Document document = conn.get();

			Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
			Element tableEle = parsingDivs.get(0); // table 전체 
			
			Element tbody = tableEle.children().get(1);
			
			List<DividendDto> dividends = new ArrayList<>();
			for (Element e : tbody.children()) {
				String txt = e.text();
				if (!txt.endsWith("Dividend")) {
					continue;
				}
				
				String[] splits = txt.split(" ");
				int month = Month.strToNumber(splits[0]);
				int day = Integer.parseInt(splits[1].replace(",", ""));
				int year = Integer.parseInt(splits[2]);
				String dividend = splits[3];
				
				if (month < 0) {
					throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
				}
				
//				System.out.println(year + "/" + month + "/" + day + " -> " + dividend);

				dividends.add(DividendDto.builder()
						.date(LocalDateTime.of(year, month, day, 0, 0))
						.devidend(dividend)
						.build());
				
			}

			scrapResult.setDividendEntities(dividends);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return scrapResult;
	}
	
	public CompanyDto scrapCompanyByTicker(String ticker) {
		return null;
	}
}
