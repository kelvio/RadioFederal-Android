package br.com.radiofederal.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class QuitDialogUtil {

	public static void openQuitDialog(final Activity context) {
		AlertDialog.Builder quitDialog = new AlertDialog.Builder(
				context);
		quitDialog.setTitle("Você realmente deseja sair?");

		quitDialog.setPositiveButton("Sim", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				MediaPlayerFacade.dispose();
				context.finish();
			}
		});

		quitDialog.setNegativeButton("Não", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		quitDialog.show();
	}
	
}
