package org.alice.stageide.apis.story.event;

public abstract class AbstractAdapter {
	protected final org.lgna.project.virtualmachine.LambdaContext context;
	protected final org.lgna.project.virtualmachine.UserInstance userInstance;
	protected final org.lgna.project.ast.Lambda lambda;
	public AbstractAdapter( org.lgna.project.virtualmachine.LambdaContext context, org.lgna.project.ast.Lambda lambda, org.lgna.project.virtualmachine.UserInstance userInstance ) {
		this.context = context;
		this.lambda = lambda;
		this.userInstance = userInstance;
		assert this.userInstance != null;
	}

}
