CPAchecker Report Generator
---------------------------

The report generator can be used to compile an interactive analysis report of a CPAchecker run.
The report is in HTML format and works best in Firefox,
mainly because of problems accessing local resources via Javascript in other browsers.
If the file is hosted on a server, Google Chrome/Chromium should also work with some quirks
(the focused SVG element does not scroll into view).


Generate a Report
---------------------

1. Make sure `dot` from the [graphviz](http://www.graphviz.org) package is on your `$PATH`.
   For Ubuntu: `sudo apt-get install graphviz`

2. It's best to clear the output directory of CPAchecker,
   so that old files won't get mixed up with the new files.

3. Run CPAchecker as usual (make sure to not use `-noout`):

4. After CPAchecker has completed, generate the report with
   `scripts/report-generator.py`.
   If you use non-default output file paths and names for CPAchecker,
   specify the path to the config with `--config`.
   If dot takes too much time for generating the ARG,
   press Ctrl+C to cancel this step
   (you will not be able to see the ARG in the browser in this case).

5. The report should automatically open in your default browser,
   otherwise open the file mentioned in the output of the script.


Quick Reference to use the report
---------------------------------

TODO: This description is somewhat outdated and needs to be updated
to reflect the improvements of the new report.

  - Left Panel: Error Path (if a bug is found)

     - Indentation reflects height of call stack.
     - Click nodes/edges to jump to the location in the `CFA`/`ARG`/Source (depending on the active tab).
     - Function Start, Function Return, and unlabeled edges are not displayed.
     - Use Start (or click a node/edge) and Prev/Next to walk along the error path.

  - Right Panel:

    - CFA Tab

      - The CFA has been cut into multiple images (one image per function).
         Change the displayed function by using the select box at the top.
      - Linear sequences of "normal" edges (StatementEdges, DeclarationEdges, and BlankEdges)
         are displayed as a node containing a table. The left column contains the node number
         of the predecessor of an edge. The right column contains the edge label.
         The successor can be found in the left column of the next row.
      - Special function call nodes and edges have been introduced, as the CFA is spread out over multiple images.
      - The error path is highlighted in red.
      - Click an edge (or the edge label) to jump to the location in the source code.

    - ARG Tab

      - The error path is highlighted in red.
      - Click an edge to jump to the location in the CFA.

    - Source Tab

      - Displays source code.

    - Log Tab

      - Displays the output of CPAchecker.


Known Problems
--------------

  - The CFA tab uses an `iframe` to load SVG images. The `errorpath` is highlighted dynamically using Javascript.
     Unfortunately, the `onload` event fires only when the first SVG is loaded (and does not fire later when the SVG is changed).
     To work around that, the `onload` event is no longer used. Instead, the script assumes that it takes at most 0.5s to
     load an SVG. If the report is placed on a server, this may no longer hold. As a result, the error path might not be highlighted.
     Workaround: When using Next and the displayed function changes, but the error path is not highlighted or the arrow is not displayed,
     press Prev followed by Next. This time the SVG will be most likely served out of the local cache.

  - Clicking edges leaving the introduced `multistatement` nodes does not jump to the correct location in the source code.
     All other edges in the `CFA` should be ok.

  - When an item is selected in the error path window on the left, only the active tab on the right is updated.
     Workaround: After changing the tab on the right, click the selected item in the error path window again.

  - As the `ARG` might be huge, it could take a couple of seconds when switching to the `ARG` tab.
     Also you might have to scroll to the right initially to find the root of the `ARG`. Alternatively, click an item in
     the error path window on the left.

  - The error path in the `ARG` sometimes misses a few edges (they stay black). Also not all edges are clickable
     (but the red ones seem to be always clickable).
     This error is not deterministic. Reloading the page seems to fix the coloring issue. I suspect the error has to do with the huge
     number of click events that are placed on the ARG. I tried to use event delegation instead, but it seems SVG events
     do not bubble up the way HTML events do. Stepping through the error path works perfectly though.

  - `graphviz` does not like to create the really big node in `main()`, that contains the global declarations (label to long?)
     and only includes the node number instead. I consider this a feature, as the relevant statements are listed in the
     error path window on the left and this keeps the graph smaller.
