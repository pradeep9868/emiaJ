package co.jlabs.jaime.jaime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Adapt_Search extends BaseAdapter{

	// Declare Variables
	Context context;
	LayoutInflater inflater;
    private ArrayList<JSONObject>data = null;

    static class ViewHolder
    {
        public TextView text_location;
        public TextView text_lat_lng;
        public TextView text_uniq_code;
    }
	public Adapt_Search(Context context, ArrayList<JSONObject> data) {
		this.context = context;
		this.data = data;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;

        try {
            Log.i("Myapp","Printing position "+ position+" "+data.get(position).getString("title"));
        } catch (JSONException e) {

        }
        if (convertView == null||convertView.getTag()==null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_gmap, parent, false);
            holder=new ViewHolder();
            holder.text_location=(TextView) convertView.findViewById(R.id.text);
            holder.text_uniq_code=(TextView) convertView.findViewById(R.id.uniq_code);
            holder.text_lat_lng=(TextView) convertView.findViewById(R.id.lat_lng);
            convertView.setTag(holder);
        } else {
            holder=(ViewHolder)convertView.getTag();
        }
        try {
            holder.text_location.setText(data.get(position).getString("title"));
        }catch (Exception e){
            Log.i("Myapp","New Exception");
        }
        try {
            holder.text_uniq_code.setText("Unique Code : "+data.get(position).getString("uniq_id"));
        }catch (Exception e){
            Log.i("Myapp","New Exception");
        }
        try {
            holder.text_lat_lng.setText("Lat : "+data.get(position).getString("lat")+" | Lng : "+data.get(position).getString("lng"));
        }catch (Exception e){
            Log.i("Myapp","New Exception");
        }
		return convertView;
	}

}
