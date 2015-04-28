package utilitaires.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import utilitaires.formating.forhumans.Html;

public class ConnectionTest {

	public static void main(String[] args) {

		String strUrl = "https://css.wdf.sap.corp/sap/bc/bsp/spn/jcwb_api_extern/get_correction_requests?pointer=012006153200001159182015";
		String username = "bitp4_ngcp";
		String password = "Paris@123";
		// "https://bcdmain.wdf.sap.corp/sap/bc/srt/scs/svc/ii_rfc_read_zini?sap-client=001";
		// "https://bcdmain.wdf.sap.corp/sap/bc/srt/wsdl/flv_10002P111AD1/sdef_url/SVC/II_RFC_READ_ZINI?sap-client=001";
		HttpsURLConnection con = null;

		try {
			con = sendRequest(strUrl, username, password);
		} catch (Exception e) {
			System.err.println("Failed to send request\n\t---> " + e);
		}

		try {
			System.out.println("Response code = " + con.getResponseCode());
			System.out.println("Response message = " + con.getResponseMessage());

		} catch (IOException e) {
			System.err.println("Failed to get response code or message\n\t---> " + e);
		}

		InputStream inputStream = null;
		try {
			inputStream = con.getInputStream();
		} catch (IOException e) {
			System.err.println("Failed to get input stream\n\t---> " + e);
		}

		// System.out.println(Html.indent2(getStreamToString(inputStream)));
		System.out.println("Status: " + getCRStatus(inputStream));

	}

