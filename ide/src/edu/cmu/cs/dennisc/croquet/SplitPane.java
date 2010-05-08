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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class SplitPane extends Component< javax.swing.JSplitPane > {
	protected SplitPane( int orientation ) {
		this( orientation, null, null );
	}
	protected SplitPane( int orientation, Component<?> topOrLeftComponent, Component<?> bottomOrRightComponent ) {
		this.getJComponent().setOrientation( orientation );
		this.setTopOrLeftComponent( topOrLeftComponent );
		this.setBottomOrRightComponent( bottomOrRightComponent );
	}
	@Override
	protected javax.swing.JSplitPane createJComponent() {
		return new javax.swing.JSplitPane();
	}
	
	protected void setTopOrLeftComponent( Component<?> component ) {
		if( component != null ) {
			component.adding();
			this.getJComponent().setTopComponent( component.getJComponent() );
			component.added();
		} else {
			this.getJComponent().setTopComponent( null );
		}
	}
	protected void setBottomOrRightComponent( Component<?> component ) {
		if( component != null ) {
			component.adding();
			this.getJComponent().setBottomComponent( component.getJComponent() );
			component.added();
		} else {
			this.getJComponent().setBottomComponent( null );
		}
	}
	
	public double getResizeWeight() {
		return this.getJComponent().getResizeWeight();
	}
	public void setResizeWeight( double resizeWeight ) {
		this.getJComponent().setResizeWeight( resizeWeight );
	}
	public int getDividerSize() {
		return this.getJComponent().getDividerSize();
	}
	public void setDividerSize( int dividerSize ) {
		this.getJComponent().setDividerSize( dividerSize );
	}
	public int getDividerLocation() {
		return this.getJComponent().getDividerLocation();
	}
	public void setDividerLocation( int dividerLocation ) {
		this.getJComponent().setDividerLocation( dividerLocation );
	}
	public void setDividerProportionalLocation( double proportionalLocation ) {
		this.getJComponent().setDividerLocation( proportionalLocation );
	}
}
