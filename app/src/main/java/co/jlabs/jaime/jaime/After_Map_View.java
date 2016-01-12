package co.jlabs.jaime.jaime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class After_Map_View extends Activity {
    EditText session_name;
    TextView done, get_location, lat, lng;
    CheckBox toi, dus, uri, ctc, flush, run_water, mirror, odour;
    LinearLayout ques, toilet;
    String s_lat,s_lng;
    Class_GMap_title Gmap_title;

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
        Gmap_title = (Class_GMap_title) getIntent().getSerializableExtra("info");
        get_location.setVisibility(View.GONE);
        lat.setVisibility(View.VISIBLE);
        lng.setVisibility(View.VISIBLE);
        s_lng="" + Gmap_title.lng;
        s_lat = "" + Gmap_title.lat;
        lat.setText(s_lat);
        lng.setText(s_lng);
        session_name.setText(Gmap_title.sess_name);

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
                    mainClass=new MainClass(Gmap_title.uniq_id,session_name.getText().toString(),s_lat,s_lng);
                }
                else if(ctc.isChecked())
                {
                    mainClass=new MainClass(Gmap_title.uniq_id,session_name.getText().toString(),s_lat,s_lng,flush.isChecked(),run_water.isChecked(),mirror.isChecked(),odour.isChecked());
                }
                else
                {
                    mainClass=new MainClass(Gmap_title.uniq_id,session_name.getText().toString(),s_lat,s_lng,flush.isChecked(),run_water.isChecked(),odour.isChecked());
                }
                Intent i = new Intent(After_Map_View.this,Camera_Activity.class);
                i.putExtra("data", mainClass);
                startActivity(i);
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


    @Override
    protected void onResume() {
        super.onResume();

    }

}
