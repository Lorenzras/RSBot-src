package softclayfactory.methods;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;

import softclayfactory.util.Utilities;

public class FailSafe {
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
}
