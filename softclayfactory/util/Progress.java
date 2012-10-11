package softclayfactory.util;


import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

public class Progress {
	public static int startExp;
	
	public static int startLvl;
	public static String commander = "";
	public static int productsMade, 
	profitGained, 
	profitSoftening,
	profitMining;
	
	public static int softclaysInBank = 0;
	public static long beforeBreakLength = 10800000;
	public static Timer runTime = new Timer(0);
	public static Timer timeBeforeBreak = new Timer((long) Random.nextDouble(beforeBreakLength-300000, beforeBreakLength + 300000));
	

}
