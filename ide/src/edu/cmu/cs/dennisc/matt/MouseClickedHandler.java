package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Scene;
import org.lgna.story.Visual;
import org.lgna.story.event.MouseButtonEvent;
import org.lgna.story.event.MouseButtonListener;

import edu.cmu.cs.dennisc.java.util.Collections;


public class MouseClickedHandler extends AbstractEventHandler< MouseButtonListener, MouseButtonEvent > {

	HashMap<Object, LinkedList<MouseButtonListener>> map = new HashMap<Object, LinkedList<MouseButtonListener>>();
	Object empty = new Object();

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
	
	public MouseClickedHandler() {
		map.put(empty, new LinkedList<MouseButtonListener>());
	}
	public void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote, Scene scene ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseButtonEvent mbe = new org.lgna.story.event.MouseButtonEvent( e, scene );
			Model model = mbe.getModelAtMouseLocation();
			//todo
//			if( model != null ) {
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
//			}
		}
	}

	public void fireAllTargeted(org.lgna.story.event.MouseButtonEvent event) {
		if(shouldFire){
			if(event != null){
				LinkedList<MouseButtonListener> listeners = new LinkedList<MouseButtonListener>();
				listeners.addAll(map.get(empty));
				Model modelAtMouseLocation = event.getModelAtMouseLocation();
				if(modelAtMouseLocation != null){
					if(map.get(modelAtMouseLocation) != null)
					listeners.addAll(map.get(modelAtMouseLocation));
				}
				if(listeners != null){
					for(MouseButtonListener listener: listeners){
						fireEvent(listener, event, modelAtMouseLocation);
					}
				}
			}
		}
	}
	public void addListener(MouseButtonListener mouseButtonListener, MultipleEventPolicy eventPolicy, Visual[] targets) {
		registerIsFiringMap(mouseButtonListener, targets);
		registerPolicyMap(mouseButtonListener, eventPolicy);
		if(targets != null && targets.length > 0){
			for(Visual target: targets){
				if(map.get(target) != null){
					map.get(target).add(mouseButtonListener);
				} else{
					LinkedList<MouseButtonListener> list = new LinkedList<MouseButtonListener>();
					list.add(mouseButtonListener);
					map.put(target, list);
				}
			}
		} else{
			map.get(empty).add(mouseButtonListener);
		}
	}
	@Override
	protected void fire(MouseButtonListener listener, MouseButtonEvent event) {
		listener.mouseButtonClicked(event);
	}
}
