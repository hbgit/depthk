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
package org.sosy_lab.cpachecker.cpa.statistics;

import org.sosy_lab.cpachecker.core.interfaces.AbstractDomain;
import org.sosy_lab.cpachecker.core.interfaces.AbstractState;
import org.sosy_lab.cpachecker.exceptions.CPAException;

/**
 * The Domain of the StatisticsCPA; delegates its work to the states, which in turn delegate to StatisticsStateFactory.
 */
public class StatisticsCPADomain implements AbstractDomain {

  @Override
  public AbstractState join(AbstractState pState1, AbstractState pState2) throws CPAException {
    StatisticsState state1 = (StatisticsState) pState1;
    StatisticsState state2 = (StatisticsState) pState2;
    assert (state1.getLocationNode().equals(state2.getLocationNode())) : "can only merge on the same location";
    return state1.mergeState(state2);
  }

  @Override
  public boolean isLessOrEqual(AbstractState pState1, AbstractState pState2) throws CPAException, InterruptedException {
    StatisticsState state1 = (StatisticsState) pState1;
    StatisticsState state2 = (StatisticsState) pState2;

    return state2.containsPrevious(state1);
  }

}
