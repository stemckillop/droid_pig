package com.drkdagron.pigdicegame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stemc on 2017-03-30.
 */

public class DiceRollDialog extends DialogFragment {

    int[] imgIndex = new int[] {R.drawable.diered_border1, R.drawable.diered_border2, R.drawable.diered_border3, R.drawable.diered_border4, R.drawable.diered_border5, R.drawable.diered_border6};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());

        LayoutInflater inflate = getActivity().getLayoutInflater();
        final View v = inflate.inflate(R.layout.dialog_rolldice, null);

        final TextView text = (TextView) v.findViewById(R.id.dialog_diceroll_text);
        final ImageView dice = (ImageView) v.findViewById(R.id.dialog_diceroll_image);

        int roll = getArguments().getInt("roll");
        int turnScore = getArguments().getInt("turn");
        if (roll == 1) {
            text.setText("Next players turn!");
        }
        else
        {
            text.setText("Current turn score is: " + String.valueOf(turnScore));
        }
        dice.setImageResource(imgIndex[roll-1]);

        build.setView(v).setTitle(String.valueOf(roll) + " was rolled!").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return build.create();

    }
}
