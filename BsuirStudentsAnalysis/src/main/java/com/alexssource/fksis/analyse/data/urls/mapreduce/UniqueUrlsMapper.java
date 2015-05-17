package com.alexssource.fksis.analyse.data.urls.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueUrlsMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
	private final static Logger logger = LoggerFactory.getLogger(UniqueUrlsMapper.class);

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException 
	{
		logger.debug("Run UniqueUrlsMapper map()");
		logger.debug("key (names file id): {}", key);
		logger.debug("value (names): {}", value);
		context.write(value, NullWritable.get());
	}
}
