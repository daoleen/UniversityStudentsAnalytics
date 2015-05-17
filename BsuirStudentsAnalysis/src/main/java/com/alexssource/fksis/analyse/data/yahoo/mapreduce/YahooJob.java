package com.alexssource.fksis.analyse.data.yahoo.mapreduce;

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

public class YahooJob extends Configured implements Tool {
	private final static Logger logger = LoggerFactory
			.getLogger(YahooJob.class);
//	private final static String bsuirQuery = "%s \"belarusian state university of informatics and radioelectronics\" site:linkedin.com/in/";
//	private final static String bsuQuery = "%s \"belarusian state university\" site:linkedin.com/in/";

	@Override
	public int run(String[] arg0) throws Exception {
		String[] args = new GenericOptionsParser(arg0).getRemainingArgs();
		Configuration conf = getConf();

		if (conf == null) {
			conf = new Configuration();
		}

		if (args.length < 3) {
			logger.error("Incorrect invocation of main() method");
			logger.error("Args: <RussianNames filepath> <{bsuir|bsu}> <output path> "
					+ "<output log path>[ <proxy host> <proxy port>]");
			
			logger.error("Args: <Query filepath> <output path> <output log path> "
					+"[ <proxy host> <proxy port>]");
			
			System.exit(-1);
		}

//		String namesFile = args[0];
//		String universityName = args[1];
//		String folder = args[2];
//		String outputLogPath = args[3];
//		String template = folder + "/yahoo-%d_%s-%s.html";

		String queryFile = args[0];
		String folder = args[1];
		String outputLogPath = args[2];
		String template = folder + "/yahoo-%d_%s-%s.html";
		
		if (args.length == 5) {
			conf.setBoolean("proxyEnabled", true);
			conf.set("proxyHost", args[3]);
			conf.setInt("proxyPort", Integer.parseInt(args[4]));
		}

		conf.set("outputFileTemplate", template);
		logger.debug("The outputFileTemplate template has been set to configuration: {}", template);
		//conf.set("searchQuery", getSearchQueryByUniversityName(universityName));
		conf.set("outputFolder", folder);

		FileSystem hdfs = FileSystem.get(conf);
		Path outputLogDir = new Path(outputLogPath);
		if ( hdfs.exists( outputLogDir )) { 
			hdfs.delete( outputLogDir, true ); 
		} 
		
		Path outputDir = new Path(folder);
		if(hdfs.exists(outputDir)) {
			hdfs.delete(outputDir, true);
		}

		Job job = new Job(conf, "Read yahoo pages by students names");
		job.setJarByClass(YahooJob.class);

		FileInputFormat.setInputPaths(job, queryFile);
		FileOutputFormat.setOutputPath(job, new Path(outputLogPath));

		job.setMapperClass(YahooMapper.class);
		job.setReducerClass(YahooReducer.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(NullWritable.class);
		job.setNumReduceTasks(10);

		return job.waitForCompletion(true) ? 0 : 1;
	}

//	public String getSearchQueryByUniversityName(String universityName) {
//		if (universityName.equalsIgnoreCase("bsuir")) {
//			return bsuirQuery;
//		}
//		if (universityName.equalsIgnoreCase("bsu")) {
//			return bsuQuery;
//		}
//		return null;
//	}

	public static void main(String[] args) throws Exception {
		new YahooJob().run(args);
	}
}
