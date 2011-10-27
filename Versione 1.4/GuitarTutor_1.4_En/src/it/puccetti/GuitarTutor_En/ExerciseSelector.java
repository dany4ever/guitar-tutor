package it.puccetti.GuitarTutor;

import android.R;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.Context;

public class ExerciseSelector extends Activity implements OnClickListener {

	MediaPlayer m_mediaPlayer = null;
	MediaPlayer m_mediaPlayerMetro = null;
	Button m_buttonPlay = null;
	Button m_buttonPause = null;
	Button m_buttonBack = null;
	Button m_buttonRew = null;
	CheckBox m_chkLoop = null;
	ToggleButton m_Toggle = null;
	Button m_plusBpm = null;
	Button m_minBpm = null;
	SeekBar m_Volume = null;
	TextView m_TextBpm = null;
	int m_maxVolume = 0;
	int m_curVolume = 0;
	String m_immagine = null;
	String m_audio = null;
	AudioManager m_audioManager = null;
	final static String[] m_BpmSource = { "60 bpm.mid", "64 bpm.mid",
			"68 bpm.mid", "72 bpm.mid", "78 bpm.mid", "84 bpm.mid",
			"90 bpm.mid", "96 bpm.mid", "100 bpm.mid", "106 bpm.mid",
			"110 bpm.mid", "116 bpm.mid", "120 bpm.mid", "124 bpm.mid",
			"130 bpm.mid", "136 bpm.mid", "140 bpm.mid", "146 bpm.mid",
			"150 bpm.mid" };
	int m_indexBpm = 0;
	private static final String CLICK_FOLDER = "data/data/it.puccetti.GuitarTutor/files/click/";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.exercise_selector);

		Bundle b = getIntent().getExtras();
		m_audio = b.getString("audio");
		m_immagine = b.getString("img");

		UpdateMediaPlayer(true, false);

		m_buttonPlay = (Button) findViewById(R.id.playButton);
		m_buttonPause = (Button) findViewById(R.id.stopButton);
		m_buttonBack = (Button) findViewById(R.id.backButton);
		m_buttonRew = (Button) findViewById(R.id.rewButton);
		m_chkLoop = (CheckBox) findViewById(R.id.checkloop);
		m_Toggle = (ToggleButton) findViewById(R.id.toggle);
		m_plusBpm = (Button) findViewById(R.id.buttonIncrBpm);
		m_minBpm = (Button) findViewById(R.id.buttonDecrBpm);
		m_Volume = (SeekBar) findViewById(R.id.seekBarVolume);
		m_TextBpm = (TextView) findViewById(R.id.textViewBpm);
		m_buttonPlay.setOnClickListener(buttonPlayOnClickListener);
		m_buttonPause.setOnClickListener(buttonPauseOnClickListener);
		m_buttonBack.setOnClickListener(buttonBackOnClickListener);
		m_buttonRew.setOnClickListener(buttonRewOnClickListener);
		m_chkLoop.setOnClickListener(chcBoxOnClickListener);
		m_Toggle.setOnClickListener(buttonToggleOnClickListener);
		m_plusBpm.setOnClickListener(buttonPlusBpmOnClickListener);
		m_minBpm.setOnClickListener(buttonMinBpmOnClickListener);
		m_Volume.setOnSeekBarChangeListener(seekVolumeOnChangeListener);

		m_audioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		m_maxVolume = m_audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		m_curVolume = m_audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		m_Volume.setMax(m_maxVolume);
		m_Volume.setProgress(m_curVolume);
	}

	private void UpdateMediaPlayer(boolean breloadImg, boolean bpmClick) {

		/* IMPOSTO L'IMMAGINE DELL'ESERCIZIO DA VISUALIZZARE */

		if (!bpmClick) {
			if (breloadImg) {
				ImageView img = (ImageView) findViewById(R.id.imgex);
				img.setMinimumHeight(600);
				img.setMinimumHeight(200);
			}
			// SWITCH SULL'ESERCIZIO DA VISUALIZZARE
			if (m_immagine.equalsIgnoreCase("e_cromatici_4_dita_1.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this,
						R.raw.e_cromatici_4_dita_1_a);
				if (breloadImg) {
					img.setImageResource(R.raw.e_cromatici_4_dita_1);
				}
			}
			if (m_immagine.equalsIgnoreCase("e_cromatici_4_dita_2.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this,
						R.raw.e_cromatici_4_dita_2_a);
				if (breloadImg) {
					img.setImageResource(R.raw.e_cromatici_4_dita_2);
				}
			}

			if (m_immagine.equalsIgnoreCase("e_cromatici_4_dita_3.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this,
						R.raw.e_cromatici_4_dita_3_a);

				if (breloadImg) {
					img.setImageResource(R.raw.e_cromatici_4_dita_3);
				}
			}

			if (m_immagine.equalsIgnoreCase("e_cromatici_4_dita_4.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this,
						R.raw.e_cromatici_4_dita_4_a);
				if (breloadImg) {
					img.setImageResource(R.raw.e_cromatici_4_dita_4);
				}
			}

			if (m_immagine.equalsIgnoreCase("e_cromatici_4_dita_5.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this,
						R.raw.e_cromatici_4_dita_5_a);
				if (breloadImg) {
					img.setImageResource(R.raw.e_cromatici_4_dita_5);
				}
			}

			if (m_immagine.equalsIgnoreCase("e_penta_1.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this, R.raw.e_penta_1_a);

				if (breloadImg) {
					img.setImageResource(R.raw.e_penta_1);
				}
			}

			if (m_immagine.equalsIgnoreCase("e_minore_1.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this, R.raw.e_minore_1_a);
				if (breloadImg) {
					img.setImageResource(R.raw.e_minore_1);
				}
			}

			if (m_immagine.equalsIgnoreCase("e_maggiore_1.jpg") == true) {

				m_mediaPlayer = MediaPlayer.create(this, R.raw.e_maggiore_1_a);
				if (breloadImg) {
					img.setImageResource(R.raw.e_maggiore_1);
				}
			}
		} else {
			// Click BPM
			if (bpmClick) {
				m_mediaPlayerMetro = MediaPlayer.create(this, Uri
						.parse(CLICK_FOLDER + m_BpmSource[m_indexBpm]));
				m_mediaPlayerMetro.setLooping(true);
				m_mediaPlayerMetro.start();
			} else {
				if (m_mediaPlayerMetro.isPlaying()) {
					m_mediaPlayerMetro.pause();
				}
			}
		}
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

	ToggleButton.OnClickListener buttonToggleOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			UpdateControls(m_Toggle.isChecked());
			UpdateMediaPlayer(false, m_Toggle.isChecked());
		}
	};

	Button.OnClickListener buttonPlusBpmOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int startIndex = 0;
			int endIndex = 0;
			// incremento indice
			m_indexBpm++;
			endIndex = m_BpmSource[m_indexBpm].indexOf(".");
			m_TextBpm.setText(m_BpmSource[m_indexBpm].substring(startIndex,
					endIndex));
		}
	};

	Button.OnClickListener buttonMinBpmOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int startIndex = 0;
			int endIndex = 0;
			// decremento indice
			m_indexBpm--;
			endIndex = m_BpmSource[m_indexBpm].indexOf(".");
			m_TextBpm.setText(m_BpmSource[m_indexBpm].substring(startIndex,
					endIndex));
		}
	};

	SeekBar.OnSeekBarChangeListener seekVolumeOnChangeListener = new SeekBar.OnSeekBarChangeListener() {
		public void onStopTrackingTouch(SeekBar seekBar) {

			m_curVolume = seekBar.getProgress();
			m_Volume.setProgress(m_curVolume);

		}

		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

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

	private void UpdateControls(boolean checked) {
		// TODO Auto-generated method stub
		// Disattiva\Attiva i controlli della sezione alta\bassa dello schermo
		m_buttonPlay.setEnabled(!checked);
		m_buttonPause.setEnabled(!checked);
		m_buttonBack.setEnabled(!checked);
		m_buttonRew.setEnabled(!checked);
		m_chkLoop.setEnabled(!checked);

		// Sezione Metronomo
		m_plusBpm.setEnabled(checked);
		m_minBpm.setEnabled(checked);

		if (checked) {
			if (m_mediaPlayer.isPlaying()) {
				m_mediaPlayer.pause();
			}
		}
	}

}