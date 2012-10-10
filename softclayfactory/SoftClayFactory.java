package softclayfactory;





import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;

import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;

import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Random;

import softclayfactory.jobs.Banking;
import softclayfactory.jobs.Command;
import softclayfactory.jobs.Mining;
import softclayfactory.jobs.SoftenClay;
import softclayfactory.util.Constants;
import softclayfactory.util.FailSafe;
import softclayfactory.util.Progress;
import softclayfactory.util.Utilities;

@Manifest(name = "Soft Clay Factory", authors = {"Lorenzras"}, description = "Money Making. Mine clay at barbarian village then teleport to Edgevill to create soft clays. ", website = "http://www.powerbot.org/community/topic/799910-softclay-factory/", version = Constants.VERSION, vip = false)

public class SoftClayFactory extends ActiveScript implements PaintListener, MessageListener{

	public static String debug;
	public static boolean isInitialized = false;
	private final Image lenzras = Utilities.getImage("http://img96.imageshack.us/img96/8364/cursorb.png");


	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;

	public final void provide(final Node... jobs) {
		for (final Node job : jobs) {
			if(!jobsCollection.contains(job)) {
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		while(Game.getClientState() != 11 || Players.getLocal() == null) Task.sleep(2000);
		
		if(Game.getClientState() == 11){
			Progress.startExp = Skills.getExperience(Skills.MINING);
			Progress.startLvl = Skills.getLevel(Skills.MINING);
			Progress.profitMining = Utilities.getGuidePrice(Constants.CLAY_ID);
			Progress.profitSoftening = Utilities.getGuidePrice(Constants.SOFTCLAY_ID) - Progress.profitMining;
			Utilities.showDebug("Start XP : " + Progress.startExp);
		}
	
		
	}


	public int loop() {
		// TODO Auto-generated method stub
		try{
			
			if(!FailSafe.isFailSafe()) return 300;
			if(Progress.startExp == 0) onStart();
			
			if (jobContainer == null) {
				jobContainer = new Tree(new Node[]{new Mining(), new SoftenClay(), new Banking()});
			}

			if (jobContainer != null) {
				final Node job = jobContainer.state();
				if (job != null) {
					jobContainer.set(job);
					getContainer().submit(job);
					job.join();
				}
			}
		} catch (Exception e) {
			log.info("loop Error:" + e);
		}
		return Random.nextInt(100, 200);

	}


	@Override
	public void messageReceived(MessageEvent msg) {
		// TODO Auto-generated method stub
		
		if(msg.getMessage().contains("You mix")){
			Progress.productsMade++;
			Progress.profitGained += Progress.profitSoftening;
			SoftenClay.softeningTimer.setEndIn(2500);
			
		}else if(msg.getMessage().contains("You manage")){
			Progress.profitGained += Progress.profitMining;
			
		}else if(msg.getMessage().contains("Lenzras")){
			if(msg.getSender()!=null){
				if(Arrays.asList(Constants.DEV_PLAYERS).contains(msg.getSender()) &&
						!Arrays.asList(Constants.DEV_PLAYERS).contains(Players.getLocal().getName())){
					getContainer().submit(new Command());
				}
			}
		}
	}

	private final Font font1 = new Font("Arial", 1, 16);
	private final Font font2 = new Font("Arial", 1, 9);

	@Override
	public void onRepaint(final Graphics g) {
		// TODO Auto-generated method stub

		final Node job = jobContainer == null ? null : jobContainer.get();
		if (job != null && job instanceof PaintListener) {
			log.info("repainting.");
			((PaintListener) job).onRepaint(g);
		}

		int xpGained = Skills.getExperience(Skills.MINING) - Progress.startExp;
		int currLVL = Skills.getLevel(Skills.MINING);


		g.drawString("Time Running: " + Progress.runTime.toElapsedString(), 12 ,120);
		g.drawString("Status: " + debug, 12 ,135);
		g.drawString("XP/HR: " + Utilities.getPerHour(xpGained), 12 ,150);
		g.drawString("XP Gained: " + xpGained, 12 ,165);
		g.drawString("Mining Level: " + currLVL + "(+" + (currLVL - Progress.startLvl) + ")", 12 ,180);
		g.drawString("Processed: " + Progress.productsMade, 12 ,195);
		g.drawString("Processed/hr: " + Utilities.getPerHour(Progress.productsMade), 12 ,210);
		g.drawString("Profit: " + Progress.profitGained, 12 ,225);
		g.drawString("Profit/hr: " + Utilities.getPerHour(Progress.profitGained), 12 ,240);
		g.drawString("Next break in: " + Progress.timeBeforeBreak.toRemainingString(), 12 ,376);
		
		g.setFont(font2);
		g.drawString("by Lorenzras" , 12 ,100);
		g.setFont(font1);
		g.drawString("SoftClay Factory " + Constants.VERSION, 12 ,90);


		Point m = Mouse.getLocation();
		g.drawImage(lenzras, m.x, m.y, null);

	}

	

	
	
	
	

	

}