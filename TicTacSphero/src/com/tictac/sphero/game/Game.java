package com.tictac.sphero.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.Point;

public class Game {
	private Player currentPlayer;
	private Player[][] cell;
	
	public Game() {
		this.restart();
	}
	
	public void restart() {
		currentPlayer = Player.X;
		cell = new Player[3][3];
		for (int x=0; x<3; x++) {
			for (int y=0; y<3; y++) {
				cell[x][y] = Player.NONE;
			}
		}
	}
	
	public void makeMove(int x, int y) {
		// If cell already filled, take no action
		if (cell[x][y] != Player.NONE) {
			return;
		}
		
		cell[x][y] = currentPlayer;
		currentPlayer = currentPlayer.getNextPlayer();
	}

	public boolean isGameOver() {
		return (getWinner() != Player.NONE) || isTie();
	}
	
	public Player get(int x, int y) {
		return cell[x][y];
	}
	
	public Player getWinner() {
		List<Point> winningCells = getWinningCells();
		
		if(winningCells.isEmpty()) {
			return Player.NONE;
		} else {
			Point p = winningCells.get(0);
			return get(p.x, p.y);
		}
	}
	
	public List<Point> getWinningCells(){
		// Vertical
		for (int x=0; x<3; x++) {
			if (isEqualAndNotNone(get(x,0), get(x,1), get(x,2))) {
				return Arrays.asList(new Point(x, 0), new Point(x, 1), new Point(x,2));
			}
		}
		
		// Horizontal
		for (int y=0; y<3; y++) {
			if (isEqualAndNotNone(get(0,y), get(1,y), get(2,y))) {
				return Arrays.asList(new Point(0, y), new Point(1, y), new Point(2,y));
			}
		}
		
		// Diagonal
		if (isEqualAndNotNone(get(0,0), get(1,1), get(2,2))) {
			return Arrays.asList(new Point(0, 0), new Point(1, 1), new Point(2,2));
		}
		if (isEqualAndNotNone(get(0,2), get(1,1), get(2,0))) {
			return Arrays.asList(new Point(0, 2), new Point(1, 1), new Point(2,0));
		}
		
		return new ArrayList<Point>();
	}
	
	public boolean isTie() {
		for (int x=0; x<3; x++) {
			for (int y=0; y<3; y++) {
				if (get(x,y) == Player.NONE) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isEqualAndNotNone(Player p1, Player p2, Player p3) {
		return (p1 == p2) && (p2 == p3) && (p1 != Player.NONE);
	}
}
