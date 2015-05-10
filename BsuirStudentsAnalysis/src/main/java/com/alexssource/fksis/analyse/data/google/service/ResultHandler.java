package com.alexssource.fksis.analyse.data.google.service;

import java.util.List;

import com.alexssource.fksis.analyse.data.google.domain.Result;
import com.alexssource.fksis.analyse.data.google.exception.ResultHandlerException;

public interface ResultHandler {
	public void handle(List<Result> data) throws ResultHandlerException;
	public void savePosition(int position) throws ResultHandlerException;
	public int readPosition();
}
