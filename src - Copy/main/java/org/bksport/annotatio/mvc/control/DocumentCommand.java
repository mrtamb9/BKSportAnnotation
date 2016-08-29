package org.bksport.annotation.mvc.control;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.SwingWorker;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.connector.KIMConnector;
import org.bksport.annotation.core.DocumentProcessor;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.bksport.annotation.mvc.model.DocumentProxy;
import org.bksport.annotation.mvc.model.data.Document;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.ontotext.kim.client.corpora.KIMDocument;

import vn.com.datasection.ioutil.JSV;
import vn.com.datasection.ioutil.JSVFileReader;

/**
 * 
 * handling every tasks related to documents
 * 
 * @author congnh
 * 
 */
public class DocumentCommand extends SimpleCommand 
{
	int docCount = 0;

	@Override
	public void execute(INotification notification) 
	{
		DocumentProxy dProxy = (DocumentProxy) facade.retrieveProxy(AppFacade.DOC_PROXY);
		if (notification.getName().equals(AppFacade.ADD_DOCS_CMD)) // neu click vao nut add cac file da chon
		{
			int docCount = 0;
			@SuppressWarnings("unchecked")
			ArrayList<File> files = (ArrayList<File>) notification.getBody();
			for (File file : files) 
			{
				Document doc = new Document();
				if (file.isFile() && (file.getName().endsWith(".html") 
						|| file.getName().endsWith(".htm") || file.getName().endsWith(".txt"))) 
				{
					doc.setURL(file.toURI().toString());
					doc.setTitle(file.getName());
					try 
					{
						BufferedReader br = new BufferedReader(new FileReader(file));
						String line = null;
						StringBuilder sb = new StringBuilder();
						while ((line = br.readLine()) != null) 
						{
							if (sb.length() > 0) 
							{
								sb.append('\n');
							}
							sb.append(line);
						}
						br.close();
						doc.setContent(sb.toString());	// phan noi dung cua file
						Calendar pubdate = Calendar.getInstance();
						pubdate.setTimeInMillis(file.lastModified());
						doc.setPubdate(pubdate);
					} catch (FileNotFoundException e) 
					{
						Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
					} catch (IOException e) 
					{
						Logger.getLogger(getClass()).error(ExceptionUtils.getStackTrace(e));
					}
				}

				if (file.isFile() && file.getName().endsWith(".tsv")) 
				{
					doc.setURL(file.toURI().toString());
					doc.setTitle(file.getName());

					JSVFileReader reader = new JSVFileReader(new File(file.toPath().toString()), true);
					reader.open();
					JSV[] rs = null;
					
					StringBuilder sb = new StringBuilder();
					int counter = 0; // thiet lap mot cai counter han che so luong thong tin doc vao  
					do{
						rs = reader.read(1);
						for(JSV jsv : rs)
						{
							// System.out.println("TSV");
							if (sb.length() > 0) 
							{
								sb.append('\n');
							}
							sb.append(jsv.getTitle());
							sb.append('\n');
							sb.append(jsv.getContent());
							counter++;
						}
					}while(rs != null && rs.length > 0 && counter<1);
					reader.close();

					doc.setContent(sb.toString());	// phan noi dung cua file
					Calendar pubdate = Calendar.getInstance();
					pubdate.setTimeInMillis(file.lastModified());
					doc.setPubdate(pubdate);

				}

				dProxy.addDocument(doc);
				sendNotification(AppFacade.DOC_ADDED, doc.getTitle());
				docCount++;
			}
			sendNotification(AppFacade.DOCS_ADDED, docCount);
			sendNotification(AppFacade.UPDATE_STATUS_CMD, "Add " + notification.getBody() + " documents successfully.");
		}

		if (notification.getName().equals(AppFacade.REMOVE_DOCS_CMD)) // neu click vao nut remove file
		{
			dProxy.removeDocument((String) notification.getBody());
			sendNotification(AppFacade.DOC_REMOVED, (String) notification.getBody());
			sendNotification(AppFacade.UPDATE_STATUS_CMD, "Remove " + notification.getBody() + " document successfully.");
		}

		if (notification.getName().equals(AppFacade.LOAD_DOCS_INFO_CMD)) 
		{
			sendNotification(AppFacade.DOCS_INFO_LOADED, dProxy.documents().values());
		}

		if (notification.getName().equals(AppFacade.LOAD_DOCS_CMD))
		{
			// cau duoi nghia la neu unchecked thi khong lam gi? (ke ca cau lenh in ra ben tren a?)
			@SuppressWarnings("unchecked")
			ArrayList<String> ids = (ArrayList<String>) notification.getBody();
			ArrayList<KIMDocument> docs = new ArrayList<KIMDocument>();
			for (String id : ids) 
			{
				Document doc = dProxy.getDocument(id);
				if (doc != null && doc.getKIMDoc() != null) 
				{
					docs.add(doc.getKIMDoc());
				}
			}
			sendNotification(AppFacade.DOCS_LOADED, docs);
			sendNotification(AppFacade.UPDATE_STATUS_CMD, "Load " + docs.size() + " documents.");
		}

		if (notification.getName().equals(AppFacade.ANNOTATE_DOCS_CMD)) // kiem tra tin hieu nhan nut annotate
		{
			KIMConnector connector = ((ConnectorProxy) facade.retrieveProxy(AppFacade.CONNECTOR_PROXY)).getKIMConnector();
			if (connector != null && connector.isConnected()) 
			{
				@SuppressWarnings("unchecked")
				ArrayList<String> ids = (ArrayList<String>) notification.getBody();
				ArrayList<Document> docs = new ArrayList<Document>();
				for (String id : ids) 
				{
					Document doc = dProxy.getDocument(id);
					if (doc != null) 
					{
						docs.add(doc);
					}
				}
				DocumentAnnotator docAnnotator = new DocumentAnnotator(connector);
				docAnnotator.addPropertyChangeListener(new PropertyChangeListener() 
				{

					@Override
					public void propertyChange(PropertyChangeEvent evt) 
					{
						if (evt.getPropertyName().equals("annotateFinish")) 
						{
							// ArrayList<?> docs = (ArrayList<?>) evt.getNewValue();
							// update document in list
							// for (int i = 0; i < docs.size(); i++) {
							// KIMDocument doc = (KIMDocument) docs.get(i);
							// docProxy.setDocument(doc.getDocumentId(), doc);
							// }
							sendNotification(AppFacade.UPDATE_STATUS_CMD, "Annotate " + docCount + " documents successfully.");
							sendNotification(AppFacade.UPDATE_STATUS_PROGRESS, 0);
						}
					}
				});
				docAnnotator.setDocuments(docs);
				sendNotification(AppFacade.UPDATE_STATUS_PROGRESS, -1);
				docAnnotator.execute();
			} else {
				sendNotification(AppFacade.ALERT_CMD, "KIMServer isn't connected");
			}
		}
	}

