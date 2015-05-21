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

package org.lgna.croquet.views;

import org.lgna.croquet.Application;

/**
 * @author Dennis Cosgrove
 */
public final class Dialog extends AbstractWindow<javax.swing.JDialog> {
	public enum DefaultCloseOperation {
		DO_NOTHING( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE ),
		HIDE( javax.swing.WindowConstants.HIDE_ON_CLOSE ),
		DISPOSE( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		//note: no longer necessary
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

	private static class JDialog extends javax.swing.JDialog {
		public JDialog( java.awt.Frame owner, boolean isModal ) {
			super( owner, isModal );
		}

		public JDialog( java.awt.Dialog owner, boolean isModal ) {
			super( owner, isModal );
		}

		public JDialog( boolean isModal ) {
			this( (java.awt.Frame)null, isModal );
		}

		@Override
		public void setDefaultCloseOperation( int operation ) {
			super.setDefaultCloseOperation( operation );
			if( operation != javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( operation );
			}
		}
	}

	private static javax.swing.JDialog createJDialog( ScreenElement owner, boolean isModal ) {
		javax.swing.JDialog rv;
		if( owner != null ) {
			AbstractWindow<?> root = owner.getRoot();
			if( root != null ) {
				java.awt.Window ownerWindow = root.getAwtComponent();
				if( ownerWindow instanceof java.awt.Frame ) {
					rv = new JDialog( (java.awt.Frame)ownerWindow, isModal );
				} else if( ownerWindow instanceof java.awt.Dialog ) {
					rv = new JDialog( (java.awt.Dialog)ownerWindow, isModal );
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
			rv = new JDialog( isModal );
		}
		return rv;
	}

	private DefaultCloseOperation defaultCloseOperation = DefaultCloseOperation.HIDE;
	private final java.awt.event.WindowListener windowListener = new java.awt.event.WindowListener() {
		@Override
		public void windowOpened( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowClosing( java.awt.event.WindowEvent e ) {
			Dialog.this.handleWindowClosing( e );
		}

		@Override
		public void windowClosed( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowActivated( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowDeactivated( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowIconified( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowDeiconified( java.awt.event.WindowEvent e ) {
		}
	};

	public Dialog() {
		this( null );
	}

	public Dialog( ScreenElement owner ) {
		this( owner, true );
	}

	public Dialog( ScreenElement owner, boolean isModal ) {
		super( Dialog.createJDialog( owner, isModal ) );
		//		this.getAwtComponent().setModal( isModal );
		this.getAwtComponent().setDefaultCloseOperation( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE );
		this.addWindowListener( this.windowListener );
	}

	protected boolean isClearedToClose() {
		return true;
	}

	private void handleWindowClosing( java.awt.event.WindowEvent e ) {
		if( ( this.defaultCloseOperation == DefaultCloseOperation.DO_NOTHING ) || ( this.defaultCloseOperation == null ) ) {
			//pass
		} else {
			if( isClearedToClose() ) {
				if( this.defaultCloseOperation == DefaultCloseOperation.HIDE ) {
					this.setVisible( false );
				} else {
					System.exit( 0 );
				}
			}
		}
	}

	@Override
	/* package-private */java.awt.Container getAwtContentPane() {
		return this.getAwtComponent().getContentPane();
	}

	@Override
	/* package-private */javax.swing.JRootPane getJRootPane() {
		return this.getAwtComponent().getRootPane();
	}

	public DefaultCloseOperation getDefaultCloseOperation() {
		return this.defaultCloseOperation;
	}

	public void setDefaultCloseOperation( DefaultCloseOperation defaultCloseOperation ) {
		this.defaultCloseOperation = defaultCloseOperation;
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
				Application.getActiveInstance().getDocumentFrame().pushWindow( this );
			} else {
				assert this == Application.getActiveInstance().getDocumentFrame().popWindow();
			}
			super.setVisible( isVisible );
		}
	}

	@Override
	protected void setJMenuBar( javax.swing.JMenuBar jMenuBar ) {
		this.getAwtComponent().setJMenuBar( jMenuBar );
	}

	public void dispose() {
		this.removeWindowListener( this.windowListener );
		this.getAwtComponent().dispose();
	}
}
