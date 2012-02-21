package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.NumberKeyPressListener;

public class NumberKeyAdapterFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static NumberKeyAdapterFillIn instance = new NumberKeyAdapterFillIn();
	}
	public static NumberKeyAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public NumberKeyAdapterFillIn() {
		super( java.util.UUID.fromString( "c9fbf4e3-15be-40de-a456-a94729852aa5" ), NumberKeyPressListener.class );
	}
}
