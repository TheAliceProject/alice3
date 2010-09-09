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
public final class Frame extends AbstractWindow<javax.swing.JFrame> {
	/**
	 * @author Dennis Cosgrove
	 */
	public enum DefaultCloseOperation {
		DO_NOTHING( javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE ),
		HIDE( javax.swing.WindowConstants.HIDE_ON_CLOSE ),
		DISPOSE( javax.swing.WindowConstants.DISPOSE_ON_CLOSE ),
		EXIT( javax.swing.WindowConstants.EXIT_ON_CLOSE );
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
	
	public Frame() {
		super( new javax.swing.JFrame() );
		
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

	public void setMenuBar(MenuBar menuBar) {
		this.getAwtComponent().setJMenuBar(menuBar.getAwtComponent());
//		try {
//			java.util.List< javax.swing.KeyStroke > keyStrokesToRemove = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//			javax.swing.JComponent component = this.getAwtComponent().getRootPane();
//			//javax.swing.JComponent component = new javax.swing.JDesktopPane();
//			//javax.swing.JComponent component = this.getAwtComponent().getLayeredPane();
//			
//			//int condition = javax.swing.JComponent.WHEN_FOCUSED;
//			int condition = javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
//			//int condition = javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
//			javax.swing.InputMap inputMap = javax.swing.SwingUtilities.getUIInputMap( component, condition );
//			javax.swing.KeyStroke[] allKeys = inputMap.allKeys();
//			for( javax.swing.KeyStroke keyStroke : allKeys ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( keyStroke, inputMap.get( keyStroke ) );
//				if( keyStroke.getKeyCode() == java.awt.event.KeyEvent.VK_F6 ) {
//					keyStrokesToRemove.add( keyStroke );
//				}
//			}
//			for( javax.swing.KeyStroke keyStroke : keyStrokesToRemove ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "removing:", keyStroke );
//				inputMap.remove( keyStroke );
//			}
//
//			//javax.swing.SwingUtilities.replaceUIInputMap( component, type, inputMap );
//			inputMap = javax.swing.SwingUtilities.getUIInputMap( component, condition );
//			allKeys = inputMap.allKeys();
//			for( javax.swing.KeyStroke keyStroke : allKeys ) {
//				if( keyStroke.getKeyCode() == java.awt.event.KeyEvent.VK_F6 ) {
//					assert false;
//				}
//			}
//		} catch( Exception e ) {
//			e.printStackTrace();
//		}
	}

	public void maximize() {
		this.getAwtComponent().setExtendedState( this.getAwtComponent().getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH );
	}
}
