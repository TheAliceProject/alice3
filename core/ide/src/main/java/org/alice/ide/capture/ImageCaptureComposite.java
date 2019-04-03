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
package org.alice.ide.capture;

import edu.cmu.cs.dennisc.capture.ImageCaptureUtilities;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import org.alice.ide.capture.views.ImageCaptureRectangleStencilView;
import org.alice.ide.capture.views.ImageCaptureView;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.BoundedIntegerState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FrameCompositeWithInternalIsShowingState;
import org.lgna.croquet.Group;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.AbstractWindow;

import javax.swing.JLayeredPane;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ImageCaptureComposite extends FrameCompositeWithInternalIsShowingState<ImageCaptureView> {
  //todo
  private static final Group IMAGE_CAPTURE_GROUP = Group.getInstance(UUID.fromString("220c4c60-aea1-4c18-95f9-7a0ef0a1f30b"), "IMAGE_CAPTURE_GROUP");

  private static class SingletonHolder {
    private static ImageCaptureComposite instance = new ImageCaptureComposite();
  }

  public static ImageCaptureComposite getInstance() {
    return SingletonHolder.instance;
  }

  private static final int LAYER_ID = JLayeredPane.POPUP_LAYER + 1;

  private ImageCaptureRectangleStencilView getImageCaptureRectangleStencilView(AbstractWindow<?> window) {
    return mapWindowToStencilView.getInitializingIfAbsent(window, new InitializingIfAbsentMap.Initializer<AbstractWindow<?>, ImageCaptureRectangleStencilView>() {
      @Override
      public ImageCaptureRectangleStencilView initialize(AbstractWindow<?> key) {
        return new ImageCaptureRectangleStencilView(key, LAYER_ID, ImageCaptureComposite.this);
      }
    });
  }

  private final Operation captureEntireWindowOperation = this.createActionOperation("captureEntireWindow", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      Application app = Application.getActiveInstance();
      AbstractWindow<?> window = app.getDocumentFrame().peekWindow();
      Image image = ImageCaptureUtilities.captureComplete(window.getAwtComponent(), getDpi());
      image = convertToRgbaIfNecessary(image);

      ImageCaptureRectangleStencilView stencilView = getImageCaptureRectangleStencilView(window);
      stencilView.getImageComposite().setImageClearShapesAndShowFrame(image);
      //edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
      return null;
    }
  });

  private final Operation captureEntireContentPaneOperation = this.createActionOperation("captureEntireContentPane", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      Application app = Application.getActiveInstance();
      AbstractWindow<?> window = app.getDocumentFrame().peekWindow();
      Image image = ImageCaptureUtilities.captureComplete(window.getRootPane().getAwtComponent(), getDpi());
      image = convertToRgbaIfNecessary(image);
      ImageCaptureRectangleStencilView stencilView = getImageCaptureRectangleStencilView(window);
      stencilView.getImageComposite().setImageClearShapesAndShowFrame(image);
      //edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
      return null;
    }
  });

  private final Operation captureRectangleOperation = this.createActionOperation("captureRectangle", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      Application app = Application.getActiveInstance();
      AbstractWindow<?> window = app.getDocumentFrame().peekWindow();
      ImageCaptureRectangleStencilView stencilView = getImageCaptureRectangleStencilView(window);
      stencilView.setStencilShowing(stencilView.isStencilShowing() == false);
      return null;
    }
  });

  private final PlainStringValue operationsHeader = this.createStringValue("operationsHeader");
  private final PlainStringValue propertiesHeader = this.createStringValue("propertiesHeader");
  private final BoundedIntegerState dpiState = this.createBoundedIntegerState("dpiState", new BoundedIntegerDetails().minimum(0).maximum(3000).initialValue(300));
  private final BooleanState isAlphaChannelState = this.createPreferenceBooleanState("isAlphaChannelState", false);

  private final InitializingIfAbsentMap<AbstractWindow<?>, ImageCaptureRectangleStencilView> mapWindowToStencilView = Maps.newInitializingIfAbsentHashMap();

  private ImageCaptureComposite() {
    super(UUID.fromString("84f73ef2-a5d1-4784-a902-45343434b0f0"), IMAGE_CAPTURE_GROUP);
  }

  public Image convertToRgbaIfNecessary(Image image) {
    if (this.isAlphaChannelState.getValue()) {
      int imageType = BufferedImage.TYPE_INT_ARGB_PRE; //to pre or not to pre
      return ImageUtilities.createBufferedImage(image, imageType);
    } else {
      return image;
    }
  }

  public PlainStringValue getOperationsHeader() {
    return this.operationsHeader;
  }

  public PlainStringValue getPropertiesHeader() {
    return this.propertiesHeader;
  }

  public Operation getCaptureEntireWindowOperation() {
    return this.captureEntireWindowOperation;
  }

  public Operation getCaptureEntireContentPaneOperation() {
    return this.captureEntireContentPaneOperation;
  }

  public Operation getCaptureRectangleOperation() {
    return this.captureRectangleOperation;
  }

  public BoundedIntegerState getDpiState() {
    return this.dpiState;
  }

  public BooleanState getIsAlphaChannelState() {
    return this.isAlphaChannelState;
  }

  public int getDpi() {
    final boolean IS_SPINNER_UPDATING_CORRECTLY = false;
    if (IS_SPINNER_UPDATING_CORRECTLY) {
      return this.dpiState.getValue();
    } else {
      return (Integer) this.dpiState.getSwingModel().getSpinnerModel().getValue();
    }
  }

  @Override
  protected ImageCaptureView createView() {
    return new ImageCaptureView(this);
  }

  public static void main(String[] args) throws Exception {
    UIManagerUtilities.setLookAndFeel("Nimbus");
    SimpleApplication app = new SimpleApplication();
    ImageCaptureComposite.getInstance().getIsFrameShowingState().getImp().getSwingModel().getButtonModel().setSelected(true);
  }

}
