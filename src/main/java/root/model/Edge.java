package root.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import root.GraphWorkerApplication;
import root.components.ChoseGraphElement;
import root.components.GraphElement;
import root.components.Pair;
import root.view.EditableLabel;

public class Edge extends GraphElement implements ChoseGraphElement {

	@Override
	public Node getNode() {
		return groupEdge;
	}

	private class Arrow {

		private double size;
		private final Path path;
		private PathEdge line;
		private Line lineSample;

		public Arrow(double size) {
			this.size = size;
			path = new Path();
		}

		public Path getPath() {
			return path;
		}

		private void setArrowLineEnd(PathEdge line) {
			this.line = line;
			if (!Edge.this.isOrientated()) {
				return;
			}
			MoveTo moveToEndLine;
			LineTo leftLineTo;
			QuadCurveTo quadCurveToRight;
			ClosePath closePath;
			Point2D baseArrow;
			Point2D left;
			Point2D right;
			if (!line.isCurve()) {
				Point2D endLine = new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY());
				Point2D startLine = new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY());

				double distance = endLine.distance(startLine);
				double ko = size / distance;
				baseArrow = new Point2D(endLine.getX() + (startLine.getX() - endLine.getX()) * ko,
						endLine.getY() + (startLine.getY() - endLine.getY()) * ko);
				ko = (size / 1.6) / distance;
				Point2D curvePoint = new Point2D(endLine.getX() + (startLine.getX() - endLine.getX()) * ko,
						endLine.getY() + (startLine.getY() - endLine.getY()) * ko);
				double radius = size + 2;// baseArrow.distance(endLine);
				// System.out.println("radius = " + radius);
				double angle = Math.atan2(baseArrow.getY() - endLine.getY(), baseArrow.getX() - endLine.getX());
				// System.out.println("���� = " + Math.toDegrees(angle));
				left = new Point2D(endLine.getX() + radius * Math.cos(Math.toRadians(30) + angle),
						endLine.getY() + radius * Math.sin(Math.toRadians(30) + angle));
				right = new Point2D(endLine.getX() + radius * Math.cos(Math.toRadians(-30) + angle),
						endLine.getY() + radius * Math.sin(Math.toRadians(-30) + angle));
				if (!path.getElements().isEmpty()) {
					path.getElements().clear();
				}
				moveToEndLine = new MoveTo(endLine.getX(), endLine.getY());
				leftLineTo = new LineTo(left.getX(), left.getY());
				quadCurveToRight = new QuadCurveTo(curvePoint.getX(), curvePoint.getY(), right.getX(), right.getY());
				closePath = new ClosePath();
			} else {
				Point2D endLine = new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY());
				Point2D curvePoint = line.getEdgeCurvePoint();
				double distance = curvePoint.distance(endLine);
				double ko = size / distance;
				// (1 - t)^2*P0 + (1 - t)*t*P1 + t^2*P2
				baseArrow = new Point2D(endLine.getX() + (curvePoint.getX() - endLine.getX()) * ko,
						endLine.getY() + (curvePoint.getY() - endLine.getY()) * ko);
				ko = (size / 1.6) / distance;
				Point2D curvePointArrow = new Point2D(endLine.getX() + (curvePoint.getX() - endLine.getX()) * ko,
						endLine.getY() + (curvePoint.getY() - endLine.getY()) * ko);
				double radius = size + 2;// baseArrow.distance(endLine);
				// System.out.println("radius = " + radius);
				double angle = Math.atan2(baseArrow.getY() - endLine.getY(), baseArrow.getX() - endLine.getX());
				left = new Point2D(endLine.getX() + radius * Math.cos(Math.toRadians(30) + angle),
						endLine.getY() + radius * Math.sin(Math.toRadians(30) + angle));
				right = new Point2D(endLine.getX() + radius * Math.cos(Math.toRadians(-30) + angle),
						endLine.getY() + radius * Math.sin(Math.toRadians(-30) + angle));
				if (!path.getElements().isEmpty()) {
					path.getElements().clear();
				}
				moveToEndLine = new MoveTo(endLine.getX(), endLine.getY());
				leftLineTo = new LineTo(left.getX(), left.getY());
				quadCurveToRight = new QuadCurveTo(curvePointArrow.getX(), curvePointArrow.getY(), right.getX(),
						right.getY());
				closePath = new ClosePath();
			}
			path.getElements().add(moveToEndLine);
			path.getElements().add(leftLineTo);
			// arrow.getElements().add(new LineTo(right.getX(), right.getY()));
			path.getElements().add(quadCurveToRight);
			path.getElements().add(closePath);
		}

		private void setArrowLineEnd(Line line) {
			this.lineSample = line;
			if (!Edge.this.isOrientated()) {
				return;
			}
			Point2D endLine = new Point2D(line.getEndX(), line.getEndY());
			Point2D startLine = new Point2D(line.getStartX(), line.getStartY());
			Point2D baseArrow;
			Point2D left;
			Point2D right;

			MoveTo moveToEndLine;
			LineTo leftLineTo;
			QuadCurveTo quadCurveToRight;
			ClosePath closePath;

			double distance = endLine.distance(startLine);
			double ko = size / distance;
			baseArrow = new Point2D(endLine.getX() + (startLine.getX() - endLine.getX()) * ko,
					endLine.getY() + (startLine.getY() - endLine.getY()) * ko);
			ko = (size / 1.6) / distance;
			Point2D curvePoint = new Point2D(endLine.getX() + (startLine.getX() - endLine.getX()) * ko,
					endLine.getY() + (startLine.getY() - endLine.getY()) * ko);
			double radius = size + 2;// baseArrow.distance(endLine);
			// System.out.println("radius = " + radius);
			double angle = Math.atan2(baseArrow.getY() - endLine.getY(), baseArrow.getX() - endLine.getX());
			// System.out.println("���� = " + Math.toDegrees(angle));
			left = new Point2D(endLine.getX() + radius * Math.cos(Math.toRadians(30) + angle),
					endLine.getY() + radius * Math.sin(Math.toRadians(30) + angle));
			right = new Point2D(endLine.getX() + radius * Math.cos(Math.toRadians(-30) + angle),
					endLine.getY() + radius * Math.sin(Math.toRadians(-30) + angle));
			if (!path.getElements().isEmpty()) {
				path.getElements().clear();
			}
			moveToEndLine = new MoveTo(endLine.getX(), endLine.getY());
			leftLineTo = new LineTo(left.getX(), left.getY());
			quadCurveToRight = new QuadCurveTo(curvePoint.getX(), curvePoint.getY(), right.getX(), right.getY());
			closePath = new ClosePath();

			path.getElements().add(moveToEndLine);
			path.getElements().add(leftLineTo);
			// arrow.getElements().add(new LineTo(right.getX(), right.getY()));
			path.getElements().add(quadCurveToRight);
			path.getElements().add(closePath);
			// arrow.setFill(Paint.valueOf("RED"));
			// arrow.setStrokeWidth(1);
			// arrow.getElements().add(new LineTo(endLine.getX(), endLine.getY()));
		}

		public void setSize(double size) {
			this.size = size;
			if (line != null)
				setArrowLineEnd(line);
			else
				setArrowLineEnd(lineSample);
		}

	}

	private class EdgeSettingWindow {

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

		private Label lArrowColor;
		private Label lNameColor;
		private Label lBorderArrowColor;
		private Label lNameEdge;

		private Label lWeightEdge;

		private CheckBox cbEffectOnText;

		private CheckBox cbToAllEdges;
		private CheckBox cbOrientation;
		private ColorPicker cpMainColor;
		private ColorPicker cpArrowColor;
		private ColorPicker cpNameColor;
		private ColorPicker cpBorderArrowColor;
		private TextField tfName;
		private TextField tfWeight;
		private Separator separatorFP;
		// private Group gVertexSample;
		private Line lineSample;
		private Text nameSample;
		private Arrow arrowSample;
		private WeightEdge weightSample;
		private Label lSizeEdge;
		private Label lSizeName;
		private Label lSizeArrow;

		private Label lSizeWeight;

		private Slider sSizeEdge;

		private Slider sSizeName;
		private Slider sSizeArrow;
		private Slider sSizeWeight;
		private String choosenMainColor = getColorEdge();

		private String choosenArrowColor = getColorArrow();
		private String choosenNameColor = getColorName();
		private String choosenArrowBorderColor = getColorArrowStroke();
		private boolean isToAll = false;
		private boolean isOriented = isOrientated();
		private String newName = getLabelName().getText();
		private double nameSize = getSizeName();
		private double edgeSize = getSizeEdge();

		private double arrowSize = getSizeArrow();

		private double weightSize = getSizeWeight();

		private int weightVal = getWeight();
		private boolean shadowOn = isShadowOn();

		public EdgeSettingWindow() {
			buildInterface();
			buildFunctionalities();
		}

		private FlowPane buildFlowPane() {
			flowPane = new FlowPane();
			flowPane.setVgap(3);
			flowPane.setPadding(new Insets(5, 2, 2, 5));
			flowPane.setPrefSize(170, 232);
			flowPane.setMinSize(170, 232);
			flowPane.setMaxSize(170, 232);

			flowPane.getChildren().addAll(initiateFlowPaneElements());
			return flowPane;
		}

		private void buildFunctionalities() {
			setColorsListeners();
			setSlidersListeners();
			setTextFieldsListeners();
			setCheckBoxesListeners();
			setButtonsListeners();
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
			settingWindow.setTitle("Настройка грани");
//			settingWindow.setX(graph.getRightPane().localToScreen(graph.getRightPane().getBoundsInLocal()).getMinX()
//					+ graph.getRightPane().getPrefWidth() / 2 - rootPane.getPrefWidth() / 2);
//			settingWindow.setY(graph.getRightPane().localToScreen(graph.getRightPane().getBoundsInLocal()).getMinY()
//					+ graph.getRightPane().getPrefHeight() / 2 - rootPane.getPrefHeight() / 2);
			settingWindow.centerOnScreen();
		}

		private VBox buildRootPane() {
			rootPane = new VBox();
			rootPane.setPrefSize(300, 260);
			rootPane.setMinSize(300, 260);
			rootPane.setMaxSize(300, 260);
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
			separator.setPrefSize(116, 10);
			lSizeEdge = new Label("Толщина грани");
			lSizeEdge.setFont(new Font("System", 11));
			sSizeEdge = new Slider(3, 6, getSizeEdge());
			lSizeName = new Label("Размер имени");
			lSizeName.setFont(new Font("System", 11));
			sSizeName = new Slider(12, 24, getSizeName());
			lSizeArrow = new Label("Размер стрелки");
			lSizeArrow.setFont(new Font("System", 11));
			sSizeArrow = new Slider(9, 18, getSizeArrow());
			lSizeWeight = new Label("Размер веса");
			lSizeWeight.setFont(new Font("System", 11));
			sSizeWeight = new Slider(14, 18, getSizeWeight());

			if (!isOrientated()) {
				lSizeArrow.setDisable(true);
			}
			samplePane.getChildren().addAll(buildSampleVertex(), separator, lSizeEdge, sSizeEdge, lSizeName, sSizeName,
					lSizeArrow, sSizeArrow, lSizeWeight, sSizeWeight);
			return samplePane;
		}

		private AnchorPane buildSampleVertex() {
			AnchorPane stackPane = new AnchorPane();
			stackPane.setPrefSize(samplePane.getPrefWidth(), 60);
			lineSample = new Line(15, stackPane.getPrefHeight() / 2, stackPane.getPrefWidth() - 15,
					stackPane.getPrefHeight() / 2);
			lineSample.setStyle(getEdgeStyle());
			arrowSample = new Arrow(getSizeArrow());
			arrowSample.setArrowLineEnd(lineSample);
			arrowSample.getPath().setStyle(getArrowStyle());
			nameSample = new Text(labelName.getText());
			nameSample.setFont(new Font(getLabelName().getFont().getName(), getSizeName()));
			nameSample.setStyle(getNameStyle());
			weightSample = new WeightEdge(getWeight(), lineSample);
			nameSample.setLayoutX(new Point2D(lineSample.getStartX(), lineSample.getStartY())
					.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getX()
					- ((nameSize / 2)) * nameSample.getText().length() / 2);
			nameSample.setLayoutY(new Point2D(lineSample.getStartX(), lineSample.getStartY())
					.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getY()
					- weightSample.getRectang().getPrefHeight() / (1.5));
			stackPane.getChildren().addAll(lineSample, arrowSample.getPath(), nameSample, weightSample.getRectang());
			weightSample.setSetOnLine(lineSample);
			return stackPane;
		}

		private SplitPane buildSplitPane() {
			splitPane = new SplitPane();
			splitPane.setDividerPositions(0.5805);
			splitPane.setPrefSize(300, 234);
			splitPane.getItems().addAll(buildFlowPane(), buildSamplePane());
			return splitPane;
		}

		private ArrayList<Node> initiateFlowPaneElements() {
			lMainColor = new Label("Основной цвет:");
			lArrowColor = new Label("Цвет стрелки:");
			lNameColor = new Label("Цвет имени:");
			lBorderArrowColor = new Label("Обводка стрелки:");
			lNameEdge = new Label("Имя грани:");
			lWeightEdge = new Label("Вес грани:");
			cbToAllEdges = new CheckBox("Применить ко всем");
			cbEffectOnText = new CheckBox("Тень от имени");
			cbOrientation = new CheckBox("Ориентированная");
			cpMainColor = new ColorPicker(Color.web(getColorEdge()));
			cpArrowColor = new ColorPicker(Color.web(getColorArrow()));
			cpNameColor = new ColorPicker(Color.web(getColorName()));
			cpBorderArrowColor = new ColorPicker(Color.web(getColorArrowStroke()));
			tfName = new TextField(labelName.getText());
			tfWeight = new TextField(Integer.toString(getWeight()));
			separatorFP = new Separator(Orientation.HORIZONTAL);

			lMainColor.setPrefSize(102, 17);
			lArrowColor.setPrefSize(102, 17);
			lNameColor.setPrefSize(102, 17);
			lNameEdge.setPrefSize(102, 17);
			lBorderArrowColor.setPrefSize(102, 17);
			lWeightEdge.setPrefSize(102, 17);
			cbEffectOnText.setFont(new Font("System", 11));
			cbEffectOnText.setSelected(isShadowOn());
			cbEffectOnText.setPrefSize(110, 16);
			cbToAllEdges.setPrefSize(124, 16);
			cbToAllEdges.setFont(new Font("System", 11));
			cbOrientation.setFont(new Font("System", 11));
			cbOrientation.setPrefSize(122, 16);
			cbOrientation.setSelected(isOrientated());

			// cbOrientation.setDisable(true);

			cpMainColor.setPrefSize(41, 25);
			cpArrowColor.setPrefSize(41, 25);
			cpNameColor.setPrefSize(41, 25);
			cpBorderArrowColor.setPrefSize(41, 25);
			tfName.setPrefSize(60, 18);
			tfName.setMinSize(60, 18);
			tfWeight.setPrefSize(41, 18);
			tfWeight.setMinSize(41, 18);
			separatorFP.setPrefSize(160, 10);
			return new ArrayList<Node>(Arrays.asList(lMainColor, cpMainColor, lArrowColor, cpArrowColor, lNameColor,
					cpNameColor, lBorderArrowColor, cpBorderArrowColor, cbEffectOnText, cbToAllEdges, separatorFP,
					lNameEdge, tfName, lWeightEdge, tfWeight, cbOrientation));
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
			return new ArrayList<>(Arrays.asList(bToDefault, pane, bSave, bCancel));
		}

		private void setButtonsListeners() {
			bCancel.setOnAction(e -> settingWindow.close());
			bToDefault.setOnAction(event -> {
				lineSample.setStyle(EDGE_STYLE_DEFAULT);
				nameSample.setStyle(NAME_STYLE_DEFAULT);
				arrowSample.getPath().setStyle(ARROW_STYLE_DEFAULT);

				String color = null;
				String regex = "-fx-stroke:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(EDGE_STYLE_DEFAULT);
				if (m.find()) {
					color = EDGE_STYLE_DEFAULT.substring(m.start() + 12, m.end() - 1);
				}
				sSizeEdge.setValue(DEFAULT_SIZE_WEIGHT);
				cbEffectOnText.setSelected(false);
				cpMainColor.setValue(Color.web(color));
				weightSample.setColorWeight(color);
				weightSample.setSizeWeight(DEFAULT_SIZE_WEIGHT);
				String colorArrow = null;
				regex = "-fx-fill:\\s[^-]+;";
				p = Pattern.compile(regex);
				m = p.matcher(ARROW_STYLE_DEFAULT);
				if (m.find()) {
					colorArrow = ARROW_STYLE_DEFAULT.substring(m.start() + 10, m.end() - 1);
				}
				cpArrowColor.setValue(Color.web(colorArrow));
				String colorArrowBorder = null;
				regex = "-fx-stroke:\\s[^-]+;";
				p = Pattern.compile(regex);
				m = p.matcher(ARROW_STYLE_DEFAULT);
				if (m.find()) {
					colorArrowBorder = ARROW_STYLE_DEFAULT.substring(m.start() + 12, m.end() - 1);
				}
				cpBorderArrowColor.setValue(Color.web(colorArrowBorder));
				String colorName = null;
				regex = "-fx-fill:\\s[^-]+;";
				p = Pattern.compile(regex);
				m = p.matcher(NAME_STYLE_DEFAULT);
				if (m.find()) {
					colorName = NAME_STYLE_DEFAULT.substring(m.start() + 10, m.end() - 1);
				}
				cpNameColor.setValue(Color.web(colorName));
				sSizeWeight.setValue(DEFAULT_SIZE_WEIGHT);
				sSizeArrow.setValue(DEFAULT_SIZE_ARROW);
				sSizeEdge.setValue(DEFAULT_SIZE_EDGE);
				sSizeName.setValue(DEFAULT_SIZE_NAME);
				tfName.setText(getName());
				tfWeight.setText(Integer.toString(WeightEdge.defaultValue));
				cbOrientation.setSelected(graph.getOrientationProperty().get() == Graph.OrientationGraph.ORIENTED);
			});
			bSave.setOnAction(event -> {
				if (!isToAll) {
					setColorEdge(choosenMainColor);
					setColorName(choosenNameColor);
					setColorArrow(choosenArrowColor);
					setColorArrowStroke(choosenArrowBorderColor);
					setSizeEdge(edgeSize);
					setSizeArrow(arrowSize);
					setSizeName(nameSize);
					setSizeWeight(weightSize);
					weight.setColorWeight(choosenMainColor);
					shadowOnText(shadowOn);
					setOrientationEdge(isOriented ? Graph.OrientationGraph.ORIENTED : Graph.OrientationGraph.NON_ORIENTED);
				} else {
					graph.getEdges().forEach((name, edge) -> {
						edge.setColorEdge(choosenMainColor);
						edge.setColorName(choosenNameColor);
						edge.setColorArrow(choosenArrowColor);
						edge.setColorArrowStroke(choosenArrowBorderColor);
						edge.setSizeEdge(edgeSize);
						edge.setSizeArrow(arrowSize);
						edge.setSizeName(nameSize);
						edge.setSizeWeight(weightSize);
						edge.weight.setColorWeight(choosenMainColor);
						edge.shadowOnText(shadowOn);
						edge.setOrientationEdge(isOriented ? Graph.OrientationGraph.ORIENTED : Graph.OrientationGraph.NON_ORIENTED);
					});
				}
				setUserName(newName);
				setWeight(weightVal);
				settingWindow.close();
				// TODO
			});
			bCancel.setCancelButton(true);
			bSave.setDefaultButton(true);
		}

		private void setCheckBoxesListeners() {
			cbEffectOnText.selectedProperty().addListener((obs, o, n) -> {

				String shadow = "-fx-effect: dropshadow(one-pass-box,black,5,0,0,1);";
				String regex = "-fx-effect:\\sdropshadow\\(one-pass-box,black,5,0,0,1\\);";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(nameSample.getStyle());
				if (n) {
					nameSample.setStyle(nameSample.getStyle() + " " + shadow);
				} else {
					if (m.find()) {
						nameSample.setStyle(m.replaceAll(""));
					}
				}
				shadowOn = n;
			});

			cbToAllEdges.selectedProperty().addListener((obs, oldv, newv) -> isToAll = newv);

			cbOrientation.selectedProperty().addListener((obs, o, n) -> {
				isOriented = n;
				arrowSample.getPath().setVisible(n);
			});
		}

		private void setColorsListeners() {
			cpMainColor.valueProperty().addListener((obs, o, n) -> {
				choosenMainColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
						+ (n.getBlue() * 100) + "%)";
				String regex = "-fx-stroke:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(lineSample.getStyle());
				lineSample.setStyle(m.replaceAll("-fx-stroke: " + choosenMainColor + ";"));
				m = p.matcher(weightSample.getRectang().getStyle());
				weightSample.setColorWeight(choosenMainColor);
			});

			cpArrowColor.valueProperty().addListener((obs, o, n) -> {
				choosenArrowColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
						+ (n.getBlue() * 100) + "%)";
				String regex = "-fx-fill:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(arrowSample.getPath().getStyle());
				arrowSample.getPath().setStyle(m.replaceAll("-fx-fill: " + choosenArrowColor + ";"));
			});

			cpNameColor.valueProperty().addListener((obs, o, n) -> {
				choosenNameColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
						+ (n.getBlue() * 100) + "%)";
				String regex = "-fx-fill:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(nameSample.getStyle());
				nameSample.setStyle(m.replaceAll("-fx-fill: " + choosenNameColor + ";"));
			});

			cpBorderArrowColor.valueProperty().addListener((obs, o, n) -> {
				choosenArrowBorderColor = "rgb(" + (n.getRed() * 100) + "%," + (n.getGreen() * 100) + "%,"
						+ (n.getBlue() * 100) + "%)";
				String regex = "-fx-stroke:\\s[^-]+;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(arrowSample.getPath().getStyle());
				arrowSample.getPath().setStyle(m.replaceAll("-fx-stroke: " + choosenArrowBorderColor + ";"));
			});
		}

		private void setSlidersListeners() {
			sSizeEdge.valueProperty().addListener((obs, o, n) -> {
				edgeSize = (Double) n;
				String regex = "-fx-stroke-width:\\s\\d+\\.?\\d*px;";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(lineSample.getStyle());
				lineSample.setStyle(m.replaceAll("-fx-stroke-width: " + edgeSize + "px;"));
			});

			sSizeName.valueProperty().addListener((obs, o, n) -> {
				nameSize = (Double) n;
				nameSample.setFont(new Font(getLabelName().getFont().getName(), nameSize));
				nameSample.setLayoutX(new Point2D(lineSample.getStartX(), lineSample.getStartY())
						.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getX()
						- ((nameSize / 2)) * nameSample.getText().length() / 2);
				nameSample.setLayoutY(new Point2D(lineSample.getStartX(), lineSample.getStartY())
						.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getY()
						- weightSample.getRectang().getPrefHeight() / (1.5));
			});

			sSizeArrow.valueProperty().addListener((obs, o, n) -> {
				arrowSize = (Double) n;
				arrowSample.size = arrowSize;
				arrowSample.setSize(arrowSize);
			});

			sSizeWeight.valueProperty().addListener((obs, o, n) -> {
				weightSize = (Double) n;
				weightSample.setSizeWeight(weightSize);
				weightSample.setSetOnLine(lineSample);
				nameSample.setLayoutX(new Point2D(lineSample.getStartX(), lineSample.getStartY())
						.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getX()
						- ((nameSize / 2)) * nameSample.getText().length() / 2);
				nameSample.setLayoutY(new Point2D(lineSample.getStartX(), lineSample.getStartY())
						.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getY()
						- weightSample.getRectang().getPrefHeight() / (1.5));
			});
		}

		private void setTextFieldsListeners() {
			tfName.textProperty().addListener((obs, oldv, newv) -> {
				if (newv.length() > 0) {
					nameSample.setText(newv);
					newName = newv;
					nameSample.setLayoutX(new Point2D(lineSample.getStartX(), lineSample.getStartY())
							.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getX()
							- ((nameSize / 2)) * nameSample.getText().length() / 2);
					nameSample.setLayoutY(new Point2D(lineSample.getStartX(), lineSample.getStartY())
							.midpoint(new Point2D(lineSample.getEndX(), lineSample.getEndY())).getY()
							- weightSample.getRectang().getPrefHeight() / (1.5));
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
			tfWeight.textProperty().addListener((obs, oldv, newv) -> {
				if (newv.length() > 0) {
					weightSample.getTextValue().setText(newv);
					weightVal = Integer.parseInt(newv);
				}
			});
			// provided
// filter
			filterDelimited = t -> {
				if (t.getText().matches("[^0123456789]") || (tfWeight.getText().length() > 8 && t.isAdded()))
					return null;
				return t;
			};
			formatterDelimited = new TextFormatter<>(filterDelimited);
			tfWeight.setTextFormatter(formatterDelimited);
		}

		public void show() {
			settingWindow.show();
		}
	}

	private class WeightEdge {
		public static final int defaultValue = 1;
		private int value;
		private StackPane rectang;
		private EditableLabel textValue;
		private double size = getSizeWeight();
		private PathEdge line;
		private Line lineSample;

		public WeightEdge(int weigthValue, PathEdge line) {
			if (weigthValue < 1)
				this.value = defaultValue;
			else
				this.value = weigthValue;
			initateGraphicsElements();
		}

		private WeightEdge(int weigthValue, Line line) {
			if (weigthValue < 1)
				this.value = defaultValue;
			else
				this.value = weigthValue;
			initateGraphicsElements();
		}

		public WeightEdge(PathEdge line) {
			this.value = defaultValue;
			initateGraphicsElements();
		}

		public StackPane getRectang() {
			return rectang;
		}

		public EditableLabel getTextValue() {
			return textValue;
		}

		public int getValue() {
			return value;
		}

		private void initateGraphicsElements() {
			textValue = new EditableLabel(String.valueOf(value));
			textValue.setFont(new Font("Calibri Bold", size));
			rectang = new StackPane();
			rectang.setPrefHeight(textValue.getFont().getSize() + 1);
			rectang.setMinHeight(textValue.getFont().getSize() + 1);
			// graphic setting
			getRectang().setStyle("-fx-background-color: #ffffff; -fx-padding: 2px 1px 2px 2px; -fx-border-width: "
					+ (sizeEdge * 0.35) + "px; -fx-border-color: " + getColorEdge() + ";");
			textValue.setStyle("-fx-text-fill: " + getColorEdge() + ";");
			settingEditableLabel();
//			rectang.setStyle("-fx-background-color: #ffffff; -fx-border-width: " + sizeWeight * 0.1
//					+ "px; -fx-border-color: #FF8C00;"); // #FFA500;
			rectang.getChildren().add(textValue);
			if (!showWeight)
				rectang.setVisible(false);
		}

		public void setColorWeight(String color) {
			String regex = "-fx-border-color:\\s[^-]+;";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(rectang.getStyle());
			rectang.setStyle(m.replaceAll("-fx-border-color: " + color + ";"));
			regex = "-fx-text-fill:\\s[^-]+;";
			p = Pattern.compile(regex);
			m = p.matcher(textValue.getStyle());
			textValue.setStyle(m.replaceAll("-fx-text-fill: " + color + ";"));
		}

		public void setSetOnLine(PathEdge line) {
			this.line = line;
			if (!line.isCurve()) {
				if (getTextValue().getGraphic() != null) {
					getRectang()
							.setLayoutX(new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY())
									.midpoint(new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY()))
									.getX() - textValue.getBoundsInLocal().getWidth() / 2);
					getRectang()
							.setLayoutY(new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY())
									.midpoint(new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY()))
									.getY() - textValue.getBoundsInLocal().getHeight() / 2);
				} else {
					getRectang()
							.setLayoutX(new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY())
									.midpoint(new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY()))
									.getX() - ((size / 2)) * getTextValue().getText().length() / 2 - (size / 4));
					getRectang()
							.setLayoutY(new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY())
									.midpoint(new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY()))
									.getY() - (size / 2));
				}
			} else {
				Point2D middle = line.getOnEdgePoint(0.5);
				if (getTextValue().getGraphic() != null) {
					getRectang().setLayoutX(middle.getX() - textValue.getBoundsInParent().getWidth() / 2);
					getRectang().setLayoutY(middle.getY() - textValue.getBoundsInParent().getHeight() / 2);
				} else {
					getRectang().setLayoutX(
							middle.getX() - ((size / 2)) * getTextValue().getText().length() / 2 - (size / 4));
					getRectang().setLayoutY(middle.getY() - (size / 2));
				}
			}
		}

		//
		private void setSetOnLine(Line line) {
			this.lineSample = line;
			if (getTextValue().getGraphic() != null) {
				getRectang().setLayoutX(new Point2D(line.getStartX(), line.getStartY())
						.midpoint(new Point2D(line.getEndX(), line.getEndY())).getX()
						- textValue.getBoundsInLocal().getWidth() / 2);
				getRectang().setLayoutY(new Point2D(line.getStartX(), line.getStartY())
						.midpoint(new Point2D(line.getEndX(), line.getEndY())).getY()
						- textValue.getBoundsInLocal().getHeight() / 2);
			} else {
				getRectang().setLayoutX(new Point2D(line.getStartX(), line.getStartY())
						.midpoint(new Point2D(line.getEndX(), line.getEndY())).getX()
						- ((size / 2)) * getTextValue().getText().length() / 2 - (size / 4));
				getRectang().setLayoutY(new Point2D(line.getStartX(), line.getStartY())
						.midpoint(new Point2D(line.getEndX(), line.getEndY())).getY() - (size / 2));
			}
		}

		public void setSizeWeight(double size) {
			this.size = size;
			textValue.setFont(new Font("Calibri Bold", size));
			rectang.setPrefHeight(textValue.getFont().getSize() + 1);
			rectang.setMinHeight(textValue.getFont().getSize() + 1);
			if (line != null)
				setSetOnLine(line);
			else
				setSetOnLine(lineSample);
		}

		private void settingEditableLabel() {
			textValue.getTf().setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:transparent; "); // -fx-background-color:transparent;
			textValue.getTf().setPrefSize(34, textValue.getFont().getSize() + 1);
			textValue.getTf().setMinHeight(textValue.getFont().getSize() + 1);
			textValue.getTf().setFont(new Font("System", 11));
			// provided
// filter
			UnaryOperator<TextFormatter.Change> filterDelimited = t -> {
				if (t.getText().matches("[^0123456789]")
						|| (textValue.getTf().getText().length() > 8 && t.isAdded()))
					return null;
				return t;
			};
			TextFormatter<String> formatterDelimited = new TextFormatter<>(filterDelimited);
			textValue.getTf().setTextFormatter(formatterDelimited);
			textValue.textProperty().addListener((observable, oldValue, newValue) -> {
				if (!newValue.equals("")) {
					setValue(Integer.parseInt(newValue));
					// System.out.println("width = " + textValue.getWidth());
					if (line != null) {
					getRectang().setLayoutX(
							new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY()).midpoint(
									new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY()))
									.getX() - ((size / 2)) * getTextValue().getText().length() / 2 - (size / 4));
					getRectang().setLayoutY(
							new Point2D(line.getEdgeStartPoint().getX(), line.getEdgeStartPoint().getY()).midpoint(
									new Point2D(line.getEdgeEndPoint().getX(), line.getEdgeEndPoint().getY()))
									.getY() - (size / 2));
					} else {
						getRectang().setLayoutX(
								new Point2D(lineSample.getStartX(), lineSample.getStartY()).midpoint(
										new Point2D(lineSample.getEndX(), lineSample.getEndY()))
										.getX() - ((size / 2)) * getTextValue().getText().length() / 2 - (size / 4));
						getRectang().setLayoutY(
								new Point2D(lineSample.getStartX(), lineSample.getStartY()).midpoint(
										new Point2D(lineSample.getEndX(), lineSample.getEndY()))
										.getY() - (size / 2));
					}
				}
			});
		}

		public void setValue(int value) {
			// System.out.println(value);
			this.value = Math.max(value, 1);
		}
	}

	public class PathEdge extends Path {

		private boolean isCurve;

		public PathEdge(boolean isCurve) {
			super();
			if (vertex1 != vertex2)
				this.isCurve = isCurve;
			else
				this.isCurve = true;
			buildPath();
		}

		public void buildPath() {
			if (!getElements().isEmpty())
				getElements().clear();
			if (isLoop) {
				buildLoopPath();
				return;
			}
			if (isCurve) {
				buildCurvePath();
			} else {
				buildStraightLine();
			}
		}

		private void buildLoopPath() {
			Pair<Point2D> startEnd = solveConnectorsCoordinates();
			MoveTo start = new MoveTo(startEnd.getFirst().getX(), startEnd.getFirst().getY());
			Point2D control1 = new Point2D(startEnd.getFirst().getX() - 2.5 * vertex1.getDefaultRadius(),
					startEnd.getFirst().getY() - 3 * vertex1.getDefaultRadius());
			Point2D control2 = new Point2D(startEnd.getSecond().getX() + 2.5 * vertex1.getDefaultRadius(),
					startEnd.getSecond().getY() - 3 * vertex1.getDefaultRadius());
			CubicCurveTo end = new CubicCurveTo(control1.getX(), control1.getY(), control2.getX(), control2.getY(),
					startEnd.getSecond().getX(), startEnd.getSecond().getY());
			getElements().addAll(start, end);
		}

		private void buildStraightLine() {
			Pair<Point2D> startEnd = solveConnectorsCoordinates();
			MoveTo start = new MoveTo(startEnd.getFirst().getX(), startEnd.getFirst().getY());
			LineTo end = new LineTo(startEnd.getSecond().getX(), startEnd.getSecond().getY());
			getElements().addAll(start, end);
		}

		private double defaultRadius = 30;
		private double defaultAngleCurvePoint = Math.toRadians(90);

		private MoveTo start;
		private QuadCurveTo end;

		private void buildCurvePath() {
			Pair<Point2D> startEndTemp = solveConnectorsCoordinates();
			Point2D base = startEndTemp.getFirst().midpoint(startEndTemp.getSecond());// new Point2D(
			double radius = defaultRadius;
			double angleCurvePoint = defaultAngleCurvePoint;
			double angle = Math.atan2(startEndTemp.getFirst().getY() - startEndTemp.getSecond().getY(),
					startEndTemp.getFirst().getX() - startEndTemp.getSecond().getX());
			Point2D curve = new Point2D(base.getX() + radius * Math.cos(angleCurvePoint + angle),
					base.getY() + radius * Math.sin(angleCurvePoint + angle));
			Pair<Point2D> startEnd = solveConnectorsCoordinatesCurve(curve);
			start = new MoveTo(startEnd.getFirst().getX(), startEnd.getFirst().getY());
			end = new QuadCurveTo(curve.getX(), curve.getY(), startEnd.getSecond().getX(), startEnd.getSecond().getY());
			getElements().addAll(start, end);
		}

		private Pair<Point2D> solveConnectorsCoordinatesCurve(Point2D curvePoint) {
			double xNew;
			double yNew;
			double xNew2;
			double yNew2;
			if (vertex1 != vertex2) {
				double distance;
				double ko;
				distance = new Point2D(curvePoint.getX(), curvePoint.getY())
						.distance(vertex2.getVertexCircle().getCenterX(), vertex2.getVertexCircle().getCenterY());
				// TODO �������� ������ ko � ������ ����� �������
				ko = (vertex2.getVertexCircle().getRadius() + vertex2.getDefaultStrokeWidth()) / distance;
				xNew = vertex2.getVertexCircle().getCenterX()
						+ (curvePoint.getX() - vertex2.getVertexCircle().getCenterX()) * ko;
				yNew = vertex2.getVertexCircle().getCenterY()
						+ (curvePoint.getY() - vertex2.getVertexCircle().getCenterY()) * ko;

				distance = new Point2D(curvePoint.getX(), curvePoint.getY())
						.distance(vertex1.getVertexCircle().getCenterX(), vertex1.getVertexCircle().getCenterY());
				double ko2;
				ko2 = (vertex1.getVertexCircle().getRadius() + vertex1.getDefaultStrokeWidth()) / distance;
				xNew2 = vertex1.getVertexCircle().getCenterX()
						+ (curvePoint.getX() - vertex1.getVertexCircle().getCenterX()) * ko2;
				yNew2 = vertex1.getVertexCircle().getCenterY()
						+ (curvePoint.getY() - vertex1.getVertexCircle().getCenterY()) * ko2;
			} else {
				xNew2 = vertex1.getVertexCircle().getCenterX() - vertex1.getDefaultRadius() + 3;
				yNew2 = vertex1.getVertexCircle().getCenterY() - vertex1.getDefaultRadius() + 3;
				xNew = vertex1.getVertexCircle().getCenterX() + vertex1.getDefaultRadius() - 3;
				yNew = vertex1.getVertexCircle().getCenterY() - vertex1.getDefaultRadius() + 3;
			}
			// System.out.println("x1: " + xNew2 + " y1: " + yNew2 + " x2: " + xNew + " y2:
			// " + yNew);
			return new Pair<>(new Point2D(xNew2, yNew2), new Point2D(xNew, yNew));
		}

		private Pair<Point2D> solveConnectorsCoordinates() {
			double distance;
			double ko;
			double xNew;
			double yNew;
			double xNew2;
			double yNew2;
			if (vertex1 != vertex2) {
				distance = new Point2D(vertex1.getVertexCircle().getCenterX(), vertex1.getVertexCircle().getCenterY())
						.distance(vertex2.getVertexCircle().getCenterX(), vertex2.getVertexCircle().getCenterY());
				// TODO �������� ������ ko � ������ ����� �������
				ko = (vertex2.getVertexCircle().getRadius() + vertex2.getDefaultStrokeWidth()) / distance;
				xNew = vertex2.getVertexCircle().getCenterX()
						+ (vertex1.getVertexCircle().getCenterX() - vertex2.getVertexCircle().getCenterX()) * ko;
				yNew = vertex2.getVertexCircle().getCenterY()
						+ (vertex1.getVertexCircle().getCenterY() - vertex2.getVertexCircle().getCenterY()) * ko;
				double ko2;
				ko2 = (vertex1.getVertexCircle().getRadius() + vertex1.getDefaultStrokeWidth()) / distance;
				xNew2 = vertex1.getVertexCircle().getCenterX()
						+ (vertex2.getVertexCircle().getCenterX() - vertex1.getVertexCircle().getCenterX()) * ko2;
				yNew2 = vertex1.getVertexCircle().getCenterY()
						+ (vertex2.getVertexCircle().getCenterY() - vertex1.getVertexCircle().getCenterY()) * ko2;
			} else {
				xNew2 = vertex1.getVertexCircle().getCenterX() - vertex1.getDefaultRadius() + 2;
				yNew2 = vertex1.getVertexCircle().getCenterY() - vertex1.getDefaultRadius() + 2;
				xNew = vertex1.getVertexCircle().getCenterX() + vertex1.getDefaultRadius() - 2;
				yNew = vertex1.getVertexCircle().getCenterY() - vertex1.getDefaultRadius() + 2;
			}
			return new Pair<>(new Point2D(xNew2, yNew2), new Point2D(xNew, yNew));
		}

		public Point2D getEdgeStartPoint() {
			if (getElements().isEmpty())
				return null;
			PathElement pe = getElements().get(0);
			if (pe instanceof MoveTo) {
				MoveTo m = (MoveTo) pe;
				return new Point2D(m.getX(), m.getY());
			}
			return null;
		}

		public Point2D getEdgeEndPoint() {
			if (getElements().isEmpty())
				return null;
			PathElement pe = getElements().get(getElements().size() - 1);
			if (pe instanceof QuadCurveTo) {
				QuadCurveTo qc = (QuadCurveTo) pe;
				return new Point2D(qc.getX(), qc.getY());
			}
			if (pe instanceof LineTo) {
				LineTo l = (LineTo) pe;
				return new Point2D(l.getX(), l.getY());
			}
			if (pe instanceof CubicCurveTo) {
				CubicCurveTo cq = (CubicCurveTo) pe;
				return new Point2D(cq.getX(), cq.getY());
			}
			return null;
		}

		public Point2D getEdgeCurvePoint() {
			// TODO
			if (!isCurve)
				return null;
			PathElement pe = getElements().get(getElements().size() - 1);
			if (pe instanceof QuadCurveTo) {
				QuadCurveTo qc = (QuadCurveTo) pe;
				return new Point2D(qc.getControlX(), qc.getControlY());
			}
			if (pe instanceof CubicCurveTo) {
				CubicCurveTo cq = (CubicCurveTo) pe;
				return new Point2D(cq.getControlX2(), cq.getControlY2());
			}
			return null;
		}

		/**
		 * ���������� ����� �� �����
		 * 
		 * @param t - ���������� �� ������ ����� �� ����� (0 - ������, 1 - �����)
		 * @return ����� Point2D ��� null, ���� t ������ ���� ��� ������ �������
		 */
		public Point2D getOnEdgePoint(double t) {
			if (t < 0 || t > 1)
				return null;
			if (isLoop) {
				PathElement pe = getElements().get(getElements().size() - 1);
				Point2D curve1 = null;
				Point2D curve2 = null;
				if (pe instanceof CubicCurveTo) {
					CubicCurveTo cq = (CubicCurveTo) pe;
					curve1 = new Point2D(cq.getControlX1(), cq.getControlY1());
					curve2 = new Point2D(cq.getControlX2(), cq.getControlY2());
				}
				Point2D start = getEdgeStartPoint();
				Point2D end = getEdgeEndPoint();
				return new Point2D(
						Math.pow((1 - t), 3) * start.getX() + 3 * t * Math.pow((1 - t), 2) * curve1.getX()
								+ 3 * t * t * (1 - t) * curve2.getX() + t * t * t * end.getX(),
						Math.pow((1 - t), 3) * start.getY() + 3 * t * Math.pow((1 - t), 2) * curve1.getY()
								+ 3 * t * t * (1 - t) * curve2.getY() + t * t * t * end.getY());
			} else
			// (1 - t)^2*P0 + 2*(1 - t)*t*P1 + t^2*P2
			if (isCurve) {
				Point2D curve = getEdgeCurvePoint();
				Point2D start = getEdgeStartPoint();
				Point2D end = getEdgeEndPoint();
				return new Point2D(
						Math.pow((1 - t), 2) * start.getX() + 2 * (1 - t) * t * curve.getX() + t * t * end.getX(),
						Math.pow((1 - t), 2) * start.getY() + 2 * (1 - t) * t * curve.getY() + t * t * end.getY());
			} else {
				Point2D start = getEdgeStartPoint();
				Point2D end = getEdgeEndPoint();
				return new Point2D((1 - t) * start.getX() + t * end.getX(),
						(1 - t) * start.getY() + t * end.getY());
			}
		}

		private Group controlGroup;

		private void showControlsPoints() {
			if (!isCurve)
				return;
			if (controlGroup != null && groupEdge.getChildren().contains(controlGroup))
				groupEdge.getChildren().remove(controlGroup);
			controlGroup = new Group();
			if (isLoop) {
//				PathElement pe = getElements().get(getElements().size() - 1);
//				if (pe instanceof CubicCurveTo) {
//					CubicCurveTo cc = (CubicCurveTo) pe;
//					Circle c1 = new Circle(cc.getControlX1(), cc.getControlY1(), 4, Paint.valueOf("#B0C4DE"));
//					c1.setStroke(Paint.valueOf("black"));
//					c1.setOnMouseDragged(value -> {
//						c1.setCenterX(value.getX());
//						c1.setCenterY(value.getY());
//						cc.setControlX1(value.getX());
//						cc.setControlY1(value.getY());
//					});
//					Circle c2 = new Circle(cc.getControlX2(), cc.getControlY2(), 4, Paint.valueOf("#B0C4DE"));
//					c2.setStroke(Paint.valueOf("black"));
//					c2.setOnMouseDragged(value -> {
//						c2.setCenterX(value.getX());
//						c2.setCenterY(value.getY());
//						cc.setControlX2(value.getX());
//						cc.setControlY2(value.getY());
//					});
//					controlGroup.getChildren().addAll(c1, c2);
//				}
			} else {
				PathElement pe = getElements().get(getElements().size() - 1);
				if (pe instanceof QuadCurveTo) {
					QuadCurveTo qc = (QuadCurveTo) pe;
					Circle c = new Circle(qc.getControlX(), qc.getControlY(), 4);

					//c.centerXProperty().bind(qc.controlXProperty());
					//c.centerYProperty().bind(qc.controlYProperty());

					c.setFill(Paint.valueOf("#B0C4DE"));
					c.setStroke(Paint.valueOf("black"));
					Line l1 = new Line(((MoveTo) getElements().get(0)).getX(), ((MoveTo) getElements().get(0)).getY(),
							qc.getControlX(), qc.getControlY());

					//l1.startXProperty().bind(((MoveTo) getElements().get(0)).xProperty());
					//l1.startYProperty().bind(((MoveTo) getElements().get(0)).yProperty());
					//l1.endXProperty().bind(qc.controlXProperty());
					//l1.endYProperty().bind(qc.controlYProperty());


					l1.setStroke(Paint.valueOf("#4682B4"));
					Line l2 = new Line(qc.getX(), qc.getY(), qc.getControlX(), qc.getControlY());

					//l2.startXProperty().bind(qc.xProperty());
					//l2.startYProperty().bind(qc.yProperty());
					//l2.endXProperty().bind(qc.controlXProperty());
					//l2.endYProperty().bind(qc.controlYProperty());

					l2.setStroke(Paint.valueOf("#4682B4"));
					c.setOnMouseEntered(value -> c.setFill(Paint.valueOf("red")));
					c.setOnMouseExited(value -> c.setFill(Paint.valueOf("#B0C4DE")));
					c.setOnMouseDragged(value -> {
						c.setCenterX(value.getX());
						c.setCenterY(value.getY());
						l1.setEndX(value.getX());
						l1.setEndY(value.getY());
						l2.setEndX(value.getX());
						l2.setEndY(value.getY());
						qc.setControlX(value.getX());
						qc.setControlY(value.getY());
						MoveTo mt = (MoveTo) getElements().get(0);
						l1.setStartX(mt.getX());
						l1.setStartY(mt.getY());
						Point2D base = new Point2D(mt.getX(), mt.getY()).midpoint(qc.getX(), qc.getY());
						defaultRadius = (new Point2D(value.getX(), value.getY()))
								.distance(new Point2D(base.getX(), base.getY()));
						double angle = Math.atan2(qc.getY() - mt.getY(), qc.getX() - mt.getX());
						defaultAngleCurvePoint = Math.atan2(base.getY() - value.getY(), base.getX() - value.getX())
								- angle;
						setCoordinatesEdge();
						l2.setStartX(((QuadCurveTo) getElements().get(getElements().size() - 1)).getX());
						l2.setStartY(((QuadCurveTo) getElements().get(getElements().size() - 1)).getY());
					});
					controlGroup.getChildren().addAll(l1, l2, c);
				}
			}
			groupEdge.getChildren().add(controlGroup);
			// controlGroup.toBack();
		}

		private void hideControlsPoints() {
			if (controlGroup != null)
				groupEdge.getChildren().remove(controlGroup);
		}

		public boolean isCurve() {
			return isCurve;
		}

		public void setCurve(boolean isCurve) {
			if (vertex1 == vertex2)
				return;
			this.isCurve = isCurve;
			if (!isCurve)
				hideControlsPoints();
		}

	}

	// -fx-stroke: #FFC618
	public static String EDGE_STYLE_DEFAULT = "-fx-stroke: #000000; -fx-stroke-width: " + 4 + "px; -fx-smooth: true;";
	public static final String NAME_STYLE_DEFAULT = "-fx-fill: #000000;";
	public static final String ARROW_STYLE_DEFAULT = "-fx-stroke-width: " + (4 * 0.35)
			+ "px; -fx-stroke: #000000; -fx-fill: RED;";
	public static final double DEFAULT_SIZE_ARROW = 12;

	public static final double DEFAULT_SIZE_NAME = 15;
	public static final double DEFAULT_SIZE_EDGE = 4;
	public static final double DEFAULT_SIZE_WEIGHT = 15;
	public static boolean IS_CURVE_NEW_EDGES;
	private final Graph graph;
	private Vertex vertex1;
	private Vertex vertex2;
	// public int orientation;
	private final SimpleObjectProperty<Graph.OrientationGraph> orientationEdgeProperty;

	private String name;
	// ��� �����
	private final WeightEdge weight;
	private boolean showWeight;
	private PathEdge edgeLine;
	private Text labelName;
	private final SimpleBooleanProperty showNameEdge = new SimpleBooleanProperty();
	private Group groupEdge;
	private boolean isChosedModeOn = false;
	private double sizeArrow = DEFAULT_SIZE_ARROW;

	private double sizeName = DEFAULT_SIZE_NAME;

	private double sizeEdge = DEFAULT_SIZE_EDGE;
	private double sizeWeight = DEFAULT_SIZE_WEIGHT;
	private boolean isShadowOn = false;
	private boolean isLoop = false;
	private String edgeStyle = EDGE_STYLE_DEFAULT;// "-fx-stroke: #000000; -fx-stroke-width: " + sizeEdge + "px;
													// -fx-smooth: true;";
	private String choosedEdgeStyle = "-fx-stroke: #4682B4; -fx-stroke-width: " + (sizeEdge + 1)
			+ "px; -fx-smooth: true;";

	private String nameStyle = "-fx-fill: #000000;";

	private String arrowStyle = "-fx-stroke-width: " + (sizeEdge * 0.35) + "px; -fx-stroke: #000000; -fx-fill: RED;";

	private String choosedArrowStyle = "-fx-stroke-width: " + (sizeEdge * 0.35 + 1)
			+ "px; -fx-stroke: #4682B4; -fx-fill: #4682B4";

	private Arrow arrow;

	// �������� ������������ �� ����� ��� ���������
	private Circle circleStart;

	private Circle circleEnd;

	private Line tempLineChangingVertex;

	public Edge(Graph graph, Vertex vertex1, Vertex vertex2, String name) {
		this(graph, vertex1, vertex2, name, 1,graph.getOrientationProperty().get() != Graph.OrientationGraph.MIXED ? graph.getOrientationProperty().get()
				: Graph.OrientationGraph.NON_ORIENTED);
	}

	public Edge(Graph graph, Vertex vertex1, Vertex vertex2, String name, int weight,
				Graph.OrientationGraph orientation) {
		this.graph = graph;
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		if (vertex1 == vertex2)
			isLoop = true;
		this.setName(name);
		showWeight = graph.isShowWeightsEdges();
		this.weight = new WeightEdge(weight, edgeLine);
		orientationEdgeProperty = new SimpleObjectProperty<>();
		setOrientationEdge(orientation);
		initiateEdge();
		addEdgeToGraph();
	}

	private void initiateEdge() {
		groupEdge = new Group();
		arrow = new Arrow(sizeArrow);
		labelName = new Text(getName());
		edgeLine = new PathEdge(IS_CURVE_NEW_EDGES);
		if (!graph.getShowNamesEdges().get()) {
			labelName.setVisible(false);
		}
		labelName.setFont(new Font("Calibri Bold", sizeName));
		labelName.setPickOnBounds(false);
	}

	// ��������� ����� (�����) � �����
	public void addEdgeToGraph() {
		setCoordinatesEdge();
		settingWeightPosition();
		initiateHandlers();
		setChoseModeOff();
		groupEdge.getChildren().addAll(edgeLine, arrow.getPath(), labelName, weight.getRectang());
		graph.getGraphPane().getChildren().add(groupEdge);
		groupEdge.toBack();
		graph.getGraphPane().getTransparencyGraphPane().toBack();
	}

	private EventHandler<MouseEvent> choseEdgeHandler;

	private void initiateHandlers() {
		choseEdgeHandler = event -> graph.choseObject(Edge.this);
		edgeLine.setOnMousePressed(choseEdgeHandler);
		arrow.getPath().setOnMousePressed(choseEdgeHandler);
		weight.getRectang().setOnMousePressed(choseEdgeHandler);
		initiateContextMenu();
		groupEdge.setOnContextMenuRequested((value) -> contextEdge.show(edgeLine, value.getScreenX(), value.getScreenY()));
		showNameEdge.bind(graph.getShowNamesEdges());
		showNameEdge.addListener((observable, oldValue, newValue) -> labelName.setVisible(newValue));
	}

	// ���������� EventHadler "���������������" ��������� ������� ����� �� ������
	private EventHandler<MouseEvent> edgeChangeVertexHandler(Circle chosenCircle) {
		class VertexMovingHandler implements EventHandler<MouseEvent> {
			@Override
			public void handle(MouseEvent event) {
				Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
				Point2D local = graph.getGraphPane().screenToLocal(mouse.x, mouse.y);
				if (chosenCircle == circleStart) {
					circleEnd.setCenterX(edgeLine.getEdgeEndPoint().getX());
					circleEnd.setCenterY(edgeLine.getEdgeEndPoint().getY());
					circleStart.setCenterX(local.getX());
					circleStart.setCenterY(local.getY());
					if (tempLineChangingVertex == null) {
						tempLineChangingVertex = new Line(local.getX(), local.getY(), edgeLine.getEdgeEndPoint().getX(),
								edgeLine.getEdgeEndPoint().getY());
						tempLineChangingVertex.getStrokeDashArray().addAll(8., 4.);
						tempLineChangingVertex.setVisible(true);
					} else {
						tempLineChangingVertex.setVisible(true);
						tempLineChangingVertex.setStartX(local.getX());
						tempLineChangingVertex.setStartY(local.getY());
						tempLineChangingVertex.setEndX(edgeLine.getEdgeEndPoint().getX());
						tempLineChangingVertex.setEndY(edgeLine.getEdgeEndPoint().getY());
					}
					if (!graph.getGraphPane().getChildren().contains(tempLineChangingVertex)) {
						graph.getGraphPane().getChildren().add(tempLineChangingVertex);
						tempLineChangingVertex.toBack();
						graph.getGraphPane().getTransparencyGraphPane().toBack();
					}
				} else if (chosenCircle == circleEnd) {
					circleStart.setCenterX(edgeLine.getEdgeStartPoint().getX());
					circleStart.setCenterY(edgeLine.getEdgeStartPoint().getY());
					circleEnd.setCenterX(local.getX());
					circleEnd.setCenterY(local.getY());
					if (tempLineChangingVertex == null) {
						tempLineChangingVertex = new Line(edgeLine.getEdgeStartPoint().getX(),
								edgeLine.getEdgeStartPoint().getY(), local.getX(), local.getY());
						tempLineChangingVertex.getStrokeDashArray().addAll(8., 4.);
						tempLineChangingVertex.setVisible(true);
					} else {
						tempLineChangingVertex.setVisible(true);
						tempLineChangingVertex.setStartX(edgeLine.getEdgeStartPoint().getX());
						tempLineChangingVertex.setStartY(edgeLine.getEdgeStartPoint().getY());
						tempLineChangingVertex.setEndX(local.getX());
						tempLineChangingVertex.setEndY(local.getY());
					}
					if (!graph.getGraphPane().getChildren().contains(tempLineChangingVertex)) {
						graph.getGraphPane().getChildren().add(tempLineChangingVertex);
						tempLineChangingVertex.toBack();
						graph.getGraphPane().getTransparencyGraphPane().toBack();
					}
				}
			}
		}
		return new VertexMovingHandler();
	}

	// ���������� EventHadler "���������������" ��������� ������� ����� �� ������
	private EventHandler<MouseEvent> edgeChangeVertexHandlerOnRealease(Circle chosenCircle) {
		class VertexMovingHandlerOnRealease implements EventHandler<MouseEvent> {
			@Override
			public void handle(MouseEvent event) {
				if (tempLineChangingVertex == null) {
					return;
				}
				for (Entry<Integer, Vertex> entryVertex : graph.getVertexes().entrySet()) {
					if (entryVertex.getValue().getVertexCircle().intersects(chosenCircle.getBoundsInLocal())) {
						if (chosenCircle == circleStart) {
							if (entryVertex.getValue() == vertex1 || entryVertex.getValue() == vertex2) {
								setCoordinatesTempFigureToDefault();
								return;
							}
							graph.removeEdge(getName());
							vertex1 = entryVertex.getValue();

							setName(entryVertex.getValue().getNumber() + " " + vertex2.getNumber());
							if (vertex1 != vertex2) {
								isLoop = false;
								curveItem.setDisable(false);
							}
							setCoordinatesEdge();
							graph.addEdge(Edge.this);
							graph.getGraphPane().getChildren().add(groupEdge);
							groupEdge.toBack();
							graph.getGraphPane().getTransparencyGraphPane().toBack();

							setCoordinatesTempFigureToDefault();
							entryVertex.getValue().getVertexCircle().setOnMouseDragReleased(null);
							return;
						} else if (chosenCircle == circleEnd) {
							if (entryVertex.getValue() == vertex1 || entryVertex.getValue() == vertex2) {
								setCoordinatesTempFigureToDefault();
								return;
							}
							graph.removeEdge(getName());
							vertex2 = entryVertex.getValue();
							setName(vertex1.getNumber() + " " + entryVertex.getValue().getNumber());
							if (vertex1 != vertex2) {
								isLoop = false;
								curveItem.setDisable(false);
							}
							setCoordinatesEdge();

							graph.addEdge(Edge.this);
							graph.getGraphPane().getChildren().add(groupEdge);
							groupEdge.toBack();
							graph.getGraphPane().getTransparencyGraphPane().toBack();

							setCoordinatesTempFigureToDefault();
							entryVertex.getValue().getVertexCircle().setOnMouseDragReleased(null);
							return;
						}
					}
				}
				setCoordinatesTempFigureToDefault();
			}
		}
		return new VertexMovingHandlerOnRealease();
	}

	public String getArrowStyle() {
		return arrowStyle;
	}

	public String getChoosedArrowStyle() {
		return choosedArrowStyle;
	}

	public String getChoosedEdgeStyle() {
		return choosedEdgeStyle;
	}

	public String getColorArrow() {
		String color = null;
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(arrowStyle);
		if (m.find()) {
			color = arrowStyle.substring(m.start() + 10, m.end() - 1);
		}
		return color;
	}

	public String getColorArrowStroke() {
		String color = null;
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(arrowStyle);
		if (m.find()) {
			color = arrowStyle.substring(m.start() + 12, m.end() - 1);
		}
		return color;
	}

	public String getColorEdge() {
		String color = null;
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(edgeStyle);
		if (m.find()) {
			color = edgeStyle.substring(m.start() + 12, m.end() - 1);
		}
		return color;
	}

	public String getColorName() {
		String color = null;
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(nameStyle);
		if (m.find()) {
			color = nameStyle.substring(m.start() + 10, m.end() - 1);
		}
		return color;
	}

	public PathEdge getEdgeLine() {
		return edgeLine;
	}

	public String getEdgeStyle() {
		return edgeStyle;
	}

	public Text getLabelName() {
		return labelName;
	}

	public String getName() {
		return name;
	}

	public String getNameStyle() {
		return nameStyle;
	}

	public SimpleObjectProperty<Graph.OrientationGraph> getOrientationEdgeProperty() {
		return orientationEdgeProperty;
	}

	public double getSizeArrow() {
		return sizeArrow;
	}

	public double getSizeEdge() {
		return sizeEdge;
	}

	public double getSizeName() {
		return sizeName;
	}

	public double getSizeWeight() {
		return sizeWeight;
	}

	public Vertex getVertexFirst() {
		return vertex1;
	}

	public Vertex getVertexSecond() {
		return vertex2;
	}

	public int getWeight() {
		return weight.getValue();
	}

	// ���������� true, ���� ����� ��������� �������
	public boolean isChosedModeOn() {
		return isChosedModeOn;
	}

	public boolean isOrientated() {
		return orientationEdgeProperty.get() == Graph.OrientationGraph.ORIENTED;
	}

	public boolean isShadowOn() {
		return isShadowOn;
	}

	public boolean isShowWeight() {
		return showWeight;
	}

	public boolean isVertexAffiliation(int vertexNum) {
		return vertexNum == vertex1.getNumber() || vertexNum == vertex2.getNumber();
	}

	// vertex affiliation
	public boolean isVertexAffiliation(Vertex vertex) {
		return vertex == vertex1 || vertex == vertex2;
	}

	// ��������� ��������� ����� "�����" �� ��������
	protected void MovingEdgeAfterVertex() {
		setCoordinatesEdge();
	}

	// ������� ����� (�����) �� �����
	public void removeEdge() {
		setChoseModeOff();
		graph.getGraphPane().getChildren().remove(groupEdge);
	}

	public void setArrowStyle(String arrowStyle) {
		if (arrow != null && arrowStyle != null) {
			this.arrowStyle = arrowStyle;
			if (!graph.getChosenGraphElements().contains(this))
				arrow.getPath().setStyle(arrowStyle);
		}
	}

	public void setChoosedArrowStyle(String choosedArrowStyle) {
		if (arrow != null && choosedArrowStyle != null) {
			this.choosedArrowStyle = choosedArrowStyle;
			if (graph.getChosenGraphElements().contains(this))
				arrow.getPath().setStyle(arrowStyle);
		}
	}

	public void setChoosedEdgeStyle(String choosedEdgeStyle) {
		if (choosedEdgeStyle != null && edgeLine != null) {
			this.choosedEdgeStyle = choosedEdgeStyle;
			if (graph.getChosenGraphElements().contains(this))
				edgeLine.setStyle(choosedEdgeStyle);
		}
	}

	// ����� �� ���������
	@Override
	public void setChoseModeOff() {
		isChosedModeOn = false;
		edgeLine.setStyle(edgeStyle); // #FFC618 #DAA520
		labelName.setStyle(nameStyle);
		// TODO
		weight.getRectang().setStyle("-fx-background-color: #ffffff; -fx-padding: 2px 1px 2px 2px; -fx-border-width: "
				+ (sizeEdge * 0.35) + "px; -fx-border-color: " + getColorEdge() + ";");
		weight.textValue.setStyle("-fx-text-fill: " + getColorEdge() + ";");
		if (circleStart != null) groupEdge.getChildren().remove(circleStart);
		if (circleEnd != null) groupEdge.getChildren().remove(circleEnd);
		if (tempLineChangingVertex != null) {
			groupEdge.getChildren().remove(tempLineChangingVertex);
		}
		if (arrow != null) {
			arrow.getPath().setStyle(arrowStyle);
		}
		edgeLine.hideControlsPoints();
		groupEdge.toBack();
		graph.getGraphPane().getTransparencyGraphPane().toBack();
	}

	// ����� ����� ��� ��������� (������� ����� �� �����)
	@Override
	public void setChoseModeOn() {
		isChosedModeOn = true;
		if (circleStart != null) groupEdge.getChildren().remove(circleStart);
		if (circleEnd != null) groupEdge.getChildren().remove(circleEnd);
		edgeLine.setStyle(choosedEdgeStyle);// #FFA500 #4682B4
		weight.getRectang().setStyle("-fx-background-color: #ffffff; -fx-padding: 2px 1px 2px 2px; -fx-border-width: "
				+ (sizeEdge * 0.35) + "px; -fx-border-color: #4682B4;");
		weight.textValue.setStyle("-fx-text-fill: #4682B4");
		circleStart = new Circle(edgeLine.getEdgeStartPoint().getX(), edgeLine.getEdgeStartPoint().getY(), 4);
		circleEnd = new Circle(edgeLine.getEdgeEndPoint().getX(), edgeLine.getEdgeEndPoint().getY(), 4);
		circleStart.setStyle("-fx-fill: RED; -fx-stroke: #000000; -fx-stroke-width: 1px;");
		circleEnd.setStyle("-fx-fill: RED; -fx-stroke: #000000; -fx-stroke-width: 1px;");
		if (arrow != null) {
			arrow.getPath().setStyle(choosedArrowStyle);
		}
		groupEdge.getChildren().addAll(circleStart, circleEnd);
		circleStart.setVisible(true);
		circleEnd.setVisible(true);
		circleStart.toFront();
		circleEnd.toFront();
		groupEdge.toFront();
		edgeLine.showControlsPoints();
		// groupEdge.setBlendMode(BlendMode.EXCLUSION);
		circleStart.setOnMouseDragged(edgeChangeVertexHandler(circleStart));
		circleEnd.setOnMouseDragged(edgeChangeVertexHandler(circleEnd));
		circleStart.setOnMouseReleased(edgeChangeVertexHandlerOnRealease(circleStart));
		circleEnd.setOnMouseReleased(edgeChangeVertexHandlerOnRealease(circleEnd));
	}

	public void setColorArrow(String color) {
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(arrowStyle);
		setArrowStyle(m.replaceAll("-fx-fill: " + color + ";"));
	}

	public void setColorArrowStroke(String color) {
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(arrowStyle);
		setArrowStyle(m.replaceAll("-fx-stroke: " + color + ";"));
	}

	public void setColorEdge(String color) {
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(edgeStyle);
		setEdgeStyle(m.replaceAll("-fx-stroke: " + color + ";"));
	}

	public static void setColorNewEdges(String color) {
		String regex = "-fx-stroke:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(EDGE_STYLE_DEFAULT);
		EDGE_STYLE_DEFAULT = (m.replaceAll("-fx-stroke: " + color + ";"));
	}

	// TODO ������������ ������� (����������������)

	public void setColorName(String color) {
		String regex = "-fx-fill:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(nameStyle);
		setNameStyle(m.replaceAll("-fx-fill: " + color + ";"));
	}

	private ContextMenu contextEdge;
	private CheckMenuItem curveItem;

	private void initiateContextMenu() {
		contextEdge = new ContextMenu();
		MenuItem deleteItem = new MenuItem("Удалить грань");
		deleteItem.setOnAction(event -> graph.removeEdge(name));
		MenuItem settingsItem = new MenuItem("Настройка грани");
		settingsItem.setOnAction(event -> {
			EdgeSettingWindow windowSettings = new EdgeSettingWindow();
			windowSettings.show();
		});
		curveItem = new CheckMenuItem("Преобразовать в кривую");
		if (vertex1 == vertex2)
			curveItem.setDisable(true);
		curveItem.setSelected(edgeLine.isCurve());
		curveItem.selectedProperty().addListener((obs, o, n) -> {
			if (o == edgeLine.isCurve())
				setCurveEdge(n);
		});
		contextEdge.getItems().addAll(deleteItem, settingsItem, curveItem);
	}

	public void setCurveEdge(boolean n) {
		edgeLine.setCurve(n);
		setCoordinatesEdge();
		if (curveItem.isSelected() != n)
			if (!isLoop)
				curveItem.setSelected(n);
	}

	// TEST
	private void setCoordinatesEdge() {
		edgeLine.buildPath();
		arrow.setArrowLineEnd(edgeLine);
		setCoordinatesTempFigureToDefault();
		setCoordinatesName();
		weight.setSetOnLine(edgeLine);
	}

	private void setCoordinatesName() {
		if (!edgeLine.isCurve()) {
			labelName.setLayoutX(vertex1.getCoordinates().midpoint(vertex2.getCoordinates()).getX()
					- ((labelName.getFont().getSize() / 2)) * labelName.getText().length() / 2);
			labelName.setLayoutY(vertex1.getCoordinates().midpoint(vertex2.getCoordinates()).getY()
					- weight.getRectang().getPrefHeight() / (1.5));
		} else {
			Point2D middle = edgeLine.getOnEdgePoint(0.5);
			labelName.setLayoutX(
					middle.getX() - ((labelName.getFont().getSize() / 2)) * labelName.getText().length() / 2);
			labelName.setLayoutY(middle.getY() - weight.getRectang().getPrefHeight() / (1.5));
		}
	}

	/*
	 * ��������� ��������� circleStart, circleEnd � tempLineChangingVertex
	 * ��������������� ����� ��� ������ edgeChangeVertexHandlerOnRealease
	 */
	private void setCoordinatesTempFigureToDefault() {
		if (circleStart != null || circleEnd != null) {
			circleStart.setCenterX(edgeLine.getEdgeStartPoint().getX());
			circleStart.setCenterY(edgeLine.getEdgeStartPoint().getY());
			circleEnd.setCenterX(edgeLine.getEdgeEndPoint().getX());
			circleEnd.setCenterY(edgeLine.getEdgeEndPoint().getY());
		}
		if (tempLineChangingVertex != null) {
			tempLineChangingVertex.setStartX(edgeLine.getEdgeStartPoint().getX());
			tempLineChangingVertex.setStartY(edgeLine.getEdgeStartPoint().getY());
			tempLineChangingVertex.setEndX(edgeLine.getEdgeEndPoint().getX());
			tempLineChangingVertex.setEndY(edgeLine.getEdgeEndPoint().getY());
			tempLineChangingVertex.setVisible(false);
		}
	}

	public void setEdgeStyle(String edgeStyle) {
		if (edgeStyle != null && edgeLine != null) {
			this.edgeStyle = edgeStyle;
			if (!graph.getChosenGraphElements().contains(this))
				edgeLine.setStyle(edgeStyle);
		}
		if (!isChosedModeOn) {
			weight.getRectang().setStyle("-fx-background-color: #ffffff; -fx-padding: 2px 1px 2px 2px; -fx-border-width: "
					+ (sizeEdge * 0.35) + "px; -fx-border-color: " + getColorEdge() + ";");
			weight.textValue.setStyle("-fx-text-fill: " + getColorEdge() + ";");
		}
	}

	public void setName(String name) {
		this.name = name;
		if (labelName != null) {
			labelName.setText(name);
			setCoordinatesName();
		}
	}

	public void setNameStyle(String nameStyle) {
		if (labelName != null && nameStyle != null) {
			this.nameStyle = nameStyle;
			labelName.setStyle(nameStyle);
		}
	}

	public void setOrientationEdge(Graph.OrientationGraph orientationEdge) {
		if (orientationEdge == null)
			return;
		this.orientationEdgeProperty.set(orientationEdge);
		if (orientationEdge == Graph.OrientationGraph.ORIENTED) {
			if (edgeLine != null)
				arrow.setArrowLineEnd(edgeLine);
			if (arrow != null && !groupEdge.getChildren().contains(arrow.getPath())) {
				groupEdge.getChildren().add(arrow.getPath());
			}
		} else {
			if (groupEdge != null) {
				groupEdge.getChildren().remove(arrow.getPath());
			}
		}
		if (graph.getOrientationProperty().get() != orientationEdge) {
			graph.setOrientation(Graph.OrientationGraph.MIXED);
		}
	}

	public void setShowWeight(boolean showWeight) {
		this.showWeight = showWeight;
		weight.getRectang().setVisible(showWeight);
	}

	public void setSizeArrow(double sizeArrow) {
		this.sizeArrow = sizeArrow;
		arrow.setSize(sizeArrow);
	}

	public void setSizeEdge(double sizeEdge) {
		this.sizeEdge = sizeEdge;
		String regex = "-fx-stroke-width:\\s\\d+\\.?\\d*px;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(edgeStyle);
		setEdgeStyle(m.replaceAll("-fx-stroke-width: " + sizeEdge + "px;"));
		m = p.matcher(choosedEdgeStyle);
		setChoosedEdgeStyle(m.replaceAll("-fx-stroke-width: " + (sizeEdge + 1) + "px;"));
	}

	public void setSizeName(double sizeName) {
		this.sizeName = sizeName;
		labelName.setFont(new Font("Calibri bold", sizeName));
		setCoordinatesName();
	}

	public void setSizeWeight(double sizeWeight) {
		this.sizeWeight = sizeWeight;
		weight.getTextValue().setFont(new Font("Calibri Bold", sizeWeight));
		// TODO
	}

	private void settingWeightPosition() {

		weight.textValue.getTf().setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				if (weight.getTextValue().getTf().getText().length() < 1) {
					weight.getTextValue().getTf().setText(Integer.toString(WeightEdge.defaultValue));
				}
				weight.textValue.toLabel();
				weight.setSetOnLine(edgeLine);
			} else if (e.getCode().equals(KeyCode.ESCAPE)) {
				weight.textValue.getTf().setText(weight.textValue.backup);
				weight.textValue.toLabel();
				weight.setSetOnLine(edgeLine);
			}
		});

		weight.textValue.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				weight.textValue.getTf().setText(weight.textValue.backup = weight.textValue.getText());
				weight.textValue.setGraphic(weight.textValue.getTf());
				weight.textValue.setText("");
				weight.textValue.getTf().requestFocus();
				weight.setSetOnLine(edgeLine);
			}
		});
