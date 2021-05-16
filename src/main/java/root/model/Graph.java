package root.model;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import root.components.*;
import root.view.GraphPane;

//1 2; 3 4; 5 2; 6 7; 8 9;
public class Graph {

	public enum OrientationGraph implements Serializable {
		ORIENTED, NON_ORIENTED, MIXED
	}

	private GraphPane graphPane;
	private final ObservableMap<Integer, Vertex> vertexes;
	private final ObservableMap<String, Edge> edges;

	//
	private final Add_DeleteEdgeMode add_DeleteEdgeMode = new Add_DeleteEdgeMode(this);
	private final Add_DeleteVertexMode add_DeleteVertexMode = new Add_DeleteVertexMode(this);

	private String defaultStyleVertex = "-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width: 1.5px; -fx-smooth: true;";

	private final SimpleObjectProperty<OrientationGraph> orientation = new SimpleObjectProperty<>(
			OrientationGraph.ORIENTED);
	private SimpleBooleanProperty showNamesEdges = new SimpleBooleanProperty(false);
	private boolean showWeightsEdges = true;

	public boolean isMultipleChoosingOn() {
		return multipleChoosingOn;
	}

	public void multipleChoosingOn(boolean multipleChoosingOn) {
		this.multipleChoosingOn = multipleChoosingOn;
	}

	private boolean multipleChoosingOn;
	private ObservableList<GraphElement> chosenGraphElements = FXCollections.observableArrayList();
	public final MatrixGraph matrix = new MatrixGraph(this, MatrixGraph.TypeMatrix.INCIDENCE);
	private AlgorithmGraph algorithm;

	private MatrixGraph.TypeMatrix typeMatrix = MatrixGraph.TypeMatrix.ADJACENCY;

	public MultipleMovingGraphElements getMultipleMovingGraphElements() {
		return multipleMovingGraphElements;
	}

	private final MultipleMovingGraphElements multipleMovingGraphElements;

	public Graph() {
		vertexes = FXCollections.observableHashMap();
		edges = FXCollections.observableHashMap();
        multipleMovingGraphElements = new MultipleMovingGraphElements(this);
	}

	public void setGraphPane(GraphPane graphPane) {
		this.graphPane = graphPane;
	}

	public void addEdge(Edge edge) {
		if (!getEdges().containsValue(edge)) {
			getEdges().putIfAbsent(edge.getName(), edge);
		}
	}

	public void addEdge(int numberVertexFirst, int numberVertexSecond) {
		if (!isEdgesContains(numberVertexFirst, numberVertexSecond)) {
			getEdges().putIfAbsent(numberVertexFirst + " " + numberVertexSecond,
					new Edge(this, getVertexes().get(numberVertexFirst), getVertexes().get(numberVertexSecond),
							numberVertexFirst + " " + numberVertexSecond));
		}
	}

	public void addEdge(int numberVertexFirst, int numberVertexSecond, int weight, OrientationGraph orientation) {
		if (!isEdgesContains(numberVertexFirst, numberVertexSecond)) {
			getEdges().putIfAbsent(numberVertexFirst + " " + numberVertexSecond,
					new Edge(this, getVertexes().get(numberVertexFirst), getVertexes().get(numberVertexSecond),
							numberVertexFirst + " " + numberVertexSecond, weight, orientation));
		}
	}

	public void addVertex(int number) {
		if (!getVertexes().containsKey(number)) {
			getVertexes().put(number, new Vertex(this, number));
		}
	}

	public void addVertex(int number, double x, double y) {
		if (!getVertexes().containsKey(number)) {
			getVertexes().put(number, new Vertex(this, number, x, y));
		}
	}

