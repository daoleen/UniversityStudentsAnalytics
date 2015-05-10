package com.alexssource.fksis.analyse.data.google.impl;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.google.domain.SearchResults;
import com.alexssource.fksis.analyse.data.google.exception.ResultHandlerException;
import com.alexssource.fksis.analyse.data.google.exception.SearchCrawlerException;
import com.alexssource.fksis.analyse.data.google.service.ResultHandler;
import com.alexssource.fksis.analyse.data.google.service.SearchCrawler;
import com.alexssource.fksis.analyse.data.google.service.Searchable;


/**
 * Crawler by pages of Google with specified query
 * Supports continuous reading from terminated state
 * 
 * @author alexander.kozlov
 */
public class GoogleSearchCrawler implements SearchCrawler {
	private final static Logger logger = LoggerFactory.getLogger(GoogleSearchCrawler.class);
	private int position;
	private int page = 0;
	private Searchable searchable;
	private ResultHandler pageResultHandler;
	
	public GoogleSearchCrawler() {
		pageResultHandler = new FileResultHandler();
		position = pageResultHandler.readPosition();
		page = (position+1) / Searchable.resultSize;
		searchable = new GoogleSearch();
	}
	
	/* (non-Javadoc)
	 * @see com.alexssource.fksis.analyse.data.google.impl.SearchCrawler#crawl(java.lang.String)
	 */
	@Override
	public void crawl(String searchString) throws SearchCrawlerException {
		int resultsCount = -1;
		int pages = -1;
		
		do {
			try {
				Thread.sleep(10001L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.debug("Parsing page {} of {} [resultsCount: {}, position: {}]", 
					page, pages, resultsCount, position);
			SearchResults searchResults = searchable.search(searchString, position);
			if(resultsCount == -1) {
				if(searchResults.getResponseData() == null) {
					resultsCount = 0;
				} else {
					resultsCount = searchResults.getResponseData().getCursor().getResultCount();
				}
				pages = (int)Math.floor((double)resultsCount / Searchable.resultSize);
			}
			
			// saving the results to an external data warehouse
			try {
				pageResultHandler.handle(searchResults.getResponseData().getResults());
			} catch (ResultHandlerException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				savePosition();
				throw new SearchCrawlerException(e, position);
			} catch (NullPointerException ne) {
				logger.error("SearchResults: {}", searchResults);
				logger.error(ExceptionUtils.getStackTrace(ne));
				savePosition();
				throw new SearchCrawlerException(ne, position);
			}
			
			position += Searchable.resultSize;
			page++;
		} while(page < pages);
	}
	
	
	private void savePosition() {
		try {
			pageResultHandler.savePosition(position);
		} catch (ResultHandlerException positionSavingException) {
			logger.error(ExceptionUtils.getStackTrace(positionSavingException));
			logger.error("Sorry, the position of cursor couldn't be saved!");
		}
	}
}
