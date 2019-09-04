package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Searcher {
	private static String wikiApiURL = "https://www.wikipedia.org/w/api.php";
	private static String wikiArticleURL = "https://en.wikipedia.org/wiki/";
	
	public BufferedImage image (String link) {
		// TODO: Return image from page
		return null;
	}
	
	public String text (String link) {
		// TODO: Return text from page
		return null;
	}
	
	public String[] links (String searchTerm) {
		String urlString = wikiApiURL + "?action=query&list=search&srsearch=" + searchTerm.replaceAll(" ", "%20") + "&format=json";
		String data = rawData (urlString);
		ArrayList<String> newnames = new ArrayList<String> ();
		int startChar = data.indexOf("\"search\"");
		while (startChar > 0) {
			startChar = data.indexOf("title", startChar);
			newnames.add(data.substring(data.indexOf(":", startChar) + 1, data.indexOf(",", startChar)).replaceAll(" ", "_"));
			startChar = data.indexOf("}", startChar);
			if (data.charAt(startChar+1) == ']') break;
		}
		availablePageNames.addAll(newnames);
		String[] ret = new String[newnames.size()];
		return newnames.toArray(ret);
	}
	
	public String rawData (String urlString) {
		try {
			URL url = new URL (urlString);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			String data = new String (is.readAllBytes());
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public ArrayList<String> availablePageNames = new ArrayList<String> ();
	
	public Searcher () {
		
	}
}
