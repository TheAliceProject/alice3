package edu.cmu.cs.dennisc.croquet;

public abstract class CompletionStep< M extends CompletionModel > extends Step< M > {
	public CompletionStep( M model ) {
		super( model );
	}
	public CompletionStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
}
