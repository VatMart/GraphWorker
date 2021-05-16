package root.view;

import java.io.File;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import root.model.Graph;

public class LeftPane extends VBox {
    public static class PaneMatrix extends FlowPane {

		private Graph graph;

		private final MainPane parent;

		private Label labelMatrixInputInc;
		private JFXButton buttonLoadMatrix;

		private JFXComboBox<Label> mbMatrixInput;

		private Label itemIncInput;
		private Label itemAdjacencyInput;
		private JFXTextArea taMatrixInput;
		private Label labelIconMatrix;

		private ImageView viewOkIconMatrix;

		private ImageView viewWarningIconMatrix;
		private JFXButton buttonBuildGraph;

		public PaneMatrix(Graph graph, MainPane parent) {
			super();
			this.graph = graph;
			this.parent = parent;
			initiate();
			initiateComponents();
		}

		private void createButtonBuildGraph() {
			buttonBuildGraph = new JFXButton("Построить граф");
			StackPane sp = new StackPane();
			sp.getStyleClass().add("shape-build-graph");
			buttonBuildGraph.getStyleClass().add("button-custom");
			buttonBuildGraph.getStyleClass().add("custom-font");
			buttonBuildGraph.setGraphic(sp);
		}

		private void createLabelIconMatrix() {
			File fileOkIconMatrix = new File("src\\main\\resources\\GraphWorkerItems\\ok_icon.png");
			File fileWarningkIconMatrix = new File("src\\main\\resources\\GraphWorkerItems\\Warning.png");
			Image iconMatrix = new Image(fileOkIconMatrix.toURI().toString(), 15, 15, true, true);
			viewOkIconMatrix = new ImageView(iconMatrix);
			Image WarningingMatrix = new Image(fileWarningkIconMatrix.toURI().toString(), 15, 15, true, true);
			viewWarningIconMatrix = new ImageView(WarningingMatrix);
			labelIconMatrix = new Label();
			labelIconMatrix.setGraphicTextGap(1.5);
			labelIconMatrix.setGraphic(viewOkIconMatrix);
		}

		private JFXRippler ripplerMBMatrixInput;

		private void createMenuButtonMatrixInput() {
			mbMatrixInput = new JFXComboBox<>();
			ripplerMBMatrixInput = new JFXRippler(mbMatrixInput);
			ripplerMBMatrixInput.setRipplerFill(Paint.valueOf("#006FFF"));
			mbMatrixInput.setMinHeight(20);
			itemIncInput = new Label("Инцидентности");
			itemAdjacencyInput = new Label("Смежности");
			mbMatrixInput.getItems().add(itemIncInput);
			mbMatrixInput.getItems().add(itemAdjacencyInput);
			mbMatrixInput.setConverter(new StringConverter<Label>() {
				@Override
				public String toString(Label object) {
					return object == null ? "" : object.getText();
				}

				@Override
				public Label fromString(String string) {
					return new Label(string);
				}
			});
			mbMatrixInput.getStyleClass().add("menuButtonScale");
			mbMatrixInput.getStyleClass().add("custom-font");
		}

		private void createTextAreaMatrixInput() {
			taMatrixInput = new JFXTextArea();
			taMatrixInput.setPromptText("1 0        1, 0,        1; 0;\r\n" + "0 1 или 0, 1, или 0; 1;\r\n"
					+ "\r\nДля построения матрицы смежности взвешенного графа, вводите значение веса вместо единицы");
			taMatrixInput.setPrefSize(225, 200);
			taMatrixInput.setFocusColor(Paint.valueOf("#006FFF"));
			taMatrixInput.editableProperty().set(true);
			// provided
// filter
			UnaryOperator<Change> filter = t -> {
				if (t.getText().matches("[^0123456789 \\-,;\n]"))
					return null;
				return (Change) t;
			};
			TextFormatter<String> formatter = new TextFormatter<>(filter);
			taMatrixInput.setTextFormatter(formatter);
		}

