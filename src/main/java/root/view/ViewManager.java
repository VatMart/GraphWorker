package root.view;

import root.model.Graph;

public class ViewManager {
    private Graph graph;
    private MainPane mainPane;

    public ViewManager(Graph graph) {
        this.graph = graph;
        initiateView();
    }

    public void initiateView() {
        mainPane = new MainPane(graph);
    }

    public MainPane getMainPane() {
        return mainPane;
    }
}
