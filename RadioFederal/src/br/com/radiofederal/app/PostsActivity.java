package br.com.radiofederal.app;

import java.util.ArrayList;
import java.util.List;

import br.com.radiofederal.app.util.ConnectionUtil;
import br.com.radiofederal.app.util.QuitDialogUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PostsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_posts);
		
		((Button) this.findViewById(R.id.postsBotaoAtualizar)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ConnectionUtil.checkConnection(PostsActivity.this, true, null);
				
			}
		});
		
		this.prepareList();
		
	}
	
	private void prepareList() {
		
		List<String> list = new ArrayList<String>();
		
		list.add("HOJE, às 20h, o programa RUIDO URBANO");
		list.add("Programa Papo Firme – Especial Porão do Rock 27/08/2013");
		list.add("Light Hits – 24/08/2013");
		list.add("Programa Momento Intolerante – 23/08/2013");
		list.add("RUIDO URBANO & O ROCK BRASILIENSE");		
				
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		ListView listView = (ListView) this.findViewById(R.id.postsListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
								
				//Intent i = new Intent();
				//i.setClass(PostsActivity.this, DayViewerActivity.class);
				//i.putExtra("index", position);
				//startActivity(i);
				
			}
		});
		listView.setAdapter(arrayAdapter);		
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
}
