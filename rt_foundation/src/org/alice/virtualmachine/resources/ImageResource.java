package org.alice.virtualmachine.resources;

public class ImageResource extends org.alice.virtualmachine.Resource {
	private static java.util.Map< java.util.UUID, ImageResource > uuidToResourceMap = new java.util.HashMap< java.util.UUID, ImageResource >(); 
	private static ImageResource get( java.util.UUID uuid ) {
		ImageResource rv = uuidToResourceMap.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			rv = new ImageResource( uuid );
			uuidToResourceMap.put( uuid, rv );
		}
		return rv;
	}
	public static ImageResource valueOf( String s ) {
		return get( java.util.UUID.fromString( s ) );
	}
	private int width = -1;
	private int height = -1;
	public ImageResource( java.util.UUID uuid ) {
		super( uuid );
	}
	public ImageResource( Class<?> cls, String resourceName, String contentType ) {
		super( cls, resourceName, contentType );
	}
	public ImageResource( java.io.File file, String contentType ) {
		super( file, contentType );
	}
	public int getWidth() {
		return this.width;
	}
	public void setWidth( int width ) {
		this.width = width;
	}
	public int getHeight() {
		return this.height;
	}
	public void setHeight( int height ) {
		this.height = height;
	}
	private static String XML_WIDTH_ATTRIBUTE = "width";
	private static String XML_HEIGHT_ATTRIBUTE = "height";
	@Override
	public void encodeAttributes( org.w3c.dom.Element xmlElement ) {
		super.encodeAttributes( xmlElement );
		xmlElement.setAttribute( XML_WIDTH_ATTRIBUTE, Integer.toString( this.width ) );
		xmlElement.setAttribute( XML_HEIGHT_ATTRIBUTE, Integer.toString( this.height ) );
	}
	@Override
	public void decodeAttributes( org.w3c.dom.Element xmlElement ) {
		super.decodeAttributes( xmlElement );
		this.width = Integer.parseInt( xmlElement.getAttribute( XML_WIDTH_ATTRIBUTE ) );
		this.height = Integer.parseInt( xmlElement.getAttribute( XML_HEIGHT_ATTRIBUTE ) );
	}
}
