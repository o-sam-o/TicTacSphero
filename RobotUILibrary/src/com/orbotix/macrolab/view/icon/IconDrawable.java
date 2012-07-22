package com.orbotix.macrolab.view.icon;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import orbotix.macro.MacroCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orbotix Inc.
 * Date: 3/12/12
 *
 * @author Adam Williams
 */
public abstract class IconDrawable<T extends MacroCommand> extends Drawable {

    protected T mCommand;

    private final List<Drawable> mDrawables = new ArrayList<Drawable>();

    {
        setFilterBitmap(true);
    }

    public void setCommand(T command) {
        mCommand = command;
        updateFromCommand();
    }

    public void updateFromCommand(){

        if(mCommand != null){
            update();
        }
    }

    public void addDrawable(Drawable d){
        mDrawables.add(d);
    }
    
    @Override
    public void draw(Canvas canvas) {
        for(Drawable d : mDrawables){
            d.setBounds(new Rect(getBounds()));
            d.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int i) {

        for(Drawable d : mDrawables){
            d.setAlpha(i);
        }
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        
        for(Drawable d : mDrawables){
            d.setColorFilter(colorFilter);
        }
    }

    @Override
    public int getOpacity() {
        
        int format = PixelFormat.OPAQUE;
        
        for(Drawable d : mDrawables){
            if(d.getOpacity() == PixelFormat.TRANSLUCENT){
                format = d.getOpacity();
            }
        }
        return format;
    }

    protected abstract void update();
}
