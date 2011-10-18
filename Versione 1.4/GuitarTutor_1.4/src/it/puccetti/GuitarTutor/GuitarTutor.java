package it.puccetti.GuitarTutor;

import it.puccetti.GuitarTutor.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class GuitarTutor extends TabActivity {

	public static TabHost m_tabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.print("super.onCreate");
		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		m_tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		try {
			/*****************************************************************/
			/* CREAZIONE DEI TABS */
			/* PRIMO TAB */
			// Create an Intent to launch an Activity for the tab (to be reused)

			intent = new Intent().setClass(this,
					it.puccetti.GuitarTutor.GuitarVideoSearcher.class);
			// Initialize a TabSpec for each tab and add it to the TabHost
			
			spec = m_tabHost.newTabSpec("GuitarTutor").setIndicator("Video",
					res.getDrawable(R.drawable.ic_tab_guitar_lessons))
					.setContent(intent);
			
			m_tabHost.addTab(spec);
			/* SECONDO TAB */
			intent = new Intent().setClass(this,
					it.puccetti.GuitarTutor.GuitarLessonsActivity.class);
			spec = m_tabHost.newTabSpec("GuitarTutor").setIndicator("Didattica",
					res.getDrawable(R.drawable.ic_tab_guitar_teaching))
					.setContent(intent);
			m_tabHost.addTab(spec);

			/* TERZO TAB */
			intent = new Intent().setClass(this,
					it.puccetti.GuitarTutor.GuitarExercisesActivity.class);
			spec = m_tabHost.newTabSpec("GuitarTutor").setIndicator("Esercizi",
					res.getDrawable(R.drawable.ic_tab_guitar_exercises))
					.setContent(intent);
			m_tabHost.addTab(spec);

			/* QUARTO TAB */
			intent = new Intent().setClass(this,
					it.puccetti.GuitarTutor.GuitarExerciseDiaryActivity.class);
			spec = m_tabHost.newTabSpec("GuitarTutor").setIndicator("Diario di allenamento",
					res.getDrawable(R.drawable.ic_tab_guitar_diary))
					.setContent(intent);
			m_tabHost.addTab(spec);

			/* QUINTO TAB */
			intent = new Intent().setClass(this,
					it.puccetti.GuitarTutor.GuitarExerciseDiaryHistoryActivity.class);
			spec = m_tabHost.newTabSpec("GuitarTutor").setIndicator("Storico Diario",
					res.getDrawable(R.drawable.ic_tab_guitar_diary_history))
					.setContent(intent);
			m_tabHost.addTab(spec);
		} catch (Exception e) {
			Toast.makeText(GuitarTutor.this, e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	private void setTabColors(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			// m_tabHost.getTabWidget().getChildAt(i).getLayoutParams().height =
			// 50;
			// m_tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.GRAY);

		}
	}
}