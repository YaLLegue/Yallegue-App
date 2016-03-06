package pibes.yallegue.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pibes.yallegue.R;

/**
 * Created by Edgar Salvador Maurilio on 06/03/2016.
 */
public class WinnnerDialog extends DialogFragment {


    public static WinnnerDialog newIntancer(String typeMessage) {
        WinnnerDialog winnnerDialog = new WinnnerDialog();
        Bundle bundle = new Bundle();
        bundle.putString("mesa", typeMessage);
        winnnerDialog.setArguments(bundle);
        return winnnerDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_winner, null, false);

        TextView textView = (TextView) view.findViewById(R.id.textViewWinner);

        textView.setText(getArguments().getString("mesa"));
        builder.setView(view);
        builder.setPositiveButton("Ok ", null);


        return builder.create();
    }
}
