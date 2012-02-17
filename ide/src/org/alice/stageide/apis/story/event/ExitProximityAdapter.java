package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ExitProximityListener;

public class ExitProximityAdapter extends AbstractAdapter implements ExitProximityListener{

	public ExitProximityAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void whenTheseMoveApart(ExitProximityEvent e) {
		invokeEntryPoint( e );
	}

}