	public void buildEdges(String strokeEdges) { // "1 2; 3 4;" -> {root.model.Edge, root.model.Edge}
		String[] pairs = splitStringVertexes(strokeEdges);
		ArrayList<String> pairss = new ArrayList<>();
		for (int i = 0; i < pairs.length - 1; i++) {
			int first = pairFactoryVertexes(pairs[i]).getFirst();
			int second = pairFactoryVertexes(pairs[i]).getSecond();
			pairss.add(first + " " + second);
			// System.out.println("????: firs: " + first + " second: " + second);
			if (getVertexes().containsKey(first) && getVertexes().containsKey(second)) {
				if (!getEdges().containsKey(first + " " + second)) {
					addEdge(first, second);
				}
			} else {
				System.out.println("??????? " + (getVertexes().containsKey(first) ? first : " ")
						+ (getVertexes().containsKey(second) ? second : " ") + " ?? ??????????");
			}
		}
		ArrayList<String> deleteTemp = new ArrayList<>();
		for (Entry<String, Edge> entry : getEdges().entrySet()) {
			if (!pairss.contains(entry.getKey())) {
				deleteTemp.add(entry.getKey());
			}
		}
		for (String s : deleteTemp) {
			removeEdge(s);
			// edges.remove(deleteTemp.get(i));
		}
	}

	public void buildGraphFromMatrix(MatrixGraph matrix) {
		clearGraph();
		int quantityVertex = matrix.getQuanityVertexsFromMatrix();
		for (int i = 0; i < quantityVertex; i++) {
			addVertex(i + 1);
		}
		ArrayList<Pair<Integer>> edges = matrix.getEdgesFromMatrix();
		HashMap<Pair<Integer>, Integer> weightEdges = matrix.getWeightEdgesFromMatrix();
		HashMap<Pair<Integer>, OrientationGraph> orientationEdges = matrix.isOrientedEdgesFromMatrix();
		edges.forEach(name -> addEdge(name.getFirst(), name.getSecond(), weightEdges.get(name), orientationEdges.get(name)));
	}

	public void buildVertexes(int quantityVertex) {
		if (getVertexes().size() == 0) {
			for (int i = 0; i < quantityVertex; i++) {
				if (!getVertexes().containsKey(i + 1)) {
					addVertex(i + 1);
				}
			}
			return;
		}
		if (getVertexes().size() < quantityVertex) {
			for (int i = 0; i < quantityVertex; i++) {
				if (getVertexes().size() < quantityVertex) {
					if (!getVertexes().containsKey(i + 1)) {
						addVertex(i + 1);
					}
				}
			}
			return;
		}
		while (getVertexes().size() > quantityVertex) {
			if (getVertexes().containsKey(getVertexes().size())) {
				ArrayList<String> deletedEdges = new ArrayList<>();
				for (Entry<String, Edge> entry : getEdges().entrySet()) {
					if (entry.getValue().getVertexFirst().getNumber() == getVertexes().size()
							|| entry.getValue().getVertexSecond().getNumber() == getVertexes().size()) {
						deletedEdges.add(entry.getKey());
						// getEdges().get(entry.getKey()).removeEdge();
					}
				}
				for (String deletedEdge : deletedEdges) {
					removeEdge(deletedEdge);
					// getEdges().remove(deletedEdges.get(i));
				}
				// textAreaEdges.setText(rewriteTextArea);
				// getVertexs().get(getVertexs().size()).removeVertex();
				// getVertexs().remove(getVertexs().size());
				removeVertex(getVertexes().size());
			}
		}
	}

	public void choseObject(GraphElement graphElement) {
		if (add_DeleteEdgeMode.isModeActive()) {
			add_DeleteEdgeMode.choseAdd_DeleteEdge(graphElement);
			return;
		}
		if (algorithm != null && algorithm.isAlgorithmActive()) {
			algorithm.choseAlgorithmElement(graphElement);
			return;
		}
		if ((graphElement == null)) {
			if (!getChosenGraphElements().isEmpty()) {
				clearChoseElements();
			}
			return;
		}
		if (isMultipleChoosingOn() && !getChosenGraphElements().contains(graphElement)) {
			addChoseElement(graphElement);
			return;
		}
		if (!getChosenGraphElements().contains(graphElement) && !isMultipleChoosingOn()) {
			clearChoseElements();
			addChoseElement(graphElement);
			return;
		}
        if (getChosenGraphElements().contains(graphElement) && isMultipleChoosingOn()) {
			removeChoseElement(graphElement);
        }
	}

