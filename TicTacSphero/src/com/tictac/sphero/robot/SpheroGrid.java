package com.tictac.sphero.robot;

import java.util.ArrayList;
import java.util.List;

import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;

import android.util.Log;

import com.tictac.sphero.game.Player;

public class SpheroGrid {

	private List<Player> robotColors;
	private List<Robot> robots;
	
	public SpheroGrid(List<Robot> robots) {
		this.robots = robots;
		robotColors = new ArrayList<Player>();
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				robotColors.add(Player.NONE);
				set(x, y, Player.NONE);
			}
		}
	}
	
	public void set(int x, int y, Player player) {
		robotColors.set(getIndex(x, y), player);
		
		if(robots.size() < getIndex(x, y) + 1) {
			Log.w("TTS", "Not enough Spheros! Unable to update (" + x + ", " + y + ")");
			return;
		}
		
		Robot robot = robots.get(getIndex(x, y));
		switch (player) {
		case NONE:
			RGBLEDOutputCommand.sendCommand(robot, 0, 0, 0);
			break;
	    case O:
			RGBLEDOutputCommand.sendCommand(robot, 255, 0, 0);
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
	
	private int getIndex(int x, int y) {
		int xCoefficient = 0;
		if(x == 1) {
			xCoefficient = 3;
		} else if (x == 2) {
			xCoefficient = 6;
		}
		
		return xCoefficient + y;
	}
	
}
