package co.jlabs.jaime.jaime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapView extends FragmentActivity {

    private GoogleMap googleMap;
    Context context;
    ArrayList<Class_Map_Marker> marks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        context=this;
       // setMapTransParent((ViewGroup)itemView);
        Class_map_info map_info = (Class_map_info) getIntent().getSerializableExtra("map_info");
        marks= map_info.class_map_markers;
        CreateMap();
    }

    public  void CreateMap()
    {


        try {
            // Loading map
            initilizeMap();

            // Changing map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

            // Showing / hiding your current location
            googleMap.setMyLocationEnabled(true);

            // Enable / Disable zooming controls
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // Enable / Disable my location button
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            // Enable / Disable Compass icon
            googleMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

            MarkerOptions marker;
            for(int i=0;i<marks.size();i++)
            {
                marker = new MarkerOptions().position(
                        new LatLng(marks.get(i).latitude, marks.get(i).longitude))
                        .title(marks.get(i).title);

                marker.icon(BitmapDescriptorFactory.fromResource(marks.get(i).icon));
                marker.snippet("" + marks.get(i).id);
                googleMap.addMarker(marker);
            }
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker arg0) {
                    if(googleMap.getMyLocation().getLatitude()>0) {
                        Log.i("Myapp", "Hai" + arg0.getTitle() + " " + arg0.getSnippet() + " " + googleMap.getMyLocation().getLatitude() + " " + googleMap.getMyLocation().getLongitude());
                        Class_GMap_title title = new Class_GMap_title(arg0.getTitle(), Integer.parseInt(arg0.getSnippet()), googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
                        Intent i = new Intent(MapView.this, After_Map_View.class);
                        i.putExtra("info", title);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(context,"Unable to get your current location to update info",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(marks.get(0).latitude,
                            marks.get(0).longitude)).zoom(10).build();

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initilizeMap() {
        if (googleMap == null) {

            googleMap = ((SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(context,
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();

            }
        }
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.map_info_adapter, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText("Unique ID : "+marker.getSnippet());
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
