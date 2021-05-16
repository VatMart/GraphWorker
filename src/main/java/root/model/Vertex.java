package root.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import root.GraphWorkerApplication;
import root.components.ChoseGraphElement;
import root.components.GraphElement;

public class Vertex extends GraphElement implements ChoseGraphElement {

	@Override
	public Node getNode() {
		return groupVertex;
	}

	private class VertexSettingWindow {

		private Stage settingWindow;

		private VBox rootPane;

		private SplitPane splitPane;

		private FlowPane flowPane;
		private VBox samplePane;
		private HBox hBox;
		private Button bToDefault;
		private Button bSave;

		private Button bCancel;

		private Pane pane;

		private Label lMainColor;

		private Label lBorderColor;
		private Label lTextColor;
		private Label lNameVertex;
		private CheckBox cbToAllVertex;

		private CheckBox cbEffectOnText;

		private ColorPicker cpMainColor;

		private ColorPicker cpBorderColor;
		private ColorPicker cpTextColor;
		private TextField tfName;
		// private Group gVertexSample;
		private Circle cVertexSample;
		private Text nameSample;
		private Label lWidthBorder;
		private Label lSizeFont;
		private Label lSizeVertex;
		private Slider sWidthBorder;
		private Slider sSizeFont;

		private Slider sSizeVertex;

		private String choosenMainColor = getFillColorDefault();

		private String choosenBorderColor = getStrokeColorDefault();
		private String choosenTextColor = getFillColorName();

		private boolean isToAll = false;
		private String newName = getTextNumber().getText();
		private double fontSize = getDefaultSizeText();
		private double strokeWidth = getDefaultStrokeWidth();
		private double radius = getDefaultRadius();
		private boolean shadowOn = isShadowOn();

		public VertexSettingWindow() {
			buildInterface();
			buildFunctionalities();
		}

		private FlowPane buildFlowPane() {
			flowPane = new FlowPane();
			flowPane.setVgap(3);
			flowPane.setPadding(new Insets(5, 2, 2, 5));
			flowPane.setPrefSize(176, 151);
			flowPane.setMinSize(176, 151);
			flowPane.setMaxSize(176, 151);

			flowPane.getChildren().addAll(initiateFlowPaneElements());
			return flowPane;
		}

