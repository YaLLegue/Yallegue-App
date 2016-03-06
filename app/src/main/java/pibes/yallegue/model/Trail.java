package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 06/03/16.
 */
public class Trail {

    @SerializedName("_id")
    private String mId;

    @SerializedName("st_asgeojson")
    private String mStAsgeo;

    @SerializedName("route_id")
    private String mRouteId;

    @SerializedName("route_short_name")
    private String mRouteShortName;

    @SerializedName("route_long_name")
    private String mRouteLongName;

    public String getId() {
        return mId;
    }

    public String getStAsgeo() {
        return mStAsgeo;
    }

    public String getRouteId() {
        return mRouteId;
    }

    public String getRouteShortName() {
        return mRouteShortName;
    }

    public String getRouteLongName() {
        return mRouteLongName;
    }


}
