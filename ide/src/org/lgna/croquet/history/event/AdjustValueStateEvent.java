package org.lgna.croquet.history.event;

public class AdjustValueStateEvent extends Event< org.lgna.croquet.history.Transaction > {
	public AdjustValueStateEvent( org.lgna.croquet.history.Transaction transaction ) {
		super( transaction );
	}
}
