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
import org.lgna.story.event.MouseClickOnScreenListener;

public class MouseClickedHandler extends AbstractEventHandler<Object, MouseClickEvent> {

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
				mouseCOOL.mouseClicked( new MouseClickOnObjectEvent( event ) );
			}
		} else if( listener instanceof MouseClickOnScreenListener ) {
			MouseClickOnScreenListener mouseCOSL = (MouseClickOnScreenListener)listener;
			mouseCOSL.mouseClicked();
		} else if( listener instanceof MouseClickEvent ) {//TODO: Deprecated
			MouseClickOnObjectListener mouseCOOL = (MouseClickOnObjectListener)listener;
			mouseCOOL.mouseClicked( new MouseClickOnObjectEvent( event ) );
		}
	}

	public MouseClickedHandler() {
		map.put( empty, new CopyOnWriteArrayList<Object>() );
	}

	public void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, /* int quoteClickCountUnquote, */SScene scene ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseClickEvent mbe = new org.lgna.story.event.MouseClickEvent( e, scene );
			SModel model = mbe.getModelAtMouseLocation();
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
				CopyOnWriteArrayList<Object> listeners = new CopyOnWriteArrayList<Object>();
				listeners.addAll( map.get( empty ) );
				SModel modelAtMouseLocation = event.getModelAtMouseLocation();
				if( modelAtMouseLocation != null ) {
					if( map.get( modelAtMouseLocation ) != null ) {
						listeners.addAll( map.get( modelAtMouseLocation ) );
					}
				}
				if( listeners != null ) {
					for( Object listener : listeners ) {
						fireEvent( listener, event, modelAtMouseLocation );
					}
				}
			}
		}
	}

	public void addListener( Object listener, MultipleEventPolicy eventPolicy, Visual[] targets ) {
		registerIsFiringMap( listener, targets );
		registerPolicyMap( listener, eventPolicy );
		if( ( targets != null ) && ( targets.length > 0 ) ) {
			for( Visual target : targets ) {
				if( map.get( target ) != null ) {
					map.get( target ).add( listener );
				} else {
					CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<Object>();
					list.add( listener );
					map.put( target, list );
				}
			}
		} else if( listener instanceof MouseClickOnScreenListener ) {
			map.get( empty ).add( listener );
		}
	}
}
