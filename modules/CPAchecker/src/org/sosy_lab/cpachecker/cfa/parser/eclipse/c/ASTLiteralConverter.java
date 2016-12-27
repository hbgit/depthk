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
package org.sosy_lab.cpachecker.cfa.parser.eclipse.c;

import static java.lang.Character.isDigit;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.ast.c.CCharLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CFloatLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CImaginaryLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CIntegerLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CStringLiteralExpression;
import org.sosy_lab.cpachecker.cfa.types.MachineModel;
import org.sosy_lab.cpachecker.cfa.types.c.CNumericTypes;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.cfa.types.c.CType;

/** This Class contains functions,
/** This Class contains functions,
 * that convert literals (chars, numbers) from C-source into CPAchecker-format. */
class ASTLiteralConverter {

  private final MachineModel machine;

  private final ParseContext parseContext;

  ASTLiteralConverter(MachineModel pMachineModel, ParseContext pParseContext) {
    machine = pMachineModel;
    parseContext = pParseContext;
  }

  private void check(boolean assertion, String msg, IASTNode astNode) throws CFAGenerationRuntimeException {
    if (!assertion) {
      throw parseContext.parseError(msg, astNode);
    }
  }

  /** This function converts literals like chars or numbers. */
  CLiteralExpression convert(final IASTLiteralExpression e, final CType type, final FileLocation fileLoc) {
    if (!(type instanceof CSimpleType)
        && (e.getKind() != IASTLiteralExpression.lk_string_literal)) {
      throw parseContext.parseError("Invalid type " + type + " for literal expression", e);
    }

    String valueStr = String.valueOf(e.getValue());
    if (valueStr.endsWith("i") || valueStr.endsWith("j")) {
      return handleImaginaryNumber(fileLoc, (CSimpleType)type, e, valueStr);
    }

    switch (e.getKind()) {
    case IASTLiteralExpression.lk_char_constant:
      return new CCharLiteralExpression(fileLoc, type, parseCharacterLiteral(valueStr, e));

    case IASTLiteralExpression.lk_integer_constant:
      Suffix suffix = determineSuffix(valueStr, e);
      BigInteger integerValue = parseIntegerLiteral(suffix, valueStr.substring(0, valueStr.length() - suffix.getLength()), e);
      CSimpleType actualType = suffix.getTypeForValue(integerValue, machine);
      return new CIntegerLiteralExpression(fileLoc, actualType, integerValue);

    case IASTLiteralExpression.lk_float_constant:
      BigDecimal value;
      try {

        //in Java float and double can be distinguished by the suffixes "f" (Float) and "d" (Double)
        // in C the suffixes are "f" / "F" (Float) and "l" / "L" (Long Double)
        if (valueStr.endsWith("L") || valueStr.endsWith("l")) {
          valueStr = valueStr.substring(0, valueStr.length() - 1) + "d";
        }

        value = new BigDecimal(valueStr);
      } catch (NumberFormatException nfe1) {
        try {
          // this might be a hex floating point literal
          // BigDecimal doesn't support this, but Double does
          // TODO handle hex floating point literals that are too large for Double
          value = BigDecimal.valueOf(Double.parseDouble(valueStr));
        } catch (NumberFormatException nfe2) {
          throw parseContext.parseError("illegal floating point literal", e);
        }
      }

      return new CFloatLiteralExpression(fileLoc, type, value);

    case IASTLiteralExpression.lk_string_literal:
      return new CStringLiteralExpression(fileLoc, type, valueStr);

    default:
      throw parseContext.parseError("Unknown literal", e);
    }
  }

  private CImaginaryLiteralExpression handleImaginaryNumber(FileLocation fileLoc, CSimpleType type, IASTLiteralExpression exp, String valueStr) {
    valueStr = valueStr.substring(0, valueStr.length()-1);
    type = new CSimpleType(type.isConst(), type.isVolatile(), type.getType(), type.isLong(),
        type.isShort(), type.isSigned(), type.isUnsigned(), type.isComplex(), true, type.isLongLong());
    switch (exp.getKind()) {
    case IASTLiteralExpression.lk_char_constant:
      return new CImaginaryLiteralExpression(fileLoc,
                                             type,
                                             new CCharLiteralExpression(fileLoc, type, parseCharacterLiteral(valueStr, exp))) ;


    case IASTLiteralExpression.lk_integer_constant:
      Suffix suffix = determineSuffix(valueStr, exp);
      BigInteger integerValue = parseIntegerLiteral(suffix, valueStr.substring(0, valueStr.length() - suffix.getLength()), exp);
      CSimpleType actualType = suffix.getTypeForValue(integerValue, machine);
      return new CImaginaryLiteralExpression(fileLoc,
                                             actualType,
                                             new CIntegerLiteralExpression(fileLoc, actualType, integerValue)) ;

    case IASTLiteralExpression.lk_float_constant:
      BigDecimal val;
      try {

        //in Java float and double can be distinguished by the suffixes "f" (Float) and "d" (Double)
        // in C the suffixes are "f" / "F" (Float) and "l" / "L" (Long Double)
        if (valueStr.endsWith("L") || valueStr.endsWith("l")) {
          valueStr = valueStr.substring(0, valueStr.length()-1) + "d";
        }

        val = new BigDecimal(valueStr);
      } catch (NumberFormatException nfe1) {
        try {
          // this might be a hex floating point literal
          // BigDecimal doesn't support this, but Double does
          // TODO handle hex floating point literals that are too large for Double
          val = BigDecimal.valueOf(Double.parseDouble(valueStr));
        } catch (NumberFormatException nfe2) {
          throw parseContext.parseError("illegal floating point literal", exp);
        }
      }

      return new CImaginaryLiteralExpression(fileLoc,
                                             type,
                                             new CFloatLiteralExpression(fileLoc, type, val));

    default:
      throw parseContext.parseError("Unknown imaginary literal", exp);
    }
  }

