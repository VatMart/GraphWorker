package root.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import root.GraphWorkerApplication;
import root.components.GraphExporter;
import root.components.GraphLoader;
import root.components.SavedGraph;
import root.view.CanvasSettingWindow;
import root.view.LeftPane;
import root.view.MainPane;
import root.model.Edge;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPaneController implements ActionableComponents {
    private MainPane mainPane;

    public MainPaneController(MainPane mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public void buildActionsComponents() {
        mainPane.getTbHideLeftPane().selectedProperty().addListener((obs, o, n) -> {
            if (n) {
                mainPane.getLeftPane().setVisible(false);
                AnchorPane.setLeftAnchor(mainPane.getTbHideLeftPane(), 0.);
                mainPane.getGraph().getGraphPane().setLayoutX(mainPane.getGraph().getGraphPane().getLayoutX() + 245);
                AnchorPane.setLeftAnchor(mainPane.getDeskPane(), 5.);
                mainPane.getSpMenuBar().setStyle("-fx-background-color: #F5F5F5;");
            } else {
                mainPane.getLeftPane().setVisible(true);
                AnchorPane.setLeftAnchor(mainPane.getTbHideLeftPane(), 245.);
                mainPane.getGraph().getGraphPane().setLayoutX(mainPane.getGraph().getGraphPane().getLayoutX() - 245);
                AnchorPane.setLeftAnchor(mainPane.getDeskPane(), 250.);
                mainPane.getSpMenuBar().setStyle("-fx-background-color: rgba(0,0,0,0.7);"); // #3366CC
            }
        });
        buildMenuBarComponentsActions();
    }
    public static SimpleObjectProperty<File> sFileSave = new SimpleObjectProperty<File>(null);

    private void buildMenuBarComponentsActions() {
        mainPane.getToCurveAll().selectedProperty().addListener((obs, o, n) -> {
            for (Map.Entry<String, Edge> entry : mainPane.getGraph().getEdges().entrySet()) {
                entry.getValue().setCurveEdge(n);
            }
            Edge.IS_CURVE_NEW_EDGES = n;
        });

        mainPane.getCanvasSettings().setOnAction(value -> {
            CanvasSettingWindow.getInstance(mainPane.getGraph()).show();
            ;
        });

        mainPane.getSaveAsFile().setOnAction(value -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GRAPHW files (*.graph_w)",
                    "*.graph_w");
            fileChooser.getExtensionFilters().add(extFilter);
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd_MM");
            fileChooser.setInitialFileName("Graph" + formatForDateNow.format(dateNow));
            fileChooser.setTitle("Выберите директорю");
            // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            sFileSave.set(fileChooser.showSaveDialog(GraphWorkerApplication.getInstance().getPrimaryStage()));
            if (sFileSave.get() != null) {
                try {
                    SavedGraph savedGraph = new SavedGraph(mainPane.getGraph());
                    FileOutputStream fileOutputStream = new FileOutputStream(sFileSave.get());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(savedGraph);
                    objectOutputStream.close();
                } catch (IOException ex) {
                    System.out.println("DDDDD");
                    Logger.getLogger(LeftPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        mainPane.getSaveFile().setOnAction(value -> {
            if (sFileSave.get() != null) {
                try {
                    SavedGraph savedGraph = new SavedGraph(mainPane.getGraph());
                    FileOutputStream fileOutputStream = new FileOutputStream(sFileSave.get());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(savedGraph);
                    objectOutputStream.close();
                } catch (IOException ex) {
                    System.out.println("DDDDD");
                    Logger.getLogger(LeftPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else mainPane.getSaveAsFile().fire();
        });

        mainPane.getOpenFile().setOnAction(value -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GRAPHW files (*.graph_w)",
                    "*.graph_w");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Выберите файл");
            // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(GraphWorkerApplication.getInstance().getPrimaryStage());
            if (file != null) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    SavedGraph savedGraph = (SavedGraph) objectInputStream.readObject();
                    objectInputStream.close();
                    GraphLoader graphLoader = new GraphLoader(savedGraph, mainPane);
                    graphLoader.loadGraph();
                    sFileSave.set(file);
                    //System.out.println(savedGame);
                } catch (IOException ex) {
                    System.out.println("DDDDD");
                    Logger.getLogger(LeftPane.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException e) {
                    //
                    e.printStackTrace();
                }
            }
        });

        mainPane.getExportPNG().setOnAction(value -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)",
                    "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd_MM");
            fileChooser.setInitialFileName("GraphPNG" + formatForDateNow.format(dateNow));
            fileChooser.setTitle("Выберите директорю");
            // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(GraphWorkerApplication.getInstance().getPrimaryStage());
            if (file != null) {
                GraphExporter graphExporter = new GraphExporter(mainPane.getGraph());
                graphExporter.exportPNG(file);
            }
        });

        mainPane.getExportJPG().setOnAction(value -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)",
                    "*.jpg");
            fileChooser.getExtensionFilters().add(extFilter);
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd_MM");
            fileChooser.setInitialFileName("GraphJPG" + formatForDateNow.format(dateNow));
            fileChooser.setTitle("Выберите директорю");
            // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(GraphWorkerApplication.getInstance().getPrimaryStage());
            if (file != null) {
                GraphExporter graphExporter = new GraphExporter(mainPane.getGraph());
                graphExporter.exportJPG(file);
            }
        });
    }
}
