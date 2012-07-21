package com.tictac.sphero.game;

public enum Player {
	NONE, X, O;
	
	public Player getNextPlayer() {
		switch (this) {
			case X:
				return Player.O;
			case O:
				return Player.X;
			default:
				return Player.NONE;
		}
	}
}
