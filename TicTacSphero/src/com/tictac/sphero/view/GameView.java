package com.tictac.sphero.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tictac.sphero.R;
import com.tictac.sphero.game.Game;
import com.tictac.sphero.game.Player;

public class GameView extends View {

	public final static int BOARD_SIZE = 3;
	
	private Game game;
	private Paint paint;
	
	private Bitmap xOrbImg;
	private Bitmap yOrbImg;
	
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

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("TTS", "On Draw");
  	    drawBoardLines(canvas);
		
  	    for (int x=0; x<3; x++) {
  	    	for (int y=0; y<3; y++) {
  	    		drawMove(canvas, x, y, game.get(x, y));
  	  	    	updateSpheroGrid(x, y, game.get(x, y));
  	    	}
  	    }

  	    if(game.isGameOver()) {
  	    	handleGameOver();
  	    }
  	    
	    super.onDraw(canvas);
	}

	private void updateSpheroGrid(int x, int y, Player player) {
		// TODO Auto-generated method stub
	}

	public void handleGameOver() {
		Log.i("TTS", "Winner is " + game.getWinner());
		
		//TODO handle game over;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
            int xCell = (int) (event.getX() / (this.getWidth() / BOARD_SIZE));
            int yCell = (int) (event.getY() / (this.getHeight() / BOARD_SIZE));

            Log.d("TTS", String.format("Cick on (%s, %s)", xCell, yCell));
            
            game.makeMove(xCell, yCell);

            this.invalidate();
            
            return super.onTouchEvent(event);
    }
	
	private void drawMove(Canvas canvas, int xCell, int yCell, Player player) {
		Log.d("TTS", "draw move (" + xCell + "," + yCell + ") for " + player);
		Point imgLocation = getImgLocationFor(xCell, yCell);
		canvas.drawBitmap(getPlayerOrb(player), imgLocation.x, imgLocation.y, new Paint());
	}
	
	private Point getImgLocationFor(int xCell, int yCell) {
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
	
	private Bitmap getPlayerOrb(Player player) {
		switch(player) {
			case X:
				return xOrbImg;
			case O:
				return yOrbImg;
			default:
				throw new RuntimeException("Unknown player type " + player);
		}
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
	
}
