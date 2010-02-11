package org.alice.virtualmachine.resources;

public class AudioResource extends org.alice.virtualmachine.Resource {
	private static java.util.Map< java.util.UUID, AudioResource > uuidToResourceMap = new java.util.HashMap< java.util.UUID, AudioResource >(); 
	private static AudioResource get( java.util.UUID uuid ) {
		AudioResource rv = uuidToResourceMap.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			rv = new AudioResource( uuid );
			uuidToResourceMap.put( uuid, rv );
		}
		return rv;
	}
	public static AudioResource valueOf( String s ) {
		return get( java.util.UUID.fromString( s ) );
	}
	private double duration = Double.NaN;
	public AudioResource( java.util.UUID uuid ) {
		super( uuid );
	}
	public AudioResource( Class<?> cls, String resourceName, String contentType ) {
		super( cls, resourceName, contentType );
	}
	public AudioResource( java.io.File file, String contentType ) {
		super( file, contentType );
	}
	public double getDuration() {
		return this.duration;
	}
	public void setDuration( double duration ) {
		this.duration = duration;
	}
	private static String XML_DURATION_ATTRIBUTE = "duration";
	@Override
	public void encodeAttributes( org.w3c.dom.Element xmlElement ) {
		super.encodeAttributes( xmlElement );
		xmlElement.setAttribute( XML_DURATION_ATTRIBUTE, Double.toString( this.duration ) );
	}
	@Override
	public void decodeAttributes( org.w3c.dom.Element xmlElement ) {
		super.decodeAttributes( xmlElement );
		this.duration = Double.parseDouble( xmlElement.getAttribute( XML_DURATION_ATTRIBUTE ) );
	}
}
