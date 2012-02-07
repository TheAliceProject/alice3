package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.Key;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.AbstractKeyPressListener;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;

public class KeyPressedHandler extends AbstractEventHandler< AbstractKeyPressListener, KeyEvent > {

//	private final List< AbstractKeyPressListener > list = Collections.newLinkedList();
	HashMap< Object, LinkedList< AbstractKeyPressListener > > map = new HashMap< Object, LinkedList< AbstractKeyPressListener > >();
	Object empty = new Object();
	
	public KeyPressedHandler() {
		map.put( empty, new LinkedList<AbstractKeyPressListener>() );
	}

	public void addListener( AbstractKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys ) {
		if( validKeys == null ) {
			map.get( empty ).add( keyList );
		} else {
			for ( Key k: validKeys ) {
				if ( map.get( k ) == null ) {
					map.put( k , new LinkedList<AbstractKeyPressListener>() );
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

	public void fireAllTargeted( KeyEvent e ) {
		if( shouldFire ){
				Key key = e.getKey();
			if ( map.get(key) != null ) {
				for( AbstractKeyPressListener listener: map.get( key ) ){
					fireEvent( listener, e );
				}
			}
			for( AbstractKeyPressListener listener: map.get( empty ) ){
				fireEvent( listener, e );
			}
			
		}
	}

	@Override
	protected void nameOfFireCall(AbstractKeyPressListener listener, KeyEvent event) {
		if ( listener instanceof ArrowKeyPressListener ) {
			ArrowKeyPressListener arrowListener = ( ArrowKeyPressListener ) listener;
			arrowListener.keyPressed(new ArrowKeyEvent( event ) );
		} else if ( listener instanceof NumberKeyPressListener ) {
			NumberKeyPressListener numberListener = ( NumberKeyPressListener ) listener;
			numberListener.keyPressed( new NumberKeyEvent( event ) );
		} else if ( listener instanceof KeyPressListener ) {
			KeyPressListener keyListener = ( KeyPressListener ) listener;
			keyListener.keyPressed( event );
		}
	}
}
