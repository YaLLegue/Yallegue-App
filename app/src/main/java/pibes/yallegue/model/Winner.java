package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 06/03/16.
 */
public class Winner {

    @SerializedName("winner")
    private String mWinner;

    public String getWinner() {
        return mWinner;
    }
}
