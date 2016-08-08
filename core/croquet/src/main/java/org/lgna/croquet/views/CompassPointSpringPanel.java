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
public class CompassPointSpringPanel extends CornerSpringPanel {
	private AwtComponentView<?> northComponent;
	private AwtComponentView<?> eastComponent;
	private AwtComponentView<?> southComponent;
	private AwtComponentView<?> westComponent;

	public CompassPointSpringPanel() {
		this( null );
	}

	public CompassPointSpringPanel( org.lgna.croquet.Composite composite ) {
		super( composite );
	}

	public AwtComponentView<?> getNorthComponent() {
		return this.northComponent;
	}

	public void setNorthComponent( AwtComponentView<?> northComponent ) {
		if( this.northComponent != northComponent ) {
			if( this.northComponent != null ) {
				this.removeComponent( this.northComponent );
			}
			this.northComponent = northComponent;
			if( this.northComponent != null ) {
				int pad = getPad();
				this.addComponent( this.northComponent, Horizontal.CENTER, 0, Vertical.NORTH, pad );
			}
		}
	}

	public AwtComponentView<?> getEastComponent() {
		return this.eastComponent;
	}

	public void setEastComponent( AwtComponentView<?> eastComponent ) {
		if( this.eastComponent != eastComponent ) {
			if( this.eastComponent != null ) {
				this.removeComponent( this.eastComponent );
			}
			this.eastComponent = eastComponent;
			if( this.eastComponent != null ) {
				int pad = getPad();
				this.addComponent( this.eastComponent, Horizontal.EAST, -pad, Vertical.CENTER, 0 );
			}
		}
	}

	public AwtComponentView<?> getSouthComponent() {
		return this.southComponent;
	}

	public void setSouthComponent( AwtComponentView<?> southComponent ) {
		if( this.southComponent != southComponent ) {
			if( this.southComponent != null ) {
				this.removeComponent( this.southComponent );
			}
			this.southComponent = southComponent;
			if( this.southComponent != null ) {
				int pad = getPad();
				this.addComponent( this.southComponent, Horizontal.CENTER, 0, Vertical.SOUTH, -pad );
			}
		}
	}

	public AwtComponentView<?> getWestComponent() {
		return this.westComponent;
	}

	public void setWestComponent( AwtComponentView<?> westComponent ) {
		if( this.westComponent != westComponent ) {
			if( this.westComponent != null ) {
				this.removeComponent( this.westComponent );
			}
			this.westComponent = westComponent;
			if( this.westComponent != null ) {
				int pad = getPad();
				this.addComponent( this.westComponent, Horizontal.WEST, -pad, Vertical.CENTER, 0 );
			}
		}
	}
}
