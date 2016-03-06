package pibes.yallegue.home;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pibes.yallegue.R;

/**
 * Created by Jhordan on 05/03/16.
 */
public class HomeBottomSheetDialog extends DialogFragment {

    public static HomeBottomSheetDialog newInstance() {
        return new HomeBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_home_sheet_bottom, container, false);
        return view;
    }
}
