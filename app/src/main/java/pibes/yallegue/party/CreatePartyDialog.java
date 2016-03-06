package pibes.yallegue.party;

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
public class CreatePartyDialog extends DialogFragment {

    public static CreatePartyDialog newInstance() {
        return new CreatePartyDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_fragment, container, false);
        return view;
    }
}
