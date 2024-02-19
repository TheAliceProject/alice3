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
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Function;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon;
import org.alice.ide.Theme;
import org.alice.stageide.StageIDE;
import org.alice.stageide.icons.IconFactoryManager;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.alice.stageide.sceneeditor.CameraOption;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.icon.ImageIconFactory;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.UserField;
import org.lgna.story.CameraMarker;
import org.lgna.story.Color;
import org.lgna.story.SMarker;
import org.lgna.story.SThingMarker;

import edu.cmu.cs.dennisc.pattern.Tuple2;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * @author dculyba
 *
 */
public class MarkerUtilities {
  private static final String[] COLOR_NAME_KEYS;
  private static final Color[] COLORS;

  private static final HashMap<CameraOption, Tuple2<ImageIconFactory, ImageIconFactory>> cameraToIconMap = Maps.newHashMap();

  private static final HashMap<Color, Icon> colorToObjectIcon = Maps.newHashMap();
  private static final HashMap<Color, Icon> colorToCameraIcon = Maps.newHashMap();
  private static final HashMap<Color, Icon> colorToVrUserIcon = Maps.newHashMap();

  static {
    String[] colorNameKeys = {
        "red",
        "green",
        //        "blue",
        "magenta",
        "yellow",
        //        "cyan",
        "orange",
        "pink",
        "purple",
    };
    COLOR_NAME_KEYS = colorNameKeys;

    Color[] colors = {
        Color.RED,
        Color.GREEN,
        //        org.lgna.story.Color.BLUE,
        Color.MAGENTA,
        Color.YELLOW,
        //        org.lgna.story.Color.CYAN,
        Color.ORANGE,
        Color.PINK,
        Color.PURPLE
    };
    COLORS = colors;
  }

