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
package org.sosy_lab.cpachecker.cpa.arg.counterexamples;

import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.core.counterexample.CounterexampleInfo;
import org.sosy_lab.cpachecker.core.interfaces.ConfigurableProgramAnalysis;

import java.util.Optional;
import com.google.common.collect.ImmutableSet;


/**
 * A {@link CounterexampleFilter} that defines paths as similar
 * if they contain the exact same set of {@link CFAEdge}s,
 * but the order of the edges, and how many times they are visited along the path,
 * are irrelevant.
 *
 * This filter subsumes {@link PathEqualityCounterexampleFilter},
 * so if you use this class, you do not need to (additionally) use
 * {@link PathEqualityCounterexampleFilter}.
 */
public class PathEdgesEqualityCounterexampleFilter extends AbstractSetBasedCounterexampleFilter<ImmutableSet<CFAEdge>> {

  public PathEdgesEqualityCounterexampleFilter(Configuration pConfig, LogManager pLogger, ConfigurableProgramAnalysis pCpa) {
    super(pConfig, pLogger, pCpa);
  }

  @Override
  protected Optional<ImmutableSet<CFAEdge>> getCounterexampleRepresentation(CounterexampleInfo counterexample) {
    return Optional.of(ImmutableSet.copyOf(counterexample.getTargetPath().getInnerEdges()));
  }
}
