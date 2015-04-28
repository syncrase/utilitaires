package utilitaires.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * http://stackoverflow.com/questions/2793150/using-java-net-urlconnection-to-fire-and-handle-http-requests
 * 
 * @author I310911
 *
 */
public class HttpTest {

	private static void setProxy() {

		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", "emea-fr-par08-px01.par.sap.corp");
		System.getProperties().put("proxyPort", "8080");
	}

	private static void printResult(InputStream response) {
		try {
			StringBuilder sb = new StringBuilder();
			int i = 0;

			while ((i = response.read()) != -1) {
				// System.out.print(i);
				sb.append((char) i);
				if ((char) i == '>') {
					sb.append("\n");
				}
			}
			System.out.println(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printResult2(InputStream response) {
		InputStreamReader isr = new InputStreamReader(response);
		BufferedReader in = new BufferedReader(isr);
		String inputLine;
		System.out.println("Printing the Response ");

		try {
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// getTest1();
		// getTest2();
		// postTest1();
		// jsoupTest();
		detect2();
	}

	/**
	 * More information at http://stackoverflow.com/questions/2835505/how-to-scan-a-website-or-page-for-info-and-bring-it-into-my-program/2835555#2835555
	 */
	private static void jsoupTest() {
		setProxy();
		String url = "http://stackoverflow.com/questions/2835505";
		Document document = null;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String question = document.select("#question .post-text").text();
		System.out.println("Question: " + question);

		Elements answerers = document.select("#answers .user-details a");
		for (Element answerer : answerers) {
			System.out.println("Answerer: " + answerer.text());
		}
	}

	/**
	 * Possible to set some request parameter
	 */
	private static void getTest1() {
		setProxy();

		String url = "http://www.google.com";
		String charset = "UTF-8"; // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
		String param1 = "value1";
		String param2 = "value2";
		String query = null;
		// try {
		// query = String.format("param1=%s&param2=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		URLConnection connection = null;
		InputStream response = null;

		try {
			connection = new URL(url + (query == null ? "" : ("?" + query))).openConnection();
			// set up header
			connection.setRequestProperty("Accept-Charset", charset);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (checkResponseCode(connection)) {

			try {
				response = connection.getInputStream();
				printResult(response);
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Request without parameters
	 */
	public static void getTest2() {
		setProxy();
		String url = "http://www.google.com";
		InputStream response = null;
		try {
			response = new URL(url).openStream();

			printResult2(response);
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * see : http://www.ietf.org/rfc/rfc2616.txt It's a reading input method
	 * 
	 * @param connection
	 */
	private static boolean checkResponseCode(URLConnection connection) {
		try {

			HttpURLConnection connectionOngoing = (HttpURLConnection) connection;
			int responseCode = connectionOngoing.getResponseCode();
			String toPrint = "";
			toPrint += "URL : " + connectionOngoing.getURL() + "\n";
			toPrint += "connect time out : " + connectionOngoing.getConnectTimeout() + "\n";
			toPrint += "Encoding : " + connectionOngoing.getContentEncoding() + "\n";
			toPrint += "Content Length : " + connectionOngoing.getContentLength() + "\n";
			toPrint += "Content type : " + connectionOngoing.getContentType() + "\n";
			toPrint += "Date : " + connectionOngoing.getDate() + "\n";
			toPrint += "Expiration : " + connectionOngoing.getExpiration() + "\n";
			toPrint += "Request method : " + connectionOngoing.getRequestMethod() + "\n";
			toPrint += "Response message : " + connectionOngoing.getResponseMessage() + "\n";
			toPrint += "Allow user interaction : " + connectionOngoing.getAllowUserInteraction() + "\n";
			try {
				toPrint += "Content : " + connectionOngoing.getContent().toString() + "\n";
			} catch (Exception e) {

			}

			toPrint += "DoInput : " + connectionOngoing.getDoInput() + "\n";
			toPrint += "DoOutput : " + connectionOngoing.getDoOutput() + "\n";

			try {
				toPrint += "request properties : " + connectionOngoing.getRequestProperties() + "\n";
			} catch (Exception e) {

			}

			int i = 0;
			String str;
			System.out.println("\n\n");
			while ((str = connectionOngoing.getHeaderField(i++)) != null) {
				System.out.println("Header field " + (i) + " : " + str);
			}
			if (i == 1) {
				System.out.println("Il n'y a pas de header field");
			}

			System.out.println("\n\n");
			for (Entry<String, List<String>> header : connectionOngoing.getHeaderFields().entrySet()) {
				System.out.println(header.getKey() + "=" + header.getValue());
			}
			System.out.println("\n\n");
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";
			// toPrint += " : " + responseCode + "\n";

			switch (responseCode) {
			case 200:
				toPrint += "200 OK\n\t" + "The request has succeeded. The information returned with the response"
						+ "is dependent on the method used in the request, for example:"
						+ "\n\tGET    an entity corresponding to the requested resource is sent in" + "the response;"
						+ "\n\tHEAD   the entity-header fields corresponding to the requested" + "resource are sent in the response without any message-body;"
						+ "\n\tPOST   an entity describing or containing the result of the action;"
						+ "\n\tTRACE  an entity containing the request message as received by the" + "end server.";
				break;

			case 400:
				toPrint += "400 Bad Request \n\tThe request could not be understood by the server due to "
						+ "malformed syntax. The client SHOULD NOT repeat the request without modifications.";
				break;

			case 401:
				toPrint += "401 Unauthorized \n\tThe request requires user authentication. The response MUST include a "
						+ "WWW-Authenticate header field (section 14.47) containing a challenge"
						+ "applicable to the requested resource. The client MAY repeat the"
						+ "request with a suitable Authorization header field (section 14.8). If"
						+ "the request already included Authorization credentials, then the 401"
						+ "response indicates that authorization has been refused for those"
						+ "credentials. If the 401 response contains the same challenge as the" + "prior response, and the user agent has already attempted"
						+ "authentication at least once, then the user SHOULD be presented the"
						+ "entity that was given in the response, since that entity might"
						+ "include relevant diagnostic information. HTTP access authentication"
						+ "is explained in \"HTTP Authentication: Basic and Digest Access" + "Authentication\" [43].";
				break;
			case 405:
				toPrint += "405 Method Not Allowed\n\t" + "The method specified in the Request-Line is not allowed for the"
						+ "resource identified by the Request-URI. The response MUST include an"
						+ "Allow header containing a list of valid methods for the requested" + "resource.";
				break;
			default:
				toPrint += "Response code " + responseCode + " isn't handle. Please refer http://www.ietf.org/rfc/rfc2616.txt to implement the missing code.";
			}
			System.out.println(toPrint);
			if (responseCode == 200) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return false;
	}

	private static void postTest1() {
		setProxy();

		String url = "http://www.google.com";
		String charset = "UTF-8"; // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()

		URLConnection connection = null;
		InputStream response = null;

		// query parameters are required
		try {
			connection = new URL(url).openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

		String query = null;
		String param1 = "value1";
		String param2 = "value2";
		try {
			query = String.format("param1=%s&param2=%s", URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// specify that's a post request
		// With wrong request query, error 405 Method Not Allowed
		connection.setDoOutput(true); // Triggers POST. else => java.net.ProtocolException: cannot write to a URLConnection if doOutput=false - call
										// setDoOutput(true)
		try (OutputStream output = connection.getOutputStream()) {
			if (query != null) {
				output.write(query.getBytes(charset));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		// try {
		// System.out.println("response code : " + ((HttpURLConnection) connection).getResponseCode());
		// } catch (IOException e2) {
		// e2.printStackTrace();
		// }
		if (checkResponseCode(connection)) {
			try {
				response = connection.getInputStream();

				printResult(response);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			InputStream error = ((HttpURLConnection) connection).getErrorStream();
			System.setProperty("http.keepAlive", "false");
		}

	}

	public static void detect2() {

		try {

			System.setProperty("java.net.useSystemProxies", "true");

			List l = ProxySelector.getDefault().select(

			new URI("http://www.yahoo.com/"));

			for (Iterator iter = l.iterator(); iter.hasNext();) {

				Proxy proxy = (Proxy) iter.next();

				System.out.println("proxy type : " + proxy.type());

				InetSocketAddress addr = (InetSocketAddress)

				proxy.address();

				if (addr == null) {

					System.out.println("No Proxy");

				} else {

					System.out.println("proxy hostname : " +

					addr.getHostName());

					System.out.println("proxy port : " +

					addr.getPort());

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void detect(String location)

	{


	}

}
