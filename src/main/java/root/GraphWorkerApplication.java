package root;

import com.jfoenix.assets.JFoenixResources;
import root.controller.ControllerManager;
import root.model.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import root.view.GraphPane;
import root.view.ViewManager;

public class GraphWorkerApplication extends Application {
	private static GraphWorkerApplication instance;

	public static GraphWorkerApplication getInstance() {
		if (instance == null)
			instance = new GraphWorkerApplication();
		return instance;
	}

	private Stage primaryStage;
	private Scene scene;
	private ViewManager viewManager;
	private ControllerManager controllerManager;
	//private MainPane mainPane;
	//private JFXDecorator decorator;

	public Stage getPrimaryStage() {
		return instance.primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	//Temporary
	private Graph graph;

	@Override
	public void start(Stage stage) {
		long start = System.currentTimeMillis();
		long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		instance = this;
		instance.primaryStage = stage;

		instance.graph = new Graph();
		instance.graph.setGraphPane(new GraphPane());

		instance.viewManager = new ViewManager(instance.graph);
		instance.controllerManager = new ControllerManager(instance.graph,instance.viewManager);
		instance.controllerManager.setActions();



		//instance.mainPane = new MainPane(instance.graph);
		
//		instance.decorator = new JFXDecorator(stage, instance.mainPane);
//		instance.decorator.setCustomMaximize(true);
//		instance.decorator.setGraphic(new SVGGlyph("", Paint.valueOf("#006FFF")));
		
		instance.scene = new Scene(instance.viewManager.getMainPane(), 977, 700);
		String css = getClass().getResource("/application.css").toExternalForm();
		instance.scene.getStylesheets().addAll(JFoenixResources.load("/GraphWorkerItems/css/jfoenix-fonts.css").toExternalForm(), css);
		stage.setScene(instance.scene);
		//setFileListener();

		stage.getIcons().add(new Image("/GraphWorkerItems/iconBlackStage.png"));
		stage.setTitle("GraphWorker");
		stage.setMinWidth(instance.scene.getWidth());
		stage.setMinHeight(instance.scene.getHeight());
		stage.centerOnScreen();
		stage.show();
		instance.viewManager.getMainPane().getDeskPane().centreGraphPane();
		long finish = System.currentTimeMillis();
		long timeConsumedMillis = finish - start;
		long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Initiate time: " + timeConsumedMillis);
		System.out.println("Initiate memory uses: " + ((after - before)/1048576) + " Mb");
	}
	
	public void loadGraph(root.components.GraphLoader graphLoader) {
//		primaryStage.close();
		instance.viewManager = new ViewManager(graphLoader.getLoadedGraph());
		instance.controllerManager = new ControllerManager(instance.graph,instance.viewManager);
		instance.controllerManager.setActions();
		//instance.decorator.setContent(instance.mainPane);
		//instance.scene.setRoot(instance.decorator);
		instance.scene = new Scene(instance.viewManager.getMainPane(), 977, 700);
		String css = getClass().getResource("/application.css").toExternalForm();
		instance.scene.getStylesheets().addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(), css);
		instance.primaryStage.setScene(instance.scene);
//		primaryStage.show();
	}
	
//	private void setFileListener() {
//		MainPane.sFileSave.addListener((obs, o, n) -> {
//			if (n == null) {
//				instance.primaryStage.setTitle("GraphWorker" + " - *NewGraph");
//			} else {
//				instance.primaryStage.setTitle("GraphWorker" + " - " +n.getName());
//			}
//		});
//	}
}
