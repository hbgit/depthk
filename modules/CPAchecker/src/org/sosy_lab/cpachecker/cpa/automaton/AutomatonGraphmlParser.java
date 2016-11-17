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
package org.sosy_lab.cpachecker.cpa.automaton;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.ByteSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.FileOption;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.configuration.Option;
import org.sosy_lab.common.configuration.Options;
import org.sosy_lab.common.io.MoreFiles;
import org.sosy_lab.common.log.LogManager;
import org.sosy_lab.cpachecker.cfa.CParser;
import org.sosy_lab.cpachecker.cfa.CProgramScope;
import org.sosy_lab.cpachecker.cfa.CSourceOriginMapping;
import org.sosy_lab.cpachecker.cfa.ParseResult;
import org.sosy_lab.cpachecker.cfa.ast.AExpression;
import org.sosy_lab.cpachecker.cfa.ast.AExpressionAssignmentStatement;
import org.sosy_lab.cpachecker.cfa.ast.AIdExpression;
import org.sosy_lab.cpachecker.cfa.ast.AIntegerLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.ALeftHandSide;
import org.sosy_lab.cpachecker.cfa.ast.AStatement;
import org.sosy_lab.cpachecker.cfa.ast.FileLocation;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CBinaryExpressionBuilder;
import org.sosy_lab.cpachecker.cfa.ast.c.CCastExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpressionAssignmentStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CExpressionStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CIntegerLiteralExpression;
import org.sosy_lab.cpachecker.cfa.ast.c.CLeftHandSide;
import org.sosy_lab.cpachecker.cfa.ast.c.CSimpleDeclaration;
import org.sosy_lab.cpachecker.cfa.ast.c.CStatement;
import org.sosy_lab.cpachecker.cfa.ast.c.CUnaryExpression;
import org.sosy_lab.cpachecker.cfa.model.AReturnStatementEdge;
import org.sosy_lab.cpachecker.cfa.model.AStatementEdge;
import org.sosy_lab.cpachecker.cfa.model.AssumeEdge;
import org.sosy_lab.cpachecker.cfa.model.CFAEdge;
import org.sosy_lab.cpachecker.cfa.model.CFANode;
import org.sosy_lab.cpachecker.cfa.model.FunctionEntryNode;
import org.sosy_lab.cpachecker.cfa.parser.Scope;
import org.sosy_lab.cpachecker.cfa.types.MachineModel;
import org.sosy_lab.cpachecker.cfa.types.c.CBasicType;
import org.sosy_lab.cpachecker.cfa.types.c.CNumericTypes;
import org.sosy_lab.cpachecker.cfa.types.c.CSimpleType;
import org.sosy_lab.cpachecker.cfa.types.c.CType;
import org.sosy_lab.cpachecker.cpa.automaton.SourceLocationMatcher.OffsetMatcher;
import org.sosy_lab.cpachecker.cpa.automaton.SourceLocationMatcher.OriginLineMatcher;
import org.sosy_lab.cpachecker.exceptions.CParserException;
import org.sosy_lab.cpachecker.exceptions.ParserException;
import org.sosy_lab.cpachecker.util.CFAUtils;
import org.sosy_lab.cpachecker.util.automaton.AutomatonGraphmlCommon.AssumeCase;
import org.sosy_lab.cpachecker.util.automaton.AutomatonGraphmlCommon.GraphMlTag;
import org.sosy_lab.cpachecker.util.automaton.AutomatonGraphmlCommon.GraphType;
import org.sosy_lab.cpachecker.util.automaton.AutomatonGraphmlCommon.KeyDef;
import org.sosy_lab.cpachecker.util.automaton.AutomatonGraphmlCommon.NodeFlag;
import org.sosy_lab.cpachecker.util.automaton.VerificationTaskMetaData;
import org.sosy_lab.cpachecker.util.expressions.And;
import org.sosy_lab.cpachecker.util.expressions.ExpressionTree;
import org.sosy_lab.cpachecker.util.expressions.ExpressionTreeFactory;
import org.sosy_lab.cpachecker.util.expressions.ExpressionTrees;
import org.sosy_lab.cpachecker.util.expressions.LeafExpression;
import org.sosy_lab.cpachecker.util.expressions.Simplifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Options(prefix="spec")
public class AutomatonGraphmlParser {

  private static final String DISTANCE_TO_VIOLATION = "__DISTANCE_TO_VIOLATION";

  public static final String WITNESS_AUTOMATON_NAME = "WitnessAutomaton";

  @Option(secure=true, description="Consider assumptions that are provided with the path automaton?")
  private boolean considerAssumptions = true;

  @Option(secure=true, description="Legacy option for token-based matching with path automatons.")
  private boolean transitionToStopForNegatedTokensetMatch = false; // legacy: tokenmatching

  @Option(secure=true, description="Match the source code provided with the witness.")
  private boolean matchSourcecodeData = false;

  @Option(secure=true, description="Match the line numbers within the origin (mapping done by preprocessor line markers).")
  private boolean matchOriginLine = true;

  @Option(secure=true, description="Match the character offset within the file.")
  private boolean matchOffset = true;

  @Option(secure=true, description="Match the branching information at a branching location.")
  private boolean matchAssumeCase = true;

  @Option(secure=true, description="Do not try to \"catch up\" with witness guards: If they do not match, go to the sink.")
  private boolean strictMatching = false;

  @Option(secure=true, description="File for exporting the path automaton in DOT format.")
  @FileOption(FileOption.Type.OUTPUT_FILE)
  private Path automatonDumpFile = null;

  private Scope scope;
  private final LogManager logger;
  private final Configuration config;
  private final MachineModel machine;
  private final CBinaryExpressionBuilder binaryExpressionBuilder;
  private final Function<AStatement, ExpressionTree<AExpression>> fromStatement;
  private final ExpressionTreeFactory<AExpression> factory = ExpressionTrees.newCachingFactory();
  private final Simplifier<AExpression> simplifier = ExpressionTrees.newSimplifier(factory);
  private final VerificationTaskMetaData verificationTaskMetaData;

  public AutomatonGraphmlParser(
      Configuration pConfig, LogManager pLogger, MachineModel pMachine, Scope pScope)
      throws InvalidConfigurationException {
    pConfig.inject(this);

    this.scope = pScope;
    this.machine = pMachine;
    this.logger = pLogger;
    this.config = pConfig;

    binaryExpressionBuilder = new CBinaryExpressionBuilder(machine, logger);
    fromStatement = pStatement -> LeafExpression.fromStatement(pStatement, binaryExpressionBuilder);
    verificationTaskMetaData = new VerificationTaskMetaData(pConfig, pLogger);
  }

  /**
   * Parses a witness specification from a file and returns the Automata found in the file.
   *
   * @param pInputFile the path to the input file to parse the witness from.
   *
   * @throws InvalidConfigurationException if the configuration is invalid.
   *
   * @return the automata representing the witnesses found in the file.
   */
  public List<Automaton> parseAutomatonFile(Path pInputFile) throws InvalidConfigurationException {
    return parseAutomatonFile(MoreFiles.asByteSource(pInputFile));
  }

  /**
   * Parses a witness specification from a ByteSource and returns the Automata found in the source.
   *
   * @param pInputSource the ByteSource to parse the witness from.
   *
   * @throws InvalidConfigurationException if the configuration is invalid.
   *
   * @return the automata representing the witnesses found in the source.
   */
  public List<Automaton> parseAutomatonFile(ByteSource pInputSource)
      throws InvalidConfigurationException {
    try {
      try (InputStream inputStream = pInputSource.openStream();
          InputStream gzipInputStream = new GZIPInputStream(inputStream)) {
        return parseAutomatonFile(gzipInputStream);
      } catch (IOException e) {
        try (InputStream plainInputStream = pInputSource.openStream()) {
          return parseAutomatonFile(plainInputStream);
        }
      }
    } catch (IOException e) {
      throw new InvalidConfigurationException("Error while accessing automaton file!", e);
    }
  }

