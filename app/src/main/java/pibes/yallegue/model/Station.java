package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 06/03/16.
 */
public class Station {

    @SerializedName("id")
    private String mId;

    @SerializedName("nombre")
    private String mName;

    @SerializedName("latitud")
    private String mLat;

    @SerializedName("longitud")
    private String mLong;

    @SerializedName("icono")
    private String mIcon;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getLat() {
        return mLat;
    }

    public String getLong() {
        return mLong;
    }

    public String getIcon() {
        return mIcon;
    }


}
