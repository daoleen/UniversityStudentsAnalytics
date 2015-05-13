package com.alexssource.fksis.analyse.data.linkedin.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkedinJob extends Configured implements Tool {
	private final static Logger logger = LoggerFactory.getLogger(LinkedinJob.class);
	
	@Override
	public int run(String[] arg0) throws Exception {
		String[] args = new GenericOptionsParser(arg0).getRemainingArgs();
		Configuration conf = getConf();

		if (conf == null) {
			conf = new Configuration();
		}

		if (args.length < 3) {
			logger.error("Incorrect invocation of main() method");
			System.out.println("Args: <input file> <output path> <output log path>[ <proxy host> <proxy port>]");
			System.exit(-1);
		}

		String urlFile = args[0];
		String outputFolder = args[1];
		String outputLogFolder = args[2];
		conf.set("outputFolder", outputFolder);

		if (args.length == 5) {
			conf.setBoolean("proxyEnabled", true);
			conf.set("proxyHost", args[3]);
			conf.setInt("proxyPort", Integer.parseInt(args[4]));
		}

		FileSystem hdfs = FileSystem.get(conf);
		Path outputLogDir = new Path(outputLogFolder);
		if ( hdfs.exists( outputLogDir )) { 
			hdfs.delete( outputLogDir, true ); 
		} 
		
		Path outputDir = new Path(outputFolder);
		if(hdfs.exists(outputDir)) {
			hdfs.delete(outputDir, true);
		}

		Job job = new Job(conf, "Read linkedin profiles by URLs file");
		job.setJarByClass(LinkedinJob.class);

		FileInputFormat.setInputPaths(job, urlFile);
		FileOutputFormat.setOutputPath(job, outputLogDir);

		job.setMapperClass(LinkedinMapper.class);
		job.setReducerClass(LinkedinReducer.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(NullWritable.class);
		job.setNumReduceTasks(10);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	
	public static void main(String[] args) throws Exception {
		new LinkedinJob().run(args);
	}
}
