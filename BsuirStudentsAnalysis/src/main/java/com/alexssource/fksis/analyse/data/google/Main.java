package com.alexssource.fksis.analyse.data.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.alexssource.fksis.analyse.data.google.domain.ResponseData;
import com.alexssource.fksis.analyse.data.google.domain.Result;
import com.alexssource.fksis.analyse.data.google.domain.SearchResults;
import com.alexssource.fksis.analyse.data.google.exception.ResultHandlerException;
import com.alexssource.fksis.analyse.data.google.exception.SearchCrawlerException;
import com.alexssource.fksis.analyse.data.google.impl.FileResultHandler;
import com.alexssource.fksis.analyse.data.google.impl.GoogleSearch;
import com.alexssource.fksis.analyse.data.google.impl.GoogleSearchCrawler;
import com.alexssource.fksis.analyse.data.google.service.ResultHandler;
import com.alexssource.fksis.analyse.data.google.service.SearchCrawler;
import com.alexssource.fksis.analyse.data.google.service.Searchable;

public class Main {
	public static void main(String[] args) throws ResultHandlerException, FileNotFoundException {
		//readFromGoogleTest();
		//resultHandlerTest();
		//readFromGoogleProduction();
		getSearchString();
	}
	
	static void readFromGoogleProduction(String searchString) {
		//String searchString = "belarusian state university of informatics and radioelectronics site:linkedin.com/in/";
		SearchCrawler crawler = new GoogleSearchCrawler();
		try {
			crawler.crawl(searchString);
		} catch (SearchCrawlerException e) {
			System.out.println("An exceptions were occured while crawler worked.");
		}
	}
	
	static void readFromGoogleTest() {
		String searchString = "belarusian state university of informatics and radioelectronics site:linkedin.com/in/";
		Searchable searchable = new GoogleSearch();
		SearchResults results = searchable.search(searchString, 0);
		
		ResponseData response = results.getResponseData();
		List<Result> responseResults = response.getResults();		
		System.out.println(String.format("Total number of results: %d", response.getCursor().getResultCount()));
		System.out.println(String.format("Found %d results in this page", responseResults.size()));
		
		for(Result res : responseResults) {
			System.out.println(String.format("%s -> %s", res.getTitle(), res.getUrl()));
		}
	}
	
	static void resultHandlerTest() throws ResultHandlerException {
		ResultHandler handler = new FileResultHandler();
		System.out.println("Start position: " + handler.readPosition());
		
		List<Result> results = new ArrayList<Result>(3);
		results.add(new Result("http://user1/", "user1"));
		results.add(new Result("http://user2/", "user2"));
		results.add(new Result("http://user3/", "user3"));
		handler.handle(results);
		
		int position = 11;
		handler.savePosition(position);
	}
	
	
	static void getSearchString() throws FileNotFoundException {
		String template = "%s belarusian state university of informatics and radioelectronics site:linkedin.com/in/";
		InputStream namesStream = new FileInputStream("P:\\Data\\BsuirStudentData\\GoogleSearchCrawler\\UniqueRussianNames.txt");
		Scanner scanner = new Scanner(namesStream);
		
		while(scanner.hasNext()) {
			String str = scanner.next();
			str = str.substring(1, str.length()-1);
			String query = String.format(template, str);
			System.out.println(str);
			readFromGoogleProduction(query);
			
			try {
				Thread.sleep(20001L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println();
		System.out.println("Done.");
	}
}
