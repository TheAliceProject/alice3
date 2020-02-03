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
package org.alice.ide.ast.type.merge.croquet.views;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextAttribute;
import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.javax.swing.plaf.HyperlinkUI;
import org.alice.ide.ast.type.merge.croquet.DifferentImplementation;
import org.alice.ide.ast.type.merge.croquet.DifferentSignature;
import org.alice.ide.ast.type.merge.croquet.Identical;
import org.alice.ide.ast.type.merge.croquet.ImportOnly;
import org.alice.ide.ast.type.merge.croquet.MembersToolPalette;
import org.alice.ide.ast.type.merge.croquet.ProjectOnly;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.CheckBox;
import org.lgna.croquet.views.Hyperlink;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.Separator;
import org.lgna.croquet.views.TextField;
import org.lgna.project.ast.Member;
import org.lgna.project.ast.UserMethod;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class MembersView<M extends Member> extends MigPanel {
  //  private static final int DIFFERENT_SIGNATURE_PRE_GAP = 0;
  //  private static final int DIFFERENT_SIGNATURE_POST_GAP = 0;
  //  private static final int DIFFERENT_IMPLEMENTATION_PRE_GAP = 0;
  //  private static final int DIFFERENT_IMPLEMENTATION_POST_GAP = 0;

  private static final int GAP_Y = 8;
  private static final int SPACE = 32;
  private static final int BRACKET_WIDTH = 8;

  private static final String COLUMN_0_CONSTRAINT = "[grow 0,center]";
  private static final String COLUMN_1_CONSTRAINT = COLUMN_0_CONSTRAINT;
  private static final String COLUMN_2_CONSTRAINT = "[grow 0]";

  private static AbstractLabel createHeader(PlainStringValue stringValue) {
    final TextAttribute<?>[] HEADER_TEXT_ATTRIBUTES = {TextWeight.BOLD, TextPosture.OBLIQUE};
    AbstractLabel header = stringValue.createLabel(HEADER_TEXT_ATTRIBUTES);
    return header;
  }

  private static Separator createSeparator() {
    return Separator.createInstanceSeparatingTopFromBottom();
  }

  private int row = 0;
  private final List<List<Component>> rows = Lists.newLinkedList();
  private final List<Integer> startIndicesOfRowPairs = Lists.newLinkedList();

  private static String getTitleText(MembersToolPalette<?, ?> composite) {
    return composite.getOuterComposite().getIsExpandedState().getTrueText();
  }

  private static String createDifferentSignatureToolText(String titleText, Member member) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>\"");
    sb.append(member.getName());
    sb.append("\" ");
    sb.append(titleText);
    if (member instanceof UserMethod) {
      sb.append(" have different signatures.");
    } else {
      sb.append(" have different value classes.");
    }
    sb.append("<p><strong>You must change at least one of their names.</strong></html>");
    return sb.toString();
  }

  private static String createDifferentImplementationToolText(String titleText, Member member) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>\"");
    sb.append(member.getName());
    sb.append("\" ");
    sb.append(titleText);
    if (member instanceof UserMethod) {
      sb.append(" have different implementations.");
    } else {
      sb.append(" have different initializers.");
    }
    sb.append("<p><strong>You must change at least one of their names.</strong></html>");
    return sb.toString();
  }

  private static String createIdenticalToolText(String titleText, Member member) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>\"");
    sb.append(member.getName());
    sb.append("\" ");
    sb.append(titleText);
    sb.append(" ");
    sb.append(" are identical.<p>No action is required.</html>");
    return sb.toString();
  }

  public MembersView(MembersToolPalette<?, M> composite, Color backgroundColor) {
    super(composite, "fill, insets 8 12 4 4, gapy " + GAP_Y, COLUMN_0_CONSTRAINT + 16 + COLUMN_1_CONSTRAINT + SPACE + COLUMN_2_CONSTRAINT + "24[grow]");

    //todo
    backgroundColor = ColorUtilities.scaleHSB(backgroundColor, 1.0, 1.0, 1.1);
    this.setBackgroundColor(backgroundColor);

    String titleText = getTitleText(composite);
    this.addComponent(createHeader(composite.getFromImportHeader()));
    this.addComponent(createHeader(composite.getAlreadyInProjectHeader()));
    this.addComponent(createHeader(composite.getResultHeader()), "span 2, wrap");
    this.addComponent(createSeparator(), "grow, shrink");
    this.addComponent(createSeparator(), "grow, shrink");
    this.addComponent(createSeparator(), "span 2, grow, shrink, wrap");

    for (ImportOnly<M> importOnly : composite.getImportOnlys()) {
      this.addComponent(importOnly.getImportHub().getIsDesiredState().createCheckBox());

      this.addComponent(MemberViewUtilities.createPopupView(importOnly.getImportHub().getPopup()), "split 2, skip 1");
      AbstractLabel label = MemberViewUtilities.createAddMemberLabel(importOnly.getImportHub().getMember());
      this.addComponent(label, "wrap");

      //todo: removeValueListener somewhere
      importOnly.getImportHub().getIsDesiredState().addNewSchoolValueListener(new IsAddDesiredListener(label));
    }

    for (final DifferentSignature<M> differentSignature : composite.getDifferentSignatures()) {
      this.markStartIndexOfRowPair();

      //this.addSpacerNotTrackedAsRow( DIFFERENT_SIGNATURE_PRE_GAP );

      //BracketView rightBracket = new BracketView( false );
      //rightBracket.setToolTipText( createDifferentSignatureToolText( titleText, differentSignature.getImportMember() ) );

      this.addComponent(differentSignature.getImportHub().getIsDesiredState().createCheckBox());

      this.addComponent(MemberViewUtilities.createPopupView(differentSignature.getImportHub().getPopup()), "skip 1, split 2");
      TextField textField = MemberViewUtilities.createTextField(differentSignature.getImportHub().getNameState(), differentSignature.getForegroundCustomizer());
      this.addComponent(textField);
      //this.addComponent( rightBracket, "grow, spany 2, wrap" );

      Hyperlink helpHyperlink = differentSignature.getHelpComposite().getLaunchOperation().createHyperlink();
      ((HyperlinkUI) helpHyperlink.getAwtComponent().getUI()).setUnderlinedOnlyWhenRolledOver(false);
      this.addComponent(helpHyperlink, "spany 2, gapright 8, wrap");

      CheckBox keepCheckBox = differentSignature.getProjectHub().getIsDesiredState().createCheckBox();
      this.addComponent(keepCheckBox, "skip 1");

      this.addComponent(MemberViewUtilities.createPopupView(differentSignature.getProjectHub().getPopup()), "split 2");
      this.addComponent(differentSignature.getProjectCardOwner().getRootComponent(), "grow, shrink, wrap");
      //this.addComponent( MemberViewUtilities.createTextField( differentSignature.getProjectNameState(), differentSignature.getForegroundCustomizer() ), "wrap" );

      //todo: removeValueListener somewhere
      differentSignature.getImportHub().getIsDesiredState().addNewSchoolValueListener(new IsAddDesiredListener(textField));

      //this.addSpacerNotTrackedAsRow( DIFFERENT_SIGNATURE_POST_GAP );
    }

    for (final DifferentImplementation<M> differentImplementation : composite.getDifferentImplementations()) {
      this.markStartIndexOfRowPair();

      //this.addSpacerNotTrackedAsRow( DIFFERENT_IMPLEMENTATION_PRE_GAP );

      //BracketView rightBracket = new BracketView( false );
      //rightBracket.setToolTipText( createDifferentImplementationToolText( titleText, differentImplementation.getImportMember() ) );

      CheckBox addCheckBox = differentImplementation.getImportHub().getIsDesiredState().createCheckBox();
      CheckBox keepCheckBox = differentImplementation.getProjectHub().getIsDesiredState().createCheckBox();
      this.addComponent(addCheckBox);
      this.addComponent(MemberViewUtilities.createPopupView(differentImplementation.getImportHub().getPopup()), "skip 1, split 2");
      this.addComponent(differentImplementation.getImportCardOwner().getRootComponent());
      //this.addComponent( rightBracket, "grow, spany 2, wrap" );

      Hyperlink helpHyperlink = differentImplementation.getHelpComposite().getLaunchOperation().createHyperlink();
      ((HyperlinkUI) helpHyperlink.getAwtComponent().getUI()).setUnderlinedOnlyWhenRolledOver(false);
      this.addComponent(helpHyperlink, "spany 2, gapright 8, wrap");

      this.addComponent(keepCheckBox, "skip 1");
      this.addComponent(MemberViewUtilities.createPopupView(differentImplementation.getProjectHub().getPopup()), "split 2");
      this.addComponent(differentImplementation.getProjectCardOwner().getRootComponent(), "wrap");

      //this.addSpacerNotTrackedAsRow( DIFFERENT_IMPLEMENTATION_POST_GAP );
    }

    for (Identical<M> identical : composite.getIdenticals()) {
      String toolTipText = createIdenticalToolText(titleText, identical.getImportHub().getMember());
      CheckBox addCheckBox = identical.getImportHub().getIsDesiredState().createCheckBox();
      addCheckBox.setToolTipText(toolTipText);
      CheckBox keepCheckBox = identical.getProjectHub().getIsDesiredState().createCheckBox();
      keepCheckBox.setToolTipText(toolTipText);
      this.addComponent(addCheckBox);
      this.addComponent(keepCheckBox);
      this.addComponent(MemberViewUtilities.createPopupView(identical.getProjectHub().getPopup()), "split 2");
      this.addComponent(MemberViewUtilities.createKeepIdenticalMemberLabel(identical.getProjectHub().getMember()), "wrap");
    }

    for (ProjectOnly<M> projectOnly : composite.getProjectOnlys()) {
      CheckBox keepCheckBox = projectOnly.getProjectHub().getIsDesiredState().createCheckBox();
      this.addComponent(keepCheckBox, "skip 1");
      this.addComponent(MemberViewUtilities.createPopupView(projectOnly.getProjectHub().getPopup()), "split 2");
      this.addComponent(MemberViewUtilities.createKeepUniqueMemberLabel(projectOnly.getProjectHub().getMember()), "wrap");
    }
  }

  //  private void addSpacerNotTrackedAsRow( int height ) {
  //    if( height > 0 ) {
  //      this.getAwtComponent().add( new javax.swing.JLabel(), "wrap, height " + height + "px" );
  //    }
  //  }

  private void markStartIndexOfRowPair() {
    this.startIndicesOfRowPairs.add(this.row);
  }

  private void addToRow(AwtComponentView<?> component) {
    while (this.rows.size() <= this.row) {
      this.rows.add(new LinkedList<Component>());
    }
    this.rows.get(this.row).add(component.getAwtComponent());
  }

  @Override
  public void addComponent(AwtComponentView<?> component) {
    super.addComponent(component);
    this.addToRow(component);
  }

  @Override
  public void addComponent(AwtComponentView<?> component, String constraint) {
    super.addComponent(component, constraint);
    if (constraint.contains("spany")) {
      //pass
    } else {
      this.addToRow(component);
    }
    if (constraint.contains("wrap")) {
      this.row++;
    }
  }

  private static Rectangle getRowBounds(List<Component> row) {
    if (row.size() > 0) {
      Rectangle rv = null;
      for (Component awtComponent : row) {
        Rectangle bounds = awtComponent.getBounds();
        if (rv != null) {
          rv = rv.union(bounds);
        } else {
          rv = bounds;
        }
      }
      return rv;
    } else {
      return new Rectangle(0, 0, 0, 0);
    }
  }

  @Override
  protected JPanel createJPanel() {
    class JMembersPanel extends DefaultJPanel {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Color brighterColor = ColorUtilities.scaleHSB(this.getBackground(), 1.0, 1.0, 1.1);
        final int SKIP_ROW_COUNT = 2;
        int rowIndex = 0;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        for (List<Component> row : rows) {
          if (rowIndex >= SKIP_ROW_COUNT) {
            for (Component awtComponent : row) {
              Rectangle bounds = awtComponent.getBounds();
              minX = Math.min(minX, bounds.x);
              maxX = Math.max(maxX, bounds.x + bounds.width);
            }
          }
          rowIndex++;
        }

        final boolean IS_STRETCH_ACROSS_DESIRED = true;
        int width;
        int x;
        if (IS_STRETCH_ACROSS_DESIRED) {
          x = 16;
          width = this.getWidth() - x - 4 - (BRACKET_WIDTH / 2);
        } else {
          x = minX;
          width = maxX - minX;
        }
        if (this.getComponentCount() > 2) {
          int xSeparator = getComponent(2).getX() - (SPACE / 2) - 2;
          rowIndex = 0;
          Rectangle prevBounds = null;
          for (List<Component> row : rows) {
            if (rowIndex >= SKIP_ROW_COUNT) {
              boolean isEvenRow = ((rowIndex - SKIP_ROW_COUNT) % 2) == 0;
              boolean isStartIndexOfRowPair = startIndicesOfRowPairs.contains(rowIndex);
              Rectangle bounds;
              if (isEvenRow || isStartIndexOfRowPair || (prevBounds != null)) {
                bounds = getRowBounds(row);
              } else {
                bounds = null;
              }

              if (isEvenRow) {
                g.setColor(brighterColor);
                g.fillRect(x, bounds.y - (GAP_Y / 2), width, bounds.height + GAP_Y);
              }

              if (prevBounds != null) {
                g.setColor(Color.BLACK);
                double y0 = prevBounds.getCenterY();
                double y1 = bounds.getCenterY();

                int x0 = xSeparator + 12;
                int x1 = x0 + BRACKET_WIDTH;

                GeneralPath path = new GeneralPath();
                path.moveTo(x1, y0);
                path.lineTo(x0, y0);
                path.lineTo(x0, y1);
                path.lineTo(x1, y1);
                g2.draw(path);

                Rectangle pairBounds = prevBounds.union(bounds);
                g.drawRect(x, pairBounds.y - (GAP_Y / 2) - 1, width, (pairBounds.height + GAP_Y) - 2);
              }
              prevBounds = isStartIndexOfRowPair ? bounds : null;
            }
            rowIndex++;
          }

          Color separatorColor = ColorUtilities.scaleHSB(this.getBackground(), 1.0, 0.8, 0.6);
          g.setColor(separatorColor);
          g.fillRect(xSeparator, 0, 3, this.getHeight());
        }

      }
    }
    return new JMembersPanel();
  }
}
