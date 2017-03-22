package com.emailer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.emailer.dto.MessageRequest;
import com.emailer.dto.MessageResponse;
import com.emailer.util.EmailUtil;
import com.emailer.util.JsonUtil;

/**
 * A server program which accepts requests from clients to send emails. When
 * clients connect, a new thread is started. When the client sends an request
 * payload for email thread sends back the acknowledgement.
 *
 * The program is runs in an infinite loop, so shutdown in platform dependent.
 */
public class EmailSender {

	/**
	 * Application method to run the server runs in an infinite loop listening
	 * on port 4045. When a connection is requested, it spawns a new thread to
	 * do the servicing and immediately returns to listening. The server keeps a
	 * unique client number for each client that connects just to show
	 * interesting logging messages.
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("The Email Sender is running...");
		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(4045);
		try {
			while (true) {
				new Emailer(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}

	/**
	 * A private thread to handle email sending requests on a particular socket.
	 */
	private static class Emailer extends Thread {
		private Socket socket;
		private int clientNumber;

		public Emailer(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client# " + clientNumber + " at " + socket);
		}

		/**
		 * Services this thread's client by first sending the client a welcome
		 * message then sends acknowledgement of email.
		 */
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				// Send a welcome message to the client.
				out.println("Hello, you are client #" + clientNumber + ".");
				out.println("Welcome to EmailSender !!!\n");
				
				while (true) {
					String input = in.readLine();
					if (input == null) {
						break;
					}
					
					MessageRequest request = JsonUtil.readJson(input, MessageRequest.class);
					
					//send email
					EmailUtil.send(request);	
					
					//populate acknowledgement response
					MessageResponse response = new MessageResponse();
					response.setRequestId(request.getRequestId());
					response.setStatus("OK");
					
					out.println(JsonUtil.writeJson(response, MessageResponse.class));
				}
			} catch (IOException e) {
				System.out.println("Error handling client# " + clientNumber + ": " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket, what's going on?");
				}
				System.out.println("Connection with client# " + clientNumber + " closed");
			}
		}
		
	}
}

