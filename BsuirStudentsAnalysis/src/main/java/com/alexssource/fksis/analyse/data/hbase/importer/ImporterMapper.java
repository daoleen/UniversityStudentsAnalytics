package com.alexssource.fksis.analyse.data.hbase.importer;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImporterMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
	private final static Logger logger = LoggerFactory.getLogger(ImporterMapper.class);
	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException
	{
		logger.debug("Key is: {}", key);
		context.write(key, value);
	}
}
