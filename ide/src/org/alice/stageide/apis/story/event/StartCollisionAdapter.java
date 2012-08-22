package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.StartCollisionEvent;

public class StartCollisionAdapter extends AbstractAdapter implements CollisionStartListener {

	public StartCollisionAdapter( LambdaContext context, Lambda lambda, UserInstance userInstance ) {
		super( context, lambda, userInstance );
	}

	public void collisionStarted( StartCollisionEvent e ) {
		invokeEntryPoint( e );
	}

}
