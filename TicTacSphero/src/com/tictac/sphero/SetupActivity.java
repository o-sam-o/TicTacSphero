package com.tictac.sphero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import orbotix.robot.app.MultipleRobotStartupActivity;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.medio.client.android.eventsdk.EventAPI;
import com.tictac.sphero.robot.SpheroGrid;
import com.tictac.sphero.view.SetupView;

public class SetupActivity extends Activity {
	
	private static final String MEDIO_API_KEY = "0158483b1d1d0b2804181a190607020f0536046c1e0e0d005c173d4e1f2c271040041301474f1c07213b3e4106656f6c7b70667b7b7b246443542222426c7b6c13747f467c";
	private static final String MEDIO_API_URL = "http://hackathon.ingenius.medio.com:8080/events/v1";
	
	private final static int STARTUP_ACTIVITY = 0;
	
	private SetupView setupView;
	private SpheroGrid grid;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent i = new Intent(this, MultipleRobotStartupActivity.class);  
        startActivityForResult(i, STARTUP_ACTIVITY);
        
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setMessage("Each time a Sphero blinks, move it into the appropriate position on the grid. When all 9 spheros are in place, the game will begin.");
		alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// nothing
			}
		});
		AlertDialog dialog = alertBuilder.create();
		dialog.show();
        
        setupView = new SetupView(this);
        setContentView(setupView);
        
        EventAPI.configure(this.getApplicationContext(), MEDIO_API_URL, MEDIO_API_KEY);
    }
	
    @Override
    protected void onStart()
    {
        super.onStart();

        // Open Session
        EventAPI.openSession(this);

        // Log an event with no key-value pairs
        EventAPI.logEvent("onStart", null);
    }
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == STARTUP_ACTIVITY && resultCode == RESULT_OK) {
			grid = new SpheroGrid(RobotProvider.getDefaultProvider().getControlledRobots());
		} else {
			grid = new SpheroGrid(new ArrayList<Robot>());
			Toast.makeText(this, "No Spheros connected!", 5).show();
		}
		logMedioEvents();
		
		setupView.start(grid);
	}

	private void logMedioEvents() {
		Map<String, String> stats = new HashMap<String, String>();
		stats.put("spherosConnected", "" + grid.getSpherosConnected());
		EventAPI.logEvent("setup", stats);
		EventAPI.flushEvents();
	}
}