  private static String findLocalizedText(String subKey) {
    String bundleName = MarkerUtilities.class.getPackage().getName() + ".croquet";
    try {
      ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle(bundleName, JComponent.getDefaultLocale());
      String key = MarkerUtilities.class.getSimpleName();

      if (subKey != null) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(".");
        sb.append(subKey);
        key = sb.toString();
      }
      String rv = resourceBundle.getString(key);
      return rv;
    } catch (MissingResourceException mre) {
      return null;
    }
  }

  private static int getColorIndexForColor(Color color) {
    for (int i = 0; i < getColorCount(); i++) {
      if (getColorForIndex(i).equals(color)) {
        return i;
      }
    }
    return -1;
  }

  private static int getColorCount() {
    return COLORS.length;
  }

  private static Color getColorForIndex(int i) {
    return COLORS[i];
  }

  private static String getColorFileName(Color color) {
    int index = getColorIndexForColor(color);
    if (index != -1) {
      String colorName = COLOR_NAME_KEYS[index];
      String properName = colorName.substring(0, 1).toUpperCase() + colorName.substring(1);
      return properName;
    }
    return "White";
  }

  private static String getIconSuffixForMarkerColor(Color color) {
    String colorName = getColorFileName(color);
    return "_" + colorName + ".png";
  }

  public static String getNameForView(CameraOption cameraOption) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(StorytellingSceneEditor.class.getPackage().getName() + ".cameraViews");
    if (cameraOption != null) {
      return switch (cameraOption) {
        case STARTING_CAMERA_VIEW -> resourceBundle.getString("sceneCameraView");
        case LAYOUT_SCENE_VIEW -> resourceBundle.getString("layoutPerspectiveView");
        case TOP -> resourceBundle.getString("topOrthographicView");
        case SIDE -> resourceBundle.getString("sideOrthographicView");
        case FRONT -> resourceBundle.getString("frontOrthographicView");
      };
    }
    return "";
  }

  public static String getNameForCamera(CameraOption option) {
    return getNameForView(option);
  }

  public static void addIconForCameraOption(CameraOption option, String iconName) {
    URL normalIconURL = StorytellingSceneEditor.class.getResource("images/" + iconName + "Icon.png");
    assert normalIconURL != null;
    ImageIcon normalIcon = new ImageIcon(normalIconURL);
    URL highlightedIconURL = StorytellingSceneEditor.class.getResource("images/" + iconName + "Icon_highlighted.png");
    assert highlightedIconURL != null;
    ImageIcon highlightedIcon = new ImageIcon(highlightedIconURL);

    cameraToIconMap.put(option, Tuple2.createInstance(new ImageIconFactory(normalIcon), new ImageIconFactory(highlightedIcon)));
  }

  private static ImageIcon loadIconForObjectMarker(Color color) {
    URL markerIconURL = StorytellingSceneEditor.class.getResource("images/axis" + getIconSuffixForMarkerColor(color));
    assert markerIconURL != null : color;
    ImageIcon markerIcon = new ImageIcon(markerIconURL);
    return markerIcon;
  }

  private static Icon loadIconForCameraMarker(Color color) {
    URL markerIconURL = StorytellingSceneEditor.class.getResource(
        StageIDE.getActiveInstance().getSceneEditor().isVrActive() ? "images/vrMarkerIconGrayScale.png" : "images/markerIconGrayScale.png");
    if (markerIconURL == null) {
      return null;
    }
    try {
      BufferedImage markerImage = ImageIO.read(markerIconURL);
      applyColor(markerImage, color);
      if ((markerImage.getWidth() == MarkerUtilities.ICON_SIZE.width) && markerImage.getHeight() == MarkerUtilities.ICON_SIZE.height) {
        return new ImageIcon(markerImage);
      } else {
        return new ScaledIcon(new ImageIcon(markerImage), MarkerUtilities.ICON_SIZE);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void applyColor(BufferedImage image, Color color) {
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        int rgb = image.getRGB(x, y);
        java.awt.Color pixelColor = new java.awt.Color(rgb);
        if ((rgb & 0xFF000000) == 0xFF000000) {
          image.setRGB(x, y, color.applyTo(pixelColor).getRGB());
        }
      }
    }
  }

  public static IconFactory getIconFactoryForObjectMarker(UserField marker) {
    if (marker != null) {
      Color markerColor = getColorForMarkerField(marker);
      return IconFactoryManager.getIconFactoryForObjectMarker(markerColor);
    }
    return null;
  }

  public static Icon getIconForCameraMarker(UserField marker) {
    if (marker != null) {
      Color markerColor = getColorForMarkerField(marker);
      return getCameraMarkIconForColor(markerColor);
    }
    return null;
  }

  public static Icon getCameraMarkIconForColor(Color markerColor) {
    return getIcon(markerColor,
        StageIDE.getActiveInstance().getSceneEditor().isVrActive() ? colorToVrUserIcon : colorToCameraIcon,
        MarkerUtilities::loadIconForCameraMarker);
  }

  public static Icon getObjectMarkIconForColor(Color markerColor) {
    return getIcon(markerColor, colorToObjectIcon, MarkerUtilities::loadIconForObjectMarker);
  }

  private static Icon getIcon(Color markerColor, HashMap<Color, Icon> colorsToIcons, Function<Color, Icon> creator) {
    if (colorsToIcons.containsKey(markerColor)) {
      return colorsToIcons.get(markerColor);
    }
    Icon icon = creator.apply(markerColor);
    colorsToIcons.put(markerColor, icon);
    return icon;
  }

  public static Icon getIconForMarkerField(UserField markerField) {
    if (markerField == null) {
      return null;
    }
    Color markerColor = getColorForMarkerField(markerField);
    if (markerField.getValueType().isAssignableTo(CameraMarker.class)) {
      return getCameraMarkIconForColor(markerColor);
    }
    return getObjectMarkIconForColor(markerColor);
  }

  private static IconFactory getIconFactoryForCamera(CameraOption camera) {
    assert cameraToIconMap.containsKey(camera);
    return cameraToIconMap.get(camera).getA();
  }

  private static IconFactory getHighlightedIconFactoryForCamera(CameraOption camera) {
    assert cameraToIconMap.containsKey(camera);
    return cameraToIconMap.get(camera).getB();
  }

  public static final Dimension ICON_SIZE = Theme.MEDIUM_SQUARE_ICON_SIZE;

  public static Icon getIconForCamera(CameraOption option) {
    if (option != null) {
      IconFactory factory = getIconFactoryForCamera(option);
      return factory.getIconToFit(ICON_SIZE);
    }
    return null;
  }

  public static Icon getHighlightedIconForCamera(CameraOption option) {
    if (option != null) {
      IconFactory factory = getHighlightedIconFactoryForCamera(option);
      return factory.getIconToFit(ICON_SIZE);
    }
    return null;
  }

  private static Color getNewMarkerColor(Class<? extends SMarker> markerCls) {
    AbstractType<?, ?, ?> sceneType = StageIDE.getActiveInstance().getSceneEditor().getActiveSceneField().getValueType();
    int[] colorCounts = new int[getColorCount()];
    Arrays.fill(colorCounts, 0);
    List<? extends AbstractField> fields = sceneType.getDeclaredFields();
    for (AbstractField f : fields) {
      if (f.getValueType().isAssignableTo(markerCls)) {
        SMarker marker = StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField(f, markerCls);
        if (marker != null) {
          int colorIndex = getColorIndexForColor(marker.getColorId());
          if (colorIndex != -1) {
            colorCounts[colorIndex]++;
          }
        }
      }
    }
    int minIndex = 0;
    int minCount = Integer.MAX_VALUE;
    for (int i = 0; i < colorCounts.length; i++) {
      if (colorCounts[i] < minCount) {
        minIndex = i;
        minCount = colorCounts[i];
      }
    }
    return getColorForIndex(minIndex);
  }

  public static Color getNewObjectMarkerColor() {
    return getNewMarkerColor(SThingMarker.class);
  }

  public static Color getNewCameraMarkerColor() {
    return getNewMarkerColor(CameraMarker.class);
  }

  public static Color getColorForMarkerField(UserField markerField) {
    SMarker marker = StageIDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForField(markerField, SMarker.class);
    if (marker != null) {
      return marker.getColorId();
    } else {
      return Color.WHITE;
    }
  }

  public static String getBaseNameForObjectMarker() {
    return findLocalizedText("defaultMarkerName");
  }

  public static String getBaseNameForCameraMarker() {
    return findLocalizedText("defaultCameraMarkerName");
  }
}
