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
package org.sosy_lab.cpachecker.cpa.predicate;

import static com.google.common.base.Preconditions.checkState;

import org.sosy_lab.common.configuration.Options;
import org.sosy_lab.cpachecker.cfa.blocks.BlockPartitioning;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.util.predicates.BlockOperator;

@Options
public class BAMBlockOperator extends BlockOperator {

  private BlockPartitioning partitioning = null;

  void setPartitioning(BlockPartitioning pPartitioning) {
    checkState(partitioning == null);
    partitioning = pPartitioning;
  }

  /**
   * @see{@link BlockOperator#isBlockEnd}
   */
  @Override
  public boolean isBlockEnd(CFANode loc, int thresholdValue) {
    return super.isBlockEnd(loc, thresholdValue)
        || partitioning.isCallNode(loc)
        || partitioning.isReturnNode(loc);
  }

  @Override
  public boolean alwaysReturnsFalse() {
    return super.alwaysReturnsFalse()
        && partitioning.getBlocks().isEmpty();
  }

  public BlockPartitioning getPartitioning() {
    checkState(partitioning != null);
    return partitioning;
  }
}
