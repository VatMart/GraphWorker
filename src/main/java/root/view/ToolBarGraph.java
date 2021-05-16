package root.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import root.components.*;
import root.model.Graph;
import root.view.MainPane;

public class ToolBarGraph extends ToolBar {
//	private static root.view.ToolBarGraph instance;
//
//	public static root.view.ToolBarGraph getInstance(root.model.Graph graph, root.view.MainPane parent) {
//		if (instance == null)
//			instance = new root.view.ToolBarGraph(graph, parent);
//		return instance;
//	}
	private Graph graph;

	@SuppressWarnings("unused")
	private MainPane parent;

	private FlowPane paneCoordinateMouth;

	private Label xLabel;

	private Label yLabel;
	private Text textXMouth;
	private Text textYMouth;
	private ToggleButton buttonAddVertex;
	private ToggleButton buttonAddEdge;

	private Button buttonClearGraph;
	private MenuButton menuButtonShow;

	private CheckMenuItem checkNames;

	private CheckMenuItem showWeightsEdges;
	private SplitMenuButton pathButton;

	private String defaultStylePathButton;

	private MenuItem wideSearchItem;
	private MenuItem depthSearchItem;
	private MenuItem dijkstraItem;
	private MenuItem findCyclesItem;
	private MenuButton mbScale;
	private MenuItem mi800;
	private MenuItem mi400;
	private MenuItem mi200;
	private MenuItem mi150;
	private MenuItem mi125;
	private MenuItem mi100;
	private MenuItem mi75;
	private MenuItem mi50;
	private MenuItem mi25;

	public ToolBarGraph(Graph graph, MainPane parent) {
		super();
		this.graph = graph;
		this.parent = parent;
		initiate();
		initiateComponents();
	}

	private void createButtonClearGraph() {
		buttonClearGraph = new Button();
		buttonClearGraph.setTooltip(new Tooltip("Очистить холст"));
		buttonClearGraph.getStyleClass().add("button-custom");
		StackPane spClearIcon = new StackPane();
		spClearIcon.getStyleClass().add("shape-clear");
		buttonClearGraph.setGraphic(spClearIcon);
	}

	private void createMenuButtonScale() {
		mbScale = new MenuButton("100%");
		mbScale.setTooltip(new Tooltip("Масштаб холста"));
		StackPane spScaleIcon = new StackPane();
		spScaleIcon.getStyleClass().add("shapeScale");
		// mbScale.setStyle("-fx-background-color: transparent;");
		mbScale.setGraphic(spScaleIcon);
		mbScale.setGraphicTextGap(2.);
		mbScale.setContentDisplay(ContentDisplay.RIGHT);
		mbScale.getStyleClass().add("menuButtonScale");
		mi800 = new MenuItem("800%");
		mi400 = new MenuItem("400%");
		mi200 = new MenuItem("200%");
		mi150 = new MenuItem("150%");
		mi125 = new MenuItem("125%");
		mi100 = new MenuItem("100%");
		mi75 = new MenuItem("75%");
		mi50 = new MenuItem("50%");
		mi25 = new MenuItem("25%");
		mbScale.getItems().addAll(mi800, mi400, mi200, mi150, mi125, mi100, mi75, mi50, mi25);
	}

	private void createMenuButtonShow() {
		menuButtonShow = new MenuButton("Показать");
		menuButtonShow.setMinHeight(21);
		menuButtonShow.getStyleClass().add("menuButtonScale");
		checkNames = new CheckMenuItem("Имена граней");
		// TODO
		showWeightsEdges = new CheckMenuItem("Веса граней");

		menuButtonShow.getItems().add(showWeightsEdges);
		// checkNames
		menuButtonShow.getItems().add(checkNames);
	}

	private void createPaneCoordinateMouth() {
		paneCoordinateMouth = new FlowPane();
		paneCoordinateMouth.setPadding(new Insets(1, 0, 0, 4));
		paneCoordinateMouth.setAlignment(Pos.CENTER_LEFT);
		paneCoordinateMouth.setPrefWidth(92);
		paneCoordinateMouth.setMinHeight(18);
		paneCoordinateMouth.setStyle(
				"-fx-background-color: #F5F5F5; -fx-border-width: 1px; -fx-border-color: BLACK; -fx-background-radius: 5px; -fx-border-radius: 5px;");
		InnerShadow innerShadowEffect = new InnerShadow();
		innerShadowEffect.setBlurType(BlurType.THREE_PASS_BOX);
		innerShadowEffect.setChoke(0);
		innerShadowEffect.setWidth(12.0);
		innerShadowEffect.setHeight(12.0);
		innerShadowEffect.setRadius(5.);
		innerShadowEffect.setColor(Color.BLACK);
		paneCoordinateMouth.setEffect(innerShadowEffect);
		xLabel = new Label("X:");
		xLabel.setFont(new Font("Calibri Bold", 15));
		xLabel.setTextFill(Color.BLACK);
		xLabel.setPadding(new Insets(0, 0, 3, 0));
		paneCoordinateMouth.getChildren().add(xLabel);
		// textXMouth (rightPane.ToolBar.anchorPane)
		textXMouth = new Text("0000");
		getTextXMouth().setFont(new Font("Calibri Bold", 13));
		getTextXMouth().setLayoutX(20);
		getTextXMouth().setLayoutY(20);
		getTextXMouth().setWrappingWidth(30);
		paneCoordinateMouth.getChildren().add(getTextXMouth());
		// Label y (rightPane.ToolBar.anchorPane)
		yLabel = new Label("Y:");
		yLabel.setFont(new Font("Calibri Bold", 15));
		yLabel.setTextFill(Color.BLACK);
		yLabel.setPadding(new Insets(0, 0, 3, 0));
		paneCoordinateMouth.getChildren().add(yLabel);
		// textYMouth (rightPane.ToolBar.anchorPane)
		textYMouth = new Text("0000");
		getTextYMouth().setFont(new Font("Calibri Bold", 13));
		getTextYMouth().setLayoutX(63);
		getTextYMouth().setLayoutY(20);
		getTextYMouth().setWrappingWidth(30.);
		paneCoordinateMouth.getChildren().add(getTextYMouth());
	}

