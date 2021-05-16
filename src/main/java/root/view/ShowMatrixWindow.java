package root.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import root.GraphWorkerApplication;
import root.model.MatrixGraph;

public class ShowMatrixWindow extends Stage {
	private static ShowMatrixWindow instance;

	public static ShowMatrixWindow getInstance(MatrixGraph matrix) {
		if (instance == null)
			instance = new ShowMatrixWindow(matrix);
		return instance;
	}
	
	private MatrixGraph matrix;
	private ScrollPane root;
	private BorderPane borderPane;
	private ListView<String> listView;
	private TextArea taOutputMatrix;
	
	private ShowMatrixWindow(MatrixGraph matrix) {
		super();
		this.matrix = matrix;
		initiate();
	}
	
	private void initiate() {
		createRootPane();
		Scene scene = new Scene(root, 500, 400);
		String css = getClass().getResource("/application.css").toExternalForm();
		scene.getStylesheets().add(css);
		setScene(scene);
		setTitle("Матрица");
		setMinHeight(400);
		setMinWidth(500);
		sizeToScene();
		centerOnScreen();
		initStyle(StageStyle.UTILITY);
		initOwner(GraphWorkerApplication.getInstance().getPrimaryStage());
		setResizable(true);
	}

	private void createRootPane() {
		root = new ScrollPane();
		root.setPrefHeight(400);
		root.setPrefWidth(500);
		root.setPadding(new Insets(10));
		root.setHbarPolicy(ScrollBarPolicy.NEVER);
		root.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		initiateComponents();
		buildActionsComponents();
		root.prefWidthProperty().bind(this.widthProperty());
		borderPane.prefWidthProperty().bind(root.widthProperty().add(-20));
		borderPane.prefHeightProperty().bind(root.heightProperty().add(-20));
	}
	
	public void showMatrix() {
		taOutputMatrix.setText(this.matrix.buildMatrix());
		listView.getItems().setAll(this.matrix.getListVertexsPosition());
	}

	private void buildActionsComponents() {
		// TODO Auto-generated method stub
		
	}

	private void initiateComponents() {
		borderPane = new BorderPane();
		//borderPane.setPrefSize(465, 470);
		root.setContent(borderPane);
		listView = new ListView<String>();
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
		borderPane.setCenter(taOutputMatrix);
	}
}
