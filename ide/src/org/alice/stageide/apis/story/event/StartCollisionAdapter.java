package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartCollisionListener;

public class StartCollisionAdapter extends AbstractAdapter implements StartCollisionListener {

	public StartCollisionAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void whenTheseCollide(StartCollisionEvent e) {
		invokeEntryPoint( e );
	}

}
