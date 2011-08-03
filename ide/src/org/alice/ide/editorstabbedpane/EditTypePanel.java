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

package org.alice.ide.editorstabbedpane;

/**
 * @author Dennis Cosgrove
 */
public class EditTypePanel extends org.lgna.croquet.components.BorderPanel {
	interface MemberFilter< M extends org.lgna.project.ast.MemberDeclaredInAlice > {
		public boolean isAcceptable( M member );
	}
	interface MethodFilter extends MemberFilter< org.lgna.project.ast.MethodDeclaredInAlice > {
	}
	interface FieldFilter extends MemberFilter< org.lgna.project.ast.FieldDeclaredInAlice > {
	}
	
	class TypeMembersListModel< M extends org.lgna.project.ast.MemberDeclaredInAlice > extends edu.cmu.cs.dennisc.javax.swing.models.AbstractReorderableListModel< M > { //javax.swing.DefaultListModel { //javax.swing.AbstractListModel implements javax.swing.MutableComboBoxModel {
		private java.util.List< M > list = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		private edu.cmu.cs.dennisc.property.event.ListPropertyListener listPropertyListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener() {
			public void adding(edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e) {
			}
			public void added(edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e) {
				refresh();
			}
			public void clearing(edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e) {
			}
			public void cleared(edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e) {
				refresh();
			}
			public void removing(edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e) {
			}
			public void removed(edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e) {
				refresh();
			}
			public void setting(edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e) {
			}
			public void set(edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e) {
				refresh();
			}
		};
		private org.lgna.project.ast.TypeDeclaredInAlice type;
		private MemberFilter<M> memberFilter;
		private Class<M> cls;
		public TypeMembersListModel( org.lgna.project.ast.TypeDeclaredInAlice type, MemberFilter<M> memberFilter, Class<M> cls ) {
			this.type = type;
			this.memberFilter = memberFilter;
			this.cls = cls;
			if (this.cls.isAssignableFrom(org.lgna.project.ast.MethodDeclaredInAlice.class)) {
				this.type.methods.addListPropertyListener(this.listPropertyListener);
			}
			if (this.cls.isAssignableFrom(org.lgna.project.ast.FieldDeclaredInAlice.class)) {
				this.type.fields.addListPropertyListener(this.listPropertyListener);
			}
			this.refresh();
		}
		private void refresh() {
			this.list.clear();
			if (cls.isAssignableFrom(org.lgna.project.ast.MethodDeclaredInAlice.class)) {
				for (org.lgna.project.ast.MethodDeclaredInAlice method : this.type.methods) {
					M m = cls.cast(method);
					if (memberFilter.isAcceptable(m)) {
						this.list.add(m);
					}
				}
			}
			if (cls.isAssignableFrom(org.lgna.project.ast.FieldDeclaredInAlice.class)) {
				for (org.lgna.project.ast.FieldDeclaredInAlice field : this.type.fields) {
					M m = cls.cast(field);
					if (memberFilter.isAcceptable(m)) {
						this.list.add(m);
					}
				}
			}
			this.fireContentsChanged( this, 0, this.list.size() );
		}
		public void swap( int indexA, int indexB ) {
			M memberA = this.list.get( indexA );
			M memberB = this.list.get( indexB );
			this.list.set( indexA, memberB );
			this.list.set( indexB, memberA );
			this.fireContentsChanged( this, indexA, indexB );
		}
		public void slide( int prevIndex, int nextIndex ) {
			M member = this.list.get( prevIndex );
			if( prevIndex < nextIndex ) {
				for( int i=prevIndex; i<nextIndex; i++ ) {
					this.list.set( i, this.list.get( i+1 ) );
				}
			} else {
				int i = prevIndex-1;
				while( i >= nextIndex ) {
					this.list.set( i+1, this.list.get( i ) );
					i--;
				}
			}
			this.list.set( nextIndex, member );
			this.fireContentsChanged( this, prevIndex, nextIndex );
		}
		public Object getElementAt( int index ) {
			return this.list.get( index );
		}
		public int getSize() {
			return this.list.size();
		}
		
		
	}
	class TypeMethodsListModel extends TypeMembersListModel< org.lgna.project.ast.MethodDeclaredInAlice > {
		public TypeMethodsListModel( org.lgna.project.ast.TypeDeclaredInAlice type, MemberFilter<org.lgna.project.ast.MethodDeclaredInAlice> methodFilter ) {
			super( type, methodFilter, org.lgna.project.ast.MethodDeclaredInAlice.class );
		}
	}
	class TypeFieldsListModel extends TypeMembersListModel< org.lgna.project.ast.FieldDeclaredInAlice > {
		public TypeFieldsListModel( org.lgna.project.ast.TypeDeclaredInAlice type, MemberFilter<org.lgna.project.ast.FieldDeclaredInAlice> fieldFilter ) {
			super( type, fieldFilter, org.lgna.project.ast.FieldDeclaredInAlice.class );
		}
	}
	
