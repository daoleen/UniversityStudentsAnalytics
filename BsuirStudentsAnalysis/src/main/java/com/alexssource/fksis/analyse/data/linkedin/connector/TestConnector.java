package com.alexssource.fksis.analyse.data.linkedin.connector;

import java.util.Scanner;

import org.scribe.model.Response;
import org.scribe.model.Verb;

@Deprecated
public class TestConnector {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("Enter the LinkedIn API URL for request or 'exit' to terminate: ");
			String choice = scanner.nextLine();
			
			if(choice.equalsIgnoreCase("exit")) {
				break;
			}
			Connector connector = Connector.getInstance();
			Response response = connector.sendRequest(Verb.GET, choice);
			System.out.println("Response: " + response.getBody());
		}
		scanner.close();
	}
}
