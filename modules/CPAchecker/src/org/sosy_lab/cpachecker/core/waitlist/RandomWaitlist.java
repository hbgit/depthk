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

import java.util.LinkedList;
import java.util.Random;

import org.sosy_lab.cpachecker.core.interfaces.AbstractState;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Waitlist implementation that considers states in a random order for pop().
 */
@SuppressFBWarnings(value = "BC_BAD_CAST_TO_CONCRETE_COLLECTION",
justification = "warnings is only because of casts introduced by generics")
public class RandomWaitlist extends AbstractWaitlist<LinkedList<AbstractState>> {

  private final Random rand = new Random();

  protected RandomWaitlist() {
    super(new LinkedList<>());
  }

  @Override
  public AbstractState pop() {
    int r = rand.nextInt(waitlist.size());
    return waitlist.remove(r);
  }
}
