/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package sceneeditor;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;

import edu.cmu.cs.dennisc.ui.DragStyle;
import edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter;
import edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter;

/**
 * @author David Culyba
 */
public class GlobalDragAdapter extends OnscreenLookingGlassDragAdapter implements java.awt.event.MouseWheelListener, java.awt.event.KeyListener {

	private edu.cmu.cs.dennisc.scenegraph.Transformable manipulatedObject = null;
	
	private java.util.Vector< sceneeditor.DragManipulator > currentDragManipulators = new java.util.Vector< sceneeditor.DragManipulator >();
	private sceneeditor.CameraTranslateManipulator cameraTranslator = new sceneeditor.CameraTranslateManipulator();
	
	private InputState currentInputState = new InputState();
	private InputState previousInputState = new InputState(); 
	private double timePrev = Double.NaN;
	
	protected void handleStateChange()
	{
		System.out.println("Current State: \n"+this.currentInputState+"\nPrevious State: \n"+this.previousInputState+"\n");
		this.previousInputState.copyState(this.currentInputState);
	}
	
	protected void update( double timeDelta )
	{
		
	}
	
	@Override
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		super.setOnscreenLookingGlass( onscreenLookingGlass );
		onscreenLookingGlass.getLookingGlassFactory().addAutomaticDisplayListener( new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
			public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
				GlobalDragAdapter.this.handleDisplayed();
			}
		} );
	}

	private void handleDisplayed() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			double timeCurr = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			if( sgCamera != null ) {
				if( Double.isNaN( this.timePrev ) ) {
					this.timePrev = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
				}
				double timeDelta = timeCurr - this.timePrev;
				update( timeDelta );
			}
			this.timePrev = timeCurr;
		}
	}
	
	private int cameraIndex = 0;
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		if( onscreenLookingGlass != null ) {
			if( this.cameraIndex < onscreenLookingGlass.getCameraCount() ) {
				return onscreenLookingGlass.getCameraAt( this.cameraIndex );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraTransformable() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgCamera.getParent();
		} else {
			return null;
		}
	}
	
	@Override
	protected void addListeners( java.awt.Component component ) {
		super.addListeners( component );
		this.getOnscreenLookingGlass().getAWTComponent().addMouseWheelListener( this );
	}
	@Override
	protected void removeListeners( java.awt.Component component ) {
		super.removeListeners( component );
		this.getOnscreenLookingGlass().getAWTComponent().removeMouseWheelListener( this );
	}
	
	
	@Override
	protected void handleMouseDrag(Point current, int deltaSince0,
			int deltaSince02, int xDeltaSincePrevious, int yDeltaSincePrevious,
			DragStyle dragStyle) {

		//this.cameraTranslator.updateManipulator( current, xDeltaSincePrevious, yDeltaSincePrevious );

	}

	@Override
	protected void handleMousePress(Point current, DragStyle dragStyle,
			boolean isOriginalAsOpposedToStyleChange) {
		
//		this.cameraTranslator.setManipulatedTransformable( this.getSGCameraTransformable() );
//		this.cameraTranslator.startManipulator( current );
	}

	@Override
	protected Point handleMouseRelease(Point rvCurrent, DragStyle dragStyle,
			boolean isOriginalAsOpposedToStyleChange) {
//		this.cameraTranslator.endManipulator( rvCurrent );
		return null;
	}
	
	@Override
	public void mousePressed( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		super.mousePressed( e );
		this.handleStateChange();
	}
	
	@Override
	public void mouseReleased( java.awt.event.MouseEvent e) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		super.mouseReleased( e );
		this.handleStateChange();
	}
	
	@Override
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.handleStateChange();
	}
	
	@Override
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		
	}
	
	public void mouseWheelMoved( MouseWheelEvent e ) {
		this.currentInputState.setMouseWheelState( e.getClickCount() );
		this.handleStateChange();
		this.currentInputState.setMouseWheelState( 0 );
	}
	
	@Override
	public void keyPressed( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), true );
		super.keyPressed( e );
		handleStateChange();
		
	}
	
	@Override
	public void keyReleased( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), false );
		super.keyPressed( e );
		handleStateChange();

	}

}
