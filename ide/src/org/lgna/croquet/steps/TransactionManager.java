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
package org.lgna.croquet.steps;

import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Menu;
import org.lgna.croquet.components.MenuBar;
import org.lgna.croquet.components.MenuItem;
import org.lgna.croquet.components.MenuTextSeparator;
import org.lgna.croquet.components.PopupMenu;
import org.lgna.croquet.components.ViewController;

import edu.cmu.cs.dennisc.croquet.*;

/**
 * @author Dennis Cosgrove
 */
public class TransactionManager {
	public static interface Observer {
		public void addingStep( Step<?> step );
		public void addedStep( Step<?> step );
		public void editCommitting( edu.cmu.cs.dennisc.croquet.Edit<?> edit );
		public void editCommitted( edu.cmu.cs.dennisc.croquet.Edit<?> edit );
		public void finishing( Transaction transaction );
		public void finished( Transaction transaction );
		public void dropPending( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite );
		public void dropPended( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite );
		public void menuItemsSelectionChanged( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models );
		public void popupMenuResized( org.lgna.croquet.components.PopupMenu popupMenu );
		public void dialogOpened( org.lgna.croquet.components.Dialog dialog );
		public void transactionCanceled( Transaction transaction );
	}

	public static interface EventObserver {
		public void firingEvent( org.lgna.cheshire.events.Event event );
		public void firedEvent( org.lgna.cheshire.events.Event event );
	}
	
	private static final java.util.List<Observer> observers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private static final java.util.List<EventObserver> eventObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private static final java.util.Stack< TransactionHistory > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	static {
		stack.push( new TransactionHistory() );
	}
	private TransactionManager() {
		throw new AssertionError();
	}

	public static void addObserver( Observer observer ) {
		observers.add( observer );
	}
	public static void removeObserver( Observer observer ) {
		observers.remove( observer );
	}
	
	public static void addEventObserver( EventObserver eventObserver ) {
		eventObservers.add( eventObserver );
	}
	public static void removeEventObserver( EventObserver eventObserver ) {
		eventObservers.remove( eventObserver );
	}

	public static TransactionHistory getRootTransactionHistory() {
		return stack.firstElement();
	}
	private static TransactionHistory getActiveTransactionHistory() {
		return stack.peek();
	}
	
	public static void pushTransactionHistory( TransactionHistory transactionHistory ) {
		stack.push( transactionHistory );
	}
	public static TransactionHistory popTransactionHistory() {
		return stack.pop();
	}
	
	private static boolean isCroquetMenuSelection( javax.swing.MenuElement[] menuElements ) {
		for( javax.swing.MenuElement menuElement : menuElements ) {
			Component< ? > component = Component.lookup( menuElement.getComponent() );
			if( component instanceof MenuBar || component instanceof MenuItem || component instanceof Menu || component instanceof PopupMenu || component instanceof MenuTextSeparator ) {
				return true;
			}
		}
		return menuElements.length == 0;
	}

	private static javax.swing.JMenuBar getJMenuBarOrigin( javax.swing.MenuElement[] menuElements ) { 
		if( menuElements.length > 0 ) {
			javax.swing.MenuElement menuElement0 = menuElements[ 0 ];
			if( menuElement0 instanceof javax.swing.JMenuBar ) {
				return (javax.swing.JMenuBar)menuElement0;
			}
		}
		return null;
	}
	private static MenuBar getMenuBarOrigin( javax.swing.MenuElement[] menuElements ) {
		javax.swing.JMenuBar jMenuBar = getJMenuBarOrigin( menuElements );
		if( jMenuBar != null ) {
			return (MenuBar)Component.lookup( jMenuBar );
		} else {
			return null;
		}
	}
	private static MenuBarComposite getMenuBarModelOrigin( javax.swing.MenuElement[] menuElements ) {
		MenuBar menuBar = getMenuBarOrigin( menuElements );
		if( menuBar != null ) {
			return menuBar.getModel();
		} else {
			return null;
		}
	}

