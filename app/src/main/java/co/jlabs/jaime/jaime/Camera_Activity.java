package co.jlabs.jaime.jaime;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class Camera_Activity extends Activity {

    private static final int CAMERA_PIC_REQUEST = 1111;
    ImageLoader imageLoader;
    TextView tv1,tv2;
    Uri imageUri;
    MainClass maindata;
    ArrayList<String> image;
    JSONObject jsonObject;
    Database db;
    LinearLayout images;
    String sess_name;

    ProgressDialog mProgressDialog;

    JSONArray arr;
    int i;
    String url=Static_Catelog.geturl()+"jaime/insert";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader=new ImageLoader(this);
        db = new Database(this);
        maindata= (MainClass) getIntent().getSerializableExtra("data");
        image=new ArrayList<>();
        sess_name=maindata.sess_name;
        jsonObject = maindata.toJson();

        arr=new JSONArray();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Uploading Data to server...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(true);
        i=1;
        Log.i("Myapp", "MainData " + jsonObject.toString());
        setContentView(R.layout.camera_image);
        tv1= (TextView) findViewById(R.id.textview1);
        tv2= (TextView) findViewById(R.id.textview2);
        images = (LinearLayout) findViewById(R.id.images);
        //1
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(Camera_Activity.this,"Data"+maindata.toJson().toString(),Toast.LENGTH_LONG).show();

                Log.i("Myapp", "Calling " + url);
                try
                {
                    jsonObject.put("pics", arr);
                }
                catch (Exception e)
                {

                }
                if(!mProgressDialog.isShowing())
                mProgressDialog.show();
                Log.i("Myapp",jsonObject.toString());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(final JSONObject response) {
                                        Log.i("Myapp", response.toString());

                                if(mProgressDialog.isShowing())
                                    mProgressDialog.hide();
                                try {
                                    if (response.getInt("success") == 1) {
                                        Toast.makeText(Camera_Activity.this,"Data Stored Successfully",Toast.LENGTH_LONG).show();
                                        for(int i=0;i<image.size();i++) {
                                            Class_Image barcode = new Class_Image(sess_name, image.get(i)+"_"+i);
                                            db.addBarocde(barcode);
                                        }
                                        finish();

                                    } else {
                                        Toast.makeText(Camera_Activity.this,"Something Went Wrong!! Please Retry!",Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(Camera_Activity.this,"Invalid Json Response Received",Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Myapp", "Calling eror " + error.getMessage());
                        if(mProgressDialog.isShowing())
                            mProgressDialog.hide();
                        Toast.makeText(Camera_Activity.this,"Error!! Not Connected to internet",Toast.LENGTH_LONG).show();
                    }
                });
                AppController.getInstance().addToRequestQueue(jsonObjReq, "new_request");


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {



            Bitmap thumbnail;// = (Bitmap) data.getExtras().get("data");
            try {
                thumbnail = imageLoader.scaleBitmap(MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri),512,512) ;
                setcontent(thumbnail);
                Log.i("hello", "" + getRealPathFromURI(imageUri));
                if(imageLoader.barcodesetBitmap(getRealPathFromURI(imageUri),sess_name,image.size())) {
                    image.add(imageLoader.getfilename(sess_name));
                    tv2.setEnabled(true);

                    arr.put("http://jlabs.co/manoj/" + Static_Catelog.MD5(sess_name) + "/" + i + ".jpg");
                    i++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void setcontent(Bitmap thumbnail){
        ImageView imageView;
            imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 0);
            imageView.setLayoutParams(lp);
            imageView.setImageBitmap(thumbnail);
            images.addView(imageView);
    }

}