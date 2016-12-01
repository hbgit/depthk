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
package org.sosy_lab.cpachecker.util.automaton;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.FileOption;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.configuration.Option;
import org.sosy_lab.common.configuration.Options;
import org.sosy_lab.common.io.MoreFiles;
import org.sosy_lab.common.log.LogManager;

public class VerificationTaskMetaData {

  /**
   * This is a temporary hack to easily obtain specification and verification tasks. TODO: Move the
   * witness export out of the ARG CPA after the new error report has been integrated and obtain the
   * values without this hack.
   */
  @Options
  private static class HackyOptions {

    @Option(
      secure = true,
      name = "analysis.programNames",
      description = "A String, denoting the programs to be analyzed"
    )
    private String programs;

    @Option(
      secure = true,
      name = "properties",
      description = "List of property files (INTERNAL USAGE ONLY - DO NOT USE)"
    )
    @FileOption(FileOption.Type.OPTIONAL_INPUT_FILE)
    private List<Path> propertyFiles = ImmutableList.of();

    @Option(
      secure = true,
      name = "cpa.predicate.handlePointerAliasing",
      description =
          "Handle aliasing of pointers. "
              + "This adds disjunctions to the formulas, so be careful when using cartesian abstraction."
    )
    private boolean handlePointerAliasing = true;
  }

  private final VerificationTaskMetaData.HackyOptions hackyOptions = new HackyOptions();

  private final String memoryModel;

  private final Iterable<String> specifications;

  private final Optional<Iterable<String>> programNames;

  private String programHash;

  public VerificationTaskMetaData(Configuration pConfig, LogManager pLogger)
      throws InvalidConfigurationException {
    pConfig.inject(hackyOptions);

    memoryModel = hackyOptions.handlePointerAliasing ? "precise" : "simple";
    specifications =
        FluentIterable.from(hackyOptions.propertyFiles)
            .transform(
                new Function<Path, String>() {

                  @Override
                  public String apply(Path pArg0) {
                    try {
                      return MoreFiles.toString(pArg0, Charsets.UTF_8).trim();
                    } catch (IOException e) {
                      pLogger.logUserException(
                          Level.WARNING, e, "Could not export specification to witness.");
                      return "Unknown specification";
                    }
                  }
                });
    if (hackyOptions.programs == null) {
      programNames = Optional.empty();
    } else {
      Splitter commaSplitter = Splitter.on(',').omitEmptyStrings().trimResults();
      programNames = Optional.of(commaSplitter.split(hackyOptions.programs));
    }
  }

  private static String computeProgramHash(Iterable<String> pProgramDenotations)
      throws IOException {
    List<ByteSource> sources = new ArrayList<>(1);
    for (String programDenotation : pProgramDenotations) {
      Path programPath = Paths.get(programDenotation);
      sources.add(MoreFiles.asByteSource(programPath));
    }
    HashCode hash = ByteSource.concat(sources).hash(Hashing.sha1());
    return BaseEncoding.base16().lowerCase().encode(hash.asBytes());
  }

  /**
   * Gets the names of the source-code files of the verification task if this information is
   * available.
   *
   * @return the names of the source-code files of the verification task.
   */
  public Optional<Iterable<String>> getProgramNames() {
    return programNames;
  }

  /**
   * Gets the SHA-1 hash sum of the source-code files of the verification task if they are
   * available.
   *
   * @return the SHA-1 hash sum of the source-code files of the verification task.
   * @throws IOException if an {@code IOException} occurs while trying to read the source-code
   *     files.
   */
  public Optional<String> getProgramHash() throws IOException {
    if (!programNames.isPresent()) {
      return Optional.empty();
    }
    if (programHash == null) {
      programHash = computeProgramHash(programNames.get());
    }
    return Optional.of(programHash);
  }

  /**
   * Gets an identifier for the assumed memory model.
   *
   * @return an identifier for the assumed memory model.
   */
  public String getMemoryModel() {
    return memoryModel;
  }

  /**
   * Gets the specifications considered for this verification task.
   *
   * @return the specifications considered for this verification task.
   */
  public Iterable<String> getSpecifications() {
    return specifications;
  }
}
