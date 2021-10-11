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
package org.alice.ide.cascade;

import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Stacks;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.cascade.fillerinners.AudioResourceFillerInner;
import org.alice.ide.cascade.fillerinners.BooleanFillerInner;
import org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner;
import org.alice.ide.cascade.fillerinners.DoubleFillerInner;
import org.alice.ide.cascade.fillerinners.ExpressionFillerInner;
import org.alice.ide.cascade.fillerinners.ImageResourceFillerInner;
import org.alice.ide.cascade.fillerinners.IntegerFillerInner;
import org.alice.ide.cascade.fillerinners.PoseFillerInner;
import org.alice.ide.cascade.fillerinners.StringFillerInner;
import org.alice.ide.croquet.models.cascade.LocalAccessFillIn;
import org.alice.ide.croquet.models.cascade.ParameterAccessFillIn;
import org.alice.ide.croquet.models.cascade.PreviousExpressionItselfFillIn;
import org.alice.ide.croquet.models.cascade.SimpleExpressionFillIn;
import org.alice.ide.croquet.models.cascade.ThisExpressionFillIn;
import org.alice.ide.croquet.models.cascade.ThisFieldAccessFillIn;
import org.alice.ide.croquet.models.cascade.TypeExpressionCascadeMenu;
import org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn;
import org.alice.ide.croquet.models.cascade.array.ArrayLengthFillIn;
import org.alice.ide.croquet.models.cascade.array.ArrayLengthSeparator;
import org.alice.ide.croquet.models.cascade.array.LocalArrayLengthFillIn;
import org.alice.ide.croquet.models.cascade.array.ParameterArrayLengthFillIn;
import org.alice.ide.croquet.models.cascade.array.ThisFieldArrayLengthFillIn;
import org.alice.ide.croquet.models.cascade.literals.NullLiteralFillIn;
import org.alice.ide.croquet.models.cascade.string.StringConcatinationLeftAndRightOperandsFillIn;
import org.alice.ide.croquet.models.cascade.string.StringConcatinationRightOperandOnlyFillIn;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.custom.ArrayCustomExpressionCreatorComposite;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeItemMenuCombo;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.AbstractEachInTogether;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractForEachLoop;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.BooleanExpressionBodyPair;
import org.lgna.project.ast.CountLoop;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.LocalAccess;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserParameter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionCascadeManager {
  private final BooleanFillerInner booleanFillerInner = new BooleanFillerInner();
  private final StringFillerInner stringFillerInner = new StringFillerInner();
  private final List<ExpressionFillerInner> expressionFillerInners = Lists.newLinkedList();

  private final DStack<ExpressionCascadeContext> contextStack = Stacks.newStack();

  public ExpressionCascadeManager() {
    this.addExpressionFillerInner(new DoubleFillerInner());
    this.addExpressionFillerInner(new IntegerFillerInner());
    this.addExpressionFillerInner(this.booleanFillerInner);
    this.addExpressionFillerInner(this.stringFillerInner);
    this.addExpressionFillerInner(new AudioResourceFillerInner());
    this.addExpressionFillerInner(new ImageResourceFillerInner());
    this.addExpressionFillerInner(new PoseFillerInner());
  }

  public void addRelationalTypeToBooleanFillerInner(AbstractType<?, ?, ?> operandType) {
    this.booleanFillerInner.addRelationalType(operandType);
  }

  public void addRelationalTypeToBooleanFillerInner(Class<?> operandCls) {
    addRelationalTypeToBooleanFillerInner(JavaType.getInstance(operandCls));
  }

  protected void addExpressionFillerInner(ExpressionFillerInner expressionFillerInner) {
    this.expressionFillerInners.add(expressionFillerInner);
  }

  private final ExpressionCascadeContext NULL_CONTEXT = new ExpressionCascadeContext() {
    @Override
    public Expression getPreviousExpression() {
      return null;
    }

    @Override
    public BlockStatementIndexPair getBlockStatementIndexPair() {
      return null;
    }
  };

  private ExpressionCascadeContext safePeekContext() {
    if (this.contextStack.size() > 0) {
      return this.contextStack.peek();
    } else {
      return NULL_CONTEXT;
    }
  }

  public void pushContext(ExpressionCascadeContext context) {
    assert context != null;
    this.contextStack.push(context);
  }

  public void pushNullContext() {
    this.pushContext(NULL_CONTEXT);
  }

  public ExpressionCascadeContext popContext() {
    if (this.contextStack.size() > 0) {
      return this.contextStack.pop();
    } else {
      Logger.severe("contextStack is empty", this);
      //todo?
      return NULL_CONTEXT;
    }
  }

  public ExpressionCascadeContext popAndCheckContext(ExpressionCascadeContext expectedContext) {
    ExpressionCascadeContext poppedContext = this.popContext();
    if (poppedContext != expectedContext) {
      Logger.severe(poppedContext, expectedContext);
    }
    return poppedContext;
  }

  public void popAndCheckNullContext() {
    this.popAndCheckContext(NULL_CONTEXT);
  }

  public Expression getPreviousExpression() {
    return this.safePeekContext().getPreviousExpression();
  }

  private LinkedList<UserLocal> updateAccessibleLocalsForBlockStatementAndIndex(LinkedList<UserLocal> rv, BlockStatement blockStatement, int index) {
    while (index >= 1) {
      index--;
      //todo: investigate
      if (index >= blockStatement.statements.size()) {
        try {
          throw new IndexOutOfBoundsException(index + " " + blockStatement.statements.size());
        } catch (IndexOutOfBoundsException ioobe) {
          Logger.throwable(ioobe);
        }
        index = blockStatement.statements.size();
      } else {
        Statement statementI = blockStatement.statements.get(index);
        if (statementI instanceof LocalDeclarationStatement) {
          LocalDeclarationStatement localDeclarationStatement = (LocalDeclarationStatement) statementI;
          rv.add(localDeclarationStatement.local.getValue());
        }
      }
    }
    return rv;
  }

  private LinkedList<UserLocal> updateAccessibleLocals(LinkedList<UserLocal> rv, Statement statement) {
    Node parent = statement.getParent();
    if (parent instanceof BooleanExpressionBodyPair) {
      parent = parent.getParent();
    }
    if (parent instanceof Statement) {
      Statement statementParent = (Statement) parent;
      if (statementParent instanceof BlockStatement) {
        BlockStatement blockStatementParent = (BlockStatement) statementParent;
        int index = blockStatementParent.statements.indexOf(statement);
        this.updateAccessibleLocalsForBlockStatementAndIndex(rv, blockStatementParent, index);
      } else if (statementParent instanceof CountLoop) {
        CountLoop countLoopParent = (CountLoop) statementParent;
        boolean areCountLoopLocalsViewable = FormatterState.isJava();
        if (areCountLoopLocalsViewable) {
          rv.add(countLoopParent.variable.getValue());
        }
      } else if (statementParent instanceof AbstractForEachLoop) {
        AbstractForEachLoop forEachLoopParent = (AbstractForEachLoop) statementParent;
        rv.add(forEachLoopParent.item.getValue());
      } else if (statementParent instanceof AbstractEachInTogether) {
        AbstractEachInTogether eachInTogetherParent = (AbstractEachInTogether) statementParent;
        rv.add(eachInTogetherParent.item.getValue());
      }
      updateAccessibleLocals(rv, statementParent);
    }
    return rv;
  }

  public Iterable<UserLocal> getAccessibleLocals(BlockStatementIndexPair blockStatementIndexPair) {
    LinkedList<UserLocal> rv = Lists.newLinkedList();
    updateAccessibleLocalsForBlockStatementAndIndex(rv, blockStatementIndexPair.getBlockStatement(), blockStatementIndexPair.getIndex());
    updateAccessibleLocals(rv, blockStatementIndexPair.getBlockStatement());
    return rv;
  }

  protected final boolean isApplicableForFillIn(AbstractType<?, ?, ?> desiredType, AbstractType<?, ?, ?> expressionType) {
    return desiredType.isAssignableFrom(expressionType);
  }

  protected abstract boolean isApplicableForPartFillIn(AbstractType<?, ?, ?> desiredType, AbstractType<?, ?, ?> expressionType);

  protected final boolean isApplicableForFillInAndPossiblyPartFillIns(AbstractType<?, ?, ?> desiredType, AbstractType<?, ?, ?> expressionType) {
    return this.isApplicableForFillIn(desiredType, expressionType) || this.isApplicableForPartFillIn(desiredType, expressionType);
  }

  protected abstract CascadeMenuModel<Expression> createPartMenuModel(Expression expression, AbstractType<?, ?, ?> desiredType, AbstractType<?, ?, ?> expressionType, boolean isOwnedByCascadeItemMenuCombo);

  private void appendFillInAndPossiblyPartFillIns(List<CascadeBlankChild> blankChildren, AbstractType<?, ?, ?> desiredType, Expression expression, AbstractType<?, ?, ?> expressionType) {
    CascadeBlankChild blankChild;
    CascadeFillIn<? extends Expression, ?> expressionFillIn;
    if (this.isApplicableForFillIn(desiredType, expressionType)) {
      if (expression instanceof ThisExpression) {
        ThisExpression thisExpression = (ThisExpression) expression;
        expressionFillIn = ThisExpressionFillIn.getInstance();
      } else if (expression instanceof FieldAccess) {
        FieldAccess fieldAccess = (FieldAccess) expression;
        Expression instanceExpression = fieldAccess.expression.getValue();
        if (instanceExpression instanceof ThisExpression) {
          expressionFillIn = ThisFieldAccessFillIn.getInstance(fieldAccess.field.getValue());
        } else {
          expressionFillIn = null;
        }
      } else if (expression instanceof ParameterAccess) {
        ParameterAccess parameterAccess = (ParameterAccess) expression;
        expressionFillIn = ParameterAccessFillIn.getInstance(parameterAccess.parameter.getValue());
      } else if (expression instanceof LocalAccess) {
        LocalAccess localAccess = (LocalAccess) expression;
        expressionFillIn = LocalAccessFillIn.getInstance(localAccess.local.getValue());
      } else {
        expressionFillIn = null;
      }
      if (expressionFillIn != null) {
        //pass
      } else {
        boolean isLeadingIconDesired = true;
        expressionFillIn = new SimpleExpressionFillIn<Expression>(expression, isLeadingIconDesired);
      }
    } else {
      expressionFillIn = null;
    }
    if (this.isApplicableForPartFillIn(desiredType, expressionType)) {
      CascadeMenuModel<Expression> menuModel = this.createPartMenuModel(expression, desiredType, expressionType, expressionFillIn != null);
      if (menuModel != null) {
        if (expressionFillIn != null) {
          blankChild = new CascadeItemMenuCombo(expressionFillIn, menuModel);
        } else {
          blankChild = menuModel;
        }
      } else {
        blankChild = expressionFillIn;
      }
    } else {
      if (expressionFillIn != null) {
        blankChild = expressionFillIn;
      } else {
        blankChild = null;
      }
    }
    if (blankChild != null) {
      blankChildren.add(blankChild);
    }
  }

  private void appendFillInAndPossiblyPartFillIns(List<CascadeBlankChild> blankChildren, AbstractType<?, ?, ?> desiredType, Expression expression) {
    this.appendFillInAndPossiblyPartFillIns(blankChildren, desiredType, expression, expression.getType());
  }

  private void appendExpressionBonusFillInsForType(List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode, AbstractType<?, ?, ?> type) {
    Expression prevExpression = this.safePeekContext().getPreviousExpression();
    BlockStatementIndexPair blockStatementIndexPair = this.safePeekContext().getBlockStatementIndexPair();
    List<ArrayLengthFillIn> arrayLengthFillIns;
    if (blankNode.isTop()) {
      if ((type == JavaType.INTEGER_OBJECT_TYPE) || (type.isAssignableFrom(JavaType.INTEGER_OBJECT_TYPE) && (prevExpression != null))) {
        arrayLengthFillIns = Lists.newLinkedList();
      } else {
        arrayLengthFillIns = null;
      }
    } else {
      arrayLengthFillIns = null;
    }

    AbstractType<?, ?, ?> selectedType = IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue();
    if (this.isApplicableForFillInAndPossiblyPartFillIns(type, selectedType)) {
      this.appendFillInAndPossiblyPartFillIns(blankChildren, type, new ThisExpression(), selectedType);
    }

    for (AbstractField field : selectedType.getDeclaredFields()) {
      AbstractType<?, ?, ?> fieldType = field.getValueType();
      if (fieldType != null) {
        if (this.isApplicableForFillInAndPossiblyPartFillIns(type, fieldType)) {
          Expression fieldAccess = new FieldAccess(field);
          this.appendFillInAndPossiblyPartFillIns(blankChildren, type, fieldAccess);
        }
        if (fieldType.isArray()) {
          AbstractType<?, ?, ?> fieldComponentType = fieldType.getComponentType();
          if (this.isApplicableForFillInAndPossiblyPartFillIns(type, fieldComponentType)) {
            Expression fieldAccess = new FieldAccess(field);
            blankChildren.add(new ArrayAccessFillIn(fieldAccess));
          }
          if (arrayLengthFillIns != null) {
            arrayLengthFillIns.add(ThisFieldArrayLengthFillIn.getInstance(field));
          }
        }
      }
    }

    AbstractCode codeInFocus = IDE.getActiveInstance().getDocumentFrame().getFocusedCode();
    if (codeInFocus instanceof UserCode) {
      UserCode userCode = (UserCode) codeInFocus;
      for (UserParameter parameter : userCode.getRequiredParamtersProperty()) {
        AbstractType<?, ?, ?> parameterType = parameter.getValueType();
        if (this.isApplicableForFillInAndPossiblyPartFillIns(type, parameterType)) {
          this.appendFillInAndPossiblyPartFillIns(blankChildren, type, new ParameterAccess(parameter));
        }
        if (parameterType.isArray()) {
          AbstractType<?, ?, ?> parameterArrayComponentType = parameterType.getComponentType();
          if (this.isApplicableForFillInAndPossiblyPartFillIns(type, parameterArrayComponentType)) {
            blankChildren.add(new ArrayAccessFillIn(new ParameterAccess(parameter)));
          }
          if (arrayLengthFillIns != null) {
            arrayLengthFillIns.add(ParameterArrayLengthFillIn.getInstance(parameter));
          }
        }
      }
      if (blockStatementIndexPair != null) {
        for (UserLocal local : this.getAccessibleLocals(blockStatementIndexPair)) {
          AbstractType<?, ?, ?> localType = local.getValueType();
          if (this.isApplicableForFillInAndPossiblyPartFillIns(type, localType)) {
            this.appendFillInAndPossiblyPartFillIns(blankChildren, type, new LocalAccess(local));
          }
          if (localType.isArray()) {
            AbstractType<?, ?, ?> localArrayComponentType = localType.getComponentType();
            if (this.isApplicableForFillInAndPossiblyPartFillIns(type, localArrayComponentType)) {
              blankChildren.add(new ArrayAccessFillIn(new LocalAccess(local)));
            }
            if (localType.isArray()) {
              if (arrayLengthFillIns != null) {
                arrayLengthFillIns.add(LocalArrayLengthFillIn.getInstance(local));
              }
            }
          }
        }
      }
    }
    if (arrayLengthFillIns != null) {
      if (arrayLengthFillIns.size() > 0) {
        blankChildren.add(ArrayLengthSeparator.getInstance());
        blankChildren.addAll(arrayLengthFillIns);
      }
    }
  }

  protected AbstractType<?, ?, ?> getEnumTypeForInterfaceType(AbstractType<?, ?, ?> interfaceType) {
    return null;
  }

  protected List<CascadeBlankChild> addCustomFillIns(List<CascadeBlankChild> rv, BlankNode<Expression> step, AbstractType<?, ?, ?> type) {
    return rv;
  }

  protected AbstractType<?, ?, ?> getTypeFor(AbstractType<?, ?, ?> type) {
    if (type == JavaType.getInstance(Number.class)) {
      return JavaType.DOUBLE_OBJECT_TYPE;
    } else {
      return type;
    }
  }

  protected boolean areEnumConstantsDesired(AbstractType<?, ?, ?> enumType) {
    return true;
  }

  protected boolean isNullLiteralAllowedForType(AbstractType<?, ?, ?> type, List<CascadeBlankChild> items) {
    return false;
  }

  public void appendItems(List<CascadeBlankChild> items, BlankNode<Expression> blankNode, AbstractType<?, ?, ?> type, ValueDetails<?> details) {
    if (type != null) {
      boolean isRoot = blankNode.isTop();

      ExpressionCascadeContext context = this.safePeekContext();

      Expression prevExpression = context.getPreviousExpression();
      if (isRoot && (prevExpression != null)) {
        if (type.isAssignableFrom(prevExpression.getType())) {
          items.add(PreviousExpressionItselfFillIn.getInstance(type));
          items.add(CascadeLineSeparator.getInstance());
        } else {
          Logger.severe(prevExpression);
        }
      }
      this.addCustomFillIns(items, blankNode, type);
      type = this.getTypeFor(type);
      boolean isOtherTypeMenuDesired = type == JavaType.OBJECT_TYPE;
      if (isOtherTypeMenuDesired) {
        if (isRoot && (prevExpression != null)) {
          type = prevExpression.getType();
        }
      }
      if (type == JavaType.OBJECT_TYPE) {
        type = JavaType.STRING_TYPE;
      }
      for (ExpressionFillerInner expressionFillerInner : this.expressionFillerInners) {
        if (expressionFillerInner.isAssignableTo(type)) {
          expressionFillerInner.appendItems(items, details, isRoot, prevExpression);
          items.add(CascadeLineSeparator.getInstance());
        }
      }
      if (type == JavaType.STRING_TYPE) {
        this.appendConcatenationItemsIfAppropriate(items, details, isRoot, prevExpression);
      }

      AbstractType<?, ?, ?> enumType;
      if (type.isInterface()) {
        enumType = this.getEnumTypeForInterfaceType(type);
      } else {
        if (type.isAssignableTo(Enum.class)) {
          enumType = type;
        } else {
          enumType = null;
        }
      }
      if ((enumType != null) && this.areEnumConstantsDesired(enumType)) {
        items.add(CascadeLineSeparator.getInstance());
        ConstantsOwningFillerInner.getInstance(enumType).appendItems(items, details, isRoot, prevExpression);
      }

      items.add(CascadeLineSeparator.getInstance());
      this.appendExpressionBonusFillInsForType(items, blankNode, type);
      items.add(CascadeLineSeparator.getInstance());
      if (type.isArray()) {
        items.add(ArrayCustomExpressionCreatorComposite.getInstance(type).getValueCreator().getFillIn());
      }
      if (this.isNullLiteralAllowedForType(type, items)) {
        items.add(NullLiteralFillIn.getInstance());
      }

      if (isOtherTypeMenuDesired) {
        List<AbstractType<?, ?, ?>> otherTypes = Lists.newLinkedList();
        this.appendOtherTypes(otherTypes);
        for (AbstractType<?, ?, ?> otherType : otherTypes) {
          if (type == otherType) {
            //pass
          } else {
            items.add(TypeExpressionCascadeMenu.getInstance(otherType));
          }
        }
      }
    } else {
      Logger.severe("type is null");
    }
  }

  protected void appendOtherTypes(List<AbstractType<?, ?, ?>> types) {
    types.add(JavaType.STRING_TYPE);
    types.add(JavaType.DOUBLE_OBJECT_TYPE);
    types.add(JavaType.INTEGER_OBJECT_TYPE);
  }

  private void appendConcatenationItemsIfAppropriate(List<CascadeBlankChild> items, ValueDetails<?> details, boolean isTop, Expression prevExpression) {
    if (isTop) {
      if (prevExpression != null) {
        items.add(CascadeLineSeparator.getInstance());
        if (prevExpression instanceof NullLiteral) {
          //pass
        } else {
          if (prevExpression.getType().isAssignableTo(String.class)) {
            items.add(StringConcatinationRightOperandOnlyFillIn.getInstance());
          }
        }
        items.add(StringConcatinationLeftAndRightOperandsFillIn.getInstance());
      }
    }
  }

}
