package com.orbotix.macrolab.list;

import android.content.Context;
import android.view.View;

/**
 * A type that represends a model that can be listed in a ListView
 * 
 * Created by Orbotix Inc.
 * Date: 2/8/12
 *
 * @author Adam Williams
 *
 */
public interface Listable<T extends View> {

    /**
     * Creates and returns a new View that represents this Listable
     * @param context The Android Context
     * @return A type of View
     */
    public T getView(Context context);

    /**
     * Fills the provided View with this Listable's presentation.
     * @param view a type of View to fill
     */
    public void fillView(T view);
}