		private void buildFunctionalities() {
			cpMainColor.valueProperty().addListener((obs, oldv, newv) -> {
				cVertexSample.setFill(newv);
				choosenMainColor = "rgb(" + (newv.getRed() * 100) + "%," + (newv.getGreen() * 100) + "%,"
						+ (newv.getBlue() * 100) + "%)";
				// System.out.println(choosenMainColor);
			});
			cpBorderColor.valueProperty().addListener((obs, oldv, newv) -> {
				cVertexSample.setStroke(newv);
				choosenBorderColor = "rgb(" + (newv.getRed() * 100) + "%," + (newv.getGreen() * 100) + "%,"
						+ (newv.getBlue() * 100) + "%)";
			});
			cpTextColor.valueProperty().addListener((obs, oldv, newv) -> {
				// nameSample.setFill(newv);
				choosenTextColor = "rgb(" + (newv.getRed() * 100) + "%," + (newv.getGreen() * 100) + "%,"
						+ (newv.getBlue() * 100) + "%)";
				String regex = "-fx-fill:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(nameSample.getStyle());
				nameSample.setStyle(m.replaceAll("-fx-fill: " + choosenTextColor + ";"));
			});
			cbToAllVertex.selectedProperty().addListener((obs, oldv, newv) -> isToAll = newv);
			cbEffectOnText.selectedProperty().addListener((obs, oldv, newv) -> {
				String shadow = "-fx-effect: dropshadow(one-pass-box,black,5,0,0,1);";
				String regex = "-fx-effect:\\sdropshadow\\(one-pass-box,black,5,0,0,1\\);";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(nameSample.getStyle());
				if (newv) {
					nameSample.setStyle(nameSample.getStyle() + " " + shadow);
				} else {
					if (m.find()) {
						nameSample.setStyle(m.replaceAll(""));
					}
				}
				shadowOn = newv;
			});
			tfName.textProperty().addListener((obs, oldv, newv) -> {
				if (newv.length() > 0) {
					nameSample.setText(newv);
					newName = newv;
				}
			});
			// provided
// filter
			UnaryOperator<TextFormatter.Change> filterDelimited = t -> {
				if ((tfName.getText().length() > 18 && t.isAdded()))
					return null;
				return t;
			};
			TextFormatter<String> formatterDelimited = new TextFormatter<>(filterDelimited);
			tfName.setTextFormatter(formatterDelimited);

			sWidthBorder.valueProperty().addListener((obs, o, n) -> {
				strokeWidth = (Double) n;
				cVertexSample.setStrokeWidth(strokeWidth);
			});
			sSizeFont.valueProperty().addListener((obs, o, n) -> {
				fontSize = (Double) n;
				nameSample.setFont(new Font("Calibri bold", fontSize));
			});
			sSizeVertex.valueProperty().addListener((obs, o, n) -> {
				radius = (Double) n;
				cVertexSample.setRadius(radius);
			});

			bToDefault.setOnAction(event -> {
				nameSample.setStyle(Vertex.TEXT_STYLE_DEFAULT);
				cVertexSample.setStyle(graph.getDefaultStyleVertex());
				// choosenMainColor;
				String regex = "-fx-fill:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(cVertexSample.getStyle());
				if (m.find()) {
					choosenMainColor = graph.getDefaultStyleVertex().substring(m.start() + 10, m.end() - 1);
					cpMainColor.setValue(Color.web(choosenMainColor));
				}

				regex = "-fx-stroke:\\s[^-]+;";
				p = Pattern.compile(regex);
				m = p.matcher(cVertexSample.getStyle());
				if (m.find()) {
					choosenBorderColor = graph.getDefaultStyleVertex().substring(m.start() + 12, m.end() - 1);
					cpBorderColor.setValue(Color.web(choosenBorderColor));
				}

				regex = "-fx-fill:\\s[^-]+;";
				p = Pattern.compile(regex);
				m = p.matcher(nameSample.getStyle());
				if (m.find()) {
					choosenTextColor = Vertex.TEXT_STYLE_DEFAULT.substring(m.start() + 10, m.end() - 1);
					cpTextColor.setValue(Color.web(choosenTextColor));
				}
				sSizeFont.setValue(DEFAULT_SIZE_TEXT);
				sSizeVertex.setValue(DEFAULT_RADIUS);
				sWidthBorder.setValue(DEFAULT_STROKE_WIDTH);
				if (tfName.getText().length() > 1)
					tfName.deleteText(0, 1);
				tfName.setText(Integer.toString(getNumber()));
				cbEffectOnText.setSelected(false);
			});
			bCancel.setOnAction(event -> settingWindow.close());
			bSave.setOnAction(event -> {
				if (!isToAll) {
					setFillColorText(choosenTextColor);
					setFillColorVertexDefault(choosenMainColor);
					setStrokeColorDefault(choosenBorderColor);
					setDefaultSizeText(fontSize);
					setDefaultRadius(radius);
					setDefaultStrokeWidth(strokeWidth);
					shadowOnText(shadowOn);
				} else {
					graph.getVertexes().forEach((number, vertex) -> {
						vertex.setFillColorText(choosenTextColor);
						vertex.setFillColorVertexDefault(choosenMainColor);
						vertex.setStrokeColorDefault(choosenBorderColor);
						vertex.setDefaultSizeText(fontSize);
						vertex.setDefaultRadius(radius);
						vertex.setDefaultStrokeWidth(strokeWidth);
						vertex.shadowOnText(shadowOn);
					});
				}
				setNameVertex(newName);
				settingWindow.close();
			});
		}
		private HBox buildHBox() {
			hBox = new HBox(5);
			hBox.setPadding(new Insets(3));
			hBox.setPrefSize(300, 27);
			hBox.setMinSize(300, 27);
			hBox.getChildren().addAll(initiateHBoxElements());
			return hBox;
		}
		private void buildInterface() {
			Scene settingScene = new Scene(buildRootPane());

			settingWindow = new Stage();
			settingWindow.setResizable(false);
			settingWindow.initModality(Modality.WINDOW_MODAL);
			settingWindow.initOwner(GraphWorkerApplication.getInstance().getPrimaryStage());
			settingWindow.setScene(settingScene);
			settingWindow.setTitle("Настройка вершины");
//			settingWindow.setX(graph.getRightPane().localToScreen(graph.getRightPane().getBoundsInLocal()).getMinX()
//					+ graph.getRightPane().getPrefWidth() / 2 - rootPane.getPrefWidth() / 2);
//			settingWindow.setY(graph.getRightPane().localToScreen(graph.getRightPane().getBoundsInLocal()).getMinY()
//					+ graph.getRightPane().getPrefHeight() / 2 - rootPane.getPrefHeight() / 2);
			settingWindow.centerOnScreen();
		}
		private VBox buildRootPane() {
			rootPane = new VBox();
			rootPane.setPrefSize(300, 180);
			rootPane.setMinSize(300, 180);
			rootPane.setMaxSize(300, 180);
			rootPane.getChildren().addAll(buildSplitPane(), buildHBox());
			return rootPane;
		}
		private VBox buildSamplePane() {
			samplePane = new VBox();
			samplePane.setAlignment(Pos.TOP_CENTER);
			samplePane.setPrefSize(116, 151);
			samplePane.setMinSize(116, 151);
			samplePane.setPadding(new Insets(3, 3, 0, 3));
			Separator separator = new Separator(Orientation.HORIZONTAL);
			separator.setPrefSize(110, 14);
			lWidthBorder = new Label("Толщина обводки");
			lWidthBorder.setFont(new Font("System", 11));
			sWidthBorder = new Slider(1, 3, getDefaultStrokeWidth());
			lSizeFont = new Label("Размер шрифта");
			lSizeFont.setFont(new Font("System", 11));
			sSizeFont = new Slider(12, 24, getDefaultSizeText());
			lSizeVertex = new Label("Размер вершины");
			lSizeVertex.setFont(new Font("System", 11));
			sSizeVertex = new Slider(9, 18, getDefaultRadius());
			samplePane.getChildren().addAll(buildSampleVertex(), separator, lWidthBorder, sWidthBorder, lSizeFont,
					sSizeFont, lSizeVertex, sSizeVertex);
			return samplePane;
		}
		private StackPane buildSampleVertex() {
			cVertexSample = new Circle(getVertexCircle().getRadius(), getVertexCircle().getFill());
			cVertexSample.setStyle(getDefaultStyle());
			nameSample = new Text(getTextNumber().getText());
			nameSample.setFont(new Font(getTextNumber().getFont().getName(), getTextNumber().getFont().getSize()));
			nameSample.setStyle(getTextStyle());
			// gVertexSample = new Group(cVertexSample, nameSample);
			StackPane stackPane = new StackPane(cVertexSample, nameSample);
			stackPane.setPrefSize(samplePane.getPrefWidth(), 40);
			return stackPane;
		}
		private SplitPane buildSplitPane() {
			splitPane = new SplitPane();
			splitPane.setDividerPositions(0.602);
			splitPane.setPrefSize(300, 153);
			splitPane.getItems().addAll(buildFlowPane(), buildSamplePane());
			return splitPane;
		}
		private ArrayList<Node> initiateFlowPaneElements() {
			lMainColor = new Label("Основной цвет:");
			lBorderColor = new Label("Цвет обводки:");
			lTextColor = new Label("Цвет текста:");
			lNameVertex = new Label("Имя вершины:");
			cbToAllVertex = new CheckBox("Применить ко всем");
			cbEffectOnText = new CheckBox("Тень от имени");
			cpMainColor = new ColorPicker(Color.web(getFillColorDefault()));
			cpBorderColor = new ColorPicker(Color.web(getStrokeColorDefault()));
			cpTextColor = new ColorPicker(Color.web(getFillColorName()));
			tfName = new TextField(textNumber.getText());

			lMainColor.setPrefSize(96, 17);
			lBorderColor.setPrefSize(96, 17);
			lTextColor.setPrefSize(96, 17);
			lNameVertex.setPrefSize(96, 17);
			cbToAllVertex.setPrefSize(124, 16);
			cbToAllVertex.setFont(new Font("System", 11));
			cbEffectOnText.setFont(new Font("System", 11));
			cbEffectOnText.setSelected(isShadowOn());
			cbEffectOnText.setPrefSize(110, 16);
			cpMainColor.setPrefSize(41, 25);
			cpBorderColor.setPrefSize(41, 25);
			cpTextColor.setPrefSize(41, 25);
			tfName.setPrefSize(62, 18);
			tfName.setMinSize(62, 18);
			return new ArrayList<Node>(Arrays.asList(lMainColor, cpMainColor, lBorderColor, cpBorderColor, lTextColor,
					cpTextColor, cbEffectOnText, lNameVertex, tfName, cbToAllVertex));
		}
		private ArrayList<Node> initiateHBoxElements() {
			bToDefault = new Button("По умолчанию");
			bSave = new Button("Сохранить");
			bCancel = new Button("Отмена");
			pane = new Pane();

			bToDefault.setFont(new Font("System", 11));
			bToDefault.setPrefSize(95, 21);
			bToDefault.setMinHeight(21);
			bSave.setFont(new Font("System", 11));
			bSave.setDefaultButton(true);
			bSave.setPrefSize(71, 21);
			bSave.setMinHeight(21);
			bCancel.setFont(new Font("System", 11));
			bCancel.setCancelButton(true);
			bCancel.setPrefSize(59, 21);
			bCancel.setMinHeight(21);
			pane.setPrefSize(54, 21);
			return new ArrayList<Node>(Arrays.asList(bToDefault, pane, bSave, bCancel));
		}

