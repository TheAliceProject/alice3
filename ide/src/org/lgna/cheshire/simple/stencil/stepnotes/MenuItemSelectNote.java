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

package org.lgna.cheshire.simple.stencil.stepnotes;

/**
 * @author Dennis Cosgrove
 */
public class MenuItemSelectNote extends PrepNote< org.lgna.croquet.history.MenuItemSelectStep > {
	public MenuItemSelectNote( org.lgna.croquet.history.MenuItemSelectStep step ) {
		super( step );
	}
	
	@Override
	protected void addFeatures( org.lgna.croquet.history.MenuItemSelectStep step ) {
		this.addFeature( new org.lgna.cheshire.simple.stencil.features.MenuHole( 
				new org.lgna.cheshire.simple.stencil.resolvers.ModelFirstComponentResolver( step ), 
				org.lgna.cheshire.simple.Feature.ConnectionPreference.NORTH_SOUTH,
				true,
				true,
				false
		) ) ;
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( org.lgna.croquet.history.event.Event<?> event ) {
		if( event instanceof org.lgna.croquet.history.event.AddStepEvent ) {
			org.lgna.croquet.history.event.AddStepEvent addStepEvent = (org.lgna.croquet.history.event.AddStepEvent)event;
			org.lgna.croquet.history.Step< ? > step = addStepEvent.getStep();
			if( step instanceof org.lgna.croquet.history.MenuItemSelectStep ) {
				org.lgna.croquet.history.MenuItemSelectStep menuItemSelectStep = (org.lgna.croquet.history.MenuItemSelectStep)step;
				if( menuItemSelectStep.getModel() == this.getStep().getModel() ) {
					return true;
				} else {
					System.err.println( menuItemSelectStep.getModel() + " != " + this.getStep().getModel() );
				}
			}
		}
		return false;
//		if( event instanceof org.lgna.croquet.history.event.MenuSelectionChangedEvent ) {
//			org.lgna.croquet.history.event.MenuSelectionChangedEvent menuSelectionChangedEvent = (org.lgna.croquet.history.event.MenuSelectionChangedEvent)event;
//			java.util.List< org.lgna.croquet.Model > models = menuSelectionChangedEvent.getModels();
//			final int N = models.size();
//			if( N > 0 ) {
//				return models.get( N-1 ) == this.getStep().getModel();
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
	}
	@Override
	public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
		return isMouseEventInterceptedInAllCasesEvenPopups( e );
	}
}
