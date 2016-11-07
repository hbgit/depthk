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
package org.sosy_lab.cpachecker.cpa.smg.objects.dls;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import org.sosy_lab.cpachecker.cpa.smg.SMGAbstractionBlock;
import org.sosy_lab.cpachecker.cpa.smg.SMGAbstractionCandidate;
import org.sosy_lab.cpachecker.cpa.smg.SMGEdgeHasValue;
import org.sosy_lab.cpachecker.cpa.smg.SMGInconsistentException;
import org.sosy_lab.cpachecker.cpa.smg.graphs.CLangSMG;
import org.sosy_lab.cpachecker.cpa.smg.objects.SMGObject;
import org.sosy_lab.cpachecker.cpa.smg.objects.sll.SMGSingleLinkedListCandidateSequenceBlock;
import org.sosy_lab.cpachecker.cpa.smg.refiner.SMGMemoryPath;

import java.util.Optional;

public class SMGDoublyLinkedListCandidateSequenceBlock
    implements SMGAbstractionBlock {

  private final SMGDoublyLinkedListShape shape;
  private final int length;
  private final SMGMemoryPath pointerToStartObject;

  public SMGDoublyLinkedListCandidateSequenceBlock(SMGDoublyLinkedListShape pShape, int pLength,
      SMGMemoryPath pPointerToStartObject) {
    super();
    shape = pShape;
    length = pLength;
    pointerToStartObject = pPointerToStartObject;
  }

  @Override
  public boolean isBlocked(SMGAbstractionCandidate pCandidate, CLangSMG smg) throws SMGInconsistentException {

    if (!(pCandidate instanceof SMGDoublyLinkedListCandidateSequence)) { return false; }

    SMGDoublyLinkedListCandidateSequence dllcs = (SMGDoublyLinkedListCandidateSequence) pCandidate;

    if (!shape.equals(dllcs.getCandidate().getDllShape())) {
      return false;
    }

    if(length != dllcs.getLength()) {
      return false;
    }

    Optional<SMGEdgeHasValue> edge = smg.getHVEdgeFromMemoryLocation(pointerToStartObject);

    if(!edge.isPresent()) {
      return false;
    }

    int value = edge.get().getValue();

    if (!smg.isPointer(value)) {
      return false;
    }

    SMGObject startObjectLock = smg.getPointer(value).getObject();

    if (!startObjectLock.equals(dllcs.getCandidate().getObject())) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + length;
    result =
        prime * result + ((pointerToStartObject == null) ? 0 : pointerToStartObject.hashCode());
    result = prime * result + ((shape == null) ? 0 : shape.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SMGDoublyLinkedListCandidateSequenceBlock other =
        (SMGDoublyLinkedListCandidateSequenceBlock) obj;
    if (length != other.length) {
      return false;
    }
    if (pointerToStartObject == null) {
      if (other.pointerToStartObject != null) {
        return false;
      }
    } else if (!pointerToStartObject.equals(other.pointerToStartObject)) {
      return false;
    }
    if (shape == null) {
      if (other.shape != null) {
        return false;
      }
    } else if (!shape.equals(other.shape)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(SMGAbstractionBlock other) {

    if(other instanceof SMGSingleLinkedListCandidateSequenceBlock) {
      return 1;
    }

    SMGDoublyLinkedListCandidateSequenceBlock otherDoublyLinkedBlock =
        (SMGDoublyLinkedListCandidateSequenceBlock) other;

    return ComparisonChain.start()
        .compare(shape, otherDoublyLinkedBlock.shape)
        .compare(length, otherDoublyLinkedBlock.length, Ordering.<Integer> natural().nullsFirst())
        .compare(pointerToStartObject, otherDoublyLinkedBlock.pointerToStartObject)
        .result();

  }

}