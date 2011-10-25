package it.puccetti.GuitarTutor;

import android.app.Activity;
import android.os.Bundle;

/**
 * Mostra un grafico dei progressi su uno specifico esercizio
 * @author Arno den Hond
 *
 */
public class StatisticsExActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float[] values = new float[] { 2.0f,1.5f, 2.5f, 1.0f , 3.0f };
		String[] verlabels = new String[] { "great", "ok", "bad" };
		String[] horlabels = new String[] { "today", "tomorrow", "next week", "next month" };
		GraphView graphView = new GraphView(this, values, "GraphViewDemo",horlabels, verlabels, GraphView.LINE);
		setContentView(graphView);
	}
}