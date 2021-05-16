package root.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import root.view.LeftPane;
import root.model.Edge;
import root.model.Graph;

public class LeftPaneController implements ActionableComponents {

    private LeftPane leftPane;

    public LeftPaneController(LeftPane leftPane) {
        this.leftPane = leftPane;
    }

    @Override
    public void buildActionsComponents() {
        buildPaneOrientationActions();
        buildToolBarGenerateGraphActions();
    }

    private void buildPaneOrientationActions() {
        EventHandler<ActionEvent> radioButtonDirectedGraphGraphEventHandler = event -> {
            // if (rbDirectedGraph.isSelected()) {
            leftPane.getGraph().setOrientation(Graph.OrientationGraph.ORIENTED);
            leftPane.getPaneVariety().getTaOutputMatrix().setText(leftPane.getGraph().matrix.buildMatrix());
            leftPane.getPaneVariety().getListView().getItems().setAll(leftPane.getGraph().matrix.getListVertexsPosition());
            // }
        };
        leftPane.getRbDirectedGraph().addEventHandler(ActionEvent.ACTION, radioButtonDirectedGraphGraphEventHandler);
        EventHandler<ActionEvent> radioButtonUndirectedGraphEventHandler = event -> {
            // if (rbUndirectedGraph.isSelected()) {
            leftPane.getGraph().setOrientation(Graph.OrientationGraph.NON_ORIENTED);
            leftPane.getPaneVariety().getTaOutputMatrix().setText(leftPane.getGraph().matrix.buildMatrix());
            leftPane.getPaneVariety().getListView().getItems().setAll(leftPane.getGraph().matrix.getListVertexsPosition());
            // }
        };
        leftPane.getRbUndirectedGraph().addEventHandler(ActionEvent.ACTION, radioButtonUndirectedGraphEventHandler);
        leftPane.getRbMixedGraph().selectedProperty().addListener((obs, o, n) -> {
            leftPane.getPaneVariety().getTaOutputMatrix().setText(leftPane.getGraph().matrix.buildMatrix());
            leftPane.getPaneVariety().getListView().getItems().setAll(leftPane.getGraph().matrix.getListVertexsPosition());
        });

        leftPane.getGraph().getOrientationProperty().addListener((obs, o, n) -> {
            if (n == Graph.OrientationGraph.ORIENTED) {
                if (!leftPane.getRbDirectedGraph().isSelected())
                    leftPane.getRbDirectedGraph().setSelected(true);
            }
            if (n == Graph.OrientationGraph.NON_ORIENTED) {
                if (!leftPane.getRbUndirectedGraph().isSelected())
                    leftPane.getRbUndirectedGraph().setSelected(true);
            }
            if (n == Graph.OrientationGraph.MIXED) {
                if (!leftPane.getRbMixedGraph().isSelected())
                    leftPane.getRbMixedGraph().setSelected(true);
            }
            // paneVariety.taOutputMatrix.setText(graph.matrix.buildMatrix());
            // paneVariety.listView.getItems().setAll(graph.matrix.getListVertexsPosition());
        });
        ChangeListener<Graph.OrientationGraph> listener = (observable, oldValue, newValue) -> {
            leftPane.getPaneVariety().getTaOutputMatrix().setText(leftPane.getGraph().matrix.buildMatrix());
            leftPane.getPaneVariety().getListView().getItems().setAll(leftPane.getGraph().matrix.getListVertexsPosition());
        };
        leftPane.getGraph().getEdges().addListener((MapChangeListener<String, Edge>) change -> {
            if (change.wasAdded()) {
                change.getValueAdded().getOrientationEdgeProperty().addListener(listener);
            }
            if (change.wasRemoved()) {
                change.getValueRemoved().getOrientationEdgeProperty().removeListener(listener);
            }
        });
    }

    private void buildToolBarGenerateGraphActions() {
        leftPane.getButtonGenerateGraph().setOnAction(event -> {
            leftPane.getTaEdges().clear();
            leftPane.getGraph().generateRandomGraph();
        });
    }
}
