package com.alexssource.fksis.analyse.data.linkedin.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.linkedin.service.FileHandler;

public class LinkedinFileHandler implements FileHandler {
	private final static Logger logger = LoggerFactory.getLogger(LinkedinFileHandler.class);

	@Override
	public List<String> readUrls(String path) {
		List<String> urls = null;
		Path hpath = new Path(path);
		FileSystem fs = null;
		Reader reader = null;
		try {
			fs = FileSystem.get(new Configuration());
			reader = new InputStreamReader(fs.open(hpath));
			urls = IOUtils.readLines(reader);
			
			if(logger.isDebugEnabled()) {
				for(String url : urls) {
					logger.debug("Read: {}", url);
				}
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ArrayList<String>();
		} finally {
			try {
				reader.close();
				//fs.close();
			} catch (IOException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		
		return urls;
	}

	@Override
	public void saveProfile(String filepath, String html) {
		Path path = new Path(filepath);
		OutputStream out = null;
		BufferedWriter writer = null;
		FileSystem fs = null;
		
		try {
			fs = FileSystem.get(new Configuration());
			if(fs.exists(path)) {
				fs.delete(path, true);
			}
			
			out = fs.create(path);
			writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
			writer.write(html);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return;
		} finally {
			try {
				writer.close();
				out.close();
				//fs.close();
			} catch (IOException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}
}
