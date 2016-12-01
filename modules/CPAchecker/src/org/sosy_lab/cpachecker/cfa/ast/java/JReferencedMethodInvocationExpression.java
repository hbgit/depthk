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
package org.sosy_lab.cpachecker.cfa.ast.java;

import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.types.java.JType;

import java.util.List;
import java.util.Objects;

/**
 *
 * This class represents the qualified method invocation expression AST node type.
 *
 *  Qualified MethodInvocation:
 *  <pre>
 *     Expression .
 *        [ < Type { , Type } > ]
 *        Identifier ( [ Expression { , Expression } ] )
 *  </pre>
 *  Note that the qualification only consist of variables.
 *  In the cfa, all method names are transformed to have unique names.
 *
 *
 */
public final class JReferencedMethodInvocationExpression extends JMethodInvocationExpression {

  private final JIdExpression qualifier;

  public JReferencedMethodInvocationExpression(FileLocation pFileLocation, JType pType, JExpression pFunctionName,
      List<? extends JExpression> pParameters, JMethodDeclaration pDeclaration, JIdExpression pQualifier) {
    super(pFileLocation, pType, pFunctionName, pParameters, pDeclaration);
      qualifier = pQualifier;
  }

  public JIdExpression getReferencedVariable() {
    return qualifier;
  }

  @Override
  public String toASTString() {
    return qualifier.toASTString() + "_" + super.toASTString();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 7;
    result = prime * result + Objects.hashCode(qualifier);
    result = prime * result + super.hashCode();
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof JReferencedMethodInvocationExpression)
        || super.equals(obj)) {
      return false;
    }

    JReferencedMethodInvocationExpression other = (JReferencedMethodInvocationExpression) obj;

    return Objects.equals(other.qualifier, qualifier);
  }

}
