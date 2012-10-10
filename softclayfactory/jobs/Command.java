package softclayfactory.jobs;

import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.input.Keyboard;

import softclayfactory.util.Progress;

public class Command extends Node{

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Keyboard.sendText("I have " + Progress.softclaysInBank + " soft clays in bank.", true);
		Keyboard.sendText("I have been playing for " + Progress.runTime.toElapsedString() + ".", true);
	}

}
