package org.alice.tweedle.ast;

import org.alice.tweedle.TweedleArray;
import org.alice.tweedle.TweedleArrayType;
import org.alice.tweedle.TweedleType;
import org.alice.tweedle.TweedleValue;
import org.alice.tweedle.run.Frame;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class TweedleArrayInitializer extends TweedleExpression {
  private List<TweedleExpression> elements;
  private TweedleExpression initializeSize;

  public TweedleArrayInitializer(TweedleArrayType arrayType, List<TweedleExpression> elements) {
    super(arrayType);
    this.elements = elements;
  }

  public TweedleArrayInitializer(TweedleType elementType, List<TweedleExpression> elements) {
    super(new TweedleArrayType(elementType));
    this.elements = elements;
  }

  public TweedleArrayInitializer(List<TweedleExpression> elements) {
    super(new TweedleArrayType(findCommonType(elements)));
    this.elements = elements;
  }

  public TweedleArrayInitializer(TweedleArrayType arrayType, TweedleExpression initializeSize) {
    super(arrayType);
    this.initializeSize = initializeSize;
  }

  private static TweedleType findCommonType(List<TweedleExpression> elements) {
    // TODO
    return null;
  }

  @Override
  public TweedleValue evaluate(Frame frame) {
    return new TweedleArray((TweedleArrayType) this.getType(), elements.stream().map(el -> el.evaluate(frame)).collect(toList()));
  }

  @Override
  public String toString() {
    return getType().getName() + " {" + elements.stream().map(Object::toString).collect(Collectors.joining(",")) + "}";
  }
}
