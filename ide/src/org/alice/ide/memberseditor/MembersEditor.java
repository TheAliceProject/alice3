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
public class MembersEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	public static boolean IS_FOLDER_TABBED_PANE_DESIRED = true;
	
//	private edu.cmu.cs.dennisc.croquet.TabSelectionState tabbedPaneSelectionState = new org.alice.ide.croquet.models.members.MembersTabSelectionState( 
//			new ProceduresContentPanel(), new FunctionsContentPanel(), new FieldsContentPanel(), ICON );
	
//	public edu.cmu.cs.dennisc.croquet.TabSelectionState getTabbedPaneSelectionState() {
//		return this.tabbedPaneSelectionState;
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
		edu.cmu.cs.dennisc.croquet.ComboBox< edu.cmu.cs.dennisc.alice.ast.Accessible > comboBox = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().createComboBox();
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

		org.alice.ide.croquet.models.members.MembersTabSelectionState membersTabSelectionState = org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance();
		edu.cmu.cs.dennisc.croquet.AbstractTabbedPane tabbedPane = IS_FOLDER_TABBED_PANE_DESIRED ? membersTabSelectionState.createDefaultFolderTabbedPane() : membersTabSelectionState.createDefaultToolPaletteTabbedPane();
		this.addComponent( instancePanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START );
		this.addComponent( tabbedPane, Constraint.CENTER );
		tabbedPane.scaleFont( 1.5f );
		if( tabbedPane instanceof edu.cmu.cs.dennisc.croquet.ToolPaletteTabbedPane ) {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
			membersTabSelectionState.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.croquet.PredeterminedTab >() {
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
