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

package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactorySelectionPanel extends org.lgna.croquet.components.PanelViewController< org.alice.ide.instancefactory.croquet.InstanceFactoryState > {
	private static final class InternalButton extends org.lgna.croquet.components.JComponent< javax.swing.AbstractButton > {
		private final org.alice.ide.instancefactory.InstanceFactory instanceFactory;
		private final javax.swing.Action action = new javax.swing.AbstractAction() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().setValue( InternalButton.this.instanceFactory );
			}
			
		};
		
		private final edu.cmu.cs.dennisc.java.awt.event.AltTriggerMouseAdapter altTriggerMouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.AltTriggerMouseAdapter() {
			@Override
			protected void altTriggered( java.awt.event.MouseEvent e ) {
				InternalButton.this.handleAltTriggered( e );
			}
		};
		
		public InternalButton( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
			this.instanceFactory = instanceFactory;
		}
		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			javax.swing.JRadioButton rv = new javax.swing.JRadioButton( this.action ) {
				@Override
				protected void paintComponent( java.awt.Graphics g ) {
					//note: do not invoke super
					//super.paintComponent( g );
				}
				@Override
				protected void paintChildren( java.awt.Graphics g ) {
					//todo: better indication of selection/rollover
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					
					java.awt.Composite prevComposite = g2.getComposite();
					
					java.awt.Composite nextComposite = prevComposite;
					float alpha;
					if( model.isSelected() ) {
						if( model.isRollover() ) {
							alpha = 1.0f;
						} else {
							alpha = 0.9f;
						}
						nextComposite = prevComposite;
					} else {
						if( model.isRollover() ) {
							alpha = 0.5f;
						} else {
							alpha = 0.25f;
						}
					}
					nextComposite = java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, alpha );
					
					g2.setComposite( nextComposite );
					try {
						super.paintChildren( g );
					} finally {
						g2.setComposite( prevComposite );
					}
				}
			};
			rv.setOpaque( false );

			rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.LINE_AXIS ) );
			org.lgna.project.ast.Expression expression = this.instanceFactory.createTransientExpression();
			rv.add( org.alice.ide.x.PreviewAstI18nFactory.getInstance().createExpressionPane( expression ).getAwtComponent() );

			//todo: replace w/ spring layout (or something)
			int top = 0;
			int left = 0;
			int bottom = 0;
			int right = 0;
			if( this.instanceFactory instanceof org.alice.ide.instancefactory.ThisInstanceFactory ) {
				//pass
			} else {
				left += 16;
			}
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( top, left, bottom, right ) );
			return rv;
		}
		
		protected void handleAltTriggered( java.awt.event.MouseEvent e ) {
			if( this.instanceFactory instanceof org.alice.ide.instancefactory.ThisFieldAccessFactory ) {
				org.alice.ide.instancefactory.ThisFieldAccessFactory thisFieldAccessFactory = (org.alice.ide.instancefactory.ThisFieldAccessFactory)this.instanceFactory;
				org.lgna.project.ast.AbstractField field = thisFieldAccessFactory.getField();
				if( field instanceof org.lgna.project.ast.UserField ) {
					org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
					org.alice.stageide.operations.ast.oneshot.OneShotMenuModel.getInstance( userField ).getPopupPrepModel().fire( new org.lgna.croquet.triggers.MouseEventTrigger( null, e ) );
				}
			}
		}
		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			this.addMouseListener( this.altTriggerMouseAdapter );
		}
		@Override
		protected void handleUndisplayable() {
			this.removeMouseListener( this.altTriggerMouseAdapter );
			super.handleUndisplayable();
		}
	}
	private static final class InternalPanel extends org.lgna.croquet.components.PageAxisPanel {
		private final javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		private final java.util.Map< org.alice.ide.instancefactory.InstanceFactory, InternalButton > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		public InternalPanel() {
			this.setBackgroundColor( null );
		}
		private InternalButton getButtonFor( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
			InternalButton rv = map.get( instanceFactory );
			if( rv != null ) {
				//pass
			} else {
				rv = new InternalButton( instanceFactory );
				map.put( instanceFactory, rv );
			}
			return rv;
		}
		
		@Override
		protected void internalRefresh() {
			super.internalRefresh();
			this.removeAllComponents();
			java.util.List< InternalButton > buttons = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			buttons.add( getButtonFor( org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() ) );
			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			org.lgna.project.ast.NamedUserType sceneType = ide.getSceneType();
			if( sceneType != null ) {
				for( org.lgna.project.ast.UserField field : sceneType.fields ) {
					buttons.add( getButtonFor( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) ) );
				}
			}
			for( InternalButton button : buttons ) {
				this.internalAddComponent( button );
				this.buttonGroup.add( button.getAwtComponent() );
			}
			this.setSelected( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getValue() );
		}
		
		private void setSelected( org.alice.ide.instancefactory.InstanceFactory instanceFactory ) {
			InternalButton button;
			if( instanceFactory != null ) {
				button = this.map.get( instanceFactory );
			} else {
				button = null;
			}
			if( button != null ) {
				this.buttonGroup.setSelected( button.getAwtComponent().getModel(), true );
			} else {
				javax.swing.ButtonModel buttonModel = this.buttonGroup.getSelection();
				if( buttonModel != null ) {
					this.buttonGroup.setSelected( buttonModel, false );
				}
			}
		}
	}
	
	private final org.lgna.croquet.State.ValueObserver< org.alice.ide.instancefactory.InstanceFactory > instanceFactoryListener = new org.lgna.croquet.State.ValueObserver< org.alice.ide.instancefactory.InstanceFactory >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
			InstanceFactorySelectionPanel.this.getInternalPanel().refreshLater();
		}
	};
	private org.lgna.project.ast.UserType type;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserField > fieldsListener = new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< org.lgna.project.ast.UserField >() {
		@Override
		protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		@Override
		protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			InstanceFactorySelectionPanel.this.getInternalPanel().refreshLater();
		}
	};
	public InstanceFactorySelectionPanel() {
		super( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance(), new InternalPanel() );
		this.setBackgroundColor( null );
		this.getAwtComponent().setOpaque( false );
	}
	
	public org.lgna.project.ast.UserType getType() {
		return this.type;
	}
	public void setType( org.lgna.project.ast.UserType type ) {
		if( this.type != null ) {
			this.type.fields.removeListPropertyListener( this.fieldsListener );
		}
		this.type = type;
		if( this.type != null ) {
			this.type.fields.addListPropertyListener( this.fieldsListener );
		}
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().addValueObserver( this.instanceFactoryListener );
	}
	@Override
	protected void handleUndisplayable() {
		this.getModel().removeValueObserver( this.instanceFactoryListener );
		super.handleUndisplayable();
	}
}
