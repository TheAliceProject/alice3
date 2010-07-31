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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
abstract class OrganizedByTypeMembersContentPanel extends MembersContentPanel {
	@Override
	protected void refresh( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > types ) {
		this.removeAllComponents();
		boolean isNonConsumedTypeDeclaredInJavaAlreadyEncountered = false;

		if( types.size() > 0 ) {
			boolean isSeparatorDesired = types.get( 0 ) instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
			for( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type : types ) {
				boolean isFirstNonConsumedTypeEncounteredInJava = false;
				if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
					if( isSeparatorDesired ) {
						this.addComponent( new edu.cmu.cs.dennisc.croquet.HorizontalSeparator() );
						this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 16 ) );
						isSeparatorDesired = false;
					}
					if( isNonConsumedTypeDeclaredInJavaAlreadyEncountered ) {
						//pass
					} else {
						if( type.isConsumptionBySubClassDesired() ) {
							//pass
						} else {
							isFirstNonConsumedTypeEncounteredInJava = true;
							isNonConsumedTypeDeclaredInJavaAlreadyEncountered = true;
						}
					}
				}
				if( type.isConsumptionBySubClassDesired() ) {
					//pass
				} else {
					if( /*org.alice.ide.IDE.getSingleton().isEmphasizingClasses() ||*/ type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice || isFirstNonConsumedTypeEncounteredInJava ) {
						this.addComponent( MembersEditor.getComponentFor( this.getClass(), type ) );
					}
				}
				this.addComponent( this.getTypeMembersPane( type ) );
			}
		}
		this.revalidateAndRepaint();
	}

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, AbstractTypeMembersPane > mapTypeToPane = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, AbstractTypeMembersPane >();
	protected abstract AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type );
	protected AbstractTypeMembersPane getTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		AbstractTypeMembersPane rv = this.mapTypeToPane.get( type );
		if( rv != null ) {
			//todo?
			rv.refresh();
		} else {
			rv = this.createTypeMembersPane( type );
			this.mapTypeToPane.put( type, rv );
		}
		return rv;
	}
}

abstract class MethodsContentPanel extends OrganizedByTypeMembersContentPanel {
}

class ProceduresContentPanel extends MethodsContentPanel {
	public ProceduresContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getProcedureColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		return new TypeProceduresPane( type );
	}
}

class FunctionsContentPanel extends MethodsContentPanel {
	public FunctionsContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getFunctionColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		return new TypeFunctionsPane( type );
	}
}

class FieldsContentPanel extends OrganizedByTypeMembersContentPanel {
	public FieldsContentPanel() {
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getFieldColor() );
	}
	@Override
	protected AbstractTypeMembersPane createTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		return new TypeFieldsPane( type );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MembersEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static class IndirectCurrentAccessibleTypeIcon implements javax.swing.Icon {
//todo:
//		public IndirectCurrentAccessibleTypeIcon() {
//			org.alice.ide.IDE.getSingleton().getAccessibleListState().addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Accessible >() {
//				public void changed(edu.cmu.cs.dennisc.alice.ast.Accessible nextValue) {
//					MembersEditor.this.repaint();
//				}
//			} );
//		}
		private javax.swing.Icon getCurrentAccessibleTypeIcon() {
			edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.IDE.getSingleton().getAccessibleListState().getSelectedItem();
			String className;
			if( accessible != null ) {
				className = accessible.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getName();
			} else {
				className = null;
			}
			return org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForGalleryClassName( className );
		}
		public int getIconHeight() {
			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
			if( icon != null ) {
				return icon.getIconHeight();
			} else {
				return 0;
			}
		}
		public int getIconWidth() {
			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
			if( icon != null ) {
				return icon.getIconWidth();
			} else {
				return 0;
			}
		}
		public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
			javax.swing.Icon icon = getCurrentAccessibleTypeIcon();
			if( icon != null ) {
				icon.paintIcon(c, g, x, y);
			}
		}
	}
	private static boolean IS_FOLDER_TABBED_PANE_DESIRED = true;
	private static javax.swing.Icon ICON = IS_FOLDER_TABBED_PANE_DESIRED ? null : new IndirectCurrentAccessibleTypeIcon();
	
//	private static abstract class MemberTab extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
//		private static String getTitle( String key ) {
//			java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.memberseditor.TabTitles", javax.swing.JComponent.getDefaultLocale() );
//			return resourceBundle.getString( key );
//		}
//		public MemberTab( java.util.UUID individualId, String key ) {
//			super( individualId, getTitle( key ), ICON );
//		}
//		@Override
//		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane() {
//			edu.cmu.cs.dennisc.croquet.ScrollPane rv = super.createScrollPane();
//			rv.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
//			return rv;
//		}
//	}

