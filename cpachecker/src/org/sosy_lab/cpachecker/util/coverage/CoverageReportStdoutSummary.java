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
package org.sosy_lab.cpachecker.util.coverage;

import java.io.PrintStream;
import java.util.Map;

import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.configuration.Option;
import org.sosy_lab.common.configuration.Options;
import org.sosy_lab.cpachecker.util.statistics.StatisticsUtils;

@Options
class CoverageReportStdoutSummary implements CoverageWriter {

  @Option(secure=true,
      name="coverage.stdout",
      description="print coverage summary to stdout")
  private boolean enabled = true;

  public CoverageReportStdoutSummary(Configuration pConfig) throws InvalidConfigurationException {
    pConfig.inject(this);
  }

  @Override
  public void write(Map<String, FileCoverageInformation> pCoverage, PrintStream pStdOut) {

    if (!enabled) {
      return;
    }

    long numTotalConditions = 0;
    long numTotalFunctions = 0;
    long numTotalLines = 0;

    long numVisitedConditions = 0;
    long numVisitedFunctions = 0;
    long numVisitedLines = 0;

    for (FileCoverageInformation info: pCoverage.values()) {
      numTotalFunctions =+ info.allFunctions.size();
      numVisitedFunctions =+ info.visitedFunctions.size();

      numTotalConditions =+ info.allAssumes.size();
      numVisitedConditions =+ info.visitedAssumes.size();

      numTotalLines =+ info.allLines.size();

      for (Integer line : info.allLines) {
        if (info.getVisitedLine(line) > 0) {
          numVisitedLines += 1;
        }
      }
    }

    pStdOut.println("Code Coverage");
    pStdOut.println("-----------------------------");

    if (numTotalFunctions > 0) {
      final double functionCoverage = numVisitedFunctions / (double) numTotalFunctions;
      StatisticsUtils.write(pStdOut, 1, 25, "Function coverage", String.format("%.3f", functionCoverage));
    }

    if (numTotalLines > 0) {
      final double lineCoverage = numVisitedLines / (double) numTotalLines;
      StatisticsUtils.write(pStdOut, 1, 25, "Visited lines", numVisitedLines);
      StatisticsUtils.write(pStdOut, 1, 25, "Total lines", numTotalLines);
      StatisticsUtils.write(pStdOut, 1, 25, "Line coverage", String.format("%.3f", lineCoverage));
    }

    if (numTotalConditions > 0) {
      final double conditionCoverage = numVisitedConditions / (double) numTotalConditions;
      StatisticsUtils.write(pStdOut, 1, 25, "Visited conditions", numVisitedConditions);
      StatisticsUtils.write(pStdOut, 1, 25, "Total conditions", numTotalConditions);
      StatisticsUtils.write(pStdOut, 1, 25, "Condition coverage", String.format("%.3f", conditionCoverage));
    }

    pStdOut.println();

  }

}
