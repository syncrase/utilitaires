package utilitaires.printing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class InConsole {

	/**
	 * Use example : C:\\Users\\&lt;username&gt;\\...\\&lt;filename&gt;
	 * @param file
	 *            This is a file url on the local disk<br>
	 *            
	 */
	public static void printLocalFileFromPath(String filePath) {
		try {

			File file = new File(filePath);
			printFile(file);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Use example :
	 *            file://10.70.4.96\\info\\make\\pblack\\.prodpassaccess
	 *            \\credentials.properties
	 * @param urlstr This is a file url on the local disk
	 *            
	 */
	public static void printRemoteFileFromPath(String urlstr) {
		URL url;
		try {

			url = new URL(urlstr);
			InputStream is;
			try {

				is = url.openStream();
				InputStreamReader isReader = new InputStreamReader(is);
				BufferedReader in = new BufferedReader(isReader);

				String inputLine;
				while ((inputLine = in.readLine()) != null)
					System.out.println(inputLine);
				in.close();

			} catch (IOException e) {
				System.err.println("failed to open stream\n" + e.getMessage());
			}
		} catch (MalformedURLException e) {
			System.err.println("url malformed\n" + e.getMessage());
		}

	}

	/**
	 * 
	 * @param file This is the file to print in the console
	 */
	public static void printFile(File file) {
		try {

			BufferedReader br = new BufferedReader(new FileReader(file));
			try {

				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				do {
					sb.append(line);
					sb.append(System.lineSeparator());
				} while ((line = br.readLine()) != null);

				System.out.println(sb.toString());

			} catch (Throwable t) {
				t.printStackTrace();
			} finally {

				br.close();
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
