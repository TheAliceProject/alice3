package edu.cmu.cs.dennisc.media;

class ByteArraySeekablePullSourceStream implements javax.media.protocol.PullSourceStream, javax.media.protocol.Seekable {
	private static final javax.media.protocol.ContentDescriptor RAW_CONTENT_DISCRIPTOR = new javax.media.protocol.ContentDescriptor( javax.media.protocol.ContentDescriptor.RAW );
	private byte[] data;
	private long location;

	public ByteArraySeekablePullSourceStream( byte[] data ) {
		this.data = data;
		this.location = 0;
	}
	public int read( byte[] buffer, int offset, int length ) throws java.io.IOException {
		long bytesLeft = (this.data.length - this.location);
		if( bytesLeft == 0 ) {
			return -1;
		}
		int intBytesLeft = (int)bytesLeft;
		int toRead = length;
		if( intBytesLeft < length )
			toRead = intBytesLeft;
		System.arraycopy( this.data, (int)this.location, buffer, offset, toRead );
		this.location = this.location + toRead;
		return toRead;
	}
	public Object getControl( String controlType ) {
		return null;
	}
	public Object[] getControls() {
		return null;
	}
	public javax.media.protocol.ContentDescriptor getContentDescriptor() {
		return RAW_CONTENT_DISCRIPTOR;
	}
	public boolean endOfStream() {
		return (this.location == this.data.length);
	}
	public long getContentLength() {
		return this.data.length;
	}
	public boolean willReadBlock() {
		return endOfStream();
	}
	public boolean isRandomAccess() {
		return true;
	}
	public long seek( long where ) {
		if( where > this.data.length ) {
			this.location = this.data.length;
		} else {
			this.location = where;
		}
		return this.location;
	}
	public long tell() {
		return this.location;
	}
}

class ByteArrayDataSource extends javax.media.protocol.PullDataSource {
	private byte[] data;
	private String contentType;

	public ByteArrayDataSource( byte[] data, String contentType ) {
		this.data = data;
		this.contentType = contentType;
	}
	public byte[] getData() {
		return this.data;
	}
	@Override
	public String getContentType() {
		return this.contentType;
	}
	@Override
	public javax.media.Time getDuration() {
		return javax.media.Duration.DURATION_UNKNOWN;
	}
	@Override
	public void connect() throws java.io.IOException {
	}
	@Override
	public void start() throws java.io.IOException {
	}
	@Override
	public void disconnect() {
	}
	@Override
	public Object getControl( String parm1 ) {
		return null;
	}
	@Override
	public javax.media.protocol.PullSourceStream[] getStreams() {
		return new javax.media.protocol.PullSourceStream[] { new ByteArraySeekablePullSourceStream( this.data ) };
	}
	@Override
	public void stop() throws java.io.IOException {
	}
	@Override
	public Object[] getControls() {
		return null;
	}
}

public class Manager {
	private static java.util.Map< java.net.URL, javax.media.protocol.DataSource > urlToDataSourceMap = new java.util.HashMap< java.net.URL, javax.media.protocol.DataSource >();
	private static java.util.Map< String, String > extensionToContentTypeMap;
	static {
		System.out.print( "Attempting to register mp3 capability... " );
		com.sun.media.codec.audio.mp3.JavaDecoder.main( new String[] {} );
		Manager.extensionToContentTypeMap = new java.util.HashMap< String, String >();
		Manager.extensionToContentTypeMap.put( "mp3", javax.media.protocol.FileTypeDescriptor.MPEG_AUDIO );
		Manager.extensionToContentTypeMap.put( "wav", javax.media.protocol.FileTypeDescriptor.WAVE );
		Manager.extensionToContentTypeMap.put( "au", javax.media.protocol.FileTypeDescriptor.BASIC_AUDIO );
	}

	public static javax.media.Player getPlayer( edu.cmu.cs.dennisc.resource.Resource resource ) {
		java.net.URL url = resource.getURL();
		javax.media.protocol.DataSource dataSource = Manager.urlToDataSourceMap.get( url );
		if( dataSource != null ) {
			//pass
		} else {
			byte[] data;
			try {
				data = edu.cmu.cs.dennisc.io.InputStreamUtilities.getBytes( resource.getInputStream() );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( url.toExternalForm(), ioe );
			}
			String path = url.getPath();
			String extension = edu.cmu.cs.dennisc.io.FileUtilities.getExtension( path );
			String contentType = Manager.extensionToContentTypeMap.get( extension.toLowerCase() );
			assert contentType != null : extension;
			dataSource = new ByteArrayDataSource( data, contentType );
			Manager.urlToDataSourceMap.put( url, dataSource );
		}
		try {
			return javax.media.Manager.createPlayer( dataSource );
		} catch( javax.media.NoPlayerException npe ) {
			throw new RuntimeException( url.toExternalForm(), npe );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( url.toExternalForm(), ioe );
		}
	}
}
