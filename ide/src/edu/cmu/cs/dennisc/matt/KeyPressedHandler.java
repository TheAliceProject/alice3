package edu.cmu.cs.dennisc.matt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lgna.common.ComponentThread;
import org.lgna.story.HeldKeyPolicy;
import org.lgna.story.Key;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Collections;

public class KeyPressedHandler extends AbstractEventHandler<Object,KeyEvent> {

	HashMap<Object,LinkedList<Object>> map = new HashMap<Object,LinkedList<Object>>();
	Object empty = new Object();
	private HashMap<Object,HeldKeyPolicy> heldKeyMap = Collections.newHashMap();
	private HashMap<Object,HashMap<Key,Boolean>> firePolicyMap = Collections.newHashMap();
	private long sleepTime = 500;

	public KeyPressedHandler( SceneImp scene ) {
		super(scene);
		map.put( empty, new LinkedList<Object>() );
	}

	private void internalAddListener( Object keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		heldKeyMap.put( keyList, heldKeyPolicy );
		firePolicyMap.put( keyList, new HashMap<Key,Boolean>() );
		if( validKeys == null ) {
			map.get( empty ).add( keyList );
		} else {
			for( Key k : validKeys ) {
				if( map.get( k ) == null ) {
					map.put( k, new LinkedList<Object>() );
				}
				map.get( k ).add( keyList );
			}
		}
		registerIsFiringMap( keyList );
		registerPolicyMap( keyList, policy );
	}
	public void addListener( KeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		this.internalAddListener( keyList, policy, validKeys, heldKeyPolicy );
	}
	public void addListener( ArrowKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		this.internalAddListener( keyList, policy, validKeys, heldKeyPolicy );
	}
	public void addListener( NumberKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		this.internalAddListener( keyList, policy, validKeys, heldKeyPolicy );
	}

	public void fireAllTargeted( KeyEvent e ) {
		if( shouldFire ) {
			Key key = e.getKey();
			if( map.get( key ) != null ) {
				for( Object listener : map.get( key ) ) {
					fireEvent( listener, e );
				}
			}
			for( Object listener : map.get( empty ) ) {
				fireEvent( listener, e );
			}
		}
	}

	@Override
	protected void nameOfFireCall( Object listener, KeyEvent event ) {
		if( listener instanceof ArrowKeyPressListener ) {
			ArrowKeyPressListener arrowListener = (ArrowKeyPressListener)listener;
			arrowListener.arrowKeyPressed( new ArrowKeyEvent( event ) );
		} else if( listener instanceof NumberKeyPressListener ) {
			NumberKeyPressListener numberListener = (NumberKeyPressListener)listener;
			numberListener.numberKeyPressed( new NumberKeyEvent( event ) );
		} else if( listener instanceof KeyPressListener ) {
			KeyPressListener keyListener = (KeyPressListener)listener;
			keyListener.keyPressed( event );
		}
	}

	public void handleKeyPress( final KeyEvent event ) {
		if( shouldFire ) {
			final Key key = event.getKey();
			if( map.get( key ) != null ) {
				for( final Object listener : map.get( key ) ) {
					if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_PRESS ) {
						if( firePolicyMap.get( listener ).get( key ) == null || !firePolicyMap.get( listener ).get( key ) ) {
							firePolicyMap.get( listener ).put( key, true );
							fireEvent( listener, event );
						}
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE ) {
						//pass
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_MULTIPLE ) {
						if( firePolicyMap.get( listener ).get( key ) == null || !firePolicyMap.get( listener ).get( key ) ) {
							firePolicyMap.get( listener ).put( key, true );
							final ComponentThread thread = new ComponentThread( new Runnable() {
								public void run() {
									while( firePolicyMap.get( listener ).get( key ) ) {
										fireEvent( listener, event );
										try {
											ComponentThread.sleep( sleepTime );
										} catch( InterruptedException e ) {
											e.printStackTrace();
										}
									}
								}
							}, "keyPressedThread" );
							thread.start();
						}
					}
				}
			} else {
				for( final Object listener : map.get( empty ) ) {
					if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_PRESS ) {
						if( firePolicyMap.get( listener ).get( key ) == null || !firePolicyMap.get( listener ).get( key ) ) {
							System.out.println( "FIRE_ONCE_ON_PRESS" );
							firePolicyMap.get( listener ).put( key, true );
							fireEvent( listener, event );
						}
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE ) {
						//pass
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_MULTIPLE ) {
						if( firePolicyMap.get( listener ).get( key ) == null || !firePolicyMap.get( listener ).get( key ) ) {
							firePolicyMap.get( listener ).put( key, true );
							final ComponentThread thread = new ComponentThread( new Runnable() {
								public void run() {
									while( firePolicyMap.get( listener ).get( key ) ) {
										fireEvent( listener, event );
										try {
											ComponentThread.sleep( sleepTime );
										} catch( InterruptedException e ) {
											e.printStackTrace();
										}
									}
								}
							}, "keyPressedThread" );
							thread.start();
						}
					}
				}
			}
		}
	}
	public void handleKeyRelease( KeyEvent event ) {
		if( shouldFire ) {
			Key key = event.getKey();
			if( map.get( key ) != null ) {
				for( Object listener : map.get( key ) ) {
					if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_PRESS ) {
						if( firePolicyMap.get( listener ).get( key ) ) {
							firePolicyMap.get( listener ).put( key, false );
						}
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE ) {
						fireEvent( listener, event );
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_MULTIPLE ) {
						firePolicyMap.get( listener ).put( key, false );
					}
				}
			} else {
				for( Object listener : map.get( empty ) ) {
					if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_PRESS ) {
						if( firePolicyMap.get( listener ).get( key ) ) {
							firePolicyMap.get( listener ).put( key, false );
						}
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE ) {
						fireEvent( listener, event );
					} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_MULTIPLE ) {
						firePolicyMap.get( listener ).put( key, false );
					}
				}
			}
		}
	}
}
