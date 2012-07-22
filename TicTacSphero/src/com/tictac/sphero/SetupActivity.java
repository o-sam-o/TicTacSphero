package com.tictac.sphero;

import orbotix.robot.app.MultipleRobotStartupActivity;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.tictac.sphero.game.Player;
import com.tictac.sphero.robot.SpheroGrid;
import com.tictac.sphero.view.SetupView;

public class SetupActivity extends Activity {
	
	private final static int STARTUP_ACTIVITY = 0;
	
	private Player start = Player.O;
	
	private SetupView setupView;
	private SpheroGrid grid;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = new Intent(this, MultipleRobotStartupActivity.class);  
        startActivityForResult(i, STARTUP_ACTIVITY);  
        
        setupView = new SetupView(this);
        setContentView(setupView);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == STARTUP_ACTIVITY && resultCode == RESULT_OK) {
			grid = new SpheroGrid(RobotProvider.getDefaultProvider().getControlledRobots());
		} else {
			Toast.makeText(this, "Robot connect fail", 300);
		}
		blink();
	}
	
	private void blink() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				grid.set(x, y, flip(grid.get(x, y)));
			}
		}
			// Send delayed message on a handler to run blink again
			final Handler handler = new Handler(); // 3
			handler.postDelayed(new Runnable() {
				public void run() {
					blink();
				}
			}, 1000);
	}
    
	private Player flip(Player player) {
		switch (player) {
		case X:
			return Player.O;
		case O:
			return Player.X;
		default:
			start = flip(start);
			return start;
		}
	}
	
}
