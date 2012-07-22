package com.tictac.sphero.robot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import orbotix.macro.MacroCommandCreationException;
import orbotix.macro.MacroObject;
import orbotix.macro.MacroObject.MacroObjectMode;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;

import android.content.Context;
import android.util.Log;

import com.tictac.sphero.game.Player;

public class SpheroGrid {

	private List<Player> robotColors;
	private List<Robot> robots;
	
	private MacroObject victoryDanceMacro;
	
	// === Init ===
	
	public SpheroGrid(Context context, List<Robot> robots) {
		this.robots = robots;
		robotColors = new ArrayList<Player>();
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				robotColors.add(Player.NONE);
				set(x, y, Player.NONE);
			}
		}
		
		loadMacros(context);
	}
	
	private void loadMacros(Context context) {
		try {
			this.victoryDanceMacro = loadMacro(context, "Victory dance.sphero");
		} catch (Exception e) {
			// HACK: Error handling would be nice...
			e.printStackTrace();
		}
	}
	
	private MacroObject loadMacro(Context context, String filename) throws IOException, MacroCommandCreationException {
		InputStream is = context.getAssets().open("macros/" + filename);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int next;
		while ((next = is.read()) > -1){
			bos.write(next);
		}
		
		return new MacroObject(bos.toByteArray());
	}
	
	// === Operations ===
	
	public int getSpherosConnected(){
		return robots.size();
	}
	
	public void set(int x, int y, Player player) {
		robotColors.set(getIndex(x, y), player);
		
		Robot robot = tryGetRobot(x, y);
		if (robot == null) {
			return;
		}
		
		switch (player) {
		case NONE:
			RGBLEDOutputCommand.sendCommand(robot, 0, 0, 0);
			break;
	    case O:
			RGBLEDOutputCommand.sendCommand(robot, 0, 255, 0);
	    	break;
	    case X:
			RGBLEDOutputCommand.sendCommand(robot, 0, 0, 255);
	    	break;
		default:
			throw new RuntimeException("Unknown player " + player);
		}
	}
	
	public Player get(int x, int y) {
		return robotColors.get(getIndex(x, y));
	}
	
	public void startDance(int x, int y) {
		Robot robot = tryGetRobot(x, y);
		if (robot == null) {
			return;
		}
		
		if (victoryDanceMacro != null) {
			victoryDanceMacro.setMode(MacroObjectMode.Normal);
			victoryDanceMacro.setRobot(robot);
			victoryDanceMacro.playMacro();
		}
	}
	
	// === Utilities ===
	
	private Robot tryGetRobot(int x, int y) {
		if (!(getIndex(x, y) < robots.size())) {
			Log.w("TTS", "Not enough Spheros! Unable to update (" + x + ", " + y + ")");
			return null;
		}
		
		return robots.get(getIndex(x, y));
	}
	
	private int getIndex(int x, int y) {
		return x + y*3;
	}
}
