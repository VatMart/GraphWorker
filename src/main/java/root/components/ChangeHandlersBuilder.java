package root.components;

import java.util.Map.Entry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import root.model.Edge;
import root.model.Graph;
import root.model.Vertex;

public class ChangeHandlersBuilder {

	private Graph graph;
	public static SplitMenuButton pathButton;

	private MapChangeListener<Integer, Vertex> newVertexsListner;
	private MapChangeListener<String, Edge> newEdgesListener;
	private ChangeListener<Graph.OrientationGraph> newOrientationListener;
	public ChangeHandlersBuilder(Graph graph) {
		this.graph = graph;
	}

	public void bringBackHandlersWideDepth() {
//		if (graph.getNodeHandlers().get(graph.getRightPane(), MouseEvent.MOUSE_CLICKED) != null) 
//			graph.getRightPane().addEventHandler(MouseEvent.MOUSE_CLICKED,
//					graph.getNodeHandlers().get(graph.getRightPane(), MouseEvent.MOUSE_CLICKED));

//		for (Entry<Integer, root.model.Vertex> entry : getVertexs().entrySet()) {
//			getNodeHandlers().removeHandler(entry.getValue().getGroupVertex(), MouseEvent.MOUSE_DRAGGED);
//		}

		graph.getVertexes().removeListener(newVertexsListner);
		graph.getEdges().removeListener(newEdgesListener);
		graph.getOrientationProperty().removeListener(newOrientationListener);

	}

	public void changeHandlersWideDepth() {
//		handlers = new root.components.NodeHandler();
//		handlers.add(graph.getRightPane(), root.components.NodeHandler.getInstance().get(graph.getRightPane(), MouseEvent.MOUSE_CLICKED),
//				MouseEvent.MOUSE_CLICKED);
//		root.components.NodeHandler.getInstance().removeHandler(graph.getRightPane(), MouseEvent.MOUSE_CLICKED);
		for (Entry<Integer, Vertex> entry : graph.getVertexes().entrySet()) {
			NodeHandler.getInstance().removeHandler(entry.getValue().getGroupVertex(), MouseEvent.MOUSE_DRAGGED);
		}
		
		newVertexsListner = change -> {
			if (pathButton != null)
				pathButton.fire();
		};
		graph.getVertexes().addListener(newVertexsListner);
		newEdgesListener = change -> {
			if (pathButton != null)
				pathButton.fire();
		};
		graph.getEdges().addListener(newEdgesListener);
		newOrientationListener = (observable, oldValue, newValue) -> {
			if (pathButton != null)
				pathButton.fire();
		};
		graph.getOrientationProperty().addListener(newOrientationListener);
	}
}
