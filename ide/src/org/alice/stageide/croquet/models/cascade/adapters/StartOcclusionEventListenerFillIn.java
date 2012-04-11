package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.OcclusionStartListener;

public class StartOcclusionEventListenerFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static StartOcclusionEventListenerFillIn instance = new StartOcclusionEventListenerFillIn();
	}
	public static StartOcclusionEventListenerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public StartOcclusionEventListenerFillIn() {
		super( java.util.UUID.fromString( "3e3f4c80-e82f-4d12-9263-959fdbb76735" ), OcclusionStartListener.class );
	}
}
