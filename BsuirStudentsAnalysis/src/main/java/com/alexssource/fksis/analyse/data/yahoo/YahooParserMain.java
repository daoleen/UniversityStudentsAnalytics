package com.alexssource.fksis.analyse.data.yahoo;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YahooParserMain {
	private final static Logger logger = LoggerFactory.getLogger(YahooParserMain.class);
	private final static String serviceUrl = "https://search.yahoo.com/search";
	private String searchString;
	private YahooFileWriter filewriter;
	private YahooCrawler crawler = new YahooCrawler();
	
	
	public YahooParserMain(YahooFileWriter filewriter, String queryString) {
		this.filewriter = filewriter;
		this.searchString = queryString;
	}
	
	
	public void parse(int pages) {
		readFromYahoo(pages);
	}
	
	public void parse() {
		readFromYahoo();
	}
	
	private void readFromYahoo(int pages) {
		for(int i = 1; i <= pages; i++) {
			String content = crawler.getPageContent(serviceUrl, searchString, i);
			filewriter.writeToFile(i, content);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
	}
	
	private void readFromYahoo() {
		// <div class="compTitle">
		String foundFlag = "<div class=\"compTitle\">";
		
		for(int i = 1; ; i++) {
			String content = crawler.getPageContent(serviceUrl, searchString, i);
			
			if(!content.contains(foundFlag)) {
				return;
			}
			
			filewriter.writeToFile(i, content);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
	}
}
