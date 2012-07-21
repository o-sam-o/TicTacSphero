package com.tictac.sphero;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.tictac.sphero.view.GameView;

public class GameActivity extends Activity {

	private GameView gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
}