//	private MemberTab proceduresTab = new MemberTab( java.util.UUID.fromString( "2731d704-1f80-444e-a610-e6e5866c0b9a" ), "procedure" ) {
//		@Override
//		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
//			return new ProceduresContentPanel();
//		}
//	};
//	private MemberTab functionsTab = new MemberTab( java.util.UUID.fromString( "0f5d1f93-fc67-4109-9aff-0e7b232f201c" ), "function" ) {
//		@Override
//		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
//			return new FunctionsContentPanel();
//		}
//	};
//	private MemberTab fieldsTab = new MemberTab( java.util.UUID.fromString( "6cb9c5a1-dc60-48e7-9a52-534009a093b8" ), "field" ) {
//		@Override
//		protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
//			return new FieldsContentPanel();
//		}
//	};
//	private edu.cmu.cs.dennisc.croquet.TabSelectionState tabbedPaneSelectionState = new edu.cmu.cs.dennisc.croquet.TabSelectionState( 
//			org.alice.ide.IDE.UI_STATE_GROUP, 
//			java.util.UUID.fromString( "d8348dfa-35df-441d-b233-0e1bd9ffd68f" ),
//			0,
//			this.proceduresTab, this.functionsTab, this.fieldsTab );


	private edu.cmu.cs.dennisc.croquet.TabSelectionState tabbedPaneSelectionState = new org.alice.ide.croquet.models.members.MembersTabSelectionState( 
			new ProceduresContentPanel(), new FunctionsContentPanel(), new FieldsContentPanel(), ICON );
	
	public edu.cmu.cs.dennisc.croquet.TabSelectionState getTabbedPaneSelectionState() {
		return this.tabbedPaneSelectionState;
	}
//	private edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField > fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
//		public void changed( edu.cmu.cs.dennisc.alice.ast.AbstractField nextValue ) {
//			MembersEditor.this.handleFieldSelection( nextValue );
//		}
//	};
//	private void handleFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
//		final int N = this.tabbedPaneSelectionState.getItemCount();
//		for( int i=0; i<N; i++ ) {
//			MembersContentPanel membersTab = (MembersContentPanel)this.tabbedPaneSelectionState.getItemAt( i ).getMainComponent();
//			membersTab.handleFieldSelection( field );
//		}
//	}
//	private void refresh() {
//		final int N = this.tabbedPaneSelectionState.getItemCount();
//		for( int i=0; i<N; i++ ) {
//			MembersContentPanel membersTab = (MembersContentPanel)this.tabbedPaneSelectionState.getItemAt( i ).getMainComponent();
//			membersTab.refresh();
//		}
//	}

	//private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, java.awt.Component > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static edu.cmu.cs.dennisc.map.MapToMap< Class< ? >, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>, edu.cmu.cs.dennisc.croquet.Component< ? > > map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static final byte PROTOTYPE = 0;
	
	public static edu.cmu.cs.dennisc.croquet.Component< ? > getComponentFor( Class< ? > cls, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		edu.cmu.cs.dennisc.croquet.Component< ? > rv = map.get( cls, type );
		if( rv != null ) {
			//pass
		} else {
			rv = org.alice.ide.common.TypeComponent.createInstance( type );
			map.put( cls, type, rv );
		}
		return rv;
	}
	public MembersEditor() {
		final float FONT_SCALAR = 1.4f;
		edu.cmu.cs.dennisc.croquet.ComboBox< edu.cmu.cs.dennisc.alice.ast.Accessible > comboBox = org.alice.ide.IDE.getSingleton().getAccessibleListState().createComboBox();
		comboBox.scaleFont( FONT_SCALAR );
		//comboBox.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		
		class AccessibleCellRenderer extends javax.swing.JLabel implements javax.swing.ListCellRenderer {
			private boolean isInScope = true;
			public AccessibleCellRenderer() {
				this.setOpaque( true );
			}
			public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				edu.cmu.cs.dennisc.alice.ast.Accessible accessible = (edu.cmu.cs.dennisc.alice.ast.Accessible)value;
				if( accessible != null ) {
					this.isInScope = true;
					StringBuilder sb = new StringBuilder();
					sb.append( "<html>" );
					if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
						//pass
					} else if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.AbstractParameter ) {
						sb.append( "<i>parameter:</i> " );
					} else if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
						sb.append( "<i>variable:</i> " );
					} else if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
						sb.append( "<i>constant:</i> " );
					}
					sb.append( "<strong>" );
					sb.append( accessible.getValidName() );
					sb.append( "</strong>" );

					//rv.setEnabled( true );
					if( accessible instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)accessible;
						edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = org.alice.ide.IDE.getSingleton().getFocusedCode();
						if( focusedCode != null ) {
							edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> scopeType = focusedCode.getDeclaringType();
							if( field.getValueType() == scopeType ) {
								sb.append( " <em>(this)</em>" );
							} else if( field.getDeclaringType() == scopeType ) {
								//pass
							} else {
								this.isInScope = false;
								sb.append( " -out-of-scope-" );
							}
						}
					}
					sb.append( "</html>" );
					this.setText( sb.toString() );
					
					edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> valueType = accessible.getValueType();
