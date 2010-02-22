package org.alice.virtualmachine.resources;

public class AudioResource extends org.alice.virtualmachine.Resource {
	private static java.util.Map< String, String > extensionToContentTypeMap;
	static {
		AudioResource.extensionToContentTypeMap = new java.util.HashMap< String, String >();
		AudioResource.extensionToContentTypeMap.put( "au", "audio.basic" );
		AudioResource.extensionToContentTypeMap.put( "wav", "audio.x_wav" );
		AudioResource.extensionToContentTypeMap.put( "mp3", "audio.mpeg" );
	}

	public static String getContentType( String path ) {
		String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( path );
		String contentType = AudioResource.extensionToContentTypeMap.get( extension.toLowerCase() );
		return contentType;
	}
	public static String getContentType( java.io.File file ) {
		return getContentType( file.getName() );
	}
	public static boolean isAcceptableContentType( String contentType ) {
		return AudioResource.extensionToContentTypeMap.containsValue( contentType );
	}
	
	public static java.io.FilenameFilter createFilenameFilter( final boolean areDirectoriesAccepted ) {
		return new java.io.FilenameFilter() {
			public boolean accept( java.io.File dir, String name ) {
				java.io.File file = new java.io.File( dir, name );
				if( file.isDirectory() ) {
					return areDirectoriesAccepted;
				} else {
					return getContentType( name ) != null;
				}
			}
		};
	}

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
	public AudioResource( Class<?> cls, String resourceName ) {
		this( cls, resourceName, getContentType( resourceName ) );
	}
	public AudioResource( java.io.File file, String contentType ) throws java.io.IOException {
		super( file, contentType );
	}
	public AudioResource( java.io.File file ) throws java.io.IOException {
		this( file, getContentType( file ) );
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
	public void decodeAttributes( org.w3c.dom.Element xmlElement, byte[] data ) {
		super.decodeAttributes( xmlElement, data );
		this.duration = Double.parseDouble( xmlElement.getAttribute( XML_DURATION_ATTRIBUTE ) );
	}
}
