package org.alice.tweedle;

import java.util.List;

public class TweedleConstructor extends TweedleMethod {
  public TweedleConstructor(TweedleType type, String name, List<TweedleRequiredParameter> required, List<TweedleOptionalParameter> optional, List<TweedleStatement> body) {
    super(type, name, required, optional, body);
  }
}
