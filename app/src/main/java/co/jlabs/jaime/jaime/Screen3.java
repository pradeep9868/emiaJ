package co.jlabs.jaime.jaime;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Screen3 extends Activity {
    TextView sess_Name_text;
    Database db;
    ArrayList<String> Images;
    ImageLoader imageLoader;
    LinearLayout images;
    String sess_name;
    int num_img=0;

    String url = "http://jlabs.co/manoj/image_upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database(this);
        imageLoader = new ImageLoader(this);
        setContentView(R.layout.upload_image);
        sess_name=getIntent().getStringExtra("session_name").toString();
        images = (LinearLayout) findViewById(R.id.images);
        Images =new ArrayList<>();
        sess_Name_text= (TextView) findViewById(R.id.textview);
        sess_Name_text.setText("Session Name : "+sess_name);
        Images = db.getAllImage(sess_name);
        if(Images.size()>0)
            setcontent(this);
        else
        {
            Toast.makeText(getApplicationContext(),"It has no images",Toast.LENGTH_SHORT).show();
            finish();
        }
        num_img=Images.size();
    }
    private void setcontent(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        FrameLayout layout;
        ImageView imageView;

        for(int i=0;i<Images.size();i++) {
            layout = (FrameLayout) (inflater).inflate(R.layout.frame_image, null);
            imageView = (ImageView) layout.findViewById(R.id.image);
            final DonutProgress pb = (DonutProgress) layout.findViewById(R.id.circle_progress);
            layout.setTag(i);
            imageView.setImageBitmap(imageLoader.barcodegetBitmap(Images.get(i)));
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FrameLayout lay= (FrameLayout) v;
                    lay.setClickable(false);
                    new Uploadtask(pb, lay, imageLoader.barcodegetFile(Images.get((int) lay.getTag()))).execute();
                }
            });
            images.addView(layout);

        }
    }


    private class Uploadtask extends AsyncTask<Void, Integer, String> {
        DonutProgress pb;
        long totalSize;
        FrameLayout layout;
        File file;
        Uploadtask(DonutProgress pb,FrameLayout layout,File file)
        {
            this.pb=pb;
            this.file=file;
            this.layout=layout;
        }
        @Override
        protected void onPreExecute() {
            pb.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            pb.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(Void... params) {
            return upload();
        }

        private String upload() {
            String responseString = "no";

            //File sourceFile = new File(filename);
            File sourceFile = file;
            if (!sourceFile.isFile()) {
                return "not a file";
            }
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url+"?sess_name="+Static_Catelog.MD5(sess_name));

            try {
                CustomMultiPartEntity entity=new CustomMultiPartEntity(new CustomMultiPartEntity.ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                entity.addPart("type", new StringBody("image/jpg"));
                entity.addPart("uploaded_file", new FileBody(sourceFile));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                responseString = EntityUtils.toString(r_entity);

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }


        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject obj = new JSONObject(result);
                if(obj.getBoolean("success"))
                {
                    Toast.makeText(getApplicationContext(),file.getName()+" was Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                    deleteimage(file);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error Uploading file : "+file.getName(),Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Error Uploading file : "+file.getName(),Toast.LENGTH_SHORT).show();
            }
            layout.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

    }
    public void deleteimage(File file)
    {
        num_img=num_img-1;
        if(file.delete())
        {
            Toast.makeText(getApplicationContext(),file.getName()+" was Deleted Successfully!",Toast.LENGTH_SHORT).show();
            db.deleterow(file.getName());
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error Deleting file : "+file.getName(),Toast.LENGTH_SHORT).show();
        }
        if(num_img==0)
            finish();
    }
}
