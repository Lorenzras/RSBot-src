package softclayfactory.util;


import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;


public class Command{
	private static String player = "";
	
	public static void doCommand(String recipient, String command, String arg){
		// TODO Auto-generated method stub
		if(player.length() == 0) player = Players.getLocal().getName();
		Utilities.showDebug(recipient + " " + command + " " + arg);
		if(recipient.contains("all") || recipient.contains(player)){
			Utilities.showDebug("recipient check");
			if(command.contains("say")){
				
				Utilities.showDebug("command check");
				switch(arg.trim()){
				case "count":
					Keyboard.sendText("I have " + Progress.softclaysInBank + " soft clays in bank.", true);
					break;
				case "runtime":
					Keyboard.sendText("I have been playing for " + Progress.runTime.toElapsedString() + ".", true);
					break;
				default:
					Keyboard.sendText(arg, true);
					break;
				}
			}
			
		}
	}
}
