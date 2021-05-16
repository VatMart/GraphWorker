package root.components;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import root.model.Graph;
import root.model.Vertex;

public class MultipleMovingGraphElements {
    private Graph graph;

    public MultipleMovingGraphElements(Graph graph) {
        this.graph = graph;
        initiateHandlers();
    }

    private EventHandler<MouseEvent> draggedHandler;

    private void initiateHandlers() {
        lastMouseLocation = new MouseLocation();
        draggedHandler = mouseEvent -> {
            double deltaX = mouseEvent.getX() - lastMouseLocation.x ;
            double deltaY = mouseEvent.getY() - lastMouseLocation.y ;
            for (GraphElement c : graph.getChosenGraphElements()) {
                if (c instanceof Vertex) {
                    Vertex v = (Vertex) c;
                    v.setCoordinates(v.getVertexCircle().getCenterX() + deltaX, v.getVertexCircle().getCenterY() + deltaY);
                }
            }
            lastMouseLocation.x = mouseEvent.getX();
            lastMouseLocation.y = mouseEvent.getY();
        };
    }

    public MouseLocation getLastMouseLocation() {
        return lastMouseLocation;
    }

    private MouseLocation lastMouseLocation;
    public void makeDraggable(GraphElement graphElement) {
        if (!(graphElement instanceof Vertex)) return;
        Vertex vertex = (Vertex) graphElement;
        vertex.getGroupVertex().addEventHandler(MouseEvent.MOUSE_DRAGGED, draggedHandler);
        //NodeHandler.getInstance().add(vertex.getGroupVertex(), draggedHandler, MouseEvent.MOUSE_DRAGGED);
    }

    public void removeDraggable(GraphElement graphElement) {
        if (!(graphElement instanceof Vertex)) return;
        Vertex vertex = (Vertex) graphElement;
        vertex.getGroupVertex().removeEventHandler(MouseEvent.MOUSE_DRAGGED, draggedHandler);
    }

    public static final class MouseLocation {
        public double x, y;
    }

}