		public void show() {
			settingWindow.show();
		}

	}
	private class PathVertex extends Path {
		
	}
	// id ������� TODO
	// private int id;
	// ������ ������� (���������� + �����) // -fx-fill: #006FFF;
//	public static String graph.getDefaultStyleVertex() = "-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width: 1.5px; -fx-smooth: true;";
	private static String TOP_CHOOSED_STYLE_DEFAULT = "-fx-fill: #FFFFFF; -fx-stroke: #4682B4; -fx-stroke-width: 3px; -fx-smooth: true;";
	public static final String TEXT_STYLE_DEFAULT = "-fx-fill: #000000;";
	public static final double DEFAULT_RADIUS = 13;
	public static final double DEFAULT_SIZE_TEXT = 15;
	public static final double DEFAULT_STROKE_WIDTH = 1.5;
	// ����� �������
	private final int number;
	// ���������� �������
	private Point2D coordinates;

	// ����������, ������������ ������� �� �����
	private Circle vertexCircle;
	// �����, ������������ �� �������
	private Text textNumber;
	private Graph graph;
	private Group groupVertex;
	private boolean isChosedModeOn = false;
	private double defaultRadius = 13;// 13
	private double defaultSizeText = 15;

	private double defaultStrokeWidth = 1.5;
	private boolean isShadowOn = false;
	private String defaultStyle;// "-fx-fill: #FFFFFF; -fx-stroke: #000000; -fx-stroke-width: " + defaultStrokeWidth
		//	+ "px; -fx-smooth: true;";
	//#6EC5FF
	private String choosedStyle;

