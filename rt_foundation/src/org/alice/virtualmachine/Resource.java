package org.alice.virtualmachine;

public abstract class Resource extends edu.cmu.cs.dennisc.pattern.DefaultNameable {
	private java.util.UUID uuid;
	private String contentType;
	private byte[] data;

	protected Resource( java.util.UUID uuid ) {
		this.uuid = uuid;
	}
	protected Resource( Class<?> cls, String resourceName, String contentType ) {
		this.uuid = null;
		try {
			byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( cls, resourceName );
			this.setData( data );
			this.setName( resourceName );
			this.setContentType( contentType );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( resourceName, ioe );
		}
	}
	protected Resource( java.io.File file, String contentType ) {
		this.uuid = java.util.UUID.randomUUID();
		String resourceName = file.getName();
		try {
			byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( file );
			this.setData( data );
			this.setName( resourceName );
			this.setContentType( contentType );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( resourceName, ioe );
		}
	}
	
	public java.util.UUID getUUID() {
		return this.uuid;
	}
	public String getContentType() {
		return this.contentType;
	}
	public void setContentType( String contentType ) {
		this.contentType = contentType;
	}
	public byte[] getData() {
		return this.data;
	}
	public void setData( byte[] data ) {
		this.data = data;
	}
	
	private static String XML_NAME_ATTRIBUTE = "name";
	private static String XML_CONTENT_TYPE_ATTRIBUTE = "contentType";
	public void decodeAttributes( org.w3c.dom.Element xmlElement ) {
		this.setName( xmlElement.getAttribute( XML_NAME_ATTRIBUTE ) );
		this.setContentType( xmlElement.getAttribute( XML_CONTENT_TYPE_ATTRIBUTE ) );
	}
	public void encodeAttributes( org.w3c.dom.Element xmlElement ) {
		xmlElement.setAttribute( XML_NAME_ATTRIBUTE, this.getName() );
		xmlElement.setAttribute( XML_CONTENT_TYPE_ATTRIBUTE, this.getContentType() );
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		//todo
//		sb.append( Resource.class.getName() );
//		sb.append( "[name=" );
		sb.append( this.getName() );
//		sb.append( ";contentType=" );
//		sb.append( this.getContentType() );
//		sb.append( ";uuid=" );
//		sb.append( this.getUUID() );
//		sb.append( "]" );
		return sb.toString();
	}
}
