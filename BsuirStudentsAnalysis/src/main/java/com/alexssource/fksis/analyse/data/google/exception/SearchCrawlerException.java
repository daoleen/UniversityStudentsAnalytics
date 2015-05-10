package com.alexssource.fksis.analyse.data.google.exception;

public class SearchCrawlerException extends Exception {
	private static final long serialVersionUID = -1068047157300186617L;
	private int position;
	
	public SearchCrawlerException(Throwable e, int position) {
		super(e);
		setPosition(position);
	}
	
	public SearchCrawlerException(String message, int position) {
		super(message);
		setPosition(position);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
