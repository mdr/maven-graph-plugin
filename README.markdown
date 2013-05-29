Plugin to display an ASCII-art dependency graph.

Usage
=====

You can run this without modifying your project with the following command:

    mvn com.github.mdr:maven-dependencygraph-plugin:0.0.1:graph

Larger graphs might need to be piped through a pager, e.g. `less -S`:

    mvn com.github.mdr:maven-dependencygraph-plugin:0.0.1:graph | less -S

You can also customise some of the drawing options:

    mvn -Dgraph.vertical -Dgraph.rounded com.github.mdr:maven-dependencygraph-plugin:0.0.1:graph | less -S

Options
=======

* `graph.unicode` -- if true (default), use Unicode box-drawing characters, else use ASCII only
* `graph.vertical` -- if true, layers flow top to bottom, else left to right
* `graph.doubleVertices` -- if true, use double line box characters for vertices (requires graph.unicode)
* `graph.rounded` -- if true, draw corners using rounded characters (requires graph.unicode)

The dependency graph calculation is derived from the existing maven-graph-plugin at:

* https://github.com/janssk1/maven-graph-plugin
