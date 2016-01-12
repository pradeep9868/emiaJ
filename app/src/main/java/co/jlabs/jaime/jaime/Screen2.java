package co.jlabs.jaime.jaime;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Screen2 extends Activity {
    EditText session_name;
    TextView done, get_location, lat, lng;
    CheckBox toi, dus, uri, ctc, flush, run_water, mirror, odour;
    LinearLayout ques, toilet;
    LocationManager locationManager;
    LocationListener locationListener;
    ProgressDialog mProgressDialog;
    String s_lat,s_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);
        session_name = (EditText) findViewById(R.id.session_name);
        done = (TextView) findViewById(R.id.done);
        get_location = (TextView) findViewById(R.id.get_loc);
        lat = (TextView) findViewById(R.id.lat);
        lng = (TextView) findViewById(R.id.lng);
        toi = (CheckBox) findViewById(R.id.toi);
        dus = (CheckBox) findViewById(R.id.dus);
        uri = (CheckBox) findViewById(R.id.uri);
        ctc = (CheckBox) findViewById(R.id.ctc);
        flush = (CheckBox) findViewById(R.id.flush);
        run_water = (CheckBox) findViewById(R.id.run_water);
        mirror = (CheckBox) findViewById(R.id.mirror);
        odour = (CheckBox) findViewById(R.id.odour);
        ques = (LinearLayout) findViewById(R.id.ques);
        toilet = (LinearLayout) findViewById(R.id.toilet);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Retrieving Current Location...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        s_lat="0";
        s_lng="0";
        locationManager = (LocationManager)
                getSystemService(this.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_location.setVisibility(View.GONE);
                lat.setVisibility(View.VISIBLE);
                lng.setVisibility(View.VISIBLE);
                if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }
                else {
                    mProgressDialog.show();
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                }
            }
        });

        toi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    select_toilet();
                    toi.setChecked(true);
            }
        });

        dus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_dustbin();
                dus.setChecked(true);
            }
        });
        uri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_uri();
                uri.setChecked(true);
            }
        });
        ctc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_ctc();
                ctc.setChecked(true);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainClass mainClass;
                if(dus.isChecked())
                {
                    mainClass=new MainClass(0,session_name.getText().toString(),s_lat,s_lng);
                }
                else if(ctc.isChecked())
                {
                    mainClass=new MainClass(0,session_name.getText().toString(),s_lat,s_lng,flush.isChecked(),run_water.isChecked(),mirror.isChecked(),odour.isChecked());
                }
                else
                {
                    mainClass=new MainClass(0,session_name.getText().toString(),s_lat,s_lng,flush.isChecked(),run_water.isChecked(),odour.isChecked());
                }
                Intent i = new Intent(Screen2.this,Camera_Activity.class);
                i.putExtra("data", mainClass);
                startActivity(i);
                locationManager.removeUpdates(locationListener);
                finish();
            }
        });

    }
    public void select_toilet()
    {
        deselect_dustbin();
        show_hide_dustbin_parts(false);
        show_hide_toilet_parts(true);
        done.setEnabled(false);
    }
    public void select_dustbin()
    {
        deselect_toilet();
        show_hide_toilet_parts(false);
        show_hide_dustbin_parts(true);
        done.setEnabled(true);
    }
    public void deselect_toilet()
    {
        toi.setChecked(false);
    }
    public void deselect_dustbin()
    {
        dus.setChecked(false);
    }
    public void show_hide_dustbin_parts(Boolean b)
    {

    }
    public void show_hide_toilet_parts(Boolean b)
    {
        if(b)
        {
            toilet.setVisibility(View.VISIBLE);
        }
        else
        {
            toilet.setVisibility(View.INVISIBLE);
        }
    }


    public void select_uri()
    {
        deselect_ctc();
        mirror.setVisibility(View.GONE);
        if(ques.getVisibility()==View.GONE)
        {
            ques.setVisibility(View.VISIBLE);
            done.setEnabled(true);
        }
    }
    public void select_ctc()
    {
        deselect_uri();
        mirror.setVisibility(View.VISIBLE);
        if(ques.getVisibility()==View.GONE)
        {
            ques.setVisibility(View.VISIBLE);
            done.setEnabled(true);
        }
    }
    public void deselect_uri()
    {
        uri.setChecked(false);
    }
    public void deselect_ctc()
    {
        ctc.setChecked(false);
    }






    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("Myapp", longitude);
            s_lng=""+loc.getLongitude();
            s_lat=""+loc.getLatitude();
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("Myapp", latitude);
        /*-------to get City-Name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            //editLocation.setText(s);
            lat.setText(""+latitude);
            lng.setText(""+longitude);
            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        else {
            mProgressDialog.show();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
    }
    public  void onDestroy()
    {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
