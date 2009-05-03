/*
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
package org.alice.stageide.modelviewer;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractViewer extends org.alice.apis.moveandturn.Program {
	private org.alice.apis.moveandturn.Scene scene = new org.alice.apis.moveandturn.Scene();
	private org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = new org.alice.apis.moveandturn.SymmetricPerspectiveCamera();
	private org.alice.apis.moveandturn.DirectionalLight sunLight = new org.alice.apis.moveandturn.DirectionalLight();
	@Override
	protected java.awt.Component createSpeedMultiplierControlPanel() {
		return null;
	}
	protected org.alice.apis.moveandturn.SymmetricPerspectiveCamera getCamera() {
		return this.camera;
	}
	@Override
	protected void initialize() {
		this.scene.addComponent( this.camera );
		this.scene.addComponent( this.sunLight );
		this.sunLight.turn( org.alice.apis.moveandturn.TurnDirection.FORWARD, new org.alice.apis.moveandturn.AngleInRevolutions( 0.25 ) );
		this.setScene( this.scene );
	}
	@Override
	protected void run() {
	}
}
/**
 * @author Dennis Cosgrove
 */
public class ModelViewer extends AbstractViewer {
	private org.alice.apis.moveandturn.Model model = null;
	public org.alice.apis.moveandturn.Model getModel() {
		return this.model;
	}
	public void setModel( org.alice.apis.moveandturn.Model model ) {
		if( this.model != null ) {
			this.getScene().removeComponent( this.model );
		}
		this.model = model;
		if( this.model != null ) {
			this.getScene().addComponent( this.model );
		}
	}

}