	private void createPathButton() {
		pathButton = new SplitMenuButton();
		pathButton.setTooltip(new Tooltip("Алгоритмы для поиска путей и т.п."));
		pathButton.getStyleClass().add("menu-split-button-alg");
		StackPane spAlgIcon = new StackPane();
		spAlgIcon.getStyleClass().add("shape-alg");
		pathButton.setGraphic(spAlgIcon);
		defaultStylePathButton = pathButton.getStyle();
		pathButton.setText("Поиск в ширину");
		wideSearchItem = new MenuItem("Поиск в ширину");
		depthSearchItem = new MenuItem("Поиск в глубину");
		dijkstraItem = new MenuItem("Поиск кратчайшего пути");
		findCyclesItem = new MenuItem("Поиск циклов");
		pathButton.getItems().addAll(wideSearchItem, depthSearchItem, dijkstraItem, findCyclesItem);
		ChangeHandlersBuilder.pathButton = pathButton;
	}

	private void createToggleButtonsVertexEdge() {
		ToggleGroup toggleButtonsAddGroup = new ToggleGroup();

		buttonAddVertex = new ToggleButton();
		buttonAddVertex.setTooltip(new Tooltip("Добавить/удалить вершину"));
		StackPane spAddVertexIcon = new StackPane();
		spAddVertexIcon.getStyleClass().add("shape-AddVertex");
		buttonAddVertex.getStyleClass().add("toggle-custom");
		buttonAddVertex.setGraphic(spAddVertexIcon);

		// ������-����� ����������-�������� ������
		buttonAddEdge = new ToggleButton();
		buttonAddEdge.setTooltip(new Tooltip("Добавить/удалить грань"));
		buttonAddEdge.getStyleClass().add("toggle-custom");
		StackPane spAddEdgeIcon = new StackPane();
		spAddEdgeIcon.getStyleClass().add("shape-AddEdge");
		buttonAddEdge.setGraphic(spAddEdgeIcon);

		buttonAddVertex.setToggleGroup(toggleButtonsAddGroup);
		buttonAddEdge.setToggleGroup(toggleButtonsAddGroup);
	}

	public Text getTextXMouth() {
		return textXMouth;
	}

	public Text getTextYMouth() {
		return textYMouth;
	}

	public MenuButton getMbScale() {
		return mbScale;
	}
	private void initiate() {
		setMinHeight(30);
		setMaxHeight(30);
		setPadding(new Insets(5));
		setStyle("-fx-background-color: #F5F5F5;");
		AnchorPane.setLeftAnchor(this, 250.);
		AnchorPane.setRightAnchor(this, 0.);
	}

	private void initiateComponents() {
		createPaneCoordinateMouth();
		createToggleButtonsVertexEdge();
		createButtonClearGraph();
		createMenuButtonShow();
		createPathButton();
		createMenuButtonScale();
		getItems().addAll(paneCoordinateMouth, buttonAddVertex, buttonAddEdge, buttonClearGraph, menuButtonShow,
				pathButton, mbScale);
	}

	public Graph getGraph() {
		return graph;
	}

	public MainPane getMainPane() {
		return parent;
	}

	public FlowPane getPaneCoordinateMouth() {
		return paneCoordinateMouth;
	}

	public Label getxLabel() {
		return xLabel;
	}

	public Label getyLabel() {
		return yLabel;
	}

	public ToggleButton getButtonAddVertex() {
		return buttonAddVertex;
	}

	public ToggleButton getButtonAddEdge() {
		return buttonAddEdge;
	}

	public Button getButtonClearGraph() {
		return buttonClearGraph;
	}

	public MenuButton getMenuButtonShow() {
		return menuButtonShow;
	}

	public CheckMenuItem getCheckNames() {
		return checkNames;
	}

	public CheckMenuItem getShowWeightsEdges() {
		return showWeightsEdges;
	}

	public SplitMenuButton getPathButton() {
		return pathButton;
	}

	public String getDefaultStylePathButton() {
		return defaultStylePathButton;
	}

	public MenuItem getWideSearchItem() {
		return wideSearchItem;
	}

	public MenuItem getDepthSearchItem() {
		return depthSearchItem;
	}

	public MenuItem getDijkstraItem() {
		return dijkstraItem;
	}

	public MenuItem getFindCyclesItem() {
		return findCyclesItem;
	}

	public MenuItem getMi800() {
		return mi800;
	}

	public MenuItem getMi400() {
		return mi400;
	}

	public MenuItem getMi200() {
		return mi200;
	}

	public MenuItem getMi150() {
		return mi150;
	}

	public MenuItem getMi125() {
		return mi125;
	}

	public MenuItem getMi100() {
		return mi100;
	}

	public MenuItem getMi75() {
		return mi75;
	}

	public MenuItem getMi50() {
		return mi50;
	}

	public MenuItem getMi25() {
		return mi25;
	}
}
