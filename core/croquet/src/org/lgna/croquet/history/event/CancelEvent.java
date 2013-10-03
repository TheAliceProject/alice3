package org.lgna.croquet.history.event;

/**
 * @author Kyle J. Harms
 */
public class CancelEvent extends CompletionEvent {
	public CancelEvent( org.lgna.croquet.history.CompletionStep<?> step ) {
		super( step );
	}
}
