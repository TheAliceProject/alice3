package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.story.event.ComesIntoViewEventListener;

public class ComesIntoViewEventListenerFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static ComesIntoViewEventListenerFillIn instance = new ComesIntoViewEventListenerFillIn();
	}
	public static ComesIntoViewEventListenerFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public ComesIntoViewEventListenerFillIn() {
		super( java.util.UUID.fromString( "d3a7b186-8481-4069-b617-aa14f3f84701" ) );
		this.transientValue = this.createValue();
	}
	@Override
	protected org.lgna.project.ast.LambdaExpression createValue() { 
		return AstUtilities.createLambdaExpression(ComesIntoViewEventListener.class);
	}

}
