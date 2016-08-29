package org.bksport.annotation;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.Console;
import org.bksport.annotation.mvc.model.data.Document;

import com.ontotext.kim.client.corpora.KIMCorporaException;

/**
 * 
 * @author congnh
 * 
 */
public class BKSportAnnotation {

	public static void main(String args[]) {
		// args = new String[] { "./corpus" };
		if (args.length == 0) {
			AppFacade facade = AppFacade.getInstance();
			facade.startup();
		} else {
			Console console = new Console();
			List<Document> docList = new ArrayList<Document>();
			boolean isRecursive = false;
			for (String arg : args) {
				if (arg.equals("-r")) {
					isRecursive = true;
				}
			}
			for (String path : args) {
				if (!path.startsWith("-")) {
					docList.addAll(console.getDocumentList(path, isRecursive));
				}
			}
			try {
				// TODO: write to file
				System.out.println(console.annotate(docList));
			} catch (KIMCorporaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
