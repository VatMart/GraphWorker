package root.components;

import root.GraphWorkerApplication;
import root.view.MainPane;
import root.model.Graph;
import root.view.GraphPane;

public class GraphLoader {
	
	private SavedGraph savedGraph;
	private Graph loadedGraph;
	private MainPane root;
	
	public GraphLoader(SavedGraph sGraph, MainPane root) {
		this.savedGraph = sGraph;
		this.root = root;
		loadedGraph = new Graph();
		loadedGraph.setGraphPane(new GraphPane());
	}
	
	public void loadGraph() {
		GraphWorkerApplication.getInstance().loadGraph(this);
		//root.changeGraph(loadedGraph);
		loadGraphProperties();
		loadGraphPaneProperties();
		loadVertexs();
		loadEdges();
	}

	private void loadEdges() {
		savedGraph.getEdges().forEach(name -> {
			Pair<Integer> vert = Graph.pairFactoryVertexes(name);
			loadedGraph.addEdge(vert.getFirst(), vert.getSecond(), savedGraph.getWeightsEdges().get(name), savedGraph.getOrientationsEdges().get(name));
			loadedGraph.getEdges().get(name).setEdgeStyle(savedGraph.getStylesEdges().get(name));
			loadedGraph.getEdges().get(name).setUserName(savedGraph.getTextEdges().get(name));
			loadedGraph.getEdges().get(name).setArrowStyle(savedGraph.getStylesArrowsEdges().get(name));
			loadedGraph.getEdges().get(name).setNameStyle(savedGraph.getStylesNamesEdges().get(name));
			loadedGraph.getEdges().get(name).shadowOnText(savedGraph.getIsShadowTextEdges().get(name));
			loadedGraph.getEdges().get(name).setSizeName(savedGraph.getSizeNamesEdges().get(name));
			loadedGraph.getEdges().get(name).setSizeArrow(savedGraph.getSizeArrowsEdges().get(name));
			loadedGraph.getEdges().get(name).setSizeWeight(savedGraph.getSizeWeightsEdges().get(name));
		});
		
	}

	private void loadVertexs() {
		savedGraph.getNumbersVertexs().forEach(num -> {
			loadedGraph.addVertex(num, savedGraph.getCoordinatesVertexs().get(num).getFirst(), savedGraph.getCoordinatesVertexs().get(num).getSecond());
			loadedGraph.getVertexes().get(num).setDefaultStyle(savedGraph.getStylesVertexs().get(num));
			loadedGraph.getVertexes().get(num).setChoosedStyle(savedGraph.getStylesChoosedVertexs().get(num));
			loadedGraph.getVertexes().get(num).setNameVertex(savedGraph.getNamesVertexs().get(num));
			loadedGraph.getVertexes().get(num).setTextStyle(savedGraph.getStylesNamesVertexs().get(num));
			loadedGraph.getVertexes().get(num).setDefaultRadius(savedGraph.getSizeVertexs().get(num));
			loadedGraph.getVertexes().get(num).setDefaultSizeText(savedGraph.getSizeTextVertexs().get(num));
			loadedGraph.getVertexes().get(num).shadowOnText(savedGraph.getIsShadowTextVertexs().get(num));
		});
	}

	private void loadGraphProperties() {
		loadedGraph.setDefaultStyleVertex(savedGraph.getDefaultStyleVertex());
		loadedGraph.setOrientation(savedGraph.getOrientationGraph());
	}

	private void loadGraphPaneProperties() {
		loadedGraph.getGraphPane().setWidthCanvas(savedGraph.getWidthGPane());
		loadedGraph.getGraphPane().setHeightCanvas(savedGraph.getHeightGPane());
		loadedGraph.getGraphPane().setStylePane(savedGraph.getStyleGPane());
	}

	public Graph getLoadedGraph() {
		return loadedGraph;
	}
}
