package co.jlabs.jaime.jaime;

import java.io.Serializable;

/**
 * Created by JussConnect on 10/2/2015.
 */
public class Class_GMap_title implements Serializable {
    String sess_name;
    int uniq_id;
    double lat;
    double lng;
    Class_GMap_title(String sess_name, int uniq_id,double lat,double lng)
    {
        this.sess_name=sess_name;
        this.uniq_id=uniq_id;
        this.lat=lat;
        this.lng=lng;
    }
}
