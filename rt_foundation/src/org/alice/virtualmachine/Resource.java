package org.alice.virtualmachine;

public abstract class Resource implements edu.cmu.cs.dennisc.pattern.Nameable, edu.cmu.cs.dennisc.pattern.NameChangeListenable {
	private java.util.UUID uuid;
	private String name;
	private String originalFileName;
	private String contentType;
	private byte[] data;
	private java.util.List< edu.cmu.cs.dennisc.pattern.event.NameListener > nameListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.pattern.event.NameListener >();
	private java.util.List< org.alice.virtualmachine.event.ResourceContentListener > contentListeners = new java.util.LinkedList< org.alice.virtualmachine.event.ResourceContentListener >();

	protected Resource( java.util.UUID uuid ) {
		this.uuid = uuid;
	}
	protected Resource( Class<?> cls, String resourceName, String contentType ) {
		this.uuid = null;
		try {
			byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( cls, resourceName );
			this.setOriginalFileName( resourceName );
			this.setName( resourceName );
			this.setContent( contentType, data );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( resourceName, ioe );
		}
	}
	protected Resource( java.io.File file, String contentType ) throws java.io.IOException {
		this.uuid = java.util.UUID.randomUUID();
		String resourceName = file.getName();
		byte[] data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( file );
		this.setOriginalFileName( resourceName );
		this.setName( resourceName );
		this.setContent( contentType, data );
	}
	
	public String getName() {
		return this.name;
	}
	public void setName( String name ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areNotEquivalent( this.name, name ) ) {
			synchronized( this.nameListeners ) {
				edu.cmu.cs.dennisc.pattern.event.NameEvent nameEvent = new edu.cmu.cs.dennisc.pattern.event.NameEvent( this, name );
				for( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener : this.nameListeners ) {
					nameListener.nameChanging( nameEvent );
				}
				this.name = name;
				for( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener : this.nameListeners ) {
					nameListener.nameChanged( nameEvent );
				}
			}
		}
	}
	public void addNameListener( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener ) {
		assert nameListener != null;
		synchronized( this.nameListeners ) {
			this.nameListeners.add( nameListener );
		}
	}
	public void removeNameListener( edu.cmu.cs.dennisc.pattern.event.NameListener nameListener ) {
		assert nameListener != null;
		synchronized( this.nameListeners ) {
			this.nameListeners.remove( nameListener );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.pattern.event.NameListener > getNameListeners() {
		return this.nameListeners;
	}
	
	public void addContentListener( org.alice.virtualmachine.event.ResourceContentListener contentListener ) {
		assert contentListener != null;
		synchronized( this.contentListeners ) {
			this.contentListeners.add( contentListener );
		}
	}
	public void removeContentListener( org.alice.virtualmachine.event.ResourceContentListener contentListener ) {
		assert contentListener != null;
		synchronized( this.contentListeners ) {
			this.contentListeners.remove( contentListener );
		}
	}
	public Iterable< org.alice.virtualmachine.event.ResourceContentListener > getContentListeners() {
		return this.contentListeners;
	}
	
	
	public java.util.UUID getUUID() {
		return this.uuid;
	}
	public String getContentType() {
		return this.contentType;
	}
	public byte[] getData() {
		return this.data;
	}
	public void setContent( String contentType, byte[] data ) {
		synchronized( this.contentListeners ) {
			
			//todo
			org.alice.virtualmachine.event.ResourceContentListener[] array = edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( this.contentListeners, org.alice.virtualmachine.event.ResourceContentListener.class );
			
			
			org.alice.virtualmachine.event.ResourceContentEvent e = new org.alice.virtualmachine.event.ResourceContentEvent( this, contentType, data );
			for( org.alice.virtualmachine.event.ResourceContentListener contentListener : array ) {
				contentListener.contentChanging( e );
			}
			this.contentType = contentType;
			this.data = data;
			for( org.alice.virtualmachine.event.ResourceContentListener contentListener : array ) {
				contentListener.contentChanged( e );
			}
		}
	}
	public String getOriginalFileName() {
		return this.originalFileName;
	}
	public void setOriginalFileName( String originalFileName ) {
		this.originalFileName = originalFileName;
	}
	private static String XML_NAME_ATTRIBUTE = "name";
	private static String XML_ORIGINAL_FILE_NAME_ATTRIBUTE = "originalFileName";
	private static String XML_CONTENT_TYPE_ATTRIBUTE = "contentType";
	public void decodeAttributes( org.w3c.dom.Element xmlElement, byte[] data ) {
		this.setName( xmlElement.getAttribute( XML_NAME_ATTRIBUTE ) );
		this.setOriginalFileName( xmlElement.getAttribute( XML_ORIGINAL_FILE_NAME_ATTRIBUTE ) );
		this.setContent( xmlElement.getAttribute( XML_CONTENT_TYPE_ATTRIBUTE ), data );
	}
	public void encodeAttributes( org.w3c.dom.Element xmlElement ) {
		xmlElement.setAttribute( XML_NAME_ATTRIBUTE, this.getName() );
		xmlElement.setAttribute( XML_ORIGINAL_FILE_NAME_ATTRIBUTE, this.getOriginalFileName() );
		xmlElement.setAttribute( XML_CONTENT_TYPE_ATTRIBUTE, this.getContentType() );
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( Resource.class.getName() );
		sb.append( "[name=" );
		sb.append( this.getName() );
		sb.append( ";contentType=" );
		sb.append( this.getContentType() );
		sb.append( ";uuid=" );
		sb.append( this.getUUID() );
		sb.append( "]" );
		return sb.toString();
	}
}
