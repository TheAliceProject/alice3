package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.ViewEnterListener;

public class ComesIntoViewEventAdapter extends AbstractAdapter implements ViewEnterListener {

	public ComesIntoViewEventAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void viewEntered(ComesIntoViewEvent e) {
		invokeEntryPoint( e );
	}
}
