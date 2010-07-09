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
public class EditTypePanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	interface MemberFilter< M extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice > {
		public boolean isAcceptable( M member );
	}
	interface MethodFilter extends MemberFilter< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
	}
	interface FieldFilter extends MemberFilter< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
	}
	
	class TypeMembersListModel< M extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice > extends edu.cmu.cs.dennisc.javax.swing.models.AbstractReorderableListModel< M > { //javax.swing.DefaultListModel { //javax.swing.AbstractListModel implements javax.swing.MutableComboBoxModel {
		private java.util.List< M > list = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
		public TypeMembersListModel( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, MemberFilter<M> memberFilter, Class<M> cls ) {
			 if( cls.isAssignableFrom( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class ) ) {
				 for( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method : type.methods ) {
					 M m = cls.cast( method );
					 if( memberFilter.isAcceptable( m ) ) {
						 this.list.add( m );
					 }
				 }
			 }
			 if( cls.isAssignableFrom( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice.class ) ) {
				 for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field : type.fields ) {
					 M m = cls.cast( field );
					 if( memberFilter.isAcceptable( m ) ) {
						 this.list.add( m );
					 }
				 }
			 }
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
	class TypeMethodsListModel extends TypeMembersListModel< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > {
		public TypeMethodsListModel( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, MemberFilter<edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice> methodFilter ) {
			super( type, methodFilter, edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
		}
	}
	class TypeFieldsListModel extends TypeMembersListModel< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > {
		public TypeFieldsListModel( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, MemberFilter<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice> fieldFilter ) {
			super( type, fieldFilter, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice.class );
		}
	}
	
	private static final MethodFilter ALL_PROCEDURES_FILTER = new MethodFilter() { 
		public boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
			return method.isProcedure();
		}
	};
	private static final MethodFilter ALL_FUNCTIONS_FILTER = new MethodFilter() { 
		public boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
			return method.isFunction();
		}
	};
	private static final FieldFilter ALL_FIELDS_FILTER = new FieldFilter() { 
		public boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
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

	static class ReorderableList extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JList > {
		@Override
		protected javax.swing.JList createAwtComponent() {
			final javax.swing.JList rv = new javax.swing.JList();
			rv.setAlignmentX( 0.0f );
			rv.setDragEnabled( true );

			try {
				Class<Enum<?>> dropModeClass = (Class<Enum<?>>)Class.forName( "javax.swing.DropMode" );
				java.lang.reflect.Method setDropModeMethod = javax.swing.JList.class.getMethod( "setDropMode", dropModeClass );
				java.lang.reflect.Field insertField = dropModeClass.getField( "INSERT" );
				Object insertConstant = insertField.get( null );
				setDropModeMethod.invoke(rv, insertConstant );
				//1.6
				//rv.setDropMode( javax.swing.DropMode.INSERT );
			} catch( Exception e ) {
				//pass
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
	
	class Title extends edu.cmu.cs.dennisc.croquet.FlowPanel {
		public Title( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
			super( Alignment.LEADING );
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "class ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ) );
			this.addComponent( org.alice.ide.common.TypeComponent.createInstance( type ) );
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( " extends ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ) );
			this.addComponent( org.alice.ide.common.TypeComponent.createInstance( type.getSuperType() ) );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,0,0,8 ) );
		}
	}

	private static edu.cmu.cs.dennisc.croquet.Container< ? > createMembersPanel( ReorderableList list, edu.cmu.cs.dennisc.croquet.InputDialogOperation operation ) {
		edu.cmu.cs.dennisc.croquet.PageAxisPanel rv = new edu.cmu.cs.dennisc.croquet.PageAxisPanel();
		rv.addComponent( list );
		rv.addComponent( operation.createButton() );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 16, 4, 0 ) );
		return rv;
	}

	public EditTypePanel( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		javax.swing.ListCellRenderer nodeListCellRenderer = new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< edu.cmu.cs.dennisc.alice.ast.Node >() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.Node value, int index, boolean isSelected, boolean cellHasFocus ) {
				rv.setText( value.getName() );
				return rv;
			}
		};
		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		ReorderableList proceduresList = new ReorderableList();
		ReorderableList functionsList = new ReorderableList();
		ReorderableList fieldsList = new ReorderableList();

		proceduresList.getAwtComponent().setModel( new TypeMethodsListModel( type, ALL_PROCEDURES_FILTER ) );
		functionsList.getAwtComponent().setModel( new TypeMethodsListModel( type, ALL_FUNCTIONS_FILTER ) );
		fieldsList.getAwtComponent().setModel( new TypeFieldsListModel( type, ALL_FIELDS_FILTER ) );

		proceduresList.getAwtComponent().setCellRenderer( nodeListCellRenderer );
		functionsList.getAwtComponent().setCellRenderer( nodeListCellRenderer );
		fieldsList.getAwtComponent().setCellRenderer( nodeListCellRenderer );

		proceduresList.setBackgroundColor( null );
		functionsList.setBackgroundColor( null );
		fieldsList.setBackgroundColor( null );

		edu.cmu.cs.dennisc.croquet.BooleanState proceduresToolPaletteState = new edu.cmu.cs.dennisc.croquet.BooleanState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "4236c740-8881-4cf1-82e3-e3aef61c13dd" ), true, "procedures" );
		edu.cmu.cs.dennisc.croquet.ToolPalette proceduresToolPalette = proceduresToolPaletteState.createToolPalette( createMembersPanel( proceduresList, org.alice.ide.operations.ast.DeclareProcedureOperation.getInstance( type ) ) );
		proceduresToolPalette.setBackgroundColor( ide.getProcedureColor() );

		edu.cmu.cs.dennisc.croquet.BooleanState functionsToolPaletteState = new edu.cmu.cs.dennisc.croquet.BooleanState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "ea7e601f-255b-41aa-bccd-af181c6b3bf0" ), true, "functions" );
		edu.cmu.cs.dennisc.croquet.ToolPalette functionsToolPalette = functionsToolPaletteState.createToolPalette( createMembersPanel( functionsList, org.alice.ide.operations.ast.DeclareFunctionOperation.getInstance( type ) ) );
		functionsToolPalette.setBackgroundColor( ide.getFunctionColor() );

		edu.cmu.cs.dennisc.croquet.BooleanState fieldsToolPaletteState = new edu.cmu.cs.dennisc.croquet.BooleanState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "7176c895-4e0f-4ebe-98a2-f820b27c9206" ), true, "properties" );
		edu.cmu.cs.dennisc.croquet.ToolPalette fieldsToolPalette = fieldsToolPaletteState.createToolPalette( createMembersPanel( fieldsList, org.alice.ide.operations.ast.DeclareFieldOperation.getInstance( type ) ) );
		fieldsToolPalette.setBackgroundColor( ide.getFieldColor() );

		proceduresToolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		functionsToolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		fieldsToolPalette.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );

		edu.cmu.cs.dennisc.croquet.PageAxisPanel pageAxisPanel = new edu.cmu.cs.dennisc.croquet.PageAxisPanel();
		pageAxisPanel.addComponent( proceduresToolPalette );
		pageAxisPanel.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 4 ) );
		pageAxisPanel.addComponent( functionsToolPalette );
		pageAxisPanel.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 4 ) );
		pageAxisPanel.addComponent( fieldsToolPalette );
