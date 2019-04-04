package org.alice.tweedle;

import org.alice.tweedle.run.Frame;

public class TweedleStatement {
  private boolean enabled = true;

  public boolean isEnabled() {
    return enabled;
  }

  // TODO determine how to handle return values. Should void be changed to TweedleValue?
  public void execute(Frame frame) {
    if (isEnabled()) {
    }
  }

  public void disable() {
    enabled = false;
  }
}
