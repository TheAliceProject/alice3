package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.MouseClickOnObjectListener;

public class MouseClickOnObjectFillerInner extends LambdaExpressionFillIn {
	private static class SingletonHolder {
		private static MouseClickOnObjectFillerInner instance = new MouseClickOnObjectFillerInner();
	}
	public static MouseClickOnObjectFillerInner getInstance() {
		return SingletonHolder.instance;
	}
	private MouseClickOnObjectFillerInner() {
		super( java.util.UUID.fromString( "705cf4c9-ab82-420d-949c-b99be0ed9728" ), MouseClickOnObjectListener.class );
	}
}
