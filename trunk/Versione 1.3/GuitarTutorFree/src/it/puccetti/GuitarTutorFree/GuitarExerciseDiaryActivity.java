package it.puccetti.GuitarTutorFree;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class GuitarExerciseDiaryActivity extends ListActivity implements
		OnItemSelectedListener {
	private static final int DIALOG_TUTORIAL = 0;
	private static final String DIALOG_TUTORIAL_MESSAGE = "Features non disponibile in questa versione!";
	private final String CHAPTER_TAG = "number";
	private final String TITLE_TAG = "title";
	private final String DESCR_TAG = "description";
	private final String HTML_SOURCE = "html_source";
	private final String IMAGE_SORCE = "image";
	private final String AUDIO_SORCE = "audio";
	private ArrayList<Elemento> m_ListaElementi = null;
	private GTutorAdapter m_adapter = null;
    
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Elemento curEl = null;

		//Versione Lite esco e non entro
		showDialog(DIALOG_TUTORIAL);
		
		InputStream in;
		DocumentBuilder builder;
		Document doc;
		NodeList title;
		NodeList desc;
		NodeList chapter;
		NodeList img_ex;
		NodeList audio_ex;
		
		
		try {

			try {
				curEl = null;

				in = getResources().openRawResource(R.raw.exercises);
				builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				doc = builder.parse(in, null);
				title = doc.getElementsByTagName(TITLE_TAG);
				desc = doc.getElementsByTagName(DESCR_TAG);
				chapter = doc.getElementsByTagName(CHAPTER_TAG);
				img_ex = doc.getElementsByTagName(IMAGE_SORCE);
				audio_ex = doc.getElementsByTagName(AUDIO_SORCE);

				for (int i = 0; i < title.getLength(); i++) {
					curEl = new Elemento();
					curEl.m_sDescrizione = "";
					curEl.m_sTitolo = "";
					curEl.m_sAudio = "";
					curEl.m_sImage = "";
					curEl.m_sNumber = "";

					curEl.m_sNumber = chapter.item(i).getFirstChild()
							.getNodeValue();
					curEl.m_sTitolo = chapter.item(i).getFirstChild()
							.getNodeValue()
							+ ". "
							+ title.item(i).getFirstChild().getNodeValue();
					curEl.m_sDescrizione = desc.item(i).getFirstChild()
							.getNodeValue();
					curEl.m_sImage = img_ex.item(i).getFirstChild()
							.getNodeValue();
					curEl.m_sAudio = audio_ex.item(i).getFirstChild()
							.getNodeValue();
					if (m_ListaElementi == null) {
						m_ListaElementi = new ArrayList();
					}
					m_ListaElementi.add(curEl);

				}
				in.close();

				title = null;
				desc = null;
			} catch (Throwable t) {
				Toast.makeText(this, "Exception: " + t.toString(), 2000).show();
			}

			/*
			 * Imposto l'animazione
			 */
			AnimationSet set = new AnimationSet(true);

			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(50);
			set.addAnimation(animation);

			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			animation.setDuration(100);
			set.addAnimation(animation);

			LayoutAnimationController controller = new LayoutAnimationController(
					set, 0.5f);

			if (m_adapter == null) {

				m_adapter = new GTutorAdapter(this,
						R.layout.list_chapters_diary_ex, m_ListaElementi);
			}

			setListAdapter(m_adapter);

			/*************************/
			ListView lv = getListView();
			lv.setEnabled(false);
			lv.setTextFilterEnabled(true);

			lv.setSelector(R.drawable.listviewselector);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					// DISPATCH ALL'ACTIVITY DEI DETTAGLI
					Intent poIntent;
					String sTitle = null;
					String sNumber = null;

					sNumber = m_ListaElementi.get(position).m_sNumber;
					sTitle = m_ListaElementi.get(position).m_sTitolo;

					poIntent = new Intent(view.getContext(),
							GuitarDiaryDetails.class);

					poIntent.putExtra("chnumber", sNumber);
					poIntent.putExtra("chtitle", sTitle);
					startActivityForResult(poIntent, 0);
				}

			});

			// Listener
			lv.setOnItemSelectedListener(this);

			in = null;
			builder = null;
			doc = null;
			title = null;
			desc = null;
			chapter = null;
			img_ex = null;
			audio_ex = null;

		} catch (Exception e) {
			Log.w("Warning", e.getMessage());
		}
	}

	public void onDestroy() {
		super.onDestroy();
		if (m_ListaElementi != null) {
			m_ListaElementi.clear();
			m_ListaElementi = null;
		}

		m_adapter.clear();

	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		m_adapter.update();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		m_adapter.update();
	}

	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		Dialog dRet = null;
		AlertDialog.Builder builder = null;
		switch (id) {
		case DIALOG_TUTORIAL:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(DIALOG_TUTORIAL_MESSAGE)
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});
			dRet = builder.create();

			break;
		}
		return dRet;
	}

	
}