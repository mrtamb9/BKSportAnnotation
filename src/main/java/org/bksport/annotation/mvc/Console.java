package org.bksport.annotation.mvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.connector.KIMConnector;
import org.bksport.annotation.core.DocumentProcessor;
import org.bksport.annotation.mvc.model.data.Document;
import org.bksport.annotation.util.NSUtil;

import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;

public class Console {

	public String annotate(List<Document> docList) throws KIMCorporaException,
			MalformedURLException {
		KIMConnector connector = new KIMConnector("localhost", 1099);
		connector.connect();
		String exportStr = "";
		for (Document doc : docList) {
			doc.setKIMDoc(connector.annotate(connector.create(doc.getContent())));
			doc.getKIMDoc().setSourceUrl(new URL(doc.getURL()));
			DocumentProcessor.annotate(doc.getKIMDoc());

			String extract = DocumentProcessor.extractSemanticFromKIM(doc
					.getKIMDoc())
					+ DocumentProcessor.extractSemanticFromWeight(doc
							.getKIMDoc());
			String docUri = "http://bk.sport.owl#document"
					+ doc.getKIMDoc().getDocumentId();
			String url;
			try {
				url = doc.getKIMDoc().getSourceUrl().toString();
				extract += NSUtil.toTriple(docUri, NSUtil.rdf("type"),
						NSUtil.bksport("Document"))
						+ "\n";
				extract += NSUtil
						.toTriple(
								docUri,
								NSUtil.rdf("hasURL"),
								"\""
										+ url
										+ "\"^^<http://www.w3.org/2001/XMLSchema#string>")
						+ "\n";
			} catch (KIMCorporaException ex) {
				Logger.getLogger(getClass()).info(
						ExceptionUtils.getStackTrace(ex));
			}
			/**
			 * End v2.0.0
			 */
			if (extract != null && !extract.isEmpty())
				exportStr += extract + "\n";
		}
		/**
		 * V2
		 */
		// reification
		for (Document document : docList) {
			KIMDocument doc = document.getKIMDoc();
			String extract = DocumentProcessor.extractReificationFromKIM(doc);
			if (extract != null && !extract.isEmpty())
				exportStr += extract + "\n";
		}
		connector.disconnect();
		return exportStr;
	}

	/**
	 * get list of document recursive(apply only for small directory)
	 * 
	 * @param path
	 * @param isRecursive
	 * @return
	 */
	public List<Document> getDocumentList(String path, boolean isRecursive) {
		System.out.println(path);
		List<Document> docList = new ArrayList<Document>();
		File cfile = new File(path);
		if (cfile.isDirectory())
			for (File file : cfile.listFiles()) {
				if (file.isDirectory() && isRecursive) {
					docList.addAll(getDocumentList(file.getAbsolutePath(), true));
				} else {
					Document doc = getDocument(file);
					if (doc != null) {
						docList.add(doc);
					}
				}
			}
		else {
			Document doc = getDocument(cfile);
			if (doc != null) {
				docList.add(doc);
			}
		}
		return docList;
	}

	public Document getDocument(File file) {
		Document doc = new Document();
		if (file.isFile()
				&& (file.getName().endsWith(".html")
						|| file.getName().endsWith(".htm") || file.getName()
						.endsWith(".txt"))) {
			doc.setURL(file.toURI().toString());
			doc.setTitle(file.getName());
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					if (sb.length() > 0) {
						sb.append('\n');
					}
					sb.append(line);
				}
				br.close();
				doc.setContent(sb.toString());
				Calendar pubdate = Calendar.getInstance();
				pubdate.setTimeInMillis(file.lastModified());
				doc.setPubdate(pubdate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return doc;
		}
		return null;
	}

}
