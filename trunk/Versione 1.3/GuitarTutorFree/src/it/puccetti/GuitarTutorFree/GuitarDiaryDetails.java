package it.puccetti.GuitarTutorFree;

import it.puccetti.GuitarTutorFree.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.sax.Element;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GuitarDiaryDetails extends Activity implements OnClickListener,
		OnItemSelectedListener {

	private Spinner m_ListBPms;
	private EditText m_data = null;
	private Button m_buttonBack = null;
	private Button m_buttonSave = null;

	private String m_oszCurSel = null;
	private String m_sNumber = null;
	private String m_sTitle = null;

	private static final int DATE_DIALOG_ID = 0;
	private static final int DIALOG_ERR = 1;
	private static final int DIALOG_ERR_SAVE = 2;
	private static final int DIALOG_OK = 3;
	private static final int DIALOG_ERR_NO_DATA = 4;
	private static final String ERR_SAVE_MESSAGE = "Si e' verificato un errore!";
	private static final String ERR_MESSAGE = "Pianificazione gia' salvata per questa data!";
	private static final String OK_MESSAGE = "Pianificazione salvata correttamente!";
	private static final String ERR_NO_DATA_MESSAGE = "Inserire una data valida";
	// COSTATNTI PER ACCEDERE AL FILE XML DELL'EXERCISE DIARY
	private static final String XML_PLAN_FILE = "data/data/it.puccetti.GuitarTutor/files/exercise_diary.xml";
	private static final String ROOT_TAG = "guitar_diary";
	private static final String EXERCISE_TAG = "guitar_exercise";
	private static final String EXERCISE_NUM_TAG = "number";
	private static final String EXERCISE_TITLE_TAG = "title";
	private static final String EXERCISE_DATE_TAG = "date";
	private static final String EXERCISE_BPM_TAG = "bpm";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.diarydetails);

		Bundle b = getIntent().getExtras();

		m_sNumber = null;
		m_sTitle = null;
		String sText = null;
		m_sNumber = b.getString("chnumber");
		m_sTitle = b.getString("chtitle");

		sText = m_sTitle;
		TextView poTextTitle = (TextView) findViewById(R.id.ExTitle);
		poTextTitle.setText(sText.toCharArray(), 0, sText.toCharArray().length);

		m_ListBPms = (Spinner) findViewById(R.id.search_bpms);
		m_data = (EditText) findViewById(R.id.data);
		// m_data.setEnabled(false);
		m_data.setOnClickListener(this);
		populateSpinnerWithArray(m_ListBPms);

		m_buttonBack = (Button) findViewById(R.id.backButton);
		m_buttonSave = (Button) findViewById(R.id.saveButton);
		m_buttonSave.setOnClickListener(buttonSaveOnClickListener);
		m_buttonBack.setOnClickListener(buttonBackOnClickListener);
	}

	public void onDestroy() {

		super.onDestroy();

	}

	public void populateSpinnerWithArray(Spinner poSpinner) {

		ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.bpms, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m_ListBPms.setAdapter(adapter);
		m_ListBPms.setOnItemSelectedListener(this);

	}

	// Listeners

	Button.OnClickListener buttonBackOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	Button.OnClickListener buttonSaveOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			boolean bRet = false;

			String sTemp = m_data.getText().toString().trim();

			// TODO Auto-generated method stub
			if ((sTemp.length() < 9)) {
				showDialog(DIALOG_ERR_NO_DATA);
			} else {
				if (CanSavePlan(sTemp) == true) {

					bRet = SavePlan();

					if (bRet == false) {

						showDialog(DIALOG_ERR_SAVE);
					} else {
						showDialog(DIALOG_OK);
					}
				} else {
					showDialog(DIALOG_ERR);
					bRet = true;
				}
			}
		}

	};

	private boolean CanSavePlan(String oszData) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory domFactory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		String expression = null;
		File f = null;
		File fXml = null;
		FileOutputStream fos = null;
		FileInputStream fis = null;
		XPath xpath = XPathFactory.newInstance().newXPath();
		Boolean bRet = false;
		String xmlEmptyHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?><guitar_diary></guitar_diary>";

		// f = Environment.getDataDirectory().getAbsoluteFile();
		fXml = new File(XML_PLAN_FILE);
		System.out.println(fXml.getAbsolutePath());
		// Se il file non esiste lo creo vuoto
		if (fXml.exists() == false) {
			// Creo il file
			try {
				fXml.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// CreateHeader();
			try {
				FileOutputStream fout = new FileOutputStream(
						fXml.getAbsolutePath());
				try {
					fout.write(xmlEmptyHeader.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		fos = null;

		if (oszData != null) {
			domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);

			try {
				builder = domFactory.newDocumentBuilder();

			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

				// File f = new File(getFileStreamPath(name));
				if (builder != null) {
					fis = new FileInputStream(XML_PLAN_FILE);
					doc = builder.parse(fXml);
					expression = "//guitar_exercise[@date='" + oszData
							+ "' and @number='" + m_sNumber.trim()
							+ "']/@number";
					try {
						NodeList widgetNode = (NodeList) xpath.evaluate(
								expression, doc, XPathConstants.NODESET);
						if (((widgetNode == null))
								|| (widgetNode.getLength() == 0)) {

							// Può salvare
							bRet = true;
						}

						widgetNode = null;
						doc = null;
					} catch (XPathExpressionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		builder = null;
		domFactory = null;
		f = null;
		fXml = null;
		return bRet;
	}

	private boolean SavePlan() {
		// TODO Auto-generated method stub
		Boolean bRet;
		bRet = false;

		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		Element root = null;
		Node rootDoc = null;
		org.w3c.dom.Element childElement = null;
		File file = null;

		try {
			file = new File(XML_PLAN_FILE);

			// Create instance of DocumentBuilderFactory
			factory = DocumentBuilderFactory.newInstance();

			// Get the DocumentBuilder
			docBuilder = factory.newDocumentBuilder();
			factory.setNamespaceAware(true);

			// Using existing XML Document
			doc = docBuilder.parse(file);

			// create the root element
			if (doc != null) {
				// rootDoc = (Document) doc.getDocumentElement();
				// visit(doc,0);

				rootDoc = doc.getFirstChild();
				// NodeList nodeList = root..getElementsByTagName(ROOT_TAG);
				// create child element
				// childElement = (Element) (doc.createElement(EXERCISE_TAG));
				childElement = doc.createElement(EXERCISE_TAG);
				// Add the attributes
				((org.w3c.dom.Element) childElement).setAttribute(
						EXERCISE_NUM_TAG, m_sNumber.trim());
				((org.w3c.dom.Element) childElement).setAttribute(
						EXERCISE_TITLE_TAG, m_sTitle.trim());
				((org.w3c.dom.Element) childElement).setAttribute(
						EXERCISE_DATE_TAG, m_data.getText().toString().trim());
				((org.w3c.dom.Element) childElement).setAttribute(
						EXERCISE_BPM_TAG, m_oszCurSel.trim());
				((Node) rootDoc).appendChild((Node) childElement);

				// set up a transformer
				TransformerFactory transfac = TransformerFactory.newInstance();
				Transformer trans = transfac.newTransformer();

				// create string from xml tree
				StringWriter sw = new StringWriter();
				StreamResult result = new StreamResult(sw);
				DOMSource source = new DOMSource(doc);
				trans.transform(source, result);
				String xmlString = sw.toString();

				OutputStream f0;
				byte buf[] = xmlString.getBytes();
				f0 = new FileOutputStream(XML_PLAN_FILE);
				for (int i = 0; i < buf.length; i++) {
					f0.write(buf[i]);
				}
				f0.close();
				f0 = null;
				buf = null;
				bRet = true;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		factory = null;
		docBuilder = null;
		doc = null;
		root = null;
		childElement = null;
		file = null;

		return bRet;
	}

	// CREA L'HEADER DEL FILE
	private boolean CreateHeader() {
		// TODO Auto-generated method stub
		Boolean bRet;
		bRet = false;

		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		Element root = null;
		Element childElement = null;
		File file = null;

		try {
			file = new File(XML_PLAN_FILE);

			// Create instance of DocumentBuilderFactory
			factory = DocumentBuilderFactory.newInstance();

			// Get the DocumentBuilder
			docBuilder = factory.newDocumentBuilder();

			// Using existing XML Document
			doc = docBuilder.parse(file);

			// create the root element
			root = (Element) doc.getDocumentElement();

			// create child element
			childElement = (Element) doc.createElement(ROOT_TAG);
			((Node) root).appendChild((Node) childElement);

			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			OutputStream f0;
			byte buf[] = xmlString.getBytes();
			f0 = new FileOutputStream(XML_PLAN_FILE);
			for (int i = 0; i < buf.length; i++) {
				f0.write(buf[i]);
			}
			f0.close();
			f0 = null;
			buf = null;
			bRet = true;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		factory = null;
		docBuilder = null;
		doc = null;
		root = null;
		childElement = null;
		file = null;

		return bRet;
	}

	// Creating dialog
	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		Dialog dRet = null;
		AlertDialog.Builder builder = null;
		switch (id) {
		case DATE_DIALOG_ID:
			dRet = new DatePickerDialog(this, mDateSetListener, cyear, cmonth,
					cday);
			break;
		case DIALOG_ERR:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(ERR_MESSAGE)
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

		case DIALOG_ERR_SAVE:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(ERR_SAVE_MESSAGE)
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

		case DIALOG_OK:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(OK_MESSAGE)
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
		case DIALOG_ERR_NO_DATA:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(ERR_NO_DATA_MESSAGE)
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

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String date_selected = String.valueOf(dayOfMonth) + "/"
					+ String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(year);
			m_data.setText(date_selected.toCharArray(), 0,
					date_selected.toCharArray().length);
		}

	};

	public void onClick(View v) {
		if (v == m_data)
			showDialog(DATE_DIALOG_ID);
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