  char parseCharacterLiteral(String s, final IASTNode e) {
    check(s.length() >= 3, "invalid character literal (too short)", e);
    check(s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\'',
        "character literal without quotation marks", e);
    s = s.substring(1, s.length() - 1); // remove the surrounding quotation marks ''

    final char result;
    if (s.length() == 1) {
      result = s.charAt(0);
      check(result != '\\', "invalid quoting sequence", e);

    } else {
      check(s.charAt(0) == '\\', "character literal too long", e);
      // quoted character literal
      s = s.substring(1); // remove leading backslash \
      check(s.length() >= 1, "invalid quoting sequence", e);

      final char c = s.charAt(0);
      if (c == 'x' || c == 'X') {
        // something like '\xFF'
        s = s.substring(1); // remove leading x
        check(s.length() > 0 && s.length() <= 3, "character literal with illegal hex number", e);
        try {
          result = (char) Integer.parseInt(s, 16);
          check(result <= 0xFF, "hex escape sequence out of range", e);
        } catch (NumberFormatException exception) {
          throw parseContext.parseError("character literal with illegal hex number", e);
        }

      } else if (isDigit(c)) {
        // something like '\000'
        check(s.length() <= 3, "character literal with illegal octal number", e);
        try {
          result = (char) Integer.parseInt(s, 8);
          check(result <= 0xFF, "octal escape sequence out of range", e);
        } catch (NumberFormatException exception) {
          throw parseContext.parseError("character literal with illegal octal number", e);
        }

      } else {
        // something like '\n'
        check(s.length() == 1, "character literal too long", e);
        switch (c) {
        case 'a':
          result = 7;
          break;
        case 'b':
          result = '\b';
          break;
        case 'f':
          result = '\f';
          break;
        case 'n':
          result = '\n';
          break;
        case 'r':
          result = '\r';
          break;
        case 't':
          result = '\t';
          break;
        case 'v':
          result = 11;
          break;
        case '"':
          result = '\"';
          break;
        case '\'':
          result = '\'';
          break;
        case '\\':
          result = '\\';
          break;
        default:
          throw parseContext.parseError("unknown character literal", e);
        }
      }
    }
    return result;
  }

  BigInteger parseIntegerLiteral(Suffix pSuffix, String s, final IASTNode e) {
    BigInteger result;
    try {
      if (s.startsWith("0x") || s.startsWith("0X")) {
        // this should be in hex format, remove "0x" from the string
        s = s.substring(2);
        result = new BigInteger(s, 16);

      } else if (s.startsWith("0")) {
        result = new BigInteger(s, 8);

      } else {
        result = new BigInteger(s, 10);
      }
    } catch (NumberFormatException exception) {
      throw parseContext.parseError("invalid number", e);
    }
    check(result.compareTo(BigInteger.ZERO) >= 0, "invalid number", e);

    int bits = pSuffix.getNumberOfBits(machine, result);

    // clear the bits that don't fit in the type
    // a BigInteger with the lowest "bits" bits set to one (e. 2^32-1 or 2^64-1)
    BigInteger mask = BigInteger.ZERO.setBit(bits).subtract(BigInteger.ONE);
    result = result.and(mask);
    assert result.bitLength() <= bits;

    // compute twos complement if necessary
    if (pSuffix.isSigned() && result.testBit(bits - 1)) {
      // highest bit is set
      result = result.clearBit(bits - 1);

      // a BigInteger for -2^(bits-1) (e.g. -2^-31 or -2^-63)
      final BigInteger minValue = BigInteger.ZERO.setBit(bits - 1).negate();

      result = minValue.add(result);
    }

    return result;
  }

