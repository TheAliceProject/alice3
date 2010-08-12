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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZTabbedPane extends javax.swing.JTabbedPane {
	private ActionOperation tabCloseOperation;
//	private ItemSelectionOperation tabSelectionOperation;

	public ZTabbedPane() {
		//this.setTabLayoutPolicy( javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT );
		//this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 90 ) );
		//this.setOpaque( false );
	}

	protected edu.cmu.cs.dennisc.zoot.plaf.TabbedPaneUI createTabbedPaneUI() {
		return new edu.cmu.cs.dennisc.zoot.plaf.TabbedPaneUI( this );
	}
	
	public java.awt.Color getContentAreaColor() {
		return  new java.awt.Color( 63, 63, 81 );
	}
	
	@Override
	public void updateUI() {
		//javax.swing.UIManager.put("TabbedPane.contentAreaColor", this.getContentAreaColor() );
		//javax.swing.UIManager.put("TabbedPane.contentAreaColor", null );
		this.setUI( this.createTabbedPaneUI() );
	}
	public ActionOperation getTabCloseOperation() {
		return this.tabCloseOperation;
	}
	public void setTabCloseOperation( ActionOperation tabCloseOperation ) {
		this.tabCloseOperation = tabCloseOperation;
	}

//	public ItemSelectionOperation getTabSelectionOperation() {
//		return this.tabSelectionOperation;
//	}
//	public void setTabSelectionOperation( ItemSelectionOperation tabSelectionOperation ) {
//		this.tabSelectionOperation = tabSelectionOperation;
//	}
	
	public boolean isCloseButtonDesiredAt( int index ) {
		return this.tabCloseOperation != null;
	}
	public void closeTab( int index, java.awt.event.MouseEvent mouseEvent ) {
		edu.cmu.cs.dennisc.zoot.event.TabEvent tabEvent = new edu.cmu.cs.dennisc.zoot.event.TabEvent( this, index, mouseEvent );
		//todo?
		ZManager.performIfAppropriate( this.tabCloseOperation, tabEvent, ZManager.CANCEL_IS_WORTHWHILE );
	}

	public static void main( String[] args ) {
		class MonthPane extends javax.swing.JLabel {
			MonthPane( String text ) {
				this.setText( text );
				this.setOpaque( true );
				java.awt.Color color;
				if( text.charAt( 0 ) == 'J' ) {
					color = new java.awt.Color( 0xedc484 );
				} else {
					color = new java.awt.Color( 0xb4ccaf );
				}
				this.setBackground( color );
				edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( this, 10.0f );
			}
		}

		//SingleSelectionOperation tabSelectionOperation = null;
		AbstractActionOperation tabCloseOperation = new InconsequentialActionOperation() {
			@Override
			protected void performInternal(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
				java.util.EventObject e = actionContext.getEvent();
				if( e instanceof edu.cmu.cs.dennisc.zoot.event.TabEvent ) {
					edu.cmu.cs.dennisc.zoot.event.TabEvent te = (edu.cmu.cs.dennisc.zoot.event.TabEvent)e;
					te.getTypedSource().remove( te.getIndex() );
				} else {
					actionContext.cancel();
				}
			}
		};
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ZTabbedPane tabbedPane = new ZTabbedPane() {
			@Override
			public boolean isCloseButtonDesiredAt(int index) {
				return index % 2 == 0;
			}
		};
		tabbedPane.setTabCloseOperation( tabCloseOperation );
		String[] tabTitles = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		for( String tabTitle : tabTitles ) {
			tabbedPane.addTab( tabTitle, new MonthPane( tabTitle ) );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "addTab", tabbedPane.getTabCount() );
			break;
		}
		frame.getContentPane().add( tabbedPane );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
