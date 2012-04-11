package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.OcclusionEndListener;

public class EndOcclusionEventListenerFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static EndOcclusionEventListenerFillIn instance = new EndOcclusionEventListenerFillIn();
	}
	public static EndOcclusionEventListenerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public EndOcclusionEventListenerFillIn() {
		super( java.util.UUID.fromString( "fba52dc7-80e1-4127-ae8c-670612f75ef9" ), OcclusionEndListener.class );
	}
}
