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
package org.alice.stageide.personresource.views.renderers;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.UrlAsynchronousIcon;
import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import org.alice.stageide.personresource.PersonResourceComposite;
import org.alice.stageide.personresource.views.IngredientsView;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.SkinTone;
import org.lgna.story.resourceutilities.StorytellingResources;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class IngredientListCellRenderer<E> extends ListCellRenderer<E> {

  private static final File GALLERY_ROOT = StorytellingResources.getGalleryRootDirectory();
  private static final File IMAGE_ROOT = new File(GALLERY_ROOT, "ide/person");
  private static final String DEFAULT_SKIN_TONE_NAME = "GRAY";

  private static final Map<URL, Icon> map = Maps.newHashMap();
  private static final Map<String, URL> pathMap = Maps.newHashMap();

  private static final Map<String, Boolean> skinToneIconMap = Maps.newHashMap();

  private static boolean hasIconForPath(String path) {
    URL urlForIcon = getURLForPath(path);
    return urlForIcon != null;
  }

  public static URL getURLForPath(String path) {
    URL urlForIcon = null;
    if (pathMap.containsKey(path)) {
      urlForIcon = pathMap.get(path);
    }
    if (urlForIcon == null) {
      File file = new File(IMAGE_ROOT, path);
      if (file.exists()) {
        try {
          urlForIcon = file.toURL();
        } catch (MalformedURLException murle) {
          Logger.throwable(murle, file);
          urlForIcon = null;
        }
      } else {
        //edu.cmu.cs.dennisc.java.util.logging.Logger.errln( file );
        urlForIcon = null;
      }
      //      if( urlForIcon != null ) {
      //        //pass
      //      } else {
      //        edu.cmu.cs.dennisc.java.util.logging.Logger.severe( path );
      //      }
      pathMap.put(path, urlForIcon);
    }
    return urlForIcon;
  }

  public static Icon getIconForPath(int width, int height, String path) {
    URL urlForIcon = getURLForPath(path);
    Icon rv = map.get(urlForIcon);
    if (rv != null) {
      //pass
    } else {
      rv = new UrlAsynchronousIcon(width, height, urlForIcon);
      map.put(urlForIcon, rv);
    }
    return rv;
    //return edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( urlForIcon );
  }

  private Border border = BorderFactory.createEmptyBorder(2, 2, 2, 2);

  public IngredientListCellRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public boolean ACCEPTABLE_HACK_AT_THIS_TIME_FOR_LIST_DATA_hasValidImageFor(E value, SkinTone baseSkinTone) {
    Object v = getValue(value);
    String clsName = v.getClass().getSimpleName();
    clsName = this.modifyClsNameIfNecessary(clsName, PersonResourceComposite.getInstance().getIngredientsComposite().getLifeStageState().getValue(), PersonResourceComposite.getInstance().getIngredientsComposite().getGenderState().getValue());
    String enumConstantName = v.toString();
    String path = this.getValidIconPath(baseSkinTone, clsName, enumConstantName);

    return path != null;
  }

  protected abstract String getSubPath();

  private SkinTone getSkinTone() {
    return PersonResourceComposite.getInstance().getIngredientsComposite().getClosestBaseSkinTone();
  }

  private String getIngredientResourceName(String skinToneString, String clsName, String enumConstantName) {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getSubPath());
    sb.append("/");
    sb.append(skinToneString);
    sb.append("/");
    sb.append(clsName);
    sb.append(".");
    sb.append(enumConstantName);
    sb.append(".png");
    return sb.toString();
  }

  private String getIngredientResourceName(SkinTone skinTone, String clsName, String enumConstantName) {
    return getIngredientResourceName(skinTone.toString(), clsName, enumConstantName);
  }

  protected String modifyClsNameIfNecessary(String clsName, LifeStage lifeStage, Gender gender) {
    return clsName;
  }

  protected Object getValue(E value) {
    return value;
  }

  private String getValidIconPath(SkinTone baseSkinTone, String clsName, String enumConstantName) {
    String path = null;

    boolean lookForSkinToneIcon = true;
    if (skinToneIconMap.containsKey(clsName)) {
      lookForSkinToneIcon = skinToneIconMap.get(clsName);
    }
    if ((baseSkinTone != null) && lookForSkinToneIcon) {
      path = this.getIngredientResourceName(baseSkinTone, clsName, enumConstantName);
      //If the clsName isn't in the map, then check to see if there's an icon for this path and remember the results by recording it in the map
      if (!skinToneIconMap.containsKey(clsName)) {
        if (!hasIconForPath(path)) {
          skinToneIconMap.put(clsName, false);
          path = this.getIngredientResourceName(DEFAULT_SKIN_TONE_NAME, clsName, enumConstantName);
        } else {
          skinToneIconMap.put(clsName, true);
        }
      }
    } else {
      path = this.getIngredientResourceName(DEFAULT_SKIN_TONE_NAME, clsName, enumConstantName);
    }
    if (hasIconForPath(path)) {
      return path;
    }
    return null;
  }

  @Override
  protected JLabel getListCellRendererComponent(JLabel rv, JList list, E val, int index, boolean isSelected, boolean cellHasFocus) {
    assert rv != null;
    Object v = getValue(val);
    if (v != null) {
      String clsName = v.getClass().getSimpleName();
      clsName = this.modifyClsNameIfNecessary(clsName, PersonResourceComposite.getInstance().getIngredientsComposite().getLifeStageState().getValue(), PersonResourceComposite.getInstance().getIngredientsComposite().getGenderState().getValue());
      String enumConstantName = v.toString();

      rv.setHorizontalTextPosition(SwingConstants.CENTER);
      rv.setVerticalTextPosition(SwingConstants.BOTTOM);

      rv.setOpaque(isSelected);
      SkinTone baseSkinTone = this.getSkinTone();
      Icon icon;
      String path = getValidIconPath(baseSkinTone, clsName, enumConstantName);
      icon = getIconForPath(this.width, this.height, path);
      if (icon != null) {
        rv.setIcon(icon);
        rv.setText("");
        if (isSelected) {
          rv.setBackground(IngredientsView.SELECTED_COLOR);
        }
      } else {
        rv.setText("image not found");
      }
      rv.setBorder(this.border);
    } else {
      rv.setText("null");
    }
    return rv;
  }

  private final int width;
  private final int height;
}
