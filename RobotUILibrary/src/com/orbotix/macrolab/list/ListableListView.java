package com.orbotix.macrolab.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A ListView that is capable of presenting a list of Listable mItems
 *
 * Created by Orbotix Inc.
 * Date: 2/8/12
 *
 * @param <V> The type of View that will be shown by the ListableListView to represent its Listables
 * @param <T> The type of Listable that this ListableListView will present
 *
 * @author Adam Williams
 */
public class ListableListView<V extends View, T extends Listable<V>> extends TouchListView implements ListableCollection<V, T> {

    protected ListableAdapter mAdapter = new ListableAdapter();

    public ListableListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAdapter(mAdapter);


    }

    public void moveListable(int from, int to){
        mAdapter.moveItem(from, to);

    }

    public void removeListable(int index){
        mAdapter.removeItem(index);
    }

    @Override
    public void addListable(T listable) {

        mAdapter.addItem(listable);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListables(List<T> listables) {

        mAdapter.setItems(listables);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addListables(List<T> listables) {

        mAdapter.addItems(listables);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public List<T> getListables() {
        return new ArrayList<T>(mAdapter.mItems);
    }

    @Override
    public void clear() {

        mAdapter.clear();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public ListAdapter getAdapter() {
        return super.getAdapter();
    }

    public void refresh(){
        mAdapter.notifyDataSetChanged();
    }


    public class ListableAdapter extends BaseAdapter {

        private final List<T> mItems = new ArrayList<T>();

        public void clear(){
            mItems.clear();

        }

        public void addItem(T item){
            mItems.add(item);
            notifyDataSetChanged();
        }
        
        public void setItems(List<T> items){

            clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
        
        public void addItems(List<T> items){
            mItems.addAll(items);
            notifyDataSetChanged();
        }

        public void moveItem(int from, int to){
            T item = mItems.get(from);
            mItems.remove(from);
            mItems.add(to, item);
            notifyDataSetChanged();
        }

        public void removeItem(int index){
            mItems.remove(index);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public T getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public V getView(int i, View view, ViewGroup viewGroup) {

            T item = getItem(i);
            V v = (V)view;

            if(view == null){
                
                v = item.getView(getContext());
            }else{
                item.fillView(v);
            }
            
            return v;
        }
    }
}
