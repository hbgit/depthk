/*
 *  CPAchecker is a tool for configurable software verification.
 *  This file is part of CPAchecker.
 *
 *  Copyright (C) 2007-2016  Dirk Beyer
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
package org.sosy_lab.cpachecker.cpa.termination;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.core.interfaces.Precision;
import org.sosy_lab.cpachecker.core.interfaces.PrecisionAdjustment;
import org.sosy_lab.cpachecker.core.interfaces.PrecisionAdjustmentResult;
import org.sosy_lab.cpachecker.core.reachedset.UnmodifiableReachedSet;
import org.sosy_lab.cpachecker.exceptions.CPAException;

import java.util.Optional;

public class TerminationPrecisionAdjustment implements PrecisionAdjustment {

  private final PrecisionAdjustment precisionAdjustment;

  public TerminationPrecisionAdjustment(PrecisionAdjustment pPrecisionAdjustment) {
    precisionAdjustment = Preconditions.checkNotNull(pPrecisionAdjustment);
  }

  @Override
  public Optional<PrecisionAdjustmentResult> prec(
      AbstractState pState,
      Precision pPrecision,
      UnmodifiableReachedSet pStates,
      Function<AbstractState, AbstractState> pStateProjection,
      AbstractState pFullState)
      throws CPAException, InterruptedException {
    TerminationState state = (TerminationState) pState;
    AbstractState wrappedState = state.getWrappedState();
    Optional<PrecisionAdjustmentResult> result =
        precisionAdjustment.prec(wrappedState, pPrecision, pStates, pStateProjection, pFullState);

    return result.map(r -> r.withAbstractState(state.withWrappedState(r.abstractState())));
  }
}
