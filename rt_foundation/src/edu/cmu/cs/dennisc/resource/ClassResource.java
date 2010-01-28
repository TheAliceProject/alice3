package edu.cmu.cs.dennisc.resource;

public class ClassResource implements Resource {
	private Class<?> cls;
	private String resourceName;
	public ClassResource( Class<?> cls, String resourceName ) {
		this.cls = cls;
		this.resourceName = resourceName;
	}
	public java.net.URL getURL() {
		return this.cls.getResource( this.resourceName );
	}
	public java.io.InputStream getInputStream() {
		return this.cls.getResourceAsStream( this.resourceName );
	}
}
