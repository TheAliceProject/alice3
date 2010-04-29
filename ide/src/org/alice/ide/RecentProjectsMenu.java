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
package org.alice.ide;

class MenuItem extends javax.swing.JMenuItem {
	private java.io.File file;
	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			if( file != null ) {
				IDE ide = IDE.getSingleton();
				boolean isLoadDesired;
				if( ide.isProjectUpToDateWithFile() ) {
					isLoadDesired = true;
				} else {
//					edu.cmu.cs.dennisc.zoot.ActionContext checkContext = edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate( ide.getClearToProcedeWithChangedProjectOperation(), e, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
//					isLoadDesired = checkContext.isCommitted();
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ide.getClearToProcedeWithChangedProjectOperation()" );
					isLoadDesired = true;
				}
				if( isLoadDesired ) {
					ide.loadProjectFrom( MenuItem.this.file );
				}
			}
		}
	};

	public MenuItem( int i, java.io.File file ) {
		this.file = file;
		this.setText( i + ") " + this.file.getAbsolutePath() );
		this.addActionListener( this.actionListener );
	}
	//	@Override
	//	public void addNotify() {
	//		this.addActionListener( this.actionListener );
	//		super.addNotify();
	//	}
	//	
	//	@Override
	//	public void removeNotify() {
	//		this.removeActionListener( this.actionListener );
	//		super.removeNotify();
	//	}
}

class RecentProjectsMenu extends javax.swing.JMenu {
//	private edu.cmu.cs.dennisc.preference.event.PreferenceListener< org.alice.ide.preferences.PathsPreference > preferenceListener = new edu.cmu.cs.dennisc.preference.event.PreferenceListener< org.alice.ide.preferences.PathsPreference >() {
//		public void valueChanging( edu.cmu.cs.dennisc.preference.event.PreferenceEvent< org.alice.ide.preferences.PathsPreference > e ) {
//		}
//		public void valueChanged( edu.cmu.cs.dennisc.preference.event.PreferenceEvent< org.alice.ide.preferences.PathsPreference > e ) {
//		}
//	};
	public RecentProjectsMenu() {
		this.setText( "Open Recent Projects" );
		this.addMenuListener( new javax.swing.event.MenuListener() {
			public void menuSelected( javax.swing.event.MenuEvent e ) {
				RecentProjectsMenu.this.handleMenuSelected( e );
			}
			public void menuDeselected( javax.swing.event.MenuEvent e ) {
				RecentProjectsMenu.this.handleMenuDeselected( e );
			}
			public void menuCanceled( javax.swing.event.MenuEvent e ) {
				RecentProjectsMenu.this.handleMenuCanceled( e );
			}
		} );
	}

	private void handleMenuSelected( javax.swing.event.MenuEvent e ) {
		this.removeAll();
		org.alice.ide.preferences.PathsPreference pathsPreference = org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths;
		int i = 1;
		for( String pathname : pathsPreference.getValue() ) {
			java.io.File file = new java.io.File( pathname );
			if( file.exists() ) {
				this.add( new MenuItem( i, file ) );
				i++;
			}
		}
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "menuSelected:", e );
	}
	private void handleMenuDeselected( javax.swing.event.MenuEvent e ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "menuDeselected:", e );
	}
	private void handleMenuCanceled( javax.swing.event.MenuEvent e ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "menuCanceled:", e );
	}
}
