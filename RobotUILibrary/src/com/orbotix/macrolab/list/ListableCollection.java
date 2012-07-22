package com.orbotix.macrolab.list;

import android.view.View;

import java.util.List;

/**
 * A type that contains Listables
 *
 * Created by Orbotix Inc.
 * Date: 2/8/12
 *
 * @author Adam Williams
 */
public interface ListableCollection<V extends View, T extends Listable<V>> {

    /**
     * Adds a Listable
     * @param listable
     */
    public void addListable(T listable);

    /**
     * Sets this to the provided List of Listables
     * @param listables
     */
    public void setListables(List<T> listables);

    /**
     * Adds the provided List of Listables
     * @param listables
     */
    public void addListables(List<T> listables);

    /**
     *
     * @return a List containing this ListableCollection's Listables
     */
    public List<T> getListables();

    /**
     * Clears all contained Listables
     */
    public void clear();
}
