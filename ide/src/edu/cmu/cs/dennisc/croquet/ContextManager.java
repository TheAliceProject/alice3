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
public class ContextManager {
	private static java.util.Stack< AbstractModelContext< ? > > stack;
	private static java.util.Map< AbstractModelContext< ? >, AbstractModelContext< ? > > mapChildContextPendingParentContext = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	static {
		stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
		stack.push( new RootContext() );
	}
	public static RootContext getRootContext() {
		return (RootContext)stack.firstElement();
	}
	public static AbstractModelContext< ? > getCurrentContext() {
		return stack.peek();
	}
		
	/*package-private*/ static void popParentContextWhenChildContextIsPopped( AbstractModelContext< ? > parentContext, AbstractModelContext< ? > childContext ) {
		//assert childContext.getParent() == parentContext;
		if( childContext.getParent() == parentContext ) {
			//pass
		} else {
			System.err.println( "childContext.getParent() == parentContext" );
		}
		mapChildContextPendingParentContext.put( childContext, parentContext );
	}

	private static PopupMenuOperationContext getPopupMenuOperationContextToPushOnto( PopupMenuOperationContext candidate, AbstractModelContext< ? > childContext ) {
		HistoryNode lastChild = candidate.getLastChild();
		if( lastChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent ) {
			edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent)lastChild;
			Model model = menuSelectionEvent.getLastModel();
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( model, childContext.getModel() ) ) {
				return candidate;
			}
		}
		return null;
	}
	/*package-private*/ static <C extends AbstractModelContext< ? > > C pushContext( C rv ) {
		AbstractModelContext< ? > parentContext = getCurrentContext();
		if( parentContext instanceof StringStateContext ) {
			StringStateContext stringStateContext = (StringStateContext)parentContext;
			stringStateContext.handlePop();
			stack.pop();
			parentContext = stack.peek();
		}
		HistoryNode lastChild = parentContext.getLastChild();
		MenuBarModelContext menuBarModelContextToPushOnto = null;
		DragAndDropContext dragAndDropContextToPushOnto = null;
		PopupMenuOperationContext popupMenuOperationContextToPushOnto = null;
		if( lastChild != null ) {
			HistoryNode.State state = lastChild.getState();
			if( state == null ) {
				if( lastChild instanceof MenuBarModelContext ) {
					MenuBarModelContext menuBarModelContext = (MenuBarModelContext)lastChild; 
					HistoryNode lastGrandchild = menuBarModelContext.getLastChild();
					if( lastGrandchild instanceof PopupMenuOperationContext ) {
						PopupMenuOperationContext popupMenuOperationContext = (PopupMenuOperationContext)lastGrandchild; 
						popupMenuOperationContextToPushOnto = getPopupMenuOperationContextToPushOnto( popupMenuOperationContext, rv );
						if( popupMenuOperationContextToPushOnto != null ) {
							menuBarModelContextToPushOnto = menuBarModelContext;
						}
					}
				} else if( lastChild instanceof DragAndDropContext ) {
					DragAndDropContext dragAndDropContext = (DragAndDropContext)lastChild; 
					HistoryNode lastGrandchild = dragAndDropContext.getLastChild();
					if( lastGrandchild instanceof PopupMenuOperationContext ) {
						PopupMenuOperationContext popupMenuOperationContext = (PopupMenuOperationContext)lastGrandchild; 
						popupMenuOperationContextToPushOnto = getPopupMenuOperationContextToPushOnto( popupMenuOperationContext, rv );
						if( popupMenuOperationContextToPushOnto != null ) {
							dragAndDropContextToPushOnto = dragAndDropContext;
						}
					}
				} else if( lastChild instanceof PopupMenuOperationContext ) {
					PopupMenuOperationContext popupMenuOperationContext = (PopupMenuOperationContext)lastChild; 
					popupMenuOperationContextToPushOnto = getPopupMenuOperationContextToPushOnto( popupMenuOperationContext, rv );
				}

				if( popupMenuOperationContextToPushOnto != null ) {
					parentContext = popupMenuOperationContextToPushOnto;
				}
			}
		}
		parentContext.addChild( rv );
		if( popupMenuOperationContextToPushOnto != null ) {
			if( menuBarModelContextToPushOnto != null ) {
				popParentContextWhenChildContextIsPopped( menuBarModelContextToPushOnto, popupMenuOperationContextToPushOnto );
				stack.push( menuBarModelContextToPushOnto );
			}
			if( dragAndDropContextToPushOnto != null ) {
				popParentContextWhenChildContextIsPopped( dragAndDropContextToPushOnto, popupMenuOperationContextToPushOnto );
				stack.push( dragAndDropContextToPushOnto );
			}
			popParentContextWhenChildContextIsPopped( popupMenuOperationContextToPushOnto, rv );
			stack.push( popupMenuOperationContextToPushOnto );
			parentContext = popupMenuOperationContextToPushOnto;
		}
		stack.push( rv );
		return rv;
	}
	/*package-private*/ static AbstractModelContext< ? > popContext() {
		AbstractModelContext< ? > childContext = stack.peek();
		if( childContext instanceof RootContext ) {
			System.err.println( "WARNING: attempting to pop root context" );
			return null;
		} else {
			if( childContext instanceof StringStateContext ) {
				StringStateContext stringStateContext = (StringStateContext)childContext;
				stringStateContext.handlePop();
				stack.pop();
				childContext = stack.peek();
			}
			AbstractModelContext< ? > parentContext = mapChildContextPendingParentContext.get( childContext );
			
			childContext.popping();
			AbstractModelContext< ? > rv = stack.pop();
			childContext.popped();
			if( parentContext != null ) {
				mapChildContextPendingParentContext.remove( childContext );
				assert parentContext == stack.peek() : parentContext + " " + stack.peek();
				popContext();
			}
			return rv;
		}
	}

	/*package-private*/ static void handleDocumentEvent( StringState stringState, java.util.EventObject e, ViewController< ?, ? > viewController, javax.swing.event.DocumentEvent documentEvent, String previousValue, String nextValue ) {
		AbstractModelContext< ? > topContext = stack.peek();
		StringStateContext stringStateContext;
		if( topContext instanceof StringStateContext ) {
			stringStateContext = (StringStateContext)topContext;
		} else {
			stringStateContext = pushContext( new StringStateContext( stringState, e, viewController, previousValue ) );
		}
		stringStateContext.handleDocumentEvent( documentEvent, nextValue );
	}

	
	/*package-private*/ static ActionOperationContext createAndPushActionOperationContext(ActionOperation actionOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new ActionOperationContext(actionOperation, e, viewController) );
	}
	/*package-private*/ static CompositeOperationContext createAndPushCompositeOperationContext(CompositeOperation compositeOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new CompositeOperationContext(compositeOperation, e, viewController) );
	}
	/*package-private*/ static DialogOperationContext createAndPushDialogOperationContext(DialogOperation dialogOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new DialogOperationContext(dialogOperation, e, viewController) );
	}
	/*package-private*/ static <J extends Component<?>> InformationDialogOperationContext<J> createAndPushInformationDialogOperationContext(InformationDialogOperation<J> informationDialogOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new InformationDialogOperationContext<J>(informationDialogOperation, e, viewController) );
	}
	/*package-private*/ static <J extends JComponent< ? >> InputDialogOperationContext<J> createAndPushInputDialogOperationContext(InputDialogOperation<J> inputDialogOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new InputDialogOperationContext<J>(inputDialogOperation, e, viewController) );
	}
	/*package-private*/ static WizardDialogOperationContext createAndPushWizardDialogOperationContext(WizardDialogOperation wizardDialogOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new WizardDialogOperationContext(wizardDialogOperation, e, viewController) );
	}
	
	/*package-private*/ static PopupMenuOperationContext createAndPushPopupMenuOperationContext(PopupMenuOperation popupMenuOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new PopupMenuOperationContext(popupMenuOperation, e, viewController) );
	}
	/*package-private*/ static <T> CascadeOperationContext< T > createAndPushCascadeOperationContext(CascadeOperation< T > cascadeOperation, java.util.EventObject e, ViewController<?, ?> viewController) {
		return pushContext( new CascadeOperationContext<T>(cascadeOperation, e, viewController) );
	}
	/*package-private*/ static <B> CascadeBlankContext<B> createCascadeBlankContext(CascadeBlank<B> cascadeBlank ) {
		return new CascadeBlankContext<B>(cascadeBlank, null, null );
	}
	/*package-private*/ static <F,B> CascadeFillInContext< F,B > createCascadeFillInContext( CascadeFillIn< F,B > cascadeFillIn ) {
		return new CascadeFillInContext<F,B>(cascadeFillIn, null, null );
	}
	/*package-private*/ static <F> CascadeRootContext<F> createCascadeRootContext( CascadeRoot<F> cascadeRoot ) {
		return new CascadeRootContext<F>(cascadeRoot, null, null );
	}
	/*package-private*/ static <FB> CascadeMenuContext<FB> createCascadeMenuContext( CascadeMenu<FB> cascadeMenu ) {
		return new CascadeMenuContext<FB>(cascadeMenu, null, null );
	}
	/*package-private*/ static <F,B> CascadeSeparatorContext createCascadeSeparatorContext( CascadeSeparator cascadeSeparator ) {
		return new CascadeSeparatorContext(cascadeSeparator, null, null );
	}
	/*package-private*/ static <F> CascadeCancelContext<F> createCascadeCancelContext( CascadeCancel<F> cascadeCancel ) {
		return new CascadeCancelContext<F>(cascadeCancel, null, null );
	}
	
	/*package-private*/ static <E> ListSelectionStateContext<E> createAndPushItemSelectionStateContext(ListSelectionState<E> itemSelectionState, java.util.EventObject e, ViewController<?, ?> viewController/*, int prevIndex, E prevItem, int nextIndex, E nextItem*/) {
		AbstractModelContext< ? > currentContext = getCurrentContext();
		if( currentContext instanceof ListSelectionStateContext ) {
			return (ListSelectionStateContext<E>)currentContext;
		} else {
			return pushContext( new ListSelectionStateContext<E>( itemSelectionState, e, viewController /*, prevIndex, prevItem, nextIndex, nextItem*/ ) );
		}
	}
	/*package-private*/ static <E> void addListSelectionPopupMenuWillBecomeVisible( ListSelectionState<E> itemSelectionState, javax.swing.event.PopupMenuEvent e, ItemSelectable< ?, ? > itemSelectable ) {
		ListSelectionStateContext<E> listSelectionStateContext = createAndPushItemSelectionStateContext( itemSelectionState, e, itemSelectable );
		listSelectionStateContext.handlePopupMenuWillBecomeVisibleEvent( e );
		
	}
	/*package-private*/ static <E> void addListSelectionPopupMenuWillBecomeInvisible( ListSelectionState<E> itemSelectionState, javax.swing.event.PopupMenuEvent e, ItemSelectable< ?, ? > itemSelectable ) {
	}
	/*package-private*/ static <E> void addListSelectionPopupMenuCanceled( ListSelectionState<E> itemSelectionState, javax.swing.event.PopupMenuEvent e, ItemSelectable< ?, ? > itemSelectable ) {
	}

	/*package-private*/ static BoundedRangeIntegerStateContext createAndPushBoundedRangeIntegerStateContext(BoundedRangeIntegerState boundedRangeIntegerState) {
		return pushContext( new BoundedRangeIntegerStateContext(boundedRangeIntegerState, null, null) );
	}
	/*package-private*/ static BooleanStateContext createAndPushBooleanStateContext(BooleanState booleanState, java.awt.event.ItemEvent e, ViewController<?, ?> viewController) {
		return pushContext( new BooleanStateContext( booleanState, e, viewController ) );
	}
	/*package-private*/ static MenuBarModelContext createAndPushMenuBarModelContext(MenuBarModel menuBarModel, javax.swing.event.ChangeEvent e, MenuBar menuBar) {
		return pushContext( new MenuBarModelContext(menuBarModel, e, menuBar) );
	}
	/*package-private*/ static MenuModelContext createAndPushMenuModelContext(MenuModel menuModel, MenuItemContainer menuItemContainer) {
		return pushContext( new MenuModelContext(menuModel, null, menuItemContainer.getViewController() ) );
	}
	/*package-private*/ static DragAndDropContext createAndPushDragAndDropContext(DragAndDropModel dragAndDropOperation, java.awt.event.MouseEvent originalMouseEvent, java.awt.event.MouseEvent latestMouseEvent, DragComponent dragSource) {
		return pushContext( new DragAndDropContext(dragAndDropOperation, originalMouseEvent, latestMouseEvent, dragSource) );
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
	private static MenuBarModel getMenuBarModelOrigin( javax.swing.MenuElement[] menuElements ) {
		MenuBar menuBar = getMenuBarOrigin( menuElements );
		if( menuBar != null ) {
			return menuBar.getModel();
		} else {
			return null;
		}
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
	
	private static javax.swing.MenuElement[] previousMenuElements = {};
	private static void handleMenuSelectionStateChanged( javax.swing.event.ChangeEvent e ) {
		javax.swing.MenuElement[] menuElements = javax.swing.MenuSelectionManager.defaultManager().getSelectedPath();
		if( isCroquetMenuSelection( menuElements ) ) {
			if( previousMenuElements.length > 0 ) {
				if( menuElements.length > 0 ) {
					java.util.List< Model > models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
					MenuBar menuBar = getMenuBarOrigin( menuElements );
					int i0;
					if( menuBar != null ) {
						models.add( menuBar.getModel() );
						javax.swing.JPopupMenu jPreviousPopupMenu;
						if( previousMenuElements.length >= 3 ) {
							jPreviousPopupMenu = (javax.swing.JPopupMenu)previousMenuElements[ 2 ];
						} else {
							jPreviousPopupMenu = null;
						}
						
						assert menuElements.length >= 3;
						assert menuElements[ 1 ] instanceof javax.swing.JMenu;
						assert menuElements[ 2 ] instanceof javax.swing.JPopupMenu;
						javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElements[ 2 ];
						
						javax.swing.JMenu jMenu = (javax.swing.JMenu)menuElements[ 1 ];
						Menu menu = (Menu)Component.lookup( jMenu );
						assert menu != null;

						Model menuModel = menu.getModel();
						assert menuModel != null;
						models.add( menuModel );

						if( jPreviousPopupMenu != jPopupMenu ) {
							if( jPreviousPopupMenu != null ) {
								AbstractModelContext< ? > popupMenuOperationContext = ContextManager.popContext();
								assert popupMenuOperationContext instanceof PopupMenuOperationContext;
							}
							
							if( menuModel instanceof MenuModel ) {
								/*PopupMenuOperationContext popupMenuOperationContext =*/ ContextManager.createAndPushPopupMenuOperationContext( ((MenuModel)menuModel).getPopupMenuOperation(), e, null );
							} else {
								System.err.println( "handleMenuSelectionStateChanged: " + menuModel );
							}
							
						}
						i0 = 3;
					} else {
						i0 = 0;
					}
					for( int i=i0; i<menuElements.length; i++ ) {
						javax.swing.MenuElement menuElementI = menuElements[ i ];
						if( menuElementI instanceof javax.swing.JPopupMenu ) {
							javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElementI;
							//pass
						} else if( menuElementI instanceof javax.swing.JMenuItem ) {
							javax.swing.JMenuItem jMenuItem = (javax.swing.JMenuItem)menuElementI;
							Component< ? > component = Component.lookup( jMenuItem );
							if( component instanceof ViewController< ?, ? > ) {
								ViewController< ?, ? > viewController = (ViewController< ?, ? >)component;
								models.add( viewController.getModel() );
							}
						}
					}
					AbstractModelContext< ? > modelContext = ContextManager.getCurrentContext();
					if( modelContext instanceof PopupMenuOperationContext ) {
						PopupMenuOperationContext popupMenuOperationContext = (PopupMenuOperationContext)modelContext;
						popupMenuOperationContext.handleMenuSelectionChanged( e, models );
					} else {
						System.err.println( "WARNING: handleMenuSelectionStateChanged not PopupMenuOperationContext " + modelContext );
					}
				} else {
					MenuBarModel menuBarModel = getMenuBarModelOrigin( previousMenuElements );
					if( menuBarModel != null ) {
						AbstractModelContext< ? > popupMenuOperationContext = ContextManager.popContext();
						assert popupMenuOperationContext instanceof PopupMenuOperationContext;

						AbstractModelContext< ? > menuBarContext = ContextManager.popContext();
						assert menuBarContext instanceof MenuBarModelContext;
					}
				}
			} else {
				if( menuElements.length > 0 ) {
					MenuBar menuBar = getMenuBarOrigin( menuElements );
					if( menuBar != null ) {
						/*MenuBarModelContext childContext =*/ ContextManager.createAndPushMenuBarModelContext( menuBar.getModel(), e, menuBar );
						assert menuElements.length == 2;
					} else {
						AbstractModelContext< ? > modelContext = ContextManager.getCurrentContext();
						if( modelContext instanceof PopupMenuOperationContext ) {
							//pass
						} else {
							System.err.println( "combo box? " + menuElements.length + " " + java.util.Arrays.toString( menuElements ) );
							System.err.println( "modelContext: " + modelContext );
						}
					}
				} else {
					//assert false;
					AbstractModelContext< ? > modelContext = ContextManager.getCurrentContext();
					System.err.println( "both prev and current menu selection length 0" );
					System.err.println( "modelContext: " + modelContext );
				}
			}
			previousMenuElements = menuElements;
		} else {
			System.err.println( "warning: not croquet menu selection." );
		}
	}
	private static javax.swing.event.ChangeListener menuSelectionChangeListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			handleMenuSelectionStateChanged( e );
		}
	};
	public static void startListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().addChangeListener( menuSelectionChangeListener );
	}
	public static void stopListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().removeChangeListener( menuSelectionChangeListener );
	}
	
