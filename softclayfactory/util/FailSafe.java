package softclayfactory.util;



import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.methods.widget.Lobby.World;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

import softclayfactory.SoftClayFactory;
import softclayfactory.util.Teleport;


public class FailSafe extends SoftClayFactory{
	private static Timer moveCameraTimer = new Timer(5000);
	
	public static boolean isFailSafe(){

		if(isNeedLogIn()) return false;
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

		if(!Progress.timeBeforeBreak.isRunning()){
			Utilities.showDebug("Breaking.");
			doBreak(Random.nextInt(300000, 400000));
			Progress.beforeBreakLength *= 2;
			Progress.timeBeforeBreak = new Timer((long) Random.nextDouble(Progress.beforeBreakLength-300000, Progress.beforeBreakLength + 300000));
			
		}

		if(Widgets.get(1252, 7).validate()){
			if(Bank.isOpen()) Bank.close();
			Utilities.showDebug("Interface closer.");
			Widgets.get(1252, 7).interact("Select");
		}

		if(!isInArea()){
			Teleport.teleportTo(Constants.EDGEVILL);
		}
		
		if(Progress.commander.length() != 0){
			String c = Progress.commander;
			String args[];
			args = c.split(":");
			if(args.length == 4){
				
				Command.doCommand(args[1], args[2], args[3]);
			}
			
			Progress.commander = "";
		}
		
		return true;

	}
	
	private static boolean isInArea(){
		Timer timeOut = new Timer(5000);
		while(timeOut.isRunning() && !Constants.factoryArea.contains(Players.getLocal())){
			Task.sleep(100);
		}
		
		return Constants.factoryArea.contains(Players.getLocal());
	}

	private static boolean isNeedLogIn() {
		// TODO Auto-generated method stub
		int s = Game.getClientState();

		Environment.enableRandom(org.powerbot.core.randoms.Login.class, false);
		if(s == 3){
			Utilities.showDebug("Logging in screen.");
			Environment.enableRandom(org.powerbot.core.randoms.Login.class, true);
		}else if(s == 7){
			Utilities.showDebug("Lobby screen.");
			 
			if(!Lobby.Tab.WORLD_SELECT.isOpen()) {
				Lobby.Tab.WORLD_SELECT.getWidget().click(true);
				Timer t = new Timer(3000);
				while(t.isRunning() && !Lobby.Tab.WORLD_SELECT.isOpen()){
					Task.sleep(200);
				}
			}
			
			World[] worldF2P = Lobby.getWorlds(new Filter<World>() {
				public boolean accept(World w) {
					return !w.isMembers();
				}
			});
			
			World randomWorld = worldF2P[Random.nextInt(0, worldF2P.length - 1)];
			Utilities.showDebug("Selecting world: " + randomWorld.getNumber());
			Lobby.enterGame(randomWorld);
		}

		return s == 11 ? false:true;
	}

	private static void doBreak(long ms){
		try{
			Timer breakTime = new Timer(ms);
			while(Game.isLoggedIn()) Game.logout(false);
			while(breakTime.isRunning() && !Game.isLoggedIn()){
				Task.sleep(1000);
				Utilities.showDebug("Break time: " + breakTime.toRemainingString());
			}
		}catch(Exception e){
			Utilities.showDebug("doBreak Error: " + e.getMessage());
		}
	}

	public static void doMoveCamera(final long interval){
		String cameraMovement = "None";
		if(!moveCameraTimer.isRunning()){
			switch(Random.nextInt(0, 10)){
			case 0:
				cameraMovement = "Camera Movement: Random angle, pitch.";
				Camera.setAngle(Random.nextInt(1, 359));
				Camera.setPitch(Random.nextInt(70, 99));
				break;
			case 1:
				cameraMovement ="Camera Movement: Turn to character";
				Camera.turnTo(Players.getLocal());
				Camera.setPitch(Random.nextInt(60, 99));
				break;
			case 2:
				cameraMovement ="Camera Movement: Random pitch.";
				Camera.setPitch(Random.nextInt(70, 99));
				break;

			}
			moveCameraTimer.setEndIn(interval);
			Utilities.showDebug("Camera Movement: " + cameraMovement);
			Utilities.showDebug("Camera movement timer reset by " + interval + "ms.");
		}
	}
}
