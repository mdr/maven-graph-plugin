package com.github.janssk1.maven.plugin.graph;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.janssk1.maven.plugin.graph.domain.ArtifactRevisionIdentifier;
import com.github.janssk1.maven.plugin.graph.graph.Graph;
import com.github.mdr.ascii.java.GraphLayouter;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProjectBuilder;

import java.util.List;

/**
 * Goal which generates a set of dependency graphs
 * 
 * @goal graph
 * @phase process-sources
 */
public class GraphMojo extends AbstractMojo {

    /**
     * A comma separated list of report definitions
     * 
     * @parameter expression="${graph.reports}"
     *            default-value="COMPILE"
     */

    private String reports;

    /**
     * @parameter expression="${graph.unicode}" default-value="true"
     */
    private boolean unicode;

    /**
     * @parameter expression="${graph.vertical}" default-value="false"
     */
    private boolean vertical;

    /**
     * @parameter expression="${graph.doubleVertices}" default-value="true"
     */
    private boolean doubleVertices;

    /**
     * @parameter expression="${graph.rounded}" default-value="false"
     */
    private boolean rounded;

    /**
     * @parameter expression="${graph.explicitAsciiBends}" default-value="false"
     */
    private boolean explicitAsciiBends;

    /**
     * @component
     * @required
     * @readonly
     */
    private MavenProjectBuilder mavenProjectBuilder;

    /**
     * @component
     * @required
     * @readonly
     */
    private ArtifactFactory artifactFactory;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.groupId}"
     * @required
     * @readonly
     */
    private String groupId;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.artifactId}"
     * @required
     * @readonly
     */
    private String artifactId;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.version}"
     * @required
     * @readonly
     */
    private String version;

    /**
     * Maven's local repository.
     * 
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    public void execute()
            throws MojoExecutionException {

        getLog().info("Using graph.reports=" + reports);
        List<DependencyOptions> reportDefinitions = DependencyOptions.parseReportDefinitions(reports);
        ArtifactResolver artifactResolver = new MavenArtifactResolver(getLog(), localRepository, this.artifactFactory, mavenProjectBuilder);
        for (DependencyOptions reportDefinition : reportDefinitions) {
            buildGraph(artifactResolver, reportDefinition);
        }
    }

    private void buildGraph(ArtifactResolver artifactResolver, DependencyOptions options) throws MojoExecutionException {
        GraphBuilder graphBuilder = new BreadthFirstGraphBuilder(getLog(), artifactResolver);
        Graph graph = graphBuilder.buildGraph(new ArtifactRevisionIdentifier(artifactId, groupId, version), options);
        getLog().info("Dependencies for " + options.getGraphType() + (options.isIncludeAllTransitiveDependencies() ? "-TRANSITIVE" : "") + ": ");
        getLog().info("");
        String diagram = AsciiGraphs.renderGraph(graph, graphLayouter());
        for (String line : diagram.split("\n"))
            getLog().info(line);
        getLog().info("");
    }

    private GraphLayouter<String> graphLayouter() {
        GraphLayouter<String> graphLayouter = new GraphLayouter<String>();
        graphLayouter.setUnicode(unicode);
        graphLayouter.setVertical(vertical);
        graphLayouter.setDoubleVertices(doubleVertices);
        graphLayouter.setExplicitAsciiBends(explicitAsciiBends);
        graphLayouter.setRounded(rounded);
        return graphLayouter;
    }
}
