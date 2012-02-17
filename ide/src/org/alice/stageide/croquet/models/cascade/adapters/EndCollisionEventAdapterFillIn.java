package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.story.event.EndCollisionListener;

public class EndCollisionEventAdapterFillIn extends LambdaExpressionFillIn {
	
	private static class SingletonHolder {
		private static EndCollisionEventAdapterFillIn instance = new EndCollisionEventAdapterFillIn();
	}
	public static EndCollisionEventAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public EndCollisionEventAdapterFillIn() {
		super( java.util.UUID.fromString( "d683dcd4-2449-4af2-bb0f-bfca21cbcb37" ) );
		this.transientValue = this.createValue();
	}
	@Override
	protected org.lgna.project.ast.LambdaExpression createValue() { 
		return AstUtilities.createLambdaExpression(EndCollisionListener.class);
	}

}
