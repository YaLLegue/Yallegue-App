package pibes.yallegue.searchuser;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pibes.yallegue.R;
import pibes.yallegue.YaLlegueApplication;
import pibes.yallegue.common.BaseActivity;
import pibes.yallegue.data.DataService;
import pibes.yallegue.home.HomeActivity;
import pibes.yallegue.model.User;
import pibes.yallegue.model.UserResponse;
import pibes.yallegue.receive.PushNotificationApp;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener  {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    @Bind(R.id.list_users)
    RecyclerView mRecyclerViewUsers;
    private SearchUserListAdapter searchUserListAdapter;


    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        super.initView();
        setupListUser();
        requestUser("");
    }

    private void setupListUser() {
        searchUserListAdapter = new SearchUserListAdapter(this);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerViewUsers.setAdapter(searchUserListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        initSearchView(menu);

        return true;
    }

    private void initSearchView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        setupItemSearchView(searchItem);
        setupSearchView(searchItem);
    }


    private void setupItemSearchView(MenuItem searchItem) {
        searchItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });
    }

    private void setupSearchView(MenuItem searchItem) {
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        requestUser(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private void requestUser(final String user) {
        YaLlegueApplication llegueApplication = YaLlegueApplication.create(this);

        DataService dataService = llegueApplication.getDataService();
        dataService.getUsers(user).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(llegueApplication.subscribeScheduler()).subscribe(new Action1<UserResponse>() {
            @Override
            public void call(UserResponse userResponse) {
                Log.d(LOG_TAG, "success");
                List<User> users = userResponse.getUserList();
                searchUserListAdapter.setUsers(users);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(LOG_TAG, "error");
                throwable.printStackTrace();
            }
        });

    }

    @OnClick(R.id.button_play)
    public void playGame() {

        Intent myIntent = new Intent(this, HomeActivity.class);
        myIntent.putExtra(PushNotificationApp.EXTRA_START, PushNotificationApp.START_GAME);
        startActivity(myIntent);
        finish();
    }


}
