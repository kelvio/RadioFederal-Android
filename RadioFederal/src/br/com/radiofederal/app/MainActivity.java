package br.com.radiofederal.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import br.com.radiofederal.app.util.ConnectionUtil;
import br.com.radiofederal.app.util.MediaPlayerFacade;
import br.com.radiofederal.app.util.QuitDialogUtil;

public class MainActivity extends Activity {

	LocalActivityManager mLocalActivityManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

		ConnectionUtil.checkConnection(this, true, null);

		setContentView(R.layout.activity_main);

		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);

		final HorizontalScrollView h = (HorizontalScrollView) this
				.findViewById(R.id.hScrollView);

		h.post(new Runnable() {

			@Override
			public void run() {

				h.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						h.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
						
						h.postDelayed(new Runnable() {

							@Override
							public void run() {
								h.fullScroll(HorizontalScrollView.FOCUS_LEFT);
							}
						}, 1500);
						
					}
				}, 1000);
				
				

				
			}
		});

		mLocalActivityManager = new LocalActivityManager(this, false);

		tabHost.setup(mLocalActivityManager);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		TabHost.TabSpec spec;

		spec = tabHost
				.newTabSpec("tagname1")
				.setIndicator("Ao vivo",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new Intent().setClass(this, OnLiveActivity.class));
		tabHost.addTab(spec);

		spec = tabHost
				.newTabSpec("tagname2")
				.setIndicator("Programação")
				.setContent(
						new Intent().setClass(this,
								ProgrammingGuideActivity.class));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tagname2").setIndicator("Posts")
				.setContent(new Intent().setClass(this, PostsActivity.class));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tagname3").setIndicator("Podcasts")
				.setContent(new Intent().setClass(this, PodCastActivity.class));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tagname2").setIndicator("Sobre")
				.setContent(new Intent().setClass(this, AboutActivity.class));
		tabHost.addTab(spec);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLocalActivityManager.dispatchResume(); // you have to manually dispatch
												// the resume msg
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.share_menu, menu);
		getMenuInflater().inflate(R.menu.main, menu);
		// MenuItem item = menu.findItem(R.id.share_menu);
		// mShareActionProvider = (ShareActionProvider)
		// menu.findItem(10).getActionProvider();
		// mShareActionProvider.setShareIntent(getDefaultShareIntent());

		return true;
	}

	@Override
	public void onBackPressed() {
		QuitDialogUtil.openQuitDialog(this);		
	}

}
