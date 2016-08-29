package org.bksport.annotation.kernel;

/**
 * represent resource
 * 
 */
public class Resource {

	private String uri;

	public Resource() {

	}

	public Resource(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "<" + getUri() + ">";
	}

}
