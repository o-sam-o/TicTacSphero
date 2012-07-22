package com.orbotix.macrolab.view;

import orbotix.robot.app.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orbotix.macrolab.list.Iconed;
import com.orbotix.macrolab.list.Titled;

/**
 * Created by Orbotix Inc.
 * Date: 3/27/12
 *
 * @author Adam Williams
 */
public class IconButton extends RelativeLayout implements Iconed, Titled {
    
    private final ImageView mIcon;
    private final TextView mTitle;
    
    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        inflate(context, R.layout.button_layout, this);
        
        mIcon  = (ImageView)findViewById(R.id.button_icon);
        mTitle = (TextView)findViewById(R.id.title);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconButton);
            
            if(a.hasValue(R.styleable.IconButton_icon)){
                setIconDrawable(a.getDrawable(R.styleable.IconButton_icon));
            }
            
            if(a.hasValue(R.styleable.IconButton_title)){
                setTitle(a.getString(R.styleable.IconButton_title));
            }
        }
    }

    @Override
    public void setIconDrawable(Drawable d) {
        mIcon.setImageDrawable(d);

        if(d == null){
            mIcon.setVisibility(GONE);
        }else{
            mIcon.setVisibility(VISIBLE);
        }
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public String getTitle() {
        return mTitle.getText().toString();
    }
}
