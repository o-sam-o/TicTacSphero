package com.tictac.sphero;

import java.util.ArrayList;

import orbotix.robot.app.MultipleRobotStartupActivity;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == STARTUP_ACTIVITY && resultCode == RESULT_OK) {
			grid = new SpheroGrid(RobotProvider.getDefaultProvider().getControlledRobots());
		} else {
			grid = new SpheroGrid(new ArrayList<Robot>());
			Toast.makeText(this, "No Spheros connected!", 5).show();
		}
		setupView.start(grid);
	}
}
