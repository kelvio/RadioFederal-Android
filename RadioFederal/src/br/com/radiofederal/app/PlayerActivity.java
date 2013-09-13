package br.com.radiofederal.app;

import br.com.radiofederal.app.util.QuitDialogUtil;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class PlayerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TextView playerReproduzindo = (TextView) this.findViewById(R.id.playerReproduzindo);
		playerReproduzindo.setSelected(true);
		
		TextView playerLogo = (TextView) this.findViewById(R.id.playerLogo);
		
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/LiquidCrystal-LightItalic.otf");
		playerReproduzindo.setTypeface(typeface);
		playerLogo.setTypeface(typeface);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

	
}