//	public static <A extends Application> A createAndShowApplication( Class<A> cls, String[] args ) throws IllegalAccessException, InstantiationException {
//		A application = cls.newInstance();
//		//application.initialize( args );
//		//application.getFrame().setVisible( true );
//		return application;
//	}
	
	public static MenuBarModelContext createContextFor( java.util.List<Model> path, AbstractModelContext<?> descendantContext ) {
		final int N = path.size();
		assert N >= 3;
		MenuBarModel menuBarModel = (MenuBarModel)path.get( 0 );
		MenuModel menuModel = (MenuModel)path.get( 1 );
		PopupMenuOperation popupMenuOperation = menuModel.getPopupMenuOperation();
		MenuBarModelContext rv = new MenuBarModelContext(menuBarModel, null, null);
		PopupMenuOperationContext popupMenuOperationContext = new PopupMenuOperationContext( popupMenuOperation, null, null );
		rv.addChild( popupMenuOperationContext );
		popupMenuOperationContext.handleMenuSelectionChanged( null, path );
		popupMenuOperationContext.addChild( descendantContext );
		return rv;
	}
	
	public static <E> ListSelectionStateContext< E > createContextFor( ListSelectionState< E > listSelectionState, E nextValue ) {
		ListSelectionStateContext< E > rv = new ListSelectionStateContext<E>( listSelectionState, null, null );
		CommitEvent commitEvent = new CommitEvent( new ListSelectionStateEdit< E >( null, listSelectionState.getValue(), nextValue ) );
		rv.addChild( commitEvent );
		return rv;
	}
	
	
	
	private static java.util.Map< java.util.UUID, java.util.Set< Model > > mapIdToModels = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static java.util.Set< Model > lookupModels( java.util.UUID id ) {
		synchronized ( mapIdToModels ) {
			return mapIdToModels.get( id );
		}
	}
	@Deprecated
	public static Model findFirstAppropriateModel( java.util.UUID id ) {
		java.util.Set< Model > models = lookupModels( id );
		for( Model model : models ) {
			for( JComponent<?> component : model.getComponents() ) {
				if( component.getAwtComponent().isShowing() ) {
					return model;
				}
			}
			for( JComponent<?> component : model.getComponents() ) {
				if( component.getAwtComponent().isVisible() ) {
					return model;
				}
			}
		}
		return null;
	}

	/*package-private*/ static void registerModel( Model model ) {
		java.util.UUID id = model.getId();
		synchronized ( mapIdToModels ) {
			java.util.Set< Model > set = mapIdToModels.get( id );
			if( set != null ) {
				//pass
			} else {
				set = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
				mapIdToModels.put( id, set );
			}
			set.add( model );
		}
	}
	/*package-private*/ static void unregisterModel( Model model ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "unregister:", model );
		java.util.UUID id = model.getId();
		synchronized ( mapIdToModels ) {
			java.util.Set< Model > set = mapIdToModels.get( id );
			if( set != null ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "pre set size:", set.size() );
				set.remove( model );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "post set size:", set.size() );
				if( set.size() == 0 ) {
					mapIdToModels.remove( id );
				}
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate unregister" );
			}
		}
	}
	

	/*package-private*/ static void localizeAllModels() {
		synchronized ( mapIdToModels ) {
			java.util.Collection< java.util.Set< Model > > sets = mapIdToModels.values();
			for( java.util.Set< Model > set : sets ) {
				for( Model model : set ) {
					model.localize();
//					for( JComponent<?> component : model.getComponents() ) {
//					}
				}
			}
		}
	}
	
	private static int isUndoOrRedoCount = 0;
	public static boolean isInTheMidstOfUndoOrRedo() {
		return isUndoOrRedoCount > 0;
	}
	public static void pushUndoOrRedo() {
		isUndoOrRedoCount ++;
	}
	public static void popUndoOrRedo() {
		isUndoOrRedoCount --;
	}
}
