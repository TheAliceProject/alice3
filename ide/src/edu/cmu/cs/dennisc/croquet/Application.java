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
public abstract class Application {
	public static interface LocaleObserver {
		public void localeChanging( java.util.Locale previousLocale, java.util.Locale nextLocale );
		public void localeChanged( java.util.Locale previousLocale, java.util.Locale nextLocale );
	}

	//	private static class RootContext extends CompositeContext {
//		public RootContext() {
//			super( null, null, null, null );
//		}
//	};
//	private RootContext rootContext = new RootContext();
	private static Application singleton;
	public static Application getSingleton() {
		return singleton;
	}

	private Context rootContext = new Context( null );
	private java.util.Map<java.util.UUID, Operation> mapUUIDToOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	

	public Application() {
		assert Application.singleton == null;
		Application.singleton = this;
		
		rootContext.addCommitObserver( new Context.CommitObserver() {
			public void committing( Edit edit ) {
			}
			public void committed( Edit edit ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( edit );
			}
		} );
	}
	
	public Context getRootContext() {
		return this.rootContext;
	}
	public Context getCurrentContext() {
		return this.rootContext.getCurrentContext();
	}

	public <O extends Operation> O lookupOperation( java.util.UUID id ) {
		//todo
		return (O)this.mapUUIDToOperation.get( id );
	}

	protected abstract KComponent<?> createContentPane();

	private KFrame frame = new KFrame();
	
	public KFrame getFrame() {
		return this.frame;
	}

