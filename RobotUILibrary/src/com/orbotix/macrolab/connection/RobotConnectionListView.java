package com.orbotix.macrolab.connection;

import android.content.Context;
import android.util.AttributeSet;
import com.orbotix.macrolab.list.IconListItemView;
import com.orbotix.macrolab.list.ListableListView;

import java.util.List;

/**
 * Created by Orbotix Inc.
 * Date: 4/5/12
 *
 * @author Adam Williams
 */
public class RobotConnectionListView extends ListableListView<IconListItemView, RobotWrapper>{


    public RobotConnectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Gets the first RobotWrapper in this ListView that matches the provided Id String
     * @param id
     * @return
     */
    public RobotWrapper getById(String id){

        List<RobotWrapper> wrappers = getListables();

        for(RobotWrapper w : wrappers){
            if(w.getId().equals(id)){
                return w;
            }
        }
        
        return null;
    }
}
