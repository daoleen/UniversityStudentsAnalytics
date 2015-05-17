package com.alexssource.fksis.analyse.data.urls.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueUrlsReducer extends Reducer<Text, NullWritable, NullWritable, Text> {
	private final static Logger logger = LoggerFactory.getLogger(UniqueUrlsReducer.class);

	@Override
	protected void reduce(Text key, Iterable<NullWritable> nulls, Context context)
			throws IOException, InterruptedException 
	{
		logger.info("Run UniqueUrlsReducer reduce()");
		logger.info("Key: {}", key);
		context.write(NullWritable.get(), key);
	}
}
