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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class SplitPane extends View< javax.swing.JSplitPane, org.lgna.croquet.SplitComposite > {
	protected SplitPane( org.lgna.croquet.SplitComposite splitComposite, int orientation ) {
		this( splitComposite, orientation, splitComposite != null ? splitComposite.getLeadingComposite().getView() : null, splitComposite != null ? splitComposite.getTrailingComposite().getView() : null );
	}
	protected SplitPane( org.lgna.croquet.SplitComposite splitComposite, int orientation, Component<?> topOrLeftComponent, Component<?> bottomOrRightComponent ) {
		super( splitComposite );
		this.getAwtComponent().setOrientation( orientation );
		this.setTopOrLeftComponent( topOrLeftComponent );
		this.setBottomOrRightComponent( bottomOrRightComponent );
	}
	@Override
	protected javax.swing.JSplitPane createAwtComponent() {
		return new javax.swing.JSplitPane();
//			@Override
//			public void setDividerLocation(double proportionalLocation) {
//				proportionalLocation = 1.0 - proportionalLocation;
//				super.setDividerLocation(proportionalLocation);
//				
//			}
//			@Override
//			public void setDividerLocation(int location) {
////				location = -location;
//				super.setDividerLocation(location);
//			}
//			@Override
//			public void setComponentOrientation(java.awt.ComponentOrientation o) {
//				super.setComponentOrientation(o);
//				
//			}
//		};
	}
	
	protected void setTopOrLeftComponent( Component<?> component ) {
		if( component != null ) {
			this.getAwtComponent().setLeftComponent( component.getAwtComponent() );
		} else {
			this.getAwtComponent().setLeftComponent( null );
		}
	}
	protected void setBottomOrRightComponent( Component<?> component ) {
		if( component != null ) {
			this.getAwtComponent().setRightComponent( component.getAwtComponent() );
		} else {
			this.getAwtComponent().setRightComponent( null );
		}
	}
	
	public double getResizeWeight() {
		return this.getAwtComponent().getResizeWeight();
	}
	public void setResizeWeight( double resizeWeight ) {
		this.getAwtComponent().setResizeWeight( resizeWeight );
	}
	public int getDividerSize() {
		return this.getAwtComponent().getDividerSize();
	}
	public void setDividerSize( int dividerSize ) {
		this.getAwtComponent().setDividerSize( dividerSize );
	}
	public int getDividerLocation() {
		return this.getAwtComponent().getDividerLocation();
	}
	public void setDividerLocation( int dividerLocation ) {
		this.getAwtComponent().setDividerLocation( dividerLocation );
	}
	public void setDividerProportionalLocation( double proportionalLocation ) {
		this.getAwtComponent().setDividerLocation( proportionalLocation );
	}
}
