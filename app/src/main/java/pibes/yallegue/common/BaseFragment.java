package pibes.yallegue.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Jhordan on 05/03/16.
 * <p>
 * * <p>
 * A fragment like an activity only will execute operations that affect the UI.
 * These operations are defined by a view model and are triggered by its presenter.
 * </p>
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initView(view, savedInstanceState);
    }

    /**
     * Use this method to initialize view components. This method is called after {@link BaseActivity#bindViews()}
     */
    public void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView() {
        unbindViews();
        super.onDestroyView();
    }

    /**
     * Replace all the annotated fields with ButterKnife annotations with the proper value
     */
    private void bindViews(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    private void unbindViews() {
        ButterKnife.unbind(this);
    }

    public void finishActivity() {
        getActivity().finish();
    }

    public FragmentManager getActivitySupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    public ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }


    public void setReplaceTransaction(int container, Fragment fragment) {

        getActivitySupportFragmentManager()
                .beginTransaction()
                        // TODO CHECK PERMISSION.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_left)
                .replace(container, fragment)
                .addToBackStack(Fragment.class.getName())
                .commit();

    }

    @Nullable
    public Toolbar getToolbar() {
        return ((BaseActivity) getActivity()).getToolbar();
    }

    /**
     * Specify the layout of the fragment to be inflated in the {@link BaseFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    protected abstract int getFragmentLayout();


}
