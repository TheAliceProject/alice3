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

package org.lgna.croquet.triggers;

/**
 * @author Dennis Cosgrove
 */
public abstract class EventObjectTrigger< E extends java.util.EventObject > extends Trigger {
	private final transient org.lgna.croquet.components.ViewController< ?, ? > viewController;
	private final transient E event;
	public EventObjectTrigger( Origin origin, org.lgna.croquet.components.ViewController< ?, ? > viewController, E event ) {
		super( origin );
		this.viewController = viewController;
		this.event = event;
	}
	public EventObjectTrigger( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.viewController = null;
		this.event = null;
	}
	public E getEvent() {
		return this.event;
	}
	protected abstract java.awt.Point getPoint();
	@Override
	public org.lgna.croquet.components.ViewController< ?, ? > getViewController() {
		if( this.viewController != null ) {
			return this.viewController;
		} else {
			Object source = this.event != null ? this.event.getSource() : null;
			if( source instanceof java.awt.Component ) {
				java.awt.Component awtComponent = (java.awt.Component)source;
				org.lgna.croquet.components.Component< ? > component = org.lgna.croquet.components.Component.lookup( awtComponent );
				if( component instanceof org.lgna.croquet.components.ViewController ) {
					return (org.lgna.croquet.components.ViewController< ?, ? >)component;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}
	protected java.awt.Component getComponent() {
//		if( this.viewController != null ) {
//			return this.viewController.getAwtComponent();
//		} else {
			Object source = this.event.getSource();
			if( source instanceof java.awt.Component ) {
				return (java.awt.Component)source;
			} else {
				return null;
			}
//		}
	}
	@Override
	public void showPopupMenu( org.lgna.croquet.components.PopupMenu popupMenu ) {
		java.awt.Point pt = this.getPoint();
		java.awt.Component invoker = this.getComponent();
		edu.cmu.cs.dennisc.javax.swing.PopupMenuUtilities.showModal( popupMenu.getAwtComponent(), invoker, pt );
	}
}
