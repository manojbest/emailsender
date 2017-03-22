package com.emailer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.emailer.dto.MessageRequest;
import com.emailer.util.JsonUtil;

public class EmailClient {

	private BufferedReader in;
	private PrintWriter out;

	public EmailClient() {

	}

	/**
	 * Connecting, setting up streams, and consuming the welcome messages from
	 * the server.
	 */
	public void connectToServer() throws IOException {
		// Make connection and initialize streams
		Socket socket = new Socket("localhost", 4045);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Consume the initial welcoming messages from the server
		for (int i = 0; i < 3; i++) {
			System.out.println(in.readLine() + "\n");
		}
	}

	public String sendEmail(MessageRequest message) {
		String jsonStr = JsonUtil.writeJson(message, MessageRequest.class);
		out.println(jsonStr);
		String response;
		try {
			response = in.readLine();
			System.out.println(response);
		} catch (IOException ex) {
			System.out.println(response = "Error: " + ex);
		}
		return response;
	}

	public static void main(String[] args) {

		int numberOfRequest = 5;
		int numberOfThreads = 10;

		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

		for (int i = 0; i < numberOfRequest; i++) {
			executorService.execute(new Runnable() {
				public void run() {
					try {
						new Random();
						EmailClient client = new EmailClient();
						client.connectToServer();
						MessageRequest message = new MessageRequest();
						String randomNumber = String.valueOf(new Random().nextInt(100000));
						message.setRequestId(randomNumber);
						message.setEmailAddress("testuser@test.com");
						message.setSenderName("testuser");
						message.setSubject(randomNumber);
						message.setBody("Sample body");
						client.sendEmail(message);
					} catch (IOException e) {
						System.out.println("Exception occured : " + e);
					}

				}
			});
		}

		executorService.shutdown();
	}
}
