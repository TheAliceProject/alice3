package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.story.event.CollisionListener;

public class OcclusionEventListenerFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static OcclusionEventListenerFillIn instance = new OcclusionEventListenerFillIn();
	}
	public static OcclusionEventListenerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public OcclusionEventListenerFillIn() {
		super( java.util.UUID.fromString( "3e3f4c80-e82f-4d12-9263-959fdbb76735" ) );
		this.transientValue = this.createValue();
	}
	@Override
	protected org.lgna.project.ast.LambdaExpression createValue() { 
		return AstUtilities.createLambdaExpression(CollisionListener.class);
	}

}
