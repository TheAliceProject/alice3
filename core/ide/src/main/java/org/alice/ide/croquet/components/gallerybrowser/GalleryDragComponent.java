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

package org.alice.ide.croquet.components.gallerybrowser;

import org.alice.nonfree.NebulousIde;

/**
 * @author Dennis Cosgrove
 */
public class GalleryDragComponent extends org.alice.ide.croquet.components.KnurlDragComponent<org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel> {
	private final java.awt.Color baseColor;
	private final java.awt.Color highlightColor;
	private final java.awt.Color shadowColor;
	private final java.awt.Color activeHighlightColor;
	private final java.awt.Color activeShadowColor;

	private static final class SuperclassIconLabel extends org.lgna.croquet.views.SwingComponentView<javax.swing.JLabel> {
		private final Class<?> modelResourceInterface;

		public SuperclassIconLabel( Class<?> modelResourceInterface ) {
			this.modelResourceInterface = modelResourceInterface;
		}

		@Override
		protected javax.swing.JLabel createAwtComponent() {
			javax.swing.JLabel rv = new javax.swing.JLabel() {
				private final javax.swing.JToolTip toolTipForTipLocation = new javax.swing.JToolTip();

				@Override
				public java.awt.Point getToolTipLocation( java.awt.event.MouseEvent event ) {
					toolTipForTipLocation.setTipText( this.getToolTipText() );
					int offset = toolTipForTipLocation.getPreferredSize().height;
					offset += 4;
					return new java.awt.Point( 0, -offset );
				}
			};
			StringBuilder sb = new StringBuilder();
			sb.append( "superclass: " );
			String simpleName = modelResourceInterface.getSimpleName();
			if( simpleName.endsWith( "Resource" ) ) {
				simpleName = simpleName.substring( 0, simpleName.length() - "Resource".length() );
			}
			sb.append( simpleName );
			rv.setToolTipText( sb.toString() );
			return rv;
		}
	}

