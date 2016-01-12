package co.jlabs.jaime.jaime;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Class_map_info implements Serializable {
    public ArrayList<Class_Map_Marker> class_map_markers;
    Class_map_info(ArrayList<JSONObject> jsonObject){
       class_map_markers=new ArrayList<>();
        for (int i=0;i<jsonObject.size();i++)
        {
            try {
                Class_Map_Marker temp= new Class_Map_Marker(jsonObject.get(i).getInt("uniq_id"),jsonObject.get(i).getString("title"), jsonObject.get(i).getDouble("lat"),jsonObject.get(i).getDouble("lng"),R.drawable.toilet_icon);
                class_map_markers.add(temp);
            } catch (JSONException e) {

            }

        }
        }
    Class_map_info(JSONObject jsonObject){
        class_map_markers=new ArrayList<>();
            try {
                Class_Map_Marker temp= new Class_Map_Marker(jsonObject.getInt("uniq_id"),jsonObject.getString("title"),jsonObject.getDouble("lat"),jsonObject.getDouble("lng"),R.drawable.toilet_icon);
                class_map_markers.add(temp);
            } catch (JSONException e) {

        }
    }
}