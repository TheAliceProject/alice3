package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionListener;

public class CollisionAdapter extends AbstractAdapter implements CollisionListener{

	public CollisionAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void whenTheseCollide(CollisionEvent e) {
		this.context.invokeEntryPoint( this.lambda, this.userInstance, e );
	}

}
