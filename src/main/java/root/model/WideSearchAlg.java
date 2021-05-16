package root.model;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import root.components.ChangeHandlersBuilder;
import root.components.ChoseGraphElement;
import root.components.GraphElement;
import root.view.HelpNote;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class WideSearchAlg extends AlgorithmGraph implements ChoseGraphElement {

	private GraphElement chosenGraphElement;
	private final Graph graph;
	private boolean algorithmActive;
	private Vertex startVertex;
	private final ChangeHandlersBuilder changeHandlersBuilder;

	private LinkedHashMap<Vertex, Integer> vertexesWide;
	private LinkedHashMap<Integer, Edge> edgesWide;
	private LinkedHashMap<Vertex, String> vertexesColor;
	private LinkedHashMap<Edge, String> edgesColor;

	private HashSet<Node> setStepWideSearch;

	public WideSearchAlg(Graph graph) {
		//super();
		this.graph = graph;
		setAlgorithmType(AlgorithmType.WIDE_SEARCH);
		changeHandlersBuilder = new ChangeHandlersBuilder(graph);
	}

	@Override
	public void buildAlgorithm() {
		if (startVertex == null)
			return;
		ArrayList<Vertex> visitedVertexes = new ArrayList<>();
		visitedVertexes.add(startVertex);

		ArrayDeque<Vertex> queue = new ArrayDeque<>(graph.getVertexesBelongingToVertex(startVertex).values());
		int counterVertex = 1;
		vertexesWide = new LinkedHashMap<>();
		edgesWide = new LinkedHashMap<>();
		vertexesColor = new LinkedHashMap<>();
		edgesColor = new LinkedHashMap<>();
		vertexesWide.put(startVertex, counterVertex);
		while (queue.size() > 0) {
			counterVertex++;
			Vertex currentVertex = queue.pollFirst();
			vertexesWide.put(currentVertex, counterVertex);
			for (Entry<Integer, Vertex> entry : graph.getVertexesBelongingToVertex(currentVertex).entrySet()) {
				if (!visitedVertexes.contains(entry.getValue())) {
					queue.addLast(entry.getValue());
				}
			}
			visitedVertexes.add(currentVertex);
		}
		ArrayList<Vertex> tempVertexes = new ArrayList<>(vertexesWide.keySet());
		int counterEdges = 0;
		for (int i = 1; i < tempVertexes.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (graph.getEdge(tempVertexes.get(j), tempVertexes.get(i)) != null) {
					counterEdges++;
					edgesWide.put(counterEdges, graph.getEdge(tempVertexes.get(j), tempVertexes.get(i)));
					break;
				} else if (graph.getEdge(tempVertexes.get(i), tempVertexes.get(j)) != null && !graph.getEdge(tempVertexes.get(i), tempVertexes.get(j)).isOrientated()) {
					counterEdges++;
					edgesWide.put(counterEdges, graph.getEdge(tempVertexes.get(i), tempVertexes.get(j)));
					break;
				}
			}
		}
		vertexesWide.forEach((ver, color) -> vertexesColor.put(ver, ver.getFillColorDefault()));
		edgesWide.forEach((count, edge) -> edgesColor.put(edge, edge.getColorEdge()));
		setChoseModeOn();
	}

	@Override
	public <T extends GraphElement> void choseAlgorithmElement(T graphElement) {
		if ((graphElement == null)) {
			if (chosenGraphElement != null)
				setChoseModeOff();
			chosenGraphElement = null;
			// vertexContextMenu.hide();
			return;
		}
		// Если выбран графический элемент, а прошлый элемент пустой
		if (chosenGraphElement != null) {
			setChoseModeOff();
			if (graphElement.equals(chosenGraphElement)) {
				chosenGraphElement = null;
				return;
			}
			// chosenObject.setStyle("-fx-stroke: #4682B4; -fx-fill: #1e90ff;
			// -fx-stroke-width: 4px;");s
		}
		if (graphElement instanceof Vertex) {
			chosenGraphElement = graphElement;
			startVertex = (Vertex) graphElement;
			buildAlgorithm();
		}
	}

	@Override
	public boolean isAlgorithmActive() {
		return algorithmActive;
	}

	@Override
	public void setAlgorithmActive(boolean algorithmActive) {
		if (algorithmActive) {
			graph.getGraphPane().getHelpNote().setNote("Выберите начальную вершину");
			changeHandlersBuilder.changeHandlersWideDepth();
		} else {
			graph.getGraphPane().getHelpNote().setNote(HelpNote.DEFAULT_TEXT);
			if (this.algorithmActive)
				changeHandlersBuilder.bringBackHandlersWideDepth();
			setChoseModeOff();
		}
		this.algorithmActive = algorithmActive;
	}
	
	@Override
	public void setChoseModeOff() {
		if (transitionsList != null)
			transitionsList.forEach(Animation::stop);
		for (Entry<Integer, Vertex> entry : graph.getVertexes().entrySet()) {
			if (vertexesColor != null && vertexesColor.containsKey(entry.getValue()))
				entry.getValue().getVertexCircle().setFill(Color.valueOf(vertexesColor.get(entry.getValue())));
			entry.getValue().getVertexCircle().setStyle(entry.getValue().getDefaultStyle());
		}
		if (edgesColor != null)
			edgesColor.forEach(Edge::setColorEdge);
		if (setStepWideSearch != null) {
			setStepWideSearch.forEach(node -> {
				for (Entry<Vertex, Integer> entry : vertexesWide.entrySet()) {
					entry.getKey().getGroupVertex().getChildren().remove(node);
				}
			});
		}
	}
	private HashSet<SequentialTransition> transitionsList;
	@Override
	public void setChoseModeOn() {
		transitionsList = new HashSet<>();
		setStepWideSearch = new HashSet<>();
		startVertex.getVertexCircle()
				.setStyle("-fx-fill: #FFC618; -fx-stroke: #FF0000; -fx-stroke-width: 3px; -fx-smooth: true;");
		Runnable task = () -> {
			int countMillis = 0;
			int countVertex = 0;
			for (Entry<Vertex, Integer> entry : vertexesWide.entrySet()) {
				StackPane paneStepSearch = new StackPane();
				paneStepSearch.setPrefSize(18, 4);
				paneStepSearch.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 4px;");

				InnerShadow innerShadowEffect = new InnerShadow();
				innerShadowEffect.setBlurType(BlurType.THREE_PASS_BOX);
				innerShadowEffect.setChoke(0);
				innerShadowEffect.setWidth(10.0);
				innerShadowEffect.setHeight(10.0);
				innerShadowEffect.setRadius(3.);
				innerShadowEffect.setColor(Color.BLACK);
				paneStepSearch.setEffect(innerShadowEffect);

				Label stepWideSearch = new Label(String.valueOf(countVertex));
				// stepWideSearch.setLabelFor(entry.getKey().getVertexCircle());
				stepWideSearch.setFont(new Font("Roboto Bold", 13));
				// stepWideSearch.setStyle("-fx-background-color: #00BFFF;
				// -fx-background-radius: 5px;");
				setStepWideSearch.add(paneStepSearch);

				paneStepSearch.setLayoutX(entry.getKey().getVertexCircle().getBoundsInLocal().getMinX()
						- paneStepSearch.getPrefWidth());
				paneStepSearch.setLayoutY(entry.getKey().getVertexCircle().getBoundsInLocal().getMinY()
						- paneStepSearch.getPrefHeight()/2);

				paneStepSearch.getChildren().add(stepWideSearch);
				entry.getKey().getGroupVertex().getChildren().add(paneStepSearch);
				if (entry.getKey() != startVertex) {
					PauseTransition pauseTransition = new PauseTransition();
					pauseTransition.setDuration(Duration.millis(countMillis));
					int finalCountVertex = countVertex;
					pauseTransition.setOnFinished(val -> {
						Edge edgeFill = edgesWide.get(finalCountVertex);
						if (edgeFill != null) {
							edgeFill.setColorEdge("#FFC618");
						}
					});
					FillTransition fillTransition = new FillTransition(Duration.millis(500), entry.getKey().getVertexCircle(), Color.valueOf(entry.getKey().getFillColorDefault()), Color.rgb(230, Math.min((3 + countVertex * 12), 255), Math.min((3 + countVertex * 8), 255)));
					SequentialTransition sequentialTransition = new SequentialTransition(
							pauseTransition, fillTransition);
					transitionsList.add(sequentialTransition);

					sequentialTransition.play();
					countMillis += 200;
				}
				//lastVertex = entry.getKey();
				countVertex++;
				// System.out.println(entry.getKey().getVertexCircle().getFill().toString());
			}
		};
		Platform.runLater(task);
	}
}