	public void clearGraph() {
		ArrayList<String> keysEdges = new ArrayList<>(getEdges().keySet());
		ArrayList<Integer> keysVertexes = new ArrayList<>(getVertexes().keySet());
		for (String keysEdge : keysEdges) {
			removeEdge(keysEdge);
		}
		for (Integer keysVertex : keysVertexes) {
			removeVertex(keysVertex);
		}
		// getEdges().clear();
		// getVertexs().clear();
	}

	private ArrayList<Pair<Integer>> generateEdgesStroke(int quantityEdges, int quantityVertex) {
		Random random = new Random();
		ArrayList<Pair<Integer>> pairs = new ArrayList<>(quantityEdges);
		ArrayList<String> ocEdges = new ArrayList<>();
		for (int i = 0; i < quantityEdges; i++) {
			int first = random.nextInt(quantityVertex) + 1;
			int second = random.nextInt(quantityVertex) + 1;
			Pair<Integer> pair = new Pair<>(first, second);
			while (first == second
					|| (ocEdges.contains(first + " " + second) || ocEdges.contains(second + " " + first))) {
				first = new Random().nextInt(quantityVertex) + 1;
				second = new Random().nextInt(quantityVertex) + 1;
				pair = new Pair<>(first, second);
			}
			// System.out.println("????? " + first + " " + second);
			pairs.add(pair);
			ocEdges.add(first + " " + second);
		}
		return pairs;
	}

	public void generateRandomGraph() {
		clearGraph();
		Random random = new Random();
		// 5 .. 25
		int maxVertexes = 15;
		int minVertexes = 5;
		int quantityVertex = random.nextInt((maxVertexes - minVertexes) + 1) + minVertexes;
		for (int i = 0; i < quantityVertex; i++) {
			addVertex(i + 1);
		}
		// 4..maxEdges
		int maxEdges = (quantityVertex * (quantityVertex - 1)) / 2;
		if (maxEdges > 15) {
			maxEdges = 15;
		}
		int minEdges = 4;
		int quantityEdges = random.nextInt((maxEdges - minEdges) + 1) + minEdges;
		ArrayList<Pair<Integer>> pairsEdges = generateEdgesStroke(quantityEdges, quantityVertex);
		for (int i = 0; i < quantityEdges; i++) {
			addEdge(pairsEdges.get(i).getFirst(), pairsEdges.get(i).getSecond());
		}
	}

	public AlgorithmGraph getAlgorithm() {
		return algorithm;
	}

	public ObservableList<GraphElement> getChosenGraphElements() {
		return chosenGraphElements;
	}

	public void addChoseElement(GraphElement graphElement) {
		graphElement.setChoseModeOn();
		getChosenGraphElements().add(graphElement);
		multipleMovingGraphElements.makeDraggable(graphElement);
	}

	public void removeChoseElement(GraphElement graphElement) {
		graphElement.setChoseModeOff();
		multipleMovingGraphElements.removeDraggable(graphElement);
		getChosenGraphElements().remove(graphElement);
	}

	public void clearChoseElements() {
		getChosenGraphElements().forEach(graphElement -> {
			graphElement.setChoseModeOff();
			multipleMovingGraphElements.removeDraggable(graphElement);
		});
		getChosenGraphElements().clear();
	}

	public void deleteChoosesElements() {
		getChosenGraphElements().forEach(graphElement -> {
			if (graphElement instanceof Vertex) removeVertex((Vertex) graphElement);
			if (graphElement instanceof Edge) {
				if (getEdges().containsValue((Edge) graphElement))
					removeEdge((Edge) graphElement);
			}
		});
		getChosenGraphElements().clear();
	}