	private static javax.swing.MenuElement[] previousMenuElements = {};
	private static void handleMenuSelectionStateChanged( javax.swing.event.ChangeEvent e ) {
		
		javax.swing.MenuElement[] menuElements = javax.swing.MenuSelectionManager.defaultManager().getSelectedPath();
		if( isCroquetMenuSelection( menuElements ) ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( menuElements.length );
			for( javax.swing.MenuElement menuElement : menuElements ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.print( menuElement.getClass().getName() );
				edu.cmu.cs.dennisc.print.PrintUtilities.print( ", " );
			}
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			java.util.List< MenuItemPrepModel > models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			MenuBar menuBar = getMenuBarOrigin( menuElements );
			int i0;
			if( menuBar != null ) {
				javax.swing.JPopupMenu jPreviousPopupMenu;
				if( previousMenuElements.length >= 3 ) {
					jPreviousPopupMenu = (javax.swing.JPopupMenu)previousMenuElements[ 2 ];
				} else {
					jPreviousPopupMenu = null;
				}
				if( menuElements.length >= 3 ) {
					assert menuElements.length >= 3;
					assert menuElements[ 1 ] instanceof javax.swing.JMenu;
					assert menuElements[ 2 ] instanceof javax.swing.JPopupMenu;
					javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElements[ 2 ];
					
					javax.swing.JMenu jMenu = (javax.swing.JMenu)menuElements[ 1 ];
					Menu menu = (Menu)Component.lookup( jMenu );
					assert menu != null;

					MenuItemPrepModel menuModel = menu.getModel();
					assert menuModel != null;
					models.add( menuModel );
					i0 = 3;
				} else {
					i0 = -1;
				}
			} else {
				i0 = 0;
			}
			if( i0 != -1 ) {
				for( int i=i0; i<menuElements.length; i++ ) {
					javax.swing.MenuElement menuElementI = menuElements[ i ];
					if( menuElementI instanceof javax.swing.JPopupMenu ) {
						javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElementI;
						//pass
					} else if( menuElementI instanceof javax.swing.JMenuItem ) {
						javax.swing.JMenuItem jMenuItem = (javax.swing.JMenuItem)menuElementI;
						Component< ? > component = Component.lookup( jMenuItem );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleMenuSelectionStateChanged", i, component.getClass() );
						if( component instanceof ViewController< ?, ? > ) {
							ViewController< ?, ? > viewController = (ViewController< ?, ? >)component;
							//edu.cmu.cs.dennisc.print.PrintUtilities.println( "viewController", i, viewController.getModel() );
							Model model = viewController.getModel();
							if( model != null ) {
								MenuItemPrepModel menuItemPrepModel;
								if( model instanceof MenuItemPrepModel ) {
									menuItemPrepModel = (MenuItemPrepModel)model;
								} else if( model instanceof Operation<?> ) {
									menuItemPrepModel = ((Operation< ? >)model).getMenuItemPrepModel();
								} else if( model instanceof BooleanState ) {
									menuItemPrepModel = ((BooleanState)model).getMenuItemPrepModel();
								} else {
									throw new RuntimeException( model.toString() );
								}
								models.add( menuItemPrepModel );
							} else {
								throw new NullPointerException();
							}
						}
					}
				}
				getActiveTransaction().pendMenuSelection( e, models );
			}

//			if( previousMenuElements.length > 0 ) {
//				if( menuElements.length > 0 ) {
//					java.util.List< Model > models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//					MenuBar menuBar = getMenuBarOrigin( menuElements );
//					int i0;
//					if( menuBar != null ) {
//						models.add( menuBar.getModel() );
//						javax.swing.JPopupMenu jPreviousPopupMenu;
//						if( previousMenuElements.length >= 3 ) {
//							jPreviousPopupMenu = (javax.swing.JPopupMenu)previousMenuElements[ 2 ];
//						} else {
//							jPreviousPopupMenu = null;
//						}
//						
//						assert menuElements.length >= 3;
//						assert menuElements[ 1 ] instanceof javax.swing.JMenu;
//						assert menuElements[ 2 ] instanceof javax.swing.JPopupMenu;
//						javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElements[ 2 ];
//						
//						javax.swing.JMenu jMenu = (javax.swing.JMenu)menuElements[ 1 ];
//						Menu menu = (Menu)Component.lookup( jMenu );
//						assert menu != null;
//
//						Model menuModel = menu.getModel();
//						assert menuModel != null;
//						models.add( menuModel );
//
//						if( jPreviousPopupMenu != jPopupMenu ) {
//							if( jPreviousPopupMenu != null ) {
//								ModelContext< ? > popupContext = ContextManager.popContext();
//								assert popupContext instanceof PopupOperationContext;
//							}
//							
//							if( menuModel instanceof MenuModel ) {
//								/*AbstractPopupMenuOperationContext popupContext =*/ ContextManager.createAndPushStandardPopupOperationContext( ((MenuModel)menuModel).getPopupMenuOperation(), e, null );
//							} else {
//								System.err.println( "handleMenuSelectionStateChanged: " + menuModel );
//							}
//							
//						}
//						i0 = 3;
//					} else {
//						i0 = 0;
//					}
//					for( int i=i0; i<menuElements.length; i++ ) {
//						javax.swing.MenuElement menuElementI = menuElements[ i ];
//						if( menuElementI instanceof javax.swing.JPopupMenu ) {
//							javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElementI;
//							//pass
//						} else if( menuElementI instanceof javax.swing.JMenuItem ) {
//							javax.swing.JMenuItem jMenuItem = (javax.swing.JMenuItem)menuElementI;
//							Component< ? > component = Component.lookup( jMenuItem );
//							//edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleMenuSelectionStateChanged", i, component.getClass() );
//							if( component instanceof ViewController< ?, ? > ) {
//								ViewController< ?, ? > viewController = (ViewController< ?, ? >)component;
//								//edu.cmu.cs.dennisc.print.PrintUtilities.println( "viewController", i, viewController.getModel() );
//								models.add( viewController.getModel() );
//							}
//						}
//					}
//					ModelContext< ? > modelContext = ContextManager.getCurrentContext();
//					if( modelContext instanceof PopupOperationContext ) {
//						PopupOperationContext popupContext = (PopupOperationContext)modelContext;
//						popupContext.handleMenuSelectionChanged( e, models );
//					} else {
//						System.err.println( "WARNING: handleMenuSelectionStateChanged not PopupMenuOperationContext " + modelContext );
//					}
//					TransactionManager.handleMenuSelectionChanged( models );
//				} else {
//					MenuBarModel menuBarModel = getMenuBarModelOrigin( previousMenuElements );
//					if( menuBarModel != null ) {
//						ModelContext< ? > popupContext = ContextManager.popContext();
//						assert popupContext instanceof StandardPopupOperationContext;
//
//						ModelContext< ? > menuBarContext = ContextManager.popContext();
//						assert menuBarContext instanceof MenuBarModelContext;
//					}
//				}
//			} else {
//				if( menuElements.length > 0 ) {
//					MenuBar menuBar = getMenuBarOrigin( menuElements );
//					if( menuBar != null ) {
//						/*MenuBarModelContext childContext =*/ ContextManager.createAndPushMenuBarModelContext( menuBar.getModel(), e, menuBar );
//						assert menuElements.length == 2;
//					} else {
//						ModelContext< ? > modelContext = ContextManager.getCurrentContext();
//						if( modelContext instanceof StandardPopupOperationContext ) {
//							//pass
//						} else {
//							System.err.println( "combo box? " + menuElements.length + " " + java.util.Arrays.toString( menuElements ) );
//							System.err.println( "modelContext: " + modelContext );
//						}
//					}
//				} else {
//					//assert false;
//					ModelContext< ? > modelContext = ContextManager.getCurrentContext();
//					System.err.println( "both prev and current menu selection length 0" );
//					System.err.println( "modelContext: " + modelContext );
//				}
//			}
//			previousMenuElements = menuElements;
		} else {
			System.err.println( "warning: not croquet menu selection." );
		}
	}
	private static javax.swing.event.ChangeListener menuSelectionChangeListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			handleMenuSelectionStateChanged( e );
		}
	};
	public static void pendDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		fireDropPending( completionModel, dropReceptor, dropSite );
		getLastTransaction().pendDrop( completionModel, dropReceptor, dropSite );
		fireDropPended( completionModel, dropReceptor, dropSite );
	}
	
	public static void handleMenuSelectionChanged( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models ) {
		fireMenuItemsSelectionChanged( models );
	}

	public static void handleDocumentEvent( StringState stringState, javax.swing.event.DocumentEvent documentEvent, String previousValue, String nextValue ) {
//		ModelContext< ? > topContext = stack.peek();
//		StringStateContext stringStateContext;
//		if( topContext instanceof StringStateContext ) {
//			stringStateContext = (StringStateContext)topContext;
//		} else {
//			stringStateContext = pushContext( new StringStateContext( stringState, documentEvent, viewController, previousValue ) );
//			org.lgna.croquet.steps.TransactionManager.addStringStateChangeStep( stringState );
//		}
	}

