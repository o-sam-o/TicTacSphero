package com.tictac.sphero;

import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tictac.sphero.view.SetupView;

public class SetupActivity extends Activity {
	
	private final static int STARTUP_ACTIVITY = 0;
	
	private SetupView setupView;
	private Robot mRobot;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = new Intent(this, StartupActivity.class);  
        startActivityForResult(i, STARTUP_ACTIVITY);  
        
        setupView = new SetupView(this);
        setContentView(setupView);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == STARTUP_ACTIVITY && resultCode == RESULT_OK) {
			// Get the connected Robot
			final String robot_id = data
					.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
			if (robot_id != null && !robot_id.equals("")) {
				mRobot = RobotProvider.getDefaultProvider().findRobot(robot_id);
			}
		}
		blink(false);
	}
	
	private void blink(final boolean lit) {
		if (mRobot != null) {
			// If not lit, send command to show blue light, or else, send
			// command to show no light
			if (lit) {
				RGBLEDOutputCommand.sendCommand(mRobot, 0, 0, 0); // 1
			} else {
				RGBLEDOutputCommand.sendCommand(mRobot, 0, 0, 255); // 2
			}

			// Send delayed message on a handler to run blink again
			final Handler handler = new Handler(); // 3
			handler.postDelayed(new Runnable() {
				public void run() {
					blink(!lit);
				}
			}, 1000);
		}
	}
    
}
