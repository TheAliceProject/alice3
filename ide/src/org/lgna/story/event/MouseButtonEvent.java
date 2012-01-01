/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.lgna.story.event;

import org.lgna.story.ImplementationAccessor;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy;

/**
 * @author Dennis Cosgrove
 */
public class MouseButtonEvent extends edu.cmu.cs.dennisc.pattern.event.Event< java.awt.Component > {
	private java.awt.event.MouseEvent e;
	private org.lgna.story.Scene scene;
	private boolean isPickPerformed;
	private org.lgna.story.Model partAtMouseLocation;
	private org.lgna.story.Model modelAtMouseLocation;
	public MouseButtonEvent( java.awt.event.MouseEvent e, org.lgna.story.Scene scene ) {
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
				SceneImp sceneImp = ImplementationAccessor.getImplementation(this.scene);
				ProgramImp programImp = sceneImp.getProgram();
				if( programImp != null ) {
					edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = programImp.getOnscreenLookingGlass();
					if( lg != null ) {
						edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = lg.getPicker().pickFrontMost( e.getX(), e.getY(), PickSubElementPolicy.NOT_REQUIRED );
						if( pickResult != null ) {
							edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
							if( sgVisual != null ) {
								org.lgna.story.Entity element = EntityImp.getInstance( sgVisual );
								if( element instanceof org.lgna.story.Model ) {
									this.partAtMouseLocation = (org.lgna.story.Model)element;
								}
								edu.cmu.cs.dennisc.scenegraph.Component sgComponent = sgVisual;
								while( true ) {
									edu.cmu.cs.dennisc.scenegraph.Composite sgParent = sgComponent.getParent();
									if( sgParent == null ) {
										break;
									}
									if( sgParent == sceneImp.getSgComposite() ) {
										org.lgna.story.Entity e = EntityImp.getInstance( sgComponent );
										if( e instanceof org.lgna.story.Model ) {
											this.modelAtMouseLocation = (org.lgna.story.Model)e;
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
	public org.lgna.story.Model getPartAtMouseLocation() {
		this.pickIfNecessary();
		return this.partAtMouseLocation;
	}
	public org.lgna.story.Model getModelAtMouseLocation() {
		this.pickIfNecessary();
		return this.modelAtMouseLocation;
	}

//	private synchronized void pickIfNecessary() {
//		if( this.isPickPerformed ) {
//			//pass
//		} else {
//			if( this.scene != null )  {
//				SceneImplementation sceneImplementation = ImplementationAccessor.getImplementation(this.scene);
//				org.lookingglassandalice.storytelling.SceneOwner owner = this.scene.getOwner();
//				if( owner != null ) {
//					edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = owner.getOnscreenLookingGlass();
//					if( lg != null ) {
//						edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = lg.pickFrontMost( e.getX(), e.getY(), false );
//						if( pickResult != null ) {
//							edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
//							if( sgVisual != null ) {
//								org.lookingglassandalice.storytelling.Entity element = org.lookingglassandalice.storytelling.Entity.getElement( sgVisual );
//								if( element instanceof org.lookingglassandalice.storytelling.Model ) {
//									this.partAtMouseLocation = (org.lookingglassandalice.storytelling.Model)element;
//								}
//								edu.cmu.cs.dennisc.scenegraph.Component sgComponent = sgVisual;
//								while( true ) {
//									edu.cmu.cs.dennisc.scenegraph.Composite sgParent = sgComponent.getParent();
//									if( sgParent == null ) {
//										break;
//									}
//									if( sgParent == this.scene.getSgComposite() ) {
//										org.lookingglassandalice.storytelling.Entity e = org.lookingglassandalice.storytelling.Entity.getElement( sgComponent );
//										if( e instanceof org.lookingglassandalice.storytelling.Model ) {
//											this.modelAtMouseLocation = (org.lookingglassandalice.storytelling.Model)e;
//										}
//										break;
//									}
//									sgComponent = sgParent;
//								}
//							}
//						}
//					}
//				}
//			}
//			this.isPickPerformed = true;
//		}
//	}
//	public org.lookingglassandalice.storytelling.Model getPartAtMouseLocation() {
//		this.pickIfNecessary();
//		return this.partAtMouseLocation;
//	}
//	public org.lookingglassandalice.storytelling.Model getModelAtMouseLocation() {
//		this.pickIfNecessary();
//		return this.modelAtMouseLocation;
//	}
}
