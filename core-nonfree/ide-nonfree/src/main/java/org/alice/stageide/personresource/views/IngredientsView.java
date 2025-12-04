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

package org.alice.stageide.personresource.views;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon;
import org.alice.ide.icons.Icons;
import org.alice.stageide.custom.ColorCustomExpressionCreatorComposite;
import org.alice.stageide.personresource.IngredientsComposite;
import org.alice.stageide.personresource.SkinColorState;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.*;
import org.lgna.story.resources.sims2.LifeStage;

import javax.swing.Icon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.concurrent.Callable;

/**
 * @author Dennis Cosgrove
 */
public class IngredientsView extends MigPanel {
  private final Label isLifeStageLockedLabel = new Label();
  private final HorizontalWrapList<LifeStage> lifeStageList;

  private static final Icon LOCKED_ICON = new FlatSVGIcon(Icons.class.getResource("images/locked.svg"));

  public IngredientsView(final IngredientsComposite composite) {
    super(composite, "insets 0, fill", "[][align right][][grow]", "[][][][][shrink]");

    this.addComponent(this.isLifeStageLockedLabel);
    this.addComponent(composite.getLifeStageState().getSidekickLabel().createLabel(), "");

    this.lifeStageList = new HorizontalWrapList(composite.getLifeStageState(), 1);
    this.addComponent(this.lifeStageList, "push");
    this.addComponent(composite.getRandomize().createButton(), "push, align right, wrap");

    this.addComponent(composite.getGenderState().getSidekickLabel().createLabel(), "skip");
    this.addComponent(new HorizontalWrapList(composite.getGenderState(), 1), "wrap");

    final SkinColorState skinColorState = composite.getSkinColorState();
    this.addComponent(composite.getSkinColorState().getSidekickLabel().createLabel(), "skip");

    final Color[] melaninChipColors = skinColorState.getMelaninChooserTabComposite().getMelaninChipShades();
    String constraints = "gap 0, split " + melaninChipColors.length;
    for (Color melaninShade : melaninChipColors) {
      BooleanState itemSelectedState = skinColorState.getItemSelectedState(melaninShade);
      itemSelectedState.initializeIfNecessary();
      itemSelectedState.setTextForBothTrueAndFalse("");
      itemSelectedState.setIconForBothTrueAndFalse(new ColorIcon(melaninShade));
      ToggleButton button = itemSelectedState.createToggleButton();
      button.setMinimumPreferredWidth(ColorIcon.DEFAULT_SIZE);
      this.addComponent(button, constraints);
      constraints = "gap 0";
    }

    class OtherColorCallable implements Callable<Color> {
      private Color value = null;

      public Color getValue() {
        return this.value;
      }

      public void setValue(Color value) {
        this.value = value;
      }

      @Override
      public Color call() throws Exception {
        return this.value;
      }
    }

    final OtherColorCallable otherColorCallable = new OtherColorCallable();

    final BooleanState otherColorState = skinColorState.getItemSelectedState(otherColorCallable);
    final ToggleButton otherColorButton = otherColorState.createToggleButton();
    final int SIZE = ColorIcon.DEFAULT_SIZE;
    class OtherColorIcon implements Icon {
      @Override
      public int getIconWidth() {
        return SIZE;
      }

      @Override
      public int getIconHeight() {
        return SIZE;
      }

      @Override
      public void paintIcon(Component c, Graphics g, int x, int y) {
        Color color = otherColorCallable.getValue();
        if (color != null) {
          g.setColor(color);
          g.fillRect(x, y, SIZE, SIZE);
        }
      }
    }

    otherColorButton.getAwtComponent().setText("");
    otherColorButton.getAwtComponent().setIcon(new OtherColorIcon());
    otherColorButton.setMinimumPreferredWidth(ColorIcon.DEFAULT_SIZE);
    this.addComponent(otherColorButton, "gap 8, split 2");

    //this.addComponent( new MelaninSlider( composite.getSkinColorState() ) );
    final Button customColorDialogButton = composite.getSkinColorState().getChooserDialogCoreComposite().getLaunchOperation().createButton();
    String customColorLabel = ResourceBundleUtilities.getStringForKey("ColorCustomExpressionCreatorComposite", ColorCustomExpressionCreatorComposite.class);
    customColorDialogButton.setClobberText(customColorLabel);

    ValueListener<Color> colorListener = new ValueListener<Color>() {
      @Override
      public void valueChanged(ValueEvent<Color> e) {
        Color nextValue = e.getNextValue();
        boolean isColorMelaninShade = false;
        for (Color melaninShade : skinColorState.getMelaninChooserTabComposite().getMelaninChipShades()) {
          if (melaninShade.equals(nextValue)) {
            isColorMelaninShade = true;
            break;
          }
        }
        if (!isColorMelaninShade) {
          otherColorCallable.setValue(nextValue);
        }
        otherColorState.setEnabled(otherColorCallable.getValue() != null);
      }
    };
    skinColorState.addAndInvokeNewSchoolValueListener(colorListener);

    this.addComponent(customColorDialogButton, "gapx 0, wrap");

    final Color[] melaninSliderColors = skinColorState.getMelaninChooserTabComposite().getMelaninSliderShades();
    this.getAwtComponent().add(new JColorSlider(melaninSliderColors) {
      @Override
      protected void handleNextColor(Color nextColor) {
        //todo
        skinColorState.setValueTransactionlessly(nextColor);
      }
    }, "skip 2, grow, gaptop 0, wrap");

    FolderTabbedPane tabbedPane = composite.getBodyHeadHairTabState().createFolderTabbedPane();
    this.addComponent(tabbedPane, "span 4, grow");

    final Color c = UIManager.getColor("Alice.differentBackground");
    tabbedPane.setBackgroundColor(c);
    this.setBackgroundColor(c);
  }

  @Override
  public void handleCompositePreActivation() {
    IngredientsComposite composite = (IngredientsComposite) this.getComposite();
    this.isLifeStageLockedLabel.setIcon(composite.getLifeStageState().isEnabled() ? null : LOCKED_ICON);
    super.handleCompositePreActivation();
  }
}
