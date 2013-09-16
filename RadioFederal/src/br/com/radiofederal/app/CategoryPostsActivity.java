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
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.radiofederal.app.model.ResumoPost;

public class CategoryPostsActivity extends Activity {

	private static final String TAG = "CategoryPostsActivity";
	private String url;

	private int categoryId;
	private int currentPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_posts);

		this.url = this.getIntent().getStringExtra("url");
		this.prepareList();

		Button more = (Button) findViewById(R.id.mostrarMaisPostsButtonActivityCategoryPosts);
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				currentPage++;
				buscarAsync();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category_posts, menu);
		return true;
	}

	private void prepareList() {

		this.buscarAsync();

	}

	private void buscarAsync() {

		if (currentPage == 1) {
			TextView tv = (TextView) findViewById(R.id.statusActivityCategoryPosts);
			tv.setVisibility(View.INVISIBLE);

			ProgressBar pb = (ProgressBar) this
					.findViewById(R.id.progressBarActivityCategoryPosts);
			pb.setVisibility(View.VISIBLE);

			ListView listView = (ListView) findViewById(R.id.listaPostsCategoriaActivityCategoryPosts);
			listView.setVisibility(View.INVISIBLE);
		}

		Button botaoAtualizar = (Button) findViewById(R.id.mostrarMaisPostsButtonActivityCategoryPosts);
		botaoAtualizar.setEnabled(false);

		AsyncTask<Void, Void, List<ResumoPost>> a = new AsyncTask<Void, Void, List<ResumoPost>>() {

			@Override
			protected List<ResumoPost> doInBackground(Void... params) {
				try {
					Document d = Jsoup.parse(new URL(url + "?page_id_all="
							+ currentPage + "&cat=" + categoryId), 10000);

					List<ResumoPost> l = new ArrayList<ResumoPost>();

					Elements posts = d.select(".in-sec");
					for (Element post : posts) {

						ResumoPost r = new ResumoPost();
						try {
							r.setData(StringEscapeUtils
									.unescapeHtml4(post.child(0).child(0)
											.child(0).child(0).html()));
						} catch (Exception e) {
							Log.e(TAG, "Não foi possível obter a data do post."
									+ e.getMessage());
						}

						try {
							r.setNome(StringEscapeUtils.unescapeHtml4(post
									.child(0).child(0).child(1).child(0)
									.child(0).html()));
							// r.setData(post.child(0).child(0).child(0).child(0).html());
						} catch (Exception e) {
							Log.e(TAG, "Não foi possível obter o nome do post."
									+ e.getMessage());
							continue;
						}

						try {
							r.setUrlPost(StringEscapeUtils.unescapeHtml4(post
									.child(0).child(0).child(1).child(0)
									.child(0).attr("href")));
							// r.setData(post.child(0).child(0).child(0).child(0).html());
						} catch (Exception e) {
							Log.e(TAG, "Não foi possível obter a url do post."
									+ e.getMessage());
						}

						try {
							r.setNomeAutor(StringEscapeUtils.unescapeHtml4(post
									.child(0).child(0).child(1).child(1)
									.child(0).html()));
							// r.setData(post.child(0).child(0).child(0).child(0).html());
						} catch (Exception e) {
							Log.e(TAG,
									"Não foi possível obter o nome do autor do post."
											+ e.getMessage());
						}

						try {
							String categorias = "| ";
							Elements elements = post.child(0).child(0).child(1)
									.child(3).children();
							for (Element e : elements) {
								categorias += e.html() + " |";
							}
							r.setNomeCategoria(StringEscapeUtils
									.unescapeHtml4(categorias));
							// r.setData(post.child(0).child(0).child(0).child(0).html());
						} catch (Exception e) {
							Log.e(TAG,
									"Não foi possível obter o nome da categoria do post."
											+ e.getMessage());
						}

						try {
							// r.setData(post.child(0).child(0).child(0).child(0).html());
							r.setNumeroComentarios(StringEscapeUtils
									.unescapeHtml4(""));
						} catch (Exception e) {
							Log.e(TAG,
									"Não foi possível obter o número de comentários do post."
											+ e.getMessage());
						}

						try {
							// r.setData(post.child(0).child(0).child(0).child(0).html());
							r.setResumo(StringEscapeUtils.unescapeHtml4(post
									.child(0).child(1).child(1).html().trim()));
						} catch (Exception e) {
							Log.e(TAG,
									"Não foi possível obter o resumo do post."
											+ e.getMessage());
						}

						l.add(r);
					}

					return l;

				} catch (Exception e) {
					return new ArrayList<ResumoPost>();
				}
			}

			@Override
			protected void onPostExecute(final List<ResumoPost> result) {
				super.onPostExecute(result);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Button botaoAtualizar = (Button) findViewById(R.id.mostrarMaisPostsButtonActivityCategoryPosts);
						botaoAtualizar.setEnabled(true);

						final ListView listView = (ListView) findViewById(R.id.listaPostsCategoriaActivityCategoryPosts);

						if (currentPage == 1) {
							ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarActivityCategoryPosts);
							pb.setVisibility(View.INVISIBLE);

							TextView tv = (TextView) findViewById(R.id.statusActivityCategoryPosts);
							if (result.isEmpty()) {

								tv.setText("Nenhum post encontrado.");
								tv.setVisibility(View.VISIBLE);

							} else {

								tv.setVisibility(View.INVISIBLE);
							}

							listView.setVisibility(View.VISIBLE);

							listView.setOnItemClickListener(new OnItemClickListener() {

								public void onItemClick(AdapterView<?> arg0,
										View view, int position, long id) {

									ResumoPostItemAdapter adapter = (ResumoPostItemAdapter) listView
											.getAdapter();
									ResumoPost resumo = adapter
											.getResumo(position);

									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setData(Uri.parse(resumo.getUrlPost()));
									startActivity(i);

								}
							});
							
							ResumoPost[] resumos = new ResumoPost[result.size()];
							for (int i = 0; i < result.size(); i++) {
								resumos[i] = result.get(i);
							}

							ResumoPostItemAdapter itemAdapter = new ResumoPostItemAdapter(
									CategoryPostsActivity.this,
									R.layout.podcast_list_item, resumos);
							listView.setAdapter(itemAdapter);

						} else {
							
							ResumoPostItemAdapter itemAdapter = (ResumoPostItemAdapter) listView.getAdapter();
							int oldItemAdapterSize = itemAdapter.getDatas().length;
							List<ResumoPost> r = new ArrayList<ResumoPost>();
							for (ResumoPost resumo : itemAdapter.getDatas()) {
								r.add(resumo);
							}
							
							for (ResumoPost resumo : result) {
								r.add(resumo);
							}
							
							ResumoPost[] resumos = new ResumoPost[r.size()];
							for (int i = 0; i < resumos.length; i++) {
								resumos[i] = r.get(i);
							}
							
							itemAdapter = new ResumoPostItemAdapter(
									CategoryPostsActivity.this,
									R.layout.podcast_list_item, resumos);
							listView.setAdapter(itemAdapter);
							listView.setSelection(oldItemAdapterSize - 1);
							
						}

					}
				});

			}

		};

		a.execute();

	}

	class ResumoPostItemAdapter extends ArrayAdapter<ResumoPost> {

		private Activity myContext;

		private ResumoPost[] datas;

		public ResumoPostItemAdapter(Context context, int textViewResourceId,
				ResumoPost[] objects) {
			super(context, textViewResourceId, objects);

			myContext = (Activity) context;
			datas = objects;
			
		}
		
		public ResumoPost[] getDatas() {
			return this.datas;
		}

		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = myContext.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.posts_list_item, null);

			ResumoPost resumo = this.datas[position];

			TextView tituloPost = (TextView) rowView
					.findViewById(R.id.tituloPostPostsListItem);

			tituloPost.setText(resumo.getNome());

			TextView dataPost = (TextView) rowView
					.findViewById(R.id.dataPostPostsListItem);
			dataPost.setText(resumo.getData());

			TextView autorPost = (TextView) rowView
					.findViewById(R.id.nomeAutorPostsListItem);
			autorPost.setText(resumo.getNomeAutor());

			TextView numeroComentariosPost = (TextView) rowView
					.findViewById(R.id.numeroComentariosPostsListItem);
			numeroComentariosPost.setText(resumo.getNumeroComentarios());

			TextView nomeCategoriaPost = (TextView) rowView
					.findViewById(R.id.nomeCategoriaPostsListItem);
			nomeCategoriaPost.setText(resumo.getNomeCategoria());

			TextView resumoPost = (TextView) rowView
					.findViewById(R.id.textoResumoPostsListItem);
			resumoPost.setText(resumo.getResumo());

			return rowView;
		}

		public ResumoPost getResumo(int position) {
			return this.datas[position];
		}

	}

}
