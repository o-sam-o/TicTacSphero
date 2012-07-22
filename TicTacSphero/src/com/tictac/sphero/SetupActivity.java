package com.tictac.sphero;

import orbotix.robot.app.MultipleRobotStartupActivity;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.tictac.sphero.game.Player;
import com.tictac.sphero.robot.SpheroGrid;
import com.tictac.sphero.view.SetupView;

public class SetupActivity extends Activity {
	
	private final static int STARTUP_ACTIVITY = 0;
	
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
			setupView.start(grid);
		} else {
			Toast.makeText(this, "Robot connect fail", 300);
		}
	}
}