		private void initiate() {
			setPadding(new Insets(10, 5, 5, 5));
			setHgap(5);
			setVgap(10);
			setStyle("-fx-background-color: #FFFFFF");
		}

		private void initiateComponents() {
			labelMatrixInputInc = new Label("Матрица");
			labelMatrixInputInc.getStyleClass().add("custom-font");
			createMenuButtonMatrixInput();
			StackPane sp = new StackPane();
			sp.getStyleClass().add("shape-load");
			buttonLoadMatrix = new JFXButton("Загрузить матрицу");
			buttonLoadMatrix.getStyleClass().add("custom-font");
			buttonLoadMatrix.getStyleClass().add("button-custom");
			buttonLoadMatrix.setGraphic(sp);
			createTextAreaMatrixInput();
			createLabelIconMatrix();
			createButtonBuildGraph();
			PaneMatrix.this.getChildren().addAll(labelMatrixInputInc, ripplerMBMatrixInput, buttonLoadMatrix, taMatrixInput,
					labelIconMatrix, buttonBuildGraph);
		}

		public Graph getGraph() {
			return graph;
		}

		public MainPane getMainPane() {
			return parent;
		}

		public Label getLabelMatrixInputInc() {
			return labelMatrixInputInc;
		}

		public JFXButton getButtonLoadMatrix() {
			return buttonLoadMatrix;
		}

		public JFXComboBox<Label> getMbMatrixInput() {
			return mbMatrixInput;
		}

		public Label getItemIncInput() {
			return itemIncInput;
		}

		public Label getItemAdjacencyInput() {
			return itemAdjacencyInput;
		}

		public JFXTextArea getTaMatrixInput() {
			return taMatrixInput;
		}

		public Label getLabelIconMatrix() {
			return labelIconMatrix;
		}

		public ImageView getViewOkIconMatrix() {
			return viewOkIconMatrix;
		}

		public ImageView getViewWarningIconMatrix() {
			return viewWarningIconMatrix;
		}

		public JFXButton getButtonBuildGraph() {
			return buttonBuildGraph;
		}

		public JFXRippler getRipplerMBMatrixInput() {
			return ripplerMBMatrixInput;
		}
	}

	public static class PaneVariety extends FlowPane {

		private Graph graph;

		private final MainPane parent;

		private SpinnerValueFactory<Integer> valueFactory;

		private Spinner<Integer> spinnerQuanityVertexs;

		private TextFormatter<String> formatterSpinnerQuanity;
		private Label labelQuanityVertexs;
		private Label labelVarietyEdges;

		private JFXTextArea taEdges;

		private BorderPane bp;
		private Button buttonToComplete;
		private Label labelIconEdge;
		private ImageView viewOkIconEdge;

		private ImageView viewWarningIconEdge;
		private Label labelWarningNoVertexs;
		private Separator separatorOutputInc;
		private Label labelOutputInc;

		private JFXComboBox<Label> mbMatrixs;

		private Label itemInc;
		private Label itemAdjacency;
		private ScrollPane scrollPane;
		private BorderPane borderPane;

		private BorderPane bpShowMatrix;
		private Button buttonShowMatrix;
		private Button buttonSaveMatrix;
		private ListView<String> listView;
		private TextArea taOutputMatrix;
		private String defaulStyleTextAreaEdges;

		public PaneVariety(Graph graph, MainPane parent) {
			super();
			this.graph = graph;
			this.parent = parent;
			initiate();
			initiateComponents();
		}

