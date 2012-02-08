package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Visual;
import org.lgna.story.event.AbstractKeyPressListener;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.CollisionListener;
import org.lgna.story.event.ComesIntoViewEventListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.LeavesViewEventListener;
import org.lgna.story.event.MouseClickListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.event.OcclusionEventListener;
import org.lgna.story.event.ProximityEventListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.TransformationListener;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;

public class EventManager {

	private final MouseClickedHandler mouseHandler = new MouseClickedHandler();
	
	private final TransformationHandler transHandler = new TransformationHandler();
	private final OcclusionHandler occlusionHandler = new OcclusionHandler();
	private final ViewEventHandler viewHandler = new ViewEventHandler();
	private final CollisionHandler collisionHandler = new CollisionHandler();
	private final ProximityEventHandler proxyHandler = new ProximityEventHandler();
	private final KeyPressedHandler keyHandler = new KeyPressedHandler();
	private final TimerEventHandler timer = new TimerEventHandler();
	private final AbstractEventHandler[] handlers = new AbstractEventHandler[] { mouseHandler, transHandler, collisionHandler, proxyHandler, keyHandler };

	private final edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			EventManager.this.mouseHandler.handleMouseQuoteClickedUnquote( e, quoteClickCountUnquote, EventManager.this.scene.getAbstraction() );
		}
	};
	private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
		public void keyPressed( java.awt.event.KeyEvent e ) {
			org.lgna.story.event.KeyEvent event = new org.lgna.story.event.KeyEvent( e );
			EventManager.this.handleKeyPressed( event );
		}
		public void keyReleased( java.awt.event.KeyEvent e ) {
		}
		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};

	//	private final java.util.List< org.lgna.story.event.MouseButtonListener > mouseButtonListeners = Collections.newCopyOnWriteArrayList();
	//	private final java.util.List< org.lgna.story.event.KeyListener > keyListeners = Collections.newCopyOnWriteArrayList();

	private final SceneImp scene;

	public EventManager( SceneImp scene ) {
		this.scene = scene;
	}

	public void removeMouseButtonListener( MouseClickListener mouseButtonListener ) {
		throw new RuntimeException( "todo" );
		//		this.mouse.removeListener(mouseButtonListener);
		//		this.mouseButtonListeners.remove( mouseButtonListener );
	}
	public void removeKeyListener( KeyPressListener keyListener ) {
		throw new RuntimeException( "todo" );
		//		this.mouse.removeListener(keyListener);
		//		this.keyListeners.remove( keyListener );
	}

	protected void handleKeyPressed( org.lgna.story.event.KeyEvent event ) {
		keyHandler.fireAllTargeted( event );
		//		for(KeyListener listener: keyListeners){
		//			listener.keyPressed(event);
		//		}
	}

	public void addListenersTo( OnscreenLookingGlass onscreenLookingGlass ) {
		java.awt.Component component = onscreenLookingGlass.getAWTComponent();
		component.addMouseListener( this.mouseAdapter );
		component.addMouseMotionListener( this.mouseAdapter );
		component.addKeyListener( this.keyAdapter );
	}
	public void removeListenersFrom( OnscreenLookingGlass onscreenLookingGlass ) {
		java.awt.Component component = onscreenLookingGlass.getAWTComponent();
		component.removeMouseListener( this.mouseAdapter );
		component.removeMouseMotionListener( this.mouseAdapter );
		component.removeKeyListener( this.keyAdapter );
	}

	//	public void mouseButtonClicked(MouseButtonEvent e) {
	//		mouse.fireAllTargeted(e.getModelAtMouseLocation());
	//	}
	//
	//	public void keyPressed(KeyEvent e) {
	//		key.fireAllTargeted(e);
	//	}

	private AbstractEventHandler[] getEventHandlers() {
		return handlers;
	}

	public void silenceAllListeners() {
		for( AbstractEventHandler handler : this.getEventHandlers() ) {
			handler.silenceListeners();
		}
	}

	public void restoreAllListeners() {
		for( AbstractEventHandler handler : this.getEventHandlers() ) {
			handler.restoreListeners();
		}
	}
	public void addCollisionListener( CollisionListener collisionListener, List< Entity > groupOne, List< Entity > groupTwo ) {
		collisionHandler.addCollisionListener( collisionListener, groupOne, groupTwo );
	}
	public void addProximityEventListener( ProximityEventListener proximityEventListener, List< Entity > groupOne, List< Entity > groupTwo, Double dist ) {
		proxyHandler.addProximityEventListener( proximityEventListener, groupOne, groupTwo, dist );
	}

	public void addTimerEventListener( TimeListener timerEventListener, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( timerEventListener, frequency, policy );
	}

	public void addKeyListener( AbstractKeyPressListener keyListener, MultipleEventPolicy eventPolicy ) {
		this.keyHandler.addListener( keyListener, eventPolicy, null );
	}
	public void addNumberKeyListener( NumberKeyPressListener keyPressListener, MultipleEventPolicy policy ) {
		keyHandler.addListener( keyPressListener, policy, NumberKeyEvent.NUMBERS );
	}
	public void addArrowKeyListener( ArrowKeyPressListener keyPressListener, MultipleEventPolicy policy ) {
		keyHandler.addListener( keyPressListener, policy, ArrowKeyEvent.ARROWS );
	}

	public void addMouseClickOnScreenListener( MouseClickOnScreenListener listener, MultipleEventPolicy policy ) {
		mouseHandler.addListener( listener, policy, null );
	}
	public void addMouseClickOnObjectListener( MouseClickOnObjectListener listener, MultipleEventPolicy policy, Visual[] targets ) {
		this.mouseHandler.addListener( listener, policy, targets );
	}
	public void addMouseButtonListener( MouseClickListener listener, MultipleEventPolicy policy, Visual[] targets ) {
		this.mouseHandler.addListener( listener, policy, targets );
	}

	public void addTransformationListener( TransformationListener transformationlistener, Entity[] shouldListenTo) {
		this.transHandler.addTransformationListener( transformationlistener, shouldListenTo );
	}

	public void addOcclusionEventListener( OcclusionEventListener occlusionEventListener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo) {
		this.occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	}

	public void addComesIntoViewEventListener( ComesIntoViewEventListener listener, Entity[] entities ) {
		this.viewHandler.addViewEventListener( listener, entities );
	}
	public void addLeavesViewEventListener(LeavesViewEventListener listener, Entity[] entities) {
		this.viewHandler.addViewEventListener( listener, entities );
	}

}
