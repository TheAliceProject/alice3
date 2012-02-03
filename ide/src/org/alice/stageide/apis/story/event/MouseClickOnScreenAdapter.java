package org.alice.stageide.apis.story.event;

import org.lgna.story.event.MouseButtonEvent;

public class MouseClickOnScreenAdapter extends AbstractAdapter implements org.lgna.story.event.MouseClickOnScreenListener {
	public MouseClickOnScreenAdapter( org.lgna.project.virtualmachine.LambdaContext context, org.lgna.project.ast.Lambda lambda, org.lgna.project.virtualmachine.UserInstance userInstance ) {
		super(context, lambda, userInstance);
	}
	public void mouseClicked() {
		this.context.invokeEntryPoint( this.lambda, this.userInstance );
	}
}
