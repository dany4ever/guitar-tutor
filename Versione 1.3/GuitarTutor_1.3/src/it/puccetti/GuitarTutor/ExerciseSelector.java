package it.puccetti.GuitarTutor;

import it.puccetti.GuitarTutor.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ExerciseSelector extends Activity implements OnClickListener {

	MediaPlayer m_mediaPlayer;
	Button m_buttonPlay = null;
	Button m_buttonPause = null;
	Button m_buttonBack = null;
	Button m_buttonRew = null;
	CheckBox m_chkLoop = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.exercise_selector);

		Bundle b = getIntent().getExtras();

		String audio = null;
		String immmagine = null;
		audio = b.getString("audio");
		immmagine = b.getString("img");

		/* IMPOSTO L'IMMAGINE DELL'ESERCIZIO DA VISUALIZZARE */
		ImageView img = (ImageView) findViewById(R.id.imgex);
		img.setMinimumHeight(600);
		img.setMinimumHeight(200);
		// SWITCH SULL'ESERCIZIO DA VISUALIZZARE

		if (immmagine.equalsIgnoreCase("e_cromatici_4_dita_1.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this,
					R.raw.e_cromatici_4_dita_1_a);
			img.setImageResource(R.raw.e_cromatici_4_dita_1);
		}
		if (immmagine.equalsIgnoreCase("e_cromatici_4_dita_2.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this,
					R.raw.e_cromatici_4_dita_2_a);
			img.setImageResource(R.raw.e_cromatici_4_dita_2);
		}

		if (immmagine.equalsIgnoreCase("e_cromatici_4_dita_3.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this,
					R.raw.e_cromatici_4_dita_3_a);
			img.setImageResource(R.raw.e_cromatici_4_dita_3);
		}

		if (immmagine.equalsIgnoreCase("e_cromatici_4_dita_4.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this,
					R.raw.e_cromatici_4_dita_4_a);
			img.setImageResource(R.raw.e_cromatici_4_dita_4);
		}

		if (immmagine.equalsIgnoreCase("e_cromatici_4_dita_5.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this,
					R.raw.e_cromatici_4_dita_5_a);
			img.setImageResource(R.raw.e_cromatici_4_dita_5);
		}

		if (immmagine.equalsIgnoreCase("e_penta_1.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this, R.raw.e_penta_1_a);
			img.setImageResource(R.raw.e_penta_1);
		}

		if (immmagine.equalsIgnoreCase("e_minore_1.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this, R.raw.e_minore_1_a);
			img.setImageResource(R.raw.e_minore_1);
		}

		if (immmagine.equalsIgnoreCase("e_maggiore_1.jpg") == true) {

			m_mediaPlayer = MediaPlayer.create(this, R.raw.e_maggiore_1_a);
			img.setImageResource(R.raw.e_maggiore_1);
		}

		m_buttonPlay = (Button) findViewById(R.id.playButton);
		m_buttonPause = (Button) findViewById(R.id.stopButton);
		m_buttonBack = (Button) findViewById(R.id.backButton);
		m_buttonRew = (Button) findViewById(R.id.rewButton);
		m_chkLoop = (CheckBox) findViewById(R.id.checkloop);
		m_buttonPlay.setOnClickListener(buttonPlayOnClickListener);
		m_buttonPause.setOnClickListener(buttonPauseOnClickListener);
		m_buttonBack.setOnClickListener(buttonBackOnClickListener);
		m_buttonRew.setOnClickListener(buttonRewOnClickListener);
		m_chkLoop.setOnClickListener(chcBoxOnClickListener);
	}

	// Listeners

	CheckBox.OnClickListener chcBoxOnClickListener = new CheckBox.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (m_chkLoop.isChecked()) {
				m_mediaPlayer.setLooping(true);

			} else {
				m_mediaPlayer.setLooping(false);
			}
		}
	};

	Button.OnClickListener buttonPlayOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (!m_mediaPlayer.isPlaying()) {
				m_mediaPlayer.start();
				m_buttonPlay.setEnabled(false);
				m_buttonPause.setEnabled(true);
				m_chkLoop.setEnabled(false);
			}
		}
	};

	Button.OnClickListener buttonPauseOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (m_mediaPlayer.isPlaying()) {
				m_mediaPlayer.pause();
				m_buttonPlay.setEnabled(true);
				m_buttonPause.setEnabled(false);
				m_chkLoop.setEnabled(true);
			}
		}
	};

	Button.OnClickListener buttonBackOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (m_mediaPlayer.isPlaying()) {
				m_mediaPlayer.pause();
			}
			// back
			finish();
		}
	};

	Button.OnClickListener buttonRewOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (!m_mediaPlayer.isPlaying()) {
				m_mediaPlayer.seekTo(0);
			}
		}
	};

	public void onDestroy() {

		if (m_mediaPlayer.isPlaying()) {
			m_mediaPlayer.pause();
		}
		super.onDestroy();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}