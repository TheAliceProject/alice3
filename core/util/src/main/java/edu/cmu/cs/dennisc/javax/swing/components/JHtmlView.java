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
package edu.cmu.cs.dennisc.javax.swing.components;

import edu.cmu.cs.dennisc.worker.url.TextUrlWorker;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import java.awt.Image;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Dennis Cosgrove
 */
public abstract class JHtmlView extends JEditorPane {
  public JHtmlView(String text) {
    super("text/html", text);
    assert this.getDocument() instanceof HTMLDocument : this.getDocument();
    this.setEditable(false);
  }

  public JHtmlView() {
    this("");
  }

  private Dictionary<URL, Image> getImageCache() {
    final String IMAGE_CACHE_PROPERTY_NAME = "imageCache";
    Document document = this.getDocument();
    Dictionary<URL, Image> imageCache = (Dictionary<URL, Image>) document.getProperty(IMAGE_CACHE_PROPERTY_NAME);
    if (imageCache == null) {
      imageCache = new Hashtable<URL, Image>();
      document.putProperty(IMAGE_CACHE_PROPERTY_NAME, imageCache);
    }
    return imageCache;
  }

  public void addImageToCache(URL url, Image image) {
    Dictionary<URL, Image> imageCache = this.getImageCache();
    imageCache.put(url, image);
  }

  public HTMLDocument getHtmlDocument() {
    Document document = this.getDocument();
    return (HTMLDocument) document;
  }

  public void setTextFromUrlLater(URL url) {
    TextUrlWorker worker = new TextUrlWorker(url) {
      @Override
      protected void handleDone_onEventDispatchThread(String value) {
        setText(value);
      }
    };
    worker.execute();
  }

  protected abstract void handleHyperlinkUpdate(HyperlinkEvent e);

  @Override
  public void addNotify() {
    this.addHyperlinkListener(this.hyperlinkListener);
    super.addNotify();
  }

  @Override
  public void removeNotify() {
    super.removeNotify();
    this.removeHyperlinkListener(this.hyperlinkListener);
  }

  private final HyperlinkListener hyperlinkListener = new HyperlinkListener() {
    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
      handleHyperlinkUpdate(e);
    }
  };
}
