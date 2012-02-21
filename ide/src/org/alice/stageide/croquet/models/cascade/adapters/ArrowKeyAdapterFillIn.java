package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.ArrowKeyPressListener;

public class ArrowKeyAdapterFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static ArrowKeyAdapterFillIn instance = new ArrowKeyAdapterFillIn();
	}
	public static ArrowKeyAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public ArrowKeyAdapterFillIn() {
		super( java.util.UUID.fromString( "4bf403d0-5ad8-4068-9b49-55a3e72e0c9e" ), ArrowKeyPressListener.class );
	}
}