		// Label ������ � ����� ������
		private void createLabelIconEdge() {
			File fileOkIconEdge = new File("src\\main\\resources\\GraphWorkerItems\\ok_icon.png");
			File fileWarningkIconEdge = new File("src\\main\\resources\\GraphWorkerItems\\Warning.png");
			Image iconEdge = new Image(fileOkIconEdge.toURI().toString(), 15, 15, true, true);
			viewOkIconEdge = new ImageView(iconEdge);
			Image WarningingEdge = new Image(fileWarningkIconEdge.toURI().toString(), 15, 15, true, true);
			viewWarningIconEdge = new ImageView(WarningingEdge);
			bp = new BorderPane();
			bp.setPrefHeight(74);
			bp.setPrefWidth(18);
			buttonToComplete = new Button();
			StackPane spToVomplete = new StackPane();
			spToVomplete.getStyleClass().add("shape-complete-graph");
			buttonToComplete.getStyleClass().add("button-custom");
			buttonToComplete.setGraphic(spToVomplete);
			buttonToComplete.setTooltip(new Tooltip("Привести к полному графу"));
			labelIconEdge = new Label();
			labelIconEdge.setPadding(new Insets(0, 0, 0, 2));
			// labelIconEdge.setGraphicTextGap(0);
			labelIconEdge.setGraphic(viewOkIconEdge);
			bp.setTop(buttonToComplete);
			bp.setBottom(labelIconEdge);
		}
		
		private JFXRippler ripplerMBMatrixs;

		private void createMenuButtonMatrixs() {
			mbMatrixs = new JFXComboBox<>();
			ripplerMBMatrixs = new JFXRippler(mbMatrixs);
			ripplerMBMatrixs.setRipplerFill(Paint.valueOf("#006FFF"));
			mbMatrixs.setMinHeight(20);
			itemInc = new Label("Инцидентности");
			itemAdjacency = new Label("Смежности");
			mbMatrixs.getItems().add(itemInc);
			mbMatrixs.getItems().add(itemAdjacency);
			mbMatrixs.setConverter(new StringConverter<Label>() {
				@Override
				public String toString(Label object) {
					return object == null ? "" : object.getText();
				}

				@Override
				public Label fromString(String string) {
					return new Label(string);
				}
			});
			mbMatrixs.getStyleClass().add("custom-font");
			mbMatrixs.getStyleClass().add("menuButtonScale");
		}

		private void createSpinnerQuanity() {
			valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000);
			spinnerQuanityVertexs = new Spinner<>(valueFactory);
			spinnerQuanityVertexs.getStyleClass().add("spinner-qVertex");
			spinnerQuanityVertexs.setEditable(true);
			spinnerQuanityVertexs.setPrefSize(65, 5);
			formatterSpinnerQuanity = new TextFormatter<>(c -> c.getText().matches("[^0123456789]") ? null : c);
			spinnerQuanityVertexs.getEditor().setTextFormatter(formatterSpinnerQuanity);
			spinnerQuanityVertexs.getEditor().setTooltip(new Tooltip("При ручном вводе вершин нажмите Enter"));
		}

		// TextArea ���� ������
		private void createTextAreaEdges() {
			taEdges = new JFXTextArea();
			taEdges.setPromptText("2 4; 5 3; 2 1;\n\rгде 2, 4, 5 и т.д. - вершины");
			taEdges.setPrefSize(200, 74);
			taEdges.setFocusColor(Paint.valueOf("#006FFF"));
			// provided
// filter
			UnaryOperator<Change> filterDelimited = t -> {
				if (t.getText().matches("[^0123456789 ,;\n]"))
					return null;
				return (Change) t;
			};
			TextFormatter<String> formatterDelimited = new TextFormatter<>(filterDelimited);
			taEdges.setTextFormatter(formatterDelimited);
			defaulStyleTextAreaEdges = taEdges.getStyle();
		}

