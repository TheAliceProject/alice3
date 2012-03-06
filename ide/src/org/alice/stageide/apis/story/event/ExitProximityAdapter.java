package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ProximityExitListener;

public class ExitProximityAdapter extends AbstractAdapter implements ProximityExitListener {

	public ExitProximityAdapter( LambdaContext context, Lambda lambda, UserInstance userInstance ) {
		super( context, lambda, userInstance );
	}

	public void proximityExited( ExitProximityEvent e ) {
		invokeEntryPoint( e );
	}

}
