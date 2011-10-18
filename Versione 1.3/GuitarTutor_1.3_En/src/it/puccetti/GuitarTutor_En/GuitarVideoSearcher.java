package it.puccetti.GuitarTutor_En;

import it.puccetti.GuitarTutor_En.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class GuitarVideoSearcher extends Activity implements
		OnItemSelectedListener {

	private Spinner m_searchbox;
	private Button m_searchbut;
	private String m_oszCurSel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search_video);
		m_searchbox = (Spinner) findViewById(R.id.spinner);
		m_searchbut = (Button) findViewById(R.id.searchButton);

		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.videolessons, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		m_searchbox.setAdapter(adapter);
		m_searchbox.setOnItemSelectedListener(this);

		m_searchbut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				// Invio i parametri di ricerca al YTRetriverActivity
				Intent poIntent;
				// Passare l'id dell'utente per adesso solo l'indice della
				// lista

				poIntent = new Intent(v.getContext(), YTRetriverActivity.class);
				poIntent.putExtra("keysearch", m_oszCurSel);

				try {
					startActivityForResult(poIntent, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		m_oszCurSel = arg0.getSelectedItem().toString();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
