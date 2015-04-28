package utilitaires.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Launch it with : new Thread(new SimpleClient()).start();
 * @author I310911
 *
 */
public class SimpleClient implements Runnable {
	@Override
	public void run() {

		Socket socket = null;
		try {
			Thread.sleep(3000);

			socket = new Socket("localhost", 3333);

			PrintWriter outWriter = new PrintWriter(socket.getOutputStream(),
					true);

			outWriter.println("Hello Mr. Server!");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				socket.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}
