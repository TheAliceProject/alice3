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
package org.lgna.croquet.views.imp;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class ScrollingPopupMenuLayout implements java.awt.LayoutManager2 {
	private final java.util.List<java.awt.Component> mainItems = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.Map<java.awt.Component, Integer> mapSideItemToIndex = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private java.awt.Component pageStartComponent;
	private java.awt.Component pageEndComponent;

	private javax.swing.SizeRequirements[] childWidthRequirements;
	private javax.swing.SizeRequirements[] childHeightRequirements;
	private javax.swing.SizeRequirements widthRequirement;
	private javax.swing.SizeRequirements heightRequirement;

	private int index0;

	public static enum ScrollConstraint {
		PAGE_START,
		PAGE_END;
	}

	public static enum ColumnConstraint {
		MAIN,
		SIDE
	}

	private final java.awt.Container target;

	public ScrollingPopupMenuLayout( java.awt.Container target ) {
		this.target = target;
	}

	private void constrainIndex() {
		final int N = this.mainItems.size();
		this.index0 = Math.max( this.index0, 0 );
		this.index0 = Math.min( this.index0, N - 1 );
	}

	public void adjustIndex( int delta ) {
		this.index0 += delta;
		this.constrainIndex();
		if( this.mainItems.get( this.index0 ) instanceof javax.swing.JPopupMenu.Separator ) {
			this.index0 += delta;
			this.constrainIndex();
		}
		this.layoutContainer( this.target );
	}

	@Override
	public synchronized float getLayoutAlignmentX( java.awt.Container target ) {
		this.updateRequirementsIfNecessary( target );
		return this.widthRequirement.alignment;
	}

	@Override
	public synchronized float getLayoutAlignmentY( java.awt.Container target ) {
		this.updateRequirementsIfNecessary( target );
		return this.heightRequirement.alignment;
	}

	@Override
	public void addLayoutComponent( String name, java.awt.Component comp ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, name, comp );
		this.invalidateLayout( comp.getParent() );
	}

	@Override
	public void addLayoutComponent( java.awt.Component comp, Object constraints ) {
		if( constraints != null ) {
			//pass
		} else {
			constraints = ColumnConstraint.MAIN;
		}
		if( constraints == ColumnConstraint.MAIN ) {
			this.mainItems.add( comp );
		} else if( constraints == ColumnConstraint.SIDE ) {
			final boolean IS_SIDE_READY_FOR_PRIME_TIME = true;
			if( IS_SIDE_READY_FOR_PRIME_TIME ) {
				this.mapSideItemToIndex.put( comp, this.mainItems.size() - 1 );
			} else {
				this.mainItems.add( comp );
			}
		} else if( constraints == ScrollConstraint.PAGE_START ) {
			this.pageStartComponent = comp;
		} else if( constraints == ScrollConstraint.PAGE_END ) {
			this.pageEndComponent = comp;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, comp, constraints );
		}
		this.invalidateLayout( comp.getParent() );
	}

	@Override
	public void removeLayoutComponent( java.awt.Component comp ) {
		this.invalidateLayout( comp.getParent() );
		if( this.mainItems.contains( comp ) ) {
			this.mainItems.remove( comp );
			this.constrainIndex();
		} else {
			if( ( comp == this.pageStartComponent ) || ( comp == this.pageEndComponent ) ) {
				//todo
			} else {
				if( this.mapSideItemToIndex.containsKey( comp ) ) {
					this.mapSideItemToIndex.remove( comp );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( comp );
				}
			}
		}
	}

	@Override
	public synchronized void invalidateLayout( java.awt.Container target ) {
		this.index0 = 0;
		this.childWidthRequirements = null;
		this.childHeightRequirements = null;
		this.widthRequirement = null;
		this.heightRequirement = null;
	}

	private void updateRequirementsIfNecessary( java.awt.Container target ) {
		if( this.childWidthRequirements != null ) {
			//pass
		} else {
			final int N = this.mainItems.size();
			this.childWidthRequirements = new javax.swing.SizeRequirements[ N ];
			this.childHeightRequirements = new javax.swing.SizeRequirements[ N ];
			int i = 0;
			for( java.awt.Component componentI : this.mainItems ) {
				java.awt.Dimension minimum;
				java.awt.Dimension preferred;
				java.awt.Dimension maximum;
				if( componentI.isVisible() ) {
					minimum = componentI.getMinimumSize();
					preferred = componentI.getPreferredSize();
					maximum = componentI.getMaximumSize();
				} else {
					minimum = new java.awt.Dimension();
					preferred = new java.awt.Dimension();
					maximum = new java.awt.Dimension();
				}
				this.childWidthRequirements[ i ] = new javax.swing.SizeRequirements( minimum.width, preferred.width, maximum.width, componentI.getAlignmentX() );
				this.childHeightRequirements[ i ] = new javax.swing.SizeRequirements( minimum.height, preferred.height, maximum.height, componentI.getAlignmentY() );
				i += 1;
			}
			this.widthRequirement = javax.swing.SizeRequirements.getAlignedSizeRequirements( this.childWidthRequirements );
			this.heightRequirement = javax.swing.SizeRequirements.getTiledSizeRequirements( this.childHeightRequirements );
		}
	}

	private static final int SIDE_WIDTH = 20;

	private int getSizeWidth() {
		return this.mapSideItemToIndex.size() > 0 ? SIDE_WIDTH : 0;
	}

	private java.awt.Dimension getInsetSize( int width, int height, java.awt.Container target ) {
		java.awt.Insets insets = target.getInsets();
		int sideWidth = this.getSizeWidth();
		return new java.awt.Dimension( width + insets.left + insets.right + sideWidth, height + insets.top + insets.bottom );
	}

	@Override
	public synchronized java.awt.Dimension minimumLayoutSize( java.awt.Container target ) {
		this.updateRequirementsIfNecessary( target );
		return this.getInsetSize( this.widthRequirement.minimum, this.heightRequirement.minimum, target );
	}

	private java.awt.Dimension actualPreferredLayoutSize( java.awt.Container target ) {
		return this.getInsetSize( this.widthRequirement.preferred, this.heightRequirement.preferred, target );
	}

	@Override
	public synchronized java.awt.Dimension preferredLayoutSize( java.awt.Container target ) {
		this.invalidateLayout( target );
		this.updateRequirementsIfNecessary( target );
		java.awt.Dimension rv = this.actualPreferredLayoutSize( target );
		java.awt.GraphicsConfiguration graphicsConfiguration = target.getGraphicsConfiguration();
		if( graphicsConfiguration != null ) {
			//pass
		} else {
			if( target instanceof javax.swing.JPopupMenu ) {
				javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)target;
				java.awt.Component invoker = jPopupMenu.getInvoker();
				if( invoker != null ) {
					graphicsConfiguration = invoker.getGraphicsConfiguration();
				}
			}
			if( graphicsConfiguration != null ) {
				//pass
			} else {
				java.awt.GraphicsEnvironment graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
				graphicsConfiguration = graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: determine correct graphicsConfiguration.  using default:", graphicsConfiguration );
			}
		}
		java.awt.Rectangle bounds = graphicsConfiguration.getBounds();
		rv.height = Math.min( rv.height, bounds.height - 64 );
		return rv;
	}

	@Override
	public synchronized java.awt.Dimension maximumLayoutSize( java.awt.Container target ) {
		this.updateRequirementsIfNecessary( target );
		return this.getInsetSize( this.widthRequirement.maximum, this.heightRequirement.maximum, target );
	}

	private static int calculateLastIndex( int firstIndex, int[] heights, int availableHeight ) {
		int usedHeight = 0;
		int lastIndex = firstIndex;
		for( int i = firstIndex; i < heights.length; i++ ) {
			usedHeight += heights[ i ];
			if( usedHeight <= availableHeight ) {
				lastIndex = i;
			} else {
				break;
			}
		}
		return lastIndex;
	}

	@Override
	public synchronized void layoutContainer( java.awt.Container target ) {
		this.updateRequirementsIfNecessary( target );
		java.awt.Dimension layoutSize = this.actualPreferredLayoutSize( target );
		java.awt.Insets insets = target.getInsets();
		int layoutWidth = layoutSize.width - insets.left - insets.right;
		int layoutHeight = layoutSize.height - insets.top - insets.bottom;

		final int N = this.mainItems.size();
		int[] xs = new int[ N ];
		int[] ys = new int[ N ];
		int[] widths = new int[ N ];
		int[] heights = new int[ N ];

		javax.swing.SizeRequirements.calculateAlignedPositions( layoutWidth, this.widthRequirement, this.childWidthRequirements, xs, widths );
		javax.swing.SizeRequirements.calculateTiledPositions( layoutHeight, this.heightRequirement, this.childHeightRequirements, ys, heights );

		java.awt.Dimension size = target.getSize();
		int firstIndex;
		int lastIndex;
		int pageStartHeight;
		if( size.height < layoutSize.height ) {
			firstIndex = this.index0;

			if( this.pageStartComponent != null ) {
				if( firstIndex > 0 ) {
					pageStartHeight = this.pageStartComponent.getPreferredSize().height;
				} else {
					pageStartHeight = 0;
				}
				this.pageStartComponent.setBounds( 0, 0, size.width, pageStartHeight );
			} else {
				pageStartHeight = 0;
			}

			lastIndex = calculateLastIndex( firstIndex, heights, size.height - pageStartHeight );

			int pageEndHeight;
			if( this.pageEndComponent != null ) {
				if( lastIndex < ( N - 1 ) ) {
					pageEndHeight = this.pageEndComponent.getPreferredSize().height;
				} else {
					pageEndHeight = 0;
				}
				this.pageEndComponent.setBounds( 0, size.height - pageEndHeight, size.width, pageEndHeight );
			} else {
				pageEndHeight = 0;
			}

			if( pageEndHeight > 0 ) {
				lastIndex = calculateLastIndex( firstIndex, heights, size.height - pageStartHeight - pageEndHeight );
			}

			if( lastIndex == ( N - 1 ) ) {
				int availableHeight = size.height - pageStartHeight;
				int usedHeight = 0;
				int i = lastIndex;
				while( i >= 0 ) {
					usedHeight += heights[ i ];
					if( usedHeight <= availableHeight ) {
						firstIndex = i;
						i--;
					} else {
						break;
					}
				}
				this.index0 = firstIndex;
			}

			if( this.pageStartComponent instanceof JScrollMenuItem ) {
				JScrollMenuItem pageStartScrollMenuItem = (JScrollMenuItem)this.pageStartComponent;
				pageStartScrollMenuItem.setCount( firstIndex );
			}
			if( this.pageEndComponent instanceof JScrollMenuItem ) {
				JScrollMenuItem pageEndScrollMenuItem = (JScrollMenuItem)this.pageEndComponent;
				pageEndScrollMenuItem.setCount( N - 1 - lastIndex );
			}
		} else {
			pageStartHeight = 0;
			firstIndex = 0;
			lastIndex = N - 1;
			if( this.pageStartComponent != null ) {
				this.pageStartComponent.setBounds( 0, 0, 0, 0 );
			}
			if( this.pageEndComponent != null ) {
				this.pageEndComponent.setBounds( 0, 0, 0, 0 );
			}
		}
		int sideWidth = this.getSizeWidth();
		int i = 0;
		for( java.awt.Component component : this.mainItems ) {
			if( ( firstIndex <= i ) && ( i <= lastIndex ) ) {
				component.setBounds( insets.left + xs[ i ], ( ( insets.top + ys[ i ] ) + pageStartHeight ) - ys[ firstIndex ], widths[ i ] - sideWidth, heights[ i ] );
			} else {
				component.setBounds( 0, 0, 0, 0 );
			}
			i++;
		}

		if( this.mapSideItemToIndex.size() > 0 ) {
			for( java.awt.Component component : this.mapSideItemToIndex.keySet() ) {
				i = this.mapSideItemToIndex.get( component );
				if( ( firstIndex <= i ) && ( i <= lastIndex ) ) {
					component.setBounds( size.width - sideWidth, ( ( insets.top + ys[ i ] ) + pageStartHeight ) - ys[ firstIndex ], sideWidth, heights[ i ] );
				} else {
					component.setBounds( 0, 0, 0, 0 );
				}
			}
		}

	}
}
