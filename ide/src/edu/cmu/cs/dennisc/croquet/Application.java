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
	private static class RootContext extends CompositeContext {
		public RootContext() {
			super( null, null, null, null );
		}
	};
	private RootContext rootContext = new RootContext();
	private static Application singleton;

	public static Application getSingleton() {
		return singleton;
	}

	private java.util.Map<java.util.UUID, Operation> mapUUIDToOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private KMenuBar menuBar = new KMenuBar();
	public Application() {
		assert Application.singleton == null;
		Application.singleton = this;
	}
	
	public KMenuBar getMenuBar() {
		return this.menuBar;
	}

	public CompositeContext getCurrentCompositeContext() {
		return this.rootContext.getCurrentCompositeActionContext();
	}

	
	public <O extends Operation> O lookupOperation( java.util.UUID uuid ) {
		//todo
		return (O)this.mapUUIDToOperation.get( uuid );
	}

	protected abstract KComponent<?> createContentPane();

	private KFrame frame = new KFrame();
	
	public KFrame getFrame() {
		return this.frame;
	}

	public void initialize(String[] args) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		jFrame.setJMenuBar(this.menuBar.getJComponent());
		jFrame.setContentPane(this.createContentPane().getJComponent());
		jFrame.pack();
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
		
	}

	public void setVisible(boolean isVisible) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		jFrame.setVisible(true);
	}

	public void setTitle(String title) {
		javax.swing.JFrame jFrame = (javax.swing.JFrame) frame.getAWTFrame();
		jFrame.setTitle(title);
	}

	protected abstract void handleAbout( java.util.EventObject e );
	protected abstract void handlePreferences( java.util.EventObject e );
	protected abstract void handleQuit( java.util.EventObject e );

	private static java.util.List<edu.cmu.cs.dennisc.croquet.event.ManagerListener> managerListeners = new java.util.LinkedList<edu.cmu.cs.dennisc.croquet.event.ManagerListener>();
	private static edu.cmu.cs.dennisc.croquet.event.ManagerListener[] managerListenerArray = null;

	public static void addManagerListener(edu.cmu.cs.dennisc.croquet.event.ManagerListener l) {
		synchronized (Application.managerListeners) {
			Application.managerListeners.add(l);
			Application.managerListenerArray = null;
		}
	}

	public static void removeManagerListener(edu.cmu.cs.dennisc.croquet.event.ManagerListener l) {
		synchronized (Application.managerListeners) {
			Application.managerListeners.remove(l);
			Application.managerListenerArray = null;
		}
	}

	private static edu.cmu.cs.dennisc.croquet.event.ManagerListener[] getManagerListenerArray() {
		synchronized (Application.managerListeners) {
			if (Application.managerListenerArray != null) {
				// pass
			} else {
				Application.managerListenerArray = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray(Application.managerListeners, edu.cmu.cs.dennisc.croquet.event.ManagerListener.class);
			}
			return Application.managerListenerArray;
		}
	}

	/* package-private */static void fireOperationCancelling(edu.cmu.cs.dennisc.croquet.event.CancelEvent e) {
		for (edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray()) {
			l.operationCancelling(e);
		}
	}

	/* package-private */static void fireOperationCancelled(edu.cmu.cs.dennisc.croquet.event.CancelEvent e) {
		for (edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray()) {
			l.operationCancelled(e);
		}
	}

	/* package-private */static void fireOperationCommitting(edu.cmu.cs.dennisc.croquet.event.CommitEvent e) {
		for (edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray()) {
			l.operationCommitting(e);
		}
	}

	/* package-private */static void fireOperationCommitted(edu.cmu.cs.dennisc.croquet.event.CommitEvent e) {
		for (edu.cmu.cs.dennisc.croquet.event.ManagerListener l : Application.getManagerListenerArray()) {
			l.operationCommitted(e);
		}
	}
	
	private void register( Operation operation ) {
		java.util.UUID id = operation.getIndividualUUID();
		Operation prev = this.mapUUIDToOperation.get( id );
		if( prev != null ) {
			assert prev == operation;
		} else {
			this.mapUUIDToOperation.put( id, operation );
		}
	}

	public KButton createButton(final ActionOperation actionOperation) {
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

	public KMenuItem createMenuItem(final ActionOperation actionOperation) {
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
	
	public KButton createButton(final CompositeOperation compositeOperation) {
		this.register( compositeOperation );
		return new KButton() {
			@Override
			protected void adding() {
				compositeOperation.addAbstractButton(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				compositeOperation.removeAbstractButton(this);
			}
		};
	}

	public KMenuItem createMenuItem(final CompositeOperation compositeOperation) {
		this.register( compositeOperation );
		return new KMenuItem() {
			@Override
			protected void adding() {
				compositeOperation.addAbstractButton(this);
				super.adding();
			}

			@Override
			protected void removed() {
				super.removed();
				compositeOperation.removeAbstractButton(this);
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

	public KMenuItem createMenuItem(final BooleanStateOperation booleanStateOperation) {
		this.register( booleanStateOperation );
		// todo: return javax.swing.JMenuItem if true and false different
		return new KMenuItem() {
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
	
	public KDragComponent createDragComponent(final DragOperation dragOperation) {
		this.register( dragOperation );
		// todo: return javax.swing.JMenuItem if true and false different
		return new KDragComponent() {
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

	public KMenu createMenu( final MenuOperation menuOperation ) {
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
		for( Operation operation : menuOperation.getOperations() ) {
			if( operation != null ) {
				if( operation instanceof MenuOperation ) {
					rv.addMenu( this.createMenu( (MenuOperation) operation ) );
				} else {
					KMenuItem menuItem = null;
					if (operation instanceof ActionOperation) {
						ActionOperation actionOperation = (ActionOperation) operation;
						menuItem = this.createMenuItem( actionOperation );
					} else if (operation instanceof CompositeOperation) {
						CompositeOperation compositeOperation = (CompositeOperation)operation;
						menuItem = this.createMenuItem( compositeOperation );				
					} else if (operation instanceof BooleanStateOperation) {
						BooleanStateOperation booleanStateOperation = (BooleanStateOperation)operation;
						menuItem = this.createMenuItem( booleanStateOperation );				
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

	//todo
	public <T> T showInJDialog( edu.cmu.cs.dennisc.inputpane.KInputPane<T> inputPane, String title, boolean isModal ) {
		return inputPane.showInJDialog( this.frame.getAWTWindow(), title, isModal);
	}	
}
