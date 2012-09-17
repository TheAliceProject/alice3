package org.alice.stageide.apis.story.event;

public abstract class AbstractAdapter {
	private final org.lgna.project.virtualmachine.LambdaContext context;
	private final org.lgna.project.virtualmachine.UserInstance userInstance;
	private final org.lgna.project.ast.Lambda lambda;
	private final org.lgna.project.ast.JavaMethod singleAbstractMethod;

	public AbstractAdapter( org.lgna.project.virtualmachine.LambdaContext context, org.lgna.project.ast.Lambda lambda, org.lgna.project.virtualmachine.UserInstance userInstance ) {
		this.context = context;
		this.lambda = lambda;
		this.userInstance = userInstance;
		assert this.userInstance != null;

		Class<?>[] interfaces = this.getClass().getInterfaces();
		if( interfaces.length == 1 ) {
			java.lang.reflect.Method[] mthds = interfaces[ 0 ].getDeclaredMethods();
			if( mthds.length == 1 ) {
				this.singleAbstractMethod = org.lgna.project.ast.JavaMethod.getInstance( mthds[ 0 ] );
			} else {
				this.singleAbstractMethod = null;
			}
		} else {
			this.singleAbstractMethod = null;
		}
	}

	protected void invokeEntryPoint( Object... arguments ) {
		this.context.invokeEntryPoint( this.lambda, this.singleAbstractMethod, this.userInstance, arguments );
	}

}
