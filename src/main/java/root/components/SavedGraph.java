package root.components;

import root.model.Graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;


public class SavedGraph implements Serializable {
	private static final long serialVersionUID = 1L;

	private transient Graph graph;

	// root.model.Graph properties
	private String defaultStyleVertex;
	private Graph.OrientationGraph orientationGraph;

	// root.view.GraphPane properties
	private double widthGPane;
	private double heightGPane;
	private String styleGPane;

	// Vertexs
	private ArrayList<Integer> numbersVertexs;
	private TreeMap<Integer, Pair<Double>> coordinatesVertexs; // numberVertex, (x, y)
	private TreeMap<Integer, String> stylesVertexs;
	private TreeMap<Integer, String> stylesChoosedVertexs;
	private TreeMap<Integer, String> stylesNamesVertexs;
	private TreeMap<Integer, String> namesVertexs;
	private TreeMap<Integer, Double> sizeVertexs;
	private TreeMap<Integer, Double> sizeTextVertexs;
	private TreeMap<Integer, Boolean> isShadowTextVertexs;
	// Edges
	private ArrayList<String> edges;
	private HashMap<String, String> textEdges;
	private HashMap<String, Graph.OrientationGraph> orientationsEdges;
	private HashMap<String, Integer> weightsEdges;
	private HashMap<String, String> stylesEdges;
	private HashMap<String, String> stylesArrowsEdges;
	private HashMap<String, String> stylesNamesEdges;
	private HashMap<String, Boolean> isShadowTextEdges;
	private HashMap<String, Double> sizeNamesEdges;
	private HashMap<String, Double> sizeArrowsEdges;
	private HashMap<String, Double> sizeWeightsEdges;

	public SavedGraph(Graph graph) {
		this.graph = graph;
		initiateFields();
	}

	private void initiateFields() {
		initiateGraph();
		initiateGPane();
		initiateVertexs();
		initiateEdges();
	}

	private void initiateEdges() {
		edges = new ArrayList<String>(graph.getEdges().size());
		fillEdges();
		textEdges = new HashMap<String, String>(edges.size());
		orientationsEdges = new HashMap<String, Graph.OrientationGraph>(edges.size());
		weightsEdges = new HashMap<String, Integer>(edges.size());
		stylesEdges = new HashMap<String, String>(edges.size());
		stylesArrowsEdges = new HashMap<String, String>(edges.size());
		stylesNamesEdges = new HashMap<String, String>(edges.size());
		isShadowTextEdges = new HashMap<String, Boolean>(edges.size());
		sizeNamesEdges = new HashMap<String, Double>(edges.size());
		sizeArrowsEdges = new HashMap<String, Double>(edges.size());
		sizeWeightsEdges = new HashMap<String, Double>(edges.size());
		fillPropertiesEdges();
	}

	private void fillPropertiesEdges() {
		edges.forEach(name -> {
			orientationsEdges.put(name, graph.getEdges().get(name).getOrientationEdgeProperty().get());
			textEdges.put(name, graph.getEdges().get(name).getLabelName().getText());
			weightsEdges.put(name, graph.getEdges().get(name).getWeight());
			stylesEdges.put(name, graph.getEdges().get(name).getEdgeStyle());
			stylesArrowsEdges.put(name, graph.getEdges().get(name).getArrowStyle());
			stylesNamesEdges.put(name, graph.getEdges().get(name).getNameStyle());
			isShadowTextEdges.put(name, graph.getEdges().get(name).isShadowOn());
			sizeNamesEdges.put(name, graph.getEdges().get(name).getSizeName());
			sizeArrowsEdges.put(name, graph.getEdges().get(name).getSizeArrow());
			sizeWeightsEdges.put(name, graph.getEdges().get(name).getSizeWeight());
		});
		
	}

	private void fillEdges() {
		graph.getEdges().forEach((name, edge) -> {
			edges.add((edge.getVertexFirst().getNumber() + " " + edge.getVertexSecond().getNumber()));
		});
	}

	private void initiateVertexs() {
		numbersVertexs = new ArrayList<Integer>(graph.getVertexes().size());
		fillNumbersVertexs();
		coordinatesVertexs = new TreeMap<Integer, Pair<Double>>(Comparator.naturalOrder());
		stylesVertexs = new TreeMap<Integer, String>(Comparator.naturalOrder());
		stylesChoosedVertexs = new TreeMap<Integer, String>(Comparator.naturalOrder());
		stylesNamesVertexs =  new TreeMap<Integer, String>(Comparator.naturalOrder());
		namesVertexs = new TreeMap<Integer, String>(Comparator.naturalOrder());
		sizeVertexs = new TreeMap<Integer, Double>(Comparator.naturalOrder());
		sizeTextVertexs = new TreeMap<Integer, Double>(Comparator.naturalOrder());
		isShadowTextVertexs = new TreeMap<Integer, Boolean>(Comparator.naturalOrder());
		fillPropertiesVertexs();
	}

