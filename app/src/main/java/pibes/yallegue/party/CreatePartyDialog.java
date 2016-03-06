package pibes.yallegue.party;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import pibes.yallegue.R;
import pibes.yallegue.searchuser.SearchActivity;

/**
 * Created by Jhordan on 05/03/16.
 */
public class CreatePartyDialog extends DialogFragment {

    public static CreatePartyDialog newInstance() {
        return new CreatePartyDialog();
    }

    @Bind(R.id.button_invite)
    Button mButtonInvite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_fragment, container, false);
        ButterKnife.bind(this, view);

        mButtonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
        return view;
    }
}
