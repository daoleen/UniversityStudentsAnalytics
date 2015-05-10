package com.alexssource.fksis.analyse.data.yahoo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.net.Proxy.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YahooCrawler {
	private static final Logger logger = LoggerFactory.getLogger(YahooCrawler.class);
	private final static String charset = "UTF-8";
	
	public String getPageContent(String serviceUrl, String searchString, int page) {
		String pageContent = "";
		int start = (page-1)*10+1;
		
		try {
			String apiUrl = String.format("%s?p=%s&b=%d", serviceUrl, 
					URLEncoder.encode(searchString, charset), start);
		
			SSLContext ctx = SSLContext.getInstance("TLS");
	        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
	        SSLContext.setDefault(ctx);
			
			logger.debug("Request query: {}", apiUrl);
			URL url = new URL(apiUrl);
			HttpsURLConnection conn;
			if(ProxyConfiguration.proxyEnabled) {
				Proxy proxy = new Proxy(Type.HTTP, 
						new InetSocketAddress(
								ProxyConfiguration.proxyHost, 
								ProxyConfiguration.proxyPort
						)
				);
				conn = (HttpsURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpsURLConnection) url.openConnection();
			}
				
	        conn.setHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String arg0, SSLSession arg1) {
	                return true;
	            }
	        });
	        
	        logger.info("Response code: {}", conn.getResponseCode());
	        InputStream is = (InputStream)conn.getContent();
	        pageContent = IOUtils.toString(is, charset);
	        
	        is.close();
	        conn.disconnect();
	        
	        
		} catch(MalformedURLException me) {
			logger.error(ExceptionUtils.getFullStackTrace(me));
		} catch(UnsupportedEncodingException ue) {
			logger.error(ExceptionUtils.getFullStackTrace(ue));
		} catch (IOException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} catch (KeyManagementException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} catch (NoSuchAlgorithmException e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return pageContent;
	}
	
	
	private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
