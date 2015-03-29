package com.alexssource.fksis.analyse.data.linkedin;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector {
	private static Connector connector;
	private static Object lock = new Object();
	private final static String LIN_API_KEY = "75ja0g2e9irk7j";
	private final static String LIN_API_SECRET = "KU60CjgF3Vvui85I";
	private final static Logger logger = LoggerFactory.getLogger(Connector.class);
	
	private OAuthService service;
	private Token accessToken;
	
	private Connector() {
		createService();
		connect();
	}
	
	public static Connector getInstance() {
		if(connector == null) {
			synchronized(lock) {
				if(connector == null) {
					connector = new Connector();
				}
			}
		}
		return connector;
	}
	
	public Response sendRequest(Verb verb, final String apiUrl) {
		OAuthRequest request = new OAuthRequest(verb, apiUrl);
		service.signRequest(accessToken, request);
		return request.send();
	}
	
	private void createService() {
		service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(LIN_API_KEY)
				.apiSecret(LIN_API_SECRET)
				.build();
	}
	
	private void connect() {
		Token requestToken = service.getRequestToken();
		logger.debug("Request token: {}", requestToken.getToken());
		
		String authUrl = service.getAuthorizationUrl(requestToken);
		logger.debug("Authentication url: {}", authUrl);
		
		String verificationCode = promptVerificationCode(authUrl);
		logger.debug("Verification code: {}", verificationCode);
		
		accessToken = getAccessToken(requestToken, verificationCode);
		logger.info("Access token: {}", accessToken.getToken());
	}
	
	private String promptVerificationCode(String authUrl) {
		System.out.println("Please proceed by the following url: " + authUrl);
		System.out.print("And enter the verification code here: ");
		return new Scanner(System.in).nextLine();
	}
	
	private Token getAccessToken(Token requestToken, String verificationCode) {
		Verifier verifier = new Verifier(verificationCode);
		return service.getAccessToken(requestToken, verifier);
	}
}
