package softclayfactory.jobs;


import java.awt.Polygon;
import java.util.Arrays;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
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


import softclayfactory.core.Utilities;
import softclayfactory.variables.Constants;
import softclayfactory.variables.Progress;

public class Mining extends Node{
	private final int[] clayRocks = {10579, 10577, 10578};
	private final Tile[] bestTiles = { 
			new Tile(3083, 3399, 0),
			new Tile(3082, 3400, 0),
			new Tile(3081, 3399, 0)};

	private Tile currRockTile;
	private SceneObject clayRock;

	private int min = 5000, max = 40000;
	private Timer patienceTimer = new Timer(Random.nextInt(min, max));

	@Override
	public boolean activate(){

		return Players.getLocal() != null &&
				!Inventory.isFull() && 
				!Players.getLocal().isInCombat();

	}

	public void execute() {
		// TODO Auto-generated method stub
		try{
			if(Constants.barbarianClayMiningArea.contains(Players.getLocal())){
				clayRock = SceneEntities.getNearest(clayRocks);
				if(clayRock != null){
					if(clayRock.isOnScreen()){
						boolean didWalk = false;
						Utilities.showDebug("Time to move to better tile: " + patienceTimer.toRemainingString());
						if(!patienceTimer.isRunning() || !Arrays.asList(bestTiles).contains(Players.getLocal().getLocation())){
							Utilities.showDebug("walking to best tile");
							didWalk = doWalkToBestTile();
							patienceTimer.setEndIn(Random.nextInt(min, max));
						}

						if(!didWalk){
							Utilities.showDebug("clay distance: " + clayRock.getLocation().distance(Players.getLocal().getLocation()));
							if(clayRock.getLocation().distance(Players.getLocal().getLocation()) == 1.0){
								if(isSomeoneOnTile(Players.getLocal().getLocation())){
									Utilities.showDebug("Competing to mine a rock");
									int invCount = Inventory.getCount();
									Timer t = new Timer(10000);
									Tile tileToSpamClick = clayRock.getLocation();
									while(t.isRunning() && invCount == Inventory.getCount() && !Players.getLocal().isMoving()){
										doInteract(SceneEntities.getAt(tileToSpamClick));
										Task.sleep(100, 400);
									}
								}else if(doInteract(clayRock)){
									Utilities.showDebug("Mining clay for " + Progress.profitMining + "gp each.");
									if(currRockTile != null) SceneEntities.getAt(currRockTile).hover();
									currRockTile = clayRock.getLocation();
									Timer t = new Timer(3000);
									while(t.isRunning() && (rockIsValid() || Players.getLocal().isMoving())){
										Task.sleep(100);
									}
								}
							}


						}


					}else{
						Utilities.showDebug("Found clay but not on screen. Searching...");
					}
				}else{
					Utilities.showDebug("Did not find clay. Searching...");
				}
			}else{

				Utilities.showDebug("Walking to clay mining spot.");

				Walking.newTilePath(Constants.clayPath).traverse();
				Task.sleep(400, 900);
			}

		} catch (Exception e) {
			Utilities.showDebug("Error:" + e);
		}
	}

	private boolean doWalkToBestTile() {
		Tile tile = nearestTileWithLeastPeople(bestTiles);
		if(!Players.getLocal().getLocation().equals(tile)){
			tile.interact("Walk here");
			Task.sleep(1500);
			Timer t = new Timer(2000);
			while(t.isRunning() && Players.getLocal().isMoving()){
				Task.sleep(100);
			}
			return true;
		}

		return false;
	}

	private Tile nearestTileWithLeastPeople(final Tile[] tiles){
		Tile tile = null;
		for(Tile t: sortNeareastTiles(tiles)){
			if(tile == null || getPlayersAt(tile).length > getPlayersAt(t).length){
				tile = t;
			}
		}
		return tile;

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

	private boolean isSomeoneOnTile(final Tile t){
		Utilities.showDebug(getPlayersAt(t).length + " players on tile: " + t.toString());
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
