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
package org.alice.apis.moveandturn.event;

/**
 * @author Dennis Cosgrove
 */
public class MouseButtonEvent extends edu.cmu.cs.dennisc.pattern.event.Event< java.awt.Component > {
	private java.awt.event.MouseEvent e;
	private org.alice.apis.moveandturn.Scene scene;
	private boolean isPickPerformed;
	private org.alice.apis.moveandturn.Model partAtMouseLocation;
	private org.alice.apis.moveandturn.Model modelAtMouseLocation;
	public MouseButtonEvent( java.awt.event.MouseEvent e, org.alice.apis.moveandturn.Scene scene ) {
		super( e.getComponent() );
		this.e = e;
		this.scene = scene;
		this.isPickPerformed = false;
	}

	private synchronized void pickIfNecessary() {
		if( this.isPickPerformed ) {
			//pass
		} else {
			if( this.scene != null )  {
				org.alice.apis.moveandturn.SceneOwner owner = this.scene.getOwner();
				if( owner != null ) {
					edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = owner.getOnscreenLookingGlass();
					if( lg != null ) {
						edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = lg.pickFrontMost( e.getX(), e.getY(), false );
						if( pickResult != null ) {
							edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
							if( sgVisual != null ) {
								org.alice.apis.moveandturn.Element element = org.alice.apis.moveandturn.Element.getElement( sgVisual );
								if( element instanceof org.alice.apis.moveandturn.Model ) {
									this.partAtMouseLocation = (org.alice.apis.moveandturn.Model)element;
								}
								edu.cmu.cs.dennisc.scenegraph.Component sgComponent = sgVisual;
								while( true ) {
									edu.cmu.cs.dennisc.scenegraph.Composite sgParent = sgComponent.getParent();
									if( sgParent == null ) {
										break;
									}
									if( sgParent == this.scene.getSGComposite() ) {
										org.alice.apis.moveandturn.Element e = org.alice.apis.moveandturn.Element.getElement( sgComponent );
										if( e instanceof org.alice.apis.moveandturn.Model ) {
											this.modelAtMouseLocation = (org.alice.apis.moveandturn.Model)e;
										}
										break;
									}
									sgComponent = sgParent;
								}
							}
						}
					}
				}
			}
			this.isPickPerformed = true;
		}
	}
	public org.alice.apis.moveandturn.Model getPartAtMouseLocation() {
		this.pickIfNecessary();
		return this.partAtMouseLocation;
	}
	public org.alice.apis.moveandturn.Model getModelAtMouseLocation() {
		this.pickIfNecessary();
		return this.modelAtMouseLocation;
	}
}
