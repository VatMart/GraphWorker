package root.view;

import root.GraphWorkerApplication;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import root.model.Graph;

//CHECK
public class MainPane extends AnchorPane {
//	private static root.view.MainPane instance;
//
//	public static root.view.MainPane getInstance(root.model.Graph graph) {
//		if (instance == null)
//			instance = new root.view.MainPane(graph);
//		return instance;
//	}

	private Graph graph;
	private LeftPane leftPane;
	private DeskPane deskPane;

	private ToolBarGraph toolBar;

	private ToggleButton tbHideLeftPane;
	private StackPane spMenuBar;
	private MenuBar menuBar;
	private MenuItem openFile;
	private MenuItem saveFile;
	private MenuItem saveAsFile;
	private MenuItem exportPNG;
	private MenuItem exportJPG;
	private MenuItem canvasSettings;
	//private MenuItem undo;
	//private MenuItem redo;
	private CheckMenuItem toCurveAll;

	public MainPane(Graph graph) {
		super();
		this.graph = graph;
		initiate();
		initiateComponents();
	}

	private void createHideLeftPaneButton() {
		StackPane spShapeHide = new StackPane();
		spShapeHide.getStyleClass().add("shape-hide-leftpane");
		tbHideLeftPane = new ToggleButton();
		tbHideLeftPane.setGraphic(spShapeHide);
		tbHideLeftPane.getStyleClass().add("toggle-hide-leftpane");
		tbHideLeftPane.setMinWidth(5);
		tbHideLeftPane.setMaxWidth(5);

		AnchorPane.setTopAnchor(tbHideLeftPane, 30.);
		AnchorPane.setBottomAnchor(tbHideLeftPane, 0.);
		AnchorPane.setLeftAnchor(tbHideLeftPane, 245.);
	}

	private void createMenuBar() {
		spMenuBar = new StackPane();
		spMenuBar.setStyle("-fx-background-color: rgba(0,0,0,0.7);"); // #3366CC
		spMenuBar.setPadding(new Insets(0, 5, 0, 5));
		menuBar = new MenuBar();
		spMenuBar.getChildren().add(menuBar);
		spMenuBar.setPrefHeight(30);
		spMenuBar.setPrefWidth(250);
		AnchorPane.setLeftAnchor(spMenuBar, 0.);
		AnchorPane.setTopAnchor(spMenuBar, 0.);

		DropShadow shadowMenu = new DropShadow();
		shadowMenu.blurTypeProperty().set(BlurType.GAUSSIAN);
		shadowMenu.setHeight(8.14);
		shadowMenu.setWidth(8.14);
		menuBar.setEffect(shadowMenu);

		Menu fileMenu = new Menu("Файл");
		openFile = new MenuItem("Открыть файл");
		openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		saveFile = new MenuItem("Сохранить");
		saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		saveAsFile = new MenuItem("Сохранить как");
		Menu exportFile = new Menu("Экспортировать как");
		exportPNG = new MenuItem("PNG файл");
		exportJPG = new MenuItem("JPG файл");
		exportFile.getItems().addAll(exportPNG, exportJPG);
		SeparatorMenuItem separatorMenuFile1 = new SeparatorMenuItem();
		canvasSettings = new MenuItem("Настройки холста");
		fileMenu.getItems().addAll(openFile, saveFile, saveAsFile, exportFile, separatorMenuFile1, canvasSettings);

		Menu editMenu = new Menu("Редактировать");
		//undo = new MenuItem("Undo");
		//undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		//redo = new MenuItem("Redo");
		//redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		toCurveAll = new CheckMenuItem("Все грани к кривым");
		editMenu.getItems().addAll(/*undo, redo,*/ toCurveAll);

		Menu helpMenu = new Menu("Помощь");
		menuBar.setPrefWidth(240);
		menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
	}

	public DeskPane getDeskPane() {
		return deskPane;
	}

	public LeftPane getLeftPane() {
		return leftPane;
	}

	public ToolBarGraph getToolBar() {
		return toolBar;
	}

	private void initiate() {
		setPrefHeight(700);
		setPrefWidth(977);
		setMinHeight(700);
		setMinWidth(977);
		setStyle("-fx-background-color: #F5F5F5;");
	}

	private void initiateComponents() {
		deskPane = new DeskPane(graph, this);
		toolBar = new ToolBarGraph(graph, this);
		leftPane = new LeftPane(graph, this);
		createHideLeftPaneButton();
		createMenuBar();
		getChildren().addAll(deskPane, toolBar, leftPane, tbHideLeftPane, spMenuBar);
	}

	public static void showAlertMessage(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Сообщение");
		// Header Text: null
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.initOwner(GraphWorkerApplication.getInstance().getPrimaryStage());
		alert.showAndWait();
	}

	public ToggleButton getTbHideLeftPane() {
		return tbHideLeftPane;
	}

	public void setTbHideLeftPane(ToggleButton tbHideLeftPane) {
		this.tbHideLeftPane = tbHideLeftPane;
	}

	public StackPane getSpMenuBar() {
		return spMenuBar;
	}

	public void setSpMenuBar(StackPane spMenuBar) {
		this.spMenuBar = spMenuBar;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public MenuItem getOpenFile() {
		return openFile;
	}

	public void setOpenFile(MenuItem openFile) {
		this.openFile = openFile;
	}

	public MenuItem getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(MenuItem saveFile) {
		this.saveFile = saveFile;
	}

	public MenuItem getSaveAsFile() {
		return saveAsFile;
	}

	public void setSaveAsFile(MenuItem saveAsFile) {
		this.saveAsFile = saveAsFile;
	}

	public MenuItem getExportPNG() {
		return exportPNG;
	}

	public void setExportPNG(MenuItem exportPNG) {
		this.exportPNG = exportPNG;
	}

	public MenuItem getExportJPG() {
		return exportJPG;
	}

	public void setExportJPG(MenuItem exportJPG) {
		this.exportJPG = exportJPG;
	}

	public MenuItem getCanvasSettings() {
		return canvasSettings;
	}

	public CheckMenuItem getToCurveAll() {
		return toCurveAll;
	}

	public Graph getGraph() {
		return graph;
	}
}
