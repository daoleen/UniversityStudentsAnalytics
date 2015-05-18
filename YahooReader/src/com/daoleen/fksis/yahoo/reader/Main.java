package com.daoleen.fksis.yahoo.reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alexssource.fksis.analyse.data.yahoo.FileWriterHdfs;
import com.alexssource.fksis.analyse.data.yahoo.ProxyConfiguration;
import com.alexssource.fksis.analyse.data.yahoo.YahooFileWriter;
import com.alexssource.fksis.analyse.data.yahoo.YahooParserMain;

public class Main {
private final static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		// https%3a%2f%2fwww.linkedin.com%2fpub%2fekaterina-maliarevich%2f64%2f552%2fb73/RK=0/RS=LR9SP4g5vQt9AwwNPflJGMc_Q04-
		String url;
		try {
//			url = URLDecoder.decode("https%3a%2f%2fwww.linkedin.com%2fpub%2f%25D1%2582%25D0%25B0%25D1%2582%25D1%258C%25D1%258F%25D0%25BD%25D0%25B0-%25D0%25B3%25D0%25BB%25D0%25B0%25D0%25B4%25D1%2587%25D0%25B5%25D0%25BD%25D0%25BA%25D0%25BE%2fb8%2f65%2f351", "UTF-8");
//			System.out.println(url);
//			
//			String encodedUrl = URLEncoder.encode("павел-пудов", "UTF-8");
//			System.out.println(encodedUrl);
			
			
			Proxy proxy = new Proxy(Type.HTTP, 
			new InetSocketAddress("proxy.fksis.local", 8080)
	);
			
			String rusurl = "https://www.linkedin.com/pub/павел-пудов/63/658/89";
			Pattern russianPattern = Pattern.compile("[а-яА-Я][а-яА-Я\\-0-9]+");
//			String russianPattern = "[а-яА-Я\\-0-9]+";

			Matcher matcher = russianPattern.matcher(rusurl);
			while (matcher.find()) {
//			      System.out.print("Start index: " + matcher.start());
//			      System.out.print(" End index: " + matcher.end() + " ");
//			      System.out.println(matcher.group());
				String match = matcher.group();
				rusurl = rusurl.replace(match, URLEncoder.encode(match, "UTF-8"));
			}
			
			System.out.println("URL: " + rusurl);
			
			URL purl = new URL(rusurl);
			URLConnection connection;
	connection = purl.openConnection(proxy);

	connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
	
	connection.connect();
//	content = connection.getContent().toString();
	Reader reader = new InputStreamReader(connection.getInputStream());
	String content = IOUtils.toString(reader);
			
	
	System.out.println("Content: " + content);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(args.length < 2) {
			logger.error("Incorrect invocation of Main function");
			logger.error("You need to type 2 or 4 parameters: folder where save the yahoo content and yahoo pages count to parse; and proxy host and port");
			System.exit(-1);
		}
		
		String folder = args[0];
		int pages = Integer.parseInt(args[1]);
		String template = folder + "/yahoo-%d.html";
		
		if(args.length == 4) {
			ProxyConfiguration.proxyEnabled = true;
			ProxyConfiguration.proxyHost = args[2];
			ProxyConfiguration.proxyPort = Integer.parseInt(args[3]);
		}
		
		// TODO: replace to hdfs before deployment
		YahooFileWriter filewriter = new FileWriterHdfs(template);
		YahooParserMain yahooParser = new YahooParserMain(filewriter,
			"\"belarusian state university of informatics and radioelectronics\" site:linkedin.com/in/"
		);
		yahooParser.parse(pages);
	}
}
