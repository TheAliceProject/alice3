/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package test.hair;

import org.alice.stageide.personresource.data.HairColorName;
import org.alice.stageide.personresource.data.HairColorNameHairCombo;
import org.alice.stageide.personresource.data.HairHatStyle;
import org.alice.stageide.personresource.data.HairUtilities;

/**
 * @author Dennis Cosgrove
 */
public class HairTestComposite extends org.lgna.croquet.SimpleComposite<org.lgna.croquet.views.Panel> {
  private final java.awt.event.ActionListener eyeDropperListener = new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
      java.awt.PointerInfo pointerInfo = java.awt.MouseInfo.getPointerInfo();
      java.awt.Point location = pointerInfo.getLocation();
      java.awt.Color color = edu.cmu.cs.dennisc.java.awt.RobotUtilities.getPixelColor(location.x, location.y);
      if (color != null) {
        int argb = color.getRGB();
        int rgb = 0xFFFFFF & argb;
        StringBuilder sb = new StringBuilder();
        //sb.append( "new java.awt.Color( " );
        sb.append("0x");
        sb.append(Integer.toHexString(rgb));
        //sb.append( " )" );
        edu.cmu.cs.dennisc.java.util.logging.Logger.outln(sb.toString());
        edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents(sb.toString());
        label.setBackgroundColor(color);
      } else {
        edu.cmu.cs.dennisc.java.util.logging.Logger.errln("color is null");
      }
    }
  };

  private final org.lgna.croquet.views.Label label = new org.lgna.croquet.views.Label();

  public HairTestComposite() {
    super(java.util.UUID.fromString("c51cd23c-62c0-46fe-be64-099c081ffa2a"));
  }

  @Override
  protected org.lgna.croquet.views.Panel createView() {
    //org.lgna.story.resources.sims2.SkinTone skinTone = org.lgna.story.resources.sims2.BaseSkinTone.getRandom();
    String skinTone = "GRAY";
    edu.cmu.cs.dennisc.java.util.InitializingIfAbsentListHashMap<HairColorName, String> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentListHashMap();
    for (org.lgna.story.resources.sims2.LifeStage lifeStage : org.lgna.story.resources.sims2.LifeStage.values()) {
      for (org.lgna.story.resources.sims2.Gender gender : org.lgna.story.resources.sims2.Gender.values()) {
        for (HairHatStyle hairHatStyle : HairUtilities.getHairHatStyles(lifeStage, gender)) {
          for (HairColorNameHairCombo hairColorNameHairCombo : hairHatStyle.getHairColorNameHairCombos()) {
            HairColorName hairColorName = hairColorNameHairCombo.getHairColorName();
            org.lgna.story.resources.sims2.Hair hair = hairColorNameHairCombo.getHair();
            if (hair != null) {
              java.util.List<String> list = map.getInitializingIfAbsentToLinkedList(hairColorName);
              StringBuilder sb = new StringBuilder();
              sb.append("hair_pictures/");
              sb.append(skinTone);
              sb.append("/");
              sb.append(hair.getClass().getSimpleName());
              sb.append(".");
              sb.append(hair.toString());
              sb.append(".png");
              list.add(sb.toString());
            }
            //            }
          }
        }
      }
    }

    org.lgna.croquet.views.BorderPanel outerPanel = new org.lgna.croquet.views.BorderPanel();
    org.lgna.croquet.views.MigPanel panel = new org.lgna.croquet.views.MigPanel();

    label.setOpaque(true);
    label.setBackgroundColor(java.awt.Color.RED);
    outerPanel.addPageStartComponent(new org.lgna.croquet.views.BorderPanel.Builder().lineStart(new org.lgna.croquet.views.Label("eyedropper (F2)")).center(label).build());

    class HairPathListCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer<String> {
      @Override
      protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        javax.swing.Icon icon = org.alice.stageide.personresource.views.renderers.IngredientListCellRenderer.getIconForPath(90, 120, value);
        rv.setIcon(icon);
        rv.setText(icon != null ? null : value);
        return rv;
      }
    }
    //    for( HairColorName hairColorName : map.keySet() ) {
    for (HairColorName hairColorName : HairColorName.values()) {
      java.util.List<String> paths = map.get(hairColorName);
      if (paths != null) {
        panel.addComponent(new org.lgna.croquet.views.Label(hairColorName.name(), hairColorName.getIcon()));
        org.lgna.croquet.ImmutableDataSingleSelectListState<String> state = this.createImmutableListState("unused", String.class, org.alice.ide.croquet.codecs.StringCodec.SINGLETON, -1, edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray(paths, String.class));
        org.lgna.croquet.views.List<String> listView = state.createList();
        listView.setCellRenderer(new HairPathListCellRenderer());
        listView.setLayoutOrientation(org.lgna.croquet.views.List.LayoutOrientation.HORIZONTAL_WRAP);
        listView.setVisibleRowCount(1);
        panel.addComponent(listView, "wrap");
      }
    }
    org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane(panel);
    scrollPane.setBothScrollBarIncrements(66, 66);

    outerPanel.addCenterComponent(scrollPane);
    javax.swing.KeyStroke keyStroke = javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0);
    outerPanel.registerKeyboardAction(this.eyeDropperListener, keyStroke, org.lgna.croquet.views.SwingComponentView.Condition.WHEN_IN_FOCUSED_WINDOW);

    return outerPanel;
  }

  public static void main(String[] args) {
    org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
    org.lgna.croquet.views.Frame frame = app.getDocumentFrame().getFrame();
    HairTestComposite hairTest = new HairTestComposite();
    frame.getContentPane().addCenterComponent(hairTest.getRootComponent());
    frame.setLocation(-1920, 400);
    frame.setSize(1000, 800);
    frame.maximize();
    frame.setDefaultCloseOperation(org.lgna.croquet.views.Frame.DefaultCloseOperation.EXIT);
    frame.setVisible(true);
  }
}
