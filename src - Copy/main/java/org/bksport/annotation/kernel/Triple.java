package org.bksport.annotation.kernel;

/**
 * 
 * represent triple
 * 
 */
public class Triple {

	private Resource subject;
	private Resource predicate;
	private Object object;
	private Resource context;

	public Triple() {

	}

	public Triple(Resource subject, Resource predicate, Object object) {
		this(subject, predicate, object, null);
	}

	public Triple(Resource subject, Resource predicate, Object object,
			Resource context) {
		setSubject(subject);
		setPredicate(predicate);
		setObject(object);
		setContext(context);
	}

	public Resource getSubject() {
		return subject;
	}

	public void setSubject(Resource subject) {
		this.subject = subject;
	}

	public Resource getPredicate() {
		return predicate;
	}

	public void setPredicate(Resource predicate) {
		this.predicate = predicate;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Resource getContext() {
		return context;
	}

	public void setContext(Resource context) {
		this.context = context;
	}

	public String toNTripleString() {
		if (getSubject() != null && getPredicate() != null
				&& getObject() != null) {
			return getSubject() + " " + getPredicate() + " " + getObject()
					+ ".";
		} else {
			return null;
		}
	}

	public String toNQuadString() {
		if (getSubject() != null && getPredicate() != null
				&& getObject() != null) {
			return getContext() + " " + getSubject() + " " + getPredicate()
					+ " " + getObject() + ".";
		} else {
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == Triple.class) {
			Triple tobj = (Triple) obj;
			if (getSubject().equals(tobj.getSubject())
					&& getPredicate().equals(tobj.getPredicate())
					&& getObject().equals(tobj.getObject())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
