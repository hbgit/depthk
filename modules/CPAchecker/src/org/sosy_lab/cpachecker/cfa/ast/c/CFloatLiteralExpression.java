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
package org.sosy_lab.cpachecker.cfa.ast.c;

import java.math.BigDecimal;
import org.sosy_lab.cpachecker.cfa.ast.AFloatLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.types.c.CBasicType;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.cfa.types.c.CType;

public final class CFloatLiteralExpression extends AFloatLiteralExpression implements CLiteralExpression {

  private static final BigDecimal APPROX_INFINITY = BigDecimal.valueOf(Double.MAX_VALUE).add(BigDecimal.valueOf(Double.MAX_VALUE));

  public CFloatLiteralExpression(FileLocation pFileLocation,
                                    CType pType,
                                    BigDecimal pValue) {
    super(pFileLocation, pType, adjustPrecision(pValue, pType));
  }

  private static BigDecimal adjustPrecision(BigDecimal pValue, CType pType) {
    BigDecimal value = pValue;
    if (pType instanceof CSimpleType) {
      CBasicType basicType = ((CSimpleType) pType).getType();
      switch (basicType) {
        case FLOAT:
          float fValue = pValue.floatValue();
          if (Float.isNaN(fValue)) {
            return value;
          }
          if (Float.isInfinite(fValue)) {
            if (fValue < 0) {
              return APPROX_INFINITY.negate();
            }
            return APPROX_INFINITY;
          }
          value = BigDecimal.valueOf(fValue);
          break;
        case DOUBLE:
          double dValue = pValue.doubleValue();
          if (Double.isNaN(dValue)) {
            return value;
          }
          if (Double.isInfinite(dValue)) {
            if (dValue < 0) {
              return APPROX_INFINITY.negate();
            }
            return APPROX_INFINITY;
          }
          value = BigDecimal.valueOf(dValue);
          break;
        default:
          break;
      }
    }
    return value;
  }

  @Override
  public CType getExpressionType() {
    return (CType) super.getExpressionType();
  }

  @Override
  public <R, X extends Exception> R accept(CExpressionVisitor<R, X> v) throws X {
    return v.visit(this);
  }

  @Override
  public <R, X extends Exception> R accept(CRightHandSideVisitor<R, X> v) throws X {
    return v.visit(this);
  }

  @Override
  public <R, X extends Exception> R accept(CAstNodeVisitor<R, X> pV) throws X {
    return pV.visit(this);
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = 7;
    return prime * result + super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof CFloatLiteralExpression)) {
      return false;
    }

    return super.equals(obj);
  }
}