//	public static <E> void addListSelectionPopupMenuWillBecomeVisible( ListSelectionState<E> model, javax.swing.event.PopupMenuEvent e, ItemSelectable< ?, ? > itemSelectable ) {
//		ListSelectionStateContext<E> listSelectionStateContext = createAndPushItemSelectionStateContext( model, e, itemSelectable );
//		listSelectionStateContext.handlePopupMenuWillBecomeVisibleEvent( e );
//		TransactionManager.addListSelectionPrepStep( model.getPrepModel() );
//	}
//	public static <E> void addListSelectionPopupMenuWillBecomeInvisible( ListSelectionState<E> model, javax.swing.event.PopupMenuEvent e, ItemSelectable< ?, ? > itemSelectable ) {
//	}
//	public static <E> void addListSelectionPopupMenuCanceled( ListSelectionState<E> model, javax.swing.event.PopupMenuEvent e, ItemSelectable< ?, ? > itemSelectable ) {
//		TransactionManager.addCancelCompletionStep( model );
//	}

	public static void startListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().addChangeListener( menuSelectionChangeListener );
	}
	public static void stopListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().removeChangeListener( menuSelectionChangeListener );
	}

	/*package-private*/ static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().getActiveTransaction();
	}
	private static Transaction getLastTransaction() {
		return getActiveTransactionHistory().getLastTransaction();
	}

	/*package-private*/ static void fireAddingStep( Step<?> step ) {
		for( Observer observer : observers ) {
			observer.addingStep( step );
		}
	}
	/*package-private*/ static void fireAddedStep( Step<?> step ) {
		for( Observer observer : observers ) {
			observer.addedStep( step );
		}
	}
	
	//todo: reduce accessibility
	public static void fireTransactionCanceled( Transaction transaction ) {
		for( Observer observer : observers ) {
			observer.transactionCanceled( transaction );
		}
	}
	//todo: reduce accessibility
	public static void firePopupMenuResized( org.lgna.croquet.components.PopupMenu popupMenu ) {
		for( Observer observer : observers ) {
			observer.popupMenuResized( popupMenu );
		}
	}
	public static void fireDialogOpened( org.lgna.croquet.components.Dialog dialog ) {
		for( Observer observer : observers ) {
			observer.dialogOpened( dialog );
		}
	}
	private static void fireEditCommitting( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		for( Observer observer : observers ) {
			observer.editCommitting( edit );
		}
	}
	private static void fireEditCommitted( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		for( Observer observer : observers ) {
			observer.editCommitted( edit );
		}
	}
	private static void fireFinishing( Transaction transaction ) {
		for( Observer observer : observers ) {
			observer.finishing( transaction );
		}
	}
	private static void fireFinished( Transaction transaction ) {
		for( Observer observer : observers ) {
			observer.finished( transaction );
		}
	}
	private static void fireDropPending( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		for( Observer observer : observers ) {
			observer.dropPending( completionModel, dropReceptor, dropSite );
		}
	}
	private static void fireDropPended( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		for( Observer observer : observers ) {
			observer.dropPended( completionModel, dropReceptor, dropSite );
		}
	}
	private static void fireMenuItemsSelectionChanged( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models ) {
		for( Observer observer : observers ) {
			observer.menuItemsSelectionChanged( models );
		}
	}
	
	
	public static DragStep addDragStep( edu.cmu.cs.dennisc.croquet.DragAndDropModel model, org.lgna.croquet.Trigger trigger ) {
		return DragStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static ActionOperationStep addActionOperationStep( edu.cmu.cs.dennisc.croquet.ActionOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return ActionOperationStep.createAndAddToTransaction( transaction, model, trigger ); 
	}
	public static SerialOperationStep addSerialOperationStep( edu.cmu.cs.dennisc.croquet.SerialOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return SerialOperationStep.createAndAddToTransaction( transaction, model, trigger ); 
	}
	public static PlainDialogOperationStep addPlainDialogOperationStep( edu.cmu.cs.dennisc.croquet.PlainDialogOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return PlainDialogOperationStep.createAndAddToTransaction( transaction, model, trigger );
	}
	public static PlainDialogCloseOperationStep addPlainDialogCloseOperationStep( edu.cmu.cs.dennisc.croquet.PlainDialogCloseOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return PlainDialogCloseOperationStep.createAndAddToTransaction( transaction, model, trigger );
	}
	public static InputDialogOperationStep addInputDialogOperationStep( edu.cmu.cs.dennisc.croquet.InputDialogOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return InputDialogOperationStep.createAndAddToTransaction( transaction, model, trigger );
	}
	public static InformationDialogOperationStep addInformationDialogOperationStep( edu.cmu.cs.dennisc.croquet.InformationDialogOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return InformationDialogOperationStep.createAndAddToTransaction( transaction, model, trigger );
	}
	public static WizardDialogOperationStep addWizardDialogOperationStep( edu.cmu.cs.dennisc.croquet.WizardDialogOperation model, org.lgna.croquet.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		return WizardDialogOperationStep.createAndAddToTransaction( transaction, model, trigger );
	}
	public static StandardPopupOperationStep addStandardPopupOperationStep( edu.cmu.cs.dennisc.croquet.StandardPopupOperation standardPopupOperation, org.lgna.croquet.Trigger trigger ) {
		return StandardPopupOperationStep.createAndAddToTransaction( getActiveTransaction(), standardPopupOperation, trigger );
	}
	public static <T> CascadePopupOperationStep<T> addCascadePopupOperationStep( edu.cmu.cs.dennisc.croquet.CascadePopupOperation<T> model, org.lgna.croquet.Trigger trigger ) {
		return CascadePopupOperationStep.createAndAddToTransaction( getActiveTransaction(), model, trigger );
	}

	public static BooleanStateChangeStep addBooleanStateChangeStep( edu.cmu.cs.dennisc.croquet.BooleanState model, org.lgna.croquet.Trigger trigger ) {
		return BooleanStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model, trigger );
	}
	public static StringStateChangeStep addStringStateChangeStep( edu.cmu.cs.dennisc.croquet.StringState model, org.lgna.croquet.Trigger trigger ) {
		return StringStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static <E> ListSelectionStateChangeStep<E> addListSelectionStateChangeStep( edu.cmu.cs.dennisc.croquet.ListSelectionState< E > model, org.lgna.croquet.Trigger trigger ) {
		return ListSelectionStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static <E> ListSelectionStatePrepStep<E> addListSelectionPrepStep( edu.cmu.cs.dennisc.croquet.ListSelectionStatePrepModel< E > model, org.lgna.croquet.Trigger trigger ) {
		return ListSelectionStatePrepStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static <E> CancelCompletionStep addCancelCompletionStep( edu.cmu.cs.dennisc.croquet.CompletionModel model, org.lgna.croquet.Trigger trigger ) {
		return CancelCompletionStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}


	private static void popCompletionStepTransactionHistoryIfNecessary( edu.cmu.cs.dennisc.croquet.Model model ) {
		TransactionHistory transactionHistory = getActiveTransactionHistory();
		CompletionStep< ? > completionStep = transactionHistory.getParent();
		if( completionStep != null ) {
			if( completionStep.getModel() == model ) {
				completionStep.popTransactionHistoryIfNecessary();
			}
		}
	}

//	private static void finishPendingTransactionIfNecessary() {
//		TransactionHistory activeTransactionHistory = getActiveTransactionHistory();
//		CompletionStep< ? > completionStep = activeTransactionHistory.getParent();
//		if( completionStep != null && completionStep.isActive() ) {
//			if( stepsAwaitingFinish.contains( completionStep ) ) {
//				finish( completionStep.getModel() );
//				stepsAwaitingFinish.remove( completionStep );
//			}
//		}
//	}

//	public static void commit( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
//		popCompletionStepTransactionHistoryIfNecessary( edit.getModel() );
//		fireEditCommitting( edit );
//		getLastTransaction().commit( edit );
//		fireEditCommitted( edit );
////		finishPendingTransactionIfNecessary();
//	}
//	public static void finish( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
//		popCompletionStepTransactionHistoryIfNecessary( model );
//		Transaction transaction = getLastTransaction();
//		fireFinishing( transaction );
//		transaction.finish();
//		fireFinished( transaction );
////		finishPendingTransactionIfNecessary();
//	}
//	public static void cancel( edu.cmu.cs.dennisc.croquet.CompletionModel model, org.lgna.croquet.Trigger trigger ) {
//		popCompletionStepTransactionHistoryIfNecessary( model );
//		getLastTransaction().cancel( trigger );
////		finishPendingTransactionIfNecessary();
//	}

	public static <E> Transaction createSimulatedTransaction( TransactionHistory transactionHistory, ListSelectionState< E > state, E prevValue, E nextValue, boolean isPrepStepDesired ) {
		org.lgna.croquet.steps.Transaction rv = new org.lgna.croquet.steps.Transaction( transactionHistory );
		if( isPrepStepDesired ) {
			org.lgna.croquet.steps.ListSelectionStatePrepStep.createAndAddToTransaction( rv, state.getPrepModel(), org.lgna.croquet.triggers.SimulatedTrigger.SINGLETON );
		}
		org.lgna.croquet.steps.ListSelectionStateChangeStep completionStep = org.lgna.croquet.steps.ListSelectionStateChangeStep.createAndAddToTransaction( rv, state, org.lgna.croquet.triggers.SimulatedTrigger.SINGLETON );
		org.lgna.croquet.edits.ListSelectionStateEdit edit = new org.lgna.croquet.edits.ListSelectionStateEdit( completionStep, prevValue, nextValue );
		completionStep.setEdit( edit );
		return rv;
	}
	public static void simulatedMenuTransaction( Transaction transaction, java.util.List< MenuItemPrepModel > menuItemPrepModels ) {
		for( edu.cmu.cs.dennisc.croquet.MenuItemPrepModel menuItemPrepModel : menuItemPrepModels ) {
			System.err.println( "todo: add step for: " + menuItemPrepModel );
			//org.lgna.croquet.steps.MenuItemPrepStep.createAndAddToTransaction( transaction, menuItemPrepModel, org.lgna.croquet.triggers.SimulatedTrigger.SINGLETON );
		}
	}

//	public static CascadeBlankStep createCascadeBlankStep( CascadeBlank model ) {
//		return null;
//	}
//	public static CascadeCancelStep createCascadeCancelStep( CascadeCancel model ) {
//		return null;
//	}
//	public static CascadeFillInPrepStep createCascadeFillInPrepStep( CascadeFillIn model ) {
//		return null;
//	}
//	public static CascadeMenuStep createCascadeMenuStep( CascadeMenu model ) {
//		return null;
//	}
//	public static CascadeRootStep createCascadeRootStep( CascadeRoot model ) {
//		return null;
//	}
//	public static CascadeSeparatorStep createCascadeSeparatorStep( CascadeSeparator model ) {
//		return new CascadeSeparatorStep( model, null );
//	}

	public static void handleStateChanged( BoundedRangeIntegerState boundedRangeIntegerState, javax.swing.event.ChangeEvent e ) {
//		org.lgna.croquet.steps.TransactionManager.handleStateChanged( BoundedRangeIntegerState.this, e );
//		org.lgna.croquet.steps.BoundedRangeIntegerStateChangeStep step;
//		if( this.previousValueIsAdjusting ) {
//			step = (org.lgna.croquet.steps.BoundedRangeIntegerStateChangeStep)org.lgna.croquet.steps.TransactionManager.getActiveTransaction().getCompletionStep();
//		} else {
//			step = org.lgna.croquet.steps.TransactionManager.addBoundedRangeIntegerStateChangeStep( BoundedRangeIntegerState.this );
//		}
//		this.previousValueIsAdjusting = boundedRangeModel.getValueIsAdjusting();
//		step.handleStateChanged( e );
//		BoundedRangeIntegerState.this.fireValueChanged( e );
//
//		if( this.previousValueIsAdjusting ) {
//			//pass
//		} else {
//			int nextValue = boundedRangeModel.getValue();
//			step.commitAndInvokeDo( new org.lgna.croquet.edits.BoundedRangeIntegerStateEdit( e, BoundedRangeIntegerState.this.previousValue, nextValue, false ) );
//			BoundedRangeIntegerState.this.previousValue = nextValue;
////				ModelContext< ? > popContext = ContextManager.popContext();
////				assert popContext == boundedRangeIntegerStateContext;
//		}
	}

	public static org.lgna.croquet.edits.BooleanStateEdit commitEdit( BooleanState booleanState, boolean value, org.lgna.croquet.Trigger trigger ) {
		org.lgna.croquet.steps.BooleanStateChangeStep step = org.lgna.croquet.steps.TransactionManager.addBooleanStateChangeStep( booleanState, trigger );
		org.lgna.croquet.edits.BooleanStateEdit rv = new org.lgna.croquet.edits.BooleanStateEdit( step, value );
		step.commitAndInvokeDo( rv );
		return rv;
	}
	public static void handleItemStateChanged( BooleanState booleanState, java.awt.event.ItemEvent e ) {
		if( Manager.isInTheMidstOfUndoOrRedo() ) {
			//pass
		} else {
			if( booleanState.isToBeIgnored() ) {
				//pass
			} else {
				commitEdit( booleanState, e.getStateChange() == java.awt.event.ItemEvent.SELECTED, new org.lgna.croquet.triggers.ItemEventTrigger( e ) );
			}
		}
	}
}
