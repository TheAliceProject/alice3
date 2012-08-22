package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.story.event.PointOfViewChangeListener;

public class TransformationEventAdapterFillIn extends LambdaExpressionFillIn {

	private static class SingletonHolder {
		private static TransformationEventAdapterFillIn instance = new TransformationEventAdapterFillIn();
	}

	public static TransformationEventAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public TransformationEventAdapterFillIn() {
		super( java.util.UUID.fromString( "486848e6-16a4-4697-bd69-c04f4d0e05ee" ), PointOfViewChangeListener.class );
	}
}
