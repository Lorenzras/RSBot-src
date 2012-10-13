package softclayfactory.jobs;


import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.SceneObject;

import softclayfactory.methods.Teleport;
import softclayfactory.util.Constants;
import softclayfactory.util.Progress;
import softclayfactory.util.Utilities;

public class SoftenClay extends Node{
	
	public static Timer softeningTimer = new Timer(1500);
	
	public boolean activate(){
		try{
			if(Players.getLocal() == null) return false;
			
			if(Inventory.getCount(Constants.CLAY_ID) == 0){
				return false;
			}
	
			return Inventory.isFull() &&
					!Players.getLocal().isInCombat() &&
					!softeningTimer.isRunning();
		}catch(Exception e){
			Utilities.showDebug("SoftenClay-Activate: " + e.getLocalizedMessage());
		}
		
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try{
			SceneObject well = SceneEntities.getNearest(Constants.WELL_ID);
			Timer wait = new Timer(1000);
			if(!Constants.edgevillArea.contains(Players.getLocal())) {
				Teleport.teleportTo(Constants.EDGEVILL);
			}
	
			if(well != null){
				if(well.isOnScreen()){
					if(Bank.isOpen()) Bank.close();
					
					if(Widgets.get(905, 14).validate()){
						boolean isInteracted = false;
						if(Widgets.get(905, 14).getBoundingRectangle().contains(Mouse.getLocation())){
							Mouse.click(true);
							isInteracted = true;
						}else{
							Widgets.get(905, 14).interact("Make All");
							isInteracted = true;
						}
						Task.sleep(1000);
						if(isInteracted){
							Utilities.showDebug("Softening clays for profit of " + Progress.profitSoftening + "gp each.");
							softeningTimer.setEndIn(2500);
						}
	
					}else{
						if(!softeningTimer.isRunning()){
							if(Inventory.selectItem(Constants.CLAY_ID)){
								if(well.interact("Use")){
									Utilities.showDebug("Interacting well.");
									Mouse.move(271,475,5,5);
								}
							}
							wait.setEndIn(2000);
							while(wait.isRunning() && !Widgets.get(905, 14).validate()){
								if(Players.getLocal().isMoving()) wait.setEndIn(1000);
								Task.sleep(100);
							}
						}
					}
				}else{
					Utilities.showDebug("Found well but not on screen. Walking..");
	
					Walking.walk(Walking.findPath(well).getEnd());
					wait.setEndIn(3000);
					while(wait.isRunning() && !well.isOnScreen()){
						Task.sleep(300);
					}
				}
			}
		}catch(Exception e){};

	}

}