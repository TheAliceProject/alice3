package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.StartOcclusionListener;

public class StartOcclusionEventAdapter extends AbstractAdapter implements StartOcclusionListener {

	public StartOcclusionEventAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void whenTheseOcclude(StartOcclusionEvent e) {
		invokeEntryPoint( e );
	}
}
