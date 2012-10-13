package softclayfactory.methods;

import org.powerbot.core.script.job.Task;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;


import softclayfactory.SoftClayFactory;
import softclayfactory.core.Utilities;

public class Teleport extends SoftClayFactory{
	public  final static int EDGEVILL = 45;
	private final static Area edgevillArea = new Area(new Tile[] {
			new Tile(3062, 3510, 0), new Tile(3103, 3509, 0),
			new Tile(3102, 3487, 0), new Tile(3063, 3487, 0)
	});
	
	public static void teleportTo(int whereID) {
		if(Players.getLocal().getAnimation() == 16385) return;
		Timer wait = new Timer(1200);
		if(Tabs.MAGIC.open()){
			Utilities.showDebug("Magic tab is now open.");
			if(Widgets.get(192, 24).validate()){
				if(Widgets.get(192, 24).interact("Cast")){
					Utilities.showDebug("Casting home teleport.");

					wait.setEndIn(1500);
					while(wait.isRunning() && !Widgets.get(1092, whereID).validate()){
						Task.sleep(200);
					}

					if(Widgets.get(1092, whereID).validate()){
						if(Widgets.get(1092, whereID).interact("Teleport")){
							Utilities.showDebug("Teleporting to " + whereID);
							Task.sleep(1000);

							wait.setEndIn(1500);
							while(wait.isRunning() && !edgevillArea.contains(Players.getLocal())){
								if(Players.getLocal().getAnimation() == 16385){
									wait.setEndIn(1200);
								}
								Task.sleep(200);
							}

							if(edgevillArea.contains(Players.getLocal())){
								Utilities.showDebug("Teleport done.");
							}
						}
					}
				}
			}
		}
	}
}
