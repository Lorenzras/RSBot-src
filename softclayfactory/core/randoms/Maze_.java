package softclayfactory.core.randoms;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;

import softclayfactory.core.Utilities;



public class Maze_ {
	public static boolean isTrapped(){
		return Widgets.get(209, 2).validate();
	}

	public static void solve(){
		Utilities.showDebug("Percent left: " + getPercentLeft());
		if(getPercentLeft() == 0){
			final NPC mysteriousMan = NPCs.getNearest(8632);
			if(mysteriousMan != null){
				if(mysteriousMan.isOnScreen()){
					Utilities.showDebug("Found mysterious man. Talking.. ");
					if(mysteriousMan.interact("Talk-to")){
						Timer t = new Timer(3000);
						while(t.isRunning() && !Widgets.get(1184).validate()){
							Task.sleep(100);
						}
						
						if(Widgets.get(1184,13).validate()){
							Utilities.showDebug("Mysterious old man");
							Keyboard.sendKey(' ');
							Task.sleep(2000);
							
							if(Widgets.get(1188, 30).validate()){
								Utilities.showDebug("Choosing answer.");
								Keyboard.sendKey('3');							
								Task.sleep(2000);
								
								if(Widgets.get(1191,17).validate()){
									Utilities.showDebug(Widgets.get(1191,17).getText());
									Keyboard.sendKey(' ');								
									Task.sleep(2000);
									
									if(Widgets.get(1184,13).validate()){
										Utilities.showDebug("Mysterious old man");
										Keyboard.sendKey(' ');									
										Task.sleep(2000);
										
										if(Widgets.get(1184,13).validate()){
											Utilities.showDebug("Mysterious old man");
											Keyboard.sendKey(' ');										
											Task.sleep(2000);
											
											if(Widgets.get(1188,25).validate()){
												Utilities.showDebug("Choosing answer.");
												Keyboard.sendKey('2');											
												Task.sleep(2000);
												
												if(Widgets.get(1191,17).validate()){
													Utilities.showDebug(Widgets.get(1191,17).getText());
													Keyboard.sendKey(' ');								
													Task.sleep(2000);
												}
											}
										}
									}
								}
							}
						}
						
						
	
	
						
	
	
					}
				}
			}
		}else{
			Utilities.showDebug("waiting...");
			Camera.setAngle(Random.nextInt(0, 360));
			Task.sleep(3000, 5000);
		}
	}
	private static int getPercentLeft(){
		int percentLeft = 99;
		if(Widgets.get(209, 4).validate()){
			percentLeft = Integer.parseInt(Widgets.get(209, 4).getText().substring(0, 1).trim());
		}
		return percentLeft;
	}

}
