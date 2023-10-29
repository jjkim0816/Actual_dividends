package com.zerobase.dividends;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class ActualDividendsApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ActualDividendsApplication.class, args);
		
		String url = "https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1698537600&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true";
		Connection conn = Jsoup.connect(url);

		try {
			Document document = conn.get();
			Elements eles = document.getElementsByAttributeValue("data-test", "historical-prices");
			Element ele = eles.get(0); // table 전체 
//			System.out.println(ele);
			
			Element tbody = ele.children().get(1);
			
			for (Element e : tbody.children()) {
				String txt = e.text();
				if (!txt.endsWith("Dividend")) {
					continue;
				}
				
//				System.out.println(txt);
				
				String[] splits = txt.split(" ");
				String month = splits[0];
				String day = splits[1].replace(",", "");
				String year = splits[2];
				String dividend = splits[3];
				
				System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
