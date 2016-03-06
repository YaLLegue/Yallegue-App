package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jhordan on 06/03/16.
 */
public class User {

    @SerializedName("_id")
    private String mId;

    @SerializedName("username")
    private String mUserName;

    @SerializedName("photo")
    private String mPhoto;

    public void setId(String id) {
        mId = id;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getmId() {
        return mId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmPhoto() {
        return mPhoto;
    }
}
