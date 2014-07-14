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

package org.alice.stageide.sceneeditor.views;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactorySelectionPanel extends org.lgna.croquet.views.PanelViewController<org.alice.ide.instancefactory.croquet.InstanceFactoryState> {
	private static final class InstanceFactoryLayout implements java.awt.LayoutManager2 {
		public void addLayoutComponent( String name, java.awt.Component comp ) {
			this.invalidateLayout( comp.getParent() );
		}

		public void addLayoutComponent( java.awt.Component comp, Object constraints ) {
			this.invalidateLayout( comp.getParent() );
		}

		public void removeLayoutComponent( java.awt.Component comp ) {
			this.invalidateLayout( comp.getParent() );
		}

		public void invalidateLayout( java.awt.Container target ) {
			synchronized( this ) {
				this.xChildren = null;
				this.yChildren = null;
				this.xTotal = null;
				this.yTotal = null;
			}
		}

		private void ensureSizeRequirementsUpToDate( java.awt.Container target ) {
			synchronized( this ) {
				if( ( xChildren == null ) || ( yChildren == null ) ) {
					int nChildren = target.getComponentCount();
					xChildren = new javax.swing.SizeRequirements[ nChildren ];
					yChildren = new javax.swing.SizeRequirements[ nChildren ];
					for( int i = 0; i < nChildren; i++ ) {
						java.awt.Component c = target.getComponent( i );
						if( c.isVisible() ) {
							java.awt.Dimension min = c.getMinimumSize();
							java.awt.Dimension typ = c.getPreferredSize();
							java.awt.Dimension max = c.getMaximumSize();
							xChildren[ i ] = new javax.swing.SizeRequirements( min.width, typ.width, max.width, c.getAlignmentX() );
							yChildren[ i ] = new javax.swing.SizeRequirements( min.height, typ.height, max.height, c.getAlignmentY() );
						} else {
							xChildren[ i ] = new javax.swing.SizeRequirements( 0, 0, 0, c.getAlignmentX() );
							yChildren[ i ] = new javax.swing.SizeRequirements( 0, 0, 0, c.getAlignmentY() );
						}
					}
					xTotal = javax.swing.SizeRequirements.getAlignedSizeRequirements( xChildren );
					yTotal = javax.swing.SizeRequirements.getTiledSizeRequirements( yChildren );
				}
			}
		}

		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			this.ensureSizeRequirementsUpToDate( parent );
			java.awt.Insets insets = parent.getInsets();
			return new java.awt.Dimension( this.xTotal.minimum + insets.left + insets.right, this.yTotal.minimum + insets.top + insets.bottom );
		}

		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			this.ensureSizeRequirementsUpToDate( parent );
			java.awt.Insets insets = parent.getInsets();
			return new java.awt.Dimension( this.xTotal.preferred + insets.left + insets.right, this.yTotal.preferred + insets.top + insets.bottom );
		}

		public java.awt.Dimension maximumLayoutSize( java.awt.Container parent ) {
			this.ensureSizeRequirementsUpToDate( parent );
			java.awt.Insets insets = parent.getInsets();
			return new java.awt.Dimension( this.xTotal.maximum + insets.left + insets.right, this.yTotal.maximum + insets.top + insets.bottom );
		}

		public void layoutContainer( java.awt.Container target ) {
			int nChildren = target.getComponentCount();
			int[] xOffsets = new int[ nChildren ];
			int[] xSpans = new int[ nChildren ];
			int[] yOffsets = new int[ nChildren ];
			int[] ySpans = new int[ nChildren ];

			java.awt.Dimension size = target.getSize();
			java.awt.Insets insets = target.getInsets();

			java.awt.Dimension availableSpace = new java.awt.Dimension( size );
			availableSpace.width -= insets.left + insets.right;
			availableSpace.height -= insets.top + insets.bottom;

			synchronized( this ) {
				this.ensureSizeRequirementsUpToDate( target );
				javax.swing.SizeRequirements.calculateAlignedPositions( availableSpace.width, xTotal, xChildren, xOffsets, xSpans, true );
				javax.swing.SizeRequirements.calculateTiledPositions( availableSpace.height, yTotal, yChildren, yOffsets, ySpans );
			}

			for( int i = 0; i < nChildren; i++ ) {
				java.awt.Component c = target.getComponent( i );
				int x = insets.left + xOffsets[ i ];
				int y = insets.top + yOffsets[ i ];
				c.setBounds( x, y, xSpans[ i ], ySpans[ i ] );
			}

			java.awt.Rectangle boundsI = new java.awt.Rectangle();
			int indexOfFirstComponentThatFails = -1;
			for( int i = 0; i < ( nChildren - 1 ); i++ ) {
				java.awt.Component c = target.getComponent( i );
				c.getBounds( boundsI );
				if( ( boundsI.y + boundsI.height ) >= ( size.height - insets.bottom ) ) {
					indexOfFirstComponentThatFails = i;
					break;
				}
			}

			java.awt.Component lastComponent = target.getComponent( nChildren - 1 );
			if( indexOfFirstComponentThatFails == -1 ) {
				lastComponent.setSize( 0, 0 );
			} else {
				if( indexOfFirstComponentThatFails > 0 ) {
					for( int i = indexOfFirstComponentThatFails - 1; i < ( nChildren - 1 ); i++ ) {
						target.getComponent( i ).setSize( 0, 0 );
					}
					java.awt.Point locationOfLastComponent = target.getComponent( indexOfFirstComponentThatFails - 1 ).getLocation();
					locationOfLastComponent.x += 16;
					lastComponent.setLocation( locationOfLastComponent );
				}
			}
		}

		public float getLayoutAlignmentX( java.awt.Container target ) {
			this.ensureSizeRequirementsUpToDate( target );
			return this.xTotal.alignment;
		}

		public float getLayoutAlignmentY( java.awt.Container target ) {
			this.ensureSizeRequirementsUpToDate( target );
			return this.yTotal.alignment;
		}

		private javax.swing.SizeRequirements[] xChildren;
		private javax.swing.SizeRequirements[] yChildren;
		private javax.swing.SizeRequirements xTotal;
		private javax.swing.SizeRequirements yTotal;
	}

	private static final class InternalButton extends org.lgna.croquet.views.SwingComponentView<javax.swing.AbstractButton> {
		private final org.alice.ide.instancefactory.InstanceFactory instanceFactory;
		private final javax.swing.Action action = new javax.swing.AbstractAction() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().setValueTransactionlessly( InternalButton.this.instanceFactory );
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
			org.alice.stageide.oneshot.OneShotMenuModel.getInstance( this.instanceFactory ).getPopupPrepModel().fire( org.lgna.croquet.triggers.MouseEventTrigger.createUserInstance( e ) );
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

	private static final class InternalPanel extends org.lgna.croquet.views.Panel {
		private final javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
		private final java.util.Map<org.alice.ide.instancefactory.InstanceFactory, InternalButton> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		private final org.lgna.croquet.views.SwingComponentView<?> dropDown;

		public InternalPanel() {
			this.setBackgroundColor( null );
			this.dropDown = org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getCascadeRoot().getPopupPrepModel().createPopupButton();
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
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new InstanceFactoryLayout();
		}

		@Override
		protected void internalRefresh() {
			super.internalRefresh();
			this.removeAllComponents();
			java.util.List<InternalButton> buttons = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			buttons.add( getButtonFor( org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() ) );
			org.alice.stageide.StageIDE ide = org.alice.stageide.StageIDE.getActiveInstance();
			org.alice.ide.ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
			org.lgna.project.ast.NamedUserType sceneType = ide.getSceneType();
			if( sceneType != null ) {
				for( org.lgna.project.ast.UserField field : sceneType.fields ) {
					if( field.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.MANAGED ) {
						if( apiConfigurationManager.isInstanceFactoryDesiredForType( field.getValueType() ) ) {
							buttons.add( getButtonFor( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) ) );
						}
					}
				}
			}
			for( InternalButton button : buttons ) {
				this.internalAddComponent( button );
				this.buttonGroup.add( button.getAwtComponent() );
			}

			this.internalAddComponent( this.dropDown );
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

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactoryListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.instancefactory.InstanceFactory> e ) {
			InstanceFactorySelectionPanel.this.getInternalPanel().refreshLater();
		}
	};
	private org.lgna.project.ast.UserType type;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener<org.lgna.project.ast.UserField> fieldsListener = new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter<org.lgna.project.ast.UserField>() {
		@Override
		protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent<org.lgna.project.ast.UserField> e ) {
		}

		@Override
		protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent<org.lgna.project.ast.UserField> e ) {
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
		if( this.type != type ) {
			if( this.type != null ) {
				this.type.fields.removeListPropertyListener( this.fieldsListener );
			}
			this.type = type;
			if( this.type != null ) {
				this.type.fields.addListPropertyListener( this.fieldsListener );
			}
			this.getInternalPanel().refreshLater();
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().addNewSchoolValueListener( this.instanceFactoryListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.getModel().removeNewSchoolValueListener( this.instanceFactoryListener );
		super.handleUndisplayable();
	}
}
