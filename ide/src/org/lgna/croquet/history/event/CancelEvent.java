package org.lgna.croquet.history.event;

/**
 * @author Kyle J. Harms
 */
public class CancelEvent extends Event<org.lgna.croquet.history.CompletionStep> {
	public CancelEvent( org.lgna.croquet.history.CompletionStep<?> step ) {
		super( step );
	}
}
