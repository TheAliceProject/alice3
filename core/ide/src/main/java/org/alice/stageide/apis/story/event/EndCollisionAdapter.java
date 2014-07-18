package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.EndCollisionEvent;

public class EndCollisionAdapter extends AbstractAdapter implements CollisionEndListener {

	public EndCollisionAdapter( LambdaContext context, Lambda lambda, UserInstance userInstance ) {
		super( context, lambda, userInstance );
	}

	@Override
	public void collisionEnded( EndCollisionEvent e ) {
		invokeEntryPoint( e );
	}
}
