package com.orbotix.macrolab.connection;

import orbotix.robot.app.R;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.orbotix.macrolab.list.IconListItemView;
import com.orbotix.macrolab.list.Listable;

/**
 * Created by Orbotix Inc.
 * Date: 4/5/12
 *
 * @author Adam Williams
 */
public class RobotWrapper implements Listable<IconListItemView> {

    private final Robot mRobot;
    private boolean mConnecting = false;

    private final Drawable mDefaultDrawable;
    private final Drawable mFailedToConnectDrawable;
    private final Drawable mConnectingDrawable;
    private final Drawable mConnectedDrawable;

    public RobotWrapper(Robot robot, Context context){
        mRobot = robot;

        mDefaultDrawable = context.getResources().getDrawable(R.drawable.blank_icon_drawable);
        mFailedToConnectDrawable = context.getResources().getDrawable(R.drawable.icon_stabilization_off);
        mConnectingDrawable = context.getResources().getDrawable(R.drawable.radial_load);
        mConnectedDrawable = context.getResources().getDrawable(R.drawable.icon_stabilization_on);
    }

    public void setIsConnecting(boolean is_connecting){
        mConnecting = is_connecting;
    }

    /**
     * Indicates whether this robot is connecting
     * @return
     */
    public boolean getIsConnecting(){
        return mConnecting;
    }

    /**
     * Controls the robot using the provided RobotProvider
     * @param provider
     */
    public void control(RobotProvider provider){
        provider.control(mRobot);
    }
    
    public String getId(){
        return mRobot.getUniqueId();
    }

    @Override
    public IconListItemView getView(Context context) {

        IconListItemView view = new IconListItemView(context);
        fillView(view);
        return view;
    }

    @Override
    public void fillView(IconListItemView view) {

        String name = "<font color='#888888'>";
        if(mRobot.isUnderControl() && !mRobot.isConnected() && !mConnecting){
            view.setIconDrawable(mFailedToConnectDrawable);
            name += "Failed to connect</font>";
        }else if(mRobot.isUnderControl() && mRobot.isConnected()){
            view.setIconDrawable(mConnectedDrawable);
            name += "Connected</font>";
        }else if(mRobot.isUnderControl() && !mRobot.isConnected()){
            view.setIconDrawable(mConnectingDrawable);
            name += "Connecting</font>";
        }else {
            view.setIconDrawable(mDefaultDrawable);
            name += "Not connected</font>";
        }

        name = "<font color='#EEEEEE' style='font-weight:bold;'>"+mRobot.getName()+"</font> "+name;
        view.setTitle(Html.fromHtml(name));
    }
}
