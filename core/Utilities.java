package core;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;

import javax.imageio.ImageIO;

import softclayfactory.variables.Progress;



public class Utilities{
	private static String debug = "";

	public static String getPerHour(long arg){
		return NumberFormat.getIntegerInstance().format(arg *3600000D / (Progress.runTime.getElapsed()));
	}
	
	public static Image getImage(String url) {
		try { return ImageIO.read(new URL(url)); } 
		catch(IOException e) { return null; }
	}
	
	public static String getDebug(){
		return debug;
	}
	public static void showDebug(String Status){
		System.out.println(Status);
		debug = Status;
		//System.out.println(Status);
		//Task.sleep(900);
	}
	//GE lookup......     
	public static int getGuidePrice(final int itemID) {
		return Integer.parseInt(lookup(itemID)[1]);
	}
	public static String[] lookup(final int itemID) {
		try {
			String[] info = {"0", "0", "0", "0"};
			final URL url = new URL("http://services.runescape.com/m=itemdb_rs/viewitem.ws?obj=" + itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String input;
			while ((input = br.readLine()) != null) {
				if(input.startsWith("<meta name=\"keywords")) {
					info[0] = input.substring(input.lastIndexOf(",") + 1, input.lastIndexOf("\"")).trim();
					if(info[0].equals("java")) return null;
				}
				if(input.contains("Current guide price:")) {
					input = br.readLine();
					info[1] = formatter(input.substring(4, input.lastIndexOf('<')));
					info[2] = ("" + itemID);
					return info;
				}
			}
		} catch (final Exception ignored) {}
		return null;
	}
	public static String formatter(String num) {
		try {
			return num.replaceAll("\\.","").replaceAll("m", "00000").replaceAll("k", "00").replaceAll(",", "");
		} catch (Exception e) {}
		return "0";
	}
	//------ END OF GE LOOKUP---------
}
