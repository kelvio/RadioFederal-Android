package br.com.radiofederal.app;

import java.util.ArrayList;
import java.util.List;

import br.com.radiofederal.app.util.QuitDialogUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProgrammingGuideActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_programming_guide);

		this.prepareList();
		
	}
	
	private void prepareList() {
		
		List<String> list = new ArrayList<String>();
		list.add("Domingo");
		list.add("Segunda");
		list.add("Terça");
		list.add("Quarta");
		list.add("Quinta");
		list.add("Sexta");
		list.add("Sábado");
				
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		ListView listView = (ListView) this.findViewById(R.id.programmingGuideListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
								
				Intent i = new Intent();
				i.setClass(ProgrammingGuideActivity.this, DayViewerActivity.class);
				i.putExtra("index", position);
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
