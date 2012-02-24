package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.TransformationEvent;
import org.lgna.story.event.TransformationListener;

public class TransformationEventAdapter extends AbstractAdapter implements TransformationListener {

	public TransformationEventAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}
	
	public void whenThisMoves(TransformationEvent e) {
		invokeEntryPoint( e );
	}
}
