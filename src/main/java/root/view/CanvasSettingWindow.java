package root.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import root.GraphWorkerApplication;
import root.model.Edge;
import root.model.Graph;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CanvasSettingWindow extends Stage {
	private static CanvasSettingWindow instance;

	public static CanvasSettingWindow getInstance(Graph pane) {
		if (instance == null)
			instance = new CanvasSettingWindow(pane);
		return instance;
	}

	private final Graph graph;

	private FlowPane root;

	private CanvasSettingWindow(Graph pane) {
		super();
		this.graph = pane;
		initiate();
	}

	private void createRootPane() {
		root = new FlowPane(5, 5);
		root.setPrefHeight(325);
		root.setPrefWidth(400);
		root.minWidth(400);
		root.setMinHeight(325);
		root.setPadding(new Insets(10));
		initiateComponents();
		buildActionsComponents();
	}

	private void buildActionsComponents() {
		buildListViewActions();
		buildRadioButtonsActions();
		buildSpinnersActions();
		buildColorPickersActions();
	}

	private void buildListViewActions() {
		Dimension dA4 = new Dimension("A4                            729 x 1122 px", 729, 1122);
		Dimension dA0 = new Dimension("A0                            3178 x 4493 px", 3178, 4493);
		Dimension dA1 = new Dimension("A1                            2245 x 3178 px", 2245, 3178);
		Dimension dA2 = new Dimension("A2                            1587 x 2245 px", 1587, 2245);
		Dimension dA3 = new Dimension("A3                            1122 x 1587 px", 1122, 1587);
		Dimension dA5 = new Dimension("A5                            559 x 793 px", 559, 793);
		Dimension dA6 = new Dimension("A6                            396 x 559 px", 396, 559);
		Dimension dA7 = new Dimension("A7                            279 x 396 px", 279, 396);
		Dimension dA8 = new Dimension("A8                            196 x 279 px", 196, 279);
		ObservableList<Dimension> dimensions = FXCollections.observableArrayList(dA4, dA0, dA1, dA2, dA3, dA5, dA6, dA7,
				dA8);
		lvDimensions.setItems(dimensions);
		lvDimensions.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
			//graph.getRightPane().setSizeCanvas(n.width, n.height);
			if (rbPortret.isSelected()) {
				sWidth.getValueFactory().setValue((int) n.width);
				sHeight.getValueFactory().setValue((int) n.height);
			}
			if (rbAlbum.isSelected()) {
				sWidth.getValueFactory().setValue((int) n.height);
				sHeight.getValueFactory().setValue((int) n.width);
			}
			//root.view.DeskPane.getInstance(graph, root.view.MainPane.getInstance(graph)).getCentreButton().fire();
		});
	}

	private void buildRadioButtonsActions() {
		rbPortret.selectedProperty().addListener((obs, o, n) -> {
			if (n) {
				graph.getGraphPane().setOrientation(GraphPane.Orientation.PORTRAIT);
				int temp = sHeight.getValueFactory().getValue();
				sHeight.getValueFactory().setValue(sWidth.getValueFactory().getValue());
				sWidth.getValueFactory().setValue(temp);
			}
		});
		rbAlbum.selectedProperty().addListener((obs, o, n) -> {
			if (n) {
				graph.getGraphPane().setOrientation(GraphPane.Orientation.ALBUM);
				int temp = sHeight.getValueFactory().getValue();
				sHeight.getValueFactory().setValue(sWidth.getValueFactory().getValue());
				sWidth.getValueFactory().setValue(temp);
			}
		});
	}

	private void buildSpinnersActions() {
		sWidth.valueProperty().addListener((obs, o, n) -> {
			if (n == null) {
				sWidth.getValueFactory().setValue(100);
			} else {
				graph.getGraphPane().setWidthCanvas(n);
			}
		});
		sHeight.valueProperty().addListener((obs, o, n) -> {
			if (n == null) {
				sHeight.getValueFactory().setValue(100);
			} else {
				graph.getGraphPane().setHeightCanvas(n);
			}
		});
	}

	private void buildColorPickersActions() {
		cpBackground.valueProperty().addListener((obs, o, n) -> {
			String choosenColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
					+ (n.getBlue() * 100) + "%)";
			graph.getGraphPane().setColor(choosenColor);
		});
		cpEdge.valueProperty().addListener((obs, o, n) -> {
			String choosenColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
					+ (n.getBlue() * 100) + "%)";
			Edge.setColorNewEdges(choosenColor);
		});
		cpVertex.valueProperty().addListener((obs, o, n) -> {
			String choosenColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
					+ (n.getBlue() * 100) + "%)";
			String regex = "-fx-fill:\\s[^-]+;";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(graph.getDefaultStyleVertex());
			graph.setDefaultStyleVertex(m.replaceAll("-fx-fill: " + choosenColor + ";"));
		});
		bToCurrent.setOnAction(value -> {
			Color choosenColorEdge = cpEdge.getValue();
			String ColorEdge = "rgb(" + (choosenColorEdge.getRed() * 100) + "%," + (choosenColorEdge.getGreen() * 100) + "%,"
					+ (choosenColorEdge.getBlue() * 100) + "%)";
			Color choosenColorVertex = cpVertex.getValue();
			String ColorVertex = "rgb(" + (choosenColorVertex.getRed() * 100) + "%," + (choosenColorVertex.getGreen() * 100) + "%,"
					+ (choosenColorVertex.getBlue() * 100) + "%)";
			graph.getEdges().forEach((name, edge) -> {	
				edge.setColorEdge(ColorEdge);
			});
			graph.getVertexes().forEach((num, vertex) -> {
				vertex.setFillColorVertexDefault(ColorVertex);
			});
		});
	}
	
	private void initiate() {
		createRootPane();
		Scene scene = new Scene(root, 400, 325);
		String css = getClass().getResource("/application.css").toExternalForm();
		scene.getStylesheets().add(css);
		setScene(scene);
		setTitle("Настройка холста");
		setMinWidth(scene.getWidth());
		setMinHeight(scene.getHeight());
		centerOnScreen();
		//initStyle(StageStyle.UTILITY);
		initOwner(GraphWorkerApplication.getInstance().getPrimaryStage());
		setResizable(false);
	}

	private ListView<Dimension> lvDimensions;
	private RadioButton rbPortret;
	private RadioButton rbAlbum;
	private Spinner<Integer> sWidth;
	private Spinner<Integer> sHeight;
	private ColorPicker cpBackground;
	private ColorPicker cpEdge;
	private ColorPicker cpVertex;
	private Button bToCurrent;
	private CheckBox cbGrid;

	private void initiateComponents() {
		Label lSizeCanvas = new Label("Размер холста:");
		lSizeCanvas.setFont(new Font("System Bold", 12));
		lSizeCanvas.setPrefWidth(375);
		Pane p1 = new Pane();
		p1.setPrefSize(14, 75);
		createListView();
		Label lOrientationCanvas = new Label("Ориентация:");
		lOrientationCanvas.setFont(new Font("System Bold", 12));
		lOrientationCanvas.setPrefWidth(233);
		createRadioButtons();
		Pane p2 = new Pane();
		p2.setPrefSize(20, 25);
		Label lWidth = new Label("Ширина:");
		createSpinners();
		Label lpx1 = new Label("px");
		Label lHeight = new Label("Высота:");
		lHeight.setPrefWidth(64);
		lHeight.setAlignment(Pos.CENTER_RIGHT);
		Label lpx2 = new Label("px");
		Label lColors = new Label("Цвета:");
		lColors.setFont(new Font("System Bold", 12));
		lColors.setPrefWidth(375);
		Pane p3 = new Pane();
		p3.setPrefSize(20, 42);
		Label lColorBackground = new Label("Цвет фона:");
		createColorPickers();
		Label lColorEdge = new Label("Цвет граней:");
		lColorEdge.setPrefWidth(78);
		Label lColorVertex = new Label("Цвет вершин:");
		lColorVertex.setPrefWidth(78);
		bToCurrent = new Button("to current");
		BorderPane borderPane = new BorderPane();
		borderPane.setPrefSize(215, 44);
		borderPane.setPadding(new Insets(0, 5, 0, 0));
		FlowPane flowPane = new FlowPane(5, 5);
		flowPane.setPrefSize(161, 55);
		flowPane.setPadding(new Insets(5, 5, 5, 5));
		cbGrid = new CheckBox("Сетка");
		flowPane.getChildren().addAll(lColorEdge, cpEdge, lColorVertex, cpVertex);
		borderPane.setCenter(flowPane);
		borderPane.setRight(bToCurrent);
		BorderPane.setAlignment(bToCurrent, Pos.CENTER_LEFT);
		borderPane.setStyle("-fx-border-color: rgba(0,0,0,0.3); -fx-border-radius: 3;");
		root.getChildren().addAll(lSizeCanvas, p1, lvDimensions, lOrientationCanvas, rbPortret, rbAlbum, p2, lWidth,
				sWidth, lpx1, lHeight, sHeight, lpx2, lColors, p3, lColorBackground, cpBackground, borderPane, cbGrid);
	}

	private void createListView() {
		lvDimensions = new ListView<Dimension>();
		lvDimensions.setPrefSize(356, 112);
		lvDimensions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	private void createRadioButtons() {
		rbPortret = new RadioButton("Портрет");
		rbAlbum = new RadioButton("Альбом");
		rbPortret.setSelected(true);
		ToggleGroup tgOrientation = new ToggleGroup();
		rbPortret.setToggleGroup(tgOrientation);
		rbAlbum.setToggleGroup(tgOrientation);
	}
	
	private void createSpinners() {
		SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(
				100, 10000);
		SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(
				100, 10000);
		sWidth = new Spinner<Integer>(valueFactory1);
		sWidth.getStyleClass().add("spinner-qVertex");
		sHeight = new Spinner<Integer>(valueFactory2);
		sHeight.getStyleClass().add("spinner-qVertex");
		sWidth.setEditable(true);
		sHeight.setEditable(true);
		sWidth.setPrefSize(82, 25);
		sHeight.setPrefSize(82, 25);
		TextFormatter<String> formatterSpinner1 = new TextFormatter<String>(
				c -> c.getText().matches("[^0123456789]") ? null : c);
		TextFormatter<String> formatterSpinner2 = new TextFormatter<String>(
				c -> c.getText().matches("[^0123456789]") ? null : c);
		sWidth.getValueFactory().setValue(729);
		sHeight.getValueFactory().setValue(1122);
		sHeight.getEditor().setTextFormatter(formatterSpinner1);
		sWidth.getEditor().setTextFormatter(formatterSpinner2);
	}

	private void createColorPickers() {
		cpBackground = new ColorPicker(Color.web("#FFFFFF"));
		cpEdge = new ColorPicker(Color.web("#000000"));
		cpVertex = new ColorPicker(Color.web("#FFFFFF"));
		cpBackground.setPrefSize(41, 21);
		cpEdge.setPrefSize(41, 21);
		cpVertex.setPrefSize(41, 21);
	}

	public class Dimension {

		private double width;
		private double height;
		private String name;

		public Dimension(String name, double width, double height) {
			this.width = width;
			this.height = height;
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
