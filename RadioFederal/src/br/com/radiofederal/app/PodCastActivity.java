package br.com.radiofederal.app;

import java.util.ArrayList;
import java.util.List;

import br.com.radiofederal.app.util.ConnectionUtil;
import br.com.radiofederal.app.util.QuitDialogUtil;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PodCastActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pod_cast);

		((Button) this.findViewById(R.id.podcastBotaoAtualizar))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						ConnectionUtil.checkConnection(PodCastActivity.this,
								true, null);

					}
				});

		this.prepareList();
	}

	private void prepareList() {

		List<String> list = new ArrayList<String>();
		list.add("Enerdizando - 20/07/2013");
		list.add("Falando Nisso - 18/07/2013");
		list.add("Federal Esportes - 07/07/2013");
		list.add("Light Hits - 20/07/2013");
		list.add("Momento Intolerante - 05/07/2013");
		list.add("Mulheres que Comandam - 18/07/2013");
		list.add("Papo Firme - 02/07/2013");

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		ListView listView = (ListView) this.findViewById(R.id.podcastListView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {

				Intent i = new Intent();
				i.setClass(PodCastActivity.this, PlayerActivity.class);
				i.putExtra("url", "");
				startActivity(i);

			}
		});
		listView.setAdapter(arrayAdapter);
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
}
