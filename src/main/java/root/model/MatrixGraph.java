package root.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import root.components.Pair;
import root.model.Edge;
import root.model.Graph;
import root.model.Vertex;

public class MatrixGraph implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum TypeMatrix {
		INCIDENCE, ADJACENCY
	}
	private static boolean isArrayStrokeMatrixAdj(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (!matrix[i][j].equals("0")) {
					if (!(matrix[j][i].equals(matrix[i][j]) || matrix[j][i].equals("0"))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	private static boolean isArrayStrokeMatrixInc(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (!matrix[i][j].equals("0")) {
					String val = matrix[i][j];
					switch (val) {
					case "2":
						for (int c = 0; c < matrix.length; c++) {
							if (c != i)
							if (!matrix[c][j].equals("0")) {
								return false;
							}
						}
						break;
					case "1":
						int count = 0;
						for (int c = 0; c < matrix.length; c++) {
							if (c != i) {
								if (matrix[c][j].equals("1") || matrix[c][j].equals("-1")) {
									count++;
								}
								if (matrix[c][j].equals("2")) return false;
							}
						}
						if (count == 0 || count > 1) return false;
						break;
					case "-1":
						int count2 = 0;
						for (int c = 0; c < matrix.length; c++) {
							if (c != i) {
								if (matrix[c][j].equals("1")) {
									count2++;
								}
							}
						}
						if (count2 == 0 || count2 > 1) return false;
						break;
					default:
						break;
					}
				}
			}
		}
		return true;
	}
	/**
	 * ��������� �������� �� ������ stroke �������� ���� type
	 * 
	 * @param stroke
	 * @param type  - ��� �������
	 * @return true - ��������, false - ���
	 */
	public static boolean isMatrix(String stroke, TypeMatrix type) {
		if (stroke.isEmpty()) return false;
		if (type == TypeMatrix.ADJACENCY) {
			return isMatrixAdj(stroke);
		} else if (type == TypeMatrix.INCIDENCE) {
			return isMatrixInc(stroke);
		}
		return false;
	}
	private static boolean isMatrixAdj(String stroke) {
		String regexSplit = "[,;\\s]{1}[\\s\n]*"; // �����������
		String regex = "\\d+";
		Pattern p = Pattern.compile(regexSplit);
		String[] matrVal = p.split(stroke);
		if (matrVal.length == 0) return false;
		if (matrVal[0].equals("")) {
			matrVal = Arrays.copyOfRange(matrVal, 1, matrVal.length);
		}
		//System.out.println(Arrays.toString(matrVal));
		if ((Math.sqrt(matrVal.length) - (int) Math.sqrt(matrVal.length) != 0))
			return false;
		for (String str : matrVal)
			if (str.isEmpty())
				return false;
		if (matrVal.length == 1 && matrVal[0].matches("\\d+"))
			if (Integer.parseInt(matrVal[0]) != 0 && Integer.parseInt(matrVal[0]) != 1)
				return false;

		int quanityRows = (int) Math.sqrt(matrVal.length);
		// System.out.println(Arrays.toString(matrVal));
		p = Pattern.compile(regex);
		String[][] matrix = new String[quanityRows][quanityRows];
		int row = 0;
		int column = 0;
		for (int i = 0; i < matrVal.length; i++) {
			Matcher m = p.matcher(matrVal[i]);
			if (column > (quanityRows - 1)) {
				column = column - quanityRows;
				row++;
			}
			if (m.matches()) {
				if (matrVal[i].length() > 1 && matrVal[i].charAt(0) == '0')
					return false;
				// System.out.println(matrVal[i]+true);
				matrix[row][column] = matrVal[i];
				column++;
			} else {
				// System.out.println(matrVal[i]+false);
				return false;
			}
		}
		if (!isArrayStrokeMatrixAdj(matrix))
			return false;
		// System.out.println(Arrays.deepToString(matrix));
		//System.out.println("������ �������� �������� ��������� " + true);
		return true;
	}
	/*
	 * Краткий алгоритм (без проверок на каждом шаге):
	 * 1. разбиваем строку на строки матрицы;
	 * 2. из каждой строки получаем массив значений
	 * 3. подсчитываем количество вершин и граней
	 * 4. составляем матрицу
	 * 5. проверяем значения элементов на корректность
	 */
	private static boolean isMatrixInc(String stroke) {
		if (stroke.isEmpty()) return false;
		while (stroke.charAt(0) == ' ') {
			if (stroke.length() > 1)
				stroke = stroke.substring(1);
			else return false;
		}
		String regexSplitOnRow = "\n"; // разделитель на строки матрицы
		String regexSplitOnVal = "[,;\\s]{1}[\\s\n]*"; // разделитель на значения строки
		String regexVal = "-?\\d+";
		Pattern p = Pattern.compile(regexSplitOnRow);
		String[] matrRows = p.split(stroke);
		int quanityRows = matrRows.length; // вершины
		if (quanityRows < 1) return false;
		p = Pattern.compile(regexSplitOnVal);
		int quanityColumns = p.split(matrRows[0]).length; // грани
		if (quanityColumns < 1) return false;
		String[][] matrix = new String[quanityRows][quanityColumns];
		Pattern pValue = Pattern.compile(regexVal);
		for (int row = 0; row < quanityRows; row++) {
			if (matrRows[row].isEmpty()) return false;
			while (matrRows[row].charAt(0) == ' ') {
				if (matrRows[row].length() > 1)
					matrRows[row] = matrRows[row].substring(1);
				else return false;
			}
			String[] rowVal = p.split(matrRows[row]);
			if (rowVal.length != quanityColumns) return false;
			for (int column = 0; column < rowVal.length; column++) {
				Matcher m = pValue.matcher(rowVal[column]);
				if (m.matches()) {
					if (Arrays.asList(-1, 0, 1, 2).contains(Integer.valueOf(rowVal[column]))) {
						matrix[row][column] = rowVal[column];
					} else return false;
				} else return false;
			}
		}
		if (!isArrayStrokeMatrixInc(matrix)) return false;
		return true;
	}
	// public String matrix;
	public TypeMatrix typeMatrix;
	private Graph graph;

	private String matrixStroke;
	
	private String[][] matrix;

	private int quanityVertexs;

	private ArrayList<Pair<Integer>> edges;

	private HashMap<Pair<Integer>, Integer> weightEdges;

	private HashMap<Pair<Integer>, Graph.OrientationGraph> orientationEdges;
	
	private ObservableList<String> listVertexsPosition;

	public MatrixGraph(Graph graph, TypeMatrix type) {
		this.graph = graph; 
		typeMatrix = type;
	}

	public MatrixGraph(Graph graph, TypeMatrix type, String matrixStroke) {
		this.graph = graph; 
		typeMatrix = type;
		if (isMatrix(matrixStroke, type)) {
			this.matrixStroke = matrixStroke;
			solveGraphValues();
		}
	}

	// �������� ������� (������)
	private void addRowsToMatr() {
		listVertexsPosition = FXCollections.observableArrayList();
		TreeSet<Integer> vertexsKeySet = new TreeSet<>(graph.getVertexes().keySet());
		Iterator<Integer> it = vertexsKeySet.iterator();
		while (it.hasNext()) {
			int next = it.next();
			listVertexsPosition.add(Integer.toString(next));
			// System.out.println(listView.getItems());
			// System.out.println(Integer.toString(next));
		}
	}

	// �������� ����� (�������)
	private String buildColumnToMatrInc(Edge edge, String bildingMatrix) {
		StringBuffer sbBuildingMatrix = new StringBuffer(bildingMatrix);
		String vertexFirst = Integer.toString(edge.getVertexFirst().getNumber());
		String vertexSecond = Integer.toString(edge.getVertexSecond().getNumber());
		Pattern pNewLine = Pattern.compile("\n");
		Matcher mNewLine = pNewLine.matcher(sbBuildingMatrix.toString());
		int quanityVertexs = graph.getVertexes().size();
		int posVertexFirst = listVertexsPosition.indexOf(vertexFirst) + 1;
		int posVertexSecond = listVertexsPosition.indexOf(vertexSecond) + 1;
		if (sbBuildingMatrix.toString().equals("")) {
			for (int i = 1; i <= quanityVertexs; i++) {
				if (i == posVertexFirst) {
					if (edge.isOrientated()) {
						sbBuildingMatrix.append(" 1, \n");
					} else
						sbBuildingMatrix.append("1, \n");
				} else if (i == posVertexSecond) {
					if (edge.isOrientated()) {
						sbBuildingMatrix.append("-1, \n");
					} else
						sbBuildingMatrix.append("1, \n");
				} else {
					if (edge.isOrientated()) {
						sbBuildingMatrix.append(" 0, \n");
					} else
						sbBuildingMatrix.append("0, \n");
				}
			}
			// textArea.setText(textArea.getText().substring(0,
			// textArea.getText().length()-2));
		} else {
			int counter = 0;
			// NumberVertex, PositionTextArea
			TreeMap<Integer, Integer> hm = new TreeMap<>();
			while (mNewLine.find()) {
				// System.out.println("mNewLine.start() " + mNewLine.start());
				counter++;
				hm.put(counter, mNewLine.start());
			}
			for (int i = hm.size(); i >= 1; i--) {
				if (i == posVertexFirst) {
					if (edge.isOrientated()) {
						sbBuildingMatrix.insert(hm.get(i), " 1, ");
					} else
						sbBuildingMatrix.insert(hm.get(i), "1, ");
				} else if (i == posVertexSecond) {
					if (edge.isOrientated()) {
						sbBuildingMatrix.insert(hm.get(i), "-1, ");
					} else
						sbBuildingMatrix.insert(hm.get(i), "1, ");
				} else {
					if (edge.isOrientated()) {
						sbBuildingMatrix.insert(hm.get(i), " 0, ");
					} else
						sbBuildingMatrix.insert(hm.get(i), "0, ");
				}
			}
		}
		return sbBuildingMatrix.toString();
	}

	private String buildMatixInc() {
		TreeSet<Integer> vertexsKeySet = new TreeSet<>(graph.getVertexes().keySet());
		//graph.getTextAreaMatrix().setText("");
		String bildingMatrix = "";
		//graph.getListView().getItems().clear();
		addRowsToMatr();
		if (graph.getEdges().isEmpty())
			return bildingMatrix;
		// int sizeEdges = getEdges().size();
		Iterator<Integer> it2 = vertexsKeySet.iterator();
		HashSet<Edge> setEdge = new HashSet<>();
		while (it2.hasNext()) {
			int next = it2.next();
			for (Map.Entry<String, Edge> entry : graph.getEdges().entrySet()) {
				if (entry.getValue().isVertexAffiliation(next) && !setEdge.contains(entry.getValue())) {
					bildingMatrix = buildColumnToMatrInc(entry.getValue(), bildingMatrix);
					setEdge.add(entry.getValue());
				}
			}
		}
		if (bildingMatrix.length() > 0) {
			bildingMatrix = bildingMatrix.substring(0, bildingMatrix.length() - 2);
		}
		return bildingMatrix;
	}
	public String buildMatrix() {
		if (typeMatrix == TypeMatrix.INCIDENCE) {
			return buildMatixInc();
		}
		if (typeMatrix == TypeMatrix.ADJACENCY) {
			return buildMatrixAdj();
		}
		return null;
	}

	private String buildMatrixAdj() {
		TreeSet<Integer> vertexsKeySet = new TreeSet<>(graph.getVertexes().keySet());
		String buildingMatrix = "";
		addRowsToMatr();
		if (graph.getEdges().isEmpty())
			return "";
		Iterator<Integer> it2 = vertexsKeySet.iterator();
		while (it2.hasNext()) {
			int next = it2.next();
			buildingMatrix = buildRowToMatrAdj(graph.getVertexes().get(next), buildingMatrix);
		}
		if (buildingMatrix.length() > 0) {
			buildingMatrix = buildingMatrix.substring(0, buildingMatrix.length() - 2);
		}
		return buildingMatrix;
	}
	
	private String buildRowToMatrAdj(Vertex vertex, String bildingMatrix) {
		StringBuffer sbBuildingMatrix = new StringBuffer(bildingMatrix);
		HashMap<Edge, Integer> edgesBelongingToVertex = graph.getEdgesBelongingToVertexWithoutOrientation(vertex);
		HashMap<Integer, Edge> belongVertexsInt = new HashMap<Integer, Edge>();
		for (Map.Entry<Edge, Integer> entry : edgesBelongingToVertex.entrySet()) {
			if (entry.getValue().equals(1)) {
				belongVertexsInt.put(
						listVertexsPosition.indexOf(Integer.toString(entry.getKey().getVertexSecond().getNumber()))
								+ 1,
						entry.getKey());
			} else if (entry.getValue().equals(2) && !entry.getKey().isOrientated()) {
				belongVertexsInt.put(
						listVertexsPosition.indexOf(Integer.toString(entry.getKey().getVertexFirst().getNumber()))
								+ 1,
						entry.getKey());
			}
		}
		int quanityVertexs = graph.getVertexes().size();
		for (int i = 1; i <= quanityVertexs; i++) {
			if (belongVertexsInt.containsKey(i)) {
				sbBuildingMatrix.append(belongVertexsInt.get(i).getWeight() + ", ");
			} else {
				sbBuildingMatrix.append("0, ");
			}
		}
		sbBuildingMatrix.append("\n");
		return sbBuildingMatrix.toString();
	}

	// name; ����. "1 2"
	public ArrayList<Pair<Integer>> getEdgesFromMatrix() {

		return edges;
	}
	
	public ObservableList<String> getListVertexsPosition() {
		return listVertexsPosition;
	}

	public int getQuanityVertexsFromMatrix() {

		return quanityVertexs;
	}
	
	public HashMap<Pair<Integer>, Integer> getWeightEdgesFromMatrix() {

		return weightEdges;
	}

	public HashMap<Pair<Integer>, Graph.OrientationGraph> isOrientedEdgesFromMatrix() {

		return orientationEdges;
	}
	
	private void solveEdgesAdj() {
		edges = new ArrayList<Pair<Integer>>();
		weightEdges = new HashMap<Pair<Integer>, Integer>();
		orientationEdges = new HashMap<Pair<Integer>, Graph.OrientationGraph>();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (!matrix[i][j].equals("0") && !edges.contains(new Pair<Integer>(j + 1, i + 1))) {
					Pair<Integer> name = new Pair<Integer>((i + 1), (j + 1));
					edges.add(name);
					weightEdges.put(name, Integer.parseInt(matrix[i][j]));
					if (matrix[j][i].equals("0")) {
						orientationEdges.put(name, Graph.OrientationGraph.ORIENTED);
					} else
						orientationEdges.put(name, Graph.OrientationGraph.NON_ORIENTED);
				}
			}
		}
	}

	private void solveEdgesInc() {
		edges = new ArrayList<Pair<Integer>>();
		weightEdges = new HashMap<Pair<Integer>, Integer>();
		orientationEdges = new HashMap<Pair<Integer>, Graph.OrientationGraph>();
		for (int i = 0; i < matrix[0].length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (!matrix[j][i].equals("0")) {
					String val = matrix[j][i];
					switch (val) {
					case "2":
						Pair<Integer> name = new Pair<Integer>((j + 1), (j + 1));
						edges.add(name);
						weightEdges.put(name, 1);
						orientationEdges.put(name, Graph.OrientationGraph.NON_ORIENTED);
						break;
					case "1":
						for (int c = 0; c < matrix.length; c++) {
							if (c != j) {
								if (matrix[c][i].equals("1") && !edges.contains(new Pair<Integer>((j + 1), (c + 1)))) {
									Pair<Integer> name2 = new Pair<Integer>((j + 1), (c + 1));
									edges.add(name2);
									weightEdges.put(name2, 1);
									orientationEdges.put(name2, Graph.OrientationGraph.NON_ORIENTED);
								} else if (matrix[c][i].equals("-1")  && !edges.contains(new Pair<Integer>((j + 1), (c + 1)))) {
									Pair<Integer> name3 = new Pair<Integer>((j + 1), (c + 1));
									edges.add(name3);
									weightEdges.put(name3, 1);
									orientationEdges.put(name3, Graph.OrientationGraph.ORIENTED);
								}
							}
						}
						break;
					case "-1":
						for (int c = 0; c < matrix.length; c++) {
							if (c != j) {
								if (matrix[c][i].equals("1") && !edges.contains(new Pair<Integer>((c + 1), (j + 1)))) {
									Pair<Integer> name4 = new Pair<Integer>((c + 1), (j + 1));
									edges.add(name4);
									weightEdges.put(name4, 1);
									orientationEdges.put(name4, Graph.OrientationGraph.ORIENTED);
								}
							}
						}
						break;
					default:
						break;
					}
				}
			}
		}
	}

	private void solveGraphValues() {
		if (typeMatrix == TypeMatrix.ADJACENCY) {
			solveQuanityVertexsAdj();
			solveMatrixSrokesAdj();
			solveEdgesAdj();
		} else if (typeMatrix == TypeMatrix.INCIDENCE) {
			solveQuanityVertexsInc();
			solveMatrixSrokesInc();
			solveEdgesInc();
		}
	}

	private void solveMatrixSrokesAdj() {
		String regexSplit = "[,;\\s]{1}[\\s\n]*"; // �����������
		String regex = "\\d+";
		Pattern p = Pattern.compile(regexSplit);
		String[] matrVal = p.split(matrixStroke);
		if (matrVal[0].equals("")) {
			matrVal = Arrays.copyOfRange(matrVal, 1, matrVal.length);
		}
		p = Pattern.compile(regex);
		matrix = new String[quanityVertexs][quanityVertexs];
		int row = 0;
		int column = 0;
		for (int i = 0; i < matrVal.length; i++) {
			if (column > (quanityVertexs - 1)) {
				column = column - quanityVertexs;
				row++;
			}
			matrix[row][column] = matrVal[i];
			column++;
		}
	}

	private void solveMatrixSrokesInc() {
		while (matrixStroke.charAt(0) == ' ') {
			if (matrixStroke.length() > 1)
				matrixStroke = matrixStroke.substring(1);
		}
		String regexSplitOnRow = "\n"; // ����������� �� ������ �������
		String regexSplitOnVal = "[,;\\s]{1}[\\s\n]*"; // ����������� �� �������� ������
		String regexVal = "-?\\d+";
		Pattern p = Pattern.compile(regexSplitOnRow);
		String[] matrRows = p.split(matrixStroke);
		//System.out.println("������ �����: "+Arrays.toString(matrRows));
		int quanityRows = matrRows.length; // �������
		p = Pattern.compile(regexSplitOnVal);
		int quanityColumns = p.split(matrRows[0]).length; // �����
		//System.out.println("���-�� ������: "+quanityRows+"���-�� ������: "+quanityColumns);
		matrix = new String[quanityRows][quanityColumns];
		Pattern pValue = Pattern.compile(regexVal);
		for (int row = 0; row < quanityRows; row++) {
			while (matrRows[row].charAt(0) == ' ') {
				if (matrRows[row].length() > 1)
					matrRows[row] = matrRows[row].substring(1);
			}
			String[] rowVal = p.split(matrRows[row]);
			//System.out.println("������ ����� "+row+": "+Arrays.toString(rowVal));
			for (int column = 0; column < rowVal.length; column++) {
				Matcher m = pValue.matcher(rowVal[column]);
				if (m.matches()) {
					if (Arrays.asList(-1, 0, 1, 2).contains(Integer.valueOf(rowVal[column]))) {
						matrix[row][column] = rowVal[column];
					} 
				} 
			}
		}
		//System.out.println(Arrays.deepToString(matrix));
	}

	private void solveQuanityVertexsAdj() {
		String regexSplit = "[,;\\s]{1}[\\s\n]*"; // �����������
		Pattern p = Pattern.compile(regexSplit);
		String[] matrVal = p.split(matrixStroke);
		quanityVertexs = (int) Math.sqrt(matrVal.length);
	}

	private void solveQuanityVertexsInc() {
		String regexSplitOnRow = "\n"; // ����������� �� ������ �������
		Pattern p = Pattern.compile(regexSplitOnRow);
		String[] matrRows = p.split(matrixStroke);
		quanityVertexs = matrRows.length; // �������
		
	}
}
