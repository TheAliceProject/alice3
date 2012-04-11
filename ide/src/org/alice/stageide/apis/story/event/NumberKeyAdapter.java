package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.NumberKeyEvent;

public class NumberKeyAdapter extends AbstractAdapter implements org.lgna.story.event.NumberKeyPressListener {
	public NumberKeyAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void numberKeyPressed(NumberKeyEvent e) {
		invokeEntryPoint( e );
	}
}
