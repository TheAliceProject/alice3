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
package org.alice.ide.browser;

import edu.cmu.cs.dennisc.browser.BrowserUtilities;
import edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.ide.issue.croquet.AnomalousSituationComposite;
import org.alice.ide.operations.InconsequentialActionOperation;

import javax.swing.SwingUtilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class BrowserOperation extends InconsequentialActionOperation {
  public static final String ALICE_HOME_URL = "http://www.alice.org";
  public static final String WIKI_URL = "https://github.com/TheAliceProject/alice3/wiki";
  public static final String ALICE_USE_URL = WIKI_URL + "/Using-Alice-3";
  public static final String TROUBLESHOOTING_URL = WIKI_URL + "/Troubleshooting-Known-Issues";
  public static final String RECURSION_URL = "https://en.wikipedia.org/wiki/Recursion_%28computer_science%29";

  public BrowserOperation(UUID id, String spec) {
    super(id);
    try {
      this.url = new URL(spec);
    } catch (MalformedURLException murle) {
      throw new RuntimeException(spec, murle);
    }
  }

  protected URL getUrl() {
    return this.url;
  }

  @Override
  protected final void localize() {
    String spec = this.url.toString();
    this.setName(spec);
    super.localize();
    String name = this.getImp().getName();
    if (!Objects.equals(spec, name)) {
      this.setToolTipText(spec);
    }
  }

  @Override
  protected void performInternal() {
    URL url = this.getUrl();
    if (url != null) {
      try {
        BrowserUtilities.browse(url);
      } catch (Exception e) {
        ClipboardUtilities.setClipboardContents(url.toString());
        // TODO I18n
        Dialogs.showInfo("An error has occurred in attempting to start your web browser.\n\nThe following text has been copied to your clipboard: \n\n\t" + url + "\n\nso that you may paste it into your web browser.");
      }
    } else {
      // TODO I18n
      final AnomalousSituationComposite composite =
          AnomalousSituationComposite.createInstance(
              "Oh no!  We do not know which web page to send you to.",
              "URL is null for " + this.getClass());
      SwingUtilities.invokeLater(() -> composite.getLaunchOperation().fire());
    }
  }

  private final URL url;
}
