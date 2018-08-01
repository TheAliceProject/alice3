/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.croquet.components.gallerybrowser;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.GoldenRatio;
import org.alice.ide.DefaultTheme;
import org.alice.ide.croquet.components.KnurlDragComponent;
import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel;
import org.alice.stageide.icons.IconFactoryManager;
import org.alice.stageide.icons.PlusIconFactory;
import org.alice.stageide.modelresource.*;
import org.lgna.croquet.Model;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.icon.IconSize;
import org.lgna.croquet.triggers.MouseEventTrigger;
import org.lgna.croquet.views.HorizontalTextPosition;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.VerticalAlignment;
import org.lgna.croquet.views.VerticalTextPosition;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.ModelResource;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class GalleryDragComponent extends KnurlDragComponent<GalleryDragModel> {
	private final Color baseColor;
	private final Color highlightColor;
	private final Color shadowColor;
	private final Color activeHighlightColor;
	private final Color activeShadowColor;

	private static final class SuperclassIconLabel extends SwingComponentView<JLabel> {
		private final Class<?> modelResourceInterface;

		public SuperclassIconLabel( Class<?> modelResourceInterface ) {
			this.modelResourceInterface = modelResourceInterface;
		}

		@Override
		protected JLabel createAwtComponent() {
			JLabel rv = new JLabel() {
				private final JToolTip toolTipForTipLocation = new JToolTip();

				@Override
				public Point getToolTipLocation( MouseEvent event ) {
					toolTipForTipLocation.setTipText( this.getToolTipText() );
					int offset = toolTipForTipLocation.getPreferredSize().height;
					offset += 4;
					return new Point( 0, -offset );
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

	public GalleryDragComponent( GalleryDragModel model ) {
		super( model, false );

		if (model.isUserDefinedModel()) {
			this.baseColor = ColorUtilities.scaleHSB(DefaultTheme.DEFAULT_CONSTRUCTOR_COLOR, 1.0, 2.0, 1.0);
			this.highlightColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 1.4 );
			this.shadowColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 0.9, 0.8 );
			this.activeHighlightColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 2.0 );
			this.activeShadowColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 0.9 );
		}
		else if( model.isInstanceCreator() ) {
			this.baseColor = DefaultTheme.DEFAULT_CONSTRUCTOR_COLOR;
			this.highlightColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 1.4 );
			this.shadowColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 0.9, 0.8 );
			this.activeHighlightColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 2.0 );
			this.activeShadowColor = ColorUtilities.scaleHSB( this.baseColor, 1.0, 1.0, 0.9 );
		} else {
			this.baseColor = ColorUtilities.createGray( 191 );
			this.highlightColor = ColorUtilities.createGray( 221 );
			this.shadowColor = ColorUtilities.createGray( 171 );
			this.activeHighlightColor = ColorUtilities.createGray( 255 );
			this.activeShadowColor = ColorUtilities.createGray( 181 );
		}

		Class<?> modelResourceCls = null;
		if( model instanceof ResourceNode ) {
			if( model instanceof ClassHierarchyBasedResourceNode ) {
				//pass
			} else {
				ResourceNode resourceNode = (ResourceNode)model;
				ResourceKey resourceKey = resourceNode.getResourceKey();
				if( resourceKey instanceof InstanceCreatorKey ) {
					InstanceCreatorKey instanceCreatorKey = (InstanceCreatorKey)resourceKey;
					modelResourceCls = instanceCreatorKey.getModelResourceCls();
				}
			}
		} else if( model instanceof UriGalleryDragModel ) {
			UriGalleryDragModel uriGalleryDragModel = (UriGalleryDragModel)model;
			InstanceCreatorKey resourceKey = uriGalleryDragModel.getResourceKey();
			if( resourceKey != null ) {
				modelResourceCls = resourceKey.getModelResourceCls();
			}

			Label label = new Label( PlusIconFactory.getInstance().getIcon( IconSize.SMALL.getSize() ) );
			label.setToolTipText( uriGalleryDragModel.getTypeSummaryToolTipText() );
			label.setVerticalAlignment( VerticalAlignment.BOTTOM );
			this.internalAddComponent( label, GalleryDragLayoutManager.TOP_RIGHT_CONSTRAINT );
		}
		if( modelResourceCls != null ) {
			if( modelResourceCls.isEnum() ) {
				Class<?>[] modelResourceInterfaces = modelResourceCls.getInterfaces();
				if( modelResourceInterfaces.length > 0 ) {
					Class<?> modelResourceInterface = modelResourceInterfaces[ 0 ];
					if( ModelResource.class.isAssignableFrom( modelResourceInterface ) ) {
						IconFactory iconFactory = IconFactoryManager.getIconFactoryForResourceCls( (Class<ModelResource>)modelResourceInterface );
						if( iconFactory != null ) {
							final Dimension SUPER_CLASS_ICON_SIZE = new Dimension( 32, 24 );
							Icon icon = iconFactory.getIcon( SUPER_CLASS_ICON_SIZE );
							SuperclassIconLabel superclsLabel = new SuperclassIconLabel( modelResourceInterface );
							superclsLabel.getAwtComponent().setIcon( icon );
							this.internalAddComponent( superclsLabel, GalleryDragLayoutManager.TOP_LEFT_CONSTRAINT );
						}
					}
				}
			}
		}

		Label label = new Label();
		label.setText( model.getText() );
		IconFactory iconFactory = model.getIconFactory();
		label.setIcon( iconFactory != null ? iconFactory.getIcon( model.getIconSize() ) : null );
		label.setVerticalTextPosition( VerticalTextPosition.BOTTOM );
		label.setHorizontalTextPosition( HorizontalTextPosition.CENTER );

		this.internalAddComponent( label, GalleryDragLayoutManager.BASE_CONSTRAINT );
		this.setBackgroundColor( this.baseColor );
		this.setMaximumSizeClampedToPreferredSize( true );
		this.setAlignmentY( Component.TOP_ALIGNMENT );
	}

	@Override
	protected boolean isClickAndClackAppropriate() {
		GalleryDragModel model = this.getModel();
		return model.isClickAndClackAppropriate();
	}

	private static class GalleryDragLayoutManager implements LayoutManager {
		private static String BASE_CONSTRAINT = "TOP_LEFT_CONSTRAINT_BASE";
		private static String TOP_LEFT_CONSTRAINT = "TOP_LEFT_CONSTRAINT";
		private static String TOP_RIGHT_CONSTRAINT = "TOP_RIGHT_CONSTRAINT";

		private final List<Component> topLeftComponents = Lists.newCopyOnWriteArrayList();
		private final List<Component> topRightComponents = Lists.newCopyOnWriteArrayList();
		private Component baseComponent;

		@Override
		public void addLayoutComponent( String name, Component comp ) {
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
		public void removeLayoutComponent( Component comp ) {
		}

		@Override
		public Dimension minimumLayoutSize( Container parent ) {
			return new Dimension();
		}

		@Override
		public Dimension preferredLayoutSize( Container parent ) {
			//note: ridiculous
			Dimension rv = this.baseComponent.getPreferredSize();
			Insets insets = parent.getInsets();
			rv.width += insets.left + insets.right;
			rv.height += insets.top + insets.bottom;
			return rv;
		}

		@Override
		public void layoutContainer( Container parent ) {
			//note: ridiculous
			Insets insets = parent.getInsets();
			Dimension parentSize = parent.getSize();
			final int N = parent.getComponentCount();
			for( int i = 0; i < N; i++ ) {
				Component awtComponent = parent.getComponent( N - i - 1 );
				awtComponent.setSize( awtComponent.getPreferredSize() );
				awtComponent.setLocation( insets.left, insets.top );
			}
			for( Component awtComponent : this.topLeftComponents ) {
				awtComponent.setLocation( insets.left, insets.top );
			}
			for( Component awtComponent : this.topRightComponents ) {
				awtComponent.setLocation( parentSize.width - awtComponent.getWidth() - insets.right, insets.top );
			}
		}
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jComponent ) {
		return new GalleryDragLayoutManager();
	}

	@Override
	protected void handleLeftMouseButtonQuoteClickedUnquote( MouseEvent e ) {
		super.handleLeftMouseButtonQuoteClickedUnquote( e );
		switch( e.getClickCount() ) {
		case 1:
			Model leftButtonClickModel = this.getModel().getLeftButtonClickModel();
			if( leftButtonClickModel != null ) {
				leftButtonClickModel.fire( MouseEventTrigger.createUserInstance( this, e ) );
			}
			break;
		}
	}

	@Override
	protected void handleBackButtonClicked( MouseEvent e ) {
		super.handleBackButtonClicked( e );
		Logger.outln( "todo: back" );
	}

	@Override
	protected void handleForwardButtonClicked( MouseEvent e ) {
		super.handleForwardButtonClicked( e );
		Logger.outln( "todo: forward" );
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
	protected RoundRectangle2D.Float createShape( int x, int y, int width, int height ) {
		return new RoundRectangle2D.Float( x, y, width - 1, height - 1, 8, 8 );
	}

	@Override
	protected void fillBounds( Graphics2D g2, int x, int y, int width, int height ) {
		g2.fill( this.createShape( x, y, width, height ) );
	}

	@Override
	protected void paintPrologue( Graphics2D g2, int x, int y, int width, int height ) {
		RoundRectangle2D.Float shape = this.createShape( x, y, width, height );
		int y1 = y + height;
		int yCenter = y + ( height / 2 );
		int yA = y + ( height / 3 );
		int yB = y1 - ( height / 3 );

		Color highlightColor = this.isActive() ? this.activeHighlightColor : this.highlightColor;
		Color shadowColor = this.isActive() ? this.activeShadowColor : this.shadowColor;

		GradientPaint paintTop = new GradientPaint( x, y, highlightColor, x, yA, shadowColor );
		GradientPaint paintBottom = new GradientPaint( x, yB, shadowColor, x, y1, highlightColor );

		Paint prevPaint = g2.getPaint();
		Shape prevClip = g2.getClip();

		try {
			Area topArea = AreaUtilities.createIntersection( prevClip, new Rectangle( x, y, width, yCenter - y ) );
			g2.setClip( topArea );
			g2.setPaint( paintTop );
			g2.fill( shape );

			Area bottomArea = AreaUtilities.createIntersection( prevClip, new Rectangle( x, yCenter, width, y1 - yCenter ) );
			g2.setClip( bottomArea );
			g2.setPaint( paintBottom );
			g2.fill( shape );
		} finally {
			g2.setClip( prevClip );
			g2.setPaint( prevPaint );
		}
	}

	private static Shape createShapeAround( Rectangle2D bounds ) {
		float x0 = (float)( bounds.getX() - 2 );
		float y0 = (float)( bounds.getY() - 4 );
		float x1 = (float)( x0 + bounds.getWidth() + 4 );
		float y1 = (float)( y0 + bounds.getHeight() + 5 );

		final int TAB_LENGTH = 6;

		GeneralPath rv = new GeneralPath();
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
	protected void paintEpilogue( Graphics2D g2, int x, int y, int width, int height ) {
		super.paintEpilogue( g2, x, y, width, height );

		GalleryDragModel model = this.getModel();
		if( model instanceof ResourceNode ) {
			ResourceNode resourceNode = (ResourceNode)model;

			ResourceKey resourceKey = resourceNode.getResourceKey();
			if( NebulousIde.nonfree.isInstanceOfPersonResourceKey( resourceKey ) ) {

				final boolean IS_PERSON_EDITOR_ICON_DESIRED = false;

				if( IS_PERSON_EDITOR_ICON_DESIRED ) {
					final int PAD_X = 6;
					final int PAD_Y = 4;
					final int WIDTH = 24;
					final int HEIGHT = GoldenRatio.getShorterSideLength( WIDTH );

					final int X_OFFSET = ( x + width ) - WIDTH - PAD_X;
					final int Y_OFFSET = y + PAD_Y;

					final int TITLE_HEIGHT = 3;

					g2.setPaint( Color.BLUE );
					g2.fillRect( X_OFFSET, Y_OFFSET, WIDTH, TITLE_HEIGHT );

					final int LEADING_WIDTH = ( WIDTH * 2 ) / 5;

					g2.setPaint( new Color( 0x7f7fff ) );
					g2.fillRect( X_OFFSET, Y_OFFSET + TITLE_HEIGHT, LEADING_WIDTH, HEIGHT - TITLE_HEIGHT );

					g2.setPaint( new Color( 0xada7d0 ) );
					g2.fillRect( X_OFFSET + LEADING_WIDTH, Y_OFFSET + TITLE_HEIGHT, WIDTH - LEADING_WIDTH, HEIGHT - TITLE_HEIGHT );

					g2.setPaint( Color.DARK_GRAY );
					g2.draw3DRect( X_OFFSET, Y_OFFSET, WIDTH, HEIGHT, true );
				}

			} else {
				List<ResourceNode> nodeChildren = resourceNode.getNodeChildren();
				if( nodeChildren.size() > 1 ) {
					String s = Integer.toString( nodeChildren.size() );
					FontMetrics fm = g2.getFontMetrics();

					Rectangle2D actualTextBounds = fm.getStringBounds( s, g2 );
					Rectangle2D minimumTextBounds = fm.getStringBounds( "00", g2 );

					Rectangle2D textBounds;
					if( actualTextBounds.getWidth() > minimumTextBounds.getWidth() ) {
						textBounds = actualTextBounds;
					} else {
						textBounds = minimumTextBounds;
					}

					Shape shape = createShapeAround( textBounds );
					Rectangle2D shapeBounds = shape.getBounds();

					double xTranslate = ( x + width ) - shapeBounds.getWidth() - 4;
					double yTranslate = y + shapeBounds.getHeight();

					g2.translate( xTranslate, yTranslate );
					try {
						g2.setPaint( new Color( 221, 221, 191 ) );
						g2.fill( shape );
						g2.setPaint( Color.GRAY );
						g2.draw( shape );

						g2.setPaint( Color.BLACK );
						GraphicsUtilities.drawCenteredText( g2, s, textBounds );
					} finally {
						g2.translate( -xTranslate, -yTranslate );
					}
				}
			}
		}
	}
}
