package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.story.event.EndOcclusionListener;

public class EndOcclusionEventListenerFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static EndOcclusionEventListenerFillIn instance = new EndOcclusionEventListenerFillIn();
	}
	public static EndOcclusionEventListenerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public EndOcclusionEventListenerFillIn() {
		super( java.util.UUID.fromString( "fba52dc7-80e1-4127-ae8c-670612f75ef9" ) );
		this.transientValue = this.createValue();
	}
	@Override
	protected org.lgna.project.ast.LambdaExpression createValue() { 
		return AstUtilities.createLambdaExpression(EndOcclusionListener.class);
	}

}
