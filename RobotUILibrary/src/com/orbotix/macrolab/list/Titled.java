package com.orbotix.macrolab.list;

/**
 * A type that contains a title.
 * 
 * Created by Orbotix Inc.
 * Date: 2/9/12
 *
 * @author Adam Williams
 */
public interface Titled {

    /**
     * Sets the title of this Titled to the provided String
     * @param title
     */
    public void setTitle(String title);

    /**
     *
     * @return The title of this Titled
     */
    public String getTitle();
}
