package com.tictac.sphero.view;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.medio.client.android.eventsdk.EventAPI;
import com.tictac.sphero.R;
import com.tictac.sphero.game.Game;
import com.tictac.sphero.game.Player;
import com.tictac.sphero.robot.SpheroGrid;

public class GameView extends View {
	private final static int BOARD_SIZE = 3;
	
	private Game game;
	private Paint paint;
	
	private Bitmap xOrbImg;
	private Bitmap yOrbImg;
	
	// HACK: Passing this argument as a global variable
	public static SpheroGrid spheroGrid;
	
	public GameView(Context context) {
		super(context);
		
		game = new Game();
		
		paint = new Paint();
        this.paint.setARGB(255, 0, 0, 0);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(5);
        
        xOrbImg = BitmapFactory.decodeResource(getResources(), R.drawable.x_orb);
        yOrbImg = BitmapFactory.decodeResource(getResources(), R.drawable.y_orb);
	}
	
	// === Draw ===

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("TTS", "On Draw");
  	    drawBoardLines(canvas);
		
  	    for (int x=0; x<3; x++) {
  	    	for (int y=0; y<3; y++) {
  	    		drawOrb(canvas, x, y, game.get(x, y));
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
	
	// === Touch ===
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		// Only process "tap up" events
		if (event.getAction() != MotionEvent.ACTION_UP) {
			return true;	// needed so that ACTION_UP is sent later
		}
		
		// Ignore taps when game is over
		if (game.isGameOver()) {
			return true;
		}
		
		int x = (int) (event.getX() / (this.getWidth() / BOARD_SIZE));
        int y = (int) (event.getY() / (this.getHeight() / BOARD_SIZE));
        Log.d("TTS", String.format("Click on (%s, %s)", x, y));
        
        game.makeMove(x, y);
        this.gameDidChange();
        
        if (game.isGameOver()) {
            showGameOverDialog();
            logMedioGameEnd();
  	    }
        
        super.onTouchEvent(event);
        return true;
    }
	
	private void logMedioGameEnd() {
		if (game.isGameOver()) {
			Map<String, String> stats = new HashMap<String, String>();
			if(game.isTie()) {
				stats.put("tie", "1");
			} else {
				stats.put("winner", game.getWinner().toString());
			}
			EventAPI.logEvent("gameOver", stats);
			EventAPI.flushEvents();
		}
	}
	
	public void showGameOverDialog() {
		Log.i("TTS", "Winner is " + game.getWinner());
		
		String gameOverMessage;
		Player winner = game.getWinner();
		if (winner == Player.X) {
			gameOverMessage = "X wins!";
		} else if (winner == Player.O) {
			gameOverMessage = "O wins!";
		} else {
			gameOverMessage = "Tie!";
		}
		
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.getContext());
		alertBuilder.setMessage(gameOverMessage);
		alertBuilder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// nothing
			}
		});
		AlertDialog dialog = alertBuilder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				game.restart();
				GameView.this.gameDidChange();
			}
		});
		dialog.show();
	}
	
	// === Dependencies ===
	
	private void gameDidChange() {
		this.invalidate();			// repaint self
        this.updateSpheroGrid();
	}
	
	/** Copies the state of Game to SpheroGrid. */
	private void updateSpheroGrid() {
		for (int x=0; x<3; x++) {
  	    	for (int y=0; y<3; y++) {
  	  	    	spheroGrid.set(x, y, game.get(x, y));
  	    	}
  	    }
	}
}
