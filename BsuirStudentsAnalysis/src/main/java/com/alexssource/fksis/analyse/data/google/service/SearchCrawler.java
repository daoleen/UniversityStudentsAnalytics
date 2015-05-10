package com.alexssource.fksis.analyse.data.google.service;

import com.alexssource.fksis.analyse.data.google.exception.SearchCrawlerException;

public interface SearchCrawler {

	public abstract void crawl(String searchString)
			throws SearchCrawlerException;

}