  /**
   * Parses a specification from an InputStream and returns the Automata found in the file.
   *
   * @param pInputStream the input stream to parse the witness from.
   *
   * @throws InvalidConfigurationException if the configuration is invalid.
   * @throws IOException if there occurs an IOException while reading from the stream.
   *
   * @return the automata representing the witnesses found in the stream.
   */
  private List<Automaton> parseAutomatonFile(InputStream pInputStream)
      throws InvalidConfigurationException, IOException {
    final CParser cparser =
        CParser.Factory.getParser(logger, CParser.Factory.getOptions(config), machine);
    try {
      // Parse the XML document ----
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(pInputStream);
      doc.getDocumentElement().normalize();

      GraphMlDocumentData docDat = new GraphMlDocumentData(doc);

      // (The one) root node of the graph ----
      NodeList graphs = doc.getElementsByTagName(GraphMlTag.GRAPH.toString());
      checkParsable(graphs.getLength() == 1, "The graph file must describe exactly one automaton.");
      Node graphNode = graphs.item(0);

      Set<String> programHash = GraphMlDocumentData.getDataOnNode(graphNode, KeyDef.PROGRAMHASH);
      if (programHash.isEmpty()) {
        logger.log(Level.WARNING, "Witness does not contain the hash sum "
            + "of the program and may therefore be unrelated to the "
            + "verification task it is being validated against.");
      } else if (!programHash.contains(verificationTaskMetaData.getProgramHash())) {
        throw new WitnessParseException("Hash sum of given verification-task "
            + "source code does not match the hash sum in the witness. "
            + "The witness is likely unrelated to the verification task.");
      }

      Set<String> graphTypeText = GraphMlDocumentData.getDataOnNode(graphNode, KeyDef.GRAPH_TYPE);
      final GraphType graphType;
      if (graphTypeText.isEmpty()) {
        graphType = GraphType.ERROR_WITNESS;
      } else if (graphTypeText.size() > 1) {
        throw new WitnessParseException("Only one graph type is allowed.");
      } else {
        String graphTypeToParse = graphTypeText.iterator().next().trim();
        Optional<GraphType> parsedGraphType = GraphType.tryParse(graphTypeToParse);
        if (parsedGraphType.isPresent()) {
          graphType = parsedGraphType.get();
        } else {
          graphType = GraphType.ERROR_WITNESS;
          logger.log(
              Level.WARNING,
              String.format(
                  "Unknown graph type %s, assuming %s instead.", graphTypeToParse, graphType));
        }
      }

      // Extract the information on the automaton ----
      Node nameAttribute = graphNode.getAttributes().getNamedItem("name");
      String automatonName = WITNESS_AUTOMATON_NAME;
      if (nameAttribute != null) {
        automatonName += "_" + nameAttribute.getTextContent();
      }
      String initialStateName = null;

      // Create transitions ----
      //AutomatonBoolExpr epsilonTrigger = new SubsetMatchEdgeTokens(Collections.<Comparable<Integer>>emptySet());
      NodeList edges = doc.getElementsByTagName(GraphMlTag.EDGE.toString());
      NodeList nodes = doc.getElementsByTagName(GraphMlTag.NODE.toString());
      Map<String, LinkedList<AutomatonTransition>> stateTransitions = Maps.newHashMap();
      Map<String, Deque<String>> stacks = Maps.newHashMap();

      // Create graph
      Multimap<String, Node> leavingEdges = HashMultimap.create();
      Multimap<String, Node> enteringEdges = HashMultimap.create();
      String entryNodeId = null;

      Set<String> violationStates = Sets.newHashSet();
      Set<String> sinkStates = Sets.newHashSet();

      for (int i = 0; i < edges.getLength(); i++) {
        Node stateTransitionEdge = edges.item(i);

        String sourceStateId = GraphMlDocumentData.getAttributeValue(stateTransitionEdge, "source", "Every transition needs a source!");
        String targetStateId = GraphMlDocumentData.getAttributeValue(stateTransitionEdge, "target", "Every transition needs a target!");
        leavingEdges.put(sourceStateId, stateTransitionEdge);
        enteringEdges.put(targetStateId, stateTransitionEdge);

        Element sourceStateNode = docDat.getNodeWithId(sourceStateId);
        Element targetStateNode = docDat.getNodeWithId(targetStateId);
        EnumSet<NodeFlag> sourceNodeFlags = docDat.getNodeFlags(sourceStateNode);
        EnumSet<NodeFlag> targetNodeFlags = docDat.getNodeFlags(targetStateNode);
        if (targetNodeFlags.contains(NodeFlag.ISVIOLATION)) {
          violationStates.add(targetStateId);
        }
        if (sourceNodeFlags.contains(NodeFlag.ISVIOLATION)) {
          violationStates.add(sourceStateId);
        }
        if (targetNodeFlags.contains(NodeFlag.ISSINKNODE)) {
          sinkStates.add(targetStateId);
        }
        if (sourceNodeFlags.contains(NodeFlag.ISSINKNODE)) {
          sinkStates.add(sourceStateId);
        }
      }

      // Find entry
      for (int i = 0; i < nodes.getLength(); ++i) {
        Node node = nodes.item(i);
        if (Boolean.parseBoolean(docDat.getDataValueWithDefault(node, KeyDef.ISENTRYNODE, "false"))) {
          entryNodeId = GraphMlDocumentData.getAttributeValue(node, "id", "Every node needs an id!");
          break;
        }
      }

      if (entryNodeId == null) {
        throw new WitnessParseException("You must define an entry node.");
      }

      // Determine distances to violation states
      Queue<String> waitlist = new ArrayDeque<>(violationStates);
      Map<String, Integer> distances = Maps.newHashMap();
      for (String violationState : violationStates) {
        distances.put(violationState, 0);
      }
      while (!waitlist.isEmpty()) {
        String current = waitlist.poll();
        int newDistance = distances.get(current) + 1;
        for (Node enteringEdge : enteringEdges.get(current)) {
          String sourceStateId = GraphMlDocumentData.getAttributeValue(enteringEdge, "source", "Every transition needs a source!");
          Integer oldDistance = distances.get(sourceStateId);
          if (oldDistance == null || oldDistance > newDistance) {
            distances.put(sourceStateId, newDistance);
            waitlist.offer(sourceStateId);
          }
        }
      }
      // Sink nodes have infinite distance to the target location, encoded as -1
      for (String sinkStateId : sinkStates) {
        distances.put(sinkStateId, -1);
      }

      Map<String, AutomatonBoolExpr> stutterConditions = Maps.newHashMap();

      Set<Node> visitedEdges = new HashSet<>();
      Queue<Node> waitingEdges = new ArrayDeque<>();
      waitingEdges.addAll(leavingEdges.get(entryNodeId));
      visitedEdges.addAll(waitingEdges);
      while (!waitingEdges.isEmpty()) {
        Node stateTransitionEdge = waitingEdges.poll();

        String sourceStateId = GraphMlDocumentData.getAttributeValue(stateTransitionEdge, "source", "Every transition needs a source!");
        String targetStateId = GraphMlDocumentData.getAttributeValue(stateTransitionEdge, "target", "Every transition needs a target!");

        if (graphType == GraphType.PROOF_WITNESS
            && sinkStates.contains(targetStateId)) {
          throw new WitnessParseException("Proof witnesses do not allow sink nodes.");
        }

        for (Node successorEdge : leavingEdges.get(targetStateId)) {
          if (visitedEdges.add(successorEdge)) {
            waitingEdges.add(successorEdge);
          }
        }

        Element targetStateNode = docDat.getNodeWithId(targetStateId);
        EnumSet<NodeFlag> targetNodeFlags = docDat.getNodeFlags(targetStateNode);

        boolean leadsToViolationNode = targetNodeFlags.contains(NodeFlag.ISVIOLATION);
        if (leadsToViolationNode) {
          violationStates.add(targetStateId);
        }

        Integer distance = distances.get(targetStateId);
        if (distance == null) {
          distance = Integer.MAX_VALUE;
        }
        final List<AutomatonAction> actions;
        if (graphType == GraphType.ERROR_WITNESS) {
          actions =
              Collections.<AutomatonAction>singletonList(
                  new AutomatonAction.Assignment(
                      DISTANCE_TO_VIOLATION, new AutomatonIntExpr.Constant(-distance)));
        } else {
          actions = Collections.emptyList();
        }

        List<AExpression> assumptions = Lists.newArrayList();
        ExpressionTree<AExpression> candidateInvariants = ExpressionTrees.getTrue();

        LinkedList<AutomatonTransition> transitions = stateTransitions.get(sourceStateId);
        if (transitions == null) {
          transitions = Lists.newLinkedList();
          stateTransitions.put(sourceStateId, transitions);
        }

        // Handle call stack
        Deque<String> currentStack = stacks.get(sourceStateId);
        if (currentStack == null) {
          currentStack = new ArrayDeque<>();
          stacks.put(sourceStateId, currentStack);
        }
        Deque<String> newStack = currentStack;
        Set<String> functionEntries = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.FUNCTIONENTRY);
        String functionEntry = Iterables.getOnlyElement(functionEntries, null);
        Set<String> functionExits = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.FUNCTIONEXIT);
        String functionExit = Iterables.getOnlyElement(functionEntries, null);

        // If the same function is entered and exited, the stack remains unchanged.
        // Otherwise, adjust the stack accordingly:
        if (!Objects.equals(functionEntry, functionExit)) {
          // First, perform the function exit
          if (!functionExits.isEmpty()) {
            if (newStack.isEmpty()) {
              logger.log(Level.WARNING, "Trying to return from function", functionExit, "although no function is on the stack.");
            } else {
              newStack = new ArrayDeque<>(newStack);
              String oldFunction = newStack.pop();
              assert oldFunction.equals(functionExit);
            }
          }
          // Now enter the new function
          if (!functionEntries.isEmpty()) {
            newStack = new ArrayDeque<>(newStack);
            newStack.push(functionEntry);
          }
        }
        // Store the stack in its state after the edge is applied
        stacks.put(targetStateId, newStack);

        // If the edge enters and exits the same function, assume this function for this edge only
        if (functionEntry != null
            && functionEntry.equals(functionExit)
            && (newStack.isEmpty() || !newStack.peek().equals(functionExit))) {
          newStack = new ArrayDeque<>(newStack);
        }

        // Never match on the dummy edge directly after the main function entry node
        AutomatonBoolExpr conjunctedTriggers = not(AutomatonBoolExpr.MatchProgramEntry.INSTANCE);
        // Never match on artificially split declarations
        conjunctedTriggers = and(conjunctedTriggers, not(AutomatonBoolExpr.MatchSplitDeclaration.INSTANCE));

        // Match a loop start
        boolean enterLoopHead = false;
        Set<String> loopHeadFlags =
            GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ENTERLOOPHEAD);
        if (!loopHeadFlags.isEmpty()) {
          Set<Boolean> loopHeadFlagValues =
              loopHeadFlags.stream().map(Boolean::parseBoolean).collect(Collectors.toSet());
          if (loopHeadFlagValues.size() > 1) {
            throw new WitnessParseException(
                "Conflicting values for the flag "
                    + KeyDef.ENTERLOOPHEAD
                    + ": "
                    + loopHeadFlags.toString());
          }
          if (loopHeadFlagValues.iterator().next()) {
            conjunctedTriggers =
                and(
                    conjunctedTriggers,
                    AutomatonBoolExpr.MatchLoopStart.INSTANCE);
            enterLoopHead = true;
          }
        }

