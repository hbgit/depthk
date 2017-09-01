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
package org.sosy_lab.cpachecker.cpa.value.type;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import org.sosy_lab.common.rationals.Rational;
import org.sosy_lab.cpachecker.cfa.types.c.CBasicType;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.cfa.types.c.CType;

/**
 * Stores a numeric value that can be tracked by the ValueAnalysisCPA.
 */
public class NumericValue implements Value, Serializable {

  private static final long serialVersionUID = -3829943575180448170L;

  private Number number;

  /**
   * Creates a new <code>NumericValue</code>.
   * @param pNumber the value of the number
   */
  public NumericValue(Number pNumber) {
    number = pNumber;
  }

  /**
   * Returns the number stored in the container.
   *
   * @return the number stored in the container
   */
  public Number getNumber() {
    return number;
  }

  /**
   * Returns the integer stored in the container as long. Before calling this function,
   * it must be ensured using `getType()` that this container contains an integer.
   */
  public long longValue() {
    return number.longValue();
  }

  /**
   * Returns the floating point stored in the container as float.
   */
  public float floatValue() {
    return number.floatValue();
  }

  /**
   * Returns the floating point stored in the container as double.
   */
  public double doubleValue() {
    return number.doubleValue();
  }

  /**
   * Returns a BigDecimal value representing the stored number.
   */
  public BigDecimal bigDecimalValue() {
    return new BigDecimal(number.toString());
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NumericValue [number=" + number + "]";
  }

  /**
   * Returns whether this object and a given object are equal.
   * Two <code>NumericValue</code> objects are equal if and only if their
   * stored values are equal.
   *
   * @param other the <code>Object</code> to compare to this object
   * @return <code>true</code> if the given object equals this object,
   *         <code>false</code> otherwise
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof NumericValue) {
      return this.getNumber().equals(((NumericValue) other).getNumber());
    } else {
      return false;
    }
  }

  /**
   * Always returns <code>true</code>.
   *
   * @return always <code>true</code>
   */
  @Override
  public boolean isNumericValue() {
    return true;
  }

  /**
   * Returns a <code>NumericValue</code> object that holds the negation of
   * this object's value.
   *
   * @return the negation of this objects value
   */
  public NumericValue negate() {
    // TODO explicitfloat: handle the remaining different implementations of Number properly
    final Number number = getNumber();

    // check if number is infinite or NaN
    if (number instanceof Float) {
      if (number.equals(Float.POSITIVE_INFINITY)) {
        return new NumericValue(Float.NEGATIVE_INFINITY);

      } else if (number.equals(Float.NEGATIVE_INFINITY)) {
        return new NumericValue(Float.POSITIVE_INFINITY);

      } else if (number.equals(Float.NaN)) {
        return new NumericValue(NegativeNaN.VALUE);
      }
    } else if (number instanceof Double) {
      if (number.equals(Double.POSITIVE_INFINITY)) {
        return new NumericValue(Double.NEGATIVE_INFINITY);

      } else if (number.equals(Double.NEGATIVE_INFINITY)) {
        return new NumericValue(Double.POSITIVE_INFINITY);

      } else if (number.equals(Double.NaN)) {
        return new NumericValue(NegativeNaN.VALUE);
      }
    } else if (number instanceof Rational) {
      return new NumericValue(((Rational) number).negate());
    } else if (NegativeNaN.VALUE.equals(number)) {
      return new NumericValue(Double.NaN);
    }

    if (number instanceof BigDecimal) {
      BigDecimal bd = (BigDecimal) number;
      if (bd.signum() == 0) {
        return new NumericValue(-bd.doubleValue());
      }
    }

    // if the stored number is a 'casual' number, just negate it
    return new NumericValue(this.bigDecimalValue().negate());
  }

  @Override
  public NumericValue asNumericValue() {
    return this;
  }

  @Override
  public Long asLong(CType type) {
    checkNotNull(type);
    type = type.getCanonicalType();
    if (!(type instanceof CSimpleType)) {
      return null;
    }

    if (((CSimpleType)type).getType() == CBasicType.INT) {
      return longValue();
    } else {
      return null;
    }
  }

  /**
   * Always returns <code>false</code> as each <code>NumericValue</code> holds
   * one specific value.
   *
   * @return always <code>false</code>
   */
  @Override
  public boolean isUnknown() {
    return false;
  }

  /**
   * Always returns <code>true</code> as each <code>NumericValue</code> holds
   * one specific value.
   *
   * @return always <code>true</code>
   */
  @Override
  public boolean isExplicitlyKnown() {
    return true;
  }

  @Override
  public int hashCode() {
    // fulfills contract that if this.equals(other),
    // then this.hashCode() == other.hashCode()
    return number.hashCode();
  }

  public static class NegativeNaN extends Number {

    private static final long serialVersionUID = 1L;

    public static final Number VALUE = new NegativeNaN();

    private NegativeNaN() {
    }

    @Override
    public double doubleValue() {
      return Double.NaN;
    }

    @Override
    public float floatValue() {
      return Float.NaN;
    }

    @Override
    public int intValue() {
      return (int) Double.NaN;
    }

    @Override
    public long longValue() {
      return (long) Double.NaN;
    }

    @Override
    public String toString() {
      return "-NaN";
    }

    @Override
    public boolean equals(Object pObj) {
      return pObj == this || pObj instanceof NegativeNaN;
    }

    @Override
    public int hashCode() {
      return -1;
    }

  }

}
