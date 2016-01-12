package co.jlabs.jaime.jaime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

	FileCache fileCache;
	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
  	}
    public Bitmap barcodegetBitmap(String name) {
        Log.i("fnaem","->"+name);
        File f = fileCache.getFile_noencode(name);
        Bitmap b = decodeFile(f);
            return b;
        }
    public File barcodegetFile(String name) {
        Log.i("fnaem","->"+name);
        return fileCache.getFile_noencode(name);
        //return fileCache.getFile_noencode("image.jpg");
    }
    public long barcodegetFileSize(String name) {
        Log.i("fnaem", "->" + name);
        return fileCache.getFile_noencode(name).length();
    }
    public Boolean barcodesetBitmap(String name,Bitmap thumbnail) {
        File f = fileCache.getFile(name,0);
        //InputStream is = conn.getInputStream();
        Boolean alldone=false;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 0, stream);
        InputStream is = new ByteArrayInputStream(stream.toByteArray());
        try {

            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            alldone=true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return alldone;
    }

    private Bitmap barcodedecodeFile(File f) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        return bm;
    }

    public Boolean barcodesetBitmap(String f_source,String f_dest,int i) {
        Boolean alldone=false;
        File dest = fileCache.getFile(f_dest,i);
        File source = new File(f_source);
        try {
            copyFile(source, dest);
            alldone=true;
        }
        catch (Exception e)
        {

        }
        return alldone;
    }
    public String getfilename(String f_dest)
    {
        return fileCache.getFileName(f_dest);
    }

	public void clearCache() {
		fileCache.clear();
	}

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            // Recommended Size 512
            final int REQUIRED_SIZE = 513;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

}