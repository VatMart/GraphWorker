package root.components;

import javafx.collections.MapChangeListener;
import javafx.scene.input.MouseEvent;
import root.model.Edge;
import root.model.Graph;
import root.model.Vertex;

public class Add_DeleteEdgeMode implements ChoseGraphElement {

//	private static root.components.Add_DeleteEdgeMode instance;
//	public static root.components.Add_DeleteEdgeMode getInstance(root.model.Graph graph) {
//		if (instance == null) {
//			instance = new root.components.Add_DeleteEdgeMode(graph);
//		}
//		return instance;
//	}
	private boolean modeActive;
	private Graph graph;
	private Vertex startVertex;
	private Vertex endVertex;
	private Edge deleteEdge;
	private String styleStartVertex = "-fx-fill: #006FFF; -fx-stroke: green; -fx-stroke-width: 3px; -fx-smooth: true;";

	private String styleEndVertex = "-fx-fill: #006FFF; -fx-stroke: red; -fx-stroke-width: 3px; -fx-smooth: true;";

	private MapChangeListener<Integer, Vertex> vertexsAddingListener;

	public Add_DeleteEdgeMode(Graph graph) {
		this.graph = graph;
		vertexsAddingListener = (change) -> {
			if (change.wasAdded()) {
				NodeHandler.getInstance().removeHandler(change.getValueAdded().getGroupVertex(), MouseEvent.MOUSE_DRAGGED);
			}
		};
	}

	private void buildEdge(Vertex start, Vertex end) {
		if (!graph.isEdgesContains(start, end)) {
			if (graph.getOrientationProperty().get() == Graph.OrientationGraph.ORIENTED) {
				//graph.addEdge(start.getNumber(), end.getNumber(), 1, root.model.Graph.OrientationGraph.ORIENTED);
				graph.addEdge(start.getNumber(), end.getNumber());
			} else
				//graph.addEdge(start.getNumber(), end.getNumber(), 1, root.model.Graph.OrientationGraph.NON_ORIENTED);
				graph.addEdge(start.getNumber(), end.getNumber());
		}
		setChoseModeOff();
	}

	public <T extends GraphElement> void choseAdd_DeleteEdge(T graphElement) {
		// System.out.println(root.model.Graph.chosenGraphElement == null ? "true" : "false");
		if ((graphElement == null)) {
//			if (root.model.Graph.chosenGraphElement != null)
			setChoseModeOff();
			startVertex = null;
			endVertex = null;
			//graph.setChosenGraphElement(null);
			graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
			graph.getChosenGraphElements().clear();
			// vertexContextMenu.hide();
			return;
		}
		// ���� ������ ����������� �������
		if (graphElement != null) {
			if (graphElement instanceof Vertex) {
				if (startVertex == null && endVertex == null) {
					//graph.setChosenGraphElement(graphElement);
					graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
					graph.getChosenGraphElements().clear();
					graph.getChosenGraphElements().add(graphElement);
					startVertex = (Vertex) graphElement;
					setChoseModeOn();
				} else if (startVertex != null && endVertex == null) {
					if (startVertex.equals(graphElement)) {
						setChoseModeOff();
						startVertex = null;
						endVertex = null;
						//graph.setChosenGraphElement(null);
						graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
						graph.getChosenGraphElements().clear();
						return;
					}
					//graph.setChosenGraphElement(graphElement);
					graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
					graph.getChosenGraphElements().clear();
					graph.getChosenGraphElements().add(graphElement);
					endVertex = (Vertex) graphElement;
					buildEdge(startVertex, endVertex);
					setChoseModeOff();
					startVertex = null;
					endVertex = null;
					//graph.setChosenGraphElement(null);
					graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
					graph.getChosenGraphElements().clear();
				}

			}
			if (graphElement instanceof Edge) {
				deleteEdge = (Edge) graphElement;
				setChoseModeOff();
				startVertex = null;
				endVertex = null;
				removeEdge(deleteEdge);
				//graph.setChosenGraphElement(null);
				graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
				graph.getChosenGraphElements().clear();
			}
		}
	}

	public boolean isModeActive() {
		return modeActive;
	}

	private void removeEdge(Edge edge) {
		graph.removeEdge(edge.getName());
	}

	@Override
	public void setChoseModeOff() {
		if (startVertex != null) {
			startVertex.getVertexCircle().setStyle(graph.getDefaultStyleVertex());
		}
		if (endVertex != null) {
			endVertex.getVertexCircle().setStyle(graph.getDefaultStyleVertex());
		}
	}

	@Override
	public void setChoseModeOn() {
		if (startVertex != null) {
			startVertex.getVertexCircle().setStyle(styleStartVertex);
		}
		if (endVertex != null) {
			endVertex.getVertexCircle().setStyle(styleEndVertex);
		}

	}

	public void setModeActive(boolean modeActive) {
		this.modeActive = modeActive;
		if (!graph.getChosenGraphElements().isEmpty()) {
			//graph.getChosenGraphElement().setChoseModeOff();
			//graph.setChosenGraphElement(null);
			graph.clearChoseElements();
			//graph.getChosenGraphElements().forEach(GraphElement::setChoseModeOff);
			//graph.getChosenGraphElements().clear();
		}
	}

	public void setModeOff() {
		setModeActive(false);
		graph.getVertexes().removeListener(vertexsAddingListener);
	}

	public void setModeOn() {
		setModeActive(true);
		graph.getVertexes().addListener(vertexsAddingListener);
	}
	
	public void changeGraph(Graph newGraph) {
		if (newGraph != null) {
			this.graph = newGraph;
		}
	}
}
