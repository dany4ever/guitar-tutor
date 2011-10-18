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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImageViewer extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.imageviewer);

		Bundle b = getIntent().getExtras();

		String immmagine = null;
		immmagine = b.getString("image");

		/* IMPOSTO L'IMMAGINE DELL'ESERCIZIO DA VISUALIZZARE */
		ImageView img = (ImageView) findViewById(R.id.imgex);
		img.setMinimumHeight(600);
		img.setMinimumHeight(200);
		// SWITCH SULL'ESERCIZIO DA VISUALIZZARE

		if (immmagine.equalsIgnoreCase("Tetradi") == true) {

			img.setImageResource(R.raw.tetradi);
		}

		Button buttonBack = (Button) findViewById(R.id.backButton);
		buttonBack.setOnClickListener(buttonBackOnClickListener);

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