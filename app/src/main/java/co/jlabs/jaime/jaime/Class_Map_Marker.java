package co.jlabs.jaime.jaime;

import java.io.Serializable;

public class Class_Map_Marker implements Serializable {
    String title;
    double latitude;
    double longitude;
    int icon;
    int id;

    Class_Map_Marker()
    {

    }
    Class_Map_Marker(int id,String title,double latitude,double longitude,int icon)
    {
        this.title=title;
        this.latitude=latitude;
        this.longitude=longitude;
        this.icon=icon;
        this.id=id;
    }

}