		private void createTextAreaOutputMatrix() {
			scrollPane = new ScrollPane();
			scrollPane.setPrefSize(200, 95);
			scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
			scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			borderPane = new BorderPane();
			borderPane.setPrefSize(198, 93);
			scrollPane.setContent(borderPane);
			listView = new ListView<>();
			listView.setPrefWidth(18);
			listView.getStyleClass().add("list-view");
			listView.setCellFactory(param -> new ListCell<String>() {
				{
					setMinSize(2, 2);
					setPrefSize(2, 17);
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					// System.out.println(param.getItems().size());
					super.updateItem(item, empty);
					if (item != null && !empty) {
						setTextFill(Paint.valueOf("#191970"));
						setFont(new Font("System BOLD", 12));
						setAlignment(Pos.CENTER_RIGHT);
						setPadding(new Insets(0));
						setText(item);
					} else {
						setText(null);
					}
				}
			});
			listView.setPadding(new Insets(3, 0, 0, 0));
			borderPane.setLeft(listView);
			taOutputMatrix = new TextArea();
			// -fx-border-color: #191970 #191970 #191970 transparent;
			listView.setStyle(
					"-fx-border-color: transparent; -fx-background-color: transparent; -fx-highlight-fill: transparent; -fx-highlight-text-fill: transparent");
			taOutputMatrix.setStyle(
					"-fx-faint-focus-color: transparent; -fx-focus-color:transparent; -fx-text-box-border: transparent; -fx-background-color:transparent;");
			taOutputMatrix.setPromptText("Здесь генерируется введенная матрица");
			taOutputMatrix.editableProperty().set(false);
			taOutputMatrix.textProperty().addListener((observable, oldValue, newValue) -> {
				String regex = "\n";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(oldValue);
				int oldCountEnter = 0;
				while (matcher.find()) {
					oldCountEnter++;
				}
				matcher = pattern.matcher(newValue);
				int newCountEnter = 0;
				while (matcher.find()) {
					newCountEnter++;
				}
				// System.out.println("OldValue: " + oldCountEnter);
				// System.out.println("NewValue: " + newCountEnter);
				if (oldCountEnter >= 4) {
					int lineHeight = 17;
					if (oldCountEnter < newCountEnter) {
						borderPane.setPrefHeight(borderPane.getPrefHeight()
								+ lineHeight * (Math.abs(oldCountEnter - newCountEnter)));
						// listView.setPrefHeight(borderPane.getHeight());
					} else if (oldCountEnter > newCountEnter && newCountEnter >= 4) {
						borderPane.setPrefHeight(borderPane.getPrefHeight()
								+ lineHeight * (-Math.abs(oldCountEnter - newCountEnter)));
						// listView.setPrefHeight(borderPane.getHeight());
					} else if (oldCountEnter > newCountEnter) {
						borderPane.setPrefHeight(93);
						// listView.setPrefHeight(borderPane.getHeight());
					}
				}
			});
			borderPane.setCenter(taOutputMatrix);

			bpShowMatrix = new BorderPane();
			bpShowMatrix.setPrefHeight(95);
			bpShowMatrix.setPrefWidth(18);
			VBox vBoxOut = new VBox(1.);
			vBoxOut.setPadding(new Insets(2, 1, 2, 1));
			vBoxOut.setPrefWidth(18);
			vBoxOut.setStyle("-fx-background-color: #000000; -fx-background-radius: 4px;");
			vBoxOut.setOpacity(0.8);
			buttonShowMatrix = new Button();
			StackPane spToVomplete = new StackPane();
			spToVomplete.getStyleClass().add("shape-show-matrix");
			buttonShowMatrix.getStyleClass().add("button-black");
			buttonShowMatrix.setGraphic(spToVomplete);
			buttonShowMatrix.setTooltip(new Tooltip("Открыть в отдельном окне"));
			buttonSaveMatrix = new Button();
			buttonSaveMatrix.getStyleClass().add("button-black");
			StackPane spSave = new StackPane();
			spSave.getStyleClass().add("shape-save");
			buttonSaveMatrix.setGraphic(spSave);
			buttonSaveMatrix.setTooltip(new Tooltip("Сохранить матрицу в файле"));
			vBoxOut.getChildren().addAll(buttonShowMatrix, buttonSaveMatrix);
			bpShowMatrix.setTop(vBoxOut);
		}