	private static final MethodFilter ALL_PROCEDURES_FILTER = new MethodFilter() { 
		public boolean isAcceptable( org.lgna.project.ast.MethodDeclaredInAlice method ) {
			return method.isProcedure();
		}
	};
	private static final MethodFilter ALL_FUNCTIONS_FILTER = new MethodFilter() { 
		public boolean isAcceptable( org.lgna.project.ast.MethodDeclaredInAlice method ) {
			return method.isFunction();
		}
	};
	private static final FieldFilter ALL_FIELDS_FILTER = new FieldFilter() { 
		public boolean isAcceptable( org.lgna.project.ast.FieldDeclaredInAlice field ) {
			return true;
		}
	};
	
	
	static class IndexAsStringSelectionUtilities { 
		public static java.awt.datatransfer.StringSelection createStringSelection( int index ) {
			return new java.awt.datatransfer.StringSelection( Integer.toString( index ) );
		}
		public static Integer getIndex( java.awt.datatransfer.Transferable transferable ) {
			try {
				String listIndexAsString = (String) transferable.getTransferData( java.awt.datatransfer.DataFlavor.stringFlavor );
				return Integer.parseInt( listIndexAsString );
			} catch (Exception e) {
				return null;
			}
		}
//		public static Integer getIndex( javax.swing.TransferHandler.TransferSupport transferSupport ) {
//			java.awt.datatransfer.Transferable transferable = transferSupport.getTransferable();
//			return getIndex( transferable );
//		}
	}
	
	static class ListIndexAsStringTransferHandler extends javax.swing.TransferHandler {
		@Override
		public boolean importData( javax.swing.JComponent comp, java.awt.datatransfer.Transferable t ) {
			Integer index = IndexAsStringSelectionUtilities.getIndex( t );
			return index != null;
		}
		@Override
		public boolean canImport( javax.swing.JComponent comp, java.awt.datatransfer.DataFlavor[] transferFlavors ) {
			return true;
		}
	}

