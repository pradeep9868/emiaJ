package co.jlabs.jaime.jaime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import co.jlabs.jaime.jaime.customcomponents.PullAndLoadMore;


public class ScreenGmap extends Activity {
    PullAndLoadMore list;
    String url = Static_Catelog.geturl()+"jaime/gmap?p=";
    ArrayList<JSONObject> list_item;
    Adapt_Search data;
    int p=0;
    Context context;
    TextView view_gmap;
    LocationManager locationManager;



    LocationListener locationListener;
    ProgressDialog mProgressDialog;
    String lat,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_gmap);
        context=this;
        list= (PullAndLoadMore) findViewById(R.id.listview);
        view_gmap= (TextView) findViewById(R.id.view_gmap);
        list_item = new ArrayList<>();
        locationManager = (LocationManager)
                getSystemService(this.LOCATION_SERVICE);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Retrieving Current Location...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        locationListener = new MyLocationListener();
        mProgressDialog.show();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);

        lat="";
        lng="";





        data = new Adapt_Search(this, list_item);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    Class_map_info data_s = new Class_map_info((JSONObject) data.getItem(i));
                    Intent intent = new Intent(context, MapView.class);
                    intent.putExtra("map_info", data_s);
                    startActivity(intent);
                   // finish();
                }
            }
        });
        view_gmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    Class_map_info data_s = new Class_map_info(list_item);
                    Intent intent = new Intent(context, MapView.class);
                    intent.putExtra("map_info", data_s);
                    startActivity(intent);
                   // finish();
                }
            }
        });
        list.setAdapter(data);
        list.setOnLoadMoreListener(new PullAndLoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (p != 99)
                    download_list_data(p);
            }
        });
        //download_list_data(p);
    }

    private void download_list_data(int param) {

        String tag_json_obj = "json_obj_req";

        Log.i("Myapp", "Calling " + url + param);
        String s="";
        if(lat!=""&&lng!="")
        {
            s="&lat="+lat+"&lng="+lng;
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url+ param+s, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //list_item.clear();
                                Log.i("Myapp", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray != null) {
                                        int len = jsonArray.length();
                                        for (int i = 0; i < len; i++) {
                                            list_item.add((JSONObject) jsonArray.get(i));
                                        }
                                        if(len==10)
                                            p=p+1;
                                        else
                                            p=99;
                                    } else {
                                        Log.i("Myapp", "null array");
                                    }
                                } catch (Exception e) {
                                    Log.i("Myapp", "Error" + e.getMessage());
                                }
                                Log.i("Myapp", "Before Notifying");
                                data.notifyDataSetChanged();

                                Log.i("Myapp", "Hello" + list_item.size());
                                list.onLoadMoreComplete();
                            }
                        });

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"An Error Occured while fetching list",Toast.LENGTH_SHORT).show();
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if(lat.equals("")&&lng.equals("")) {

                Toast.makeText(
                        getBaseContext(),
                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                String longitude = "" + loc.getLongitude();
                String latitude = "" + loc.getLatitude();
                lat = latitude;
                lng = longitude;
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
                p = 0;
                list_item.clear();
                download_list_data(p);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
