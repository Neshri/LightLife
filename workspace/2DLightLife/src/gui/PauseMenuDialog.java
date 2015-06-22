package gui;

import game.first.lightlife.PlayActivity;
import game.first.lightlife.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class PauseMenuDialog extends DialogFragment{

	private PlayActivity activity;
	
	public PauseMenuDialog(PlayActivity activity) {
		activity.pauseCurrent();
		this.activity = activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.pause_menu);
		
		builder.setNeutralButton(R.string.options_menu, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.optionsMenu(true);
				
			}
		});
		builder.setNegativeButton(R.string.back_to_main, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   activity.mainMenu();
	               // User clicked OK button
	           }
	       });
		return builder.create();
	}
	
	@Override
	public void onCancel(DialogInterface di) {
		super.onCancel(di);
		activity.resumeCurrent();
	}
}
