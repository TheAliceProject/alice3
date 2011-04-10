package edu.cmu.cs.dennisc.croquet;

public abstract class PrepStep<M extends PrepModel> extends Step< M > {
	public PrepStep( M model ) {
		super( model );
	}
	public PrepStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
}
