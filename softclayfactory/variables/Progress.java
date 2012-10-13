package softclayfactory.variables;


import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

import core.Utilities;


public class Progress {
	public static int startExp;
	
	public static int startLvl;
	public static String commander = "";
	public static int productsMade;
	public static int profitGained; 
	public static int profitMining = Utilities.getGuidePrice(Constants.CLAY_ID);
	public static int profitSoftening = Utilities.getGuidePrice(Constants.SOFTCLAY_ID) - Progress.profitMining;
	
	public static int softclaysInBank = 0;
	public static long beforeBreakLength = 18000000;
	public static Timer runTime = new Timer(0);
	public static Timer timeBeforeBreak = new Timer((long) Random.nextDouble(beforeBreakLength-300000, beforeBreakLength + 300000));
	

}
