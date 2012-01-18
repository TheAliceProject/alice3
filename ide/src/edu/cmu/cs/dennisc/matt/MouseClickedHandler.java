package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Scene;
import org.lgna.story.Visual;
import org.lgna.story.event.MouseButtonEvent;
import org.lgna.story.event.MouseButtonListener;


public class MouseClickedHandler extends AbstractEventHandler< MouseButtonListener, MouseButtonEvent > {

	HashMap<Visual, LinkedList<MouseButtonListener>> map = new HashMap<Visual, LinkedList<MouseButtonListener>>();

	private boolean isMouseButtonListenerInExistence() {
//		if( this.mouseButtonListeners.size() > 0 ) {
//			return true;
//		} else {
//			for( Transformable component : this.getComponents() ) {
//				if( component instanceof Model ) {
//					Model model = (Model)component;
//					if( model.getMouseButtonListeners().size() > 0 ) {
//						return true;
//					}
//				}
//			}
//			return false;
//		}
		return true;
	}
	public void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote, Scene scene ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseButtonEvent mbe = new org.lgna.story.event.MouseButtonEvent( e, scene );
			Model model = mbe.getModelAtMouseLocation();
			//todo
			if( model != null ) {
				this.fireAllTargeted(mbe);
//
//				for( final org.lgna.story.event.MouseButtonListener mouseButtonListener : this.mouseButtonListeners ) {
//					Logger.todo( "use parent tracking thread" );
//					new Thread() {
//						@Override
//						public void run() {
//							ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
//								public void run() {
//									mouseButtonListener.mouseButtonClicked( mbe );
//								}
//							} );
//						}
//					}.start();
//				}
//				for( final org.alice.apis.moveandturn.event.MouseButtonListener mouseButtonListener : model.getMouseButtonListeners() ) {
//					new Thread() {
//						@Override
//						public void run() {
//							edu.cmu.cs.dennisc.alice.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
//								public void run() {
//									mouseButtonListener.mouseButtonClicked( mbe );
//								}
//							} );
//						}
//					}.start();
//				}
			}
		}
	}

	public void fireAllTargeted(org.lgna.story.event.MouseButtonEvent event) {
		if(shouldFire){
			if(event != null){
				LinkedList<MouseButtonListener> listeners = map.get(event.getModelAtMouseLocation());
				if(listeners != null){
					for(MouseButtonListener listener: listeners){
						fireEvent(listener, event, event.getModelAtMouseLocation());
					}
				}
			}
		}
	}
	public void addListener(MouseButtonListener mouseButtonListener, MultipleEventPolicy eventPolicy, Visual[] targets) {
//		isFiringMap.put(mouseButtonListener, false);
		isFiringMap.put(mouseButtonListener, new HashMap< Object, Boolean >());
		policyMap.put(mouseButtonListener, eventPolicy);
		for(Visual target: targets){
			if(map.get(target) != null){
				map.get(target).add(mouseButtonListener);
			} else{
				LinkedList<MouseButtonListener> list = new LinkedList<MouseButtonListener>();
				list.add(mouseButtonListener);
				map.put(target, list);
				isFiringMap.get(mouseButtonListener).put( target, false );
			}
		}
	}
	@Override
	protected void fire(MouseButtonListener listener, MouseButtonEvent event) {
		listener.mouseButtonClicked(event);
	}
}