//		weight.getRectang().setLayoutX(vertex1.getCoordinates().midpoint(vertex2.getCoordinates()).getX() - weight.textValue.getBoundsInLocal().getWidth()/2);
//		weight.getRectang().setLayoutY(vertex1.getCoordinates().midpoint(vertex2.getCoordinates()).getY() - weight.textValue.getBoundsInLocal().getHeight()/2);
	}

	public void setVertex1(Vertex vertex) {
		if (vertex != null) {
			this.vertex1 = vertex;
		}
	}

	public void setVertex2(Vertex vertex) {
		if (vertex != null) {
			this.vertex2 = vertex;
		}
	}
	
	/** ������������ ��� ��������� ������������� ����� �� �����.
	 *  �� ������������ ����� setName ��� ���� �����!
	 * 
	 */
	public void setUserName(String name) {
		if (labelName != null) {
			labelName.setText(name);
			setCoordinatesName();
		}
	}

	public void setWeight(int weight) {
		this.weight.getTextValue().setText(Integer.toString(weight));
		this.weight.setValue(weight);
	}

	public void shadowOnText(boolean show) {
		isShadowOn = show;
		String shadow = "-fx-effect: dropshadow(one-pass-box,black,5,0,0,1);";
		String regex = "-fx-effect:\\sdropshadow\\(one-pass-box,black,5,0,0,1\\);";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(nameStyle);
		if (!show) {
			if (m.find()) {
				setNameStyle(m.replaceAll(""));
			}
		} else {
			setNameStyle(getNameStyle() + " " + shadow);
		}
	}

	@Override
    public String toString() {
	    return name;
    }
}
