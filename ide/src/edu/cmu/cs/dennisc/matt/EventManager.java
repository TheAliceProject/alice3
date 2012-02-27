package edu.cmu.cs.dennisc.matt;

import java.util.ArrayList;
import java.util.List;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.GlobalDragAdapter;
import org.lgna.story.Entity;
import org.lgna.story.MovableTurnable;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Visual;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.EndOcclusionListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ExitViewListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.MoveWithArrows;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.event.StartOcclusionListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

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

	private final TimerContingencyManager contingent = new TimerContingencyManager( timer );

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

	private GlobalDragAdapter dragAdapter;

	public EventManager( SceneImp scene ) {
		this.scene = scene;
		scene.addSceneActivationListener( timer );
		contingent.setScene( scene );
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
	public void addCollisionListener( Object collisionListener, List<Entity> groupOne, List<Entity> groupTwo ) {
		collisionHandler.addCollisionListener( collisionListener, groupOne, groupTwo );
	}
	public void addProximityEventListener( Object proximityEventListener, List<Entity> groupOne, List<Entity> groupTwo, Double dist ) {
		proxyHandler.addProximityEventListener( proximityEventListener, groupOne, groupTwo, dist );
	}

	public void addTimerEventListener( TimeListener timerEventListener, Long frequency, MultipleEventPolicy policy ) {
		timer.addListener( timerEventListener, frequency, policy );
	}

	public void addKeyListener( KeyPressListener keyListener, MultipleEventPolicy eventPolicy ) {
		this.keyHandler.addListener( keyListener, eventPolicy, null );
	}
	public void addNumberKeyListener( NumberKeyPressListener keyPressListener, MultipleEventPolicy policy ) {
		keyHandler.addListener( keyPressListener, policy, NumberKeyEvent.NUMBERS );
	}
	public void addArrowKeyListener( ArrowKeyPressListener keyPressListener, MultipleEventPolicy policy ) {
		keyHandler.addListener( keyPressListener, policy, ArrowKeyEvent.ARROWS );
	}
	public void moveWithArrows( MovableTurnable entity ) {
		this.keyHandler.addListener( new MoveWithArrows( entity ), MultipleEventPolicy.COMBINE, ArrowKeyEvent.ARROWS );
	}

	public void addMouseClickOnScreenListener( MouseClickOnScreenListener listener, MultipleEventPolicy policy ) {
		mouseHandler.addListener( listener, policy, null );
	}
	public void addMouseClickOnObjectListener( MouseClickOnObjectListener listener, MultipleEventPolicy policy, Visual[] targets ) {
		this.mouseHandler.addListener( listener, policy, targets );
	}
	public void addMouseButtonListener( Object listener, MultipleEventPolicy policy, Visual[] targets ) {
		this.mouseHandler.addListener( listener, policy, targets );
	}

	public void addTransformationListener( PointOfViewChangeListener transformationlistener, Entity[] shouldListenTo ) {
		this.transHandler.addTransformationListener( transformationlistener, shouldListenTo );
	}

	//	public void addOcclusionEventListener( OcclusionListener occlusionEventListener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo) {
	//		this.occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	//	}

	public void addComesIntoViewEventListener( ViewEnterListener listener, Entity[] entities ) {
		this.viewHandler.addViewEventListener( listener, entities );
	}
	public void addLeavesViewEventListener( ExitViewListener listener, Entity[] entities ) {
		this.viewHandler.addViewEventListener( listener, entities );
	}

	public void addDragAdapter() {
		if( this.dragAdapter != null ) {
			//pass
		} else {
			//			this.dragAdapter = new org.alice.interact.RuntimeDragAdapter();
			this.dragAdapter = new org.alice.interact.GlobalDragAdapter();
			OnscreenLookingGlass lookingGlass = this.scene.getProgram().getOnscreenLookingGlass();
			SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera)scene.findFirstCamera().getSgCamera();
			//			for( int i = 0; i < lookingGlass.getCameraCount(); i++ ) {
			//				System.out.println("hi");
			//				if( lookingGlass.getCameraAt( i ) instanceof SymmetricPerspectiveCamera ) 
			//				{
			//					camera = (SymmetricPerspectiveCamera)lookingGlass.getCameraAt( i );
			//					break;
			//				}
			//			}
			this.dragAdapter.setOnscreenLookingGlass( lookingGlass );
			this.dragAdapter.addCameraView( CameraView.MAIN, camera, null );
			this.dragAdapter.makeCameraActive( camera );
			this.dragAdapter.setAnimator( this.scene.getProgram().getAnimator() );
			//			for( Transformable transformable : this.scene.getComponents() ) {
			//				this.putBonusDataFor( transformable );
			//			}
		}
	}

	public void addWhileCollisionListener( WhileCollisionListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Long frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, groupOne, groupTwo, frequency, policy );
	}
	public void addWhileProximityListener( WhileProximityListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Double dist, Long frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, groupOne, groupTwo, dist, frequency, policy );
	}
	public void addWhileOcclusionListener( WhileOcclusionListener listener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo, Long frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, groupOne, groupTwo, frequency, policy );
	}
	public void addWhileInViewListener( WhileInViewListener listener, ArrayList<Entity> group, Long frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, group, frequency, policy );
	}

	public void addOcclusionEventListener( StartOcclusionListener occlusionEventListener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo ) {
		occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	}

	public void addOcclusionEventListener( EndOcclusionListener occlusionEventListener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo ) {
		occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	}
}
