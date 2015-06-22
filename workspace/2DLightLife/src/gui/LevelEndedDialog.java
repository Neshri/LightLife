package gui;

import game.first.lightlife.PlayActivity;
import game.first.lightlife.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class LevelEndedDialog extends DialogFragment {

	private PlayActivity act;

	public LevelEndedDialog(PlayActivity act) {
		this.act = act;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.level_complete);

		builder.setNeutralButton(R.string.back_to_main,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						act.mainMenu();
					}
				});
		builder.setPositiveButton(R.string.next_level,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// User clicked OK button
					}
				});
		return builder.create();
	}
}
