package battleship;

import java.io.*;
import java.net.*;

public class ShipServer{
  static int sp = 4500;
	static String sIP = "127.0.0.1";
	static InetSocketAddress isa = new InetSocketAddress(sIP, sp);

	static String messageReceived, messageToSend;

	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket();
		ss.bind(isa);
		System.out.println("Server is up and Running @: " + " " + ss.getInetAddress() + " : "
				+ ss.getLocalPort());
		while (true) {
			Socket socket = ss.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os, true);
			InputStream is = socket.getInputStream();
			BufferedReader receiveRead = new BufferedReader(
					new InputStreamReader(is));

			if ((messageReceived = receiveRead.readLine()) != null) {
				System.out.println(messageReceived);
			}
			messageToSend = br.readLine();
			pw.println(messageToSend);
			System.out.flush();
		}
		// ss.close();
	}
}
