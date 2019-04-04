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

package org.alice.stageide.properties;

import org.lgna.croquet.BooleanState;
import org.lgna.croquet.views.BooleanStateButton;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Dennis Cosgrove
 */
public class LinkScaleButton extends BooleanStateButton<AbstractButton> {
  public LinkScaleButton(BooleanState state) {
    super(state);
  }

  @Override
  protected AbstractButton createAwtComponent() {
    AbstractButton rv = new JToggleButton() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ButtonModel buttonModel = this.getModel();
        Color colorA;
        Color colorB;
        if (buttonModel.isPressed()) {
          colorA = Color.DARK_GRAY;
          colorB = Color.WHITE;
        } else if (buttonModel.isRollover()) {
          colorA = Color.WHITE;
          colorB = Color.DARK_GRAY;
        } else {
          colorA = null;
          colorB = null;
        }
        if ((colorA != null) && (colorB != null)) {
          int leftX = 0;
          int topY = 0;
          int rightX = this.getWidth() - 1;
          int bottomY = this.getHeight() - 1;
          g.setColor(colorA);
          g.drawLine(leftX, topY, rightX, topY);
          g.drawLine(leftX, topY, leftX, bottomY);
          g.setColor(colorB);
          g.drawLine(leftX, bottomY, rightX, bottomY);
          g.drawLine(rightX, topY, rightX, bottomY);
        }
      }

      @Override
      public void updateUI() {
        this.setUI(BasicToggleButtonUI.createUI(this));
      }
    };
    rv.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    rv.setRolloverEnabled(true);
    rv.setFocusable(false);
    rv.setRolloverEnabled(true);
    rv.setOpaque(false);
    return rv;
  }
}

//import java.awt.Color;
//import java.awt.Dimension;
//import java.net.URL;
//
//import javax.swing.ImageIcon;
//
//import org.lgna.croquet.BooleanState;
//
//public class LinkScaleButton extends org.lgna.croquet.components.BooleanStateButton<javax.swing.AbstractButton>
//{
//  private ImageIcon linkedIcon;
//  private ImageIcon unlinkedIcon;
//  private Dimension size;
//
//  public LinkScaleButton( BooleanState booleanState, URL linkedResource, URL unlinkedResource ) {
//    super( booleanState );
//    this.linkedIcon = new ImageIcon( linkedResource );
//    this.unlinkedIcon = new ImageIcon( unlinkedResource );
//    this.size = new Dimension( this.linkedIcon.getIconWidth(), this.linkedIcon.getIconHeight() );
//    this.updateLabel();
//  }
//
//  public LinkScaleButton( BooleanState booleanState ) {
//    this( booleanState, IsAllScaleLinkedState.class.getResource( "images/scaleLinked.png" ), IsAllScaleLinkedState.class.getResource( "images/scaleUnlinked.png" ) );
//  }
//
//  private final org.lgna.croquet.event.ValueListener<Boolean> valueListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
//    public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
//      LinkScaleButton.this.updateLabel();
//    }
//  };
//
//  @Override
//  protected void handleDisplayable() {
//    super.handleDisplayable();
//    org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().addAndInvokeNewSchoolValueListener( this.valueListener );
//  }
//
//  @Override
//  protected void handleUndisplayable() {
//    org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().removeNewSchoolValueListener( this.valueListener );
//    super.handleUndisplayable();
//  }
//
//  @Override
//  protected javax.swing.AbstractButton createAwtComponent()
//  {
//    return new javax.swing.JRadioButton()
//    {
//      @Override
//      public Dimension getPreferredSize()
//      {
//        return LinkScaleButton.this.size;
//      }
//
//      @Override
//      public Dimension getMinimumSize()
//      {
//        return LinkScaleButton.this.size;
//      }
//
//      @Override
//      public Dimension getMaximumSize()
//      {
//        return LinkScaleButton.this.size;
//      }
//
//      @Override
//      public boolean isRolloverEnabled()
//      {
//        return true;
//      }
//
//      @Override
//      public boolean isFocusable() {
//        return false;
//      }
//
//      @Override
//      public boolean isOpaque() {
//        return false;
//      }
//
//      @Override
//      protected void paintComponent( java.awt.Graphics g ) {
//        if( this.getModel().isRollover() )
//        {
//          int leftX = 0;
//          int rightX = (int)LinkScaleButton.this.size.getWidth() - 1;
//          int topY = 0;
//          int bottomY = (int)LinkScaleButton.this.size.getHeight() - 1;
//          g.setColor( Color.WHITE );
//          g.drawLine( leftX, topY, rightX, topY );
//          g.drawLine( leftX, topY, leftX, bottomY );
//          g.setColor( Color.DARK_GRAY );
//          g.drawLine( leftX, bottomY, rightX, bottomY );
//          g.drawLine( rightX, topY, rightX, bottomY );
//        }
//        if( this.getModel().isPressed() )
//        {
//          int leftX = 0;
//          int rightX = (int)LinkScaleButton.this.size.getWidth() - 1;
//          int topY = 0;
//          int bottomY = (int)LinkScaleButton.this.size.getHeight() - 1;
//          g.setColor( Color.DARK_GRAY );
//          g.drawLine( leftX, topY, rightX, topY );
//          g.drawLine( leftX, topY, leftX, bottomY );
//          g.setColor( Color.WHITE );
//          g.drawLine( leftX, bottomY, rightX, bottomY );
//          g.drawLine( rightX, topY, rightX, bottomY );
//        }
//        if( this.getModel().isSelected() )
//        {
//          g.drawImage( LinkScaleButton.this.linkedIcon.getImage(), 0, 0, null );
//        }
//        else
//        {
//          g.drawImage( LinkScaleButton.this.unlinkedIcon.getImage(), 0, 0, null );
//        }
//        //        super.paintComponent(g);
//      }
//    };
//  }
//
//  void updateLabel()
//  {
//    this.repaint();
//  }
//}
