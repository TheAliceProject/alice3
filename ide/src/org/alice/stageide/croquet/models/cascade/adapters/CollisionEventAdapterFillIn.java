package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.story.event.CollisionListener;

public class CollisionEventAdapterFillIn extends LambdaExpressionFillIn{
	
	private static class SingletonHolder {
		private static CollisionEventAdapterFillIn instance = new CollisionEventAdapterFillIn();
	}
	public static CollisionEventAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public CollisionEventAdapterFillIn() {
		super( java.util.UUID.fromString( "4888e172-636c-4d03-9b6a-0873017e00e9" ) );
		this.transientValue = this.createValue();
	}
	@Override
	protected org.lgna.project.ast.LambdaExpression createValue() { 
		return AstUtilities.createLambdaExpression(CollisionListener.class);
	}

}
