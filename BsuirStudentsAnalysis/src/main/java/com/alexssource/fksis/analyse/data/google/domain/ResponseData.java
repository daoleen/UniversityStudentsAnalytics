package com.alexssource.fksis.analyse.data.google.domain;

import java.util.List;

public class ResponseData {
    private List<Result> results;
    private Cursor cursor;
    
    public List<Result> getResults() { 
    	return results; 
    }
    
    public void setResults(List<Result> results) { 
    	this.results = results; 
    }
    
    public Cursor getCursor() {
		return cursor;
	}
	
    public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
	
	public String toString() { 
		return "Results[" + results + "]"; 
	}
}