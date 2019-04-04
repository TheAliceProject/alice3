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
package org.alice.imageeditor.croquet.views;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import org.alice.imageeditor.croquet.ImageEditorFrame;
import org.alice.imageeditor.croquet.SaveOverComposite;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.Dialog;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.Separator;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Dennis Cosgrove
 */
public class SaveOverPane extends MigPanel {
  private final ImageView toBeReplacedImageView = new ImageView();
  private final ImageView nextImageView = new ImageView();
  private final Label toBeReplacedHeaderLabel = new Label();
  private final Label toBeReplacedDetailsLabel = new Label();
  private final Label nextDetailsLabel = new Label();

  public SaveOverPane(SaveOverComposite composite) {
    super(composite, "fill", "[50%][grow 0][50%]", "[grow 0, shrink 0][grow, shrink][grow 0,shrink 0]");
    this.addComponent(this.toBeReplacedHeaderLabel);
    this.addComponent(Separator.createInstanceSeparatingLeftFromRight(), "spany 3, growy");
    this.addComponent(composite.getNextHeader().createLabel(), "wrap");
    this.addComponent(this.toBeReplacedImageView, "grow, shrink");
    this.addComponent(this.nextImageView, "skip 1, grow, shrink, wrap");
    this.addComponent(this.toBeReplacedDetailsLabel);
    this.addComponent(this.nextDetailsLabel, "skip 1");
    this.setMinimumPreferredHeight(300);
    this.setMinimumPreferredWidth(800);
  }

  @Override
  public SaveOverComposite getComposite() {
    return (SaveOverComposite) super.getComposite();
  }

  private static String getResolutionText(Image image) {
    StringBuilder sb = new StringBuilder();
    //sb.append( "(" );
    sb.append("resolution: ");
    sb.append(image.getWidth(null));
    sb.append(" x ");
    sb.append(image.getHeight(null));
    //sb.append( ")" );
    return sb.toString();
  }

  @Override
  public void handleCompositePreActivation() {
    ImageEditorFrame frame = this.getComposite().getOwner().getOwner();
    File file = frame.getFile();
    try {
      Image toBeReplacedImage = ImageUtilities.read(file);
      Image nextImage = frame.getView().render();
      this.toBeReplacedImageView.setImage(toBeReplacedImage);
      this.nextImageView.setImage(nextImage);

      DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
      DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
      Date date = new Date(file.lastModified());
      this.toBeReplacedHeaderLabel.setText(this.getComposite().getPrevHeader().getText() + " (last modified: " + dateFormat.format(date) + " " + timeFormat.format(date) + ")");
      this.toBeReplacedDetailsLabel.setText(getResolutionText(toBeReplacedImage));
      this.nextDetailsLabel.setText(getResolutionText(nextImage));
      super.handleCompositePreActivation();
      AbstractWindow<?> window = this.getRoot();
      if (window instanceof Dialog) {
        Dialog dialog = (Dialog) window;
        dialog.setTitle("Save Over " + file);
        dialog.pack();
      }
    } catch (IOException ioe) {
      throw new RuntimeException(file.getAbsolutePath(), ioe);
    }
  }
}