	private String textStyle = "-fx-fill: #000000;";

	// ����������, ������������ � ������ ��������
	private Circle creatingEdgeCircle;

	private ContextMenu vertexContextMenu;

	public Vertex(Graph graph, int number) {
		this(graph, number, null, null);
	}

	public Vertex(Graph graph, int number, Double x, Double y) {
		this.graph = graph;
		this.number = number;
		if (x != null && y != null)
			this.coordinates = new Point2D(x, y);
		else this.coordinates = generateVertexCoordinates();
		initiateVertex();
		addVertexToGraph();
	}

	private void initiateVertex() {
		groupVertex = new Group();
		defaultStyle = graph.getDefaultStyleVertex();
		choosedStyle = TOP_CHOOSED_STYLE_DEFAULT;
		textNumber = new Text(Integer.valueOf(getNumber()).toString());
		textNumber.setFont(new Font("Calibri bold", defaultSizeText));
		vertexCircle = new Circle(defaultRadius);
	}

	// ��������� ������� (����������) � ����
	public void addVertexToGraph() {
		if (coordinates == null || graph == null) return;
			setCoordinates(coordinates.getX(), coordinates.getY());
			setChoseModeOff();
			initiateHandlers();
			groupVertex.getChildren().addAll(vertexCircle, textNumber);
			graph.getGraphPane().getChildren().add(groupVertex);
			groupVertex.toFront();
	}

