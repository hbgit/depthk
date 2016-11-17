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
package org.sosy_lab.cpachecker.util.globalinfo;

import java.util.HashMap;
import java.util.Map;

import org.sosy_lab.cpachecker.cpa.automaton.Automaton;
import org.sosy_lab.cpachecker.cpa.automaton.AutomatonInternalState;
import org.sosy_lab.cpachecker.cpa.automaton.ControlAutomatonCPA;


public class AutomatonInfo {
  private final Map<Integer, AutomatonInternalState> idToState;
  private final Map<String, ControlAutomatonCPA> nameToCPA;

  AutomatonInfo() {
    idToState= new HashMap<>();
    nameToCPA = new HashMap<>();
  }

  public void register(Automaton automaton, ControlAutomatonCPA cpa) {
    for (AutomatonInternalState state : automaton.getStates()) {
      idToState.put(state.getStateId(), state);
    }
    nameToCPA.put(automaton.getName(), cpa);
  }

  public AutomatonInternalState getStateById(int id) {
    return idToState.get(id);
  }

  public ControlAutomatonCPA getCPAForAutomaton(String automatonName) {
    return nameToCPA.get(automatonName);
  }
}
