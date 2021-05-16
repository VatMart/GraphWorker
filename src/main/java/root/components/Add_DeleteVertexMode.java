package root.components;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import root.model.Graph;
import root.model.Vertex;

/*
 * ����� ���������� � �������� ������ (������ "+" ������ ��� ��������) �����
 * ����������� setButtonAddVertexHadler()
 */
public class Add_DeleteVertexMode {
//	private static root.components.Add_DeleteVertexMode instance;
//	public static root.components.Add_DeleteVertexMode getInstance(root.model.Graph graph) {
//		if (instance == null) {
//			instance = new root.components.Add_DeleteVertexMode(graph);
//		}
//		return instance;
//	}
	private Graph graph;
	private EventHandler<MouseEvent> clickOnGraphPaneHamdler;
	private HashMap<Integer, EventHandler<MouseEvent>> clickOnVertexHandler;

	private MapChangeListener<Integer, Vertex> vertexsAddingListener;

	public Add_DeleteVertexMode(Graph graph) {
		this.graph = graph;
	}

	// root.components.Add_DeleteVertexMode
	private void createHamdlers() {
		clickOnGraphPaneHamdler = (e) -> {
			int num = graph.solveNumberOfVertex();
			graph.addVertex(num, e.getX(), e.getY());
		};

		vertexsAddingListener = change -> {
			if (change.wasAdded()) {
				change.getValueAdded().getGroupVertex().addEventHandler(MouseEvent.MOUSE_PRESSED,
						createVertexsHandlers(change.getValueAdded()));
			}
			if (change.wasRemoved()) {
				change.getValueRemoved().getGroupVertex().removeEventHandler(MouseEvent.MOUSE_PRESSED,
						clickOnVertexHandler.get(change.getKey()));
			}
		};
	}

	// root.components.Add_DeleteVertexMode
	private EventHandler<MouseEvent> createVertexsHandlers(Vertex vertex) {
		class ClickOnVertexHandler implements EventHandler<MouseEvent> {
			@Override
			public void handle(MouseEvent event) {
				graph.removeVertex(vertex.getNumber());
			}
		}
		clickOnVertexHandler.put(vertex.getNumber(), new ClickOnVertexHandler());
		return clickOnVertexHandler.get(vertex.getNumber());
	}

	// root.components.Add_DeleteVertexMode
	public void setModeOff() {
		if (clickOnGraphPaneHamdler == null)
			System.out.println("null!!!");
		graph.getVertexes().removeListener(vertexsAddingListener);
		graph.getGraphPane().getTransparencyGraphPane().removeEventHandler(MouseEvent.MOUSE_CLICKED,
				clickOnGraphPaneHamdler);
		for (Map.Entry<Integer, Vertex> entry : graph.getVertexes().entrySet()) {
			entry.getValue().getGroupVertex().removeEventHandler(MouseEvent.MOUSE_PRESSED,
					clickOnVertexHandler.get(entry.getKey()));
		}
	}

	// root.components.Add_DeleteVertexMode
	public void setModeOn() {
		createHamdlers();
		graph.getGraphPane().getTransparencyGraphPane().addEventHandler(MouseEvent.MOUSE_CLICKED, clickOnGraphPaneHamdler);
		clickOnVertexHandler = new HashMap<Integer, EventHandler<MouseEvent>>();
		for (Map.Entry<Integer, Vertex> entry : graph.getVertexes().entrySet()) {
			entry.getValue().getGroupVertex().addEventHandler(MouseEvent.MOUSE_PRESSED,
					createVertexsHandlers(entry.getValue()));
		}
		graph.getVertexes().addListener(vertexsAddingListener);
	}
	
	public void changeGraph(Graph newGraph) {
		if (newGraph != null) {
			this.graph = newGraph;
		}
	}
}
