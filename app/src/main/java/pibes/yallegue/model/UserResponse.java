package pibes.yallegue.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jhordan on 06/03/16.
 */
public class UserResponse {

    @SerializedName("data")
    private List<User> mUserList;

    public List<User> getUserList() {
        return mUserList;
    }
}
