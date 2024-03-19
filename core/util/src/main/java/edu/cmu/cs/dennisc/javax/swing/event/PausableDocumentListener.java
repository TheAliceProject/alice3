package edu.cmu.cs.dennisc.javax.swing.event;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.swing.event.DocumentEvent;
import java.util.function.Consumer;

public class PausableDocumentListener extends UnifiedDocumentListener {
  public PausableDocumentListener(Consumer<DocumentEvent> update) {
    super(update);
  }

  private int ignoreCount = 0;

  public void pause() {
    ignoreCount++;
  }

  public void resume() {
    ignoreCount--;
  }

  protected void update(DocumentEvent e) {
    if (ignoreCount == 0) {
      super.update(e);
    } else {
      if (ignoreCount < 0) {
        Logger.severe("Listener resumed without first pausing.");
      }
    }
  }
}
