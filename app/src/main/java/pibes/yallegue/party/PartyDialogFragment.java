package pibes.yallegue.party;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pibes.yallegue.R;

/**
 * Created by Jhordan on 06/03/16.
 */
public class PartyDialogFragment extends DialogFragment {

    public static PartyDialogFragment newInstance() {
        return new PartyDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_party, container, false);

        return view;
    }
}