	static class ReorderableList extends org.lgna.croquet.components.JComponent< javax.swing.JList > {
		@Override
		protected javax.swing.JList createAwtComponent() {
			final javax.swing.JList rv = new javax.swing.JList();
			rv.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
			rv.setDragEnabled( true );
			try {
				String[] versionTexts = System.getProperty( "java.version" ).split( "\\." );
				if( versionTexts.length > 1 ) { 
					int majorVersion = Integer.parseInt( versionTexts[ 0 ] );
					int minorVersion = Integer.parseInt( versionTexts[ 1 ] );
					if( majorVersion > 1 || ( majorVersion == 1 && minorVersion > 5 ) ) {
						//rv.setDropMode( javax.swing.DropMode.INSERT );
						Class<Enum<?>> dropModeClass = (Class<Enum<?>>)Class.forName( "javax.swing.DropMode" );
						java.lang.reflect.Method setDropModeMethod = javax.swing.JList.class.getMethod( "setDropMode", dropModeClass );
						java.lang.reflect.Field insertField = dropModeClass.getField( "INSERT" );
						Object insertConstant = insertField.get( null );
						setDropModeMethod.invoke(rv, insertConstant );
					}
				}
			} catch( Exception e ) {
				e.printStackTrace();
			}
			final java.awt.dnd.DragSource dragSource = new java.awt.dnd.DragSource();
			final javax.swing.ListSelectionModel listSelectionModel = rv.getSelectionModel();
			java.awt.dnd.DragGestureListener dragGestureListener = new java.awt.dnd.DragGestureListener() {
				public void dragGestureRecognized( java.awt.dnd.DragGestureEvent dge ) {
					int prevIndex = listSelectionModel.getMinSelectionIndex();
					java.awt.datatransfer.StringSelection stringSelection = IndexAsStringSelectionUtilities.createStringSelection( prevIndex );
					dragSource.startDrag( dge, java.awt.dnd.DragSource.DefaultMoveDrop, stringSelection, new java.awt.dnd.DragSourceListener() {
						public void dragDropEnd( java.awt.dnd.DragSourceDropEvent dsde ) {
							if( dsde.getDropSuccess() ) {
								
								//1.6
								//getDropLocation()
								
								java.awt.Point dropLocation = dsde.getLocation();
								java.awt.Point listLocation = rv.getLocationOnScreen();
								java.awt.Point p = new java.awt.Point( dropLocation.x-listLocation.x, dropLocation.y-listLocation.y );
								
								javax.swing.plaf.ListUI listUI = rv.getUI();
								int nextIndex = listUI.locationToIndex( rv, p );
								java.awt.Rectangle cellBounds = listUI.getCellBounds( rv, nextIndex, nextIndex );
								if( p.y > cellBounds.getCenterY() ) {
									nextIndex += 1;
								}

								int prevIndex = IndexAsStringSelectionUtilities.getIndex( dsde.getDragSourceContext().getTransferable() );
								
								if( prevIndex != nextIndex ) {
									if( prevIndex < nextIndex ) {
										nextIndex --;
									}
									javax.swing.ListModel listModel = rv.getModel();
									if( listModel instanceof edu.cmu.cs.dennisc.javax.swing.models.ReorderableListModel< ? > ) {
										edu.cmu.cs.dennisc.javax.swing.models.ReorderableListModel< ? > reorderableListModel = (edu.cmu.cs.dennisc.javax.swing.models.ReorderableListModel< ? >)rv.getModel();
										reorderableListModel.slide( prevIndex, nextIndex );
									} else if( listModel instanceof javax.swing.DefaultListModel ) {
										javax.swing.DefaultListModel defaultListModel = (javax.swing.DefaultListModel)rv.getModel();
										Object item = rv.getModel().getElementAt( prevIndex );
										
										defaultListModel.remove( prevIndex );
										defaultListModel.add( nextIndex, item );
									} else {
										throw new RuntimeException();
									}
									rv.setSelectedIndex( nextIndex );
								}
							}
						}
						public void dragEnter( java.awt.dnd.DragSourceDragEvent dsde ) {
						}
						public void dragExit( java.awt.dnd.DragSourceEvent dse ) {
						}
						public void dragOver( java.awt.dnd.DragSourceDragEvent dsde ) {
						}
						public void dropActionChanged( java.awt.dnd.DragSourceDragEvent dsde ) {
						}
					} );
				}
			};
			
			dragSource.createDefaultDragGestureRecognizer( rv, java.awt.dnd.DnDConstants.ACTION_MOVE, dragGestureListener );
			
			ListIndexAsStringTransferHandler listIndexAsStringTransferHandler = new ListIndexAsStringTransferHandler();
			rv.setTransferHandler( listIndexAsStringTransferHandler );
			return rv;
		}
	}
	
	static abstract class EditableList<E> extends ReorderableList {
		protected abstract org.lgna.croquet.Operation<?> getDoubleClickOperation( E item );
		protected abstract org.lgna.croquet.Operation<?> getPopupTriggerOperation( E item );
		
		private edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter lenientMouseClickAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
			private E getItem( java.awt.event.MouseEvent e ) {
				javax.swing.JList jList = EditableList.this.getAwtComponent();
				int index = jList.locationToIndex( e.getPoint() );
				if( index != -1 ) {
					javax.swing.ListModel listModel = jList.getModel();
					if( index < listModel.getSize() ) {
						return (E)listModel.getElementAt( index );
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
			@Override
			protected void mouseQuoteClickedUnquote(java.awt.event.MouseEvent e, int quoteClickCountUnquote) {
				if( e.isPopupTrigger() ) {
					if( quoteClickCountUnquote == 1 ) {
						E item = this.getItem( e );
						if( item != null ) {
							org.lgna.croquet.Operation<?> operation = getPopupTriggerOperation( item );
							if( operation != null ) {
								operation.fire( e );
							}
						}
					}
				} else {
					if( quoteClickCountUnquote == 2 ) {
						E item = this.getItem( e );
						if( item != null ) {
							org.lgna.croquet.Operation<?> operation = getDoubleClickOperation( item );
							if( operation != null ) {
								operation.fire( e );
							}
						}
					}
				}
				edu.cmu.cs.dennisc.print.PrintUtilities.println( e, quoteClickCountUnquote );
			}
		};
		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			this.addMouseListener( this.lenientMouseClickAdapter );
			this.addMouseMotionListener( this.lenientMouseClickAdapter );
		}
		@Override
		protected void handleUndisplayable() {
			this.removeMouseMotionListener( this.lenientMouseClickAdapter );
			this.removeMouseListener( this.lenientMouseClickAdapter );
			super.handleUndisplayable();
		}
	}
	
