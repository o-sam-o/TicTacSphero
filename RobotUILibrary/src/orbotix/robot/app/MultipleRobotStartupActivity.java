package orbotix.robot.app;

import android.app.Activity;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.orbotix.macrolab.connection.RobotConnectionListView;
import com.orbotix.macrolab.connection.RobotWrapper;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;

/**
 * Gives the user the opportunity to connect to multiple Spheros
 *
 * Created by Orbotix Inc.
 * Date: 4/5/12
 *
 * @author Adam Williams
 */
public class MultipleRobotStartupActivity extends Activity {

    /**
     * ID for starting the RobotAdapterActivity for result
     */
    private final static int sEnableRobotAdapterActivity = 0;

    /**
     * Key for value of whether or not there has ever been a sphero paired to this device
     */
    private static final String sPreferenceNeverPaired = "orbotix.sphero.NEVER_PAIRED";
    private static final String sStartupPreferences = "orbotix.sphero.STARTUP_PREFS";
    
    private static final String sTag = "TTS.MultipleRobotStartupActivity";
    
    private boolean mConnected = false;

    private RobotConnectionListView mRobotListView;
    
    private BroadcastReceiver mRobotDiscoveredReceiver = new RobotDiscoveredReceiver();
    private BroadcastReceiver mRobotConnectedReceiver = new RobotConnectedReceiver();
    private BroadcastReceiver mRobotConnectFailedReceiver = new RobotConnectFailedReceiver();
    private BroadcastReceiver mNonePairedReceiver = new NonePairedReceiver();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.multiple_robot_startup);
        
        
        mRobotListView = (RobotConnectionListView)findViewById(R.id.robot_list);
        mRobotListView.setOnItemClickListener(new RobotListViewItemClickListener());
        
    }


    @Override
    protected void onStart() {
        super.onStart();

        RobotProvider provider = RobotProvider.getDefaultProvider();

        if(provider.isAdapterEnabled()){

            //Start connection
            if (provider.isAdapterEnabled()) {
                startConnection();
            } else {
                Intent intent = provider.getAdapterIntent();
                startActivityForResult(intent, sEnableRobotAdapterActivity);
            }
            
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        if(mConnected){
            
            try{
                unregisterReceiver(mRobotDiscoveredReceiver);
            }catch (RuntimeException e){
                Log.e(sTag, "Failed to unregister receiver. Likely not registered.", e);
            }

            try{
                unregisterReceiver(mRobotConnectedReceiver);
            }catch (RuntimeException e){
                Log.e(sTag, "Failed to unregister receiver. Likely not registered.", e);
            }

            try{
                unregisterReceiver(mRobotConnectFailedReceiver);
            }catch (RuntimeException e){
                Log.e(sTag, "Failed to unregister receiver. Likely not registered.", e);
            }

            try{
                unregisterReceiver(mNonePairedReceiver);
            }catch (RuntimeException e){
                Log.e(sTag, "Failed to unregister receiver. Likely not registered.", e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == sEnableRobotAdapterActivity){
            if(resultCode == RESULT_OK){
                
                startConnection();
            }else {
                finish();
            }
        }
    }
    
    public void onSettingsClicked(View v){
        Intent settings_intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(settings_intent);
        finish();
    }
    
    public void onBuyClicked(View v){
        String url = "http://store.gosphero.com";
        Intent buySpheroIntent = new Intent(Intent.ACTION_VIEW);
        buySpheroIntent.setData(Uri.parse(url));
        startActivity(buySpheroIntent);
    }
    
    public void onDoneClicked(View v){
        finish();
    }

    private void startConnection(){
        
        if(mConnected){
            return;
        }
        mConnected = true;
        
        RobotProvider provider = RobotProvider.getDefaultProvider();
        
        provider.setBroadcastContext(this);

        //Discovery receiver
        registerReceiver(mRobotDiscoveredReceiver, new IntentFilter(Robot.ACTION_FOUND));
        registerReceiver(mRobotConnectedReceiver, new IntentFilter(RobotProvider.ACTION_ROBOT_CONNECT_SUCCESS));
        registerReceiver(mRobotConnectFailedReceiver, new IntentFilter(RobotProvider.ACTION_ROBOT_CONNECT_FAILED));
        registerReceiver(mNonePairedReceiver, new IntentFilter(RobotProvider.ACTION_ROBOT_NONE_FOUND));

        provider.findRobots();
    }

    
    /**
     * BroadcastReceiver for discovered robots
     */
    private class RobotDiscoveredReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            RobotProvider provider = RobotProvider.getDefaultProvider();
            
            String id = intent.getStringExtra(Robot.EXTRA_ROBOT_ID);
            Robot robot = provider.findRobot(id);
            
            if(robot != null){
                RobotWrapper robot_wrapper = new RobotWrapper(robot, context);
                mRobotListView.addListable(robot_wrapper);

                SharedPreferences prefs = getSharedPreferences(sStartupPreferences, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(sPreferenceNeverPaired, false);
                editor.commit();
            }
        }
    }

    private class RobotConnectedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String id = intent.getStringExtra(RobotProvider.EXTRA_ROBOT_ID);
            RobotWrapper r_wrapper = mRobotListView.getById(id);

            r_wrapper.setIsConnecting(false);

            mRobotListView.refresh();

            MultipleRobotStartupActivity.this.setResult(RESULT_OK);
        }
    }

    private class RobotConnectingReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    private class RobotConnectFailedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String id = intent.getStringExtra(RobotProvider.EXTRA_ROBOT_ID);
            RobotWrapper r_wrapper = mRobotListView.getById(id);

            r_wrapper.setIsConnecting(false);

            mRobotListView.refresh();
        }
    }

    private class NonePairedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


    /**
     * OnItemClickListener for the items in this Activity's RobotConnectionListView
     */
    private class RobotListViewItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            RobotWrapper robot_wrapper = (RobotWrapper)adapterView.getItemAtPosition(i);
            RobotProvider provider = RobotProvider.getDefaultProvider();

            if(!robot_wrapper.getIsConnecting()){

                robot_wrapper.setIsConnecting(true);
                robot_wrapper.control(provider);
                provider.connectControlledRobots();
            }

            ((RobotConnectionListView)adapterView).refresh();
        }
    }
}