		private void creatLabelWarningNoVertexs() {
			labelWarningNoVertexs = new Label("Вводимой вершины не существует");
			labelWarningNoVertexs.setTextFill(Color.web("#ff0000"));
			labelWarningNoVertexs.setFont(new Font("Arial", 10));
			labelWarningNoVertexs.setLineSpacing(0);
			labelWarningNoVertexs.setVisible(false);
		}

		private void initiate() {
			setPadding(new Insets(10, 5, 5, 5));
			setHgap(5);
			setVgap(10);
			setStyle("-fx-background-color: #FFFFFF");
		}

		private void initiateComponents() {
			createSpinnerQuanity();
			labelQuanityVertexs = new Label("Количество вершин");
			labelQuanityVertexs.getStyleClass().add("custom-font");
			labelQuanityVertexs.setPrefWidth(140);
			labelVarietyEdges = new Label("Множество граней:");
			labelVarietyEdges.setPrefWidth(180);
			labelVarietyEdges.getStyleClass().add("custom-font");
			createTextAreaEdges();
			createLabelIconEdge();
			creatLabelWarningNoVertexs();
			separatorOutputInc = new Separator(Orientation.HORIZONTAL);
			separatorOutputInc.setPrefSize(200, 4);
			labelOutputInc = new Label("Матрица");
			labelOutputInc.getStyleClass().add("custom-font");
			createMenuButtonMatrixs();
			createTextAreaOutputMatrix();
			PaneVariety.this.getChildren().addAll(spinnerQuanityVertexs, labelQuanityVertexs, labelVarietyEdges,
					taEdges, bp, labelWarningNoVertexs, separatorOutputInc, labelOutputInc, ripplerMBMatrixs, scrollPane,
					bpShowMatrix);
		}

		public Graph getGraph() {
			return graph;
		}

		public MainPane getMainPane() {
			return parent;
		}

		public SpinnerValueFactory<Integer> getValueFactory() {
			return valueFactory;
		}

		public Spinner<Integer> getSpinnerQuanityVertexs() {
			return spinnerQuanityVertexs;
		}

		public TextFormatter<String> getFormatterSpinnerQuanity() {
			return formatterSpinnerQuanity;
		}

		public Label getLabelQuanityVertexs() {
			return labelQuanityVertexs;
		}

		public Label getLabelVarietyEdges() {
			return labelVarietyEdges;
		}

		public JFXTextArea getTaEdges() {
			return taEdges;
		}

		public BorderPane getBp() {
			return bp;
		}

		public Button getButtonToComplete() {
			return buttonToComplete;
		}

		public Label getLabelIconEdge() {
			return labelIconEdge;
		}

		public ImageView getViewOkIconEdge() {
			return viewOkIconEdge;
		}

		public ImageView getViewWarningIconEdge() {
			return viewWarningIconEdge;
		}

		public Label getLabelWarningNoVertexs() {
			return labelWarningNoVertexs;
		}

		public Separator getSeparatorOutputInc() {
			return separatorOutputInc;
		}

		public Label getLabelOutputInc() {
			return labelOutputInc;
		}

		public JFXComboBox<Label> getMbMatrixs() {
			return mbMatrixs;
		}

		public Label getItemInc() {
			return itemInc;
		}

		public Label getItemAdjacency() {
			return itemAdjacency;
		}

		public ScrollPane getScrollPane() {
			return scrollPane;
		}

		public BorderPane getBorderPane() {
			return borderPane;
		}

		public BorderPane getBpShowMatrix() {
			return bpShowMatrix;
		}

		public Button getButtonShowMatrix() {
			return buttonShowMatrix;
		}

		public Button getButtonSaveMatrix() {
			return buttonSaveMatrix;
		}

		public ListView<String> getListView() {
			return listView;
		}

		public TextArea getTaOutputMatrix() {
			return taOutputMatrix;
		}

		public String getDefaulStyleTextAreaEdges() {
			return defaulStyleTextAreaEdges;
		}

		public JFXRippler getRipplerMBMatrixs() {
			return ripplerMBMatrixs;
		}
	}

