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
package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class CornerSpringPanel extends SpringPanel {
	private AwtComponentView<?> northWestComponent;
	private AwtComponentView<?> northEastComponent;
	private AwtComponentView<?> southWestComponent;
	private AwtComponentView<?> southEastComponent;

	public CornerSpringPanel() {
		this( null );
	}

	public CornerSpringPanel( org.lgna.croquet.Composite composite ) {
		super( composite );
	}

	protected int getPad() {
		return 8;
	}

	public AwtComponentView<?> getNorthWestComponent() {
		return this.northWestComponent;
	}

	public void setNorthWestComponent( AwtComponentView<?> northWestComponent ) {
		if( this.northWestComponent != northWestComponent ) {
			if( this.northWestComponent != null ) {
				this.removeComponent( this.northWestComponent );
			}
			this.northWestComponent = northWestComponent;
			if( this.northWestComponent != null ) {
				int pad = getPad();
				this.addComponent( this.northWestComponent, Horizontal.WEST, pad, Vertical.NORTH, pad );
			}
		}
	}

	public AwtComponentView<?> getNorthEastComponent() {
		return this.northEastComponent;
	}

	public void setNorthEastComponent( AwtComponentView<?> northEastComponent ) {
		if( this.northEastComponent != northEastComponent ) {
			if( this.northEastComponent != null ) {
				this.removeComponent( this.northEastComponent );
			}
			this.northEastComponent = northEastComponent;
			if( this.northEastComponent != null ) {
				int pad = getPad();
				this.addComponent( this.northEastComponent, Horizontal.EAST, -pad, Vertical.NORTH, pad );
			}
		}
	}

	public AwtComponentView<?> getSouthWestComponent() {
		return this.southWestComponent;
	}

	public void setSouthWestComponent( AwtComponentView<?> southWestComponent ) {
		if( this.southWestComponent != southWestComponent ) {
			if( this.southWestComponent != null ) {
				this.removeComponent( this.southWestComponent );
			}
			this.southWestComponent = southWestComponent;
			if( this.southWestComponent != null ) {
				int pad = getPad();
				this.addComponent( this.southWestComponent, Horizontal.WEST, pad, Vertical.SOUTH, -pad );
			}
		}
	}

	public AwtComponentView<?> getSouthEastComponent() {
		return this.southEastComponent;
	}

	public void setSouthEastComponent( AwtComponentView<?> southEastComponent ) {
		if( this.southEastComponent != southEastComponent ) {
			if( this.southEastComponent != null ) {
				this.removeComponent( this.southEastComponent );
			}
			this.southEastComponent = southEastComponent;
			if( this.southEastComponent != null ) {
				int pad = getPad();
				this.addComponent( this.southEastComponent, Horizontal.EAST, -pad, Vertical.SOUTH, -pad );
			}
		}
	}
}
