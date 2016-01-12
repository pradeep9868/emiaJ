package co.jlabs.jaime.jaime;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Wadi on 30-11-2015.
 */
public class Static_Catelog {
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    public static String getcolorscheme(Context context)
    {
        return context.getResources().getString(R.string.app_color).toString();
    }
    public static void Toast(Context context,String s)
    {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    public static String geturl()
    {
        //return "http://192.168.1.103:8000/";
        return "http://lannister-api.elasticbeanstalk.com/";
    }
}
