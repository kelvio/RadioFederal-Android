package br.com.radiofederal.app;

import br.com.radiofederal.app.util.QuitDialogUtil;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		WebView wv = (WebView) this.findViewById(R.id.webView1);
		wv.loadUrl("file:///android_asset/about.html");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		QuitDialogUtil.openQuitDialog(this);		
	}
}
