package br.com.radiofederal.app;

import br.com.radiofederal.app.util.QuitDialogUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
         //       WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		setContentView(R.layout.activity_splash_screen);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException interruptedException) {
					
				} finally {
					startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
					finish();
				}
				
			}
		}).start();
	}


	@Override
	public void onBackPressed() {
		QuitDialogUtil.openQuitDialog(this);		
	}
}
