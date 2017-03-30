package com.drkdagron.pigdicegame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by drkdagron on 2017-03-29.
 */

public class NewGameDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());

        LayoutInflater inflate = getActivity().getLayoutInflater();
        final View v = inflate.inflate(R.layout.dialog_newgame, null);

        final TextView p1Name = (TextView) v.findViewById(R.id.edt_newgame_player1);
        final TextView p2Name = (TextView) v.findViewById(R.id.edt_newgame_player2);

        String title = "New Game";
        Bundle get = getArguments();
        if (get != null) {
            if (get.containsKey("title")) {
                title = get.getString("title");
            }
        }

        build.setView(v).setTitle(title).setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IGameRules rules = (IGameRules)getActivity();
                rules.StartGame(p1Name.getText().toString(), p2Name.getText().toString());
            }
        }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return build.create();

    }
}
