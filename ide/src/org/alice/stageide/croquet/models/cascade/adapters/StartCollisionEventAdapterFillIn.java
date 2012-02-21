package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.StartCollisionListener;

public class StartCollisionEventAdapterFillIn extends LambdaExpressionFillIn{
	
	private static class SingletonHolder {
		private static StartCollisionEventAdapterFillIn instance = new StartCollisionEventAdapterFillIn();
	}
	public static StartCollisionEventAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public StartCollisionEventAdapterFillIn() {
		super( java.util.UUID.fromString( "4888e172-636c-4d03-9b6a-0873017e00e9" ), StartCollisionListener.class );
	}
}
