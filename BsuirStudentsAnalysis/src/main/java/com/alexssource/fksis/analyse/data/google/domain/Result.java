package com.alexssource.fksis.analyse.data.google.domain;

public class Result {
    private String url;
    private String title;
    
    public Result() {
    }
    
    public Result(String url, String title) {
    	this.url = url;
    	this.title = title;
    }
    
    public String getUrl() { return url; }
    public String getTitle() { return title; }
    public void setUrl(String url) { this.url = url; }
    public void setTitle(String title) { this.title = title; }
    public String toString() { return "Result[url:" + url +",title:" + title + "]"; }
}
