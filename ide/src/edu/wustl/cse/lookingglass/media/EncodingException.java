package edu.wustl.cse.lookingglass.media;

public class EncodingException extends RuntimeException {

	private static final long serialVersionUID = -1149452336821467247L;

	public EncodingException( Exception e ) {
		super( e );
	}

	public EncodingException( String output, String error ) {
		super( output + "\n\n" + error );
	}

	public EncodingException( Exception e, String output, String error ) {
		super( output + "\n\n" + error, e );
	}
}
