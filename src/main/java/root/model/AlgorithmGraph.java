package root.model;

import root.components.GraphElement;

public abstract class AlgorithmGraph  {

	public enum AlgorithmType {
		EMPTY,
		WIDE_SEARCH, DEPTH_SEARCH, DIJKSTRA, FIND_CYCLES
	}
	
	//private root.model.Graph graph;
	private AlgorithmType algorithmType;
	//private boolean algorithmActive;

	public abstract void buildAlgorithm();

	public abstract <T extends GraphElement> void choseAlgorithmElement(T vertexOrEdge);
	
//	public root.model.AlgorithmGraph(root.model.Graph graph) {
//		//setAlgorithmActive(false);
//		this.graph = graph;
//	}

	public AlgorithmType getAlgorithmType() {
		return algorithmType;
	}

	public abstract boolean isAlgorithmActive();


	public abstract void setAlgorithmActive(boolean algorithmActive);
	
	public void setAlgorithmType(AlgorithmType algorithmType) {
		this.algorithmType = algorithmType;
	}

}
