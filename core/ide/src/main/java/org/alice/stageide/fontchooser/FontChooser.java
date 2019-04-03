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
package org.alice.stageide.fontchooser;

import org.lgna.story.Font;
import org.lgna.story.fontattributes.FamilyAttribute;
import org.lgna.story.fontattributes.FamilyConstant;
import org.lgna.story.fontattributes.PostureAttribute;
import org.lgna.story.fontattributes.PostureConstant;
import org.lgna.story.fontattributes.SizeAttribute;
import org.lgna.story.fontattributes.SizeValue;
import org.lgna.story.fontattributes.WeightAttribute;
import org.lgna.story.fontattributes.WeightConstant;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * @author Dennis Cosgrove
 */
public class FontChooser extends JPanel {
  private class Pane extends JPanel {
    private JLabel m_title = new JLabel();
    //private javax.swing.JTextField m_field = new javax.swing.JTextField();
    protected JList m_list = new JList();

    public Pane(String titleText) {
      m_title.setText(titleText);
      setLayout(new GridBagLayout());
      addComponents(new GridBagConstraints());

      m_list.addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          if (e.getValueIsAdjusting()) {
            //pass
          } else {
            FontChooser.this.updateSample();
          }
        }
      });
    }

    protected void addComponents(GridBagConstraints gbc) {
      gbc.fill = GridBagConstraints.BOTH;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.weightx = 1.0;
      gbc.weighty = 0.0;
      add(m_title, gbc);
      //add( m_field, gbc );
      gbc.weighty = 1.0;
      add(new JScrollPane(m_list), gbc);
    }
  }

  private class FamilyPane extends Pane {
    public FamilyPane() {
      super("Family:");
      m_list.setListData(new String[] {"Serif", "SansSerif"});
    }

    public FamilyAttribute getFamilyAttribute() {
      Object value = m_list.getSelectedValue();
      FamilyAttribute rv;
      if (value.equals("Serif")) {
        rv = FamilyConstant.SERIF;
      } else {
        rv = FamilyConstant.SANS_SERIF;
      }
      return rv;
    }

    public void setFamilyAttribute(FamilyAttribute familyAttribute) {
      if (familyAttribute == FamilyConstant.SERIF) {
        m_list.setSelectedValue("Serif", true);
      } else {
        m_list.setSelectedValue("SansSerif", true);
      }
    }
  }

  private class StylePane extends Pane {
    public StylePane() {
      super("Style:");
      m_list.setListData(new String[] {"Regular", "Bold", "Italic", "Bold Italic"});
    }

    public WeightAttribute getWeightAttribute() {
      Object value = m_list.getSelectedValue();
      WeightAttribute rv;
      if ((value != null) && (value.equals("Bold") || value.equals("Bold Italic"))) {
        rv = WeightConstant.BOLD;
      } else {
        rv = WeightConstant.REGULAR;
      }
      return rv;
    }

    public PostureAttribute getPostureAttribute() {
      Object value = m_list.getSelectedValue();
      PostureAttribute rv;
      if ((value != null) && (value.equals("Italic") || value.equals("Bold Italic"))) {
        rv = PostureConstant.OBLIQUE;
      } else {
        rv = PostureConstant.REGULAR;
      }
      return rv;
    }

    public void setStyleAttributes(WeightAttribute weight, PostureAttribute posture) {
      boolean isBold = (weight == WeightConstant.BOLD);
      boolean isItalic = (posture == PostureConstant.OBLIQUE);
      Object selectedValue;
      if (isBold) {
        if (isItalic) {
          selectedValue = "Bold Italic";
        } else {
          selectedValue = "Bold";
        }
      } else {
        if (isItalic) {
          selectedValue = "Italic";
        } else {
          selectedValue = "Regular";
        }
      }
      m_list.setSelectedValue(selectedValue, true);
    }
  }

  private class SizePane extends Pane {
    public SizePane() {
      super("Size:");
      m_list.setListData(new String[] {"8", "9", "10", "12", "14", "18", "24", "32", "48", "64", "96"});
    }

    public SizeAttribute getSizeAttribute() {
      Object value = m_list.getSelectedValue();
      if (value instanceof String) {
        return new SizeValue(Float.valueOf((String) value));
      } else {
        return null;
      }
    }

    public void setSizeAttribute(SizeAttribute sizeAttribute) {
      int size = sizeAttribute.getValue().intValue();
      m_list.setSelectedValue(Integer.toString(size), true);
    }
    //    @Override
    //    protected void addComponents( java.awt.GridBagConstraints gbc ) {
    //      super.addComponents( gbc );
    //    }
  }

  private FamilyPane m_familyPane = new FamilyPane();
  private StylePane m_stylePane = new StylePane();
  private SizePane m_sizePane = new SizePane();
  private JLabel m_sample = new JLabel();

  public FontChooser() {
    this(new Font());
  }

  public FontChooser(Font font) {
    setSampleText(null);
    m_sample.setHorizontalAlignment(SwingConstants.CENTER);
    m_sample.setVerticalAlignment(SwingConstants.CENTER);

    final int INSET = 8;
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.BOTH;

    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    add(m_familyPane, gbc);
    gbc.insets.left = INSET;
    add(m_stylePane, gbc);
    gbc.insets.left = 0;
    //add( m_sizePane, gbc );

    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.gridwidth = 2;
    gbc.weighty = 0.0;
    add(m_sample, gbc);

    gbc.gridy = 2;
    gbc.weighty = 1.0;
    add(new JPanel(), gbc);

    setValue(font);
  }

  public Font getValue() {
    FamilyAttribute family = m_familyPane.getFamilyAttribute();
    WeightAttribute weight = m_stylePane.getWeightAttribute();
    PostureAttribute posture = m_stylePane.getPostureAttribute();
    SizeAttribute size = m_sizePane.getSizeAttribute();
    if (size != null) {
      return new Font(family, weight, posture, size);
    } else {
      return null;
    }
  }

  public void setValue(Font font) {
    m_familyPane.setFamilyAttribute(font.getFamily());
    m_stylePane.setStyleAttributes(font.getWeight(), font.getPosture());
    m_sizePane.setSizeAttribute(font.getSize());
  }

  public void setSampleText(String sampleText) {
    if ((sampleText != null) && (sampleText.length() > 0)) {
      //pass
    } else {
      sampleText = "AaBbYyZz";
    }
    m_sample.setText(sampleText);
  }

  private void updateSample() {
    Font font = getValue();
    if (font != null) {
      m_sample.setFont(font.getAsAWTFont());
    }
  }
}
