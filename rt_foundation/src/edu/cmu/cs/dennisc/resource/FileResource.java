package edu.cmu.cs.dennisc.resource;

public class FileResource implements Resource {
	private java.io.File file;
	public FileResource( java.io.File file ) {
		this.file = file;
	}
	public java.net.URL getURL() {
		try {
			return this.file.toURL();
		} catch( java.net.MalformedURLException murle ) {
			throw new RuntimeException( file.getAbsolutePath(), murle );
		}
	}
	public java.io.InputStream getInputStream() {
		try {
			return new java.io.FileInputStream( this.file );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
}
