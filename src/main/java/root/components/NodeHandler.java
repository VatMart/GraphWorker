package root.components;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class NodeHandler {
	
	private static NodeHandler instance;
	public static NodeHandler getInstance() {
		if (instance == null) {
			instance = new NodeHandler();
		}
		return instance;
	}
	
	private ArrayList<Node> nodes;
	private ArrayList<EventHandler<MouseEvent>> handlers;
	private ArrayList<EventType<MouseEvent>> eventTypes;

	public NodeHandler() {
		this.nodes = new ArrayList<>();
		this.handlers = new ArrayList<EventHandler<MouseEvent>>();
		this.eventTypes = new ArrayList<EventType<MouseEvent>>();
	}

	public void add(Node node, EventHandler<MouseEvent> handler, EventType<MouseEvent> eventType) {
		nodes.add(node);
		handlers.add(handler);
		eventTypes.add(eventType);
	}

	public EventHandler<MouseEvent> get(Node node, EventType<MouseEvent> eventType) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).equals(node) && eventTypes.get(i).equals(eventType)) {
				// System.out.println("FIND");
				return handlers.get(i);
			}
		}
		return null;
	}

	public void removeHandler(Node node, EventType<MouseEvent> eventType) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).equals(node) && eventTypes.get(i).equals(eventType)) {
				// System.out.println("FIND");
				node.removeEventHandler(eventType, handlers.get(i));
			}
		}
	}
}