	public void initialize(String[] args) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		jFrame.setContentPane(this.createContentPane().getJComponent());
		jFrame.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
		jFrame.addWindowListener(new java.awt.event.WindowListener() {
			public void windowOpened(java.awt.event.WindowEvent e) {
			}
			public void windowClosing(java.awt.event.WindowEvent e) {
				Application.this.handleQuit(e);
			}
			public void windowClosed(java.awt.event.WindowEvent e) {
			}
			public void windowActivated(java.awt.event.WindowEvent e) {
			}
			public void windowDeactivated(java.awt.event.WindowEvent e) {
			}
			public void windowIconified(java.awt.event.WindowEvent e) {
			}
			public void windowDeiconified(java.awt.event.WindowEvent e) {
			}
		} );
		edu.cmu.cs.dennisc.apple.AppleUtilities.addApplicationListener( new edu.cmu.cs.dennisc.apple.event.ApplicationListener() {
			public void handleAbout( java.util.EventObject e ) {
				Application.this.handleAbout( e );
			}
			public void handlePreferences( java.util.EventObject e ) {
				Application.this.handlePreferences( e );
			}
			public void handleQuit( java.util.EventObject e ) {
				Application.this.handleQuit( e );
			}
		} );
		jFrame.pack();
	}

	private java.util.List< LocaleObserver > localeObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addLocaleObserver( LocaleObserver localeObserver ) {
		this.localeObservers.add( localeObserver );
	}
	public void removeLocaleObserver( LocaleObserver localeObserver ) {
		this.localeObservers.remove( localeObserver );
	}
	public void setLocale( java.util.Locale locale ) {
		java.util.Locale previousLocale = this.frame.getAWTFrame().getLocale();
		
		for( LocaleObserver localeObserver : this.localeObservers ) {
			localeObserver.localeChanging( previousLocale, locale );
		}
		this.frame.getAWTFrame().setLocale( locale );
		//todo: remove
		javax.swing.JComponent.setDefaultLocale( locale );
		for( LocaleObserver localeObserver : this.localeObservers ) {
			localeObserver.localeChanged( previousLocale, locale );
		}
	}
	
	public void setMenuBar( KMenuBar menuBar ) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		javax.swing.JMenuBar jMenuBar = menuBar.getJComponent();
		menuBar.adding();
		jFrame.setJMenuBar(jMenuBar);
		menuBar.added();
		jFrame.pack();
	}
	public void setVisible(boolean isVisible) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		jFrame.setVisible(true);
	}

	public void setTitle(String title) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		jFrame.setTitle(title);
	}

	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );
	protected abstract void handleAbout( java.util.EventObject e );
	protected abstract void handlePreferences( java.util.EventObject e );
	protected abstract void handleQuit( java.util.EventObject e );

	private void register( Operation operation ) {
		java.util.UUID id = operation.getIndividualUUID();
		Operation prev = this.mapUUIDToOperation.get( id );
		if( prev != null ) {
			assert prev == operation;
		} else {
			this.mapUUIDToOperation.put( id, operation );
		}
	}

	public KButton createButton(final AbstractActionOperation actionOperation) {
		this.register( actionOperation );
		return new KButton() {
			@Override
			protected void adding() {
				actionOperation.addAbstractButton(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				actionOperation.removeAbstractButton(this);
			}
		};
	}

	public KMenuItem createMenuItem(final AbstractActionOperation actionOperation) {
		this.register( actionOperation );
		return new KMenuItem() {
			@Override
			protected void adding() {
				actionOperation.addAbstractButton(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				actionOperation.removeAbstractButton(this);
			}
		};
	}
	
	// public javax.swing.AbstractButton createHyperlink( ActionOperation
	// actionOperation ) {
	// assert actionOperation != null;
	// return new ZHyperlink(actionOperation);
	// }


	public KCheckBox createCheckBox(final BooleanStateOperation booleanStateOperation) {
		this.register( booleanStateOperation );
		return new KCheckBox() {
			@Override
			protected void adding() {
				booleanStateOperation.addAbstractButton(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				booleanStateOperation.removeAbstractButton(this);
			}
		};
	}
	public KCheckBoxMenuItem createCheckBoxMenuItem(final BooleanStateOperation booleanStateOperation) {
		this.register( booleanStateOperation );
		// todo: return javax.swing.JMenuItem if true and false different
		return new KCheckBoxMenuItem() {
			@Override
			protected void adding() {
				booleanStateOperation.addAbstractButton(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				booleanStateOperation.removeAbstractButton(this);
			}
		};
	}
	
	public KDragControl createDragComponent(final DragOperation dragOperation, KDragControl.Paintable paintable ) {
		this.register( dragOperation );
		// todo: return javax.swing.JMenuItem if true and false different
		return new KDragControl( paintable ) {
			@Override
			protected void adding() {
				dragOperation.addDragComponent(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				dragOperation.removeDragComponent(this);
			}
		};
	}
	
	private KAbstractMenu< ? > addMenuElements( KAbstractMenu< ? > rv, MenuOperation menuOperation ) {
		for( Operation operation : menuOperation.getOperations() ) {
			if( operation != null ) {
				if( operation instanceof MenuOperation ) {
					rv.addMenu( this.createMenu( (MenuOperation) operation ) );
				} else {
					KAbstractMenuItem<?> menuItem = null;
					if (operation instanceof ActionOperation) {
						ActionOperation actionOperation = (ActionOperation) operation;
						menuItem = this.createMenuItem( actionOperation );
					} else if (operation instanceof CompositeOperation) {
						CompositeOperation compositeOperation = (CompositeOperation)operation;
						menuItem = this.createMenuItem( compositeOperation );				
					} else if (operation instanceof BooleanStateOperation) {
						BooleanStateOperation booleanStateOperation = (BooleanStateOperation)operation;
						menuItem = this.createCheckBoxMenuItem( booleanStateOperation );				
					} else {
						throw new RuntimeException();
					}
					rv.addMenuItem( menuItem );
				}
			} else {
				rv.addSeparator();
			}
		}
		return rv;
	}

	/*package-private*/ KMenu createMenu( final MenuOperation menuOperation ) {
		this.register( menuOperation );
		KMenu rv = new KMenu() {
			@Override
			protected void adding() {
				menuOperation.addMenu(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				menuOperation.removeMenu(this);
			}
		};
		this.addMenuElements( rv, menuOperation );
		return rv;
	}

	/*package-private*/ KPopupMenu createPopupMenu( final PopupMenuOperation popupMenuOperation ) {
		KPopupMenu rv = new KPopupMenu() {
			@Override
			protected void adding() {
				popupMenuOperation.addPopupMenu(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				popupMenuOperation.removePopupMenu(this);
			}
		};
		MenuOperation menuOperation = popupMenuOperation.getMenuOperation();
		this.addMenuElements( rv, menuOperation );
		return rv;
	}
	
	public KMenuBar createMenuBar(final MenuBarOperation menuBarOperation) {
		this.register( menuBarOperation );
		KMenuBar rv = new KMenuBar() {
			@Override
			protected void adding() {
				menuBarOperation.addMenuBar(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				menuBarOperation.removeMenuBar(this);
			}
		};
		for( MenuOperation menuOperation : menuBarOperation.getMenuOperations() ) {
			rv.addMenu( this.createMenu( menuOperation ) );
		}
		return rv;
	}

	public void showMessageDialog( Object message, String title, MessageType messageType, javax.swing.Icon icon ) {
		javax.swing.JOptionPane.showMessageDialog( this.frame.getAWTWindow(), message, title, messageType.internal, icon );
	}
	public void showMessageDialog( Object message, String title, MessageType messageType ) {
		showYesNoCancelConfirmDialog( message, title, messageType, null );
	}
	public void showMessageDialog( Object message, String title ) {
		showYesNoCancelConfirmDialog( message, title, MessageType.QUESTION );
	}
	public void showMessageDialog( Object message ) {
		showYesNoCancelConfirmDialog( message, null );
	}

	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message, String title, MessageType messageType, javax.swing.Icon icon ) {
		return YesNoCancelOption.getInstance( javax.swing.JOptionPane.showConfirmDialog( this.frame.getAWTWindow(), message, title, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, messageType.internal, icon ) );
	}
	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message, String title, MessageType messageType ) {
		return showYesNoCancelConfirmDialog( message, title, messageType, null );
	}
	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message, String title ) {
		return showYesNoCancelConfirmDialog( message, title, MessageType.QUESTION );
	}
	public YesNoCancelOption showYesNoCancelConfirmDialog( Object message ) {
		return showYesNoCancelConfirmDialog( message, null );
	}
	public YesNoOption showYesNoConfirmDialog( Object message, String title, MessageType messageType, javax.swing.Icon icon ) {
		return YesNoOption.getInstance( javax.swing.JOptionPane.showConfirmDialog( this.frame.getAWTWindow(), message, title, javax.swing.JOptionPane.YES_NO_OPTION, messageType.internal, icon ) );
	}
	public YesNoOption showYesNoConfirmDialog( Object message, String title, MessageType messageType ) {
		return showYesNoConfirmDialog( message, title, messageType, null );
	}
	public YesNoOption showYesNoConfirmDialog( Object message, String title ) {
		return showYesNoConfirmDialog( message, title, MessageType.QUESTION );
	}
	public YesNoOption showYesNoConfirmDialog( Object message ) {
		return showYesNoConfirmDialog( message, null );
	}
	
	public java.io.File showOpenFileDialog( String directoryPath, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showOpenFileDialog( this.frame.getAWTWindow(), directoryPath, filename, extension, isSharingDesired ); 
	}
	public java.io.File showSaveFileDialog( String directoryPath, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showSaveFileDialog( this.frame.getAWTWindow(), directoryPath, filename, extension, isSharingDesired ); 
	}
	public java.io.File showOpenFileDialog( java.io.File directory, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showOpenFileDialog( this.frame.getAWTWindow(), directory, filename, extension, isSharingDesired ); 
	}
	public java.io.File showSaveFileDialog( java.io.File directory, String filename, String extension, boolean isSharingDesired ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showSaveFileDialog( this.frame.getAWTWindow(), directory, filename, extension, isSharingDesired ); 
	}

	@Deprecated
	public <T> T showInJDialog( edu.cmu.cs.dennisc.inputpane.KInputPane<T> inputPane, String title, boolean isModal ) {
		return inputPane.showInJDialog( this.frame.getAWTWindow(), title, isModal);
	}
	@Deprecated
	public javax.swing.JFrame getJFrame() {
		//return ((javax.swing.JFrame)this.getFrame().getAWTFrame()).getRootPane();
		return (javax.swing.JFrame)this.getFrame().getAWTFrame();
	}
}