	private void fillPropertiesVertexs() {
		numbersVertexs.forEach(num -> {
			coordinatesVertexs.put(num, new Pair<Double>(graph.getVertexes().get(num).getCoordinates().getX(),
					graph.getVertexes().get(num).getCoordinates().getY()));
			namesVertexs.put(num, graph.getVertexes().get(num).getTextNumber().getText());
			stylesVertexs.put(num, graph.getVertexes().get(num).getDefaultStyle());
			stylesChoosedVertexs.put(num, graph.getVertexes().get(num).getChoosedStyleVertex());
			stylesNamesVertexs.put(num, graph.getVertexes().get(num).getTextStyle());
			sizeVertexs.put(num, graph.getVertexes().get(num).getDefaultRadius());
			sizeTextVertexs.put(num, graph.getVertexes().get(num).getDefaultSizeText());
			isShadowTextVertexs.put(num, graph.getVertexes().get(num).isShadowOn());
		});
		
	}

	private void fillNumbersVertexs() {
		graph.getVertexes().keySet().forEach(num -> {
			numbersVertexs.add(num);
		});
		Collections.sort(numbersVertexs);
	}

	private void initiateGPane() {
		this.widthGPane = graph.getGraphPane().getWidthPane();
		this.heightGPane = graph.getGraphPane().getHeightPane();
		this.styleGPane = graph.getGraphPane().getStylePane();
	}

	private void initiateGraph() {
		this.defaultStyleVertex = graph.getDefaultStyleVertex();
		this.orientationGraph = graph.getOrientationProperty().get();
	}
	
	@Override
	public String toString() {
		return stylesVertexs.toString();
	}

	public String getDefaultStyleVertex() {
		return defaultStyleVertex;
	}

	public Graph.OrientationGraph getOrientationGraph() {
		return orientationGraph;
	}

	public double getWidthGPane() {
		return widthGPane;
	}

	public double getHeightGPane() {
		return heightGPane;
	}

	public String getStyleGPane() {
		return styleGPane;
	}

	public ArrayList<Integer> getNumbersVertexs() {
		return numbersVertexs;
	}

	public TreeMap<Integer, Pair<Double>> getCoordinatesVertexs() {
		return coordinatesVertexs;
	}

	public TreeMap<Integer, String> getStylesVertexs() {
		return stylesVertexs;
	}

	public TreeMap<Integer, String> getNamesVertexs() {
		return namesVertexs;
	}

	public ArrayList<String> getEdges() {
		return edges;
	}

	public HashMap<String, String> getTextEdges() {
		return textEdges;
	}

	public HashMap<String, Graph.OrientationGraph> getOrientationsEdges() {
		return orientationsEdges;
	}

	public HashMap<String, Integer> getWeightsEdges() {
		return weightsEdges;
	}

	public HashMap<String, String> getStylesEdges() {
		return stylesEdges;
	}

	public TreeMap<Integer, String> getStylesChoosedVertexs() {
		return stylesChoosedVertexs;
	}

	public HashMap<String, String> getStylesNamesEdges() {
		return stylesNamesEdges;
	}

	public HashMap<String, String> getStylesArrowsEdges() {
		return stylesArrowsEdges;
	}

	public HashMap<String, Boolean> getIsShadowTextEdges() {
		return isShadowTextEdges;
	}

	public HashMap<String, Double> getSizeNamesEdges() {
		return sizeNamesEdges;
	}

	public HashMap<String, Double> getSizeArrowsEdges() {
		return sizeArrowsEdges;
	}

	public HashMap<String, Double> getSizeWeightsEdges() {
		return sizeWeightsEdges;
	}

	public TreeMap<Integer, Double> getSizeVertexs() {
		return sizeVertexs;
	}

	public TreeMap<Integer, String> getStylesNamesVertexs() {
		return stylesNamesVertexs;
	}

	public TreeMap<Integer, Double> getSizeTextVertexs() {
		return sizeTextVertexs;
	}

	public TreeMap<Integer, Boolean> getIsShadowTextVertexs() {
		return isShadowTextVertexs;
	}
	
}
