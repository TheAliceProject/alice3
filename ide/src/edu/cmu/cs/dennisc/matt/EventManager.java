package edu.cmu.cs.dennisc.matt;

import java.util.List;

import org.lgna.story.Model;
import org.lgna.story.event.CollisionListener;
import org.lgna.story.event.EventPolicy;
import org.lgna.story.event.KeyListener;
import org.lgna.story.event.MouseButtonListener;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;

public class EventManager {
	
	private final MouseClickedHandler mouseHandler = new MouseClickedHandler();
	private final TransformationChangedHandler transHandler = new TransformationChangedHandler();
	private final KeyPressedHandler keyHandler = new KeyPressedHandler();
	
	private final edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			EventManager.this.mouseHandler.handleMouseQuoteClickedUnquote( e, quoteClickCountUnquote, EventManager.this.scene.getAbstraction() );
		}
	};
	private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
		public void keyPressed(java.awt.event.KeyEvent e) {
			org.lgna.story.event.KeyEvent event = new org.lgna.story.event.KeyEvent(e);
			EventManager.this.handleKeyPressed( event );
		}
		public void keyReleased(java.awt.event.KeyEvent e) {
		}
		public void keyTyped(java.awt.event.KeyEvent e) {
		}
	};

//	private final java.util.List< org.lgna.story.event.MouseButtonListener > mouseButtonListeners = Collections.newCopyOnWriteArrayList();
//	private final java.util.List< org.lgna.story.event.KeyListener > keyListeners = Collections.newCopyOnWriteArrayList();

	
	private final SceneImp scene;
	public EventManager( SceneImp scene ) {
		this.scene = scene;
	}
	
	public void addMouseButtonListener(MouseButtonListener mouseButtonListener, EventPolicy eventPolicy, List<Model> targets){
		this.mouseHandler.addListener(mouseButtonListener, eventPolicy, targets);
//		this.mouseButtonListeners.add( mouseButtonListener);
	}
	public void removeMouseButtonListener(MouseButtonListener mouseButtonListener){
		throw new RuntimeException( "todo" );
//		this.mouse.removeListener(mouseButtonListener);
//		this.mouseButtonListeners.remove( mouseButtonListener );
	}
	public void addKeyListener(KeyListener keyListener, EventPolicy eventPolicy){
		this.keyHandler.addListener(keyListener, eventPolicy);
//		this.keyListeners.add( keyListener);
	}
	public void removeKeyListener(KeyListener keyListener){
		throw new RuntimeException( "todo" );
//		this.mouse.removeListener(keyListener);
//		this.keyListeners.remove( keyListener );
	}

	protected void handleKeyPressed(org.lgna.story.event.KeyEvent event) {
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
		return new AbstractEventHandler[] { mouseHandler, transHandler, keyHandler };
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
	public void addCollisionListener(CollisionListener collisionListener,List<Model> groupOne, List<Model> groupTwo) {
		transHandler.addCollisionListener(collisionListener, groupOne, groupTwo);
	}

}
