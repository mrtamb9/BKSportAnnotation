package org.bksport.annotation.connector;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class TestAllegroGraph {

	public static void main(String[] args) {
		AGConnector agconnector = new AGConnector();
		agconnector.connect();
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" 
				+ "PREFIX bksport: <http://bk.sport.owl#>\n"
				+ "SELECT DISTINCT ?e \n" 
				+ "WHERE "
				+ "\n{" 
				+ "\n\t?e rdf:type owl:NamedIndividual."
				+ "\n}";
		
		ResultSet results = agconnector.execSelect(queryString);
		while (results.hasNext()) {
			QuerySolution row = results.next();
			com.hp.hpl.jena.rdf.model.RDFNode e = row.get("e");
			if (e!=null) {
				System.out.println(e.toString());
			}
		}
	}
}
