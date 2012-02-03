package org.alice.stageide.apis.story.event;

import org.lgna.story.event.MouseClickedOnObjectEvent;

public class MouseClickOnObjectAdapter extends AbstractAdapter implements org.lgna.story.event.MouseClickOnObjectListener {
	public MouseClickOnObjectAdapter( org.lgna.project.virtualmachine.LambdaContext context, org.lgna.project.ast.Lambda lambda, org.lgna.project.virtualmachine.UserInstance userInstance ) {
		super(context, lambda, userInstance);
	}
	public void mouseClicked(MouseClickedOnObjectEvent e) {
		this.context.invokeEntryPoint( this.lambda, this.userInstance, e );
	}
}