	public EventHandler<MouseEvent> getChoseVertexHandler() {
		return choseVertexHandler;
	}

	private EventHandler<MouseEvent> choseVertexHandler;

	private void initiateHandlers() {
		initiateContextMenu();
		groupVertex.setOnContextMenuRequested(event -> vertexContextMenu.show(vertexCircle, Side.RIGHT, 3, -5));
		choseVertexHandler = event -> {
			graph.choseObject(Vertex.this);
			graph.getMultipleMovingGraphElements().getLastMouseLocation().x = event.getX();
			graph.getMultipleMovingGraphElements().getLastMouseLocation().y = event.getY();
		};
		this.getGroupVertex().addEventHandler(MouseEvent.MOUSE_PRESSED, choseVertexHandler);

		//initiateVertexMovingHandler();
		//root.components.NodeHandler.getInstance().add(this.getGroupVertex(), vertexMovingHandler, MouseEvent.MOUSE_DRAGGED);
		//this.getGroupVertex().addEventHandler(MouseEvent.MOUSE_DRAGGED, vertexMovingHandler);
	}

	// BETA
	private Point2D generateVertexCoordinates() {
		// centre x,y : xcentre = 342.5 + x; ycentre = 304 - y;
		Random random = new Random();
		Double minDistance = 60.; // ����������� ���������� �� ������ ������
		int countOfNull = 0;
		// ������� ������ � ������������������ ������������
		for (Entry<Integer, Vertex> entry : graph.getVertexes().entrySet()) {
			if (entry.getValue().getCoordinates() == null) {
				countOfNull++;
			}
		}
		// ���� ��������� ������� �� ����� ��������� - ����������� �������
		if (countOfNull == graph.getVertexes().size()) {
			return new Point2D(342.5, 304);
		}
		double x;
		double y;
		do {
			int numberOfCircle = random.nextInt((5 - 1) + 1) + 1; // ��������� ����� ���������� (��. circle �
																	// root.model.Graph.java)
			double angle = Math.toRadians(random.nextInt((360 - 1) + 1) + 1);
			x = 342.5 + (numberOfCircle * 50) * Math.cos(angle);
			y = 304 + (numberOfCircle * 50) * Math.sin(angle);
		} while (!isMoreThanMinDistance(new Point2D(x, y), minDistance));
		return new Point2D(x, y);
	}

	public String getChoosedStyleVertex() {
		return choosedStyle;
	}

	public Point2D getCoordinates() {
		return coordinates;
	}

	public Circle getCreatingEdgeCircle() {
		return creatingEdgeCircle;
	}

	public double getDefaultRadius() {
		return defaultRadius;
	}

