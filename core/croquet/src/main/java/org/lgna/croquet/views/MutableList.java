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

package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.awt.KnurlUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.javax.swing.ClearableButtonGroup;
import org.lgna.croquet.data.MutableListData;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Dennis Cosgrove
 */
public abstract class MutableList<E> extends SwingComponentView<JPanel> {

	private class MutableListLayout implements LayoutManager {
		@Override
		public void addLayoutComponent( String name, Component comp ) {
		}

		@Override
		public void removeLayoutComponent( Component comp ) {
		}

		@Override
		public Dimension minimumLayoutSize( Container parent ) {
			return new Dimension( 0, 0 );
		}

		@Override
		public Dimension preferredLayoutSize( Container parent ) {
			final int M = data.getItemCount();
			Dimension rv = new Dimension( 0, 0 );
			for( int i = 0; i < M; i++ ) {
				Component componentI = parent.getComponent( i );
				Dimension size = componentI.getPreferredSize();
				rv.width = Math.max( rv.width, size.width );
				rv.height += size.height;
			}
			return rv;
		}

		@Override
		public void layoutContainer( Container parent ) {
			Dimension parentSize = parent.getSize();
			int y = 0;
			final int N = parent.getComponentCount();
			final int M = data.getItemCount();
			for( int i = 0; i < M; i++ ) {
				Component componentI = parent.getComponent( i );
				componentI.setLocation( 0, y );
				int height = componentI.getPreferredSize().height;
				componentI.setSize( parentSize.width, height );
				y += height;
			}
			for( int i = M; i < N; i++ ) {
				Component componentI = parent.getComponent( i );
				componentI.setSize( 0, 0 );
			}
		}
	}

	private static Color BASE_COLOR = new Color( 221, 221, 255 );
	private static Color HIGHLIGHT_COLOR = BASE_COLOR.brighter();

	private static Color SELECTED_BASE_COLOR = new Color( 57, 105, 138 );
	private static Color SELECTED_HIGHLIGHT_COLOR = SELECTED_BASE_COLOR.brighter();

	protected abstract class JItemAtIndexButton extends JToggleButton {
		public JItemAtIndexButton() {
			this.setOpaque( false );
			this.setBorder( BorderFactory.createEmptyBorder( 4, 14, 4, 4 ) );
			this.setLayout( new BorderLayout() );
			this.setRolloverEnabled( true );
		}

		public abstract void update();

		@Override
		public void updateUI() {
			this.setUI( new BasicButtonUI() );
		}

		@Override
		protected void paintComponent( Graphics g ) {
			//super.paintComponent( g );
			ButtonModel model = this.getModel();
			Paint paint;
			int width = this.getWidth() - 1;
			int height = this.getHeight() - 1;
			if( model.isSelected() ) {
				if( model.isRollover() ) {
					paint = new GradientPaint( 0, 0, SELECTED_HIGHLIGHT_COLOR, 0, height, SELECTED_BASE_COLOR );
				} else {
					paint = SELECTED_BASE_COLOR;
				}
			} else {
				if( model.isRollover() ) {
					paint = new GradientPaint( 0, 0, HIGHLIGHT_COLOR, 0, height, BASE_COLOR );
				} else {
					paint = BASE_COLOR;
				}
			}
			Graphics2D g2 = (Graphics2D)g;
			if( paint != null ) {
				Object prevAntialiasing = g2.getRenderingHint( RenderingHints.KEY_ANTIALIASING );
				g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

				g2.setPaint( paint );
				g2.fillRoundRect( 0, 0, width, height, 8, 8 );
				g2.setPaint( Color.DARK_GRAY );
				g2.drawRoundRect( 0, 0, width, height, 8, 8 );
				if( model.isRollover() ) {
					paint = Color.LIGHT_GRAY;
				} else {
					paint = Color.GRAY;
				}
				g2.setPaint( paint );
				KnurlUtilities.paintKnurl5( g, 2, 2, 6, height - 5 );

				g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
			} else {
				//						g2.setPaint( MutableList.this.getUnselectedBackgroundColor() );
				//						g.clearRect( 0, 0, width, height );
			}
		}
	}

