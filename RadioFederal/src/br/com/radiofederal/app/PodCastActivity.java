package br.com.radiofederal.app;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.radiofederal.app.model.CanalBlog;
import br.com.radiofederal.app.model.Podcast;
import br.com.radiofederal.app.util.ConnectionUtil;
import br.com.radiofederal.app.util.MediaPlayerFacade;
import br.com.radiofederal.app.util.QuitDialogUtil;

public class PodCastActivity extends Activity {

	private static final String TAG = "PodCastActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pod_cast);

		((Button) this.findViewById(R.id.podcastBotaoAtualizar))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (ConnectionUtil.checkConnection(
								PodCastActivity.this, true, null)) {

							buscarAsync();

						}

					}
				});

		this.prepareList();
	}

	private void prepareList() {

		this.buscarAsync();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.programming_guide, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		QuitDialogUtil.openQuitDialog(this);
	}

	private void buscarAsync() {

		TextView tv = (TextView) findViewById(R.id.podcastResultStatusView);
		tv.setVisibility(View.INVISIBLE);

		ProgressBar pb = (ProgressBar) this
				.findViewById(R.id.progressBarListaPodcasts);
		pb.setVisibility(View.VISIBLE);

		ListView listView = (ListView) findViewById(R.id.podcastListView);
		listView.setVisibility(View.INVISIBLE);

		Button botaoAtualizar = (Button) findViewById(R.id.podcastBotaoAtualizar);
		botaoAtualizar.setEnabled(false);

		AsyncTask<Void, Void, List<Podcast>> a = new AsyncTask<Void, Void, List<Podcast>>() {

			@Override
			protected List<Podcast> doInBackground(Void... params) {
				try {
					Document d = Jsoup
							.parse(new URL(
									"http://www.radiofederal.com.br/wordpress/podcast-2/"),
									10000);

					Elements podcasts = d.select(".podPress_downloadlinks");

					List<Podcast> l = new ArrayList<Podcast>();

					for (Element podcast : podcasts) {

						Podcast p = new Podcast();
						try {
							String s = podcast.child(1).html();

							p.setNome(StringEscapeUtils.unescapeHtml4(s
									.substring(0, s.indexOf("-"))));
						} catch (Exception e) {
							Log.e(TAG,
									"Falha ao buscar nome do podcast:"
											+ e.getMessage());
						}

						try {
							String s = podcast.child(1).html();

							p.setData(StringEscapeUtils.unescapeHtml4(s
									.substring(s.indexOf("-")).replace("-", "")
									.trim()));
						} catch (Exception e) {
							Log.e(TAG,
									"Falha ao buscar a data do podcast:"
											+ e.getMessage());
						}

						try {
							p.setUrl(StringEscapeUtils.unescapeHtml4(podcast
									.child(0).attr("href")));
						} catch (Exception e) {
							Log.e(TAG,
									"Falha ao buscar url do podcast:"
											+ e.getMessage());
						}

						try {
							p.setDuracao(StringEscapeUtils
									.unescapeHtml4(podcast.child(2).html()));
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar duração do podcast:"
									+ e.getMessage());
						}

						l.add(p);

					}

					return l;

				} catch (Exception e) {
					return new ArrayList<Podcast>();
				}
			}

			@Override
			protected void onPostExecute(final List<Podcast> result) {
				super.onPostExecute(result);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Button botaoAtualizar = (Button) findViewById(R.id.podcastBotaoAtualizar);
						botaoAtualizar.setEnabled(true);

						ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarListaPodcasts);
						pb.setVisibility(View.INVISIBLE);

						// List<String> list = new ArrayList<String>();

						TextView tv = (TextView) findViewById(R.id.podcastResultStatusView);
						if (result.isEmpty()) {

							tv.setText("Nenhum podcast encontrado.");
							tv.setVisibility(View.VISIBLE);

						} else {

							tv.setVisibility(View.INVISIBLE);
						}

						// for (Podcast p : result) {
						// list.add(p.getNome() + " - " + p.getDuracao());
						// }

						/*
						 * list.add("Enerdizando - 20/07/2013");
						 * list.add("Falando Nisso - 18/07/2013");
						 * list.add("Federal Esportes - 07/07/2013");
						 * list.add("Light Hits - 20/07/2013");
						 * list.add("Momento Intolerante - 05/07/2013");
						 * list.add("Mulheres que Comandam - 18/07/2013");
						 * list.add("Papo Firme - 02/07/2013");
						 */

						// ArrayAdapter<String> arrayAdapter = new
						// ArrayAdapter<String>(PodCastActivity.this,
						// android.R.layout.simple_list_item_1, list);

						ListView listView = (ListView) findViewById(R.id.podcastListView);
						listView.setVisibility(View.VISIBLE);

						listView.setOnItemClickListener(new OnItemClickListener() {

							public void onItemClick(AdapterView<?> arg0,
									View view, int position, long id) {

								Intent i = new Intent();
								i.setClass(PodCastActivity.this,
										PlayerActivity.class);
								i.putExtra("url", "");
								startActivity(i);

							}
						});

						Podcast[] podcasts = new Podcast[result.size()];

						for (int i = 0; i < result.size(); i++) {
							podcasts[i] = result.get(i);
						}

						listView.setAdapter(new PodcastItemAdapter(
								PodCastActivity.this,
								R.layout.podcast_list_item, podcasts));

					}
				});

			}

		};

		a.execute();

	}

	class PodcastItemAdapter extends ArrayAdapter<Podcast> {

		private Activity myContext;

		private Podcast[] datas;
				

		public PodcastItemAdapter(Context context, int textViewResourceId,
				Podcast[] objects) {
			super(context, textViewResourceId, objects);

			myContext = (Activity) context;
			datas = objects;
			
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			LayoutInflater inflater = myContext.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.podcast_list_item, null);

			TextView nomePodcast = (TextView) rowView
					.findViewById(R.id.nomePodcastListItem);

			nomePodcast.setText(datas[position].getNome());

			TextView dataPodcast = (TextView) rowView
					.findViewById(R.id.dataPodcast);

			dataPodcast.setText(datas[position].getData());

			TextView tamanhoPodcast = (TextView) rowView
					.findViewById(R.id.tamanhoPodcast);

			tamanhoPodcast.setText(datas[position].getDuracao());

			final Button b = (Button) rowView
					.findViewById(R.id.ouvirPodcastButton);

			
			
			if (MediaPlayerFacade.getDataSource() != null
					&& MediaPlayerFacade.getDataSource().equals(
							datas[position].getUrl())) {

				if (MediaPlayerFacade.isPlaying()) {
					b.setText("Parar");
				}

			} else {
				b.setText("Ouvir");
			}

			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					for (int i = 0; i < datas.length; i++) {
						Button button = (Button) PodcastItemAdapter.this.getView(position, convertView, parent).findViewById(R.id.ouvirPodcastButton);
						button.setText("Ouvir");
					}
					
					if (ConnectionUtil.checkConnection(PodCastActivity.this,
							true, null)) {
						
						if (MediaPlayerFacade.isPlaying()) {

							MediaPlayerFacade.pause();
							b.setText("Ouvir");

						} else {

							if (MediaPlayerFacade.getDataSource() == null
									|| !MediaPlayerFacade.getDataSource()
											.equals(datas[position].getUrl())) {
								
								MediaPlayerFacade.dispose();
								MediaPlayerFacade.prepareAsync(datas[position]
										.getUrl());
								MediaPlayerFacade.play();
							} else {
								MediaPlayerFacade.resume();
							}

							
							
							b.setText("Parar");
							

						}

					}

				}
			});

			return rowView;
		}

	}

}
