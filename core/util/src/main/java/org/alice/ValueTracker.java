package org.alice;

// Optionally hold a shared value.
public interface ValueTracker {
  int check(int value);
  void reset();

  // The pass through tracker maintains no shared value.
  ValueTracker PASS_THROUGH = new ValueTracker() {
    @Override
    public int check(int value) {
      return value;
    }

    @Override
    public void reset() {
    }
  };

  // Keep the highest value checked
  class MaximumTracker implements ValueTracker {
    private int max;

    @Override
    public int check(int value) {
      max = Math.max(max, value);
      return max;
    }

    @Override
    public void reset() {
      max = 0;
    }
  }
}
