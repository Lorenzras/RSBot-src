package core.randoms;

import org.powerbot.core.randoms.Maze;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import core.Utilities;


public class FailSafe {
	public static boolean executeAll(){
		try{
			interfaceCloser();
			disableFailedRandoms();
			if(randomsHandler()) return false;
		}catch(Exception e){
			Utilities.showDebug("executeAll: " + e.getMessage());
		}
		
		return true;
	}
	
	private static void disableFailedRandoms(){
		if(Environment.isRandomEnabled(Maze.class)){
			Environment.enableRandom(Maze.class, false);
		}
		
		
	}
	
	
	public static void interfaceCloser(){
		if(Inventory.getCount(14664) > 0){
			Utilities.showDebug("Dropping gift box.");
			Inventory.getItem(14664).getWidgetChild().interact("drop");
			Task.sleep(1200, 1600);
		}

		if(Inventory.getCount(1511) > 0){
			Utilities.showDebug("Dropping logs.");
			Inventory.getItem(1511).getWidgetChild().interact("drop");
			Task.sleep(1200, 1600);
		}
		
		if(Widgets.get(1252, 7).validate()){
			if(Bank.isOpen()) Bank.close();
			Utilities.showDebug("Interface closer.");
			Widgets.get(1252, 7).interact("Select");
		}
		
		if(Widgets.get(205, 62).validate()){
			Utilities.showDebug("Interface closer.");
			if(Bank.isOpen()) Bank.close();
			Widgets.get(205, 62).interact("close");
		}
		
		if(Widgets.get(1184,13).validate()){
			Utilities.showDebug("Mysterious old man");
			Keyboard.sendKey(' ');
			Task.sleep(3000);
		}

		if(Widgets.get(1253, 175).validate()){
			Utilities.showDebug("SOF Interface closer.");
			Widgets.get(1253, 175).interact("Done");
		}

		if(Widgets.get(1253, 115).validate()){
			Utilities.showDebug("SOF Interface closer.");
			Widgets.get(1253, 115).interact("Hide");
		}

		if(Widgets.get(751, 31).validate()){

			if(!Widgets.get(751, 31).getText().contains("On")){
				Utilities.showDebug("Turning public chat ON.");
				Widgets.get(751, 31).interact("On", "Public");
			}

		}
	}
	
	public static boolean randomsHandler(){
		if(Maze_.isTrapped()){
			Maze_.solve();
			return true;
		}
		
		return false;
	}
}
