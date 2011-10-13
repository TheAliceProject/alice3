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
public class MembersEditor extends org.lgna.croquet.components.BorderPanel {
	private static edu.cmu.cs.dennisc.map.MapToMap< Class< ? >, org.lgna.project.ast.AbstractType<?,?,?>, org.lgna.croquet.components.Component< ? > > map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	public static final byte PROTOTYPE = 0;
	public static org.lgna.croquet.components.Component< ? > getComponentFor( Class< ? > cls, org.lgna.project.ast.AbstractType<?,?,?> type ) {
		org.lgna.croquet.components.Component< ? > rv = map.get( cls, type );
		if( rv != null ) {
			//pass
		} else {
			rv = org.alice.ide.common.TypeComponent.createInstance( type );
			map.put( cls, type, rv );
		}
		return rv;
	}
	private final org.lgna.croquet.components.CardPanel cardPanel = new org.lgna.croquet.components.CardPanel();
	private final java.util.Map< Boolean, org.lgna.croquet.components.CardPanel.Key > keys = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public MembersEditor() {
		final float FONT_SCALAR = 1.4f;
//		class AccessibleCellRenderer extends javax.swing.JLabel implements javax.swing.ListCellRenderer {
//			private boolean isInScope = true;
//			public AccessibleCellRenderer() {
//				this.setOpaque( true );
//			}
//			public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//				org.lgna.project.ast.Accessible accessible = (org.lgna.project.ast.Accessible)value;
//				if( accessible != null ) {
//					this.isInScope = true;
//					StringBuilder sb = new StringBuilder();
//					sb.append( "<html>" );
//					if( accessible instanceof org.lgna.project.ast.AbstractField ) {
//						//pass
//					} else if( accessible instanceof org.lgna.project.ast.AbstractParameter ) {
//						sb.append( "<i>parameter:</i> " );
//					} else if( accessible instanceof org.lgna.project.ast.VariableDeclaredInAlice ) {
//						sb.append( "<i>variable:</i> " );
//					} else if( accessible instanceof org.lgna.project.ast.ConstantDeclaredInAlice ) {
//						sb.append( "<i>constant:</i> " );
//					}
//					sb.append( "<strong>" );
//					sb.append( accessible.getValidName() );
//					sb.append( "</strong>" );
//
//					//rv.setEnabled( true );
//					if( accessible instanceof org.lgna.project.ast.AbstractField ) {
//						org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)accessible;
//						org.lgna.project.ast.AbstractCode focusedCode = org.alice.ide.IDE.getActiveInstance().getFocusedCode();
//						if( focusedCode != null ) {
//							org.lgna.project.ast.AbstractType<?,?,?> scopeType = focusedCode.getDeclaringType();
//							if( field.getValueType() == scopeType ) {
//								sb.append( " <em>(this)</em>" );
//							} else if( field.getDeclaringType() == scopeType ) {
//								//pass
//							} else {
//								this.isInScope = false;
//								sb.append( " -out-of-scope-" );
//							}
//						}
//					}
//					sb.append( "</html>" );
//					this.setText( sb.toString() );
//					
//					org.lgna.project.ast.AbstractType<?,?,?> valueType = accessible.getValueType();
////					org.lgna.project.ast.TypeDeclaredInJava valueTypeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
////					String className = valueTypeInJava.getClassReflectionProxy().getName();
//					
//					this.setIcon( org.alice.stageide.gallerybrowser.ResourceManager.getSmallIconForType( valueType ) );
//					if( this.isInScope ) {
//						if( isSelected ) {
//							this.setForeground( list.getSelectionForeground() );
//							this.setBackground( list.getSelectionBackground() );
//						} else {
//							this.setForeground( list.getForeground() );
//							this.setBackground( list.getBackground() );
//						}
//					} else {
//						if( isSelected ) {
//							this.setForeground( java.awt.Color.LIGHT_GRAY );
//							this.setBackground( java.awt.Color.GRAY );
//						} else {
//							this.setForeground( java.awt.Color.DARK_GRAY );
//							this.setBackground( list.getBackground() );
//						}
//					}
//				}
//				return this;
//			}
//			@Override
//			public void paint(java.awt.Graphics g) {
//				super.paint(g);
//				if( this.isInScope ) {
//					//pass
//				} else {
//					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//					g2.setPaint( org.lgna.croquet.components.PaintUtilities.getDisabledTexturePaint() );
//					g2.fillRect( 0, 0, this.getWidth(), this.getHeight() );
//				}
//			}
//		}

		org.lgna.croquet.components.Label instanceLabel = new org.lgna.croquet.components.Label( "instance:" );
		instanceLabel.scaleFont( FONT_SCALAR );
//		
//		final org.lgna.project.ast.JavaField PROTOTYPE_FIELD;
//		try {
//			PROTOTYPE_FIELD = org.lgna.project.ast.JavaField.getInstance( MembersEditor.class.getField( "PROTOTYPE" ) );
//		} catch( NoSuchFieldException nsfe ) {
//			throw new RuntimeException( nsfe );
//		}
//		
		org.lgna.croquet.components.LineAxisPanel instancePanel = new org.lgna.croquet.components.LineAxisPanel();
		
		instancePanel.addComponent( instanceLabel );
		instancePanel.addComponent( new org.alice.ide.croquet.components.InstanceFactoryDropDown( org.alice.ide.instancefactory.InstanceFactoryState.getInstance() ) );
		instancePanel.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		instancePanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,0,4 ) );

		this.addComponent( instancePanel, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
		this.addComponent( cardPanel, Constraint.CENTER );
	}
	private org.lgna.croquet.State.ValueObserver< Boolean > isAlwaysAvailableObserver = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			MembersEditor.this.cardPanel.show( MembersEditor.this.getKey( nextValue ) );
		}
	};
	
	private org.lgna.croquet.components.CardPanel.Key getKey( boolean isAlwaysShowingBlocks ) {
		org.lgna.croquet.components.CardPanel.Key rv = this.keys.get( isAlwaysShowingBlocks );
		if( rv != null ) {
			//pass
		} else {
			org.lgna.croquet.components.AbstractTabbedPane<?,?,?> tabbedPane;
			if( isAlwaysShowingBlocks ) {
				tabbedPane = org.alice.ide.croquet.models.members.MembersTabSelectionState.getInstance().createDefaultFolderTabbedPane();
			} else {
				tabbedPane = org.alice.ide.croquet.models.templates.TemplatesTabSelectionState.getInstance().createToolPaletteTabbedPane();
				tabbedPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
			}
			rv = this.cardPanel.createKey( tabbedPane, tabbedPane.getModel().getId() );
			this.cardPanel.addComponent( rv );
			this.keys.put( isAlwaysShowingBlocks, rv );
		}
		return rv;
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().addAndInvokeValueObserver( this.isAlwaysAvailableObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().removeValueObserver( this.isAlwaysAvailableObserver );
		super.handleUndisplayable();
	}
}
