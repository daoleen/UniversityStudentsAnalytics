package com.alexssource.fksis.analyse.data.google.domain;


public class SearchResults {
    private ResponseData responseData;
    
    public SearchResults(){
    }
    
    public ResponseData getResponseData() { return responseData; }
    public void setResponseData(ResponseData responseData) { this.responseData = responseData; }
    
    public String toString() { return "ResponseData[" + responseData + "]"; }
}