	static class MethodList extends EditableList< org.lgna.project.ast.MethodDeclaredInAlice > {
		@Override
		protected org.lgna.croquet.Operation<?> getDoubleClickOperation(org.lgna.project.ast.MethodDeclaredInAlice item) {
			if( item.isSignatureLocked.getValue() ) {
				org.lgna.croquet.Application.getActiveInstance().showMessageDialog( item.getName() + " is locked and therefore cannot be renamed." );
				return null;
			} else {
				return org.alice.ide.croquet.models.ast.rename.RenameMethodOperation.getInstance( item );
			}
		}
		@Override
		protected org.lgna.croquet.Operation<?> getPopupTriggerOperation(org.lgna.project.ast.MethodDeclaredInAlice item) {
			return null;
		}
	}
	static class FieldList extends EditableList< org.lgna.project.ast.FieldDeclaredInAlice > {
		@Override
		protected org.lgna.croquet.Operation<?> getDoubleClickOperation(org.lgna.project.ast.FieldDeclaredInAlice item) {
			org.lgna.project.ast.AbstractType<?,?,?> valueType = item.getValueType();
			if( valueType.isAssignableTo( org.lgna.story.CameraMarker.class ) ) {
				org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "Currently, camera markers cannot be renamed." );
				return null;
			} else {
				return new org.alice.ide.operations.ast.EditFieldOperation( item );
			}
		}
		@Override
		protected org.lgna.croquet.Operation<?> getPopupTriggerOperation(org.lgna.project.ast.FieldDeclaredInAlice item) {
			return null;
		}
	}
	
	
	class Title extends org.lgna.croquet.components.FlowPanel {
		public Title( org.lgna.project.ast.TypeDeclaredInAlice type ) {
			super( Alignment.LEADING );
			this.addComponent( new org.lgna.croquet.components.Label( "class ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ) );
			this.addComponent( org.alice.ide.common.TypeComponent.createInstance( type ) );
			this.addComponent( new org.lgna.croquet.components.Label( " extends ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ) );
			this.addComponent( org.alice.ide.common.TypeComponent.createInstance( type.getSuperType() ) );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,0,0,8 ) );
		}
	}

	private static org.lgna.croquet.components.Container< ? > createMembersPanel( ReorderableList list, org.lgna.croquet.InputDialogOperation operation ) {
		org.lgna.croquet.components.PageAxisPanel rv = new org.lgna.croquet.components.PageAxisPanel();
		rv.addComponent( list );
		rv.addComponent( operation.createButton() );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 16, 4, 0 ) );
		return rv;
	}

