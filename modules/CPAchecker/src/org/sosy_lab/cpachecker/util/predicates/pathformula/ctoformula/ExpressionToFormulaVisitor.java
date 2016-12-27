/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2014  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 *  CPAchecker web page:
 *    http://cpachecker.sosy-lab.org
 */
package org.sosy_lab.cpachecker.util.predicates.pathformula.ctoformula;

import static org.sosy_lab.cpachecker.util.BuiltinFloatFunctions.getTypeOfBuiltinFloatFunction;
import static org.sosy_lab.cpachecker.util.predicates.pathformula.ctoformula.CtoFormulaTypeUtils.getRealFieldOwner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpression.BinaryOperator;
import org.sosy_lab.cpachecker.cfa.ast.c.CCastExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CCharLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CFieldReference;
import org.sosy_lab.cpachecker.cfa.ast.c.CFloatLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionCallExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CFunctionDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CImaginaryLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CIntegerLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CRightHandSideVisitor;
import org.sosy_lab.cpachecker.cfa.ast.c.CSimpleDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CStringLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CTypeIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CTypeIdExpression.TypeIdOperator;
import org.sosy_lab.cpachecker.cfa.ast.c.CUnaryExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CUnaryExpression.UnaryOperator;
import org.sosy_lab.cpachecker.cfa.ast.c.DefaultCExpressionVisitor;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cfa.types.c.CEnumType.CEnumerator;
import org.sosy_lab.cpachecker.cfa.types.c.CNumericTypes;
import org.sosy_lab.cpachecker.cfa.types.c.CPointerType;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.cfa.types.c.CType;
import org.sosy_lab.cpachecker.exceptions.UnrecognizedCCodeException;
import org.sosy_lab.cpachecker.exceptions.UnsupportedCCodeException;
import org.sosy_lab.cpachecker.util.BuiltinFloatFunctions;
import org.sosy_lab.cpachecker.util.predicates.pathformula.SSAMap.SSAMapBuilder;
import org.sosy_lab.cpachecker.util.predicates.smt.BooleanFormulaManagerView;
import org.sosy_lab.cpachecker.util.predicates.smt.FloatingPointFormulaManagerView;
import org.sosy_lab.cpachecker.util.predicates.smt.FormulaManagerView;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.FloatingPointFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.FormulaType;
import org.sosy_lab.java_smt.api.FormulaType.FloatingPointType;

