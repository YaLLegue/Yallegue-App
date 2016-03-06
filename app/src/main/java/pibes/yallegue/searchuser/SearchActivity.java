package pibes.yallegue.searchuser;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import pibes.yallegue.R;
import pibes.yallegue.common.BaseActivity;
import pibes.yallegue.data.DataFactory;

public class SearchActivity extends BaseActivity implements SearchView.OnQueryTextListener  {

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        super.initView();


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


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
