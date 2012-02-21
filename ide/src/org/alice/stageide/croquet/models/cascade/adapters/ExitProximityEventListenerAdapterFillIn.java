package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.EnterProximityListener;

public class ExitProximityEventListenerAdapterFillIn extends LambdaExpressionFillIn{
	
	private static class SingletonHolder {
		private static ExitProximityEventListenerAdapterFillIn instance = new ExitProximityEventListenerAdapterFillIn();
	}
	public static ExitProximityEventListenerAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public ExitProximityEventListenerAdapterFillIn() {
		super( java.util.UUID.fromString( "af4e3b57-e586-48ee-a788-d823000f8d43" ), EnterProximityListener.class );
	}
}