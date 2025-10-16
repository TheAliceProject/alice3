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

package org.alice.ide.declarationseditor.type.components;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.awt.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.pattern.HowMuch;
import org.alice.ide.ApiConfigurationManager;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.views.TypeHeader;
import org.alice.ide.croquet.models.IdeDragModel;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingConstructors;
import org.alice.ide.declarationseditor.TypeComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.lgna.croquet.DropReceptor;
import org.lgna.croquet.views.*;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.ast.NamedUserType;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

/**
 * These are tabs at the top of alice that define a type.  Most commonly, we see this as the Scene Tab, but if you open a
 * tab for anything seen in the type hierarchy, this is what you'll see.
 *
 * @author Dennis Cosgrove
 */
public class TypeDeclarationView extends DeclarationView {
  public TypeDeclarationView(TypeComposite composite) {
    super(composite);

    NamedUserType type = composite.getDeclaration();
    Color typeColor = UIManager.getColor("Alice.Type.color");
    this.setBackgroundColor(typeColor);
    typePanel.setBackgroundColor(typeColor);

    ToolPaletteView constructorsToolPalette = composite.getConstructorsToolPaletteCoreComposite().getOuterComposite().getView();
    constructorsToolPalette.setBackgroundColor(UIManager.getColor("Alice.Constructor.color"));

    ToolPaletteView proceduresToolPalette = composite.getProceduresToolPaletteCoreComposite().getOuterComposite().getView();
    proceduresToolPalette.setBackgroundColor(UIManager.getColor("Alice.Procedure.color"));

    ToolPaletteView functionsToolPalette = composite.getFunctionsToolPaletteCoreComposite().getOuterComposite().getView();
    functionsToolPalette.setBackgroundColor(UIManager.getColor("Alice.Function.color"));

    ToolPaletteView fieldsToolPalette = composite.getFieldsToolPaletteCoreComposite().getOuterComposite().getView();
    fieldsToolPalette.setBackgroundColor(UIManager.getColor("Alice.Field.color"));


    PageAxisPanel membersPanel = new PageAxisPanel();
    membersPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

    // Add the constructor box separately, in case we're excluding it
    if (IsIncludingConstructors.getInstance().getValue()) {
      formatAndAddToolPalette(constructorsToolPalette, membersPanel);
    }

    // then add the rest
    for (ToolPaletteView toolPalette : new ToolPaletteView[] {proceduresToolPalette, functionsToolPalette, fieldsToolPalette}) {
      formatAndAddToolPalette(toolPalette, membersPanel);
    }

    TypeHeader typeHeader = new TypeHeader(type);

    this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    ApiConfigurationManager apiConfigurationManager = IDE.getActiveInstance().getApiConfigurationManager();
    if (apiConfigurationManager.isExportTypeDesiredFor(type)) {
      LineAxisPanel header = new LineAxisPanel(typeHeader, BoxUtilities.createHorizontalSliver(8), composite.getImportOperation().createButton(), composite.getExportOperation().createButton());
      this.typePanel.addPageStartComponent(header);
    } else {
      this.typePanel.addPageStartComponent(typeHeader);
    }
    this.typePanel.addCenterComponent(new BorderPanel.Builder().pageStart(membersPanel).build());

    for (JComponent component : ComponentUtilities.findAllMatches(typeHeader.getAwtComponent(), HowMuch.DESCENDANTS_ONLY, JComponent.class)) {
      FontUtilities.setFontToScaledFont(component, 1.2f);
    }
  }

  private static void formatAndAddToolPalette(ToolPaletteView toolPalette, PageAxisPanel membersPanel) {
    toolPalette.getTitle().changeFont(TextPosture.OBLIQUE);
    toolPalette.getTitle().scaleFont(1.4f);
     toolPalette.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // make what looks like a 1px line border, but round the corners nicely
    JPanel toolPaletteContainer = new JPanel(new GridLayout(1, 1));
    toolPaletteContainer.setBackground(toolPalette.getCenterView().getBackgroundColor());
    toolPaletteContainer.setBorder(new Border() {
      final int cornerRadius = 16;

      @Override
      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // we paint the area outside the rounded corner. because our unruly child paints outside the box
        g2.setColor(UIManager.getColor("Alice.Type.color"));
        g2.fillRect(0, 0, width, height);

        Color backgroundColor = c.getBackground();
        // paint the empty margin
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

        // paint the very outside
        Color outlineColor = ColorUtilities.scaleHSB(backgroundColor, 1, 4.8, .74);
        g2.setColor(outlineColor);
        g2.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
      }

      @Override
      public Insets getBorderInsets(Component c) {
        return new Insets(cornerRadius / 2, cornerRadius / 2, cornerRadius / 2, cornerRadius / 2);
      }

      @Override
      public boolean isBorderOpaque() {
        return true;
      }
    });

    toolPaletteContainer.add(toolPalette.getAwtComponent());
    membersPanel.addComponent(AwtComponentView.lookup(toolPaletteContainer));
    membersPanel.addComponent(BoxUtilities.createVerticalSliver(16));
  }

  @Override
  public void addPotentialDropReceptors(List<DropReceptor> out, IdeDragModel dragModel) {
  }

  @Override
  protected void setJavaCodeOnTheSide(boolean value, boolean isFirstTime) {
    super.setJavaCodeOnTheSide(value, isFirstTime);
    if (value) {
      if (!isFirstTime) {
        this.outerMainPanel.removeComponent(this.scrollPane);
      }
      this.scrollPane.setViewportView(null);
      this.outerMainPanel.addCenterComponent(this.typePanel);
    } else {
      if (!isFirstTime) {
        this.outerMainPanel.removeComponent(this.typePanel);
      }
      this.scrollPane.setViewportView(this.typePanel);
      this.outerMainPanel.addCenterComponent(this.scrollPane);
    }
  }

  @Override
  protected AwtComponentView<?> getMainComponent() {
    return this.outerMainPanel;
  }

  private final BorderPanel outerMainPanel = new BorderPanel();
  private final BorderPanel typePanel = new BorderPanel();
  private final ScrollPane scrollPane = new ScrollPane();
}
