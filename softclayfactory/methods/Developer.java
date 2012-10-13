package softclayfactory.methods;


import java.util.Arrays;
import java.util.List;

import org.powerbot.core.randoms.*;
import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.Players;

import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.methods.widget.Lobby;
import org.powerbot.game.api.methods.widget.Lobby.World;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;


import softclayfactory.SoftClayFactory;
import softclayfactory.core.Utilities;
import softclayfactory.core.randoms.FailSafe;
import softclayfactory.variables.Constants;
import softclayfactory.variables.Progress;


public class Developer extends SoftClayFactory{
	private static Timer moveCameraTimer = new Timer(5000);
	private static Timer updateSettingsTimer = new Timer(5000);
	private static String player = "";

	//Settings
	private static List<String> allowedUsers = Arrays.asList(
			"Lorenzras", 
			"JPA", 
			"GlassFish");
	private static int restrictedWorld = 1;
	private static boolean isRestricted = false;

	public static boolean isAllowedToContinue(){

		if(!isNeedLogIn()){
			if(FailSafe.executeAll()){
				if(!Progress.timeBeforeBreak.isRunning()){
					Utilities.showDebug("Breaking.");
					doBreak((long) Random.nextDouble(Progress.beforeBreakLength/100, Progress.beforeBreakLength/100 + 120000));
					Progress.beforeBreakLength *= 2;
					Progress.timeBeforeBreak = new Timer((long) Random.nextDouble(Progress.beforeBreakLength-300000, Progress.beforeBreakLength + 300000));
	
				}
	
				if(!isInArea()){
					Teleport.teleportTo(Teleport.EDGEVILL);
				}
	
				if(Progress.commander.length() != 0){
					String c = Progress.commander;
					String args[];
					args = c.split(":");
					if(args.length == 4){
						Utilities.showDebug(args[0] + args[3]);
						doCommand(args[1].trim(), args[2].trim(), args[3].trim());
					}
	
					Progress.commander = "";
				}
	
				if(!moveCameraTimer.isRunning()){
					doMoveCamera(Random.nextInt(5000, 30000));
				}
	
	
				isRestricted = !allowedUsers.contains(Environment.getDisplayName());
				if(!updateSettingsTimer.isRunning() && isRestricted){
					Utilities.showDebug("Restricted. Implementing restrictions.");
					if(getCurrentWorld() != restrictedWorld) {
						Game.logout(true);
						return false;
					}
					updateSettingsTimer.setEndIn(5000);
				}
	
			}
		}else{
			return false;
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

	private static int getCurrentWorld(){
		try{
			return Integer.parseInt(Widgets.get(550).getChild(18).getText().replace("Friends List<br>RuneScape ", ""));

		}catch(Exception e){
			Utilities.showDebug("getCurrentWorld: " + e.getMessage());
		}
		return 1;
	}
	private static boolean isNeedLogIn() {
		// TODO Auto-generated method stub
		int s = Game.getClientState();

		try{
			if(Environment.isRandomEnabled(Login.class)){
				Environment.enableRandom(Login.class, false);
			}

			if(s == 3){
				Utilities.showDebug("Logging in screen.");
				if(Environment.isRandomEnabled(Login.class)){
					Utilities.showDebug("Login handler disabled. Enabling..");
					Environment.enableRandom(Login.class, true);
				}
			}else if(s == 7){
				Utilities.showDebug("Lobby screen.");

				if(!Lobby.Tab.WORLD_SELECT.isOpen()) {
					Lobby.Tab.WORLD_SELECT.getWidget().click(true);
					Timer t = new Timer(3000);
					while(t.isRunning() && !Lobby.Tab.WORLD_SELECT.isOpen()){
						Task.sleep(200);
					}
				}

				Lobby.enterGame(worldToEnter(!isRestricted));
			}
		}catch(Exception e){
			Utilities.showDebug("isNeedLogin: " + e.getMessage());
		}

		return s == 11 ? false:true;
	}

	private static World worldToEnter(final boolean isRandom){
		Utilities.showDebug("World: " + restrictedWorld);
		World choosenWorld;
		if(isRandom){
			World[] worldF2P = Lobby.getWorlds(new Filter<World>() {
				public boolean accept(World w) {
					return !w.isMembers();
				}
			});
			choosenWorld = worldF2P[Random.nextInt(0, worldF2P.length-1)];
		}else{
			Utilities.showDebug("choosing restricted world.");
			choosenWorld = Lobby.getWorld(restrictedWorld);
		}
		choosenWorld.click();
		Utilities.showDebug("Choosen world number: " + choosenWorld.getNumber());
		return choosenWorld;
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

	private static void doMoveCamera(final long interval){
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

	public static void doCommand(String recipient, String command, String arg){
		// TODO Auto-generated method stub
		if(player.length() == 0) player = Players.getLocal().getName();
		Utilities.showDebug(recipient + " " + player + " " + arg);
		
		if(recipient.contains("all") || player.toLowerCase().contains(recipient)){
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
			}else if(command.contains("emote")){
				Utilities.showDebug("Doing emote.");

				if(Tabs.EMOTES.open()){
					Utilities.showDebug("Emoting");
					if(Widgets.get(590, 8).getChild(Integer.parseInt(arg.trim())).validate()){
						Timer t = new Timer(3000);
						while(t.isRunning() && Players.getLocal().getAnimation() != 2109){
							Widgets.get(590, 8).getChild(Integer.parseInt(arg.trim())).interact("Jump For Joy");
							Task.sleep(500);
						}
						Task.sleep(3000);
					}

				}

			}else if(command.contains("tele")){
				Teleport.teleportTo(Integer.parseInt(arg.trim()));
			}

		}
	}
}
