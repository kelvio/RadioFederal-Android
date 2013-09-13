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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.radiofederal.app.model.CanalBlog;
import br.com.radiofederal.app.util.ConnectionUtil;
import br.com.radiofederal.app.util.QuitDialogUtil;

public class PostsActivity extends Activity {

	private static final String TAG = "PostsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posts);

		((Button) this.findViewById(R.id.postsBotaoAtualizar))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (ConnectionUtil.checkConnection(PostsActivity.this,
								true, null)) {

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
		getMenuInflater().inflate(R.menu.posts, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		QuitDialogUtil.openQuitDialog(this);
	}

	private void buscarAsync() {

		TextView tv = (TextView) findViewById(R.id.postsResultStatusViewListaProgramas);
		tv.setVisibility(View.INVISIBLE);
		
		ProgressBar pb = (ProgressBar) this
				.findViewById(R.id.progressBarListaProgramas);
		pb.setVisibility(View.VISIBLE);

		ListView listView = (ListView) findViewById(R.id.postsListView);
		listView.setVisibility(View.INVISIBLE);
		

		Button botaoAtualizar = (Button) findViewById(R.id.postsBotaoAtualizar);
		botaoAtualizar.setEnabled(false);

		AsyncTask<Void, Void, List<CanalBlog>> a = new AsyncTask<Void, Void, List<CanalBlog>>() {

			@Override
			protected List<CanalBlog> doInBackground(Void... params) {
				try {
					Document d = Jsoup
							.parse(new URL(
									"http://www.radiofederal.com.br/wordpress/blog-2/"),
									10000);

					Elements canais = d.select(".cat-item");

					List<CanalBlog> l = new ArrayList<CanalBlog>();

					for (Element canal : canais) {

						CanalBlog c = new CanalBlog();
						try {

							c.setNome(StringEscapeUtils.unescapeHtml4(canal
									.child(0).html()));

							// p.setNome(podcast.child(1).html());
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar nome do canal do blog:"
									+ e.getMessage());
						}

						try {
							c.setUrl(canal.child(0).attr("href"));
						} catch (Exception e) {
							Log.e(TAG, "Falha ao buscar url do canal do blog:"
									+ e.getMessage());
						}

						try {
							canal.child(0).remove();
							c.setNumeroPosts(StringEscapeUtils
									.unescapeHtml4(canal.html().replace("(", "").replace(")", "").trim()));

						} catch (Exception e) {
							Log.e(TAG,
									"Falha ao buscar número de posts do canal:"
											+ e.getMessage());
							c.setNumeroPosts("0");
						}

						l.add(c);

					}

					return l;

				} catch (Exception e) {
					return new ArrayList<CanalBlog>();
				}
			}

			@Override
			protected void onPostExecute(final List<CanalBlog> result) {
				super.onPostExecute(result);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Button botaoAtualizar = (Button) findViewById(R.id.postsBotaoAtualizar);
						botaoAtualizar.setEnabled(true);

						ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarListaProgramas);
						pb.setVisibility(View.INVISIBLE);

//						List<String> list = new ArrayList<String>();

						TextView tv = (TextView) findViewById(R.id.postsResultStatusViewListaProgramas);
						if (result.isEmpty()) {

							tv.setText("Nenhum programa encontrado.");
							tv.setVisibility(View.VISIBLE);

						} else {

							tv.setVisibility(View.INVISIBLE);
						}

						//for (CanalBlog c : result) {
						//	list.add(c.getNome() + " - " + c.getNumeroPosts());
						//}

						
						
						
						//ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
						//		PostsActivity.this,
						//		android.R.layout.simple_list_item_1, list);
						final ListView listView = (ListView) findViewById(R.id.postsListView);
						listView.setVisibility(View.VISIBLE);

						listView.setOnItemClickListener(new OnItemClickListener() {

							public void onItemClick(AdapterView<?> arg0,
									View view, int position, long id) {

								
								CanalItemAdapter adapter = (CanalItemAdapter) listView.getAdapter();
								CanalBlog canal = adapter.getCanalBlog(position);
								
								Intent i = new Intent();
								i.setClass(PostsActivity.this,
										CategoryPostsActivity.class);
								i.putExtra("url", canal.getUrl());
								startActivity(i);

							}
						});
						
						CanalBlog[] canais = new CanalBlog[result.size()];
						for (int i = 0; i < result.size(); i++) {
							canais[i] = result.get(i);
						}
						
						CanalItemAdapter itemAdapter = new CanalItemAdapter(PostsActivity.this, R.layout.blog_canais_list_item, canais);
						listView.setAdapter(itemAdapter);
						

					}
				});

			}

		};

		a.execute();

	}

	class CanalItemAdapter extends ArrayAdapter<CanalBlog> {

		private Activity myContext;

		private CanalBlog[] datas;

		public CanalItemAdapter(Context context, int textViewResourceId,
				CanalBlog[] objects) {
			super(context, textViewResourceId, objects);

			myContext = (Activity) context;
			datas = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = myContext.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.blog_canais_list_item, null);
						

			TextView nomeCanalView = (TextView) rowView
					.findViewById(R.id.nomeCanalListItem);
			
			nomeCanalView.setText(datas[position].getNome());

			TextView quantidadePostsView = (TextView) rowView
					.findViewById(R.id.quantidadePostsCanalBlogCanal);
			quantidadePostsView.setText("(" + datas[position].getNumeroPosts() + ")");

			return rowView;
		}
		
		public CanalBlog getCanalBlog(int position) {
			return this.datas[position];
		}

	}

	
	
}
