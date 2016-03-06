package pibes.yallegue.party;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
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
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.label_new_party)
    public void onClickNewParty() {
        CreatePartyDialog createPartyDialog = CreatePartyDialog.newInstance();
        createPartyDialog.show(getActivity().getFragmentManager(), "");

    }
}
