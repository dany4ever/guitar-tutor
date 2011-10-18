package it.puccetti.GuitarTutor_En;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class watchVideo extends Activity {
	
	private VideoView m_Videoplayer = null;
	private MediaController m_MediaController = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.watchvideo);

		Button buttonBack = (Button) findViewById(R.id.backButton);
		buttonBack.setOnClickListener(buttonBackOnClickListener);

		
		Bundle b = getIntent().getExtras();

		String Video = null;
		Video = b.getString("videoUrl");
		Uri uri;
		uri = Uri.parse(Video);
		m_Videoplayer = (VideoView) findViewById(R.id.videox);
		m_MediaController = (MediaController) findViewById(R.id.videox_controller);
		m_Videoplayer.setVideoURI(uri);
		m_Videoplayer.setMediaController(m_MediaController);

		m_Videoplayer.start();
	}

	public void onDestroy() {

		super.onDestroy();

	}

	// Listeners

	Button.OnClickListener buttonBackOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}
