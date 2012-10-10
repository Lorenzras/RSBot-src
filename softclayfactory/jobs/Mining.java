package softclayfactory.jobs;


import java.awt.Polygon;
import java.util.Arrays;



import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.SceneObject;

import softclayfactory.util.Constants;
import softclayfactory.util.FailSafe;
import softclayfactory.util.Progress;
import softclayfactory.util.Utilities;

public class Mining extends Node{
	public final int[] clayRocks = {10579, 10577, 10578};
	public final Tile[] bestTiles = { 
			new Tile(3083, 3399, 0),
			new Tile(3082, 3400, 0),
			new Tile(3081, 3399, 0)};

	Tile currRockTile;
	SceneObject clayRock;

	@Override
	public boolean activate(){

		return Game.isLoggedIn() &&
				!Inventory.isFull() && 
				!Players.getLocal().isInCombat();

	}

	public void execute() {
		// TODO Auto-generated method stub
		clayRock = SceneEntities.getNearest(clayRocks);

		try{
			if(clayRock != null){
				if(clayRock.isOnScreen() && clayRock.getLocation().distance(Players.getLocal()) < 6){
					if(isPlayerOnGoodTile() || Players.getLocal().isMoving()){
						if(doInteract(clayRock)){
							Utilities.showDebug("Mining clay for " + Progress.profitMining + "gp each.");
							if(currRockTile != null) SceneEntities.getAt(currRockTile).hover();
							currRockTile = clayRock.getLocation();
							Timer t = new Timer(4000);
							while(t.isRunning() && rockIsValid()){
								Task.sleep(100);
							}
						}else{
							currRockTile = null;
						}
					}else{
						Utilities.showDebug("Competing to mine a rock");
						int invCount = Inventory.getCount();
						Tile tileToSpamClick = clayRock.getLocation();
						while(invCount == Inventory.getCount()){
							doInteract(SceneEntities.getAt(tileToSpamClick));
							Task.sleep(100, 400);
						}
						
						walkToBestTile(Random.nextInt(0, 4));
					}

					FailSafe.doMoveCamera(30000);
				
				}else{
					Utilities.showDebug("Walking to clay rock.");
					Walking.walk(Walking.findPath(clayRock).getEnd());
					FailSafe.doMoveCamera(1000);
					Task.sleep(900, 1600);
				}

			}else{
				FailSafe.doMoveCamera(3000);
				Utilities.showDebug("Walking to clay mining spot.");
				Walking.newTilePath(Constants.clayPath).traverse();
				Task.sleep(400, 900);
			}

		} catch (Exception e) {
			Utilities.showDebug("Error:" + e);
		}
	}



	private boolean walkToBestTile(final int chance) {
		int trigger = 0;
		if(chance == trigger){
			for(Tile t: sortNeareastTiles(bestTiles)){
				if(!isSomeoneOnTile(t)){
					if(!t.equals(Players.getLocal().getLocation())) {
						Utilities.showDebug("walking to a better tile.");
						t.interact("Walk here");
						Task.sleep(1000);
						Timer w = new Timer(4000);
						while(w.isRunning() && Players.getLocal().isMoving()){
							Task.sleep(100);
						}
						currRockTile = null;
	
					}else{
						Utilities.showDebug("Player already on a good tile.");
					}
					return true;
				}
			}
		}
		return false;
	}

	private Tile[] sortNeareastTiles(final Tile[] t){
		boolean swapped;
		Tile tempTile;
		Tile playerLocation = Players.getLocal().getLocation();

		do{
			swapped = false;
			for(int i = 1; i < t.length; i++){
				if(t[i-1].distance(playerLocation) > t[i].distance(playerLocation)){
					tempTile = t[i-1];
					t[i-1] = t[i];
					t[i] = tempTile;
					swapped = true;
				}
			}
		}while(swapped);

		return t;
	}
	
	private boolean isPlayerOnGoodTile(){
		
		return !isSomeoneOnTile(Players.getLocal().getLocation()) &&
				Arrays.asList(bestTiles).contains(Players.getLocal().getLocation());
	}
	
	private boolean isSomeoneOnTile(final Tile t){
		return getPlayersAt(t).length > 0 ? true:false;
	}

	private Player[] getPlayersAt(final Tile t) {
		return Players.getLoaded(new Filter<Player>() {
			public boolean accept(Player player) {
				return !Players.getLocal().equals(player) &&
						player.getLocation().equals(t);
			}
		});
	}

	private boolean rockIsValid() {
		try{
			if(currRockTile != null) {
				final int ID = SceneEntities.getAt(currRockTile).getId();
				for(int i : clayRocks) {
					if(i == ID) {
						return true;
					}
				}
			}
		} catch (NullPointerException exception) {
		}

		return false;
	}

	private boolean doInteract(SceneObject obj){
		try{
			if(objectContainsMouse(obj)){
				final String[] menuActions = Menu.getActions();
				final String upText = menuActions.length > 0 ? menuActions[0] : "";
				if(upText.contains("Mine")){
					Mouse.click(true);
					return true;
				}
			}

			if(obj.interact("Mine")){
				return true;
			}
		} catch (NullPointerException exception) {
		}


		return false;
	}


	private boolean objectContainsMouse(SceneObject o) {
		try{
			assert o != null;
			final Polygon[] polygons = o.getBounds();   
			for (Polygon p : polygons) {
				if (p.contains(Mouse.getLocation())) {
					return true;
				}
			}
		} catch (NullPointerException exception) {
		}

		return false;
	}

}
