package com.alexssource.fksis.analyse.data.yahoo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWriterLocal implements YahooFileWriter {
	private final static Logger logger = LoggerFactory.getLogger(FileWriterLocal.class);
	private String filenameTemplate;

	public FileWriterLocal(String filenameTemplate) {
		this.filenameTemplate = filenameTemplate;
	}
	
	@Override
	public void writeToFile(int page, String content) {
		logger.info("Writing to the file....");
		String filename = String.format(filenameTemplate, page);
        
        FileWriter writer;
		try {
			writer = new FileWriter(new File(filename));
			writer.write(content);
	        writer.close();
	        
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
        
        logger.info("Done.");
	}
}
