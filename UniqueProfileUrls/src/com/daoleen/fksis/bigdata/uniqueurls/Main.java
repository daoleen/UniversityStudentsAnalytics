package com.daoleen.fksis.bigdata.uniqueurls;

import com.alexssource.fksis.analyse.data.urls.mapreduce.UniqueUrlsJob;

public class Main {
	public static void main(String[] args) throws Exception {
		new UniqueUrlsJob().run(args);
	}
}