public class ExpressionToFormulaVisitor extends DefaultCExpressionVisitor<Formula, UnrecognizedCCodeException>
                                        implements CRightHandSideVisitor<Formula, UnrecognizedCCodeException> {

  private final CtoFormulaConverter conv;
  private final CFAEdge       edge;
  private final String        function;
  private final Constraints   constraints;
  protected final FormulaManagerView mgr;
  protected final SSAMapBuilder ssa;

  public ExpressionToFormulaVisitor(
      CtoFormulaConverter pCtoFormulaConverter,
      FormulaManagerView pFmgr,
      CFAEdge pEdge,
      String pFunction,
      SSAMapBuilder pSsa,
      Constraints pConstraints) {

    conv = pCtoFormulaConverter;
    edge = pEdge;
    function = pFunction;
    ssa = pSsa;
    constraints = pConstraints;
    mgr = pFmgr;
  }

  @Override
  protected Formula visitDefault(CExpression exp)
      throws UnrecognizedCCodeException {
    return conv.makeVariableUnsafe(exp, function, ssa, false);
  }

  protected Formula toFormula(CExpression e) throws UnrecognizedCCodeException {
    return e.accept(this);
  }

  public Formula processOperand(CExpression e, CType calculationType, CType returnType) throws UnrecognizedCCodeException {
    e = conv.convertLiteralToFloatIfNecessary(e, calculationType);
    e = conv.makeCastFromArrayToPointerIfNecessary(e, returnType);
    final CType t = e.getExpressionType();
    Formula f = toFormula(e);
    return conv.makeCast(t, calculationType, f, constraints, edge);
  }

  private Formula getPointerTargetSizeLiteral(final CPointerType pointerType, final CType implicitType) {
    final int pointerTargetSize = conv.getSizeof(pointerType.getType());
    return mgr.makeNumber(conv.getFormulaTypeFromCType(implicitType), pointerTargetSize);
  }

  private CType getPromotedCType(CType t) {
    t = t.getCanonicalType();
    if (t instanceof CSimpleType) {
      // Integer types smaller than int are promoted when an operation is performed on them.
      return conv.machineModel.getPromotedCType((CSimpleType)t);
    }
    return t;
  }

  @Override
  public Formula visit(final CBinaryExpression exp) throws UnrecognizedCCodeException {
    /* FOR SHIFTS:
     * We would not need to cast the second operand, but we do casting,
     * because Mathsat assumes 2 bitvectors of same length.
     *
     * This could be incorrect in cases of negative shifts and
     * signed/unsigned conversion, example: 5U<<(-1).
     * Instead of "undefined value", we return a possible wrong value.
     *
     * ISO-C 6.5.7 Bitwise shift operators
     * If the value of the right operand is negative or is greater than or equal
     * to the width of the promoted left operand, the behavior is undefined.
     */

    final CType returnType = exp.getExpressionType();
    final CType calculationType = exp.getCalculationType();

    final Formula f1 = processOperand(exp.getOperand1(), calculationType, returnType);
    final Formula f2 = processOperand(exp.getOperand2(), calculationType, returnType);

    return handleBinaryExpression(exp, f1, f2);
  }

  public final Formula handleBinaryExpression(final CBinaryExpression exp,
      final Formula f1, final Formula f2) throws UnrecognizedCCodeException {
    final BinaryOperator op = exp.getOperator();
    final CType returnType = exp.getExpressionType();
    final CType calculationType = exp.getCalculationType();

    // these operators expect numeric arguments
    final FormulaType<?> returnFormulaType = conv.getFormulaTypeFromCType(returnType);


    final boolean signed;
    if (calculationType instanceof CSimpleType) {
      signed = conv.machineModel.isSigned((CSimpleType)calculationType);
    } else {
      signed = false;
    }

    // to INT or bigger
    final CType t1 = exp.getOperand1().getExpressionType();
    final CType t2 = exp.getOperand2().getExpressionType();
    final CType promT1 = getPromotedCType(t1).getCanonicalType();
    final CType promT2 = getPromotedCType(t2).getCanonicalType();

    final Formula ret;

    switch (op) {
    case PLUS:
      if (!(promT1 instanceof CPointerType) && !(promT2 instanceof CPointerType)) { // Just an addition e.g. 6 + 7
        ret = mgr.makePlus(f1, f2);
      } else if (!(promT2 instanceof CPointerType)) {
        // operand1 is a pointer => we should multiply the second summand by the size of the pointer target
        ret =  mgr.makePlus(f1, mgr.makeMultiply(f2,
                                                             getPointerTargetSizeLiteral((CPointerType) promT1,
                                                             calculationType)));
      } else if (!(promT1 instanceof CPointerType)) {
        // operand2 is a pointer => we should multiply the first summand by the size of the pointer target
        ret =  mgr.makePlus(f2, mgr.makeMultiply(f1,
                                                             getPointerTargetSizeLiteral((CPointerType) promT2,
                                                             calculationType)));
      } else {
        throw new UnrecognizedCCodeException("Can't add pointers", edge, exp);
      }
      break;
    case MINUS:
      if (!(promT1 instanceof CPointerType) && !(promT2 instanceof CPointerType)) { // Just a subtraction e.g. 6 - 7
        ret =  mgr.makeMinus(f1, f2);
      } else if (!(promT2 instanceof CPointerType)) {
        // operand1 is a pointer => we should multiply the subtrahend by the size of the pointer target
        ret =  mgr.makeMinus(f1, mgr.makeMultiply(f2,
                                                              getPointerTargetSizeLiteral((CPointerType) promT1,
                                                                                            calculationType)));
      } else if (promT1 instanceof CPointerType) {
        // Pointer subtraction => (operand1 - operand2) / sizeof (*operand1)
        if (promT1.equals(promT2)) {
          ret = mgr.makeDivide(mgr.makeMinus(f1, f2),
                                     getPointerTargetSizeLiteral((CPointerType) promT1, calculationType),
                                     true);
        } else {
          throw new UnrecognizedCCodeException("Can't subtract pointers of different types", edge, exp);
        }
      } else {
        throw new UnrecognizedCCodeException("Can't subtract a pointer from a non-pointer", edge, exp);
      }
      break;
    case MULTIPLY:
      ret =  mgr.makeMultiply(f1, f2);
      break;
    case DIVIDE:
      ret = mgr.makeDivide(f1, f2, signed);
      break;
    case MODULO:
      ret = mgr.makeModulo(f1, f2, signed);

      addModuloConstraints(exp, f1, f2, signed, ret);

      break;
    case BINARY_AND:
      ret =  mgr.makeAnd(f1, f2);
      break;
    case BINARY_OR:
      ret =  mgr.makeOr(f1, f2);
      break;
    case BINARY_XOR:
      ret =  mgr.makeXor(f1, f2);
      break;
    case SHIFT_LEFT:

      // NOTE: The type of the result is that of the promoted left operand. (6.5.7 3)
      ret =  mgr.makeShiftLeft(f1, f2);
      break;
    case SHIFT_RIGHT:
      // NOTE: The type of the result is that of the promoted left operand. (6.5.7 3)
      ret =  mgr.makeShiftRight(f1, f2, signed);
      break;

    case GREATER_THAN:
    case GREATER_EQUAL:
    case LESS_THAN:
    case LESS_EQUAL:
    case EQUALS:
    case NOT_EQUALS: {
      BooleanFormula result;
      switch (op) {
        case GREATER_THAN:
          result= mgr.makeGreaterThan(f1, f2, signed);
          break;
        case GREATER_EQUAL:
          result= mgr.makeGreaterOrEqual(f1, f2, signed);
          break;
        case LESS_THAN:
          result= mgr.makeLessThan(f1, f2, signed);
          break;
        case LESS_EQUAL:
          result= mgr.makeLessOrEqual(f1, f2, signed);
          break;
        case EQUALS:
          result= handleEquals(exp, f1, f2);
          break;
        case NOT_EQUALS:
          result= conv.bfmgr.not(mgr.makeEqual(f1, f2));
          break;
        default:
          throw new AssertionError();
      }

      // Here we directly use the returnFormulaType instead of the calculcationType
      // to avoid a useless cast.
      // However, this means that we may not call makeCast() below
      // because it expects the input in the calculationType.
      // So we return here directly.
      return conv.ifTrueThenOneElseZero(returnFormulaType, result);
    }
    default:
      throw new UnrecognizedCCodeException("Unknown binary operator", edge, exp);

    }

    // The CalculationType could be different from returnType, so we cast the result.
    // If the types are equal, the cast returns the Formula unchanged.
    final Formula castedResult = conv.makeCast(calculationType, returnType, ret, constraints, edge);
    assert returnFormulaType.equals(mgr.getFormulaType(castedResult))
         : "Returntype and Formulatype do not match in visit(CBinaryExpression): " + exp;

    return castedResult;
  }

  private BooleanFormula handleEquals(CBinaryExpression exp, Formula f1, Formula f2)
      throws UnrecognizedCCodeException {
    assert exp.getOperator() == BinaryOperator.EQUALS;
    CExpression e1 = exp.getOperand1();
    CExpression e2 = exp.getOperand2();
    if (e2.equals(CIntegerLiteralExpression.ZERO)
        && e1 instanceof CBinaryExpression
        && ((CBinaryExpression) e1).getOperator() == BinaryOperator.BINARY_OR) {
      // This is code like "(a | b) == 0".
      // According to LDV, GCC sometimes produces this during weaving,
      // but for non-bitprecise analysis it can be handled in a better way as (a == 0) || (b == 0).
      // TODO Maybe refactor AutomatonASTComparator into something generic
      // and use this to match such cases.

      final CBinaryExpression or = (CBinaryExpression) e1;
      final Formula zero = f2;
      final Formula a =
          processOperand(or.getOperand1(), exp.getCalculationType(), exp.getExpressionType());
      final Formula b =
          processOperand(or.getOperand2(), exp.getCalculationType(), exp.getExpressionType());

      return conv.bfmgr.and(mgr.makeEqual(a, zero), mgr.makeEqual(b, zero));
    }
    return mgr.makeEqual(f1, f2);
  }

  /**
   * Some solvers (Mathsat, Princess) do not support MODULO and replace it with an UF.
   * Thus, we limit the result of the UF with additional constraints.
   */
  private void addModuloConstraints(final CBinaryExpression exp, final Formula f1, final Formula f2,
      final boolean signed, final Formula ret) {
    BooleanFormulaManagerView bfmgr = mgr.getBooleanFormulaManager();

    if (exp.getOperand2() instanceof CIntegerLiteralExpression) {
      long modulo = ((CIntegerLiteralExpression)exp.getOperand2()).asLong();
      BooleanFormula modularCongruence = mgr.makeModularCongruence(ret, f1, modulo);
      if (!bfmgr.isTrue(modularCongruence)) {
        constraints.addConstraint(modularCongruence);
      }
    }

    FormulaType<Formula> numberType = mgr.getFormulaType(f1);
    Formula zero = mgr.makeNumber(numberType, 0L);

    // Sign of the remainder is set by the sign of the
    // numerator, and it is bounded by the numerator.
    BooleanFormula signAndNumBound =
        bfmgr.ifThenElse(
            mgr.makeGreaterOrEqual(f1, zero, signed),
            mgr.makeRangeConstraint(ret, zero, f1, signed), // ret in [zero, f1] (both positive)
            mgr.makeRangeConstraint(ret, f1, zero, signed) // ret in [f1, zero] (both negative)
            );

    BooleanFormula denomBound = bfmgr.ifThenElse(
        mgr.makeGreaterOrEqual(f2, zero, signed),

        // Denominator is positive => remainder is strictly less than denominator.
        mgr.makeLessThan(ret, f2, signed),

        // Denominator is negative => remainder is strictly more.
        mgr.makeLessThan(f2, ret, signed)
    );

    constraints.addConstraint(signAndNumBound);
    constraints.addConstraint(denomBound);
  }

  @Override
  public Formula visit(CCastExpression cexp) throws UnrecognizedCCodeException {
    CExpression op = cexp.getOperand();
    op = conv.makeCastFromArrayToPointerIfNecessary(op, cexp.getExpressionType());

    Formula operand = toFormula(op);

    CType after = cexp.getExpressionType();
    CType before = op.getExpressionType();
    return conv.makeCast(before, after, operand, constraints, edge);
  }

  @Override
  public Formula visit(CIdExpression idExp) throws UnrecognizedCCodeException {

    if (idExp.getDeclaration() instanceof CEnumerator) {
      CEnumerator enumerator = (CEnumerator)idExp.getDeclaration();
      CType t = idExp.getExpressionType();
      if (enumerator.hasValue()) {
        return mgr.makeNumber(conv.getFormulaTypeFromCType(t), enumerator.getValue());
      } else {
        // We don't know the value here, but we know it is constant.
        return conv.makeConstant(enumerator.getName(), t);
      }
    }

    return conv.makeVariable(
        idExp.getDeclaration().getQualifiedName(),
        idExp.getExpressionType(),
        ssa);
  }

  @Override
  public Formula visit(CFieldReference fExp) throws UnrecognizedCCodeException {
    if (conv.options.handleFieldAccess()) {
      CExpression fieldOwner = getRealFieldOwner(fExp);
      Formula f = toFormula(fieldOwner);
      return conv.accessField(fExp, f);
    }

    CExpression fieldRef = fExp.getFieldOwner();
    if (fieldRef instanceof CIdExpression) {
      CSimpleDeclaration decl = ((CIdExpression) fieldRef).getDeclaration();
      if (decl instanceof CDeclaration && ((CDeclaration)decl).isGlobal()) {
        // this is the reference to a global field variable

        // we can omit the warning (no pointers involved),
        // and we don't need to scope the variable reference
        return conv.makeVariable(CtoFormulaConverter.exprToVarNameUnscoped(fExp), fExp.getExpressionType(), ssa);
      }
    }

    return super.visit(fExp);
  }


  @Override
  public Formula visit(CCharLiteralExpression cExp) throws UnrecognizedCCodeException {
    // we just take the byte value
    FormulaType<?> t = conv.getFormulaTypeFromCType(cExp.getExpressionType());
    return mgr.makeNumber(t, cExp.getCharacter());
  }

  @Override
  public Formula visit(CIntegerLiteralExpression iExp) throws UnrecognizedCCodeException {
    FormulaType<?> t = conv.getFormulaTypeFromCType(iExp.getExpressionType());
    return mgr.makeNumber(t, iExp.getValue());
  }

  @Override
  public Formula visit(CImaginaryLiteralExpression exp) throws UnrecognizedCCodeException {
    return toFormula(exp.getValue());
  }

  @Override
  public Formula visit(CFloatLiteralExpression fExp) throws UnrecognizedCCodeException {
    FormulaType<?> t = conv.getFormulaTypeFromCType(fExp.getExpressionType());
    return mgr.getFloatingPointFormulaManager().makeNumber(fExp.getValue(),
        (FloatingPointType)t);
  }

  @Override
  public Formula visit(CStringLiteralExpression lexp) throws UnrecognizedCCodeException {
    // we create a string constant representing the given
    // string literal
    return conv.makeStringLiteral(lexp.getValue());
  }

  @Override
  public Formula visit(CUnaryExpression exp) throws UnrecognizedCCodeException {
    CExpression operand = exp.getOperand();
    UnaryOperator op = exp.getOperator();
    switch (op) {
    case MINUS:
    case TILDE: {
      // Handle Integer Promotion
      CType t = operand.getExpressionType();
      CType promoted = getPromotedCType(t.getCanonicalType());
      Formula operandFormula = toFormula(operand);
      operandFormula = conv.makeCast(t, promoted, operandFormula, constraints, edge);
      Formula ret;
      if (op == UnaryOperator.MINUS) {
        ret = mgr.makeNegate(operandFormula);
      } else {
        assert op == UnaryOperator.TILDE
              : "This case should be impossible because of switch";
        ret = mgr.makeNot(operandFormula);
      }

      CType returnType = exp.getExpressionType();
      FormulaType<?> returnFormulaType = conv.getFormulaTypeFromCType(returnType);
      if (!returnFormulaType.equals(mgr.getFormulaType(ret))) {
        ret = conv.makeCast(t, returnType, ret, constraints, edge);
      }
      assert returnFormulaType.equals(mgr.getFormulaType(ret))
            : "Returntype and Formulatype do not match in visit(CUnaryExpression)";
      return ret;
    }

    case AMPER:
      return visitDefault(exp);

    case SIZEOF:
      CType lCType = exp.getOperand().getExpressionType();
      return handleSizeof(exp, lCType);

    default:
      throw new UnrecognizedCCodeException("Unknown unary operator", edge, exp);
    }
  }

  @Override
  public Formula visit(CTypeIdExpression tIdExp)
      throws UnrecognizedCCodeException {

    if (tIdExp.getOperator() == TypeIdOperator.SIZEOF) {
      CType lCType = tIdExp.getType();
      return handleSizeof(tIdExp, lCType);
    } else {
      return visitDefault(tIdExp);
    }
  }

  private Formula handleSizeof(CExpression pExp, CType pCType) {
    return mgr.makeNumber(
        conv
          .getFormulaTypeFromCType(pExp.getExpressionType()),
        conv.getSizeof(pCType));
  }

  @Override
  public Formula visit(CFunctionCallExpression e) throws UnrecognizedCCodeException {
    final CExpression functionNameExpression = e.getFunctionNameExpression();
    final CType returnType = e.getExpressionType();
    final List<CExpression> parameters = e.getParameterExpressions();

    // First let's handle special cases such as assumes, allocations, nondets, external models, etc.
    final String functionName;
    if (functionNameExpression instanceof CIdExpression) {
      functionName = ((CIdExpression)functionNameExpression).getName();
      if (conv.options.isNondetFunction(functionName)
          || conv.options.isMemoryAllocationFunction(functionName)
          || conv.options.isMemoryAllocationFunctionWithZeroing(functionName)) {
        // Function call like "random()".
        // Also "malloc()" etc. just return a random value, so handle them similarly.
        // Ignore parameters and just create a fresh variable for it.
        return makeNondet(functionName, returnType);

      } else if (conv.options.isExternModelFunction(functionName)) {
        ExternModelLoader loader = new ExternModelLoader(conv.typeHandler, conv.bfmgr, conv.fmgr);
        BooleanFormula result = loader.handleExternModelFunction(parameters, ssa);
        FormulaType<?> returnFormulaType = conv.getFormulaTypeFromCType(e.getExpressionType());
        return conv.ifTrueThenOneElseZero(returnFormulaType, result);

      } else if (CtoFormulaConverter.UNSUPPORTED_FUNCTIONS.containsKey(functionName)) {
        throw new UnsupportedCCodeException(CtoFormulaConverter.UNSUPPORTED_FUNCTIONS.get(functionName), edge, e);

      } else if (BuiltinFloatFunctions.matchesInfinity(functionName)) {

        if (parameters.size() == 0) {
          CType resultType = getTypeOfBuiltinFloatFunction(functionName);

          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(resultType);
          if (formulaType.isFloatingPointType()) {
            return mgr.getFloatingPointFormulaManager().makePlusInfinity(
                (FormulaType.FloatingPointType)formulaType);
          }
        }

      } else if (BuiltinFloatFunctions.matchesHugeVal(functionName)) {

        if (parameters.size() == 0) {
          CType resultType = getTypeOfBuiltinFloatFunction(functionName);

          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(resultType);
          if (formulaType.isFloatingPointType()) {
            return mgr.getFloatingPointFormulaManager().makePlusInfinity(
                (FormulaType.FloatingPointType)formulaType);
          }
        }

      } else if (BuiltinFloatFunctions.matchesNaN(functionName)) {

        if (parameters.size() == 1) {
          CType resultType = getTypeOfBuiltinFloatFunction(functionName);

          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(resultType);
          if (formulaType.isFloatingPointType()) {
            return mgr.getFloatingPointFormulaManager().makeNaN(
                (FormulaType.FloatingPointType)formulaType);
          }
        }

      } else if (BuiltinFloatFunctions.matchesAbsolute(functionName)) {

        if (parameters.size() == 1) {
          CType paramType = getTypeOfBuiltinFloatFunction(functionName);
          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(paramType);
          if (formulaType.isFloatingPointType()) {
            Formula param = processOperand(parameters.get(0), paramType, paramType);
            FloatingPointFormula zero = mgr.getFloatingPointFormulaManager().makeNumber(0.0, (FormulaType.FloatingPointType)formulaType);
            BooleanFormula isNegative = mgr.makeLessThan(param, zero, true);
            return mgr.getBooleanFormulaManager().ifThenElse(isNegative,
                mgr.makeNegate(param), param);
          }
        }

      } else if (BuiltinFloatFunctions.matchesFinite(functionName)) {

        if (parameters.size() == 1) {
          CType paramType = getTypeOfBuiltinFloatFunction(functionName);
          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(paramType);
          if (formulaType.isFloatingPointType()) {
            FloatingPointFormulaManagerView fpfmgr = mgr.getFloatingPointFormulaManager();
            FloatingPointFormula param = (FloatingPointFormula)processOperand(parameters.get(0), paramType, paramType);

            FormulaType<?> resultType = conv.getFormulaTypeFromCType(CNumericTypes.INT);
            return conv.bfmgr.ifThenElse(
                conv.bfmgr.or(fpfmgr.isInfinity(param), fpfmgr.isNaN(param)),
                mgr.makeNumber(resultType, 0),
                mgr.makeNumber(resultType, 1));
          }
        }

      } else if (BuiltinFloatFunctions.matchesIsNaN(functionName)) {

        if (parameters.size() == 1) {
          CType paramType = getTypeOfBuiltinFloatFunction(functionName);
          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(paramType);
          if (formulaType.isFloatingPointType()) {
            FloatingPointFormulaManagerView fpfmgr = mgr.getFloatingPointFormulaManager();
            FloatingPointFormula param = (FloatingPointFormula)processOperand(parameters.get(0), paramType, paramType);

            FormulaType<?> resultType = conv.getFormulaTypeFromCType(CNumericTypes.INT);
            return conv.bfmgr.ifThenElse(
                fpfmgr.isNaN(param),
                mgr.makeNumber(resultType, 1),
                mgr.makeNumber(resultType, 0));
          }
        }

      } else if (BuiltinFloatFunctions.matchesIsInfinity(functionName)) {

        if (parameters.size() == 1) {
          CType paramType = getTypeOfBuiltinFloatFunction(functionName);
          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(paramType);
          if (formulaType.isFloatingPointType()) {
            FloatingPointFormulaManagerView fpfmgr = mgr.getFloatingPointFormulaManager();
            FloatingPointFormula param = (FloatingPointFormula)processOperand(parameters.get(0), paramType, paramType);
            FloatingPointFormula fp_zero = fpfmgr.makeNumber(0, (FormulaType.FloatingPointType)formulaType);

            FormulaType<?> resultType = conv.getFormulaTypeFromCType(CNumericTypes.INT);
            Formula zero = mgr.makeNumber(resultType, 0);
            Formula one = mgr.makeNumber(resultType, 1);
            Formula minus_one = mgr.makeNumber(resultType, -1);

            return conv.bfmgr.ifThenElse(fpfmgr.isInfinity(param),
                conv.bfmgr.ifThenElse(fpfmgr.lessThan(param, fp_zero), minus_one, one),
                zero);
          }
        }
      } else if (BuiltinFloatFunctions.matchesFloatClassify(functionName)) {

        if (parameters.size() == 1) {
          CType paramType = getTypeOfBuiltinFloatFunction(functionName);
          FormulaType<?> formulaType = conv.getFormulaTypeFromCType(paramType);
          if (formulaType.isFloatingPointType()) {
            FloatingPointFormulaManagerView fpfmgr = mgr.getFloatingPointFormulaManager();
            FloatingPointFormula param = (FloatingPointFormula)processOperand(parameters.get(0), paramType, paramType);

            FormulaType<?> resultType = conv.getFormulaTypeFromCType(CNumericTypes.INT);
            Formula zero = mgr.makeNumber(resultType, 0);
            Formula one = mgr.makeNumber(resultType, 1);
            Formula two = mgr.makeNumber(resultType, 2);
            Formula three = mgr.makeNumber(resultType, 3);
            Formula four = mgr.makeNumber(resultType, 4);

            return
              conv.bfmgr.ifThenElse(fpfmgr.isNaN(param), zero,
                  conv.bfmgr.ifThenElse(fpfmgr.isInfinity(param), one,
                      conv.bfmgr.ifThenElse(fpfmgr.isZero(param), two,
                          conv.bfmgr.ifThenElse(fpfmgr.isSubnormal(param), three,
                              four))));
          }
        }

      } else if (!CtoFormulaConverter.PURE_EXTERNAL_FUNCTIONS.contains(functionName)) {
        if (parameters.isEmpty()) {
          // function of arity 0
          conv.logger.logOnce(Level.INFO, "Assuming external function", functionName, "to be a constant function.");
        } else {
          conv.logger.logOnce(Level.INFO, "Assuming external function", functionName, "to be a pure function.");
        }
      }
    } else {
      conv.logfOnce(Level.WARNING, edge, "Ignoring function call through function pointer %s", functionNameExpression);
      String escapedName = CtoFormulaConverter.exprToVarName(functionNameExpression, function);
      functionName = ("<func>{" + escapedName + "}").intern();
    }

    // Now let's handle "normal" functions assumed to be pure
    if (parameters.isEmpty()) {
      // This is a function of arity 0 and we assume its constant.
      return conv.makeConstant(functionName, returnType);

    } else {
      final CFunctionDeclaration functionDeclaration = e.getDeclaration();
      if (functionDeclaration == null) {
        if (functionNameExpression instanceof CIdExpression) {
          // This happens only if there are undeclared functions.
          conv.logger.logfOnce(Level.WARNING, "Cannot get declaration of function %s, ignoring calls to it.",
                               functionNameExpression);
        }
        return makeNondet(functionName, returnType);
      }

      if (functionDeclaration.getType().takesVarArgs()) {
        // Create a fresh variable instead of an UF for varargs functions.
        // This is sound but slightly more imprecise (we loose the UF axioms).
        return makeNondet(functionName, returnType);
      }

      final List<CType> formalParameterTypes = functionDeclaration.getType().getParameters();
      if (formalParameterTypes.size() != parameters.size()) {
        throw new UnrecognizedCCodeException("Function " + functionDeclaration
            + " received " + parameters.size() + " parameters"
            + " instead of the expected " + formalParameterTypes.size(),
            edge, e);
      }

      final List<Formula> arguments = new ArrayList<>(parameters.size());
      final Iterator<CType> formalParameterTypesIt = formalParameterTypes.iterator();
      final Iterator<CExpression> parametersIt = parameters.iterator();
      while (formalParameterTypesIt.hasNext() && parametersIt.hasNext()) {
        final CType formalParameterType = formalParameterTypesIt.next();
        CExpression parameter = parametersIt.next();
        parameter = conv.makeCastFromArrayToPointerIfNecessary(parameter, formalParameterType);

        Formula argument = toFormula(parameter);
        arguments.add(conv.makeCast(parameter.getExpressionType(), formalParameterType, argument, constraints, edge));
      }
      assert !formalParameterTypesIt.hasNext() && !parametersIt.hasNext();

      final CType realReturnType = conv.getReturnType(e, edge);
      final FormulaType<?> resultFormulaType = conv.getFormulaTypeFromCType(realReturnType);
      return conv.ffmgr.declareAndCallUF(functionName, resultFormulaType, arguments);
    }
  }

  protected Formula makeNondet(final String varName, final CType type) {
    Formula newVariable = conv.makeFreshVariable(varName, type, ssa);
    if (conv.options.addRangeConstraintsForNondet()) {
      conv.addRangeConstraint(newVariable, type, constraints);
    }
    return newVariable;
  }
}
