package root.model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;
import root.components.ChangeHandlersBuilder;
import root.components.ChoseGraphElement;
import root.components.GraphElement;
import root.view.DeskPane;
import root.view.HelpNote;

import java.util.*;

public class DijkstraAlg extends AlgorithmGraph implements ChoseGraphElement {

    private final Graph graph;
    private boolean algorithmActive;
    private Vertex startVertex;
    private Vertex endVertex;
    private final ChangeHandlersBuilder changeHandlersBuilder;

    public void setDesk(DeskPane desk) {
        this.desk = desk;
    }

    private DeskPane desk;

    public DijkstraAlg(Graph graph) {
        //super();
        this.graph = graph;
        setAlgorithmType(AlgorithmType.DIJKSTRA);
        changeHandlersBuilder = new ChangeHandlersBuilder(graph);
    }

    private LinkedList<PairVertexEdge> pathDijkstra;
    private LinkedHashMap<Vertex, String> vertexesColor;
    private LinkedHashMap<Edge, String> edgesColor;

    private JFXSnackbar bar;
    @Override
    public void buildAlgorithm() {
        if (startVertex == null || endVertex == null) return;
        vertexesColor = new LinkedHashMap<>();
        edgesColor = new LinkedHashMap<>();
        pathDijkstra = new LinkedList<>();
        Vertex currentVertex = endVertex;
        while (currentVertex != startVertex) {
            Vertex nextVertex = null;
            Edge nextEdge = null;
            for (PairVertexEdge pair : getVertexesBelongingToVertex(currentVertex)) {
                Vertex fromVertex = currentVertex == pair.edge.getVertexFirst() ? pair.edge.getVertexSecond() : pair.edge.getVertexFirst();
                if (vertexesLabels.get(currentVertex) - pair.edge.getWeight() == vertexesLabels.get(fromVertex)) {
                    nextVertex = fromVertex;
                    nextEdge = pair.edge;
                }
            }
            if (nextVertex != null && nextEdge != null) {
                pathDijkstra.addFirst(new PairVertexEdge(nextVertex, nextEdge));
                currentVertex = nextVertex;
            } else {
                pathDijkstra = null;
                break;
            }
        }

        vertexesColor.put(endVertex, endVertex.getFillColorDefault());
        if (pathDijkstra != null)
            pathDijkstra.forEach((verEdge) -> {
                vertexesColor.put(verEdge.vertex, verEdge.vertex.getFillColorDefault());
                edgesColor.put(verEdge.edge, verEdge.edge.getColorEdge());
            });

        //System.out.println(pathDijkstra);
        if (pathDijkstra != null)
            setChoseModeOn();
        else {
            if (desk != null) {
                if (bar != null) bar.close();
                bar = new JFXSnackbar(desk);
                bar.getStyleClass().add("jfx-snackbar-content");
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setPadding(new Insets(5));
                hBox.setAlignment(Pos.CENTER_LEFT);
                Label label1 = new Label("Нет путей к данной вершине");
                label1.setFont(new Font("Roboto", 14));
                label1.setTextFill(Color.WHITE);
                hBox.getChildren().add(label1);
                StackPane sp = new StackPane();
                JFXButton button = new JFXButton("CLOSE");
                button.setOnAction(val -> bar.close());
                button.setFont(new Font("Roboto", 15));
                button.setTextFill(Color.RED);
                sp.getChildren().add(button);
                hBox.getChildren().add(sp);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(hBox, Duration.INDEFINITE));
            }
            //System.out.println("MISTAKE");
            choseAlgorithmElement(null);
        }
    }

    private LinkedList<PairVertexEdge> getVertexesBelongingToVertex(Vertex vertex) {
        LinkedList<PairVertexEdge> list = new LinkedList<>();
        graph.getEdgesBelongingToVertexWithoutOrientation(vertex).forEach((edge, pos) -> {
            if (edge.isOrientated() && pos.equals(2)) {
                list.add(new PairVertexEdge(vertex, edge));
            } else if (!edge.isOrientated()) {
                list.add(new PairVertexEdge(vertex, edge));
            }
        });
        return list;
    }

    @Override
    public <T extends GraphElement> void choseAlgorithmElement(T vertexOrEdge) {
        if ((vertexOrEdge == null)) {
            setChoseModeOff();
            startVertex = null;
            endVertex = null;
            return;
        }
        // Если выбран графический элемент
        if (vertexOrEdge instanceof Vertex) {
            if (startVertex != null && endVertex != null) {
                setChoseModeOff();
                startVertex = (Vertex) vertexOrEdge;
                endVertex = null;
                passVertexes();
                return;
            }
            if (startVertex == null && endVertex == null) {
                startVertex = (Vertex) vertexOrEdge;
                passVertexes();
            } else if (startVertex != null) {
                if (startVertex.equals(vertexOrEdge)) {
                    setChoseModeOff();
                    startVertex = null;
                    endVertex = null;
                    return;
                }
                endVertex = (Vertex) vertexOrEdge;
                buildAlgorithm();
            }
        }
    }
    private LinkedHashMap<Vertex, Integer> vertexesLabels;
    /**
     * Реализует первый этап алгоритма. Обходит все вершины,
     * устанавливая для каждой метку
     */
    private void passVertexes() {
        if (startVertex == null) return;
        initiatePassVertexes();
        
        ArrayList<Vertex> visitedVertexes = new ArrayList<>();
        while (findMinVertexInPass(visitedVertexes) != null) {
            final Vertex minimalVertex = findMinVertexInPass(visitedVertexes);
            ArrayList<Vertex> neighboursVertexes = new ArrayList<>(graph.getVertexesBelongingToVertex(minimalVertex).values());
            neighboursVertexes.forEach(vert -> {
                if (!visitedVertexes.contains(vert)) {
                    Edge edge = graph.getEdge(minimalVertex, vert);
                    if (edge == null) edge = graph.getEdge(vert, minimalVertex);
                    if ((edge.getWeight() + vertexesLabels.get(minimalVertex)) < vertexesLabels.get(vert))
                        vertexesLabels.replace(vert, (edge.getWeight() + vertexesLabels.get(minimalVertex)));
                    //System.out.println("vert: " + vert + " label :" + vertexesLabels.get(vert));
                }
            });
            visitedVertexes.add(minimalVertex);
        }
        //System.out.println("filled : " + vertexesLabels);
        choseStartVertex(true);
    }

    private void choseStartVertex(boolean n) {
        if (n) {
            startVertex.getVertexCircle()
                    .setStyle("-fx-fill: #FFC618; -fx-stroke: #FF0000; -fx-stroke-width: 3px; -fx-smooth: true;");
            graph.getGraphPane().getHelpNote().setNote("Выберите КОНЕЧНУЮ вершину");
        } else {
            if (startVertex != null)
                startVertex.setDefaultStyle(startVertex.getDefaultStyle());
        }
    }

    private Vertex findMinVertexInPass(ArrayList<Vertex> visitedVertexes) {
        int minimalLabel = Integer.MAX_VALUE/2;
        Vertex minimalVertex = null;
        for (Map.Entry<Vertex, Integer> entry : vertexesLabels.entrySet()) {
            if (entry.getValue() <= minimalLabel && !visitedVertexes.contains(entry.getKey())) {
                minimalLabel = entry.getValue();
                minimalVertex = entry.getKey();
            }
        }
        return minimalVertex;
    }

    /**
     * Устанавливаем метки для всех вершин на максимальное число, кроме
     * начальной, которой ставится значение 0.
     */
    private void initiatePassVertexes() {
        vertexesLabels = new LinkedHashMap<>(graph.getVertexes().size());
        graph.getVertexes().forEach((num, vert) -> {
            if (vert == startVertex) vertexesLabels.put(startVertex, 0);
            else vertexesLabels.put(vert, Integer.MAX_VALUE/2);
        });
        //System.out.println("initial : " + vertexesLabels);
    }

    @Override
    public boolean isAlgorithmActive() {
        return algorithmActive;
    }

    @Override
    public void setAlgorithmActive(boolean algorithmActive) {
        if (algorithmActive) {
			graph.getGraphPane().getHelpNote().setNote("Выберите НАЧАЛЬНУЮ вершину");
            changeHandlersBuilder.changeHandlersWideDepth();
        } else {
            if (this.algorithmActive)
                changeHandlersBuilder.bringBackHandlersWideDepth();
            setChoseModeOff();
            graph.getGraphPane().getHelpNote().setNote(HelpNote.DEFAULT_TEXT);
        }
        this.algorithmActive = algorithmActive;
    }

    @Override
    public void setChoseModeOff() {
        if (transitionsList != null)
            transitionsList.forEach(Animation::stop);
        choseStartVertex(false);
        if (endVertex != null)
            endVertex.setDefaultStyle(endVertex.getDefaultStyle());
        if (vertexesColor != null)
            vertexesColor.forEach((ver, color) -> {
                ver.setFillColorVertexDefault(color);
              ver.getVertexCircle().setFill(Color.valueOf(color));
            });
        if (edgesColor != null)
            edgesColor.forEach(Edge::setColorEdge);
        graph.getGraphPane().getHelpNote().setNote("Выберите НАЧАЛЬНУЮ вершину");
        if (bar != null) bar.close();
    }

    private ArrayList<SequentialTransition> transitionsList;
    @Override
    public void setChoseModeOn() {
        if (pathDijkstra == null) return;
        transitionsList = new ArrayList<>();
        endVertex.getVertexCircle()
                .setStyle("-fx-fill: #f7943c; -fx-stroke: #FF0000; -fx-stroke-width: 3px; -fx-smooth: true;");
        int countMillis = 0;
        int counter = 1;
        for (PairVertexEdge pair: pathDijkstra) {
            PauseTransition pauseTransition1 = new PauseTransition();
            pauseTransition1.setDuration(Duration.millis(countMillis));
            FillTransition fillTransitionVertex;
            if (pair.vertex != startVertex)
                fillTransitionVertex = new FillTransition(Duration.millis(200), pair.vertex.getVertexCircle(), Color.valueOf(pair.vertex.getFillColorDefault()), Color.rgb(230, Math.min((3 + counter * 12), 255), Math.min((3 + counter * 8), 255)));
            else fillTransitionVertex = new FillTransition(Duration.millis(200), pair.vertex.getVertexCircle(), Color.valueOf(pair.vertex.getFillColorDefault()), Color.valueOf("#FFC618"));
            Circle circle = new Circle( 5);
            circle.setFill(Color.valueOf("#004299"));
            fillTransitionVertex.setOnFinished(val -> {
                pair.edge.setColorEdge("#FFC618");
                graph.getGraphPane().getChildren().add(circle);
            });
            Path path = new Path(pair.edge.getEdgeLine().getElements());
            if (pair.edge.getVertexSecond() == pair.vertex)
                path.setRotate(180);
            PathTransition pathTransition = new PathTransition(Duration.millis(500), path, circle);
            //pathTransition.setAutoReverse(true);
            pathTransition.setOnFinished(val -> graph.getGraphPane().getChildren().remove(circle));
            SequentialTransition sequentialTransition = new SequentialTransition(pauseTransition1,
                    fillTransitionVertex,
                    pathTransition);
            transitionsList.add(sequentialTransition);

            sequentialTransition.play();
            countMillis += 500;
        }
    }

    private static class PairVertexEdge {
        private Edge edge;
        private Vertex vertex;
        public PairVertexEdge(Vertex vertex, Edge edge) {
            this.vertex = vertex;
            this.edge = edge;
        }
        @Override
        public String toString() {
            return "vertex: " + vertex + "; edge: " + edge;
        }
    }
}
