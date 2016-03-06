package pibes.yallegue.searchuser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pibes.yallegue.R;
import pibes.yallegue.model.User;

/**
 * Created by Edgar Salvador Maurilio on 06/03/2016.
 */
public class SearchUserListAdapter extends RecyclerView.Adapter<SearchUserListAdapter.ViewHolderUser> {

    private static final String LOG_TAG = SearchUserListAdapter.class.getSimpleName();
    private LayoutInflater layoutInflater;
    private List<User> users;

    public SearchUserListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        users = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderUser onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder");
        return new ViewHolderUser(layoutInflater.inflate(R.layout.item_people, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderUser holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder");
        holder.setData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.image_people)
        CircleImageView imageUser;

        @Bind(R.id.label_name)
        TextView userName;

        @Bind(R.id.checkbox_user)
        CheckBox checkBox;


        public ViewHolderUser(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(User user) {
            userName.setText(user.getmUserName());
            Picasso.with(layoutInflater.getContext()).load(user.getmPhoto()).into(imageUser);
        }

        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());
        }
    }


}
