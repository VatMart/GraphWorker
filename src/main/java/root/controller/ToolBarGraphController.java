package root.controller;

import root.components.Add_DeleteEdgeMode;
import root.components.Add_DeleteVertexMode;
import root.model.FindCyclesAlg;
import root.view.ToolBarGraph;
import root.model.DepthSearchAlg;
import root.model.DijkstraAlg;
import root.model.WideSearchAlg;

public class ToolBarGraphController  implements ActionableComponents {
    private ToolBarGraph toolBarGraph;

    public ToolBarGraphController(ToolBarGraph toolBarGraph) {
        this.toolBarGraph = toolBarGraph;
    }

    @Override
    public void buildActionsComponents() {
        buildToggleButtonsVertexEdgeActions();
        toolBarGraph.getButtonClearGraph().setOnAction(event -> {
            toolBarGraph.getGraph().clearGraph();
            toolBarGraph.getMainPane().getLeftPane().getTaEdges().clear();
        });
        buildMenuButtonShowAction();
        buildPathButtonAction();
        buildMenuButtonScaleAction();
    }

    private void buildToggleButtonsVertexEdgeActions() {
        Add_DeleteVertexMode mode = toolBarGraph.getGraph().getAdd_DeleteVertexMode();
        toolBarGraph.getButtonAddVertex().selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                // System.out.println("PRESS on!");
                mode.setModeOn();
            }
            if (newValue == false) {
                // System.out.println("PRESS off!");
                mode.setModeOff();
            }
        });
        Add_DeleteEdgeMode add_DeleteEdgeMode = toolBarGraph.getGraph().getAdd_DeleteEdgeMode();
        toolBarGraph.getButtonAddEdge().selectedProperty().addListener((obs, o, n) -> {
            if (toolBarGraph.getGraph().getAlgorithm().isAlgorithmActive()) {
                toolBarGraph.getPathButton().fire();
                return;
            }
            if (n) {
                add_DeleteEdgeMode.setModeOn();
            } else {
                add_DeleteEdgeMode.setModeOff();
            }
        });
    }
    private void buildPathButtonAction() {
        toolBarGraph.getGraph().setAlgorithm(new WideSearchAlg(toolBarGraph.getGraph()));
        toolBarGraph.getWideSearchItem().setOnAction(event -> {
            toolBarGraph.getPathButton().setText(toolBarGraph.getWideSearchItem().getText());
            if (toolBarGraph.getPathButton().getStyleClass().contains("menu-split-buttonTUGGLE-alg"))
                toolBarGraph.getPathButton().getStyleClass().remove("menu-split-buttonTUGGLE-alg");
            toolBarGraph.getPathButton().getStyleClass().add("menu-split-button-alg");
            if (toolBarGraph.getGraph().getAlgorithm() != null)
                toolBarGraph.getGraph().getAlgorithm().setAlgorithmActive(false);
            toolBarGraph.getGraph().setAlgorithm(new WideSearchAlg(toolBarGraph.getGraph()));
        });
        toolBarGraph.getDepthSearchItem().setOnAction(event -> {
            toolBarGraph.getPathButton().setText( toolBarGraph.getDepthSearchItem().getText());
            if (toolBarGraph.getPathButton().getStyleClass().contains("menu-split-buttonTUGGLE-alg"))
                toolBarGraph.getPathButton().getStyleClass().remove("menu-split-buttonTUGGLE-alg");
            toolBarGraph.getPathButton().getStyleClass().add("menu-split-button-alg");
            if (toolBarGraph.getGraph().getAlgorithm() != null)
                toolBarGraph.getGraph().getAlgorithm().setAlgorithmActive(false);
            toolBarGraph.getGraph().setAlgorithm(new DepthSearchAlg(toolBarGraph.getGraph()));
        });
        toolBarGraph.getDijkstraItem().setOnAction(val -> {
            toolBarGraph.getPathButton().setText(toolBarGraph.getDijkstraItem().getText());
            if (toolBarGraph.getPathButton().getStyleClass().contains("menu-split-buttonTUGGLE-alg"))
                toolBarGraph.getPathButton().getStyleClass().remove("menu-split-buttonTUGGLE-alg");
            toolBarGraph.getPathButton().getStyleClass().add("menu-split-button-alg");
            if (toolBarGraph.getGraph().getAlgorithm() != null)
                toolBarGraph.getGraph().getAlgorithm().setAlgorithmActive(false);
            toolBarGraph.getGraph().setAlgorithm(new DijkstraAlg(toolBarGraph.getGraph()));
            ((DijkstraAlg)toolBarGraph.getGraph().getAlgorithm()).setDesk(toolBarGraph.getMainPane().getDeskPane());
        });
        toolBarGraph.getFindCyclesItem().setOnAction(event -> {
            toolBarGraph.getPathButton().setText(toolBarGraph.getFindCyclesItem().getText());
            if (toolBarGraph.getPathButton().getStyleClass().contains("menu-split-buttonTUGGLE-alg"))
                toolBarGraph.getPathButton().getStyleClass().remove("menu-split-buttonTUGGLE-alg");
            toolBarGraph.getPathButton().getStyleClass().add("menu-split-button-alg");
            if (toolBarGraph.getGraph().getAlgorithm() != null)
                toolBarGraph.getGraph().getAlgorithm().setAlgorithmActive(false);
            toolBarGraph.getGraph().setAlgorithm(new FindCyclesAlg(toolBarGraph.getGraph(), toolBarGraph.getMainPane().getDeskPane()));

        });
        toolBarGraph.getPathButton().setOnAction(event -> {
            if (toolBarGraph.getButtonAddEdge().isSelected()) {
                toolBarGraph.getButtonAddEdge().fire();
            }
            toolBarGraph.getGraph().choseObject(null);
            if (toolBarGraph.getGraph().getAlgorithm().isAlgorithmActive()) {
                // System.out.println("UNACTIVE");
                toolBarGraph.getGraph().getAlgorithm().setAlgorithmActive(false);
                if (toolBarGraph.getPathButton().getStyleClass().contains("menu-split-buttonTUGGLE-alg"))
                    toolBarGraph.getPathButton().getStyleClass().remove("menu-split-buttonTUGGLE-alg");
                toolBarGraph.getPathButton().getStyleClass().add("menu-split-button-alg");
                // pathButton.setStyle(defaultStylePathButton);
            } else {
                // System.out.println("ACTIVE");
                toolBarGraph.getGraph().getAlgorithm().setAlgorithmActive(true);
                toolBarGraph.getPathButton().getStyleClass().remove("menu-split-button-alg");
                toolBarGraph.getPathButton().getStyleClass().add("menu-split-buttonTUGGLE-alg");
                // pathButton.setStyle("-fx-background-color: red, transparent; ");
            }
        });
    }

    private void buildMenuButtonShowAction() {
        toolBarGraph.getGraph().getShowNamesEdges().bind(toolBarGraph.getCheckNames().selectedProperty());
        toolBarGraph.getShowWeightsEdges().setSelected(toolBarGraph.getGraph().isShowWeightsEdges());
        toolBarGraph.getShowWeightsEdges().selectedProperty().addListener((observable, oldValue, newValue) -> toolBarGraph.getGraph().setShowWeightsEdges(newValue));
    }
    private void buildMenuButtonScaleAction() {
        // TODO Auto-generated method stub
        toolBarGraph.getMi800().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("800%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(8.);
            toolBarGraph.getGraph().getGraphPane().setScaleY(8.);
        });
        toolBarGraph.getMi400().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("400%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(4.);
            toolBarGraph.getGraph().getGraphPane().setScaleY(4.);
        });
        toolBarGraph.getMi200().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("200%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(2.);
            toolBarGraph.getGraph().getGraphPane().setScaleY(2.);
        });
        toolBarGraph.getMi150().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("150%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(1.5);
            toolBarGraph.getGraph().getGraphPane().setScaleY(1.5);
        });
        toolBarGraph.getMi125().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("125%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(1.25);
            toolBarGraph.getGraph().getGraphPane().setScaleY(1.25);
        });
        toolBarGraph.getMi100().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("100%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(1.);
            toolBarGraph.getGraph().getGraphPane().setScaleY(1.);
        });
        toolBarGraph.getMi75().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("75%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(0.75);
            toolBarGraph.getGraph().getGraphPane().setScaleY(0.75);
        });
        toolBarGraph.getMi50().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("50%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(0.5);
            toolBarGraph.getGraph().getGraphPane().setScaleY(0.5);
        });
        toolBarGraph.getMi25().setOnAction(value -> {
            toolBarGraph.getMbScale().setText("25%");
            toolBarGraph.getGraph().getGraphPane().setScaleX(0.25);
            toolBarGraph.getGraph().getGraphPane().setScaleY(0.25);
        });
    }

}