	public void choseAllElements() {
	    getVertexes().forEach((num, vertex) -> {
	        if (!getChosenGraphElements().contains(vertex))
	            addChoseElement(vertex);
        });
	    getEdges().forEach((s, edge) -> {
	        if (!getChosenGraphElements().contains(edge))
	            addChoseElement(edge);
        });
    }

	public String getDefaultStyleVertex() {
		return defaultStyleVertex;
	}

	public ObservableMap<String, Edge> getEdges() {
		return edges;
	}

	/** Возвращает HashMap граней инцидентных вершине (ребро, номер вершины в
	 * грани(1 или 2)) (ВСЕ, БЕЗ УЧЕТА ОРИЕНТИРОВАННОСТИ)
	 */
	public HashMap<Edge, Integer> getEdgesBelongingToVertexWithoutOrientation(Vertex vertex) {
		HashMap<Edge, Integer> edgesBelongingToVertex = new HashMap<>();
		for (Entry<String, Edge> entry : getEdges().entrySet()) {
			if ((entry.getValue().getVertexFirst().equals(vertex)
					|| entry.getValue().getVertexSecond().equals(vertex))) {
				edgesBelongingToVertex.put(getEdges().get(entry.getKey()),
						vertex.equals(entry.getValue().getVertexFirst()) ? 1 : 2);
			}
		}
		return edgesBelongingToVertex;
	}
	/** Возвращает HashMap граней инцидентных вершине (ребро, номер вершины в
	 * грани(1 или 2)) (С учетом ориентированности)
	 */
	public LinkedHashMap<Edge, Integer> getEdgesBelongingToVertexTakingOrientation(Vertex vertex) {
		LinkedHashMap<Edge, Integer> edgesBelongingToVertex = new LinkedHashMap<>();
		for (Entry<String, Edge> entry : getEdges().entrySet()) {
			if (entry.getValue().getVertexFirst().equals(vertex) || (entry.getValue().getVertexSecond().equals(vertex) && !entry.getValue().isOrientated())) {
				edgesBelongingToVertex.put(getEdges().get(entry.getKey()),
						vertex.equals(entry.getValue().getVertexFirst()) ? 1 : 2);
			}
		}
		return edgesBelongingToVertex;
	}

	public SimpleObjectProperty<OrientationGraph> getOrientationProperty() {
		return orientation;
	}

	public GraphPane getGraphPane() {
		return graphPane;
	}

	public SimpleBooleanProperty getShowNamesEdges() {
		return showNamesEdges;
	}

	public ObservableMap<Integer, Vertex> getVertexes() {
		return vertexes;
	}

	/**Возвращает HashMap вершин смежных данной вершине т.е.
	 * вершины соединенные (номер вершины, вершина)
	 */
	public HashMap<Integer, Vertex> getVertexesBelongingToVertex(Vertex vertex) {
		HashMap<Integer, Vertex> vertexesBelongingToVertex = new HashMap<>();
		HashMap<Edge, Integer> edgesBelongingToVertex = getEdgesBelongingToVertexTakingOrientation(vertex);
		for (Entry<Edge, Integer> entry : edgesBelongingToVertex.entrySet()) {
			if (entry.getKey().getVertexFirst() == vertex) {
				vertexesBelongingToVertex.put(entry.getKey().getVertexSecond().getNumber(),
						entry.getKey().getVertexSecond());
			} else {
				if (entry.getKey().getVertexSecond() == vertex) {
					vertexesBelongingToVertex.put(entry.getKey().getVertexFirst().getNumber(),
							entry.getKey().getVertexFirst());
				}
			}
		}
		return vertexesBelongingToVertex;
	}

	public Edge getEdge(Vertex v1, Vertex v2) {
		String name = v1.getNumber() + " " + v2.getNumber();
		return getEdges().get(name);
	}

	public Edge getEdge(int v1, int v2) {
		String name = v1 + " " + v2;
		return getEdges().get(name);
	}

	public MatrixGraph.TypeMatrix getTypeMatrix() {
		return typeMatrix;
	}

