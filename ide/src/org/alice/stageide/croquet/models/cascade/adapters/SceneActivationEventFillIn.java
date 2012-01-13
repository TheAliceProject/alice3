package org.alice.stageide.croquet.models.cascade.adapters;

import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.story.event.SceneActivationListener;

public class SceneActivationEventFillIn extends LambdaExpressionFillIn {
	private static class SingletonHolder {
		private static SceneActivationEventFillIn instance = new SceneActivationEventFillIn();
	}
	public static SceneActivationEventFillIn getInstance() {
		return SingletonHolder.instance;
	}

	public SceneActivationEventFillIn() {
		super(java.util.UUID.fromString( "b9b19a9e-cbc8-426f-a781-85a795eb424b" ));
		this.transientValue = this.createValue();
	}

	@Override
	protected LambdaExpression createValue() {
		return AstUtilities.createLambdaExpression(SceneActivationListener.class);
	}

}