	public double getDefaultSizeText() {
		return defaultSizeText;
	}

	public double getDefaultStrokeWidth() {
		return defaultStrokeWidth;
	}

	public String getDefaultStyle() {
		return defaultStyle;
	}

	public String getFillColorDefault() {
		String color = "";
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(defaultStyle);
		if (m.find()) {
			color = defaultStyle.substring(m.start() + 10, m.end() - 1);
		}
		// System.out.println("FILL COLOR " + color);
		return color;
	}

	public String getFillColorName() {
		String color = "";
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(textStyle);
		if (m.find()) {
			color = textStyle.substring(m.start() + 10, m.end() - 1);
		}
		// System.out.println("FILL COLOR TEXT " + color);
		return color;
	}

	public Group getGroupVertex() {
		return groupVertex;
	}

	public int getNumber() {
		return number;
	}

	public String getStrokeColorDefault() {
		String color = "";
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(defaultStyle);
		if (m.find()) {
			color = defaultStyle.substring(m.start() + 12, m.end() - 1);
		}
		// System.out.println("STROKE COLOR " + color);
		return color;
	}

	public Text getTextNumber() {
		return textNumber;
	}

	public String getTextStyle() {
		return textStyle;
	}

	public Circle getVertexCircle() {
		return vertexCircle;
	}

	// ���������� true, ���� ����� ��������� �������
	public boolean isChosedModeOn() {
		return isChosedModeOn;
	}

