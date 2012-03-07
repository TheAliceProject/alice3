package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;

import org.lgna.story.Model;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Scene;
import org.lgna.story.Visual;
import org.lgna.story.event.MouseClickEvent;
import org.lgna.story.event.MouseClickOnObjectEvent;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;

import edu.cmu.cs.dennisc.java.util.Collections;

public class MouseClickedHandler extends AbstractEventHandler<Object,MouseClickEvent> {

	HashMap<Object,LinkedList<Object>> map = new HashMap<Object,LinkedList<Object>>();
	Object empty = new Object();
	private HashMap<Object,Class<?>> classMap = Collections.newHashMap();

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

	@Override
	protected void nameOfFireCall( Object listener, MouseClickEvent event ) {
		if( listener instanceof MouseClickOnObjectListener ) {
			MouseClickOnObjectListener mouseCOOL = (MouseClickOnObjectListener)listener;
			mouseCOOL.mouseClicked( new MouseClickOnObjectEvent( event ) );
		} else if( listener instanceof MouseClickOnScreenListener ) {
			MouseClickOnScreenListener mouseCOSL = (MouseClickOnScreenListener)listener;
			mouseCOSL.mouseClicked();
		} else if( listener instanceof MouseClickEvent ) {//TODO: Deprecated
			MouseClickOnObjectListener mouseCOOL = (MouseClickOnObjectListener)listener;
			mouseCOOL.mouseClicked( new MouseClickOnObjectEvent( event ) );
		}
	}

	public MouseClickedHandler() {
		map.put( empty, new LinkedList<Object>() );
	}
	public void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote, Scene scene ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseClickEvent mbe = new org.lgna.story.event.MouseClickEvent( e, scene );
			//			Model model = mbe.getModelAtMouseLocation();
			//todo
			//			if( model != null ) {
			this.fireAllTargeted( mbe );
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

	public void fireAllTargeted( org.lgna.story.event.MouseClickEvent event ) {
		if( shouldFire ) {
			if( event != null ) {
				LinkedList<Object> listeners = new LinkedList<Object>();
				listeners.addAll( map.get( empty ) );
				MouseClickOnObjectEvent<Model> checkEvent = new MouseClickOnObjectEvent<Model>( event );
				Model modelAtMouseLocation = checkEvent.getObjectAtMouseLocation();
				if( modelAtMouseLocation != null ) {
					if( map.get( modelAtMouseLocation ) != null ) {
						listeners.addAll( map.get( modelAtMouseLocation ) );
					}
					if( listeners != null ) {
						for( Object listener : listeners ) {
							if( classMap.get( listener ) == null || classMap.get( listener ).isAssignableFrom( checkEvent.getObjectAtMouseLocation().getClass() ) )
								fireEvent( listener, event, modelAtMouseLocation );
						}
					}
				}
			}
		}
	}
	public <T> void addListener( Object listener, Class<T> cls, MultipleEventPolicy eventPolicy, Visual[] targets ) {
		registerIsFiringMap( listener, targets );
		registerPolicyMap( listener, eventPolicy );
		classMap.put( listener, cls );
		if( targets != null && targets.length > 0 ) {
			for( Visual target : targets ) {
				if( map.get( target ) != null ) {
					map.get( target ).add( listener );
				} else {
					LinkedList<Object> list = new LinkedList<Object>();
					list.add( listener );
					map.put( target, list );
				}
			}
		} else {
			map.get( empty ).add( listener );
		}
	}
}