	private int historyIndex;
	public EditTypePanel( org.lgna.project.ast.TypeDeclaredInAlice type, int historyIndex ) {
		this.historyIndex = historyIndex;
		javax.swing.ListCellRenderer procedureListCellRenderer = new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< org.lgna.project.ast.MethodDeclaredInAlice >() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, org.lgna.project.ast.MethodDeclaredInAlice value, int index, boolean isSelected, boolean cellHasFocus ) {
				rv.setText( value.getName() );
				return rv;
			}
		};
		javax.swing.ListCellRenderer functionListCellRenderer = new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< org.lgna.project.ast.MethodDeclaredInAlice >() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, org.lgna.project.ast.MethodDeclaredInAlice value, int index, boolean isSelected, boolean cellHasFocus ) {
				rv.setText( value.getName() );
				rv.setIcon( org.alice.ide.common.TypeIcon.getInstance( value.getReturnType() ) );
				return rv;
			}
		};
		javax.swing.ListCellRenderer fieldListCellRenderer = new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< org.lgna.project.ast.FieldDeclaredInAlice >() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, org.lgna.project.ast.FieldDeclaredInAlice value, int index, boolean isSelected, boolean cellHasFocus ) {
				rv.setText( value.getName() );
				rv.setIcon( org.alice.ide.common.TypeIcon.getInstance( value.getValueType() ) );
				return rv;
			}
		};
		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		MethodList proceduresList = new MethodList();
		MethodList functionsList = new MethodList();
		FieldList fieldsList = new FieldList();

		proceduresList.getAwtComponent().setModel( new TypeMethodsListModel( type, ALL_PROCEDURES_FILTER ) );
		functionsList.getAwtComponent().setModel( new TypeMethodsListModel( type, ALL_FUNCTIONS_FILTER ) );
		fieldsList.getAwtComponent().setModel( new TypeFieldsListModel( type, ALL_FIELDS_FILTER ) );

		proceduresList.getAwtComponent().setCellRenderer( procedureListCellRenderer );
		functionsList.getAwtComponent().setCellRenderer( functionListCellRenderer );
		fieldsList.getAwtComponent().setCellRenderer( fieldListCellRenderer );

		proceduresList.setBackgroundColor( null );
		functionsList.setBackgroundColor( null );
		fieldsList.setBackgroundColor( null );

		org.lgna.croquet.BooleanState proceduresToolPaletteState = new org.lgna.croquet.BooleanState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "4236c740-8881-4cf1-82e3-e3aef61c13dd" ), true ) {};
		proceduresToolPaletteState.setTextForBothTrueAndFalse( "procedures" );
		org.lgna.croquet.components.ToolPalette proceduresToolPalette = proceduresToolPaletteState.createToolPalette( createMembersPanel( proceduresList, org.alice.ide.croquet.models.declaration.ProcedureDeclarationOperation.getInstance( type ) ) );
		proceduresToolPalette.setBackgroundColor( ide.getTheme().getProcedureColor() );

		org.lgna.croquet.BooleanState functionsToolPaletteState = new org.lgna.croquet.BooleanState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "ea7e601f-255b-41aa-bccd-af181c6b3bf0" ), true ) {};
		functionsToolPaletteState.setTextForBothTrueAndFalse( "functions" );
		org.lgna.croquet.components.ToolPalette functionsToolPalette = functionsToolPaletteState.createToolPalette( createMembersPanel( functionsList, org.alice.ide.croquet.models.declaration.FunctionDeclarationOperation.getInstance( type ) ) );
		functionsToolPalette.setBackgroundColor( ide.getTheme().getFunctionColor() );

		org.lgna.croquet.BooleanState fieldsToolPaletteState = new org.lgna.croquet.BooleanState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "7176c895-4e0f-4ebe-98a2-f820b27c9206" ), true ) {};
		fieldsToolPaletteState.setTextForBothTrueAndFalse( "properties" );
		org.lgna.croquet.components.ToolPalette fieldsToolPalette = fieldsToolPaletteState.createToolPalette( createMembersPanel( fieldsList, org.alice.ide.operations.ast.DeclareFieldOperation.getInstance( type ) ) );
		fieldsToolPalette.setBackgroundColor( ide.getTheme().getFieldColor() );

		proceduresToolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		functionsToolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		fieldsToolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );

		org.lgna.croquet.components.PageAxisPanel pageAxisPanel = new org.lgna.croquet.components.PageAxisPanel();
		pageAxisPanel.addComponent( proceduresToolPalette );
		pageAxisPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 4 ) );
		pageAxisPanel.addComponent( functionsToolPalette );
		pageAxisPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 4 ) );
		pageAxisPanel.addComponent( fieldsToolPalette );
//		pageAxisPanel.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue() );
		pageAxisPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,14,0,0 ) );

		org.lgna.croquet.components.BorderPanel borderPanel = new org.lgna.croquet.components.BorderPanel();
		borderPanel.addComponent( pageAxisPanel, Constraint.PAGE_START );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( borderPanel );
		scrollPane.setBorder( null );
		scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
		
		Title title = new Title( type );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		this.addComponent( title, Constraint.PAGE_START );
		this.addComponent( scrollPane, Constraint.CENTER );

//		for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( this.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.DESCENDANTS_ONLY, javax.swing.JComponent.class ) ) {
//			edu.cmu.cs.dennisc.java.awt.FontUtilities.setFontToScaledFont( component, 1.5f );
//		}
		for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( title.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.DESCENDANTS_ONLY, javax.swing.JComponent.class ) ) {
			edu.cmu.cs.dennisc.java.awt.FontUtilities.setFontToScaledFont( component, 1.2f );
		}
	}

	public int getHistoryIndex() {
		return this.historyIndex;
	}
}
