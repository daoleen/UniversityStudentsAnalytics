package com.alexssource.fksis.analyse.data.linkedin.service;

import java.util.List;

public interface UrlProfileCrawler {
	public void crawl(String inputFile, String outputDir);
	public void crawl(List<String> urls, String outputDir, int datanode);
}
