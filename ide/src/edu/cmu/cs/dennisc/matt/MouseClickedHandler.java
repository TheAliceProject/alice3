package edu.cmu.cs.dennisc.matt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SScene;
import org.lgna.story.Visual;
import org.lgna.story.event.MouseClickEvent;
import org.lgna.story.event.MouseClickOnObjectEvent;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenEvent;
import org.lgna.story.event.MouseClickOnScreenListener;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class MouseClickedHandler extends AbstractEventHandler<Object, MouseClickEvent> {

	public static final Visual[] ALL_VISUALS = new Visual[ 0 ];
	Map<Object, CopyOnWriteArrayList<Object>> map = new ConcurrentHashMap<Object, CopyOnWriteArrayList<Object>>();
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

	@Override
	protected void nameOfFireCall( Object listener, MouseClickEvent event ) {
		if( listener instanceof MouseClickOnObjectListener ) {
			if( event.getModelAtMouseLocation() != null ) {
				MouseClickOnObjectListener mouseCOOL = (MouseClickOnObjectListener)listener;
				mouseCOOL.mouseClicked( (MouseClickOnObjectEvent)event );
			}
		} else if( listener instanceof MouseClickOnScreenListener ) {
			MouseClickOnScreenListener mouseCOSL = (MouseClickOnScreenListener)listener;
			mouseCOSL.mouseClicked( (MouseClickOnScreenEvent)event );
		} else {
			Logger.severe( listener );
		}
	}

	public MouseClickedHandler() {
		map.put( empty, new CopyOnWriteArrayList<Object>() );
		map.put( ALL_VISUALS, new CopyOnWriteArrayList<Object>() );
	}

	public void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, /* int quoteClickCountUnquote, */SScene scene ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseClickEventImp mbe = new org.lgna.story.event.MouseClickEventImp( e, scene );
			//			SModel model = mbe.getModelAtMouseLocation();
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

	public void fireAllTargeted( org.lgna.story.event.MouseClickEventImp event ) {
		if( shouldFire ) {
			if( event != null ) {
				CopyOnWriteArrayList<Object> listeners = new CopyOnWriteArrayList<Object>();
				listeners.addAll( map.get( empty ) );
				SModel modelAtMouseLocation = event.getModelAtMouseLocation();
				if( modelAtMouseLocation != null ) {
					listeners.addAll( map.get( ALL_VISUALS ) );
					if( map.get( modelAtMouseLocation ) != null ) {
						listeners.addAll( map.get( modelAtMouseLocation ) );
					}
				}
				if( listeners != null ) {
					for( Object listener : listeners ) {
						if( listener instanceof MouseClickOnScreenListener ) {
							fireEvent( listener, new MouseClickOnScreenEvent( event ) );
						} else if( listener instanceof MouseClickOnObjectListener ) {
							fireEvent( listener, new MouseClickOnObjectEvent( event ), modelAtMouseLocation );
						}
					}
				}
			}
		}
	}

	public void addListener( Object listener, MultipleEventPolicy eventPolicy, Visual[] targets ) {
		registerIsFiringMap( listener, targets );
		registerPolicyMap( listener, eventPolicy );
		if( listener instanceof MouseClickOnScreenListener ) {
			map.get( empty ).add( listener );
		} else if( targets.length > 0 ) {//targets should not be null
			for( Visual target : targets ) {
				if( map.get( target ) != null ) {
					map.get( target ).add( listener );
				} else {
					CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<Object>();
					list.add( listener );
					map.put( target, list );
				}
			}
		} else {
			map.get( ALL_VISUALS ).add( listener );
		}
	}
}
