package br.com.radiofederal.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.radiofederal.app.CategoryPostsActivity.ResumoPostItemAdapter;
import br.com.radiofederal.app.model.ResumoPost;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PostViewerActivity extends Activity {

	private static final String TAG = "PostViewerActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_viewer);
		
		WebView webView = (WebView) findViewById(R.id.webViewActivityPostViewer);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarActivityPostViewer);
		//TextView tituloPost = (TextView) findViewById(R.id.tituloPostActivityPostViewer);
		//TextView autorPost = (TextView) findViewById(R.id.autorPostActivityPostViewer);
		//TextView dataPost = (TextView) findViewById(R.id.dataPostagemActivityPostViewer);
		//TextView comentariosPost = (TextView) findViewById(R.id.comentariosActivityPostViewer);
		
		webView.setVisibility(View.INVISIBLE);
		//tituloPost.setVisibility(View.INVISIBLE);
		//autorPost.setVisibility(View.INVISIBLE);
		//dataPost.setVisibility(View.INVISIBLE);
		//comentariosPost.setVisibility(View.INVISIBLE);
		
		progressBar.setVisibility(View.VISIBLE);
		
		final String url = this.getIntent().getStringExtra("url");
		
		AsyncTask<Void, Void, Document> a = new AsyncTask<Void, Void, Document>() {

			@Override
			protected Document doInBackground(Void... params) {				
				try {
					return Jsoup.parse(new URL(url), 10000);
				} catch (MalformedURLException e) {
					Log.e(TAG, "Falha ao obter dados do post:" + e.getMessage());
					return null;
				} catch (IOException e) {
					Log.e(TAG, "Falha ao obter dados do post:" + e.getMessage());
					return null;
				}
			}

			@Override
			protected void onPostExecute(final Document result) {
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						WebView webView = (WebView) findViewById(R.id.webViewActivityPostViewer);
						ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarActivityPostViewer);
						//TextView tituloPost = (TextView) findViewById(R.id.tituloPostActivityPostViewer);
						//TextView autorPost = (TextView) findViewById(R.id.autorPostActivityPostViewer);
						//TextView dataPost = (TextView) findViewById(R.id.dataPostagemActivityPostViewer);
						//TextView comentariosPost = (TextView) findViewById(R.id.comentariosActivityPostViewer);
						
						//webView.setVisibility(View.VISIBLE);
						//tituloPost.setVisibility(View.VISIBLE);
						//dataPost.setVisibility(View.VISIBLE);
						//autorPost.setVisibility(View.VISIBLE);
						//comentariosPost.setVisibility(View.VISIBLE);
						//progressBar.setVisibility(View.INVISIBLE);
						
						String titleString = "";
						
						try {
							
							//tituloPost.setText(StringEscapeUtils.unescapeHtml4(result.select(".blog-opts").first().child(1).child(0).html()));
							titleString += StringEscapeUtils.unescapeHtml4(result.select(".blog-opts").first().child(1).child(0).html());
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar título do post." + e.getMessage());
						}
						
						try {
							
							//autorPost.setText("Postado por: " + StringEscapeUtils.unescapeHtml4(result.select(".blog-opts").first().child(1).child(1).child(0).html()));
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar o autor do post." + e.getMessage());
						}
						
						try {
							
							//dataPost.setText("Em: " + StringEscapeUtils.unescapeHtml4(result.select(".blog-opts").first().child(0).child(0).html()));
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar o autor do post." + e.getMessage());
						}

						try {
							
							//comentariosPost.setText("0 Comentários");
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar comentários do post." + e.getMessage());
						}
						setTitle(titleString);
						try {
							
							String html = result.select(".blog-desc").first().toString();
							String mime = "text/html";
							String encoding = "utf-8";
							//webView.getSettings().setJavaScriptEnabled(true);
							webView.loadData(html, mime, encoding);							
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar conteúdo do post." + e.getMessage());
						}
						
						//result.select("#header").remove();
						//result.select("#footer").remove();
						
						//String html = result.toString();
						//String mime = "text/html";
						//String encoding = "utf-8";
						//webView.loadData(html, mime, encoding);
					}
				});
				
				
			}
			
			
			
		};

		a.execute();

		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_viewer, menu);
		return true;
	}

}
