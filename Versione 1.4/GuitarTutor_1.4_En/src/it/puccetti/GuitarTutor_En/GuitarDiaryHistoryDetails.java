package it.puccetti.GuitarTutor_En;

import it.puccetti.GuitarTutor_En.R;

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
import org.w3c.dom.NamedNodeMap;
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

public class GuitarDiaryHistoryDetails extends Activity implements
		OnClickListener, OnItemSelectedListener {

	private Spinner m_ListBPms = null;
	private Spinner m_data = null;
	private Button m_buttonBack = null;
	private Button m_buttonDel = null;
	private String m_oszCurSelBpm = null;
	private String m_oszCurSelDate = null;
	private String m_sNumber = null;
	private String m_sTitle = null;
	private ArrayAdapter m_adapterData;
	private ArrayAdapter m_adapterBpm;
	private ArrayList<ElementoDiary> m_ElementoArray = new ArrayList();
	private ArrayList<String> m_BpmArray = new ArrayList();
	private ArrayList<String> m_DataArray = new ArrayList();
	private static final int DATE_DIALOG_ID = 0;
	private static final int DIALOG_ERR = 1;
	private static final int DIALOG_ERR_SAVE = 2;
	private static final int DIALOG_OK = 3;
	private static final String ERR_SAVE_MESSAGE = "An error occurred!";
	private static final String ERR_MESSAGE = "This plan already exists!";
	private static final String OK_MESSAGE = "Plan deleted!";
	// COSTATNTI PER ACCEDERE AL FILE XML DELL'EXERCISE DIARY
	private static final String XML_PLAN_FILE = "data/data/it.puccetti.GuitarTutor_En/files/exercise_diary.xml";
	private static final String ROOT_TAG = "guitar_diary";
	private static final String EXERCISE_TAG = "guitar_exercise";
	private static final String EXERCISE_NUM_TAG = "number";
	private static final String EXERCISE_TITLE_TAG = "title";
	private static final String EXERCISE_DATE_TAG = "date";
	private static final String EXERCISE_BPM_TAG = "bpm";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.diaryhistorydetails);

		Bundle b = getIntent().getExtras();

		m_sNumber = null;
		m_sTitle = null;
		String sText = null;
		m_sNumber = b.getString("chnumber");
		m_sTitle = b.getString("chtitle");

		sText = m_sTitle;
		TextView poTextTitle = (TextView) findViewById(R.id.ExTitle);
		poTextTitle.setText(sText.toCharArray(), 0, sText.length());

		m_ListBPms = (Spinner) findViewById(R.id.search_bpms);
		m_ListBPms.setEnabled(false);
		m_data = (Spinner) findViewById(R.id.data_Spinner);

		m_buttonBack = (Button) findViewById(R.id.backButton);
		m_buttonDel = (Button) findViewById(R.id.deleteButton);
		m_buttonBack.setOnClickListener(buttonBackOnClickListener);
		m_buttonDel.setOnClickListener(buttonDelOnClickListener);

		// m_data.setOnClickListener(this);
		populateSpinnerWithArray();

	}

	public void onDestroy() {

		super.onDestroy();
		m_adapterBpm.clear();
		m_adapterData.clear();
		m_DataArray.clear();

	}

	public void populateSpinnerWithArray() {

		/* Carico la lista dei bpm dal file xml in base all'esercizio */
		if (LoadCombosFromFile() == false) {
			m_data.setEnabled(false);
			m_buttonDel.setEnabled(false);
		}
		m_adapterBpm = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, m_BpmArray);
		m_adapterBpm
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m_ListBPms.setAdapter(m_adapterBpm);
		m_ListBPms.setOnItemSelectedListener(this);

		
		m_adapterData = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, m_DataArray);
		m_adapterData
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m_data.setAdapter(m_adapterData);
		m_data.setOnItemSelectedListener(this);
		m_data.setOnItemSelectedListener(this);

	}

	private boolean LoadCombosFromFile() {
		// TODO Auto-generated method stub
		boolean bRet = false;
		m_ElementoArray.clear();
		m_BpmArray.clear();
		m_DataArray.clear();

		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		Element root = null;
		Node rootDoc = null;
		org.w3c.dom.Element childElement = null;
		File file = null;
		String sBpm = null;
		String sData = null;
		String sTitolo = null;
		String sNumero = null;

		try {
			file = new File(XML_PLAN_FILE);

			// Create instance of DocumentBuilderFactory
			factory = DocumentBuilderFactory.newInstance();

			// Get the DocumentBuilder
			docBuilder = factory.newDocumentBuilder();
			factory.setNamespaceAware(true);

			// Using existing XML Document
			doc = docBuilder.parse(file);

			Node entry = null;
			NamedNodeMap attrList = null;
			Node curElAttr = null;
			String sAttrName = null;
			// create the root element
			if (doc != null) {

				NodeList nodeList = doc.getElementsByTagName(EXERCISE_TAG);

				if (nodeList.getLength() > 0) {
					for (int i = 0; i < nodeList.getLength(); i++) {

						ElementoDiary el = new ElementoDiary();

						sData = "";
						sNumero = "";
						sTitolo = "";
						sBpm = "";

						entry = null;
						entry = nodeList.item(i);

						// Ciclo sugli attributi del nodo
						attrList = null;
						attrList = entry.getAttributes();

						for (int iPos = 0; ((attrList != null) & (iPos < attrList
								.getLength())); iPos++) {
							curElAttr = null;
							curElAttr = attrList.item(iPos);
							if (curElAttr != null) {

								sAttrName = curElAttr.getNodeName();

								if (sAttrName
										.equalsIgnoreCase(EXERCISE_BPM_TAG)) {
									sBpm = curElAttr.getNodeValue();
								}

								// sData
								if (sAttrName
										.equalsIgnoreCase(EXERCISE_DATE_TAG)) {
									sData = curElAttr.getNodeValue();
								}
								// sNumero
								if (sAttrName
										.equalsIgnoreCase(EXERCISE_NUM_TAG)) {
									sNumero = curElAttr.getNodeValue();
								}

								// sTitolo
								if (sAttrName
										.equalsIgnoreCase(EXERCISE_TITLE_TAG)) {
									sTitolo = curElAttr.getNodeValue();
								}
							}

						}

						if (sNumero.equalsIgnoreCase(m_sNumber)) {

							// Valorizzo l'elemento e lo aggiungo alla lista
							el.m_sBpm = sBpm;
							el.m_sData = sData;
							el.m_sNumber = sNumero;
							el.m_sTitolo = sTitolo;

							m_ElementoArray.add(el);
							// Array BPM e Data
							m_BpmArray.add(sBpm);
							m_DataArray.add(sData);
						}

					}
				}

				doc = null;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		factory = null;
		docBuilder = null;
		doc = null;
		root = null;
		childElement = null;
		file = null;

		bRet = !m_ElementoArray.isEmpty();

		return bRet;
	}

	// Listeners

	Button.OnClickListener buttonBackOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	Button.OnClickListener buttonDelOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Elimina il piano
			if (DeletePlan()) {

				showDialog(DIALOG_OK);
				//reload
				if (LoadCombosFromFile() == false) {
					m_data.setEnabled(false);
					m_buttonDel.setEnabled(false);
				}
				m_data.refreshDrawableState();
				m_ListBPms.refreshDrawableState();
				m_adapterBpm.notifyDataSetChanged();
				m_adapterData.notifyDataSetChanged();
				
			}

		}
	};

	private boolean DeletePlan() {

		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		Element root = null;
		Node rootDoc = null;
		org.w3c.dom.Element childElement = null;
		File file = null;

		boolean bRet = false;
		try {
			file = new File(XML_PLAN_FILE);

			// Create instance of DocumentBuilderFactory
			factory = DocumentBuilderFactory.newInstance();

			// Get the DocumentBuilder
			docBuilder = factory.newDocumentBuilder();
			factory.setNamespaceAware(true);

			// Using existing XML Document
			doc = docBuilder.parse(file);

			XPath xpath = XPathFactory.newInstance().newXPath();
			// create the root element
			if (doc != null) {

				String expression = "//guitar_exercise[@date='"
						+ m_oszCurSelDate + "' and @number='"
						+ m_sNumber.trim() + "']";
				try {
					NodeList listNode = (NodeList) xpath.evaluate(expression,
							doc, XPathConstants.NODESET);
					if (((listNode == null)) || (listNode.getLength() == 1)) {
						Node delNode = null;
						delNode = listNode.item(0);
						doc.getDocumentElement().removeChild(delNode);
						bRet = true;
					}

					listNode = null;

				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
				doc = null;
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
				XPath xpath = XPathFactory.newInstance().newXPath();
				String expression = "//guitar_exercise[@date='"
						+ m_oszCurSelDate + "' and @number='"
						+ m_sNumber.trim() + "']";

				try {
					NodeList widgetNode = (NodeList) xpath.evaluate(expression,
							doc, XPathConstants.NODESET);
					if (((widgetNode == null)) || (widgetNode.getLength() == 1)) {

						// CAMBIO L'ATTRIBUTO
						NamedNodeMap attrList = widgetNode.item(0)
								.getAttributes();
						for (int iPos = 0; ((attrList != null) & (iPos < attrList
								.getLength())); iPos++) {

							if (attrList.item(iPos).getNodeName()
									.equalsIgnoreCase(EXERCISE_BPM_TAG)) {
								attrList.item(iPos)
										.setNodeValue(m_oszCurSelBpm);
							}

							// Modifico il file
							TransformerFactory transfac = TransformerFactory
									.newInstance();
							Transformer trans = null;
							try {
								trans = transfac.newTransformer();
							} catch (TransformerConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							StringWriter sw = new StringWriter();
							StreamResult result = new StreamResult(sw);
							DOMSource source = new DOMSource(doc);
							try {
								trans.transform(source, result);
							} catch (TransformerException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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

						}
					}

					widgetNode = null;
					doc = null;
				} catch (XPathExpressionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				bRet = true;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
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

	// Visita l'XML
	public void visit(Node node, int level) {
		NodeList nl = node.getChildNodes();

		for (int i = 0, cnt = nl.getLength(); i < cnt; i++) {
			System.out.println("[" + nl.item(i) + "]");

			visit(nl.item(i), level + 1);
		}
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
		}
		return dRet;
	}

	public void onClick(View v) {
		if (v == m_data)
			showDialog(DATE_DIALOG_ID);
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == m_ListBPms) {
			m_oszCurSelBpm = arg0.getSelectedItem().toString();
		}

		if (arg0 == m_data) {
			m_oszCurSelDate = arg0.getSelectedItem().toString();
			UpdateBpmCombo(arg2);
		}

	}

	private void UpdateBpmCombo(int pos) {
		// TODO Auto-generated method stub
		// Reperisco l'elemento associato alla data corrente
		m_ListBPms.setSelection(pos);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}