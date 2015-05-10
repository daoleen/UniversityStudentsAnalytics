package com.alexssource.fksis.analyse.data.google.service;

import com.alexssource.fksis.analyse.data.google.domain.SearchResults;


public interface Searchable {
	public final static int resultSize = 8;
	public SearchResults search(String searchString, int start);
}
