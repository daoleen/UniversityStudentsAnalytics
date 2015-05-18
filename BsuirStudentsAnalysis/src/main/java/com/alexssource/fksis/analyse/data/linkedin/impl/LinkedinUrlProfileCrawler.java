package com.alexssource.fksis.analyse.data.linkedin.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URLEncoder;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.linkedin.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.linkedin.service.FileHandler;
import com.alexssource.fksis.analyse.data.linkedin.service.UrlProfileCrawler;

public class LinkedinUrlProfileCrawler implements UrlProfileCrawler {
	private final static Logger logger = LoggerFactory.getLogger(LinkedinUrlProfileCrawler.class);
	private FileHandler handler = new LinkedinFileHandler();
	
	@Override
	public void crawl(String inputFile, String outputDir) {
		List<String> urls = handler.readUrls(inputFile);
		String filenameTemplate = outputDir + "/%d.html"; 
		readUrls(urls, filenameTemplate);
	}
	
	@Override
	public void crawl(List<String> urls, String outputDir, int datanode) {
		String filenameTemplate = outputDir + "/" + datanode + "-%d.html";
		
		
//		// HARDCODED LOG EXCEPTION
//		String _log_urls = "";
//		for (String string : urls) {
//			_log_urls += string + " \n";
//		}
//		String _log = String.format("---------- MY ALEXSSOURCE LOG:\nURLS:\n%sOutputDir:%s\nFileTemplate: %s", 
//				_log_urls, outputDir, filenameTemplate);
//		throw new RuntimeException(_log);
//		
		
		
		readUrls(urls, filenameTemplate);
	}

	private String getUrlContent(String url) {
		String content = null;
		
		try {
			url = getEncodedUrl(url);
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
			
//			// TODO: hardcoded
//			Proxy proxy = new Proxy(Type.HTTP, 
//					new InetSocketAddress("proxy.fksis.local", 8080)
//			);
//			connection = purl.openConnection(proxy);
			
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
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
	
	
	private void readUrls(List<String> urls, String filenameTemplate) {
		for(int i = 0; i < urls.size(); i++) {
			String outputFile = String.format(filenameTemplate, i);
			String html = getUrlContent(urls.get(i));
			handler.saveProfile(outputFile, html);
		}
	}
	
	
	private String getEncodedUrl(String url) {
		Pattern russianPattern = Pattern.compile("[à-ÿÀ-ß][à-ÿÀ-ß\\-0-9]+");
		Matcher matcher = russianPattern.matcher(url);
		while (matcher.find()) {
			String match = matcher.group();
			try {
				url = url.replace(match, URLEncoder.encode(match, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return url;
	}
}
