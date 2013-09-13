package br.com.radiofederal.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.com.radiofederal.app.util.ConnectionUtil;
import br.com.radiofederal.app.util.MediaPlayerFacade;
import br.com.radiofederal.app.util.QuitDialogUtil;

public class OnLiveActivity extends Activity {

	private boolean playing;
	private boolean prepareCalled;

	private Handler mHandler = new Handler();
	public static final int TEN_SECONDS = 10000;
	
	private Runnable periodicTask = new Runnable() {
		public void run() {
			refreshDisplayText();
			mHandler.postDelayed(periodicTask, TEN_SECONDS);
		}
	};

	private void refreshDisplayText() {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					try {
						Document d = Jsoup.parse(new URL("http://radiofederal.com.br/topoplayer/textoplayer.php"), 10000);
						final String text = d.select("marquee").first().html();
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {

								TextView playerReproduzindo = (TextView) findViewById(R.id.playerReproduzindo);
								playerReproduzindo.setText(text);
							}
						});
					} catch (MalformedURLException e) {
					} catch (IOException e) {
					}
				} catch (Exception e) {
					
				}
			}
		}).start();
		
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_live);

		TextView playerReproduzindo = (TextView) this
				.findViewById(R.id.playerReproduzindo);
		playerReproduzindo.setSelected(true);

		TextView playerLogo = (TextView) this.findViewById(R.id.playerLogo);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/LiquidCrystal-LightItalic.otf");
		playerReproduzindo.setTypeface(typeface);
		playerLogo.setTypeface(typeface);

		

		final Button listen = (Button) this
				.findViewById(R.id.onLiveListenButton);
		listen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ConnectionUtil.checkConnection(OnLiveActivity.this, true,
						null)) {

					if (playing) {

						MediaPlayerFacade.pause();
						listen.setText("Ouvir");

						playing = false;

					} else {

						if (!prepareCalled) {
							MediaPlayerFacade
									.prepareAsync("http://streaming19.brlogic.com:8152/live"); // http://www.robtowns.com/music/blind_willie.mp3
							prepareCalled = true;
							MediaPlayerFacade.play();
						} else {
							MediaPlayerFacade.resume();
						}

						listen.setText("Parar");

						playing = true;

					}

				}
			}
		});
		
		mHandler.postDelayed(periodicTask, TEN_SECONDS);

	}
	

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(periodicTask);
    }
    
    @Override
	public void onBackPressed() {
		QuitDialogUtil.openQuitDialog(this);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.on_live, menu);
		return true;
	}

}
