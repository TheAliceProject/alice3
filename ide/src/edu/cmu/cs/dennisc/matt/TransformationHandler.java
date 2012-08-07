package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lgna.story.SThing;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;

import edu.cmu.cs.dennisc.java.util.Collections;

public class TransformationHandler extends TransformationChangedHandler<PointOfViewChangeListener,PointOfViewEvent> {

	private Map<SThing,List<PointOfViewChangeListener>> checkMap = Collections.newHashMap();

	public <A extends SMovableTurnable> void addTransformationListener( PointOfViewChangeListener transformationlistener, Class<A> a, ArrayList<A> shouldListenTo, MultipleEventPolicy policy ) {
		super.addSoloListener( transformationlistener, shouldListenTo, a, policy );
	}

	@Override
	protected void ammend( Object key, int group, SThing newObject ) {
		ImplementationAccessor.getImplementation( newObject ).getSgComposite().addAbsoluteTransformationListener( this );
		EventBuilder.ammend( key, group, newObject );
	}

	@Override
	protected void check( SThing changedEntity ) {
		for( PointOfViewChangeListener listener : checkMap.get( changedEntity ) ) {
			SThing[] arr = { changedEntity };
			fireEvent( listener, EventBuilder.buildViewEvent( PointOfViewEvent.class, listener, arr ) );
		}
	}

	@Override
	protected void nameOfFireCall( PointOfViewChangeListener listener, PointOfViewEvent event ) {
		listener.pointOfViewChanged( event );
	}
}
