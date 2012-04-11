package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.MouseClickOnScreenListener;

public class MouseClickedOnScreenAdapterFillIn extends LambdaExpressionFillIn {
	private static class SingletonHolder {
		private static MouseClickedOnScreenAdapterFillIn instance = new MouseClickedOnScreenAdapterFillIn();
	}
	public static MouseClickedOnScreenAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}
	private MouseClickedOnScreenAdapterFillIn() {
		super( java.util.UUID.fromString( "b7939d99-86ea-4f68-a42e-db044e8f3cf6" ), MouseClickOnScreenListener.class );
	}
}