	public boolean isEdgesContains(Vertex vertexStart, Vertex vertexEnd) {
		for (Entry<String, Edge> entry : getEdges().entrySet()) {
			if (entry.getValue().getVertexFirst() == vertexStart && entry.getValue().getVertexSecond() == vertexEnd)
				return true;
		}
		return false;
	}

	public boolean isEdgesContains(int vertexStart, int vertexEnd) {
		for (Entry<String, Edge> entry : getEdges().entrySet()) {
			if (entry.getValue().getVertexFirst().getNumber() == vertexStart
					&& entry.getValue().getVertexSecond().getNumber() == vertexEnd)
				return true;
		}
		return false;
	}

	public boolean isHasCorrectFormatEdges(String stroke) {
		if (stroke.equals("") || Pattern.matches("\\s*", stroke))
			return true;
		return Pattern.matches("((\n*\\s*\n*\\d+\n*\\s+\n*\\d+\n*\\s*\n*([,;])\n*\\s*\n*)+)", stroke);
	}

	public boolean isShowWeightsEdges() {
		return showWeightsEdges;
	}

	public boolean isVertexesContains(String strokeVertexes) {
		String[] pairs = splitStringVertexes(strokeVertexes);
		for (int i = 0; i < pairs.length - 1; i++) {
//			if (pairFactoryVertexs(pairs[i]) == null) {
//				return false;
//			}
			if (pairFactoryVertexes(pairs[i]) == null) return false;
			int first = pairFactoryVertexes(pairs[i]).getFirst();
			int second = pairFactoryVertexes(pairs[i]).getSecond();
			if (!(getVertexes().containsKey(first) && getVertexes().containsKey(second))) {
				// System.out.println("????: " + pairs[i] + "?? ???????? ???????");
				return false;
			}
		}
		return true;
	}

