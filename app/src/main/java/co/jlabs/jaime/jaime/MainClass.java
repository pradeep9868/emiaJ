package co.jlabs.jaime.jaime;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Wadi on 17-10-2015.
 */
public class MainClass implements Serializable{
    String sess_name;
    String lat,lng;
    boolean toi_or_dust;
    boolean ctc_ir_uni;
    int uniq_id;

    boolean flush,runn_water,mirror,airwick;
    //for dustbin
    MainClass(int uniq_id,String sess_name,String lat,String lng)
    {
        this.sess_name=sess_name;
        this.lat=lat;
        this.lng=lng;
        toi_or_dust=false;
        this.uniq_id=uniq_id;
    }
    //for toi ctc
    MainClass(int uniq_id,String sess_name,String lat,String lng,boolean flush,boolean runn_water,boolean mirror,boolean airwick)
    {
        this.sess_name=sess_name;
        this.lat=lat;
        this.lng=lng;
        toi_or_dust=true;
        ctc_ir_uni=true;
        this.flush=flush;
        this.runn_water=runn_water;
        this.mirror=mirror;
        this.airwick=airwick;
        this.uniq_id=uniq_id;
    }
    //for toi uni
    MainClass(int uniq_id,String sess_name,String lat,String lng,boolean flush,boolean runn_water,boolean airwick)
    {
        this.sess_name=sess_name;
        this.lat=lat;
        this.lng=lng;
        toi_or_dust=true;
        ctc_ir_uni=false;
        this.flush=flush;
        this.runn_water=runn_water;
        this.airwick=airwick;
        this.uniq_id=uniq_id;
    }



    public JSONObject toJson()
    {
        JSONObject loc=new JSONObject();
        JSONObject object=new JSONObject();
        try {

            object.put("title",sess_name);

            loc.put("lat",lat);
            loc.put("lng",lng);
            object.put("loc",loc);
            if(uniq_id>0)
            object.put("uniq_code",uniq_id);
            if(toi_or_dust)
            {
                object.put("type","toilet");
                if(ctc_ir_uni)
                {
                    object.put("sub_type","ctc");

                    object.put("flush",flush);
                    object.put("water",runn_water);
                    object.put("mirror",mirror);
                    object.put("airwick",airwick);
                }
                else
                {
                    object.put("sub_type","urinal");

                    object.put("flush",flush);
                    object.put("water",runn_water);
                    object.put("airwick",airwick);
                }
            }
            else
            {
                object.put("type","dustbin");
            }
        }
        catch (Exception e)
        {

        }
        return object;
    }

}