	private static String getStreamToString(InputStream inputStream) {
		StringBuilder buffer = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String input;
		try {
			while ((input = br.readLine()) != null) {
				buffer.append(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/*
	 * 
	 * @param strUrl url to use
	 * 
	 * @param sslContext SSL or TLS. null if you needn't to use this.
	 * 
	 * @param credentials Map containing two items with keys 'user' and 'password'
	 * 
	 * @param httpMethod GET or POST. null if you needn't to use this.
	 * 
	 * @param userAgent Request-property : the user-agent required. null if you needn't to use this.
	 * 
	 * @param contentType Request-property : the content-type required. null if you needn't to use this.
	 * 
	 * @param accept Request-property
	 * 
	 * @param useCache Request-property
	 * 
	 * @param doInput Request-property
	 * 
	 * @param doOutput Request-property
	 * 
	 * @param proxy Map containing proxy info : proxyHost, proxyPort
	 * 
	 * @return The http connection if no exception has been raised
	 * 
	 * @throws Exception NoSuchAlgorithmException Raised when no such algorithm is passed in parameter<br> KeyManagementException Raised when it's impossible to
	 * initiate the SSLContext<br> MalformedURLException : Raised when the url is malformed<br> IOException : Raised when the connection opening failed
	 */

	public static HttpsURLConnection sendRequest(String strUrl, String username, String password) throws Exception {

		disableCertificateValidation();

		URL url = new URL(strUrl);
		HttpsURLConnection httpConnexion = (HttpsURLConnection) url.openConnection();

		String authStr = username + ":" + password;
		String authEncoded = Base64.encodeBase64String(authStr.getBytes());
		httpConnexion.setRequestProperty("Authorization", "Basic " + authEncoded);

		httpConnexion.setRequestMethod("GET");

		return httpConnexion;
	}

	private static void disableCertificateValidation() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}// SSL ou TSL
		try {
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	/**
	 * 
	 */
	private static void getStatusFromCwb() {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			try {
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				/*
				 * end of the fix
				 */

				String serverUrl =
				// "https://css.wdf.sap.corp/sap/bc/bsp/spn/bcwbadr_t/loginuser.xml";
				"https://css.wdf.sap.corp/sap/bc/bsp/spn/jcwb_api_extern/get_correction_requests?pointer=012006153200001159182015";//

				String user = "bitp4_ngcp";
				String pass = "Paris@123";

				try {
					URL url = new URL(serverUrl);
					String authStr = user + ":" + pass;
					String authEncoded = Base64.encodeBase64String(authStr.getBytes());
					try {
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestProperty("Authorization", "Basic " + authEncoded);
						con.setRequestMethod("GET");
						// con.setRequestProperty("User-Agent",
						// "libwww-perl/6.02");
						// con.setRequestProperty("content-type",
						// "application/x-www-form-urlencoded");
						// con.setRequestProperty("Accept", "*/*");
						con.setUseCaches(false);

						con.setDoInput(true);
						con.setDoOutput(true);

						// OutputStreamWriter writer = null;
						// try {
						// writer = new OutputStreamWriter(
						// con.getOutputStream());
						//
						// } finally {
						// if (writer != null)
						// try {
						// writer.close();
						// } catch (IOException ioe) {
						// }
						// }

						/*
						 * ERROR: Response code is: 403 This service requires client certificate for authentication procedure.
						 */
						int status = ((HttpURLConnection) con).getResponseCode();
						System.out.println("Response code is: " + status);

						if (status == 200) {
							// it works

							// StringBuilder buffer = new StringBuilder();
							// try {
							// InputStream is = con.getInputStream();
							// InputStreamReader isr = new
							// InputStreamReader(
							// is);
							// BufferedReader br = new BufferedReader(isr);
							// String input;
							// while ((input = br.readLine()) != null) {
							// buffer.append(input);
							// }
							// br.close();
							//
							// System.out.println(Html.indent2(buffer
							// .toString()));// Html(buffer.toString())
							// } catch (Exception e) {
							// try {
							// throw new Exception(
							// "Cannot execute URL '" + url
							// + "': "
							// + e.getMessage(), e);
							// } catch (Exception e1) {
							// e1.printStackTrace();
							// }
							// }
							//
							try {
								DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
								DocumentBuilder builder = builderFactory.newDocumentBuilder();
								builder.setEntityResolver(new EntityResolver() {

									@Override
									public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
										return new InputSource(new StringReader(""));
									}
								});

								InputStream in = con.getInputStream();
								Document xmlDocument = builder.parse(in);
								in.close();
								XPathFactory xpf = XPathFactory.newInstance();
								XPath xpath = xpf.newXPath();
								NodeList nodeList = (NodeList) xpath.compile("//status").evaluate(xmlDocument, XPathConstants.NODESET);
								for (int i = 0; i < nodeList.getLength(); i++) {

									System.out.println("CR status : " + nodeList.item(i).getFirstChild().getNodeValue());

								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							Reader reader = new InputStreamReader(status < 400 ? con.getInputStream() : ((HttpURLConnection) con).getErrorStream());

							System.err.print("ERROR: ");
							StringBuilder strBuilder = new StringBuilder();

							while (true) {
								int ch = reader.read();
								if (ch == -1) {
									System.err.println(utilitaires.formating.forhumans.Html.indent2(strBuilder.toString()));
									break;
								}
								// System.err.print((char) ch);
								strBuilder.append((char) ch);
							}
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static void googleConnection() throws Exception {
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", "emea-fr-par08-px01.par.sap.corp");
		System.getProperties().put("proxyPort", "8080");
		//
		String sURL = "https://www.google.fr/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=test";
		URL url = new URL(sURL);

		HttpURLConnection httpCon;
		{// set up connection
			httpCon = (HttpURLConnection) url.openConnection();
			// set http request headers
			httpCon.addRequestProperty("Host", sURL);
			httpCon.addRequestProperty("Connection", "keep-alive");
			httpCon.addRequestProperty("Cache-Control", "max-age=0");
			httpCon.addRequestProperty("Accept", "text/html");
			httpCon.addRequestProperty("User-Agent",
			//
			// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
					"Mozilla 5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.11) Chrome/30.0.1599.101");
			// Safari/537.36 Affichage corrompu
			// AppleWebKit/537.36 (KHTML, like Gecko) aucune sortie
			httpCon.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			httpCon.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			httpCon.addRequestProperty(
					"Cookie",
					"JSESSIONID=EC0F373FCC023CD3B8B9C1E2E2F7606C; lang=tr; __utma=169322547.1217782332.1386173665.1386173665.1386173665.1; __utmb=169322547.1.10.1386173665; __utmc=169322547; __utmz=169322547.1386173665.1.1.utmcsr=stackoverflow.com|utmccn=(referral)|utmcmd=referral|utmcct=/questions/8616781/how-to-get-a-web-pages-source-code-from-java; __gads=ID=3ab4e50d8713e391:T=1386173664:S=ALNI_Mb8N_wW0xS_wRa68vhR0gTRl8MwFA; scrElm=body");
			HttpURLConnection.setFollowRedirects(false);
			httpCon.setInstanceFollowRedirects(false);
			httpCon.setDoOutput(true);
			httpCon.setUseCaches(true);

			httpCon.setRequestMethod("GET");
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuilder a = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			a.append(inputLine);
		in.close();

		// System.out.println(a.toString());

		String path = "file.html";
		utilitaires.printing.InFile.writeString(a.toString(), path, false);

		httpCon.disconnect();
	}

	private static String getCRStatus(InputStream inputStream) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Failed to create a new document builder\n\t---> " + e);
		}

		builder.setEntityResolver(new EntityResolver() {

			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				return new InputSource(new StringReader(""));
			}
		});
		Document xmlDocument = null;
		try {
			xmlDocument = builder.parse(inputStream);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();
		NodeList nodeList = null;
		try {
			nodeList = (NodeList) xpath.compile("//status").evaluate(xmlDocument, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		if (nodeList != null) {
			return nodeList.item(0).getFirstChild().getNodeValue();
		}
		return null;
	}
}
