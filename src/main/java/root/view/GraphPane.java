package root.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import root.model.Graph;

public class GraphPane extends AnchorPane {

	public enum Orientation {
		PORTRAIT, ALBUM;
	}

	public Pane getTransparencyGraphPane() {
		return transparencyGraphPane;
	}

	private Pane transparencyGraphPane = new Pane();
	//private Text helpNote;
	public boolean isChoosed = false;
	private double width = 793;
	private double height = 1122;
	private Orientation orientation = Orientation.PORTRAIT;
	private String stylePane = "-fx-background-color: #FFFFFF;";

	public HelpNote getHelpNote() {
		return helpNote;
	}

	public void setHelpNote(HelpNote helpNote) {
		this.helpNote = helpNote;
	}

	private HelpNote helpNote;

	public GraphPane() {
		super();
		setLayoutX(-27);
		setLayoutY(15);
		setPrefWidth(width);
		setPrefHeight(height);
		setMaxWidth(width);
		setMaxHeight(height);
		setMinWidth(width);
		setMinHeight(height);
		setStylePane(stylePane);
		// TODO method binding transparency pane
		transparencyGraphPane.setPrefSize(getPrefWidth(), getPrefHeight());
		transparencyGraphPane.setStyle("-fx-background-color: transparent");
		shadowOn(true);
		// Base distribution circles
		// TODO �������
		Circle circle1 = new Circle(342.5, 304, 50);
		circle1.setSmooth(true);
		circle1.setFill(Paint.valueOf("#1e90ff00"));
		circle1.setStroke(Paint.valueOf("#000000"));
		circle1.setStrokeWidth(1);
		//getChildren().add(circle1);
		Circle circle2 = new Circle(342.5, 304, 100);
		circle2.setSmooth(true);
		circle2.setFill(Paint.valueOf("#1e90ff00"));
		circle2.setStroke(Paint.valueOf("#000000"));
		circle2.setStrokeWidth(1);
		//getChildren().add(circle2);
		Circle circle3 = new Circle(342.5, 304, 150);
		circle3.setSmooth(true);
		circle3.setFill(Paint.valueOf("#1e90ff00"));
		circle3.setStroke(Paint.valueOf("#000000"));
		circle3.setStrokeWidth(1);
		//getChildren().add(circle3);
		Circle circle4 = new Circle(342.5, 304, 200);
		circle4.setSmooth(true);
		circle4.setFill(Paint.valueOf("#1e90ff00"));
		circle4.setStroke(Paint.valueOf("#000000"));
		circle4.setStrokeWidth(1);
		//getChildren().add(circle4);
		Circle circle5 = new Circle(342.5, 304, 250);
		circle5.setSmooth(true);
		circle5.setFill(Paint.valueOf("#1e90ff00"));
		circle5.setStroke(Paint.valueOf("#000000"));
		circle5.setStrokeWidth(1);
		//getChildren().add(circle5);
		getChildren().add(transparencyGraphPane);
		initiateComponents();
	}
	
	public void setPivot(double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }

	public void setSizeCanvas(double width, double height) {
		this.width = width;
		this.height = height;
		setPrefWidth(width);
		setPrefHeight(height);
		setMaxWidth(width);
		setMaxHeight(height);
		setMinWidth(width);
		setMinHeight(height);
		transparencyGraphPane.setPrefSize(getPrefWidth(), getPrefHeight());
		//setMinSize(width, height);
		//setMaxSize(width, height);
	}
	
	public void setWidthCanvas(double width) {
		this.width = width;
		setPrefWidth(width);
		setMaxWidth(width);
		setMinWidth(width);
		transparencyGraphPane.setPrefSize(getPrefWidth(), getPrefHeight());
	}
	
	public void setHeightCanvas(double height) {
		this.height = height;
		setPrefHeight(height);
		setMaxHeight(height);
		setMinHeight(height);
		transparencyGraphPane.setPrefSize(getPrefWidth(), getPrefHeight());
	}

	private void initiateComponents() {
//		helpNote = new Text("����������� ���� � ������ ������������, ����� ��������� ������� � �����");
//		helpNote.setLayoutY(15);
//		helpNote.disableProperty().set(true);
//		helpNote.setOpacity(0.6);
//		//getChildren().add(helpNote);
//		helpNote.setLayoutX(getPrefWidth() / 2 - helpNote.getBoundsInLocal().getWidth() / 2);
	}

//	public Text getHelpNote() {
//		return helpNote;
//	}

	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		if (orientation == Orientation.PORTRAIT) {
			setPrefWidth(width);
			setPrefHeight(height);
			setMaxWidth(width);
			setMaxHeight(height);
			setMinWidth(width);
			setMinHeight(height);
			transparencyGraphPane.setPrefSize(getPrefWidth(), getPrefHeight());
		} else if (orientation == Orientation.ALBUM) {
			setPrefWidth(height);
			setPrefHeight(width);
			setMaxWidth(height);
			setMaxHeight(width);
			setMinWidth(height);
			setMinHeight(width);
			transparencyGraphPane.setPrefSize(getPrefWidth(), getPrefHeight());

		}
	}
	
	public void setColor(String color) {
		String regex = "-fx-background-color:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(getStylePane());
		setStylePane(m.replaceAll("-fx-background-color: " + color + ";"));
	}
	
	public String getColor() {
		String color = "";
		String regex = "-fx-background-color:\\s[^-]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(getStylePane());
		if (m.find()) {
			color = getStylePane().substring(m.start() + 22, m.end() - 1);
		}
		// System.out.println("FILL COLOR TEXT " + color);
		return color;
	}

	public String getStylePane() {
		return stylePane;
	}

	public void setStylePane(String stylePane) {
		this.stylePane = stylePane;
		setStyle(stylePane);
	}

	public double getWidthPane() {
		return width;
	}

	public double getHeightPane() {
		return height;
	}
	
	private DropShadow shadowEffect;
	public void shadowOn(boolean n) {
		if (n == true && shadowEffect == null) {
			shadowEffect = new DropShadow();
			shadowEffect.blurTypeProperty().set(BlurType.THREE_PASS_BOX);
			shadowEffect.setHeight(5.14);
			shadowEffect.setRadius(2);
			shadowEffect.setSpread(0.1);
			shadowEffect.setWidth(5.14);
			shadowEffect.setOffsetX(1);
			shadowEffect.setOffsetY(1);
			setEffect(shadowEffect);
		} else if (n == false && shadowEffect != null) {
			shadowEffect = null;
			setEffect(null);
		}
	}
}
