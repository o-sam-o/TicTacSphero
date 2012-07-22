package com.orbotix.macrolab.view;

import orbotix.robot.app.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orbotix.macrolab.list.Titled;

/**
 * Created by Orbotix Inc.
 * Date: 2/13/12
 *
 * @author Adam Williams
 */
public class Titlebar extends RelativeLayout implements Titled {

    private final TextView mTitle;
    private final IconButton mActionButton;

    public Titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        inflate(context, R.layout.titlebar, this);

        mTitle = (TextView)findViewById(R.id.title);
        mActionButton = (IconButton)findViewById(R.id.action_button);
        
        if(attrs != null){

            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Titlebar);
            
            if(a.hasValue(R.styleable.Titlebar_title)){
                mTitle.setText(a.getString(R.styleable.Titlebar_title));
            }
            
            if(a.hasValue(R.styleable.Titlebar_action_text)){
                mActionButton.setTitle(a.getString(R.styleable.Titlebar_action_text));
                showActionButton();
            }
        }
    }

    /**
     * Shows the action button of the Titlebar
     */
    public void showActionButton(){
        mActionButton.setEnabled(true);
        mActionButton.setVisibility(VISIBLE);
    }

    /**
     * Hides the action button
     */
    public void hideActionButton(){
        mActionButton.setEnabled(false);
        mActionButton.setVisibility(GONE);
    }

    /**
     * Sets the OnClickListener of this Titlebar's action button
     * @param listener
     */
    public void setActionButtonOnClickListener(OnClickListener listener){
        mActionButton.setOnClickListener(listener);
    }
    
    public void setActionButtonText(String text){
        mActionButton.setTitle(text);
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
