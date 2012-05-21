package org.lgna.croquet.history.event;

/**
 * @author Kyle J. Harms
 */
public class AddTransactionEvent extends Event< org.lgna.croquet.history.Transaction > {
	private int index;

	public AddTransactionEvent( org.lgna.croquet.history.Transaction transaction, int index ) {
		super( transaction );
		this.index = index;
	}

	public org.lgna.croquet.history.Transaction getTransaction() {
		return this.getNode();
	}

	public int getIndex() {
		return this.index;
	}
}
