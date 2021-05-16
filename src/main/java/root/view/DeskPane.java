package root.view;

import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import root.GraphWorkerApplication;
import root.model.Graph;
import root.components.GraphElement;

public class DeskPane extends AnchorPane {
//	private static root.view.DeskPane instance;
//
//	public static root.view.DeskPane getInstance(root.model.Graph graph, root.view.MainPane parent) {
//		if (instance == null)
//			instance = new root.view.DeskPane(graph, parent);
//		return instance;
//	}

	private Graph graph;
	private GraphPane graphPane;

	private MainPane parent;

	private VBox vbToolPanel;

	private Button centreButton;
	private HelpNote helpNote;


	private ToggleButton buttonShowConsole;
	private ContextMenu vertexContextMenu;
	private double x = 0;
	private double y = 0;

	private double xG = 0;
	private double yG = 0;

	// TitledPane consoleTitle;
	public DeskPane(Graph graph, MainPane parent) {
		super();
		this.graph = graph;
		this.graphPane = graph.getGraphPane();
		this.parent = parent;
		initiate();
		initiateComponents();
	}

	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .1d;

	private boolean choosing;

	private void createCentreButton() {
		StackPane shapeCentre = new StackPane();
		shapeCentre.getStyleClass().add("shape-centre-graph");
		centreButton = new Button(null, shapeCentre);
		centreButton.setTooltip(new Tooltip("Отцентрировать холст по размеру окна"));
		centreButton.getStyleClass().add("button-black");
		// centreButton.setMinSize(21, 21);
		// centreButton.setStyle("-fx-background-color: transparent;");
	}

	private void createShowConsoleButton() {
		StackPane iconConsole = new StackPane();
		iconConsole.getStyleClass().add("shape-console");

		StackPane spArrow = new StackPane();
		spArrow.getStyleClass().add("shape-circle");

		AnchorPane aPane = new AnchorPane();
		iconConsole.setLayoutX(0);
		iconConsole.setLayoutY(0);
		spArrow.setLayoutX(8.5);
		spArrow.setLayoutY(1.5);
		aPane.getChildren().addAll(iconConsole, spArrow);
		buttonShowConsole = new ToggleButton(null, aPane);
		buttonShowConsole.setTooltip(new Tooltip("Показать/Скрыть консоль"));
		buttonShowConsole.getStyleClass().add("button-black");
	}

	private void createToolPanel() {
		vbToolPanel = new VBox(4D);
		vbToolPanel.setPadding(new Insets(4, 3, 4, 3));
		vbToolPanel.setPrefWidth(24);
		vbToolPanel.setStyle("-fx-background-color: #000000; -fx-background-radius: 4px;");
		vbToolPanel.setOpacity(0.6);
		AnchorPane.setRightAnchor(vbToolPanel, 5.);
		AnchorPane.setTopAnchor(vbToolPanel, 8.);
		createCentreButton();
		createShowConsoleButton();
		vbToolPanel.getChildren().addAll(centreButton/*, buttonShowConsole*/);
	}

	private void initiate() {
		setStyle("-fx-background-color: #E0E0E0;");
		AnchorPane.setTopAnchor(this, 30.);
		AnchorPane.setBottomAnchor(this, 0.);
		AnchorPane.setLeftAnchor(this, 250.);
		AnchorPane.setRightAnchor(this, 0.);
	}

	public void centreGraphPane() {
		double deskCentreX = GraphWorkerApplication.getInstance().getPrimaryStage().getWidth() / 2
				- (parent.getTbHideLeftPane().isSelected() ? (0) : 125);
		graphPane.setLayoutX((deskCentreX - graphPane.getBoundsInLocal().getWidth() / 2));
		graphPane.setLayoutY(15);
	}
	private RectangleChooser rectangle;
	private void initiateComponents() {
		createToolPanel();
		helpNote = new HelpNote(parent);
		rectangle = new RectangleChooser();
		graphPane.setHelpNote(helpNote);
		getChildren().addAll(graphPane, rectangle, vbToolPanel, helpNote);
	}

	public Button getCentreButton() {
		return centreButton;
	}

	public final class RectangleChooser extends Rectangle {

		public RectangleChooser() {
			getStrokeDashArray().addAll(8., 4.);
			setStyle("-fx-fill: transparent; -fx-stroke: #FFC618;");
			choosingList = new LinkedList<>();
		}

		private LinkedList<GraphElement> choosingList;

		public LinkedList<GraphElement> getChoosingList() {
			return choosingList;
		}
	}

	public Graph getGraph() {
		return graph;
	}

	public GraphPane getGraphPane() {
		return graphPane;
	}

	public MainPane getMainPane() {
		return parent;
	}

	public VBox getVbToolPanel() {
		return vbToolPanel;
	}

	public HelpNote getHelpNote() {
		return helpNote;
	}

	public ToggleButton getButtonShowConsole() {
		return buttonShowConsole;
	}

	public ContextMenu getVertexContextMenu() {
		return vertexContextMenu;
	}

	public void setVertexContextMenu(ContextMenu vertexContextMenu) {
		this.vertexContextMenu = vertexContextMenu;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getxG() {
		return xG;
	}

	public double getyG() {
		return yG;
	}

	public static double getMaxScale() {
		return MAX_SCALE;
	}

	public static double getMinScale() {
		return MIN_SCALE;
	}

	public boolean isChoosing() {
		return choosing;
	}

	public RectangleChooser getRectangle() {
		return rectangle;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setxG(double xG) {
		this.xG = xG;
	}

	public void setyG(double yG) {
		this.yG = yG;
	}

	public void setChoosing(boolean choosing) {
		this.choosing = choosing;
	}
}
