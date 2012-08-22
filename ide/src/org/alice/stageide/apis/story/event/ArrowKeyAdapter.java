package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.ArrowKeyEvent;

public class ArrowKeyAdapter extends AbstractAdapter implements org.lgna.story.event.ArrowKeyPressListener {
	public ArrowKeyAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void arrowKeyPressed(ArrowKeyEvent e) {
		invokeEntryPoint( e );
	}
}
