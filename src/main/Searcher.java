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
	
	public BufferedImage[] images (String title) {
		// TODO: Return image from page
		return null;
	}
	
	public String text (String title) {
		String urlString = wikiApiURL + "?action=parse&page=" + title + "&prop=text&format=json";
		String data = rawData (urlString);
		String prefix = "\"text\":{\"*\":\"";
		int bI = data.indexOf(prefix) + prefix.length();
		int bracks = 1;
		int eI = bI;
		while (bracks > 0 && eI < data.length()) {
			if (data.charAt(eI) == '}') bracks--;
			if (data.charAt(eI) == '{') bracks++;
			//System.out.println (bracks);
			eI++;
		}
		String content = data.substring (bI, eI-2);
		int a,b = 0;
		while (b < content.length()) {
			a = content.indexOf("<");
			b = content.indexOf(">", a);
			content = content.substring(0, a) + " " + content.substring(b+1);
		}
		content = content.trim().replaceAll("(\\s)+", " ");
		return content;
	}
	
	public String[] links (String searchTerm) {
		String urlString = wikiApiURL + "?action=query&list=search&srsearch=" + searchTerm.replaceAll(" ", "%20") + "&format=json";
		String data = rawData (urlString);
		ArrayList<String> newnames = new ArrayList<String> ();
		int startChar = data.indexOf("\"search\"");
		while (startChar > 0) {
			startChar = data.indexOf("title", startChar);
			newnames.add(data.substring(data.indexOf(":", startChar) + 1, data.indexOf(",", startChar)).replaceAll(" ", "_").replaceAll ("\"", ""));
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
			conn.connect();
			InputStream is = conn.getInputStream();
			String data = new String (is.readAllBytes());
			is.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return "";
		}
	}
	
	public ArrayList<String> availablePageNames = new ArrayList<String> ();
	
	public Searcher () {
		
	}
}
