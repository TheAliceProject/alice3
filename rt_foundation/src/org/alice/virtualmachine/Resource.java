package org.alice.virtualmachine;

public class Resource extends edu.cmu.cs.dennisc.pattern.DefaultNameable {
	
	private static java.util.Map< java.util.UUID, Resource > uuidToResourceMap = new java.util.HashMap< java.util.UUID, Resource >(); 
	public static Resource get( java.util.UUID uuid ) {
		Resource rv = uuidToResourceMap.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			rv = new Resource( uuid );
			uuidToResourceMap.put( uuid, rv );
		}
		return rv;
	}
	public static Resource valueOf( String s ) {
		java.util.UUID uuid = java.util.UUID.fromString( s );
		return get( uuid );
	}	
	
	private java.util.UUID uuid;
	private String contentType;
	private byte[] data;

	private Resource( java.util.UUID uuid ) {
		this.uuid = uuid;
	}
	public Resource( Class<?> cls, String resourceName, String contentType ) {
		this.uuid = null;
		try {
			byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( cls, resourceName );
			this.setData( data );
			this.setName( resourceName );
			this.setContentType( contentType );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "content type:", contentType );
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
