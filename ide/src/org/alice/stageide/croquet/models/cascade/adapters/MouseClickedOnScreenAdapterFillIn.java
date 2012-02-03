package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.story.event.MouseClickListener;
import org.lgna.story.event.MouseClickOnScreenListener;

public class MouseClickedOnScreenAdapterFillIn extends LambdaExpressionFillIn {
	private static class SingletonHolder {
		private static MouseClickedOnScreenAdapterFillIn instance = new MouseClickedOnScreenAdapterFillIn();
	}
	public static MouseClickedOnScreenAdapterFillIn getInstance() {
		return SingletonHolder.instance;
	}
	private MouseClickedOnScreenAdapterFillIn() {
		super( java.util.UUID.fromString( "b7939d99-86ea-4f68-a42e-db044e8f3cf6" ) );
		this.transientValue = this.createValue();
	}
	@Override
	protected org.lgna.project.ast.LambdaExpression createValue() { 
		return AstUtilities.createLambdaExpression(MouseClickOnScreenListener.class);
	}
}