	private class DocumentAnnotator extends SwingWorker<ArrayList<Document>, ArrayList<Document>> 
	{
		private ArrayList<Document> docs = null;
		private KIMConnector connector;

		public DocumentAnnotator(KIMConnector connector) 
		{
			this.connector = connector;
		}

		public void setDocuments(ArrayList<Document> docList) 
		{
			this.docs = docList;
			docCount = 0;
		}

		@Override
		protected ArrayList<Document> doInBackground() throws Exception 
		{
			DocumentProcessor.init();
			for (int i = 0; i < docs.size(); i++) 
			{
				AppFacade.getInstance().sendNotification(AppFacade.UPDATE_STATUS_CMD, "Annotate " + docs.get(i).getTitle());
				docs.get(i).setKIMDoc(connector.annotate(connector.create(docs.get(i).getContent())));
				docs.get(i).getKIMDoc().setSourceUrl(new URL(docs.get(i).getURL()));
				DocumentProcessor.annotate(docs.get(i).getKIMDoc());
				docCount++;
				AppFacade.getInstance().sendNotification(AppFacade.UPDATE_STATUS_PROGRESS, docCount * 100 / docs.size());
				// System.out.println("id: "+docs.get(i).getDocumentId());
				AppFacade.getInstance().sendNotification(AppFacade.DOC_ANNOTATED, docs.get(i).getKIMDoc().getDocumentId());
			}
			return docs;
		}

		@Override
		protected void done() 
		{
			firePropertyChange("annotateFinish", null, docs);
		}
	}
}
