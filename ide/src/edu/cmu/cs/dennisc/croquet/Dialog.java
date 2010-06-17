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
public class Dialog extends AbstractWindow< javax.swing.JDialog > {
	/**
	 * @author Dennis Cosgrove
	 */
	public enum DefaultCloseOperation {
		DO_NOTHING( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE ),
		HIDE( javax.swing.WindowConstants.HIDE_ON_CLOSE ),
		DISPOSE( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		private int internal;
		private DefaultCloseOperation( int internal ) {
			this.internal = internal;
		}
		public static DefaultCloseOperation valueOf( int windowConstant ) {
			for( DefaultCloseOperation defaultCloseOperation : DefaultCloseOperation.values() ) {
				if( defaultCloseOperation.internal == windowConstant ) {
					return defaultCloseOperation;
				}
			}
			return null;
		}
	}

	private static javax.swing.JDialog createJDialog( Component<?> owner ) {
		javax.swing.JDialog rv;
		if( owner != null ) {
			AbstractWindow<?> root = owner.getRoot();
			if( root != null ) {
				java.awt.Window ownerWindow = root.getAwtComponent();
				if( ownerWindow instanceof java.awt.Frame ) {
					rv = new javax.swing.JDialog( (java.awt.Frame)ownerWindow );
				} else 	if( ownerWindow instanceof java.awt.Dialog ) {
					rv = new javax.swing.JDialog( (java.awt.Dialog)ownerWindow );
				} else {
					rv = null;
				}
			} else {
				rv = null;
			}
		} else {
			rv = null;
		}
		if( rv != null ) {
			//pass
		} else {
			rv = new javax.swing.JDialog();
		}
		return rv;
	}
	public Dialog() {
		this( null );
	}
	public Dialog( Component<?> owner ) {
		this( owner, true );
	}
	public Dialog( Component<?> owner, boolean isModal ) {
		super( Dialog.createJDialog( owner ) );
		this.getAwtComponent().setModal( isModal );
	}
	@Override
	protected javax.swing.JRootPane getRootPane() {
		return this.getAwtComponent().getRootPane();
	}
	
	public DefaultCloseOperation getDefaultCloseOperation() {
		return DefaultCloseOperation.valueOf( this.getAwtComponent().getDefaultCloseOperation() );
	}
	public void setDefaultCloseOperation( DefaultCloseOperation defaultCloseOperation ) {
		this.getAwtComponent().setDefaultCloseOperation( defaultCloseOperation.internal );
	}
	
	public String getTitle() {
		return this.getAwtComponent().getTitle();
	}
	public void setTitle( String title ) {
		this.getAwtComponent().setTitle( title );
	}
	
	@Override
	public void setVisible( boolean isVisible ) {
		if( isVisible != this.isVisible() ) {
			if( isVisible ) {
				Application.getSingleton().pushWindow( this );
			} else {
				assert this == Application.getSingleton().popWindow();
			}
			super.setVisible( isVisible );
		}
	}
}
