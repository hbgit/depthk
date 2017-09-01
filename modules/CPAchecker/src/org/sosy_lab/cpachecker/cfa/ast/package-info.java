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
 * The classes that are used to represent single program statements, declarations,
 * and expressions in form of an abstract syntax tree (AST).
 * Sub-packages contain language-specific sub-classes
 * for representation of features of specific languages.
 *
 * The classes in this package have an "A" as prefix to show that they are
 * language-independent, in contrast to the language-specific classes
 * with prefixes like "C" and "J".
 * All classes in this package named "Abstract*" are only relevant
 * for implementing the language-specific sub-classes
 * and should not be used by other code.
 */
package org.sosy_lab.cpachecker.cfa.ast;