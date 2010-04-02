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

package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class TabbedPane extends javax.swing.JPanel implements edu.cmu.cs.dennisc.swing.event.TabListener {
	private javax.swing.JPanel m_tabsPanel = new javax.swing.JPanel();
	private java.awt.CardLayout m_cardLayout = new java.awt.CardLayout();
	private javax.swing.JPanel m_cardPane = new javax.swing.JPanel();

	//todo: remove double accounting?
	private java.util.List< Tab > m_tabs = new java.util.LinkedList< Tab >();

	private static String getNameForCardLayout( Tab tab ) {
		return Integer.toHexString( tab.hashCode() );
	}
	
	public TabbedPane( boolean isHorizontalFilled ) {
		setLayout( new java.awt.BorderLayout() );
		if( isHorizontalFilled ) {
			m_tabsPanel.setLayout( new javax.swing.BoxLayout( m_tabsPanel, javax.swing.BoxLayout.LINE_AXIS ) );
		} else {
			m_tabsPanel.setLayout( new java.awt.FlowLayout( java.awt.FlowLayout.LEFT, 0, 0 ) );
		}
		add( m_tabsPanel, java.awt.BorderLayout.NORTH );
		m_cardPane.setLayout( m_cardLayout );
		add( m_cardPane, java.awt.BorderLayout.CENTER );
	}
	public void addTab( Tab tab ) {
		m_tabs.add( tab );
		m_tabsPanel.add( tab );
		tab.addTabListener( this );
		m_cardPane.add( tab.getTabPane(), getNameForCardLayout( tab ) );
	}
	public void removeTab( Tab tab ) {
		tab.removeTabListener( this );
		m_tabs.remove( tab );
		m_tabsPanel.remove( tab );
		m_cardPane.remove( tab.getTabPane() );
	}
	
	public void clearTabs() {
		Tab[] array = new Tab[ m_tabs.size() ];
		m_tabs.toArray( array );
		for( Tab tab : array ) {
			removeTab( tab );
		}
	}
	
	public int getTabCount() {
		return m_tabs.size();
	}
	public Tab getTabAt( int i ) {
		return m_tabs.get( i );
	}

	public void tabSelected( edu.cmu.cs.dennisc.swing.event.TabEvent e ) {
		selectTab( e.getTypedSource() );
	}
	public void tabClosed( edu.cmu.cs.dennisc.swing.event.TabEvent e ) {
		Tab tab = e.getTypedSource();
		m_tabs.remove( tab );
		if( tab.isSelected() ) {
			if( m_tabs.isEmpty() ) {
				//pass
			} else {
				selectTab( m_tabs.get( 0 ) );
			}
		}
		m_tabsPanel.remove( tab );
		m_cardPane.remove( tab.getTabPane() );
		m_tabsPanel.revalidate();
		TabbedPane.this.repaint();
	}

	private Tab selectedTab = null;
	public void selectTab( Tab tab ) {
		if( this.selectedTab == tab ) {
			//pass
		} else {
			this.selectedTab = tab;
			m_cardLayout.show( m_cardPane, getNameForCardLayout( tab ) );
			for( Tab t : m_tabs ) {
				t.setSelected( t == tab );
			}
			repaint();
		}
	}
	
	public Tab getSelectedTab() {
//		for( Tab tab : m_tabs ) {
//			if( tab.isSelected() ) {
//				return tab;
//			}
//		}
//		return null;
		return this.selectedTab;
	}
	
	@Override
	protected void paintChildren( java.awt.Graphics g ) {
		super.paintChildren( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		for( Tab tab : m_tabs ) {
			if( tab.isSelected() ) {
				int x0 = m_cardPane.getX();
				int x1 = m_cardPane.getX() + m_cardPane.getWidth();
				int y = m_cardPane.getY();
				g2.setPaint( java.awt.Color.GRAY );
				g2.drawLine( x0, y, x1, y );
				x0 = tab.getX();
				x1 = tab.getX() + tab.getWidth() - 2;
				g2.setPaint( tab.getTabPane().getBackground() );
				g2.drawLine( x0, y, x1, y );
			}
		}
	}
}
