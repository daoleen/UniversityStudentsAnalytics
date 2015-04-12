package com.alexssource.fksis.analyse.data.google;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class GoogleSearch implements Searchable {
	private final static Logger logger = LoggerFactory.getLogger(GoogleSearch.class);
	private final static String googleServiceUrl = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
	private final static String charset = "UTF-8";
	private final static int resultSize = 2;
	private int start = 0;	// номер записи, с которой начинать (записи, не страницы)
	
	public GoogleSearch(){
	}

	@Override
	public SearchResults search(String searchString) {
		Reader reader;
		SearchResults results = null;
		
		try {
			String apiUrl = String.format("%s%s&start=%d&rsz=%d", googleServiceUrl, 
					URLEncoder.encode(searchString, charset), start, resultSize);
			URL url = new URL(apiUrl);
			reader = new InputStreamReader(url.openStream(), charset);
			results = new Gson().fromJson(reader, SearchResults.class);
		} catch(MalformedURLException me) {
			logger.error(ExceptionUtils.getFullStackTrace(me));
		} catch(UnsupportedEncodingException ue) {
			logger.error(ExceptionUtils.getFullStackTrace(ue));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return results;
	}
}
