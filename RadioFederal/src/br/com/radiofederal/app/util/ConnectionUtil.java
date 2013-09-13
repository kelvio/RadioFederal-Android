package br.com.radiofederal.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionUtil {

	public static boolean checkConnection(Context context, boolean makeToastWhenNotConnected, String toastText) {
		if (toastText == null) {
			toastText = "Sem conexão.";
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
			if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
				// record the fact that there is not connection				
				
				if (makeToastWhenNotConnected) {
					Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
				}
				return false;
				
			}
		}
		return true;
		
	}
	
}

