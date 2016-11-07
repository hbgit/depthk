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
package org.sosy_lab.cpachecker.pcc.strategy.arg;

import org.sosy_lab.common.ShutdownNotifier;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.configuration.Option;
import org.sosy_lab.common.configuration.Options;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.Precision;
import org.sosy_lab.cpachecker.core.interfaces.StopOperator;
import org.sosy_lab.cpachecker.core.interfaces.TransferRelation;
import org.sosy_lab.cpachecker.core.reachedset.ReachedSet;
import org.sosy_lab.cpachecker.cpa.PropertyChecker.PropertyCheckerCPA;
import org.sosy_lab.cpachecker.cpa.arg.ARGCPA;
import org.sosy_lab.cpachecker.cpa.arg.ARGState;
import org.sosy_lab.cpachecker.exceptions.CPAException;
import org.sosy_lab.cpachecker.pcc.propertychecker.DefaultPropertyChecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

@Options(prefix="pcc.arg")
public class ARG_CPAStrategy extends AbstractARGStrategy {

  @Option(secure=true,
      name = "checkPropertyPerElement",
      description = "Enable if used property checker implements satisfiesProperty(AbstractState) and checked property is violated for a set iff an element in this set exists for which violates the property")
  private boolean singleCheck = false;
  private List<AbstractState> visitedStates;
  private final StopOperator stop;
  private final TransferRelation transfer;

  public ARG_CPAStrategy(
      final Configuration pConfig,
      final LogManager pLogger,
      final ShutdownNotifier pShutdownNotifier,
      final @Nullable PropertyCheckerCPA pCpa)
      throws InvalidConfigurationException {
    super(pConfig, pLogger, pCpa == null ? new DefaultPropertyChecker() : pCpa.getPropChecker(), pShutdownNotifier);
    pConfig.inject(this);
    if (pCpa == null) {
      stop = null;
      transfer = null;
    } else {
      if(!(pCpa.getWrappedCPAs().get(0) instanceof ARGCPA)) {
        throw new InvalidConfigurationException("Expect that the property checker cpa wraps an ARG cpa");
      }
      stop = ((ARGCPA)pCpa.getWrappedCPAs().get(0)).getWrappedCPAs().get(0).getStopOperator();
      transfer = ((ARGCPA)pCpa.getWrappedCPAs().get(0)).getWrappedCPAs().get(0).getTransferRelation();
    }
  }

  @Override
  protected void initChecking(final ARGState pRoot) {
    if (!singleCheck) {
      visitedStates = new LinkedList<>();
    }

  }

  @Override
  protected boolean checkCovering(final ARGState pCovered, final ARGState pCovering, final Precision pPrecision) throws CPAException, InterruptedException {
    return checkCoverWithStopOp(pCovered.getWrappedState(), Collections.singleton(pCovering.getWrappedState()), pPrecision);
  }

  @Override
  protected boolean isCheckSuccessful() {
    if (!singleCheck) {
      try {
        stats.getPropertyCheckingTimer().start();
        return propChecker.satisfiesProperty(visitedStates);
      } finally {
        stats.getPropertyCheckingTimer().stop();
      }
    }
    return true;
  }

  @Override
  protected boolean isCheckComplete() {
    return true;
  }

  @Override
  protected boolean checkForStatePropertyAndOtherStateActions(final ARGState pState) {
    if (!singleCheck) {
      visitedStates.add(pState);
    } else {
      super.checkForStatePropertyAndOtherStateActions(pState);
    }
    return true;
  }

  @Override
  protected boolean prepareNextWaitlistIteration(final ReachedSet pReachedSet) {
    return true;
  }

  @Override
  protected boolean checkSuccessors(final ARGState pPredecessor, final Collection<ARGState> pSuccessors,
      final Precision pPrecision) throws InterruptedException, CPAException {
    Collection<AbstractState> wrappedSuccessors = new ArrayList<>(pSuccessors.size());
    for (ARGState succ: pSuccessors) {
      wrappedSuccessors.add(succ.getWrappedState());
    }

    Collection<? extends AbstractState> computedSuccessors =
        transfer.getAbstractSuccessors(pPredecessor.getWrappedState(), pPrecision);

     for (AbstractState succ : computedSuccessors) {
       if (!checkCoverWithStopOp(succ, wrappedSuccessors, pPrecision)) {
         return false;
       }
     }
    return true;
  }

  @Override
  protected boolean addSuccessors(final Collection<ARGState> pSuccessors, final ReachedSet pReachedSet,
      final Precision pPrecision) {
    for (ARGState argS : pSuccessors) {
      pReachedSet.add(argS, pPrecision);
    }
    return true;
  }

  @Override
  protected boolean treatStateIfCoveredByUnkownState(final ARGState pCovered, final ARGState pCoveringState,
      final ReachedSet pReachedSet,
      final Precision pPrecision) {
    pReachedSet.add(pCoveringState, pPrecision);
    return false;
  }

  private boolean checkCoverWithStopOp(final AbstractState pCovered, final Collection<AbstractState> pCoverElems,
      final Precision pPrecision) throws CPAException, InterruptedException {
    return stop.stop(pCovered, pCoverElems, pPrecision);
  }
}
