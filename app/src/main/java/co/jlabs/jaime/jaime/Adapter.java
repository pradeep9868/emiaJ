package co.jlabs.jaime.jaime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JussConnect on 10/4/2015.
 */
public class Adapter extends BaseAdapter {
    Context context;
    ArrayList<String> name;
    Adapter(Context context,ArrayList<String> name){
        this.context=context;
        this.name=name;
    }
    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.adapter,null);
        }
        else
        {
            v=convertView;
        }
        ((TextView)v.findViewById(R.id.textview)).setText(name.get(position));
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
