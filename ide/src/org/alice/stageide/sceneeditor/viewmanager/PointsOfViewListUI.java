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
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass;
import edu.cmu.cs.dennisc.swing.ScrollUtilities;

/**
 * @author David Culyba
 */
public class PointsOfViewListUI extends JPanel implements ListDataListener{

	protected ListModel listModel;
	protected PointOfViewManager pointOfViewManager;
	protected JPanel listHolder;
	
	protected JScrollPane scrollPane;
	protected FlowLayout layout;
	
	protected List<PointOfViewControl> povList = new LinkedList<PointOfViewControl>();
	
	
	
	public PointsOfViewListUI(PointOfViewManager listManager)
	{
		super();
		this.pointOfViewManager = listManager;
		this.setListManager( listManager );
		this.layout = new FlowLayout(){
			private Dimension preferredLayoutSize;
			
			@Override
			public Dimension preferredLayoutSize(Container target)
			{
				return layoutSize(target, true);
			}

			@Override
			public Dimension minimumLayoutSize(Container target)
			{
				return layoutSize(target, false);
			}
			
			private Dimension layoutSize(Container target, boolean preferred)
			{
				//target = target.getParent();
				synchronized (target.getTreeLock())
				{
					//  Each row must fit with the width allocated to the containter.
					//  When the container width = 0, the preferred width of the container
					//  has not yet been calculated so lets ask for the maximum.
	
					int targetWidth = target.getSize().width;
	
					if (targetWidth == 0)
						targetWidth = Integer.MAX_VALUE;
	
					int hgap = getHgap();
					int vgap = getVgap();
					Insets insets = target.getInsets();
					int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
					int maxWidth = targetWidth - horizontalInsetsAndGap;
	
					//  Fit components into the allowed width
	
					Dimension dim = new Dimension(0, 0);
					int rowWidth = 0;
					int rowHeight = 0;
	
					int nmembers = target.getComponentCount();
	
					for (int i = 0; i < nmembers; i++)
					{
						Component m = target.getComponent(i);
	
						if (m.isVisible())
						{
							Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
	
							//  Can't add the component to current row. Start a new row.
	
							if (rowWidth + d.width > maxWidth)
							{
								addRow(dim, rowWidth, rowHeight);
								rowWidth = 0;
								rowHeight = 0;
							}
	
							//  Add a horizontal gap for all components after the first
	
							if (rowWidth != 0)
							{
								rowWidth += hgap;
							}
	
							rowWidth += d.width;
							rowHeight = Math.max(rowHeight, d.height);
						}
					}
	
					addRow(dim, rowWidth, rowHeight);
	
					dim.width += horizontalInsetsAndGap;
					dim.height += insets.top + insets.bottom + vgap * 2;
	
					//	When using a scroll pane or the DecoratedLookAndFeel we need to
					//  make sure the preferred size is less than the size of the
					//  target containter so shrinking the container size works
					//  correctly. Removing the horizontal gap is an easy way to do this.
	
					dim.width -= (hgap + 1);
	
					return dim;
				}
			}
			
			@Override
			public void layoutContainer(Container target)
			{
				//target = target.getParent();
		   		Dimension size = preferredLayoutSize(target);

				//  When a frame is minimized or maximized the preferred size of the
				//  Container is assumed not to change. Therefore we need to force a
				//  validate() to make sure that space, if available, is allocated to
				//  the panel using a WrapLayout.

		   		if (size.equals(preferredLayoutSize))
		   		{
		   			super.layoutContainer(target);
		   		}
		   		else
		   		{
		   			preferredLayoutSize = size;
		   			Container top = target;

		   			while (top.getParent() != null)
		   			{
		   				top = top.getParent();
		   			}

		   			top.validate();
		   		}
			}

			private void addRow(Dimension dim, int rowWidth, int rowHeight)
			{
				dim.width = Math.max(dim.width, rowWidth);

				if (dim.height > 0)
				{
					dim.height += getVgap();
				}

				dim.height += rowHeight;
			}
		};

		this.listHolder = new JPanel();
		this.listHolder.setOpaque( false );
		this.layout.setAlignment( FlowLayout.LEFT );
		this.listHolder.setLayout( this.layout );
		this.setLayout( new BorderLayout() );
		this.scrollPane = new JScrollPane(this.listHolder, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		this.scrollPane.setBorder( null );
		this.add( this.scrollPane, BorderLayout.CENTER );
		this.setOpaque( false );
	}
	
	public void setListManager(PointOfViewManager listManager)
	{
		if (this.listModel != listManager.getPointOfViewListModel())
		{
			if (this.listModel != null)
			{
				this.listModel.removeListDataListener( this );
			}
			this.listModel = listManager.getPointOfViewListModel();
			this.listModel.addListDataListener( this );
		}
	}

	private synchronized PointOfViewControl addPointOfView(PointOfView pov, int index)
	{
		PointOfViewControl control = new PointOfViewControl(pov, this.pointOfViewManager);
		this.listHolder.add( control, index );
		this.povList.add( control );
		return control;
	}
	
	private synchronized void removePointOfView(int index)
	{
		PointOfViewControl pov = (PointOfViewControl)this.getComponent( index );
		this.listHolder.remove( index );
		this.povList.remove( pov );
	}
	
	private synchronized List<PointOfViewControl> getControlList()
	{
		List<PointOfViewControl> toReturn = new LinkedList<PointOfViewControl>();
		for (PointOfViewControl control : this.povList)
		{
			toReturn.add(control);
		}
		return toReturn;
	}
	
	public void contentsChanged( ListDataEvent e ) {
		System.out.println("Changed: "+e);
		
	}

	public void intervalAdded( ListDataEvent e ) {
		JComponent toShow = null;
		for (int i=e.getIndex0(); i<=e.getIndex1(); i++)
		{
			PointOfView pov = (PointOfView)this.listModel.getElementAt( i );
			toShow = this.addPointOfView( pov, i );
		}
		ScrollUtilities.scrollToVisible( toShow );
		this.revalidate();
		this.repaint();
	}

	public void intervalRemoved( ListDataEvent e ) {
		for (int i=e.getIndex0(); i<=e.getIndex1(); i++)
		{
			this.removePointOfView( e.getIndex0() ); //after each "remove" the next item to remove will be at the lower index
		}
		this.revalidate();
		this.repaint();
		
	}
	
	public void updatePointOfViewImages(OffscreenLookingGlass offscreenLookingGlass)
	{
		List<PointOfViewControl> controlArray = getControlList();
		for (PointOfViewControl povControl : controlArray)
		{
			povControl.captureViewThumbnail(offscreenLookingGlass);
		}
	}
	
}
