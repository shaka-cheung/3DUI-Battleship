package battleship;

import java.io.*;
import java.net.*;

public class ShipClient{

  static int sp = 4500;
	static String sIP = "127.0.0.1";
	static String messageReceived, messageToSend;

	public static void main(String[] args) throws Exception {
		while (true) {
			Socket socket = new Socket(sIP, sp);
			BufferedReader brInput = new BufferedReader(new InputStreamReader(
					System.in));
			OutputStream os = socket.getOutputStream();
			PrintWriter pwrite = new PrintWriter(os, true);
			InputStream socketIS = socket.getInputStream();
			BufferedReader receiveRead = new BufferedReader(
					new InputStreamReader(socketIS));
			System.out.println("Client is up & Running");
			messageToSend = brInput.readLine();
			pwrite.println(messageToSend);
			System.out.flush();
			if ((messageReceived = receiveRead.readLine()) != null) {
				System.out.println(messageReceived);
			}
		}
	}
}