//	private static root.view.LeftPane instance;
//
//	public static root.view.LeftPane getInstance(root.model.Graph graph, root.view.MainPane parent) {
//		if (instance == null)
//			instance = new root.view.LeftPane(graph, parent);
//		return instance;
//	}

	private Graph graph;

	private final MainPane parent;

	private FlowPane paneOrientation;
	private JFXRadioButton rbUndirectedGraph;
	private JFXRadioButton rbDirectedGraph;
	private JFXRadioButton rbMixedGraph;
	private Label labelTypeGraph;

	private Label labelTypeInut;
	private JFXTabPane inputTabPane;

	public PaneVariety getPaneVariety() {
		return paneVariety;
	}

	private PaneVariety paneVariety;
	private PaneMatrix paneMatrix;

	private Label labelFastGenerateGraph;
	private ToolBar toolBarGenerateGraph;
	private Button buttonGenerateGraph;
	private JFXButton buttonSettingGenerate;

	public LeftPane(Graph graph, MainPane parent) {
		super();
		this.graph = graph;
		this.parent = parent;
		initiate();
		initiateComponents();
	}

	private void createInputTabPane() {
		inputTabPane = new JFXTabPane();
		Tab varietyTab = new Tab("Множества");
		Tab matrixTab = new Tab("Матрица");
		paneVariety = new PaneVariety(graph, parent);
		paneMatrix = new PaneMatrix(graph, parent);
		inputTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		inputTabPane.setPrefSize(283, 360);
		inputTabPane.setEffect(getInnerShadowEffect());
		varietyTab.setContent(paneVariety);
		matrixTab.setContent(paneMatrix);
		inputTabPane.getStyleClass().add("tab-pane-left");
		inputTabPane.getTabs().addAll(varietyTab, matrixTab);
	}

	private void createLabelFastGenerateGraph() {
		labelFastGenerateGraph = new Label("Генерация случайного графа");
		labelFastGenerateGraph.setFont(new Font("Roboto Bold", 15));
		labelFastGenerateGraph.setEffect(getDropShadowEffect());
		labelFastGenerateGraph.setTextFill(Paint.valueOf("#FFFFFF"));
	}

	private void createLabelTypeInut() {
		labelTypeInut = new Label("Генерация по входным данным:");
		labelTypeInut.setFont(new Font("Roboto Bold", 15));
		// labelTypeInut.getStyleClass().add("custom-medium-font");
		labelTypeInut.setEffect(getDropShadowEffect());
		labelTypeInut.setTextFill(Paint.valueOf("#FFFFFF"));
	}

	private void createPaneOrientation() {
		paneOrientation = new FlowPane();
		paneOrientation.setPrefSize(240, 115);
		paneOrientation.setStyle("-fx-background-color: #ffffff;");
		paneOrientation.setEffect(getInnerShadowEffect());
		paneOrientation.setVgap(10);
		paneOrientation.setHgap(10);
		paneOrientation.setPadding(new Insets(10));
		// Label Тип графа
		labelTypeGraph = new Label("Тип графа");
		labelTypeGraph.getStyleClass().add("custom-medium-font");
		// RadioButton Ориентированный-Неориентированный
		ToggleGroup RadioDirected_Undirectedgroup = new ToggleGroup();
		rbUndirectedGraph = new JFXRadioButton("Неориентированный");
		rbDirectedGraph = new JFXRadioButton("Ориентированный");
		rbMixedGraph = new JFXRadioButton("Смешанный");
		rbUndirectedGraph.getStyleClass().add("custom-font");
		rbDirectedGraph.getStyleClass().add("custom-font");
		rbMixedGraph.getStyleClass().add("custom-font");

		rbDirectedGraph.setSelected(true);
		rbMixedGraph.setDisable(true);
		rbDirectedGraph.setToggleGroup(RadioDirected_Undirectedgroup);
		rbUndirectedGraph.setToggleGroup(RadioDirected_Undirectedgroup);
		rbMixedGraph.setToggleGroup(RadioDirected_Undirectedgroup);
		// Separator ��� �����
		paneOrientation.getChildren().addAll(labelTypeGraph, rbUndirectedGraph, rbDirectedGraph, rbMixedGraph);
	}

	private void createToolBarGenerateGraph() {
		toolBarGenerateGraph = new ToolBar();
		toolBarGenerateGraph.setEffect(getInnerShadowEffect());
		buttonGenerateGraph = new Button();
		StackPane sp = new StackPane();
		sp.getStyleClass().add("shape-generate-graph");
		buttonGenerateGraph.getStyleClass().add("button-custom");
		buttonGenerateGraph.setGraphic(sp);
		buttonSettingGenerate = new JFXButton("Настройки генерации");
		StackPane sp1 = new StackPane();
		sp1.getStyleClass().add("shape-generate-graph-settings");
		buttonSettingGenerate.getStyleClass().add("button-custom");
		buttonSettingGenerate.getStyleClass().add("custom-font");
		buttonSettingGenerate.setGraphic(sp1);
		// buttonSettingGenerate.getStyleClass().add("button-custom");
		// TODO
		toolBarGenerateGraph.getItems().addAll(buttonGenerateGraph, buttonSettingGenerate);
	}

	private DropShadow getDropShadowEffect() {
		DropShadow shadowEffect = new DropShadow();
		shadowEffect.blurTypeProperty().set(BlurType.GAUSSIAN);
		shadowEffect.setHeight(0);
		shadowEffect.setRadius(1.785);
		shadowEffect.setSpread(0.29);
		shadowEffect.setWidth(9.14);
		return shadowEffect;
	}

	private InnerShadow getInnerShadowEffect() {
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setBlurType(BlurType.THREE_PASS_BOX);
		innerShadow.setHeight(9.14);
		innerShadow.setWidth(9.14);
		innerShadow.setRadius(4.07);
		return innerShadow;
	}

	private void initiate() {
		setPrefWidth(245);
		setPrefHeight(parent.getPrefHeight());
		setStyle("-fx-background-color: black;"); // #3366CC
		setOpacity(0.8);
		setPadding(new Insets(5., 5., 0., 5.));
		setSpacing(10);
		AnchorPane.setTopAnchor(this, 30.);
		AnchorPane.setBottomAnchor(this, 0.);
	}

	private void initiateComponents() {
		createPaneOrientation();
		createLabelTypeInut();
		createInputTabPane();
		createLabelFastGenerateGraph();
		createToolBarGenerateGraph();
		getChildren().addAll(paneOrientation, labelTypeInut, inputTabPane, labelFastGenerateGraph,
				toolBarGenerateGraph);
	}

	public Graph getGraph() {
		return graph;
	}

	public MainPane getMainPane() {
		return parent;
	}

	public FlowPane getPaneOrientation() {
		return paneOrientation;
	}

	public JFXRadioButton getRbUndirectedGraph() {
		return rbUndirectedGraph;
	}

	public JFXRadioButton getRbDirectedGraph() {
		return rbDirectedGraph;
	}

	public JFXRadioButton getRbMixedGraph() {
		return rbMixedGraph;
	}

	public Label getLabelTypeGraph() {
		return labelTypeGraph;
	}

	public Label getLabelTypeInut() {
		return labelTypeInut;
	}

	public JFXTabPane getInputTabPane() {
		return inputTabPane;
	}

	public PaneMatrix getPaneMatrix() {
		return paneMatrix;
	}

	public Label getLabelFastGenerateGraph() {
		return labelFastGenerateGraph;
	}

	public ToolBar getToolBarGenerateGraph() {
		return toolBarGenerateGraph;
	}

	public Button getButtonGenerateGraph() {
		return buttonGenerateGraph;
	}

	public JFXButton getButtonSettingGenerate() {
		return buttonSettingGenerate;
	}

	public JFXTextArea getTaEdges() {
		return paneVariety.taEdges;
	}
}
