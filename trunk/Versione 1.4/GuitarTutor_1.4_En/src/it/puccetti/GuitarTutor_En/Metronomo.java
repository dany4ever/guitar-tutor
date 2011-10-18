package it.puccetti.GuitarTutor_En;

import it.puccetti.GuitarTutor_En.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class Metronomo extends Activity {

	private static final String URL = "http://www.guitar-tube.com/mobi/metronome.html";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.simpleweb);

		WebView webview = (WebView) findViewById(R.id.webview);
        
		WebSettings wset = webview.getSettings();

		wset.setJavaScriptEnabled(true);
		wset.setBlockNetworkLoads(false);
		//webview.setWebChromeClient(new GTutorWebChromeClient(this));
		
		webview.loadUrl(URL);
	}

	public String CaricaContenuto(String htmlfile) {

		InputStream is = null;
		String oszRet = null;

		htmlfile.trim();
		/* Rimappo per accedere ai files */

		try {
			is = openFileInput(htmlfile);
			// Here BufferedInputStream is added for fast reading.
			if (is != null) {
				// prepare the file for reading
				InputStreamReader inputreader = new InputStreamReader(is);
				BufferedReader buffreader = new BufferedReader(inputreader);

				String line;

				// read every line of the file into the line-variable, on line
				// at the time
				while ((line = buffreader.readLine()) != null) {
					// do something with the settings from the file
					oszRet = oszRet + line;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return oszRet;

	}
}
