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
/**
 * Extensions of the pure {@link org.sosy_lab.java_smt.api.FormulaManager}
 * interface and its related interfaces
 * that make it easier to use by client code.
 * This package can be used regardless of which SMT solver is the backend.
 *
 * The most important feature of this package is to replace an SMT theory
 * with another one, simulating the semantics of the replaced theory
 * with other theories.
 * This can be used to allow working with {@link org.sosy_lab.java_smt.api.BitvectorFormula}
 * even if the solver does not support the theory of bitvectors.
 * Bitvectors will then be approximated with rationals or integers.
 */
@javax.annotation.ParametersAreNonnullByDefault
@org.sosy_lab.common.annotations.FieldsAreNonnullByDefault
@org.sosy_lab.common.annotations.ReturnValuesAreNonnullByDefault
package org.sosy_lab.cpachecker.util.predicates.smt;
