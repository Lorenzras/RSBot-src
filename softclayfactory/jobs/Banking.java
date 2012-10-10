package softclayfactory.jobs;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

import softclayfactory.util.Constants;
import softclayfactory.util.Progress;
import softclayfactory.util.Utilities;

public class Banking extends Node {

	public boolean activate(){
		return Inventory.isFull() && 
				Constants.edgevillArea.contains(Players.getLocal()) &&
				Inventory.getCount(Constants.CLAY_ID) == 0;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Timer wait = new Timer(1500);

		SceneObject booth = SceneEntities.getNearest(Constants.BOOTH_ID);
		if(booth != null){
			if(booth.isOnScreen()){
				if(Bank.isOpen()){
					if(Inventory.getCount(Constants.pickAxes) == 0){
						Utilities.showDebug("Deposit All.");
						Bank.depositInventory();
						wait.setEndIn(2000);
						while(wait.isRunning() && Inventory.isFull()){
							Task.sleep(200);
						}

					}else{
						depositAllExcept(Constants.pickAxes);
					}
					Progress.softclaysInBank = Bank.getItemCount(true, Constants.SOFTCLAY_ID);
					Utilities.showDebug("Player: " + Players.getLocal().getName() + " has " + Progress.softclaysInBank + " softclays in bank. ");
					if(Bank.getItemCount(true, Constants.CLAY_ID) >= 28){
						Utilities.showDebug("Found " + Bank.getItemCount(true, Constants.CLAY_ID) + " clays in bank. Withdrawing.");
						Bank.withdraw(Constants.CLAY_ID, 0);

					}
				}else{
					Utilities.showDebug("Opening bank.");
					if(Bank.open()){
						Timer t = new Timer(1500);
						while(t.isRunning() && !Bank.isOpen()){
							if(Players.getLocal().isMoving()) t.setEndIn(1000);
						}
					}
				}
			}else{
				Utilities.showDebug("Bank found but not on screen. Walking..");
				Walking.walk(Walking.findPath(booth).getEnd());
				wait.setEndIn(2000);
				while(wait.isRunning() && !booth.isOnScreen()){
					Task.sleep(300);
				}
			}
		}
	}

	private boolean  depositAllExcept(int[] item_Ids){
		boolean isDepositedItem = false;
		Timer depositWait = new Timer(1500);
		boolean isDeposit;
		for(Item i: Inventory.getItems()){

			isDeposit = true;
			if(Inventory.getCount(i.getId()) > 0){
				for(int x: item_Ids){
					if(x == i.getId()) isDeposit = false;
					break;
				}

				if(isDeposit){
					Utilities.showDebug("Depositing " + i.getName() + ".");
					Bank.deposit(i.getId(), 0);
					Task.sleep(900);
					depositWait.setEndIn(2500);
					while(depositWait.isRunning() && Inventory.getCount(i.getId()) != 0 ){
						Task.sleep(100);
					}
					isDepositedItem = true;
				}
			}
		}
		return isDepositedItem;
	}
}
