package com.alexssource.fksis.analyse.data.yahoo;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWriterHdfs implements YahooFileWriter {
	private final static Logger logger = LoggerFactory.getLogger(FileWriterHdfs.class);
	private String filenameTemplate;
	
	
	public FileWriterHdfs(String filenameTemplate) {
		this.filenameTemplate = filenameTemplate;
	}
	
	
	@Override
	public void writeToFile(int page, String content) {
		logger.info("Writing to the file....");
		String filename = String.format(filenameTemplate, page);
		
		try {
			Configuration configuration = new Configuration();
			FileSystem hdfs = FileSystem.get(configuration );
			Path file = new Path(filename);
			if ( hdfs.exists( file )) { hdfs.delete( file, true ); } 
			OutputStream os = hdfs.create( file,
			    new Progressable() {
			        public void progress() {
			        } });
			BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
			br.write(content);
			br.close();
			//hdfs.close();
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		logger.info("Done.");
	}

}
