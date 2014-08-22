package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.StartOcclusionEvent;

public class StartOcclusionEventAdapter extends AbstractAdapter implements OcclusionStartListener {

	public StartOcclusionEventAdapter( LambdaContext context, Lambda lambda, UserInstance userInstance ) {
		super( context, lambda, userInstance );
	}

	@Override
	public void occlusionStarted( StartOcclusionEvent e ) {
		invokeEntryPoint( e );
	}
}
