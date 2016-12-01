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
package org.sosy_lab.cpachecker.core.waitlist;

import com.google.common.base.Preconditions;

import org.sosy_lab.cpachecker.core.interfaces.AbstractState;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Waitlist implementation that supports either a breadth-first (BFS) or
 * depth-first (DFS) strategy for pop().
 */
public class SimpleWaitlist extends AbstractWaitlist<Deque<AbstractState>> {

  private final TraversalMethod traversal;

  protected SimpleWaitlist(TraversalMethod pTraversal) {
    super(new ArrayDeque<>());
    Preconditions.checkArgument(pTraversal == TraversalMethod.BFS || pTraversal == TraversalMethod.DFS);
    traversal = pTraversal;
  }

  @Override
  public AbstractState pop() {
    switch (traversal) {
    case BFS:
      return waitlist.removeFirst();

    case DFS:
      return waitlist.removeLast();

    default:
      throw new AssertionError();
    }
  }
}