	public static Pair<Integer> pairFactoryVertexes(String stroke) {
		// stroke.replaceAll("\n", " ");
		StringBuffer strokeBuffer = new StringBuffer(Pattern.compile("\n").matcher(stroke).replaceAll(" ")),
				first, second;

		try {
            while (strokeBuffer.charAt(0) == ' ' || (strokeBuffer.length() > 0 && strokeBuffer.charAt(strokeBuffer.length() - 1) == ' ')) {
                if (strokeBuffer.charAt(0) == ' ')
                    strokeBuffer.deleteCharAt(0);
                if (strokeBuffer.length() > 0 && strokeBuffer.charAt(strokeBuffer.length() - 1) == ' ')
                    strokeBuffer.deleteCharAt(strokeBuffer.length() - 1);
            }
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
		
		// System.out.println("Stroke = " + strokeBuffer);
		if (strokeBuffer.indexOf(" ") == -1) {
			return null;
		}
		first = new StringBuffer(strokeBuffer.substring(0, strokeBuffer.indexOf(" ")));
		strokeBuffer.delete(0, strokeBuffer.indexOf(" "));
		strokeBuffer = new StringBuffer(Pattern.compile("\\s").matcher(strokeBuffer).replaceAll(""));
		second = new StringBuffer(strokeBuffer);
		return new Pair<>(Integer.parseInt(first.toString()), Integer.parseInt(second.toString()));
	}

	public void removeEdge(String name) {
//		int vertex1Num = getEdges().get(name).getVertexFirst().getNumber();
//		int vertex2Num = getEdges().get(name).getVertexSecond().getNumber();
		getEdges().get(name).removeEdge();
		getEdges().remove(name);
	}

	public void removeEdge(Edge edge) {
		getEdges().get(edge.getName()).removeEdge();
		getEdges().remove(edge.getName());
	}

	public void removeVertex(int number) {
		if (getVertexes().containsKey(number)) {
			ArrayList<String> deletedEdges = new ArrayList<>();
			for (Entry<String, Edge> entry : getEdges().entrySet()) {
				if (entry.getValue().getVertexFirst().getNumber() == number
						|| entry.getValue().getVertexSecond().getNumber() == number) {
					deletedEdges.add(entry.getKey());
					getEdges().get(entry.getKey()).removeEdge();
				}
			}
			for (String deletedEdge : deletedEdges) {
				getEdges().remove(deletedEdge);
			}
			getVertexes().get(number).removeVertex();
			getVertexes().remove(number);
		}
	}

	public void removeVertex(Vertex vertex) {
		if (getVertexes().containsValue(vertex)) {
			ArrayList<String> deletedEdges = new ArrayList<>();
			for (Entry<String, Edge> entry : getEdges().entrySet()) {
				if (entry.getValue().getVertexFirst().getNumber() == vertex.getNumber()
						|| entry.getValue().getVertexSecond().getNumber() == vertex.getNumber()) {
					deletedEdges.add(entry.getKey());
					getEdges().get(entry.getKey()).removeEdge();
				}
			}
			for (String deletedEdge : deletedEdges) {
				getEdges().remove(deletedEdge);
			}
			getVertexes().get(vertex.getNumber()).removeVertex();
			getVertexes().remove(vertex.getNumber());
		}
	}

	public void toCompleteGraph() {
		for (Entry<Integer, Vertex> entry : getVertexes().entrySet()) {
			for (Entry<Integer, Vertex> entrySec : getVertexes().entrySet()) {
				if ((entry.getKey().intValue() != entrySec.getKey().intValue())
						&& !isEdgesContains(entry.getKey(), entrySec.getKey())
						&& !isEdgesContains(entrySec.getKey(), entry.getKey())) {
					addEdge(entry.getKey(), entrySec.getKey());
				}
			}
		}
	}

	public void setAlgorithm(AlgorithmGraph algorithm) {
		this.algorithm = algorithm;
	}

//	public void setChosenGraphElement(root.components.GraphElement chosenGraphElement) {
//		this.chosenGraphElement = chosenGraphElement;
//	}

	public void setDefaultStyleVertex(String defaultStyleVertex) {
		this.defaultStyleVertex = defaultStyleVertex;
		String color;
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(this.defaultStyleVertex);
		if (m.find()) {
			color = this.defaultStyleVertex.substring(m.start() + 10, m.end() - 1);
			Vertex.setColorNewVertex(color);
		}
//		getVertexs().forEach((number, vertex) -> {
//			vertex.setDefaultStyle(defaultStyleVertex);
//		});
	}

	public void setOrientation(OrientationGraph orientation) {
		this.orientation.set(orientation);
		if (orientation != OrientationGraph.MIXED)
			getEdges().forEach((name, edge) -> edge.setOrientationEdge(orientation));
		matrix.buildMatrix();
	}

	public void setShowNamesEdges(SimpleBooleanProperty showNamesEdges) {
		this.showNamesEdges = showNamesEdges;
	}

	public void setShowWeightsEdges(boolean showWeightsEdges) {
		this.showWeightsEdges = showWeightsEdges;
		if (showWeightsEdges) {
			getEdges().forEach((name, edge) -> edge.setShowWeight(true));
		} else
			getEdges().forEach((name, edge) -> edge.setShowWeight(false));
	}

	public void setTypeMatrix(MatrixGraph.TypeMatrix typeMatrix) {
		this.typeMatrix = typeMatrix;
	}

	public int solveNumberOfVertex() {
		for (int i = 0; i < getVertexes().size(); i++) {
			if (!getVertexes().containsKey(i + 1)) {
				return i + 1;
			}
		}
		return getVertexes().size() + 1;
	}

	private String[] splitStringVertexes(String strokeVertexes) {
		Pattern pairStringSplitEdgePattern = Pattern.compile("[,;]");
		return pairStringSplitEdgePattern.split(strokeVertexes, -1);
	}

	public Add_DeleteEdgeMode getAdd_DeleteEdgeMode() {
		return add_DeleteEdgeMode;
	}

	public Add_DeleteVertexMode getAdd_DeleteVertexMode() {
		return add_DeleteVertexMode;
	}
}