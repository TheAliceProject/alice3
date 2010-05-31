package org.alice.app.operations.file;

public class LoadUriEdit extends edu.cmu.cs.dennisc.croquet.Edit {
	private java.net.URI uri;
	public LoadUriEdit( edu.cmu.cs.dennisc.croquet.ModelContext context, java.net.URI uri ) {
		super( context );
		this.uri = uri;
	}
	@Override
	public boolean canRedo() {
		return false;
	}
	@Override
	public boolean canUndo() {
		return false;
	}
	@Override
	public void doOrRedo(boolean isDo) {
		if( isDo ) {
			org.alice.app.ProjectApplication application = org.alice.app.ProjectApplication.getSingleton();
			application.loadProjectFrom( uri );
		} else {
			throw new AssertionError();
		}
	}
	@Override
	public void undo() {
		throw new AssertionError();
	}
	@Override
	protected void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
	}
	@Override
	protected void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
	}
	
	@Override
	protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
		rv.append( "load: " );
		rv.append( this.uri );
		return rv;
	}

}
