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
package org.lgna.croquet.icon;

import edu.cmu.cs.dennisc.java.util.Lists;

import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractMultipleSourceImageIconFactory extends AbstractIconFactory {
  private final List<ImageIcon> sortedByWithSourceImageIcons;
  private final ImageIcon defaultImageIcon;

  private static int compareInts(int a, int b) {
    if (a < b) {
      return -1;
    } else {
      if (a == b) {
        return 0;
      } else {
        return 1;
      }
    }
  }

  public AbstractMultipleSourceImageIconFactory(int defaultIndex, ImageIcon... imageIcons) {
    super(IsCachingDesired.FALSE);
    assert imageIcons.length > 0 : this;
    assert defaultIndex >= 0 : defaultIndex;
    assert defaultIndex < imageIcons.length : defaultIndex;

    this.defaultImageIcon = imageIcons[defaultIndex];

    List<ImageIcon> list = Lists.newArrayList(imageIcons);
    assert list.contains(null) == false : this;

    Collections.sort(list, new Comparator<ImageIcon>() {
      @Override
      public int compare(ImageIcon o1, ImageIcon o2) {
        return compareInts(o1.getIconWidth(), o2.getIconWidth());
      }
    });

    this.sortedByWithSourceImageIcons = Collections.unmodifiableList(list);
  }

  public Iterable<ImageIcon> getSortedByWidthSourceImageIcons() {
    return this.sortedByWithSourceImageIcons;
  }

  protected ImageIcon getDefaultImageIcon() {
    return this.defaultImageIcon;
  }

  protected ImageIcon getSourceImageIcon(Dimension size) {
    for (ImageIcon icon : this.sortedByWithSourceImageIcons) {
      if (icon.getIconWidth() >= size.width) {
        return icon;
      }
    }
    return this.sortedByWithSourceImageIcons.get(this.sortedByWithSourceImageIcons.size() - 1);
  }

  @Override
  protected double getTrimmedWidthToHeightAspectRatio() {
    return this.defaultImageIcon.getIconWidth() / (double) this.defaultImageIcon.getIconHeight();
  }

  @Override
  public final Dimension getDefaultSize(Dimension sizeIfResolutionIndependent) {
    return new Dimension(this.defaultImageIcon.getIconWidth(), this.defaultImageIcon.getIconHeight());
  }
}