        // Add assumptions to the transition
        Set<String> assumptionScopes = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ASSUMPTIONSCOPE);
        Scope scope = determineScope(assumptionScopes, newStack);
        Set<String> assumptionResultFunctions =
            GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ASSUMPTIONRESULTFUNCTION);
        Optional<String> assumptionResultFunction = determineResultFunction(assumptionResultFunctions, scope);
        if (considerAssumptions) {
          Set<String> transAssumes = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ASSUMPTION);
          assumptions.addAll(
              CParserUtils.convertStatementsToAssumptions(
                  parseStatements(transAssumes, assumptionResultFunction, scope, cparser), machine, logger));
          if (graphType == GraphType.PROOF_WITNESS && !assumptions.isEmpty()) {
            throw new WitnessParseException("Assumptions are not allowed for proof witnesses.");
          }
        }

        if (graphType == GraphType.ERROR_WITNESS && !assumptionResultFunctions.isEmpty()) {
          String resultFunctionName = assumptionResultFunction.get();
          conjunctedTriggers = and(conjunctedTriggers, new AutomatonBoolExpr.MatchFunctionCall(resultFunctionName));
        }

        Set<String> candidates =
            GraphMlDocumentData.getDataOnNode(targetStateNode, KeyDef.INVARIANT);
        Set<String> candidateScopes =
            GraphMlDocumentData.getDataOnNode(targetStateNode, KeyDef.INVARIANTSCOPE);
        final Scope candidateScope = determineScope(candidateScopes, newStack);
        Set<String> resultFunctions =
            GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ASSUMPTIONRESULTFUNCTION);
        Optional<String> resultFunction = determineResultFunction(resultFunctions, scope);
        if (!candidates.isEmpty()) {
          if (graphType == GraphType.ERROR_WITNESS) {
            throw new WitnessParseException("Invariants are not allowed for error witnesses.");
          }
          candidateInvariants =
              And.of(
                  candidateInvariants,
                  parseStatementsAsExpressionTree(
                      candidates, resultFunction, candidateScope, cparser));
        }

        if (matchOriginLine) {
          Set<String> originFileTags = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ORIGINFILE);
          checkParsable(
              originFileTags.size() < 2,
              "At most one origin-file data tag must be provided for an edge.");

          Set<String> originLineTags = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ORIGINLINE);
          checkParsable(
              originLineTags.size() < 2,
              "At most one origin-line data tag must be provided for each edge.");

          int matchOriginLineNumber = -1;
          if (originLineTags.size() > 0) {
            matchOriginLineNumber = Integer.parseInt(originLineTags.iterator().next());
          }
          if (matchOriginLineNumber > 0) {
            Optional<String> matchOriginFileName = originFileTags.isEmpty() ? Optional.empty() : Optional.of(originFileTags.iterator().next());
            OriginLineMatcher originDescriptor =
                new OriginLineMatcher(matchOriginFileName, matchOriginLineNumber);

            AutomatonBoolExpr startingLineMatchingExpr = new AutomatonBoolExpr.MatchLocationDescriptor(originDescriptor);
            if (enterLoopHead) {
              startingLineMatchingExpr = AutomatonBoolExpr.EpsilonMatch.backwardEpsilonMatch(startingLineMatchingExpr, true);
            }
            conjunctedTriggers = and(conjunctedTriggers, startingLineMatchingExpr);
          }

        }

        if (matchOffset) {
          Set<String> originFileTags = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.ORIGINFILE);
          checkParsable(
              originFileTags.size() < 2,
              "At most one origin-file data tag must be provided for an edge.");

          Set<String> offsetTags = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.OFFSET);
          checkParsable(
              offsetTags.size() < 2, "At most one offset data tag must be provided for each edge.");

          int offset = -1;
          if (offsetTags.size() > 0) {
            offset = Integer.parseInt(offsetTags.iterator().next());
          }

          if (offset >= 0) {
            Optional<String> matchOriginFileName = originFileTags.isEmpty() ? Optional.empty() : Optional.of(originFileTags.iterator().next());
            OffsetMatcher originDescriptor = new OffsetMatcher(matchOriginFileName, offset);

            AutomatonBoolExpr offsetMatchingExpr = new AutomatonBoolExpr.MatchLocationDescriptor(originDescriptor);
            if (enterLoopHead) {
              offsetMatchingExpr = AutomatonBoolExpr.EpsilonMatch.backwardEpsilonMatch(offsetMatchingExpr, true);
            }
            conjunctedTriggers = and(conjunctedTriggers, offsetMatchingExpr);
          }

        }

        if (matchSourcecodeData) {
          Set<String> sourceCodeDataTags = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.SOURCECODE);
          checkParsable(
              sourceCodeDataTags.size() < 2, "At most one source-code data tag must be provided.");
          final String sourceCode;
          if (sourceCodeDataTags.isEmpty()) {
            sourceCode = "";
          } else {
            sourceCode = sourceCodeDataTags.iterator().next();
          }
          final AutomatonBoolExpr exactEdgeMatch = new AutomatonBoolExpr.MatchCFAEdgeExact(sourceCode);
          conjunctedTriggers = and(conjunctedTriggers, exactEdgeMatch);
        }

        // If the triggers do not apply, none of the above transitions is taken,
        // so we need to build the stutter condition
        // as the conjoined negations of the transition conditions.
        AutomatonBoolExpr stutterCondition = stutterConditions.get(sourceStateId);
        if (stutterCondition == null) {
          stutterCondition = not(conjunctedTriggers);
        } else {
          stutterCondition = and(stutterCondition, not(conjunctedTriggers));
        }
        stutterConditions.put(sourceStateId, stutterCondition);

        if (matchAssumeCase) {
          Set<String> assumeCaseTags = GraphMlDocumentData.getDataOnNode(stateTransitionEdge, KeyDef.CONTROLCASE);

          if (assumeCaseTags.size() > 0) {
            checkParsable(
                assumeCaseTags.size() < 2,
                "At most one assume case tag must be provided for each edge.");
            String assumeCaseStr = assumeCaseTags.iterator().next();
            final boolean assumeCase;
            if (assumeCaseStr.equalsIgnoreCase(AssumeCase.THEN.toString())) {
              assumeCase = true;
            } else if (assumeCaseStr.equalsIgnoreCase(AssumeCase.ELSE.toString())) {
              assumeCase = false;
            } else {
              throw new WitnessParseException("Unrecognized assume case: " + assumeCaseStr);
            }

            AutomatonBoolExpr assumeCaseMatchingExpr =
                new AutomatonBoolExpr.MatchAssumeCase(assumeCase);
            if (enterLoopHead) {
              assumeCaseMatchingExpr = AutomatonBoolExpr.EpsilonMatch.backwardEpsilonMatch(assumeCaseMatchingExpr, true);
            }

            conjunctedTriggers = and(conjunctedTriggers, assumeCaseMatchingExpr);
          }
        }



        // If the triggers match, there must be one successor state that moves the automaton forwards
        transitions.add(
            createAutomatonTransition(
                conjunctedTriggers,
                Collections.<AutomatonBoolExpr>emptyList(),
                assumptions,
                candidateInvariants,
                actions,
                targetStateId,
                leadsToViolationNode,
                sinkStates));

        // Multiple CFA edges in a sequence might match the triggers,
        // so in that case we ALSO need a transition back to the source state
        if (strictMatching || !assumptions.isEmpty() || !actions.isEmpty() || !candidateInvariants.equals(ExpressionTrees.getTrue()) || leadsToViolationNode) {
          Element sourceNode = docDat.getNodeWithId(sourceStateId);
          Set<NodeFlag> sourceNodeFlags = docDat.getNodeFlags(sourceNode);
          boolean sourceIsViolationNode = sourceNodeFlags.contains(NodeFlag.ISVIOLATION);
          transitions.add(
              createAutomatonTransition(
                  and(
                      conjunctedTriggers,
                      new AutomatonBoolExpr.MatchAnySuccessorEdgesBoolExpr(conjunctedTriggers)),
                  Collections.<AutomatonBoolExpr>emptyList(),
                  Collections.emptyList(),
                  ExpressionTrees.<AExpression>getTrue(),
                  Collections.<AutomatonAction>emptyList(),
                  sourceStateId,
                  sourceIsViolationNode,
                  sinkStates));
        }
      }

      // Create states ----
      List<AutomatonInternalState> automatonStates = Lists.newArrayList();
      for (Map.Entry<String, Element> stateEntry : docDat.getIdToNodeMap().entrySet()) {
        String stateId = stateEntry.getKey();
        Element stateNode = stateEntry.getValue();
        EnumSet<NodeFlag> nodeFlags = docDat.getNodeFlags(stateNode);

        List<AutomatonTransition> transitions = stateTransitions.get(stateId);
        if (transitions == null) {
          transitions = new ArrayList<>();
        }

        // If the transition conditions do not apply, none of the above transitions is taken,
        // and instead, the stutter condition applies.
        AutomatonBoolExpr stutterCondition = stutterConditions.get(stateId);
        if (stutterCondition == null) {
          stutterCondition = AutomatonBoolExpr.TRUE;
        }
        if (graphType == GraphType.ERROR_WITNESS && strictMatching) {
          // If we are doing strict matching, anything that does not match must go to the sink
          transitions.add(
              createAutomatonSinkTransition(
                  stutterCondition,
                  Collections.<AutomatonBoolExpr>emptyList(),
                  Collections.<AutomatonAction>emptyList(),
                  false));

        } else {
          // If we are more lenient, we just wait in the source state until the witness checker catches up with the witness,
          transitions.add(
              createAutomatonTransition(
                  stutterCondition,
                  Collections.<AutomatonBoolExpr>emptyList(),
                  Collections.emptyList(),
                  ExpressionTrees.<AExpression>getTrue(),
                  Collections.<AutomatonAction>emptyList(),
                  stateId,
                  violationStates.contains(stateId),
                  sinkStates));
        }

        if (nodeFlags.contains(NodeFlag.ISVIOLATION)) {
          AutomatonBoolExpr otherAutomataSafe = createViolationAssertion();
          List<AutomatonBoolExpr> assertions = Collections.singletonList(otherAutomataSafe);
          transitions.add(
              createAutomatonTransition(
                  AutomatonBoolExpr.TRUE,
                  assertions,
                  Collections.emptyList(),
                  ExpressionTrees.<AExpression>getTrue(),
                  Collections.<AutomatonAction>emptyList(),
                  stateId,
                  true,
                  sinkStates));
        }

        if (nodeFlags.contains(NodeFlag.ISENTRY)) {
          checkParsable(initialStateName == null, "Only one entrynode is supported!");
          initialStateName = stateId;
        }

        // Determine if "matchAll" should be enabled
        boolean matchAll = true;

        AutomatonInternalState state = new AutomatonInternalState(stateId, transitions, false, matchAll);
        automatonStates.add(state);
      }

      // Build and return the result
      Preconditions.checkNotNull(initialStateName, "Every graph needs a specified entry node!");
      Map<String, AutomatonVariable> automatonVariables;
      if (graphType == GraphType.ERROR_WITNESS) {
        AutomatonVariable distanceVariable = new AutomatonVariable("int", DISTANCE_TO_VIOLATION);
        Integer initialStateDistance = distances.get(initialStateName);
        if (initialStateDistance != null) {
          distanceVariable.setValue(-initialStateDistance);
        } else {
          logger.log(
              Level.WARNING,
              String.format(
                  "There is no path from the entry state %s"
                      + " to a state explicitly marked as violation state."
                      + " Distance-to-violation waitlist order will not work"
                      + " and witness validation may fail to confirm this witness.",
                  initialStateName));
        }
        automatonVariables = Collections.singletonMap(DISTANCE_TO_VIOLATION, distanceVariable);
      } else {
        automatonVariables = Collections.emptyMap();
      }
      List<Automaton> result = Lists.newArrayList();
      Automaton automaton = new Automaton(automatonName, automatonVariables, automatonStates, initialStateName);
      result.add(automaton);

      if (automatonDumpFile != null) {
        try (Writer w = MoreFiles.openOutputFile(automatonDumpFile, Charset.defaultCharset())) {
          automaton.writeDotFile(w);
        } catch (IOException e) {
          // logger.logUserException(Level.WARNING, e, "Could not write the automaton to DOT file");
        }
      }

      return result;

    } catch (ParserConfigurationException | SAXException e) {
      throw new InvalidConfigurationException("Error while accessing automaton file!", e);
    } catch (InvalidAutomatonException e) {
      throw new InvalidConfigurationException("The automaton provided is invalid!", e);
    } catch (CParserException e) {
      throw new InvalidConfigurationException("The automaton contains invalid C code!", e);
    }
  }

  private Optional<String> determineResultFunction(Set<String> pResultFunctions, Scope pScope) {
    checkParsable(
        pResultFunctions.size() <= 1,
        "At most one result function must be provided for a transition.");
    if (!pResultFunctions.isEmpty()) {
      return Optional.of(pResultFunctions.iterator().next());
    }
    if (pScope instanceof CProgramScope) {
      CProgramScope scope = (CProgramScope) pScope;
      if (!scope.isGlobalScope()) {
        return Optional.of(scope.getCurrentFunctionName());
      }
    }
    return Optional.empty();
  }

  private Scope determineScope(Set<String> pScopes, Deque<String> pFunctionStack) {
    checkParsable(pScopes.size() <= 1, "At most one scope must be provided for a transition.");
    Scope result = this.scope;
    if (result instanceof CProgramScope && (!pScopes.isEmpty() || !pFunctionStack.isEmpty())) {
      final String functionName;
      if (!pScopes.isEmpty()) {
        functionName = pScopes.iterator().next();
      } else {
        functionName = pFunctionStack.peek();
      }
      result = ((CProgramScope) result).createFunctionScope(functionName);
    }
    return result;
  }

  private Collection<CStatement> parseStatements(
      Set<String> pStatements, Optional<String> pResultFunction, Scope pScope, CParser pCParser)
      throws CParserException, InvalidAutomatonException {
    if (!pStatements.isEmpty()) {

      Set<CStatement> result = new HashSet<>();
      for (String assumeCode : pStatements) {
        Collection<CStatement> statements =
            removeDuplicates(
                adjustCharAssignments(
                    parseAsCStatements(assumeCode, pResultFunction, pScope, pCParser)));
        result.addAll(statements);
      }
      return result;
    }
    return Collections.emptySet();
  }

  private Collection<CStatement> parseAsCStatements(
      String pCode, Optional<String> pResultFunction, Scope pScope, CParser pCParser)
      throws CParserException, InvalidAutomatonException {
    Collection<CStatement> result = new HashSet<>();
    boolean fallBack = false;
    ExpressionTree<AExpression> tree = parseStatement(pCode, pResultFunction, pScope, pCParser);
    if (!tree.equals(ExpressionTrees.getTrue())) {
      if (tree.equals(ExpressionTrees.getFalse())) {
        return Collections.<CStatement>singleton(
            new CExpressionStatement(
                FileLocation.DUMMY,
                new CIntegerLiteralExpression(
                    FileLocation.DUMMY, CNumericTypes.INT, BigInteger.ZERO)));
      }
      if (ExpressionTrees.isAnd(tree)) {
        for (ExpressionTree<AExpression> child : ExpressionTrees.getChildren(tree)) {
          if (child instanceof LeafExpression) {
            AExpression expression = ((LeafExpression<AExpression>) child).getExpression();
            if (expression instanceof CExpression) {
              result.add(new CExpressionStatement(FileLocation.DUMMY, (CExpression) expression));
            } else {
              fallBack = true;
            }
          } else {
            fallBack = true;
          }
        }
      } else {
        fallBack = true;
      }
    }
    if (fallBack) {
      return CParserUtils.parseListOfStatements(
          tryFixACSL(tryFixArrayInitializers(pCode), pResultFunction, pScope), pCParser, pScope);
    }
    return result;
  }

  private ExpressionTree<AExpression> parseStatementsAsExpressionTree(
      Set<String> pStatements, Optional<String> pResultFunction, Scope pScope, CParser pCParser)
      throws InvalidAutomatonException {
    ExpressionTree<AExpression> result = ExpressionTrees.getTrue();
    for (String assumeCode : pStatements) {
      try {
        ExpressionTree<AExpression> expressionTree =
            parseStatement(assumeCode, pResultFunction, pScope, pCParser);
        result = And.of(result, expressionTree);
      } catch (CParserException e) {
        logger.log(Level.WARNING, "Cannot parse code: " + assumeCode);
      }
    }
    return result;
  }

  private ExpressionTree<AExpression> parseStatement(
      String pAssumeCode, Optional<String> pResultFunction, Scope pScope, CParser pCParser)
      throws CParserException, InvalidAutomatonException {

    // Try the old method first; it works for simple expressions
    // and also supports assignment statements and multiple statements easily
    String assumeCode = tryFixArrayInitializers(pAssumeCode);
    Collection<CStatement> statements = null;
    try {
      statements = CParserUtils.parseListOfStatements(assumeCode, pCParser, pScope);
    } catch (RuntimeException e) {
      if (e.getMessage() != null && e.getMessage().contains("Syntax error:")) {
        statements =
            CParserUtils.parseListOfStatements(
                tryFixACSL(assumeCode, pResultFunction, pScope), pCParser, pScope);
      } else {
        throw e;
      }
    } catch (CParserException e) {
      statements =
          CParserUtils.parseListOfStatements(
              tryFixACSL(assumeCode, pResultFunction, pScope), pCParser, pScope);
    }
    statements = removeDuplicates(adjustCharAssignments(statements));
    // Check that no expressions were split
    if (!FluentIterable.from(statements)
        .anyMatch(statement -> statement.toString().toUpperCase().contains("__CPACHECKER_TMP"))) {
      return And.of(FluentIterable.from(statements).transform(fromStatement));
    }

    // For complex expressions, assume we are dealing with expression statements
    ExpressionTree<AExpression> result = ExpressionTrees.getTrue();
    try {
      result = parseExpression(pAssumeCode, pResultFunction, pScope, pCParser);
    } catch (CParserException e) {
      // Try splitting on ';' to support legacy code:
      Splitter semicolonSplitter = Splitter.on(';').omitEmptyStrings().trimResults();
      List<String> clausesStrings = semicolonSplitter.splitToList(pAssumeCode);
      if (clausesStrings.isEmpty()) {
        throw e;
      }
      List<ExpressionTree<AExpression>> clauses = new ArrayList<>(clausesStrings.size());
      for (String statement : clausesStrings) {
        clauses.add(parseExpression(statement, pResultFunction, pScope, pCParser));
      }
      result = And.of(clauses);
    }
    return result;
  }

  private String tryFixACSL(String pAssumeCode, Optional<String> pResultFunction, Scope pScope) {
    String assumeCode = pAssumeCode.trim();
    if (assumeCode.endsWith(";")) {
      assumeCode = assumeCode.substring(0, assumeCode.length() - 1);
    }

    assumeCode = replaceResultVar(pResultFunction, pScope, assumeCode);

    Splitter splitter = Splitter.on("==>").limit(2);
    while (assumeCode.contains("==>")) {
      Iterator<String> partIterator = splitter.split(assumeCode).iterator();
      assumeCode =
          String.format("!(%s) || (%s)", partIterator.next().trim(), partIterator.next().trim());
    }
    return assumeCode;
  }

  private String replaceResultVar(
      Optional<String> pResultFunction, Scope pScope, String assumeCode) {
    if (pResultFunction.isPresent() && pScope instanceof CProgramScope) {
      CProgramScope scope = (CProgramScope) pScope;
      String resultFunctionName = pResultFunction.get();
      if (scope.hasFunctionReturnVariable(resultFunctionName)) {
        CSimpleDeclaration functionReturnVariable =
            scope.getFunctionReturnVariable(resultFunctionName);
        return assumeCode.replace("\\result", " " + functionReturnVariable.getName());
      }
    }
    return assumeCode.replace("\\result", " ___CPAchecker_foo() ");
  }

  private ExpressionTree<AExpression> parseExpression(
      String pAssumeCode, Optional<String> pResultFunction, Scope pScope, CParser pCParser)
      throws CParserException {
    String assumeCode = pAssumeCode.trim();
    while (assumeCode.endsWith(";")) {
      assumeCode = assumeCode.substring(0, assumeCode.length() - 1).trim();
    }
    String formatString = "int test() { if (%s) { return 1; } else { return 0; } ; }";
    String testCode = String.format(formatString, assumeCode);

    ParseResult parseResult;
    try {
      parseResult = pCParser.parseString("", testCode, new CSourceOriginMapping(), pScope);
    } catch (ParserException e) {
      assumeCode = tryFixACSL(assumeCode, pResultFunction, pScope);
      testCode = String.format(formatString, assumeCode);
      parseResult = pCParser.parseString("", testCode, new CSourceOriginMapping(), pScope);
    }
    FunctionEntryNode entryNode = parseResult.getFunctions().values().iterator().next();

    return asExpressionTree(entryNode);
  }

  private ExpressionTree<AExpression> asExpressionTree(FunctionEntryNode pEntry) {
    Map<CFANode, ExpressionTree<AExpression>> memo = Maps.newHashMap();
    memo.put(pEntry, ExpressionTrees.<AExpression>getTrue());
    Set<CFANode> ready = new HashSet<>();
    ready.add(pEntry);
    Queue<CFANode> waitlist = new ArrayDeque<>();
    waitlist.offer(pEntry);
    while (!waitlist.isEmpty()) {
      CFANode current = waitlist.poll();

      // Current tree is already complete in this location
      ExpressionTree<AExpression> currentTree = memo.get(current);

      // Compute successor trees
      for (CFAEdge leavingEdge : CFAUtils.leavingEdges(current)) {

        CFANode succ = leavingEdge.getSuccessor();

        // Get the tree currently stored for the successor
        ExpressionTree<AExpression> succTree = memo.get(succ);
        if (succTree == null) {
          succTree = ExpressionTrees.getFalse();
        }

        // Now, build the disjunction of the old tree with the new path

        // Handle the return statement: Returning 0 means false, 1 means true
        if (leavingEdge instanceof AReturnStatementEdge) {
          AReturnStatementEdge returnStatementEdge = (AReturnStatementEdge) leavingEdge;
          Optional<? extends AExpression> optExpression = returnStatementEdge.getExpression();
          assert optExpression.isPresent();
          if (!optExpression.isPresent()) {
            return ExpressionTrees.getTrue();
          }
          AExpression expression = optExpression.get();
          if (!(expression instanceof AIntegerLiteralExpression)) {
            return ExpressionTrees.getTrue();
          }
          AIntegerLiteralExpression literal = (AIntegerLiteralExpression) expression;
          // If the value is zero, the current path is 'false', so we do not add it.
          // If the value is one, we add the current path
          if (!literal.getValue().equals(BigInteger.ZERO)) {
            succTree = factory.or(succTree, currentTree);
          }

          // Handle assume edges
        } else if (leavingEdge instanceof AssumeEdge) {
          AssumeEdge assumeEdge = (AssumeEdge) leavingEdge;
          AExpression expression = assumeEdge.getExpression();

          if (expression.toString().contains("__CPAchecker_TMP")) {
            for (CFAEdge enteringEdge : CFAUtils.enteringEdges(current)) {
              Map<AExpression, AExpression> tmpVariableValues =
                  collectCPAcheckerTMPValues(enteringEdge);
              if (!tmpVariableValues.isEmpty()) {
                expression =
                    replaceCPAcheckerTMPVariables(assumeEdge.getExpression(), tmpVariableValues);
              }
              final ExpressionTree<AExpression> newPath;
              if (assumeEdge.getTruthAssumption()
                  && !expression.toString().contains("__CPAchecker_TMP")) {
                newPath =
                    factory.and(
                        currentTree, factory.leaf(expression, assumeEdge.getTruthAssumption()));
              } else {
                newPath = currentTree;
              }
              succTree = factory.or(succTree, newPath);
            }
          } else {
            final ExpressionTree<AExpression> newPath;
            if (assumeEdge.getTruthAssumption()) {
              newPath =
                  factory.and(
                      currentTree, factory.leaf(expression, assumeEdge.getTruthAssumption()));
            } else {
              newPath = currentTree;
            }
            succTree = factory.or(succTree, newPath);
          }
          // All other edges do not change the path
        } else {
          succTree = factory.or(succTree, currentTree);
        }

        memo.put(succ, succTree);
      }

      // Prepare successors
      for (CFANode successor : CFAUtils.successorsOf(current)) {
        if (CFAUtils.predecessorsOf(successor).allMatch(Predicates.in(ready))
            && ready.add(successor)) {
          waitlist.offer(successor);
        }
      }
    }
    return simplifier.simplify(memo.get(pEntry.getExitNode()));
  }

  private AExpression replaceCPAcheckerTMPVariables(
      AExpression pExpression, Map<AExpression, AExpression> pTmpValues) {
    // Short cut if there cannot be any matches
    if (pTmpValues.isEmpty()) {
      return pExpression;
    }
    AExpression directMatch = pTmpValues.get(pExpression);
    if (directMatch != null) {
      return directMatch;
    }
    if (pExpression instanceof CBinaryExpression) {
      CBinaryExpression binaryExpression = (CBinaryExpression) pExpression;
      CExpression op1 =
          (CExpression) replaceCPAcheckerTMPVariables(binaryExpression.getOperand1(), pTmpValues);
      CExpression op2 =
          (CExpression) replaceCPAcheckerTMPVariables(binaryExpression.getOperand2(), pTmpValues);
      return new CBinaryExpression(
          binaryExpression.getFileLocation(),
          binaryExpression.getExpressionType(),
          binaryExpression.getCalculationType(),
          op1,
          op2,
          binaryExpression.getOperator());
    }
    if (pExpression instanceof CUnaryExpression) {
      CUnaryExpression unaryExpression = (CUnaryExpression) pExpression;
      CExpression op =
          (CExpression) replaceCPAcheckerTMPVariables(unaryExpression.getOperand(), pTmpValues);
      return new CUnaryExpression(
          unaryExpression.getFileLocation(),
          unaryExpression.getExpressionType(),
          op,
          unaryExpression.getOperator());
    }
    return pExpression;
  }

  private Map<AExpression, AExpression> collectCPAcheckerTMPValues(CFAEdge pEdge) {

    if (pEdge instanceof AStatementEdge) {
      AStatement statement = ((AStatementEdge) pEdge).getStatement();
      if (statement instanceof AExpressionAssignmentStatement) {
        AExpressionAssignmentStatement expAssignStmt = (AExpressionAssignmentStatement) statement;
        ALeftHandSide lhs = expAssignStmt.getLeftHandSide();
        if (lhs instanceof AIdExpression
            && ((AIdExpression) lhs).getName().contains("__CPAchecker_TMP")) {
          AExpression rhs = expAssignStmt.getRightHandSide();
          return Collections.<AExpression, AExpression>singletonMap(lhs, rhs);
        }
      }
    }

    return Collections.emptyMap();
  }

  private static AutomatonBoolExpr createViolationAssertion() {
    return and(
        not(new AutomatonBoolExpr.ALLCPAQuery(AutomatonState.INTERNAL_STATE_IS_TARGET_PROPERTY))
        );
  }

  private static AutomatonTransition createAutomatonTransition(
      AutomatonBoolExpr pTriggers,
      List<AutomatonBoolExpr> pAssertions,
      List<AExpression> pAssumptions,
      ExpressionTree<AExpression> pCandidateInvariants,
      List<AutomatonAction> pActions,
      String pTargetStateId,
      boolean pLeadsToViolationNode,
      Set<String> pSinkNodeIds) {
    if (pSinkNodeIds.contains(pTargetStateId)) {
      return createAutomatonSinkTransition(pTriggers, pAssertions, pActions, pLeadsToViolationNode);
    }
    if (pLeadsToViolationNode) {
      List<AutomatonBoolExpr> assertions = ImmutableList.<AutomatonBoolExpr>builder().addAll(pAssertions).add(createViolationAssertion()).build();
      return new ViolationCopyingAutomatonTransition(
          pTriggers, assertions, pAssumptions, pCandidateInvariants, pActions, pTargetStateId);
    }
    return new AutomatonTransition(
        pTriggers, pAssertions, pAssumptions, pCandidateInvariants, pActions, pTargetStateId);
  }

  private static AutomatonTransition createAutomatonSinkTransition(
      AutomatonBoolExpr pTriggers,
      List<AutomatonBoolExpr> pAssertions,
      List<AutomatonAction> pActions,
      boolean pLeadsToViolationNode) {
    if (pLeadsToViolationNode) {
      return new ViolationCopyingAutomatonTransition(
          pTriggers,
          pAssertions,
          pActions,
          AutomatonInternalState.BOTTOM);
    }
    return new AutomatonTransition(
        pTriggers,
        pAssertions,
        pActions,
        AutomatonInternalState.BOTTOM);
  }

  private static class ViolationCopyingAutomatonTransition extends AutomatonTransition {

    private ViolationCopyingAutomatonTransition(
        AutomatonBoolExpr pTriggers,
        List<AutomatonBoolExpr> pAssertions,
        List<AExpression> pAssumptions,
        ExpressionTree<AExpression> pCandidateInvariants,
        List<AutomatonAction> pActions,
        String pTargetStateId) {
      super(pTriggers, pAssertions, pAssumptions, pCandidateInvariants, pActions, pTargetStateId);
    }

    private ViolationCopyingAutomatonTransition(
        AutomatonBoolExpr pTriggers,
        List<AutomatonBoolExpr> pAssertions,
        List<AutomatonAction> pActions,
        AutomatonInternalState pTargetState) {
      super(pTriggers, pAssertions, pActions, pTargetState);
    }

    @Override
    public String getViolatedPropertyDescription(AutomatonExpressionArguments pArgs) {
      String own = getFollowState().isTarget() ? super.getViolatedPropertyDescription(pArgs) : null;
      List<String> violatedPropertyDescriptions = new ArrayList<>();

      if (!Strings.isNullOrEmpty(own)) {
        violatedPropertyDescriptions.add(own);
      }

      for (AutomatonState other : FluentIterable.from(pArgs.getAbstractStates()).filter(AutomatonState.class)) {
        if (other != pArgs.getState() && other.getInternalState().isTarget()) {
          String violatedPropDesc = "";

          Optional<AutomatonSafetyProperty> violatedProperty = other.getOptionalViolatedPropertyDescription();
          if (violatedProperty.isPresent()) {
            violatedPropDesc = violatedProperty.get().toString();
          }

          if (!violatedPropDesc.isEmpty()) {
            violatedPropertyDescriptions.add(violatedPropDesc);
          }
        }
      }

      if (violatedPropertyDescriptions.isEmpty() && own == null) {
        return null;
      }

      return Joiner.on(',').join(violatedPropertyDescriptions);
    }

  }

  /**
   * Some tools put assumptions for multiple statements on the same edge, which
   * may lead to contradictions between the assumptions.
   *
   * This is clearly a tool error, but for the competition we want to help them
   * out and only use the last assumption.
   *
   * @param pStatements the assumptions.
   *
   * @return the duplicate-free assumptions.
   */
  private static Collection<CStatement> removeDuplicates(Iterable<? extends CStatement> pStatements) {
    Map<Object, CStatement> result = new HashMap<>();
    for (CStatement statement : pStatements) {
      if (statement instanceof CExpressionAssignmentStatement) {
        CExpressionAssignmentStatement assignmentStatement = (CExpressionAssignmentStatement) statement;
        result.put(assignmentStatement.getLeftHandSide(), assignmentStatement);
      } else {
        result.put(statement, statement);
      }
    }
    return result.values();
  }

  /**
   * Be nice to tools that assume that default char (when it is neither
   * specified as signed nor as unsigned) may be unsigned.
   *
   * @param pStatements the assignment statements.
   *
   * @return the adjusted statements.
   */
  private static Collection<CStatement> adjustCharAssignments(Iterable<? extends CStatement> pStatements) {
    return FluentIterable.from(pStatements).transform(new Function<CStatement, CStatement>() {

      @Override
      public CStatement apply(CStatement pStatement) {
        if (pStatement instanceof CExpressionAssignmentStatement) {
          CExpressionAssignmentStatement statement = (CExpressionAssignmentStatement) pStatement;
          CLeftHandSide leftHandSide = statement.getLeftHandSide();
          CType canonicalType = leftHandSide.getExpressionType().getCanonicalType();
          if (canonicalType instanceof CSimpleType) {
            CSimpleType simpleType = (CSimpleType) canonicalType;
            CBasicType basicType = simpleType.getType();
            if (basicType.equals(CBasicType.CHAR) && !simpleType.isSigned() && !simpleType.isUnsigned()) {
              CExpression rightHandSide = statement.getRightHandSide();
              CExpression castedRightHandSide = new CCastExpression(rightHandSide.getFileLocation(), canonicalType, rightHandSide);
              return new CExpressionAssignmentStatement(statement.getFileLocation(), leftHandSide, castedRightHandSide);
            }
          }
        }
        return pStatement;
      }

    }).toList();
  }

  /**
   * Let's be nice to tools that ignore the restriction that array initializers
   * are not allowed as right-hand sides of assignment statements and try to
   * help them. This is a hack, no good solution.
   * We would need a kind-of-but-not-really-C-parser to properly handle these
   * declarations-that-aren't-declarations.
   *
   * @param pAssumeCode the code from the witness assumption.
   *
   * @return the code from the witness assumption if no supported array
   * initializer is contained; otherwise the fixed code.
   */
  private static String tryFixArrayInitializers(String pAssumeCode) {
    String C_INTEGER = "([\\+\\-])?(0[xX])?[0-9a-fA-F]+";
    String assumeCode = pAssumeCode.trim();
    if (assumeCode.endsWith(";")) {
      assumeCode = assumeCode.substring(0, assumeCode.length() - 1);
    }
    /*
     * This only covers the special case of one assignment statement using one
     * array of integers.
     */
    if (assumeCode.matches(".+=\\s*\\{\\s*(" + C_INTEGER + "\\s*(,\\s*" + C_INTEGER + "\\s*)*)?\\}\\s*")) {
      Iterable<String> assignmentParts = Splitter.on('=').trimResults().split(assumeCode);
      Iterator<String> assignmentPartIterator = assignmentParts.iterator();
      if (!assignmentPartIterator.hasNext()) {
        return pAssumeCode;
      }
      String leftHandSide = assignmentPartIterator.next();
      if (!assignmentPartIterator.hasNext()) {
        return pAssumeCode;
      }
      String rightHandSide = assignmentPartIterator.next().trim();
      if (assignmentPartIterator.hasNext()) {
        return pAssumeCode;
      }
      assert rightHandSide.startsWith("{") && rightHandSide.endsWith("}");
      rightHandSide = rightHandSide.substring(1, rightHandSide.length() - 1).trim();
      Iterable<String> elements = Splitter.on(',').trimResults().split(rightHandSide);
      StringBuilder resultBuilder = new StringBuilder();
      int index = 0;
      for (String element : elements) {
        resultBuilder.append(String.format("%s[%d] = %s; ", leftHandSide, index, element));
        ++index;
      }
      return resultBuilder.toString();
    }
    return pAssumeCode;
  }

  private static class GraphMlDocumentData {

    private final HashMap<String, Optional<String>> defaultDataValues = Maps.newHashMap();
    private final Document doc;

    private Map<String, Element> idToNodeMap = null;

    public GraphMlDocumentData(Document doc) {
      this.doc = doc;
    }

    public EnumSet<NodeFlag> getNodeFlags(Element pStateNode) {
      EnumSet<NodeFlag> result = EnumSet.noneOf(NodeFlag.class);

      NodeList dataChilds = pStateNode.getElementsByTagName(GraphMlTag.DATA.toString());

      for (int i=0; i<dataChilds.getLength(); i++) {
        Node dataChild = dataChilds.item(i);
        Node attribute = dataChild.getAttributes().getNamedItem("key");
        Preconditions.checkNotNull(attribute, "Every data element must have a key attribute!");
        String key = attribute.getTextContent();
        NodeFlag flag = NodeFlag.getNodeFlagByKey(key);
        if (flag != null) {
          result.add(flag);
        }
      }

      return result;
    }

    public Map<String, Element> getIdToNodeMap() {
      if (idToNodeMap != null) {
        return idToNodeMap;
      }

      idToNodeMap = Maps.newHashMap();

      NodeList nodes = doc.getElementsByTagName(GraphMlTag.NODE.toString());
      for (int i=0; i<nodes.getLength(); i++) {
        Element stateNode = (Element) nodes.item(i);
        String stateId = getNodeId(stateNode);

        idToNodeMap.put(stateId, stateNode);
      }

      return idToNodeMap;
    }

    private static String getAttributeValue(Node of, String attributeName, String exceptionMessage) {
      Node attribute = of.getAttributes().getNamedItem(attributeName);
      Preconditions.checkNotNull(attribute, exceptionMessage);
      return attribute.getTextContent();
    }

    private Optional<String> getDataDefault(KeyDef dataKey) {
      Optional<String> result = defaultDataValues.get(dataKey.id);
      if (result != null) {
        return result;
      }

      NodeList keyDefs = doc.getElementsByTagName(GraphMlTag.KEY.toString());
      for (int i=0; i<keyDefs.getLength(); i++) {
        Element keyDef = (Element) keyDefs.item(i);
        Node id = keyDef.getAttributes().getNamedItem("id");
        if (dataKey.id.equals(id.getTextContent())) {
          NodeList defaultTags = keyDef.getElementsByTagName(GraphMlTag.DEFAULT.toString());
          result = Optional.empty();
          if (defaultTags.getLength() > 0) {
            checkParsable(
                defaultTags.getLength() == 1,
                "There should not be multiple default tags for one key.");
            result = Optional.of(defaultTags.item(0).getTextContent());
          }
          defaultDataValues.put(dataKey.id, result);
          return result;
        }
      }
      return Optional.empty();
    }

    private static String getNodeId(Node stateNode) {
      return getAttributeValue(stateNode, "id", "Every state needs an ID!");
    }

    private Element getNodeWithId(String nodeId) {
      Element result = getIdToNodeMap().get(nodeId);
      Preconditions.checkNotNull(result, "Node not found. Id: " + nodeId);
      return result;
    }

    private String getDataValueWithDefault(Node dataOnNode, KeyDef dataKey, final String defaultValue) {
      Set<String> values = getDataOnNode(dataOnNode, dataKey);
      if (values.size() == 0) {
        Optional<String> dataDefault = getDataDefault(dataKey);
        if (dataDefault.isPresent()) {
          return dataDefault.get();
        } else {
          return defaultValue;
        }
      } else {
        return values.iterator().next();
      }
    }

    private static Set<String> getDataOnNode(Node node, final KeyDef dataKey) {
      Preconditions.checkNotNull(node);
      Preconditions.checkArgument(node.getNodeType() == Node.ELEMENT_NODE);

      Element nodeElement = (Element) node;
      Set<Node> dataNodes = findKeyedDataNode(nodeElement, dataKey);

      Set<String> result = Sets.newHashSet();
      for (Node n: dataNodes) {
        result.add(n.getTextContent());
      }

      return result;
    }

    private static Set<Node> findKeyedDataNode(Element of, final KeyDef dataKey) {
      Set<Node> result = Sets.newHashSet();
      Set<Node> alternative = null;
      NodeList dataChilds = of.getElementsByTagName(GraphMlTag.DATA.toString());
      for (int i=0; i<dataChilds.getLength(); i++) {
        Node dataChild = dataChilds.item(i);
        Node attribute = dataChild.getAttributes().getNamedItem("key");
        Preconditions.checkNotNull(attribute, "Every data element must have a key attribute!");
        String nodeKey = attribute.getTextContent();
        if (nodeKey.equals(dataKey.id)) {
          result.add(dataChild);
          alternative = null;
        }
        // Backwards-compatibility: type/graph-type
        if (alternative == null
            && result.isEmpty()
            && dataKey.equals(KeyDef.GRAPH_TYPE)
            && nodeKey.equals("type")) {
          alternative = Sets.newHashSet();
          alternative.add(dataChild);
        }
      }
      if (result.isEmpty() && alternative != null) {
        return alternative;
      }
      return result;
    }

  }

  public static boolean isGraphmlAutomaton(Path pPath, LogManager pLogger) throws InvalidConfigurationException {
    SAXParser saxParser;
    try {
      saxParser = SAXParserFactory.newInstance().newSAXParser();
    } catch (ParserConfigurationException | SAXException e) {
      pLogger.logException(
          Level.WARNING,
          e,
          "SAX parser configured incorrectly. Could not determine whether or not the path describes a graphml automaton.");
      return false;
    }
    DefaultHandler defaultHandler = new DefaultHandler();
    try {
      try (InputStream input = Files.newInputStream(pPath);
          GZIPInputStream zipInput = new GZIPInputStream(input)) {
        saxParser.parse(zipInput, defaultHandler);
      } catch (IOException e) {
        try (InputStream plainInput = Files.newInputStream(pPath)) {
          saxParser.parse(plainInput, defaultHandler);
        }
      }
      return true;
    } catch (FileNotFoundException e) {
      throw new InvalidConfigurationException(
          "Invalid automaton file provided! File not found: " + pPath);
    } catch (IOException e) {
      throw new InvalidConfigurationException("Error while accessing automaton file", e);
    } catch (SAXException e) {
      return false;
    }
  }

  private static AutomatonBoolExpr not(AutomatonBoolExpr pA) {
    if (pA.equals(AutomatonBoolExpr.TRUE)) {
      return AutomatonBoolExpr.FALSE;
    }
    if (pA.equals(AutomatonBoolExpr.FALSE)) {
      return AutomatonBoolExpr.TRUE;
    }
    return new AutomatonBoolExpr.Negation(pA);
  }

  private static AutomatonBoolExpr and(AutomatonBoolExpr pA, AutomatonBoolExpr pB) {
    if (pA.equals(AutomatonBoolExpr.TRUE) || pA.equals(AutomatonBoolExpr.FALSE)) {
      return pB;
    }
    if (pB.equals(AutomatonBoolExpr.TRUE) || pA.equals(AutomatonBoolExpr.FALSE)) {
      return pA;
    }
    return new AutomatonBoolExpr.And(pA, pB);
  }

  private static AutomatonBoolExpr and(AutomatonBoolExpr... pExpressions) {
    AutomatonBoolExpr result = AutomatonBoolExpr.TRUE;
    for (AutomatonBoolExpr e : pExpressions) {
      result = and(result, e);
    }
    return result;
  }

  private static void checkParsable(boolean pParsable, String pMessage) {
    if (!pParsable) {
      throw new WitnessParseException(pMessage);
    }
  }

  public static class WitnessParseException extends RuntimeException {

    private static final long serialVersionUID = -6357416712866877118L;

    public WitnessParseException(String pMessage) {
      super(pMessage);
    }

  }

}
