package root.controller;

import root.model.Graph;
import root.view.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerManager {

    private ViewManager viewManager;
    private List<ActionableComponents> actionableComponents;
    private Graph graph;

    public ControllerManager(Graph graph, ViewManager viewManager) {
        this.graph = graph;
        this.viewManager = viewManager;
        initiateActionableComponents();
    }

    public void setActions() {
        actionableComponents.forEach(a -> a.buildActionsComponents());
    }

    private void initiateActionableComponents() {
        MainPane mainPane = viewManager.getMainPane();
        DeskPane deskPane = viewManager.getMainPane().getDeskPane();
        ToolBarGraph toolBarGraph = viewManager.getMainPane().getToolBar();
        LeftPane leftPane = viewManager.getMainPane().getLeftPane();
        LeftPane.PaneMatrix paneMatrix = viewManager.getMainPane().getLeftPane().getPaneMatrix();
        LeftPane.PaneVariety paneVariety = viewManager.getMainPane().getLeftPane().getPaneVariety();
        HelpNote helpNote = viewManager.getMainPane().getDeskPane().getHelpNote();

        ActionableComponents mainPaneController = new MainPaneController(mainPane);
        ActionableComponents deskPaneController = new DeskPaneController(deskPane);
        ActionableComponents toolBarGraphController = new ToolBarGraphController(toolBarGraph);
        ActionableComponents leftPaneController = new LeftPaneController(leftPane);
        ActionableComponents paneMatrixController = new PaneMatrixController(paneMatrix);
        ActionableComponents paneVarietyController = new PaneVarietyController(paneVariety);
        ActionableComponents helpNoteController = new HelpNoteController(helpNote);

        actionableComponents = new ArrayList<>();
        Collections.addAll(actionableComponents, mainPaneController, deskPaneController, toolBarGraphController,
                leftPaneController, paneMatrixController, paneVarietyController, helpNoteController);
    }

}
