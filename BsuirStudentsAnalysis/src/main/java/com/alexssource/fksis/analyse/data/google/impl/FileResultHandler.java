package com.alexssource.fksis.analyse.data.google.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.google.domain.Result;
import com.alexssource.fksis.analyse.data.google.exception.ResultHandlerException;
import com.alexssource.fksis.analyse.data.google.service.ResultHandler;

public class FileResultHandler implements ResultHandler {
	private final static Logger logger = LoggerFactory.getLogger(FileResultHandler.class);
	private final static String resultFile = "P:\\Data\\BsuirStudentData\\GoogleSearchCrawler\\UserURLs.txt";
	private final static String positionFile = "P:\\Data\\BsuirStudentData\\GoogleSearchCrawler\\position.txt";
	
	
	@Override
	public void handle(List<Result> data) throws ResultHandlerException {
		File file = new File(resultFile);
		Writer writer = null;
		try {
			writer = new FileWriter(file, true);
			
			for(Result userUrl : data) {
				writer.append(String.format("%s%n", userUrl.getUrl()));
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				logger.warn(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	@Override
	public void savePosition(int position) throws ResultHandlerException {
		File file = new File(positionFile);
		Writer writer = null;
		try {
			writer = new FileWriter(file, false);
			writer.write(String.valueOf(position));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				logger.warn(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	@Override
	public int readPosition() {
		File file = new File(positionFile);
		FileReader reader = null;
		int position = 0;
		
		if(!file.exists()) {
			return 0;
		}
		
		try {
			reader = new FileReader(file);
			position = Integer.parseInt(IOUtils.toString(reader));
		} catch (FileNotFoundException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		finally {
			try {
				reader.close();
			} catch (Exception e) {
				logger.warn(ExceptionUtils.getStackTrace(e));
			}
		}
		
		return position;
	}
}
