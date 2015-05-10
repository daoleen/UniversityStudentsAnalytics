package com.alexssource.fksis.analyse.data.linkedin.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.linkedin.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.linkedin.service.FileHandler;
import com.alexssource.fksis.analyse.data.linkedin.service.UrlProfileCrawler;

public class LinkedinUrlProfileCrawler implements UrlProfileCrawler {
	private final static Logger logger = LoggerFactory.getLogger(LinkedinUrlProfileCrawler.class);
	
	@Override
	public void crawl(String inputFile, String outputDir) {
		FileHandler handler = new LinkedinFileHandler();
		List<String> urls = handler.readUrls(inputFile);
		
		for(int i = 0; i < urls.size(); i++) {
			String outputFile = String.format("%s/%d.html", outputDir, i);
			String html = getUrlContent(urls.get(i));
			handler.saveProfile(outputFile, html);
		}
	}

	private String getUrlContent(String url) {
		String content = null;
		
		try {
			URL purl = new URL(url);
			URLConnection connection;
			
			if(ProxyConfiguration.proxyEnabled) {
				Proxy proxy = new Proxy(Type.HTTP, 
						new InetSocketAddress(ProxyConfiguration.proxyHost, ProxyConfiguration.proxyPort)
				);
				connection = purl.openConnection(proxy);
			} else {
				connection = purl.openConnection();
			}
			
			connection.connect();
			//content = connection.getContent().toString();
			Reader reader = new InputStreamReader(connection.getInputStream());
			content = IOUtils.toString(reader);
		} catch (MalformedURLException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return content;
	}
}
