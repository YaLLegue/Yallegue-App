package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 06/03/16.
 */
public class Subway {

    @SerializedName("nombre")
    private String mName;
    @SerializedName("latitud")
    private String mLat;
    @SerializedName("longitud")
    private String mLong;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLong() {
        return mLong;
    }

    public void setLong(String aLong) {
        mLong = aLong;
    }


}
