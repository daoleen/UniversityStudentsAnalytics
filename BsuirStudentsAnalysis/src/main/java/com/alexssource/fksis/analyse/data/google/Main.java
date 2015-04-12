package com.alexssource.fksis.analyse.data.google;

import java.util.List;

import com.alexssource.fksis.analyse.data.google.SearchResults.ResponseData;
import com.alexssource.fksis.analyse.data.google.SearchResults.Result;

public class Main {
	public static void main(String[] args) {
		String searchString = "кенгуру";
		Searchable searchable = new GoogleSearch();
		SearchResults results = searchable.search(searchString);
		
		ResponseData response = results.getResponseData();
		List<Result> responseResults = response.getResults();		
		System.out.println(String.format("Found %d results", responseResults.size()));
		
		for(Result res : responseResults) {
			System.out.println(String.format("%s:%s", res.getTitle(), res.getUrl()));
		}
	}
}
