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

import org.sosy_lab.cpachecker.cfa.ast.AbstractReturnStatement;
import org.sosy_lab.cpachecker.cfa.ast.FileLocation;

import java.util.Optional;

/**
 *  This class represents the return statement AST node type.
 *
 *  ReturnStatement:
 *   return [ Expression ] ;
 *
 */
public class JReturnStatement extends AbstractReturnStatement implements JAstNode {

  // TODO refactor to be either abstract or final

  public JReturnStatement(FileLocation pFileLocation, Optional<JExpression> pExpression) {
    // TODO We absolutely need a correct assignment here that assigns pExpression to a special variable with the return type of the function.
    super(pFileLocation, pExpression, Optional.empty());

  }

  @SuppressWarnings("unchecked") // safe because Optional is covariant
  @Override
  public Optional<JExpression> getReturnValue() {
    return (Optional<JExpression>) super.getReturnValue();
  }

  @Override
  public <R, X extends Exception> R accept(JAstNodeVisitor<R, X> v) throws X {
    return v.visit(this);
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

    if (!(obj instanceof JReturnStatement)) {
      return false;
    }

    return super.equals(obj);
  }
}
