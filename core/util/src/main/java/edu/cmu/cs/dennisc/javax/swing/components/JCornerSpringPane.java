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
public class JCornerSpringPane extends JSpringPane {
	private java.awt.Component northWestComponent;
	private java.awt.Component northEastComponent;
	private java.awt.Component southWestComponent;
	private java.awt.Component southEastComponent;

	protected int getPad() {
		return 8;
	}

	public java.awt.Component getNorthWestComponent() {
		return this.northWestComponent;
	}

	public void setNorthWestComponent( java.awt.Component northWestComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.northWestComponent != null ) {
			springLayout.removeLayoutComponent( this.northWestComponent );
			this.remove( this.northWestComponent );
		}
		this.northWestComponent = northWestComponent;
		if( this.northWestComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.northWestComponent, Horizontal.WEST, pad, Vertical.NORTH, pad );
			//springLayout.putConstraint( javax.swing.SpringLayout.NORTH, this.northWestComponent, pad, javax.swing.SpringLayout.NORTH, this );
			//springLayout.putConstraint( javax.swing.SpringLayout.WEST, this.northWestComponent, pad, javax.swing.SpringLayout.WEST, this );
			this.add( this.northWestComponent );
		}
	}

	public java.awt.Component getNorthEastComponent() {
		return this.northEastComponent;
	}

	public void setNorthEastComponent( java.awt.Component northEastComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.northEastComponent != null ) {
			springLayout.removeLayoutComponent( this.northEastComponent );
			this.remove( this.northEastComponent );
		}
		this.northEastComponent = northEastComponent;
		if( this.northEastComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.northEastComponent, Horizontal.EAST, -pad, Vertical.NORTH, pad );
			//			springLayout.putConstraint( javax.swing.SpringLayout.NORTH, this.northEastComponent, pad, javax.swing.SpringLayout.NORTH, this );
			//			springLayout.putConstraint( javax.swing.SpringLayout.EAST, this.northEastComponent, -pad, javax.swing.SpringLayout.EAST, this );
			this.add( this.northEastComponent );
		}
	}

	public java.awt.Component getSouthWestComponent() {
		return this.southWestComponent;
	}

	public void setSouthWestComponent( java.awt.Component southWestComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.southWestComponent != null ) {
			springLayout.removeLayoutComponent( this.southWestComponent );
			this.remove( this.southWestComponent );
		}
		this.southWestComponent = southWestComponent;
		if( this.southWestComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.southWestComponent, Horizontal.WEST, pad, Vertical.SOUTH, -pad );
			//			springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, this.southWestComponent, -pad, javax.swing.SpringLayout.SOUTH, this );
			//			springLayout.putConstraint( javax.swing.SpringLayout.WEST, this.southWestComponent, pad, javax.swing.SpringLayout.WEST, this );
			this.add( this.southWestComponent );
		}
	}

	public java.awt.Component getSouthEastComponent() {
		return this.southEastComponent;
	}

	public void setSouthEastComponent( java.awt.Component southEastComponent ) {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		if( this.southEastComponent != null ) {
			springLayout.removeLayoutComponent( this.southEastComponent );
			this.remove( this.southEastComponent );
		}
		this.southEastComponent = southEastComponent;
		if( this.southEastComponent != null ) {
			int pad = getPad();
			this.putConstraint( this.southEastComponent, Horizontal.EAST, -pad, Vertical.SOUTH, -pad );
			//			springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, this.southEastComponent, -pad, javax.swing.SpringLayout.SOUTH, this );
			//			springLayout.putConstraint( javax.swing.SpringLayout.EAST, this.southEastComponent, -pad, javax.swing.SpringLayout.EAST, this );
			this.add( this.southEastComponent );
		}
	}
}