  Suffix determineSuffix(String pIntegerLiteral, IASTNode pExpression) {
    Suffix suffix = Suffix.NONE;
    int last = pIntegerLiteral.length() - 1;

    if (pIntegerLiteral.charAt(last) == 'U' || pIntegerLiteral.charAt(last) == 'u') {
      last--;
      suffix = Suffix.U;
    }
    if (pIntegerLiteral.charAt(last) == 'L' || pIntegerLiteral.charAt(last) == 'l') {
      last--;
      // one 'L' is a long int
      suffix = suffix == Suffix.NONE ? Suffix.L : Suffix.UL;
    }
    if (pIntegerLiteral.charAt(last) == 'L' || pIntegerLiteral.charAt(last) == 'l') {
      last--;
      // two 'L' are a long long int
      suffix = suffix == Suffix.L ? Suffix.LL : Suffix.ULL;
    }
    if (pIntegerLiteral.charAt(last) == 'U' || pIntegerLiteral.charAt(last) == 'u') {
      if (!suffix.isSigned()) {
        throw parseContext.parseError(
            "invalid duplicate modifier U in integer literal", pExpression);
      }
      last--;
      suffix = suffix == Suffix.L ? Suffix.UL : Suffix.ULL;
    }
    return suffix;
  }

  static enum Suffix {

    NONE {

      @Override
      public boolean isSigned() {
        return true;
      }

      @Override
      public CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel) {
        return getType(pValue,
            pMachineModel,
            CNumericTypes.INT,
            CNumericTypes.LONG_INT,
            CNumericTypes.LONG_LONG_INT);
      }

      @Override
      public int getLength() {
        return 0;
      }

    },

    U {

      @Override
      public boolean isSigned() {
        return false;
      }

      @Override
      public CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel) {
        return getType(pValue,
            pMachineModel,
            CNumericTypes.UNSIGNED_INT,
            CNumericTypes.UNSIGNED_LONG_INT,
            CNumericTypes.UNSIGNED_LONG_LONG_INT);
      }

      @Override
      public int getLength() {
        return 1;
      }

    },

    L {

      @Override
      public boolean isSigned() {
        return true;
      }

      @Override
      public CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel) {
        return getType(pValue,
            pMachineModel,
            CNumericTypes.LONG_INT,
            CNumericTypes.LONG_LONG_INT);
      }

      @Override
      public int getLength() {
        return 1;
      }

    },

    UL {

      @Override
      public boolean isSigned() {
        return false;
      }

      @Override
      public CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel) {
        return getType(pValue,
            pMachineModel,
            CNumericTypes.UNSIGNED_LONG_INT,
            CNumericTypes.UNSIGNED_LONG_LONG_INT);
      }

      @Override
      public int getLength() {
        return 2;
      }

    },

    LL {

      @Override
      public boolean isSigned() {
        return true;
      }

      @Override
      public CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel) {
        return getType(pValue,
            pMachineModel,
            CNumericTypes.LONG_LONG_INT);
      }

      @Override
      public int getLength() {
        return 2;
      }

    },

    ULL {

      @Override
      public boolean isSigned() {
        return false;
      }

      @Override
      public CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel) {
        return getType(pValue,
            pMachineModel,
            CNumericTypes.UNSIGNED_LONG_LONG_INT);
      }

      @Override
      public int getLength() {
        return 3;
      }

    };

    public abstract boolean isSigned();

    public abstract int getLength();

    /**
     * Implement section 6.4.4.1 "Integer constants" of the C standard:
     *
     * For each kind of literal suffix (including no suffix),
     * there is an ordered list of potential types,
     * and the first matching type of the list should be selected.
     *
     * @param pMachineModel the machine model.
     * @param pValue the literal value.
     *
     * @return the size that will be used to represent the value.
     */
    public int getNumberOfBits(MachineModel pMachineModel, BigInteger pValue) {
      CSimpleType type = getTypeForValue(pValue, pMachineModel);
      if (type != null) {
        return pMachineModel.getSizeof(type) * pMachineModel.getSizeofCharInBits();
      }
      return pValue.bitLength();
    }

    public abstract CSimpleType getTypeForValue(BigInteger pValue, MachineModel pMachineModel);

    protected CSimpleType getType(BigInteger pValue, MachineModel pMachineModel, CSimpleType... pCandidates) {
      int numberOfBits = pValue.bitLength();
      CSimpleType bestCandidate = null;
      for (CSimpleType candidate : pCandidates) {
        int candidateBitSize = pMachineModel.getSizeof(candidate) * pMachineModel.getSizeofCharInBits();
        int actualCandidateBitSize = isSigned() ? candidateBitSize - 1 : candidateBitSize;
        if (actualCandidateBitSize >= numberOfBits) {
          return candidate;
        }
        bestCandidate = candidate;
      }
      return bestCandidate;
    }

  }
}
