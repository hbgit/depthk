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
package org.sosy_lab.cpachecker.pcc.strategy.partialcertificate;

public class WeightedNode {

  private final int nodeNumber;
  private final int weight;

  public int getNodeNumber() {
    return nodeNumber;
  }

  public int getWeight() {
    return weight;
  }

  public WeightedNode(int pNode, int pWeight) {
    super();
    nodeNumber = pNode;
    weight = pWeight;
  }

  /**
   * Node represented by [node(W: weight)]
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(nodeNumber).append("(W:").append(weight).append(")");
    return s.toString();
  }
}