//					edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava valueTypeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
//					String className = valueTypeInJava.getClassReflectionProxy().getName();
					
					this.setIcon( org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForType( valueType ) );
					if( this.isInScope ) {
						if( isSelected ) {
							this.setForeground( list.getSelectionForeground() );
							this.setBackground( list.getSelectionBackground() );
						} else {
							this.setForeground( list.getForeground() );
							this.setBackground( list.getBackground() );
						}
					} else {
						if( isSelected ) {
							this.setForeground( java.awt.Color.LIGHT_GRAY );
							this.setBackground( java.awt.Color.GRAY );
						} else {
							this.setForeground( java.awt.Color.DARK_GRAY );
							this.setBackground( list.getBackground() );
						}
					}
				}
				return this;
			}
			@Override
			public void paint(java.awt.Graphics g) {
				super.paint(g);
				if( this.isInScope ) {
					//pass
				} else {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					g2.setPaint( edu.cmu.cs.dennisc.croquet.PaintUtilities.getDisabledTexturePaint() );
					g2.fillRect( 0, 0, this.getWidth(), this.getHeight() );
				}
			}
		}
		comboBox.setMaximumRowCount( 20 );
		comboBox.setRenderer( new AccessibleCellRenderer() );
		comboBox.setMaximumSizeClampedToPreferredSize( true );

		edu.cmu.cs.dennisc.croquet.Label instanceLabel = new edu.cmu.cs.dennisc.croquet.Label( "instance:" );
		instanceLabel.scaleFont( FONT_SCALAR );
		
		final edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField PROTOTYPE_FIELD;
		try {
			PROTOTYPE_FIELD = edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField.get( MembersEditor.class.getField( "PROTOTYPE" ) );
		} catch( NoSuchFieldException nsfe ) {
			throw new RuntimeException( nsfe );
		}
		edu.cmu.cs.dennisc.croquet.ComboBox<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField> partComboBox = org.alice.ide.croquet.models.members.PartSelectionState.getInstance().createComboBox();
		partComboBox.setRenderer( new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< edu.cmu.cs.dennisc.alice.ast.AbstractField >() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.AbstractField value, int index, boolean isSelected, boolean cellHasFocus) {
				if( value == PROTOTYPE_FIELD ) {
					value = null;
				}
				StringBuilder sb = new StringBuilder();
				sb.append( "<html><nobr>" );
				if( index == -1 ) {
					if( list.getModel().getSize() > 0 ) {
						sb.append( "part: " );
					} else {
						sb.append( "<font color=\"gray\">" );
						sb.append( "no parts" );
						sb.append( "</font>" );
					}
				}
				if( value != null ) {
					sb.append( "<strong>" );
					sb.append( value.getName() );
					sb.append( "</strong>" );
				} else {
					if( index == -1 && list.getModel().getSize()==0 ) {
						//pass
					} else {
						sb.append( "<font color=\"gray\">" );
						sb.append( "<em>" );
						sb.append( "none" );
						sb.append( "</em>" );
						sb.append( "</font>" );
					}
				}
				sb.append( "</nobr></html>" );
				rv.setText( sb.toString() );
				return rv;
			}
		} );
		
		
		partComboBox.setMaximumSizeClampedToPreferredSize( true );
		partComboBox.scaleFont( FONT_SCALAR );
		partComboBox.setMaximumRowCount( 20 );
		partComboBox.getAwtComponent().setPrototypeDisplayValue( PROTOTYPE_FIELD );
		partComboBox.setMinimumPreferredWidth( partComboBox.getAwtComponent().getPreferredSize().width );
		partComboBox.getAwtComponent().setPrototypeDisplayValue( null );


//		partComboBox.getAwtComponent().addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
//			public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
//				javax.swing.JComboBox comboBox = (javax.swing.JComboBox)e.getSource();
////				java.awt.Dimension size = popupMenu.getSize();
////				popupMenu.setSize( size );
//			}
//			public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
//			}
//			public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
//			}
//		} );
		
		edu.cmu.cs.dennisc.croquet.LineAxisPanel instancePanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel();
		instancePanel.addComponent( instanceLabel );
		instancePanel.addComponent( comboBox );
		instancePanel.addComponent( partComboBox );
		//instancePanel.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue() );

		edu.cmu.cs.dennisc.croquet.AbstractTabbedPane tabbedPane = IS_FOLDER_TABBED_PANE_DESIRED ? this.tabbedPaneSelectionState.createDefaultFolderTabbedPane() : this.tabbedPaneSelectionState.createDefaultToolPaletteTabbedPane();
		this.addComponent( instancePanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START );
		this.addComponent( tabbedPane, Constraint.CENTER );
		tabbedPane.scaleFont( 1.5f );
		if( tabbedPane instanceof edu.cmu.cs.dennisc.croquet.ToolPaletteTabbedPane ) {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
			this.tabbedPaneSelectionState.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.croquet.PredeterminedTab >() {
				public void changed(edu.cmu.cs.dennisc.croquet.PredeterminedTab nextValue) {
					if( nextValue != null ) {
						MembersEditor.this.setBackgroundColor( nextValue.getMainComponent().getBackgroundColor() );
					}
					MembersEditor.this.repaint();
				}
			} );
		} else {
			this.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		}
		
	}
}
