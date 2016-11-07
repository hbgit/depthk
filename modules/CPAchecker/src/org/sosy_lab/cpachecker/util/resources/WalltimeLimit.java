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
package org.sosy_lab.cpachecker.util.resources;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.concurrent.TimeUnit;

import org.sosy_lab.common.time.TimeSpan;

/**
 * A limit that measures the elapsed time as returned by {@link System#nanoTime()}.
 */
public class WalltimeLimit implements ResourceLimit {

  private final long duration;
  private final long endTime;

  private WalltimeLimit(long pDuration) {
    duration = pDuration;
    endTime = getCurrentValue() + pDuration;
  }

  public static WalltimeLimit fromNowOn(TimeSpan timeSpan) {
    return fromNowOn(timeSpan.asNanos(), TimeUnit.NANOSECONDS);
  }

  public static WalltimeLimit fromNowOn(long time, TimeUnit unit) {
    checkArgument(time > 0);
    long nanoDuration = TimeUnit.NANOSECONDS.convert(time, unit);
    return new WalltimeLimit(nanoDuration);
  }

  @Override
  public long getCurrentValue() {
    return System.nanoTime();
  }

  @Override
  public boolean isExceeded(long pCurrentValue) {
    return pCurrentValue >= endTime;
  }

  @Override
  public long nanoSecondsToNextCheck(long pCurrentValue) {
    return endTime - pCurrentValue;
  }

  @Override
  public String getName() {
    return "walltime limit of " + TimeUnit.NANOSECONDS.toSeconds(duration) + "s";
  }
}