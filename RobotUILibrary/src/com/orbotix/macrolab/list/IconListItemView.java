package com.orbotix.macrolab.list;

import orbotix.robot.app.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orbotix.macrolab.view.icon.IconView;

/**
 * Created by Orbotix Inc.
 * Date: 2/8/12
 *
 * @author Adam Williams
 */
public class IconListItemView extends RelativeLayout implements Iconed, Titled, Draggable {


    private TextView mTitle;
    private IconView mIcon;
    private TextView mNumberView;
    private final ImageView mDragIcon;
    
    private boolean mDraggable = false;

    public IconListItemView(Context context){
        this(context, null);
    }
    
    public IconListItemView(Context context, AttributeSet attrs){
        super(context, attrs);

        //Set the content of this View
        inflate(context, R.layout.macro_list_item_view, this);
        
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_item_state_bg));

        //Get important Views
        mIcon  = (IconView)findViewById(R.id.icon);
        mTitle = (TextView)findViewById(R.id.title);
        mDragIcon = (ImageView)findViewById(R.id.drag_button);
        mNumberView = (TextView)findViewById(R.id.number);
        
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconListItemView);
            
            if(a.hasValue(R.styleable.IconListItemView_title)){
                setTitle(a.getString(R.styleable.IconListItemView_title));
            }
            
            if(a.hasValue(R.styleable.IconListItemView_icon)){
                setIconDrawable(a.getDrawable(R.styleable.IconListItemView_icon));
            }
        }
    }


    @Override
    public void setIconDrawable(Drawable d) {
        mIcon.setImageDrawable(d);
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }
    
    public void setTitle(Spanned html){
        mTitle.setText(html);
    }

    @Override
    public String getTitle() {
        return mTitle.getText().toString();
    }

    @Override
    public void setDraggable(boolean val) {
        mDraggable = val;
        
        if(mDraggable){
            mDragIcon.setVisibility(VISIBLE);
        }else{
            mDragIcon.setVisibility(INVISIBLE);
        }
    }

    @Override
    public boolean getIsDraggable() {
        return mDraggable;
    }
    
    public void setShowNumber(boolean val){
        if(val){
            mNumberView.setVisibility(VISIBLE);
        }else{
            mNumberView.setVisibility(INVISIBLE);
        }
    }

    public void setNumber(int number){
        mNumberView.setText(new Integer(number).toString());
    }
}