	// �������������� ����� ��� generateVertexCoordinates
	private boolean isMoreThanMinDistance(Point2D point, Double minDistance) {
		ArrayList<Vertex> otherVertexsCoordinates = new ArrayList<Vertex>(graph.getVertexes().values());
		for (Vertex vertex : otherVertexsCoordinates) {
			if (vertex.getCoordinates() != null) {
				double distance = vertex.getCoordinates().distance(point);
				if (distance < minDistance) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isShadowOn() {
		return isShadowOn;
	}

	// ������� ������� (����������) �� �����
	public void removeVertex() {
		setChoseModeOff();
		graph.getGraphPane().getChildren().remove(groupVertex);
		if (getCreatingEdgeCircle() != null) {
			graph.getGraphPane().getChildren().remove(getCreatingEdgeCircle());
		}
	}

	public void setChoosedStyle(String choosedStyleVertex) {
		if (choosedStyleVertex != null && vertexCircle != null) {
			this.choosedStyle = choosedStyleVertex;
			if (graph.getChosenGraphElements().contains(this))
				vertexCircle.setStyle(choosedStyleVertex);
		}
	}

	@Override
	public void setChoseModeOff() {
		isChosedModeOn = false;
		vertexCircle.setStyle(defaultStyle);
		// defaultStyleVertex = vertexCircle.getStyle();
		textNumber.setStyle(textStyle);
		if (creatingEdgeCircle != null) {
			graph.getGraphPane().getChildren().remove(creatingEdgeCircle);
		}
	}

	@Override
	public void setChoseModeOn() {
		isChosedModeOn = true;
		vertexCircle.setStyle(choosedStyle);
		if (creatingEdgeCircle != null) {
			graph.getGraphPane().getChildren().remove(creatingEdgeCircle);
		}
		creatingEdgeCircle = new Circle(
				vertexCircle.getCenterX() + vertexCircle.getRadius()/* coordinates.getX() + vertexCircle.getRadius() / 2 */,
				vertexCircle.getCenterY(), 3.5);
		creatingEdgeCircle.setStyle("-fx-fill: RED; -fx-stroke: #000000; -fx-stroke-width: 1px;");
		setCreatingEdgeCircleHandlers();
		if (graph.getGraphPane().getChildren().contains(getGroupVertex())) {
			graph.getGraphPane().getChildren().add(getCreatingEdgeCircle());
		}
	}

	// ��������� ������������ ���� �� ������
	private void initiateContextMenu() {
		vertexContextMenu = new ContextMenu();
		// MenuItem addEdgeItem = new MenuItem("�������� �����");
		MenuItem settingVertexItem = new MenuItem("Настройка вершины");
		settingVertexItem.setOnAction(event -> {
			VertexSettingWindow windowSettings = new VertexSettingWindow();
			windowSettings.show();
		});
		MenuItem deleteVertexItem = new MenuItem("Удалить вершину");
		deleteVertexItem.setOnAction(event -> graph.removeVertex(number));
		MenuItem createLoopVertexItem = new MenuItem("Создать петлю");
		createLoopVertexItem.setOnAction(list -> graph.addEdge(getNumber(), getNumber()));
		vertexContextMenu.getItems().addAll(deleteVertexItem, settingVertexItem, createLoopVertexItem);
	}

	public void setCoordinates(Double x, Double y) {
		getVertexCircle().setCenterX(x);
		getVertexCircle().setCenterY(y);
		if (getCreatingEdgeCircle() != null) {
			getCreatingEdgeCircle().setCenterX(vertexCircle.getCenterX() + vertexCircle.getRadius());
			getCreatingEdgeCircle().setCenterY(vertexCircle.getCenterY());
		}
		getTextNumber().setX(x);
		getTextNumber().setY(y);
		getTextNumber().setX(getTextNumber().getX() - getTextNumber().getLayoutBounds().getWidth() / 2);
		getTextNumber().setY(getTextNumber().getY() + getTextNumber().getLayoutBounds().getHeight() / 4);
		if (!graph.getEdgesBelongingToVertexWithoutOrientation(Vertex.this).isEmpty()) {
			for (Entry<Edge, Integer> entry : graph.getEdgesBelongingToVertexWithoutOrientation(Vertex.this).entrySet()) {
				graph.getEdges().get(entry.getKey().getName()).MovingEdgeAfterVertex();
			}
		}
		coordinates = new Point2D(x, y);
	}

	private void setCreatingEdgeCircleHandlers() {
		Line tempLineChangingVertex = new Line();
		{
			tempLineChangingVertex.getStrokeDashArray().addAll(8., 4.);
		}
		creatingEdgeCircle.setOnMouseDragged(event -> {
			Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
			Point2D local = graph.getGraphPane().screenToLocal(mouse.x, mouse.y);
			tempLineChangingVertex.setStartX(vertexCircle.getCenterX());
			tempLineChangingVertex.setStartY(vertexCircle.getCenterY());
			tempLineChangingVertex.setEndX(local.getX());
			tempLineChangingVertex.setEndY(local.getY());
			creatingEdgeCircle.setCenterX(local.getX());
			creatingEdgeCircle.setCenterY(local.getY());
			if (!graph.getGraphPane().getChildren().contains(tempLineChangingVertex)) {
				graph.getGraphPane().getChildren().add(tempLineChangingVertex);
			}
		});
		creatingEdgeCircle.setOnMouseReleased(event -> {
			if (vertexCircle.intersects(creatingEdgeCircle.getBoundsInLocal())) {
				graph.getGraphPane().getChildren().remove(tempLineChangingVertex);
				creatingEdgeCircle.setCenterX(vertexCircle.getCenterX() + vertexCircle.getRadius());
				creatingEdgeCircle.setCenterY(vertexCircle.getCenterY());
				return;
			}
			for (Entry<Integer, Vertex> entryVertex : graph.getVertexes().entrySet()) {
				if (entryVertex.getValue().getVertexCircle().intersects(creatingEdgeCircle.getBoundsInLocal())) {
					graph.addEdge(number, entryVertex.getValue().number);
					// rewriteTextAreaEdges(number, entryVertex.getValue().number);
					graph.getGraphPane().getChildren().remove(tempLineChangingVertex);
					creatingEdgeCircle.setCenterX(vertexCircle.getCenterX() + vertexCircle.getRadius());
					creatingEdgeCircle.setCenterY(vertexCircle.getCenterY());
					return;
				}
			}
			creatingEdgeCircle.setCenterX(vertexCircle.getCenterX() + vertexCircle.getRadius());
			creatingEdgeCircle.setCenterY(vertexCircle.getCenterY());
			graph.getGraphPane().getChildren().remove(tempLineChangingVertex);
		});
	}

	public void setDefaultRadius(double defaultRadius) {
		this.defaultRadius = defaultRadius;
		vertexCircle.setRadius(defaultRadius);
		setCoordinates(getCoordinates().getX(), getCoordinates().getY());
		graph.getEdgesBelongingToVertexWithoutOrientation(this).forEach((edge, pos) -> edge.MovingEdgeAfterVertex());
	}

	public void setDefaultSizeText(double defaultSizeText) {
		this.defaultSizeText = defaultSizeText;
		textNumber.setFont(new Font("Calibri bold", defaultSizeText));
	}

	public void setDefaultStrokeWidth(double defaultStrokeWidth) {
		this.defaultStrokeWidth = defaultStrokeWidth;
		String regex = "-fx-stroke-width:\\s\\d+\\.?\\d*px;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(defaultStyle);
		setDefaultStyle(m.replaceAll("-fx-stroke-width: " + defaultStrokeWidth + "px;"));
		m = p.matcher(choosedStyle);
		setChoosedStyle(m.replaceAll("-fx-stroke-width: " + (defaultStrokeWidth + 1) + "px;"));
	}

	public void setDefaultStyle(String defaultStyleVertex) {
		if (defaultStyleVertex != null && vertexCircle != null) {
			this.defaultStyle = defaultStyleVertex;
			if (!graph.getChosenGraphElements().contains(this))
				vertexCircle.setStyle(defaultStyleVertex);
		}
	}

	public void setFillColorText(String color) {
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(textStyle);
		setTextStyle(m.replaceAll("-fx-fill: " + color + ";"));
	}

	public void setFillColorVertexDefault(String color) {
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(defaultStyle);
		setDefaultStyle(m.replaceAll("-fx-fill: " + color + ";"));
		m = p.matcher(choosedStyle);
		setChoosedStyle(m.replaceAll("-fx-fill: " + color + ";"));
	}
	
	public static void setColorNewVertex(String color) {
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		//Matcher m = p.matcher(graph.getDefaultStyleVertex());
		//graph.setDefaultStyleVertex(m.replaceAll("-fx-fill: " + color + ";"));
		Matcher m = p.matcher(TOP_CHOOSED_STYLE_DEFAULT);
		TOP_CHOOSED_STYLE_DEFAULT = (m.replaceAll("-fx-fill: " + color + ";"));
	}

	public void setNameVertex(String name) {
		// TODO
		textNumber.setText(name);
		textNumber.setX(vertexCircle.getCenterX() - textNumber.getLayoutBounds().getWidth() / 2);
		textNumber.setY(vertexCircle.getCenterY() + textNumber.getLayoutBounds().getHeight() / 4);
	}

	public void setStrokeColorDefault(String color) {
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(defaultStyle);
		setDefaultStyle(m.replaceAll("-fx-stroke: " + color + ";"));
	}

	public void setTextStyle(String textStyle) {
		if (textStyle != null && textNumber != null) {
			this.textStyle = textStyle;
			textNumber.setStyle(textStyle);
		}
	}

	public void shadowOnText(boolean show) {
		isShadowOn = show;
		String shadow = "-fx-effect: dropshadow(one-pass-box,black,5,0,0,1);";
		String regex = "-fx-effect:\\sdropshadow\\(one-pass-box,black,5,0,0,1\\);";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(textStyle);
		if (!show) {
			if (m.find()) {
				setTextStyle(m.replaceAll(""));
			}
		} else {
			setTextStyle(getTextStyle() + " " + shadow);
		}
	}

	@Override
	public String toString() {
		return Integer.toString(getNumber());
	}
}
//getNodeHandlers().add(vertex, , MouseEvent.MOUSE_CLICKED);