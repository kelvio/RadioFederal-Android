package br.com.radiofederal.app;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class DayViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_programming_of_day_viewer);
		
		//ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.programmingGuideProgressBar);
		//progressBar.setVisibility(View.VISIBLE);
		
		WebView wv = (WebView) this.findViewById(R.id.programmingOfDayViewerWebView);
		//wv.setVisibility(View.INVISIBLE);
		WebSettings settings = wv.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		
		this.buscarProgramacao();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.programming_of_day_viewer, menu);
		return true;
	}
	
	/**
	 * 
	 */
	private void buscarProgramacao() {
		WebView wv = (WebView) findViewById(R.id.programmingOfDayViewerWebView);
		switch (this.getIntent().getExtras().getInt("index")) {
		case 0:	
			wv.loadUrl("file:///android_asset/programacao_domingo.html");
			//this.buscarAsync("domingo", "http://www.radiofederal.com.br/wordpress/programacao-domingo/");			
			break;
		case 1:
			wv.loadUrl("file:///android_asset/programacao_segunda.html");
			//this.buscarAsync("segunda-feira", "http://www.radiofederal.com.br/wordpress/programacao-segunda-feira/");
			break;
		case 2:			
			wv.loadUrl("file:///android_asset/programacao_terca.html");
			//this.buscarAsync("terça-feira", "http://www.radiofederal.com.br/wordpress/programacao-terca-feira-2/");
			break;
		case 3:			
			wv.loadUrl("file:///android_asset/programacao_quarta.html");
			//this.buscarAsync("quarta-feira", "http://www.radiofederal.com.br/wordpress/programacao-quarta-feira/");
			break;
		case 4:			
			wv.loadUrl("file:///android_asset/programacao_quinta.html");
			//this.buscarAsync("quinta-feira", "http://www.radiofederal.com.br/wordpress/programacao-terca-feira/");
			break;
		case 5:			
			wv.loadUrl("file:///android_asset/programacao_sexta.html");
			//this.buscarAsync("sexta-feira", "http://www.radiofederal.com.br/wordpress/programacao-sexta-feira/");
			break;
		case 6:
			wv.loadUrl("file:///android_asset/programacao_sabado.html");
			//this.buscarAsync("sábado", "http://www.radiofederal.com.br/wordpress/programacao-sabado/");
			break;					
		default:
			finish();
		};
	}
	
	private void buscarAsync(final String dia, final String url) {
		
		AsyncTask<Void, Void, String> a = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					Document d = Jsoup.parse(new URL(url), 10000);
					
					Element tabela = d.select(".pagetxt table").first();        
			        for (Element tr : tabela.child(0).children()) {
			            Element td = tr.child(0);
			            
			            if (td.children().size() > 0) {
			                
			                if ("img".equalsIgnoreCase(td.child(0).tagName())) {
			                    
			                    if (td.child(0).attr("alt").equalsIgnoreCase("PAPO FIRME BANNER")) {
			                        td.html("");                                                                                                
			                    }
			                    
			                }
			                
			            }            
			        }			        
					
					return "<html><head><meta charset=\"utf-8\" /></head><body> <h1>Programação de " + dia + "</h1>" + tabela + "</body></html>";
				} catch (Exception e) {
					return "";
				}				
			}

			@Override
			protected void onPostExecute(final String result) {
				super.onPostExecute(result);
				
				if ("".equals(result)) {
					finish();
				} else {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							ProgressBar progressBar = (ProgressBar) findViewById(R.id.programmingGuideProgressBar);
							progressBar.setVisibility(View.INVISIBLE);
							
							WebView wv = (WebView) findViewById(R.id.programmingOfDayViewerWebView);
							wv.setVisibility(View.VISIBLE);
							
							
							
							wv.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
							
							
							
						}
					});
				}
			}
			
			
			
			
		};
		
        a.execute();
        
	}

	
}