	public GalleryDragComponent( org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel model ) {
		super( model, false );

		if( model.isInstanceCreator() ) {
			this.baseColor = org.alice.ide.DefaultTheme.DEFAULT_CONSTRUCTOR_COLOR;
			this.highlightColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 1.4 );
			this.shadowColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.baseColor, 1.0, 0.9, 0.8 );
			this.activeHighlightColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 2.0 );
			this.activeShadowColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 0.9 );
		} else {
			this.baseColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 191 );
			this.highlightColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 221 );
			this.shadowColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 171 );
			this.activeHighlightColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 255 );
			this.activeShadowColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 181 );
		}

		Class<?> modelResourceCls = null;
		if( model instanceof org.alice.stageide.modelresource.ResourceNode ) {
			if( model instanceof org.alice.stageide.modelresource.ClassHierarchyBasedResourceNode ) {
				//pass
			} else {
				org.alice.stageide.modelresource.ResourceNode resourceNode = (org.alice.stageide.modelresource.ResourceNode)model;
				org.alice.stageide.modelresource.ResourceKey resourceKey = resourceNode.getResourceKey();
				if( resourceKey instanceof org.alice.stageide.modelresource.InstanceCreatorKey ) {
					org.alice.stageide.modelresource.InstanceCreatorKey instanceCreatorKey = (org.alice.stageide.modelresource.InstanceCreatorKey)resourceKey;
					modelResourceCls = instanceCreatorKey.getModelResourceCls();
				}
			}
		} else if( model instanceof org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel ) {
			org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel uriGalleryDragModel = (org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel)model;
			org.alice.stageide.modelresource.InstanceCreatorKey resourceKey = uriGalleryDragModel.getResourceKey();
			if( resourceKey != null ) {
				modelResourceCls = resourceKey.getModelResourceCls();
			}

			org.lgna.croquet.views.Label label = new org.lgna.croquet.views.Label( org.alice.stageide.icons.PlusIconFactory.getInstance().getIcon( org.lgna.croquet.icon.IconSize.SMALL.getSize() ) );
			label.setToolTipText( uriGalleryDragModel.getTypeSummaryToolTipText() );
			label.setVerticalAlignment( org.lgna.croquet.views.VerticalAlignment.BOTTOM );
			this.internalAddComponent( label, GalleryDragLayoutManager.TOP_RIGHT_CONSTRAINT );
		}
		if( modelResourceCls != null ) {
			if( modelResourceCls.isEnum() ) {
				Class<?>[] modelResourceInterfaces = modelResourceCls.getInterfaces();
				if( modelResourceInterfaces.length > 0 ) {
					Class<?> modelResourceInterface = modelResourceInterfaces[ 0 ];
					if( org.lgna.story.resources.ModelResource.class.isAssignableFrom( modelResourceInterface ) ) {
						org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForResourceCls( (Class<org.lgna.story.resources.ModelResource>)modelResourceInterface );
						if( iconFactory != null ) {
							final java.awt.Dimension SUPER_CLASS_ICON_SIZE = new java.awt.Dimension( 32, 24 );
							javax.swing.Icon icon = iconFactory.getIcon( SUPER_CLASS_ICON_SIZE );
							SuperclassIconLabel superclsLabel = new SuperclassIconLabel( modelResourceInterface );
							superclsLabel.getAwtComponent().setIcon( icon );
							this.internalAddComponent( superclsLabel, GalleryDragLayoutManager.TOP_LEFT_CONSTRAINT );
						}
					}
				}
			}
		}

		org.lgna.croquet.views.Label label = new org.lgna.croquet.views.Label();
		label.setText( model.getText() );
		org.lgna.croquet.icon.IconFactory iconFactory = model.getIconFactory();
		label.setIcon( iconFactory != null ? iconFactory.getIcon( model.getIconSize() ) : null );
		label.setVerticalTextPosition( org.lgna.croquet.views.VerticalTextPosition.BOTTOM );
		label.setHorizontalTextPosition( org.lgna.croquet.views.HorizontalTextPosition.CENTER );

		this.internalAddComponent( label, GalleryDragLayoutManager.BASE_CONSTRAINT );
		this.setBackgroundColor( this.baseColor );
		this.setMaximumSizeClampedToPreferredSize( true );
		this.setAlignmentY( java.awt.Component.TOP_ALIGNMENT );
	}

	@Override
	protected boolean isClickAndClackAppropriate() {
		org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel model = this.getModel();
		return model.isClickAndClackAppropriate();
	}

	private static class GalleryDragLayoutManager implements java.awt.LayoutManager {
		private static String BASE_CONSTRAINT = "TOP_LEFT_CONSTRAINT_BASE";
		private static String TOP_LEFT_CONSTRAINT = "TOP_LEFT_CONSTRAINT";
		private static String TOP_RIGHT_CONSTRAINT = "TOP_RIGHT_CONSTRAINT";

		private final java.util.List<java.awt.Component> topLeftComponents = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
		private final java.util.List<java.awt.Component> topRightComponents = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
		private java.awt.Component baseComponent;

		@Override
		public void addLayoutComponent( String name, java.awt.Component comp ) {
			if( name.contentEquals( BASE_CONSTRAINT ) ) {
				this.baseComponent = comp;
			}
			if( name.startsWith( TOP_LEFT_CONSTRAINT ) ) {
				this.topLeftComponents.add( comp );
			} else {
				this.topRightComponents.add( comp );
			}
		}

		@Override
		public void removeLayoutComponent( java.awt.Component comp ) {
		}

		@Override
		public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
			return new java.awt.Dimension();
		}

		@Override
		public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
			//note: ridiculous
			java.awt.Dimension rv = this.baseComponent.getPreferredSize();
			java.awt.Insets insets = parent.getInsets();
			rv.width += insets.left + insets.right;
			rv.height += insets.top + insets.bottom;
			return rv;
		}

		@Override
		public void layoutContainer( java.awt.Container parent ) {
			//note: ridiculous
			java.awt.Insets insets = parent.getInsets();
			java.awt.Dimension parentSize = parent.getSize();
			final int N = parent.getComponentCount();
			for( int i = 0; i < N; i++ ) {
				java.awt.Component awtComponent = parent.getComponent( N - i - 1 );
				awtComponent.setSize( awtComponent.getPreferredSize() );
				awtComponent.setLocation( insets.left, insets.top );
			}
			for( java.awt.Component awtComponent : this.topLeftComponents ) {
				awtComponent.setLocation( insets.left, insets.top );
			}
			for( java.awt.Component awtComponent : this.topRightComponents ) {
				awtComponent.setLocation( parentSize.width - awtComponent.getWidth() - insets.right, insets.top );
			}
		}
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jComponent ) {
		return new GalleryDragLayoutManager();
	}

	@Override
	protected void handleLeftMouseButtonQuoteClickedUnquote( java.awt.event.MouseEvent e ) {
		super.handleLeftMouseButtonQuoteClickedUnquote( e );
		switch( e.getClickCount() ) {
		case 1:
			org.lgna.croquet.Model leftButtonClickModel = this.getModel().getLeftButtonClickModel();
			if( leftButtonClickModel != null ) {
				leftButtonClickModel.fire( org.lgna.croquet.triggers.MouseEventTrigger.createUserInstance( this, e ) );
			}
			break;
		}
	}

	@Override
	protected void handleBackButtonClicked( java.awt.event.MouseEvent e ) {
		super.handleBackButtonClicked( e );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: back" );
	}

	@Override
	protected void handleForwardButtonClicked( java.awt.event.MouseEvent e ) {
		super.handleForwardButtonClicked( e );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: forward" );
	}

	@Override
	protected int getInsetTop() {
		return 4;
	}

	@Override
	protected int getInsetRight() {
		return 4;
	}

	@Override
	protected int getInsetBottom() {
		return 4;
	}

	@Override
	protected int getDockInsetLeft() {
		return 0;
	}

	@Override
	protected int getInternalInsetLeft() {
		return 4;
	}

	@Override
	protected java.awt.geom.RoundRectangle2D.Float createShape( int x, int y, int width, int height ) {
		return new java.awt.geom.RoundRectangle2D.Float( x, y, width - 1, height - 1, 8, 8 );
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fill( this.createShape( x, y, width, height ) );
	}

	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		java.awt.geom.RoundRectangle2D.Float shape = this.createShape( x, y, width, height );
		int y1 = y + height;
		int yCenter = y + ( height / 2 );
		int yA = y + ( height / 3 );
		int yB = y1 - ( height / 3 );

		java.awt.Color highlightColor = this.isActive() ? this.activeHighlightColor : this.highlightColor;
		java.awt.Color shadowColor = this.isActive() ? this.activeShadowColor : this.shadowColor;

		java.awt.GradientPaint paintTop = new java.awt.GradientPaint( x, y, highlightColor, x, yA, shadowColor );
		java.awt.GradientPaint paintBottom = new java.awt.GradientPaint( x, yB, shadowColor, x, y1, highlightColor );

		java.awt.Paint prevPaint = g2.getPaint();
		java.awt.Shape prevClip = g2.getClip();

		try {
			java.awt.geom.Area topArea = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, new java.awt.Rectangle( x, y, width, yCenter - y ) );
			g2.setClip( topArea );
			g2.setPaint( paintTop );
			g2.fill( shape );

			java.awt.geom.Area bottomArea = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, new java.awt.Rectangle( x, yCenter, width, y1 - yCenter ) );
			g2.setClip( bottomArea );
			g2.setPaint( paintBottom );
			g2.fill( shape );
		} finally {
			g2.setClip( prevClip );
			g2.setPaint( prevPaint );
		}
	}

	private static java.awt.Shape createShapeAround( java.awt.geom.Rectangle2D bounds ) {
		float x0 = (float)( bounds.getX() - 2 );
		float y0 = (float)( bounds.getY() - 4 );
		float x1 = (float)( x0 + bounds.getWidth() + 4 );
		float y1 = (float)( y0 + bounds.getHeight() + 5 );

		final int TAB_LENGTH = 6;

		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		rv.moveTo( x0, y1 );
		rv.lineTo( x0, y0 );
		rv.lineTo( x0 + TAB_LENGTH, y0 );
		rv.lineTo( x0 + TAB_LENGTH + 3, y0 + 3 );
		rv.lineTo( x1, y0 + 3 );
		rv.lineTo( x1, y1 );
		rv.closePath();
		return rv;
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		super.paintEpilogue( g2, x, y, width, height );

		org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel model = this.getModel();
		if( model instanceof org.alice.stageide.modelresource.ResourceNode ) {
			org.alice.stageide.modelresource.ResourceNode resourceNode = (org.alice.stageide.modelresource.ResourceNode)model;

			org.alice.stageide.modelresource.ResourceKey resourceKey = resourceNode.getResourceKey();
			if( NebulousIde.nonfree.isInstanceOfPersonResourceKey( resourceKey ) ) {

				final boolean IS_PERSON_EDITOR_ICON_DESIRED = false;

				if( IS_PERSON_EDITOR_ICON_DESIRED ) {
					final int PAD_X = 6;
					final int PAD_Y = 4;
					final int WIDTH = 24;
					final int HEIGHT = edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( WIDTH );

					final int X_OFFSET = ( x + width ) - WIDTH - PAD_X;
					final int Y_OFFSET = y + PAD_Y;

					final int TITLE_HEIGHT = 3;

					g2.setPaint( java.awt.Color.BLUE );
					g2.fillRect( X_OFFSET, Y_OFFSET, WIDTH, TITLE_HEIGHT );

					final int LEADING_WIDTH = ( WIDTH * 2 ) / 5;

					g2.setPaint( new java.awt.Color( 0x7f7fff ) );
					g2.fillRect( X_OFFSET, Y_OFFSET + TITLE_HEIGHT, LEADING_WIDTH, HEIGHT - TITLE_HEIGHT );

					g2.setPaint( new java.awt.Color( 0xada7d0 ) );
					g2.fillRect( X_OFFSET + LEADING_WIDTH, Y_OFFSET + TITLE_HEIGHT, WIDTH - LEADING_WIDTH, HEIGHT - TITLE_HEIGHT );

					g2.setPaint( java.awt.Color.DARK_GRAY );
					g2.draw3DRect( X_OFFSET, Y_OFFSET, WIDTH, HEIGHT, true );
				}

			} else {
				java.util.List<org.alice.stageide.modelresource.ResourceNode> nodeChildren = resourceNode.getNodeChildren();
				if( nodeChildren.size() > 1 ) {
					String s = Integer.toString( nodeChildren.size() );
					java.awt.FontMetrics fm = g2.getFontMetrics();

					java.awt.geom.Rectangle2D actualTextBounds = fm.getStringBounds( s, g2 );
					java.awt.geom.Rectangle2D minimumTextBounds = fm.getStringBounds( "00", g2 );

					java.awt.geom.Rectangle2D textBounds;
					if( actualTextBounds.getWidth() > minimumTextBounds.getWidth() ) {
						textBounds = actualTextBounds;
					} else {
						textBounds = minimumTextBounds;
					}

					java.awt.Shape shape = createShapeAround( textBounds );
					java.awt.geom.Rectangle2D shapeBounds = shape.getBounds();

					double xTranslate = ( x + width ) - shapeBounds.getWidth() - 4;
					double yTranslate = y + shapeBounds.getHeight();

					g2.translate( xTranslate, yTranslate );
					try {
						g2.setPaint( new java.awt.Color( 221, 221, 191 ) );
						g2.fill( shape );
						g2.setPaint( java.awt.Color.GRAY );
						g2.draw( shape );

						g2.setPaint( java.awt.Color.BLACK );
						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, s, textBounds );
					} finally {
						g2.translate( -xTranslate, -yTranslate );
					}
				}
			}
		}
	}
}
