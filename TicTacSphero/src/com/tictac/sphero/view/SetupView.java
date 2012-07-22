package com.tictac.sphero.view;

import com.tictac.sphero.R;
import com.tictac.sphero.game.Game;
import com.tictac.sphero.game.Player;
import com.tictac.sphero.robot.SpheroGrid;
import com.tictac.sphero.GameActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SetupView extends View {
	private final static int BOARD_SIZE = 3;
	
	private Paint paint;
	
	private Bitmap xOrbImg;
	private Bitmap yOrbImg;
	
	private int blinkingOrbIndex = 0;
	private boolean blinkingOrbOn = false;
	
	private SpheroGrid spheroGrid;
	
	public SetupView(Context context) {
		super(context);
		
		paint = new Paint();
        this.paint.setARGB(255, 0, 0, 0);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(5);
        
        xOrbImg = BitmapFactory.decodeResource(getResources(), R.drawable.x_orb);
        yOrbImg = BitmapFactory.decodeResource(getResources(), R.drawable.y_orb);
	}
	
	public void start(SpheroGrid spheroGrid) {
		this.spheroGrid = spheroGrid;
		
		this.startBlinking();
	}
	
	// === Draw ===

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("TTS", "On Draw");
  	    drawBoardLines(canvas);
		
  	    for (int x=0; x<3; x++) {
  	    	for (int y=0; y<3; y++) {
  	    		drawOrb(canvas, x, y, game_get(x, y));
  	    	}
  	    }
  	    
	    super.onDraw(canvas);
	}
	
	private void drawBoardLines(Canvas canvas) {
		int xs = this.getWidth() / BOARD_SIZE;
		int ys = this.getHeight() / BOARD_SIZE;
		for (int i = 0; i <= BOARD_SIZE; i++) {
			canvas.drawLine(xs * i, 0, xs * i, this.getHeight(), paint);
		}
		for (int i = 0; i <= BOARD_SIZE; i++) {
			canvas.drawLine(0, ys * i, this.getWidth(), ys * i, paint);
		}
	}
	
	private void drawOrb(Canvas canvas, int xCell, int yCell, Player player) {
		if (player == Player.NONE) {
			return;
		}
		
		Log.d("TTS", "draw move (" + xCell + "," + yCell + ") for " + player);
		Point pixelLocation = convertGridToPixelLocation(xCell, yCell);
		canvas.drawBitmap(getPlayerOrbImage(player), pixelLocation.x, pixelLocation.y, new Paint());
	}
	
	private Point convertGridToPixelLocation(int xCell, int yCell) {
		Point point = new Point();

		int cellWidth = (getWidth() / BOARD_SIZE);
		int cellHeight = (getHeight() / BOARD_SIZE);
		
		int imgWidth = xOrbImg.getWidth();
		int imgHeight = xOrbImg.getHeight();
		
		int xOffset = (cellWidth / 2) - (imgWidth / 2);
		int yOffset = (cellHeight / 2) - (imgHeight / 2);
		
		int cZeroX = cellWidth * xCell;
		int cZeroY = cellHeight * yCell;
		
		point.x = cZeroX + xOffset;
		point.y = cZeroY + yOffset;
		return point;
	}
	
	private Bitmap getPlayerOrbImage(Player player) {
		switch(player) {
			case X:
				return xOrbImg;
			case O:
				return yOrbImg;
			default:
				throw new RuntimeException("Unknown player type " + player);
		}
	}
	
	// === Blink ===
	
	private Thread blinkThread;
	private boolean blinkThreadDone;
	
	private void startBlinking() {
		blinkThreadDone = false;
		
		blinkThread = new Thread() {
			public void run() {
				runBlinking();
			}
		};
		blinkThread.setDaemon(true);
		blinkThread.start();
	}
	
	private void runBlinking() {
		while (!blinkThreadDone) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Restart timer
				continue;
			}
			
			blinkingOrbOn = !blinkingOrbOn;
			orb_set(blinkingOrbIndex, blinkingOrbOn);
		}
	}
	
	private void restartBlinking() {
		if(blinkThread != null) {
			blinkThread.interrupt();
		}
	}
	
	private void stopBlinking() {
		blinkThreadDone = true;
		blinkThread.interrupt();
	}
	
	// === Touch ===
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		// Only process "tap up" events
		if (event.getAction() != MotionEvent.ACTION_UP) {
			return true;	// needed so that ACTION_UP is sent later
		}
		
		orb_set(blinkingOrbIndex, false);
		
		blinkingOrbIndex++;
		if (blinkingOrbIndex < 9) {
			blinkingOrbOn = true;
			orb_set(blinkingOrbIndex, true);
			restartBlinking();
		} else {
			stopBlinking();
			
			// HACK: Send data via global variable
			GameView.spheroGrid = spheroGrid;
			
			Intent gotoGameActivity = new Intent(getContext(), GameActivity.class);
			getContext().startActivity(gotoGameActivity);
		}
		
		return true;
	}
	
	// === Model: General ===
	
	private void orb_set(int orbIndex, boolean on) {
		int x = orbIndex % 3;
		int y = orbIndex / 3;
		Player color = on ? Player.X : Player.NONE;
		
		game_set(x, y, color);
		this.postInvalidate();
		if (spheroGrid != null) {
			spheroGrid.set(x, y, color);
		}
	}
	
	// === Model: Game ===
	
	private Player[][] cells;
	
	/*instance*/ {
		cells = new Player[3][3];
		for (int x=0; x<3; x++) {
			for (int y=0; y<3; y++) {
				cells[x][y] = Player.NONE;
			}
		}
	}
	
	private Player game_get(int x, int y) {
		return cells[x][y];
	}
	
	private void game_set(int x, int y, Player color) {
		cells[x][y] = color;
	}
}
