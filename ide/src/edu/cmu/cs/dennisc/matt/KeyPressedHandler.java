package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Key;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;

public class KeyPressedHandler extends AbstractEventHandler< Object, KeyEvent > {

//	private final List< Object > list = Collections.newLinkedList();
	HashMap< Object, LinkedList< Object > > map = new HashMap< Object, LinkedList< Object > >();
	Object empty = new Object();
	
	public KeyPressedHandler() {
		map.put( empty, new LinkedList<Object>() );
	}

	private void internalAddListener( Object keyList, MultipleEventPolicy policy, List<Key> validKeys ) {
		if( validKeys == null ) {
			map.get( empty ).add( keyList );
		} else {
			for ( Key k: validKeys ) {
				if ( map.get( k ) == null ) {
					map.put( k , new LinkedList<Object>() );
				}
				map.get( k ).add( keyList );
			}
		}
		registerIsFiringMap( keyList );
		registerPolicyMap( keyList, policy );
//		if( !list.contains( keyList ) ){
//			list.add( keyList );
//		}
	}
	public void addListener( KeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys ) {
		this.internalAddListener( keyList, policy, validKeys );
	}
	public void addListener( ArrowKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys ) {
		this.internalAddListener( keyList, policy, validKeys );
	}
	public void addListener( NumberKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys ) {
		this.internalAddListener( keyList, policy, validKeys );
	}
	
	public void fireAllTargeted( KeyEvent e ) {
		if( shouldFire ){
				Key key = e.getKey();
			if ( map.get(key) != null ) {
				for( Object listener: map.get( key ) ){
					fireEvent( listener, e );
				}
			}
			for( Object listener: map.get( empty ) ){
				fireEvent( listener, e );
			}
			
		}
	}

	@Override
	protected void nameOfFireCall(Object listener, KeyEvent event) {
		if ( listener instanceof ArrowKeyPressListener ) {
			ArrowKeyPressListener arrowListener = ( ArrowKeyPressListener ) listener;
			arrowListener.arrowKeyPressed(new ArrowKeyEvent( event.getJavaEvent() ) );
		} else if ( listener instanceof NumberKeyPressListener ) {
			NumberKeyPressListener numberListener = ( NumberKeyPressListener ) listener;
			numberListener.numberKeyPressed( new NumberKeyEvent( event.getJavaEvent() ) );
		} else if ( listener instanceof KeyPressListener ) {
			KeyPressListener keyListener = ( KeyPressListener ) listener;
			keyListener.keyPressed( event );
		}
	}
}