//		pageAxisPanel.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue() );
		pageAxisPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,14,0,0 ) );

		edu.cmu.cs.dennisc.croquet.BorderPanel borderPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		borderPanel.addComponent( pageAxisPanel, Constraint.NORTH );
		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( borderPanel );
		scrollPane.setBorder( null );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		this.addComponent( new Title( type ), Constraint.NORTH );
		this.addComponent( scrollPane, Constraint.CENTER );

		//java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( this.getFont(), 2.0f );
		for( javax.swing.JComponent component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( this.getAwtComponent(), edu.cmu.cs.dennisc.pattern.HowMuch.DESCENDANTS_ONLY, javax.swing.JComponent.class ) ) {
			//component.setFont( font );
			edu.cmu.cs.dennisc.java.awt.FontUtilities.setFontToScaledFont( component, 1.5f );
		}
	}
	
	public static void main( String[] args ) {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			try {
				edu.cmu.cs.dennisc.javax.swing.plaf.nimbus.NimbusUtilities.installModifiedNimbus( lookAndFeelInfo );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}

		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		ide.initialize( args );
		ide.loadProjectFrom( args[ 0 ] );
		
		EditTypePanel editTypePanel = new EditTypePanel( ide.getSceneType() );
		
		edu.cmu.cs.dennisc.croquet.Frame frame = new edu.cmu.cs.dennisc.croquet.Frame();
		
		
		frame.getContentPanel().addComponent( editTypePanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		
		frame.pack();
		frame.setDefaultCloseOperation( edu.cmu.cs.dennisc.croquet.Frame.DefaultCloseOperation.EXIT );
		frame.setVisible( true );
		
	}
}
