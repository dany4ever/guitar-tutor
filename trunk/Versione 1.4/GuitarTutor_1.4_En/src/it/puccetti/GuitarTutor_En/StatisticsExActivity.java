package it.puccetti.GuitarTutor_En;

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
		//Recupero i parametri
		Bundle b = getIntent().getExtras();
		String titoloex = null;
		String numeroex = null;
		titoloex = b.getString("esercizio");
		numeroex = b.getString("numero");
		//Titolo parametro		
		//Punti verticali bpm esercizio
		//Punti horizzontali data esercizio convertita
		GraphView graphView = new GraphView(this, titoloex, numeroex, GraphView.LINE);
		setContentView(graphView);
	}
}
