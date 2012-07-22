package com.orbotix.macrolab.view.icon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * A View that shows an Icon of a MacroObject
 *
 * Created by Orbotix Inc.
 * Date: 3/12/12
 *
 * @author Adam Williams
 */
public class IconView extends ImageView {

    private IconDrawable mIconDrawable;

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void update(){
        if(mIconDrawable != null){
            mIconDrawable.updateFromCommand();
            invalidate();
        }
    }
    
    public void setImageDrawable(Drawable d){
        super.setImageDrawable(d);

        mIconDrawable = null;
        if(d instanceof IconDrawable){
            mIconDrawable = (IconDrawable)d;
        }
    }
}