	private final MutableListData<E> data;
	private final java.util.List<JItemAtIndexButton> jButtons = Lists.newCopyOnWriteArrayList();
	private final ClearableButtonGroup buttonGroup = new ClearableButtonGroup();
	private final ListDataListener listDataListener = new ListDataListener() {
		@Override
		public void intervalAdded( ListDataEvent e ) {
			MutableList.this.handleListDataChanged();
		}

		@Override
		public void intervalRemoved( ListDataEvent e ) {
			MutableList.this.handleListDataChanged();
		}

		@Override
		public void contentsChanged( ListDataEvent e ) {
			MutableList.this.handleListDataChanged();
		}
	};

	public MutableList( MutableListData<E> data ) {
		this.data = data;
		this.data.addListener( this.listDataListener );
		this.handleListDataChanged();
	}

	public MutableListData<E> getData() {
		return this.data;
	}

	public void clearSelection() {
		this.buttonGroup.clearSelectedModel();
	}

	protected abstract JItemAtIndexButton createJItemAtIndexButton( int index );

	private void createAndAddJButton( int index ) {
		JItemAtIndexButton jButton = this.createJItemAtIndexButton( index );
		jButton.setFocusable( true );
		this.getAwtComponent().add( jButton );
		this.jButtons.add( jButton );
		this.buttonGroup.add( jButton );
	}

	private void handleListDataChanged() {
		synchronized( this.data ) {
			final int N = data.getItemCount();
			synchronized( this.getTreeLock() ) {
				for( int i = this.jButtons.size(); i < N; i++ ) {
					this.createAndAddJButton( i );
				}
				for( JItemAtIndexButton jButton : this.jButtons ) {
					jButton.update();
				}
			}
		}
		this.clearSelection();
		this.revalidateAndRepaint();
	}

	@Override
	protected JPanel createAwtComponent() {
		JPanel rv = new JPanel();
		rv.setLayout( new MutableListLayout() );
		return rv;
	}

	private static final KeyStroke DELETE_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 );
	private static final KeyStroke BACK_SPACE_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 );
	//note: ups/downs do not seem to work
	private static final KeyStroke UP_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0 );
	private static final KeyStroke KEYPAD_UP_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_KP_UP, 0 );
	private static final KeyStroke DOWN_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0 );
	private static final KeyStroke KEYPAD_DOWN_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_KP_DOWN, 0 );
	private final ActionListener removeSelectedListener = new ActionListener() {
		@Override
		public void actionPerformed( ActionEvent e ) {
			System.out.println( "removeSelectedItem" );
			//MutableList.this.removeSelectedItem();
		}
	};
	private final ActionListener moveSelectionUpListener = new ActionListener() {
		@Override
		public void actionPerformed( ActionEvent e ) {
			System.out.println( "moveSelectionUp" );
		}
	};
	private final ActionListener moveSelectionDownListener = new ActionListener() {
		@Override
		public void actionPerformed( ActionEvent e ) {
			System.out.println( "moveSelectionDown" );
		}
	};

	public void registerKeyboardActions() {
		this.registerKeyboardAction( this.removeSelectedListener, DELETE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.removeSelectedListener, BACK_SPACE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionUpListener, UP_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionUpListener, KEYPAD_UP_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionDownListener, DOWN_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.moveSelectionDownListener, KEYPAD_DOWN_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
	}

	public void unregisterKeyboardActions() {
		this.unregisterKeyboardAction( KEYPAD_DOWN_KEY_STROKE );
		this.unregisterKeyboardAction( DOWN_KEY_STROKE );
		this.unregisterKeyboardAction( KEYPAD_UP_KEY_STROKE );
		this.unregisterKeyboardAction( UP_KEY_STROKE );
		this.unregisterKeyboardAction( BACK_SPACE_KEY_STROKE );
		this.unregisterKeyboardAction( DELETE_KEY_STROKE );
	}
}
