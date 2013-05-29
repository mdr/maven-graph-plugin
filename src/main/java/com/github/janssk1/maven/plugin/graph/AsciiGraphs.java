package com.github.janssk1.maven.plugin.graph;

import java.util.Collection;

import com.github.janssk1.maven.plugin.graph.domain.ArtifactRevisionIdentifier;
import com.github.janssk1.maven.plugin.graph.graph.Edge;
import com.github.janssk1.maven.plugin.graph.graph.Graph;
import com.github.janssk1.maven.plugin.graph.graph.Vertex;
import com.github.mdr.ascii.java.GraphBuilder;
import com.github.mdr.ascii.java.GraphLayouter;

public class AsciiGraphs {

    public static String renderGraph(Graph graph, GraphLayouter<String> graphLayouter) {
        GraphBuilder<String> graphBuilder = new GraphBuilder<String>();
        Collection<Vertex> vertices = graph.getVertices();
        for (Vertex vertex : vertices) {
            graphBuilder.addVertex(label(vertex));
            for (Edge edge : vertex.getEdges())
                graphBuilder.addEdge(label(edge.from), label(edge.to));
        }
        return graphLayouter.layout(graphBuilder.build());
    }

    private static String label(Vertex vertex) {
        ArtifactRevisionIdentifier artifactIdentifier = vertex.getArtifactIdentifier();
        String artifactId = artifactIdentifier.getArtifactId();
        String groupId = artifactIdentifier.getGroupId();
        String version = artifactIdentifier.getVersion();
        return artifactId + "\n" + groupId + "\n" + version;
    }
}
