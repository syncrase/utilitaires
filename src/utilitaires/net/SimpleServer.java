package utilitaires.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Launch with : new Thread(new SimpleServer()).start();
 * @author I310911
 *
 */
public class SimpleServer implements Runnable {
	@Override
	public void run() {

		ServerSocket serverSocket = null;
		while (true) {
			try {
				serverSocket = new ServerSocket(3333);

				Socket clientSocket = serverSocket.accept();

				BufferedReader inputReader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				System.out.println("Client said :" + inputReader.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
