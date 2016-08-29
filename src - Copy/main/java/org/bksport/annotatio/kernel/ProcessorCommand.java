package org.bksport.annotation.kernel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bksport.annotation.connector.KIMConnector;
import org.bksport.annotation.mvc.AppFacade;
import org.bksport.annotation.mvc.model.ConnectorProxy;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.ontotext.kim.client.corpora.KIMAnnotation;
import com.ontotext.kim.client.corpora.KIMAnnotationSet;
import com.ontotext.kim.client.corpora.KIMCorporaException;
import com.ontotext.kim.client.corpora.KIMDocument;
import com.ontotext.kim.client.corpora.KIMFeatureMap;
import com.ontotext.kim.client.model.FeatureConstants;

public class ProcessorCommand extends SimpleCommand {

	@Override
	public void execute(INotification no) {
		// TODO extract doc from no
		Document doc = null;
		// TODO invoke KIM with parameter doc to get kimdoc
		KIMDocument kimdoc = null;
		Set<Triple> result = new HashSet<Triple>();
		KIMAnnotationSet docAnnSet = null;
		Resource docContext = new Resource(doc.getUri());
		try {
			docAnnSet = kimdoc.getAnnotations();
			KIMAnnotationSet semanticAnnSet = docAnnSet.get("Semantic");
			Iterator<KIMAnnotation> annIter = semanticAnnSet.iterator();

			// ===============================================================
			// step 1
			// extract triples from KIM
			// ===============================================================
			while (annIter.hasNext()) {
				KIMAnnotation ann = (KIMAnnotation) annIter.next();
				KIMFeatureMap featureMap = ann.getFeatures();
				String info = featureMap.get("info").toString();
				Object subject = featureMap.get("subject");
				Object predicate = featureMap.get("predicate");
				Object object = featureMap.get("object");
				if (info != null && !info.contains("debug")) {
					if (subject != null && predicate != null && object != null) {
						result.add(new Triple(new Resource(subject.toString()),
								new Resource(predicate.toString()), object,
								docContext));
					}
				}
			}

			// ===============================================================
			// step 2
			// rank named entities in doc and choose best named entities be
			// topic (bksport:about) of doc, other entities will be only existed
			// in doc (bksport:contains)
			// ===============================================================

			double titleWeight = 2.0;
			double occurenceWeight = 0.5;
			double sayWeight = 1.0;

			double totalWeight = 0;
			double meanWeight = 0;
			int titleEndOffset = 0;
			// store weight of inst
			HashMap<String, Double> instWeight = new HashMap<String, Double>();
			// avoid two annotation have the same offset and inst by store
			// (inst uri - start offset) to checking whether inst exist at that
			// position
			HashSet<String> instOffset = new HashSet<String>();
			annIter = docAnnSet.iterator();// reset iterator
			while (annIter.hasNext()) {
				KIMAnnotation ann = (KIMAnnotation) annIter.next();
				Object inst = ann.getFeatures().get(FeatureConstants.INSTANCE);
				if (inst != null) {
					String instUri = inst.toString();
					if (!instUri.contains("http://bk.sport.owl#"))
						continue;
					if (!instOffset.contains(instUri)) {
						instOffset.add(instUri + "==" + ann.getStartOffset());
						Double previousWeight = instWeight.get(instUri);
						if (previousWeight != null)
							instWeight.put(instUri, previousWeight
									+ occurenceWeight);
						else
							instWeight.put(instUri, occurenceWeight);
					}
				}
			}
			// compute weight of inst base on occurence in title, rule
			// output semantic from rule
			annIter = docAnnSet.iterator();// reset iterator
			while (annIter.hasNext()) {
				KIMAnnotation ann = (KIMAnnotation) annIter.next();
				if (ann.getType().contains("Sentence")) {
					continue;
				}
				if (ann.getType().equals("Semantic")) {
					continue;
				} else if (ann.getEndOffset() <= titleEndOffset) {// annotation
																	// in title
					Object inst = ann.getFeatures().get(
							FeatureConstants.INSTANCE);
					if (inst != null) {
						String instUri = inst.toString();
						if (!instUri.contains("http://bk.sport.owl#"))
							continue;
						if (!instOffset.contains(instUri)) {
							instWeight.put(instUri, titleWeight);
						} else {
							instWeight.put(instUri, instWeight.get(instUri)
									+ titleWeight);
						}
					}
				}
			}
			// compute sum of weight
			for (String inst : instWeight.keySet()) {
				totalWeight += instWeight.get(inst);
			}
			if (!instWeight.isEmpty()) {
				meanWeight = totalWeight / instWeight.size();
			}
			// output &bk;about and &bk;contain
			for (String inst : instWeight.keySet()) {
				if (meanWeight < instWeight.get(inst)) {
					result.add(new Triple(docContext, new Resource(NameSpace
							.bksport("about")), new Resource(inst)));
				} else {
					result.add(new Triple(docContext, new Resource(NameSpace
							.bksport("contain")), new Resource(inst)));
				}
			}

			// ===============================================================
			// TODO: step 3
			// extract triple from our rule, instead using KIM
			// ===============================================================

			// ===============================================================
			// step 4
			// extract triple of reification
			// ===============================================================

			annIter = semanticAnnSet.iterator();// reset iterator
			while (annIter.hasNext()) {
				KIMAnnotation ann = (KIMAnnotation) annIter.next();
				KIMFeatureMap featureMap = ann.getFeatures();
				String info = featureMap.get("info").toString();
				Object subject = featureMap.get("subject");
				Object predicate = featureMap.get("predicate");
				Object object = featureMap.get("object");
				if (subject != null && predicate != null && object != null) {
					if (info != null && info.contains("statement")) {
						KIMConnector connector = ((ConnectorProxy) AppFacade
								.getInstance().retrieveProxy(
										AppFacade.CONNECTOR_PROXY))
								.getKIMConnector();
						String statement = object
								.toString()
								.substring(
										1,
										object.toString()
												.lastIndexOf(
														"\"^^<http://www.w3.org/2001/XMLSchema#string>"));
						KIMAnnotationSet reifiAnnSet = connector
								.annotate(statement);
						Iterator<KIMAnnotation> reifiIter = reifiAnnSet
								.iterator();
						while (reifiIter.hasNext()) {
							KIMAnnotation reifiAnn = reifiIter.next();
							KIMFeatureMap reifiFeatureMap = reifiAnn
									.getFeatures();
							if (reifiFeatureMap.containsKey("info")) {
								String reifiInfo = reifiFeatureMap.get("info")
										.toString();
								String reifiSubject = reifiFeatureMap.get(
										"subject").toString();
								String reifiPredicate = reifiFeatureMap.get(
										"predicate").toString();
								String reifiObject = reifiFeatureMap.get(
										"object").toString();
								if (!reifiInfo.equals("debug")
										&& reifiInfo.equals("statement")) {
									Resource statementRes = new Resource(UUID
											.randomUUID().toString());
									Triple statementDefTriple = new Triple(
											statementRes, new Resource(
													NameSpace.rdf("type")),
											new Resource(NameSpace
													.bksport("Statement")),
											docContext);
									Triple statementSubTriple = new Triple(
											statementRes, new Resource(
													NameSpace.rdf("subject")),
											reifiSubject, docContext);
									Triple statementPreTriple = new Triple(
											statementRes,
											new Resource(NameSpace
													.rdf("predicate")),
											reifiPredicate, docContext);
									Triple statementObjTriple = new Triple(
											statementRes, new Resource(
													NameSpace.rdf("object")),
											reifiObject, docContext);
									result.add(statementDefTriple);
									result.add(statementSubTriple);
									result.add(statementPreTriple);
									result.add(statementObjTriple);
								}
							}
						}
					}
				}
			}
		} catch (KIMCorporaException ex) {
			Logger.getLogger(getClass())
					.error(ExceptionUtils.getStackTrace(ex));
		}
		// TODO store result of doc to somewhere
	}
}
