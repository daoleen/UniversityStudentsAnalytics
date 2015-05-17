package com.alexssource.fksis.analyse.data.urls.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueUrlsJob extends Configured implements Tool {
	private final static Logger logger = LoggerFactory.getLogger(UniqueUrlsJob.class);

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		new UniqueUrlsJob().run(args);
	}

	@Override
	public int run(String[] arg0) throws Exception {
		String[] args = new GenericOptionsParser(arg0).getRemainingArgs();
		Configuration conf = getConf();

		if (conf == null) {
			conf = new Configuration();
		}

		if (args.length < 2) {
			logger.error("Incorrect invocation of main() method");
			logger.error("Args: <input path> <output path>");
			System.exit(-1);
		}

		String inputPath = args[0];
		String outputPath = args[1];
		
		Path outputDir = new Path(outputPath);
		FileSystem hdfs = FileSystem.get(conf);
		if(hdfs.exists(outputDir)) {
			hdfs.delete(outputDir, true);
		}

		Job job = new Job(conf, "Unique profile urls");
		job.setJarByClass(UniqueUrlsJob.class);

		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputDir);
		
		job.setMapperClass(UniqueUrlsMapper.class);
		job.setReducerClass(UniqueUrlsReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		//job.setNumReduceTasks(10);

		return job.waitForCompletion(true) ? 0 : 1;
	}

}
