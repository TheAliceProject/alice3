package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;

public class TransformationEventAdapter extends AbstractAdapter implements PointOfViewChangeListener {

	public TransformationEventAdapter( LambdaContext context, Lambda lambda, UserInstance userInstance ) {
		super( context, lambda, userInstance );
	}

	public void pointOfViewChanged( PointOfViewEvent e ) {
		invokeEntryPoint( e );
	}
}
