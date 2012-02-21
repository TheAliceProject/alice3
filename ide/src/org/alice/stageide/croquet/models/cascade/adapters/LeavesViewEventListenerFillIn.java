package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.ExitViewListener;

public class LeavesViewEventListenerFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static LeavesViewEventListenerFillIn instance = new LeavesViewEventListenerFillIn();
	}
	public static LeavesViewEventListenerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public LeavesViewEventListenerFillIn() {
		super( java.util.UUID.fromString( "5527c6f4-a785-4c0d-9a7d-68f82e23f6a8" ), ExitViewListener.class );
	}
}
