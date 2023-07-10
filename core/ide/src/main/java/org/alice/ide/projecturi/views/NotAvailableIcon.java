package org.alice.ide.projecturi.views;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dennis Cosgrove
 */
public class NotAvailableIcon implements Icon {
  private final String text;

  public NotAvailableIcon(String text) {
    this.text = text;
  }

  @Override
  public int getIconWidth() {
    return 160;
  }

  @Override
  public int getIconHeight() {
    return 120;
  }

  protected String getText() {
    return text;
  }

  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    int width = this.getIconWidth();
    int height = this.getIconHeight();
    g.setColor(Color.DARK_GRAY);
    g.fillRect(x, y, width, height);
    g.setColor(Color.LIGHT_GRAY);
    GraphicsUtilities.drawCenteredText(g, this.getText(), x, y, width, height);
  }
}
