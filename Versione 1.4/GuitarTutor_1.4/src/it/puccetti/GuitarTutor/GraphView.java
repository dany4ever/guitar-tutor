package it.puccetti.GuitarTutor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.sax.Element;
import android.text.InputFilter.LengthFilter;
import android.view.View;

/**
 * GraphView creates a scaled line or bar graph with x and y axis labels. 
 * @author Arno den Hond
 *
 */
public class GraphView extends View {

	public static boolean BAR = true;
	public static boolean LINE = false;
	private Paint paint;
	private String[] m_horlabels = null;
	private String[] m_verlabels = null;
	private String m_title = null;
	private String m_numeroex = null;
	private boolean m_type = LINE;
    private ArrayList<String> m_BpmArray = new ArrayList();
    private ArrayList<String> m_DataArray = new ArrayList();
	// COSTATNTI PER ACCEDERE AL FILE XML DELL'EXERCISE DIARY
	private static final String XML_PLAN_FILE = "data/data/it.puccetti.GuitarTutor/files/exercise_diary.xml";
	private static final String ROOT_TAG = "guitar_diary";
	private static final String EXERCISE_TAG = "guitar_exercise";
	private static final String EXERCISE_NUM_TAG = "number";
	private static final String EXERCISE_TITLE_TAG = "title";
	private static final String EXERCISE_DATE_TAG = "date";
	private static final String EXERCISE_BPM_TAG = "bpm";    
	public GraphView(Context context, String title, String numero, boolean type) {
		super(context);
		this.m_numeroex = numero;
		this.m_title = title;
		this.m_type = type;
		
		//CARICO I DATI DAL FILE XML SE VALORIZZATI
		if(LoadDataFromFile()){
		  
		  //Start onDraw
		  paint = new Paint();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float border = 20;
		float horstart = border * 2;
		float height = getHeight() -1;
		float width = getWidth() - 1;
		float max = getMax();
		float min = getMin();
		float diff = max - min;
		float graphheight = height - (2 * border);
		float graphwidth = width - (2 * border);

		paint.setTextAlign(Align.LEFT);
		int vers = m_verlabels.length - 1;
		for (int i = 0; i < m_verlabels.length; i++) {
			paint.setColor(Color.DKGRAY);
			float y = ((graphheight / vers) * i) + border;
			canvas.drawLine(horstart, y, width, y, paint);
			paint.setColor(Color.WHITE);
			canvas.drawText(m_verlabels[i], 0, y, paint);
		}
		int hors = m_horlabels.length - 1;
		for (int i = 0; i < m_horlabels.length; i++) {
			paint.setColor(Color.DKGRAY);
			float x = ((graphwidth / hors) * i) + horstart;
			canvas.drawLine(x, height - border, x, border, paint);
			paint.setTextAlign(Align.CENTER);
			if (i==m_horlabels.length-1)
				paint.setTextAlign(Align.RIGHT);
			if (i==0)
				paint.setTextAlign(Align.LEFT);
			paint.setColor(Color.WHITE);
			canvas.drawText(m_horlabels[i], x, height - 4, paint);
		}

		paint.setTextAlign(Align.CENTER);
		canvas.drawText(m_title, (graphwidth / 2) + horstart, border - 4, paint);

		if (max != min) {
			paint.setColor(Color.LTGRAY);
			if(m_type = LINE) {
				//Grafico Lineare
				float datalength = m_horlabels.length;
				float colwidth = (width - (2 * border)) / datalength;
				float halfcol = colwidth / 2;
				float val = 0.0f;
				float rat = 0.0f;
				float h = 0.0f;
				float lasth = 0;
				float startX = 0.0f;
				float startY = 0.0f;
				float endX = 0.0f;
				float endY = 0.0f;
				for (int i = 0; i < m_verlabels.length; i++) {
					val = DateToNumber(m_verlabels[i]) - min;
					rat = val / diff;
					h = graphheight * rat;
					startX = ((i - 1) * colwidth) + (horstart + 1 + DateToNumber(m_horlabels[i]) + halfcol);
					startY = (border - lasth) + graphheight + Float.parseFloat(m_verlabels[i]);
					endX = ((i - 1) * colwidth) + (horstart + 1 + DateToNumber(m_horlabels[i+1]) + halfcol);
					endY = (border - lasth) + graphheight + Float.parseFloat(m_verlabels[i+1]);
					if (i > 0 && (i+1<=m_horlabels.length)){
						canvas.drawLine(startX,startY,endX,endY,paint);
						lasth = h;
					}
				}
		   }
		}
	}

	private float getMax() {
		float largest = Integer.MIN_VALUE;
		for (int i = 0; i < m_horlabels.length; i++)
			if (DateToNumber(m_horlabels[i]) > largest)
				largest = DateToNumber(m_horlabels[i]);
		return largest;
	}

	private float getMin() {
		float smallest = Integer.MAX_VALUE;
		for (int i = 0; i < m_horlabels.length; i++)
			if (DateToNumber(m_horlabels[i]) < smallest)
				smallest = DateToNumber(m_horlabels[i]);
		return smallest;
	}
	
	
	//CARICO I DATI DELL'ESERCIZIO
	private boolean LoadDataFromFile() {
		// TODO Auto-generated method stub
		boolean bRet = false;
		m_BpmArray.clear();
		m_DataArray.clear();
		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		NodeList nodeList = null;
		Node entry = null;
		NamedNodeMap attrList = null;
		Node curElAttr = null;
		String sAttrName = null;
		File file = null;
		String sBpm = null;
		String sData = null;
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

			entry = null;
			attrList = null;
			curElAttr = null;
			sAttrName = null;
			// create the root element
			if (doc != null) {

				nodeList = doc.getElementsByTagName(EXERCISE_TAG);

				if (nodeList.getLength() > 0) {
					for (int i = 0; i < nodeList.getLength(); i++) {

						sData = "";
						sNumero = "";
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
								
							}

						}

						if (sNumero.equalsIgnoreCase(m_numeroex)) {
							// Valorizzo l'elemento e lo aggiungo alla lista
							// Array BPM (Asse Y) e Data (Asse X)
							m_BpmArray.add(sBpm);
							m_DataArray.add(sData);
						}

					}
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		m_horlabels = (String[]) m_DataArray.toArray(new String[m_DataArray.size()]);
		m_verlabels = (String[]) m_BpmArray.toArray(new String[m_BpmArray.size()]);
		
		bRet = !(m_verlabels.length>0) && (m_horlabels.length>0);
		
		m_BpmArray.clear();
		m_DataArray.clear();
		
		doc = null;
		factory = null;
		docBuilder = null;
		doc = null;
		file = null;
		nodeList = null;
		entry = null;
		attrList = null;
		curElAttr = null;
		sAttrName = null;
		

		return bRet;
	}
	
	//Converte in un valore staampabile
	public float DateToNumber(String oszDate){
	 float fRet = 0.0f;
	 Date d = new Date(oszDate);
	 fRet = (d.getTime()/(10^9));
	 
	 d = null;
	 
	 return fRet;
	}
	

}
