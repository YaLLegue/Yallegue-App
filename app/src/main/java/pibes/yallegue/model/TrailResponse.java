package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jhordan on 06/03/16.
 */
public class TrailResponse {

    @SerializedName("data")
    private List<Trail> mTrailList;

    public List<Trail> getTrailList() {
        return mTrailList;
    }
}
