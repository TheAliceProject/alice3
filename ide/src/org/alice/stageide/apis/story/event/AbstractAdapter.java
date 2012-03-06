package org.alice.stageide.apis.story.event;

public abstract class AbstractAdapter {
	private final org.lgna.project.virtualmachine.LambdaContext context;
	private final org.lgna.project.virtualmachine.UserInstance userInstance;
	private final org.lgna.project.ast.Lambda lambda;
	public AbstractAdapter( org.lgna.project.virtualmachine.LambdaContext context, org.lgna.project.ast.Lambda lambda, org.lgna.project.virtualmachine.UserInstance userInstance ) {
		this.context = context;
		this.lambda = lambda;
		this.userInstance = userInstance;
		assert this.userInstance != null;
	}
	protected void invokeEntryPoint( Object... arguments ) {
		this.context.invokeEntryPoint( this.lambda, this.userInstance, arguments );
	}

}
