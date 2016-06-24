/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package edu.cmu.cs.dennisc.javax.swing.components;

/**
 * @author Dennis Cosgrove
 */
public class JCompassPointSpringPane extends JCornerSpringPane {
	private java.awt.Component northComponent;
	private java.awt.Component eastComponent;
	private java.awt.Component southComponent;
	private java.awt.Component westComponent;

	public java.awt.Component getNorthComponent() {
		return this.northComponent;
	}

	public void setNorthComponent( java.awt.Component northComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.northComponent != null ) {
			springLayout.removeLayoutComponent( this.northComponent );
			this.remove( this.northComponent );
		}
		this.northComponent = northComponent;
		if( this.northComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.northComponent, Horizontal.CENTER, 0, Vertical.NORTH, pad );
			this.add( this.northComponent );
		}
	}

	public java.awt.Component getEastComponent() {
		return this.eastComponent;
	}

	public void setEastComponent( java.awt.Component eastComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.eastComponent != null ) {
			springLayout.removeLayoutComponent( this.eastComponent );
			this.remove( this.eastComponent );
		}
		this.eastComponent = eastComponent;
		if( this.eastComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.eastComponent, Horizontal.EAST, -pad, Vertical.CENTER, 0 );
			this.add( this.eastComponent );
		}
	}

	public java.awt.Component getSouthComponent() {
		return this.southComponent;
	}

	public void setSouthComponent( java.awt.Component southComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.southComponent != null ) {
			springLayout.removeLayoutComponent( this.southComponent );
			this.remove( this.southComponent );
		}
		this.southComponent = southComponent;
		if( this.southComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.southComponent, Horizontal.CENTER, 0, Vertical.SOUTH, -pad );
			this.add( this.southComponent );
		}
	}

	public java.awt.Component getWestComponent() {
		return this.westComponent;
	}

	public void setWestComponent( java.awt.Component westComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.westComponent != null ) {
			springLayout.removeLayoutComponent( this.westComponent );
			this.remove( this.westComponent );
		}
		this.westComponent = westComponent;
		if( this.westComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.westComponent, Horizontal.WEST, -pad, Vertical.CENTER, 0 );
			this.add( this.westComponent );
		}
	}
}
