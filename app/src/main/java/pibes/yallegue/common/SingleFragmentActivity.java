package pibes.yallegue.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import pibes.yallegue.R;


/**
 * Created by Jhordan on 05/03/16.
 * <p/>
 * Use this activity when you only need an activity to instance a Fragment
 */
public abstract class SingleFragmentActivity extends BaseActivity {

    @Override
    public void initView() {
        super.initView();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, createFragment())
                    .commit();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_container;
    }

    protected abstract Fragment createFragment();
}
