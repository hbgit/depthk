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
package org.sosy_lab.cpachecker.util.predicates.pathformula;

import static org.sosy_lab.cpachecker.util.statistics.StatisticsUtils.toPercent;

import org.sosy_lab.common.time.Timer;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CIdExpression;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cfa.types.c.CType;
import org.sosy_lab.cpachecker.cpa.arg.ARGState;
import org.sosy_lab.cpachecker.exceptions.CPATransferException;
import org.sosy_lab.cpachecker.exceptions.UnrecognizedCCodeException;
import org.sosy_lab.cpachecker.util.Pair;
import org.sosy_lab.java_smt.api.BooleanFormula;
import org.sosy_lab.java_smt.api.Formula;
import org.sosy_lab.java_smt.api.Model.ValueAssignment;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link PathFormulaManager} that delegates to another
 * instance but caches results of some methods.
 */
public class CachingPathFormulaManager implements PathFormulaManager {

  public final Timer pathFormulaComputationTimer = new Timer();
  public int pathFormulaCacheHits = 0;

  public final PathFormulaManager delegate;

  private final Map<Pair<CFAEdge, PathFormula>, Pair<PathFormula, ErrorConditions>> andFormulaWithConditionsCache
            = new HashMap<>();
  private final Map<Pair<CFAEdge, PathFormula>, PathFormula> andFormulaCache
            = new HashMap<>();

  private final Map<Pair<PathFormula, PathFormula>, PathFormula> orFormulaCache
            = new HashMap<>();

  private final Map<PathFormula, PathFormula> emptyFormulaCache
            = new HashMap<>();

  private final PathFormula emptyFormula;

  public CachingPathFormulaManager(PathFormulaManager pDelegate) {
    delegate = pDelegate;
    emptyFormula = delegate.makeEmptyPathFormula();
  }

  @Override
  public Pair<PathFormula, ErrorConditions> makeAndWithErrorConditions(PathFormula pOldFormula, CFAEdge pEdge) throws CPATransferException, InterruptedException {

    final Pair<CFAEdge, PathFormula> formulaCacheKey = Pair.of(pEdge, pOldFormula);
    Pair<PathFormula, ErrorConditions> result = andFormulaWithConditionsCache.get(formulaCacheKey);
    if (result == null) {
      pathFormulaComputationTimer.start();
      // compute new pathFormula with the operation on the edge
      result = delegate.makeAndWithErrorConditions(pOldFormula, pEdge);
      pathFormulaComputationTimer.stop();
      andFormulaWithConditionsCache.put(formulaCacheKey, result);

    } else {
      pathFormulaCacheHits++;
    }
    return result;
  }

  @Override
  public PathFormula makeAnd(PathFormula pOldFormula, CFAEdge pEdge) throws CPATransferException, InterruptedException {
    final Pair<CFAEdge, PathFormula> formulaCacheKey = Pair.of(pEdge, pOldFormula);
    PathFormula result = andFormulaCache.get(formulaCacheKey);
    if (result == null) {
      try {
      pathFormulaComputationTimer.start();
      // compute new pathFormula with the operation on the edge
      result = delegate.makeAnd(pOldFormula, pEdge);
      andFormulaCache.put(formulaCacheKey, result);
      } finally {
        pathFormulaComputationTimer.stop();
      }

    } else {
      pathFormulaCacheHits++;
    }
    return result;
  }

  @Override
  public PathFormula makeOr(PathFormula pF1, PathFormula pF2) throws InterruptedException {
    final Pair<PathFormula, PathFormula> formulaCacheKey = Pair.of(pF1, pF2);

    PathFormula result = orFormulaCache.get(formulaCacheKey);
    if (result == null) {
      // try again with other order
      result = orFormulaCache.get(Pair.of(pF2, pF1));
    }

    if (result == null) {
      result = delegate.makeOr(pF1, pF2);
      orFormulaCache.put(formulaCacheKey, result);
    } else {
      pathFormulaCacheHits++;
    }
    return result;
  }

  @Override
  public PathFormula makeEmptyPathFormula() {
    return emptyFormula;
  }

  @Override
  public PathFormula makeEmptyPathFormula(PathFormula pOldFormula) {
    PathFormula result = emptyFormulaCache.get(pOldFormula);
    if (result == null) {
      result = delegate.makeEmptyPathFormula(pOldFormula);
      emptyFormulaCache.put(pOldFormula, result);
    } else {
      pathFormulaCacheHits++;
    }
    return result;
  }

  @Override
  public Formula makeFormulaForVariable(
      PathFormula pContext, String pVarName, CType pType, boolean forcePointerDereference) {
    return delegate.makeFormulaForVariable(pContext, pVarName, pType, forcePointerDereference);
  }

  @Override
  public PathFormula makeAnd(PathFormula pPathFormula, BooleanFormula pOtherFormula) {
    return delegate.makeAnd(pPathFormula, pOtherFormula);
  }

  @Override
  public PathFormula makeAnd(PathFormula pPathFormula, CExpression pAssumption)
      throws CPATransferException, InterruptedException {
    return delegate.makeAnd(pPathFormula, pAssumption);
  }

  @Override
  public PathFormula makeNewPathFormula(PathFormula pOldFormula, SSAMap pM) {
    return delegate.makeNewPathFormula(pOldFormula, pM);
  }

  @Override
  public PathFormula makeFormulaForPath(List<CFAEdge> pPath) throws CPATransferException, InterruptedException {
    return delegate.makeFormulaForPath(pPath);
  }

  @Override
  public BooleanFormula buildBranchingFormula(Set<ARGState> pElementsOnPath)
      throws CPATransferException, InterruptedException {
    return delegate.buildBranchingFormula(pElementsOnPath);
  }

  @Override
  public Map<Integer, Boolean> getBranchingPredicateValuesFromModel(Iterable<ValueAssignment> pModel) {
    return delegate.getBranchingPredicateValuesFromModel(pModel);
  }

  @Override
  public Formula expressionToFormula(PathFormula pFormula, CIdExpression expr,
      CFAEdge edge) throws UnrecognizedCCodeException {
    return delegate.expressionToFormula(pFormula, expr, edge);
  }

  @Override
  public BooleanFormula buildImplicationTestAsUnsat(PathFormula pF1, PathFormula pF2) throws InterruptedException {
    return delegate.buildImplicationTestAsUnsat(pF1, pF2);
  }

  @Override
  public void printStatistics(PrintStream out) {
    int pathFormulaCacheHits = this.pathFormulaCacheHits;
    int totalPathFormulaComputations = this.pathFormulaComputationTimer.getNumberOfIntervals() + pathFormulaCacheHits;
    out.println("Number of path formula cache hits:   " + pathFormulaCacheHits + " (" + toPercent(pathFormulaCacheHits, totalPathFormulaComputations) + ")");
    out.println();

    out.println("Inside post operator:                  ");
    out.println("  Inside path formula creation:        ");
    out.println("    Time for path formula computation: " + pathFormulaComputationTimer);
    out.println();

    delegate.printStatistics(out);
  }
}
