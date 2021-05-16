package root.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import root.components.ChangeHandlersBuilder;
import root.components.ChoseGraphElement;
import root.components.GraphElement;
import root.view.DeskPane;
import root.view.HelpNote;
import root.view.MainPane;

import java.util.*;
import java.util.stream.Collectors;

public class FindCyclesAlg extends AlgorithmGraph implements ChoseGraphElement {

    private final Graph graph;
    private boolean algorithmActive;
    private final ChangeHandlersBuilder changeHandlersBuilder;

    // упордоченные вершины вершина, номер вершины в обходе
    private ArrayList<Vertex> vertexesDepth;
    private LinkedHashMap<Integer, Edge> edgesDepth;

    private HashSet<Node> setStepDepthSearch;

    private DeskPane deskPane;

    public FindCyclesAlg(Graph graph, DeskPane desk) {
        this.graph = graph;
        this.deskPane = desk;
        setAlgorithmType(AlgorithmType.FIND_CYCLES);
        changeHandlersBuilder = new ChangeHandlersBuilder(graph);
    }

    @Override
    public void setChoseModeOff() {

    }

    @Override
    public void setChoseModeOn() {

    }

    private ArrayList<ArrayList<Vertex>> cycles;

    @Override
    public void buildAlgorithm() {
        cycles = new ArrayList<>();
        graph.getVertexes().forEach((num, vertex) -> {
            HashSet<Vertex> visitedVertexes = new HashSet<>();
            visitedVertexes.add(vertex);
            ArrayDeque<Vertex> queue = new ArrayDeque<>(graph.getVertexesBelongingToVertex(vertex).values());
            int counterVertex = 1;
            vertexesDepth = new ArrayList<>();
            edgesDepth = new LinkedHashMap<>();
            vertexesDepth.add(vertex);
            while (queue.size() > 0) {
                counterVertex++;
                Vertex currentVertex = queue.pollFirst();
                vertexesDepth.add(currentVertex);
                int quantityBelongsVertex = graph.getVertexesBelongingToVertex(currentVertex).size();
                int countDontVisited = 0;
                for (Map.Entry<Integer, Vertex> entry : graph.getVertexesBelongingToVertex(currentVertex).entrySet()) {
                    quantityBelongsVertex--;
                    if (!visitedVertexes.contains(entry.getValue())) {
                        queue.addFirst(entry.getValue());
                        countDontVisited++;
                    }
                }
                visitedVertexes.add(currentVertex);
                if (counterVertex >= 3 &&
                        graph.getVertexesBelongingToVertex(currentVertex).containsValue(vertex)) {
                    ArrayList<Vertex> cycle = new ArrayList<>();
                    cycle.addAll(vertexesDepth);
                    //vertexesDepth.clear();
                    cycle.add(vertex);
                    if (!checkIsContainsCycle(cycle)) {
                        cycles.add(cycle);
                        vertexesDepth.clear();
                        counterVertex = 0;
                    }
                } else {
                if (countDontVisited == 0) vertexesDepth.clear();
                }

            }
        });
        createResultMessage();
    }

    private void createResultMessage() {
        StackPane stackPane = new StackPane();
        deskPane.getChildren().add(stackPane);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Результат поиска циклов"));
        content.setBody(new Text(cyclesToString(cycles)));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Закрыть");
        button.setOnAction(ev -> {
            dialog.close();
        });
        content.setActions(button);
        dialog.show();
    }

    private String cyclesToString(ArrayList<ArrayList<Vertex>> cycles) {
        String result;
        if (cycles.isEmpty()) {
            result = "В графе нет циклов";
        } else result = cycles.stream().filter(cycle -> {
            boolean a1 = cycle.size() >= 4;
            boolean a2 = a1 ? cycle.get(0) == cycle.get(cycle.size()-1) : false;return a2;}).
                map(arrays -> arrays.toString()).
                collect(Collectors.joining("\n", "", ""));
        System.out.println(result);
        return result;
    }

    private boolean checkIsContainsCycle(ArrayList<Vertex> newCycle) {
        for (List<Vertex> cycle : cycles) {
            if (cycle.containsAll(newCycle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends GraphElement> void choseAlgorithmElement(T vertexOrEdge) {

    }

    @Override
    public boolean isAlgorithmActive() {
        return algorithmActive;
    }

    @Override
    public void setAlgorithmActive(boolean algorithmActive) {
        if (algorithmActive) {
            graph.getGraphPane().getHelpNote().setNote("TEST ALG");
            changeHandlersBuilder.changeHandlersWideDepth();
            buildAlgorithm();
        } else {
            graph.getGraphPane().getHelpNote().setNote(HelpNote.DEFAULT_TEXT);
            if (this.algorithmActive)
                changeHandlersBuilder.bringBackHandlersWideDepth();
            //setChoseModeOff();
        }
        this.algorithmActive = algorithmActive;
    }

    public void setDeskPane(DeskPane deskPane) {
        this.deskPane = deskPane;
    }
}
