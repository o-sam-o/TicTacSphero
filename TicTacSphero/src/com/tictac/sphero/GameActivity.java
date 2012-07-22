package com.tictac.sphero;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.medio.client.android.eventsdk.EventAPI;
import com.tictac.sphero.view.GameView;

public class GameActivity extends Activity {

	private GameView gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
        getWindow().setWindowAnimations(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }
    
    @Override
    protected void onStop()
    {
        // Close Session
        EventAPI.closeSession(this);

        super.onStop();
    }
}
