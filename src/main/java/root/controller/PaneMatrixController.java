package root.controller;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import root.GraphWorkerApplication;
import root.view.LeftPane;
import root.view.MainPane;
import root.model.MatrixGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PaneMatrixController implements ActionableComponents {

    private LeftPane.PaneMatrix paneMatrix;

    public PaneMatrixController(LeftPane.PaneMatrix paneMatrix) {
        this.paneMatrix = paneMatrix;
    }

    @Override
    public void buildActionsComponents() {
        buildMenuButtonMatrixInputActions();
        buildTextAreaMatrixInputAction();
        buildButtonBuildGraphAction();
    }

    private void buildMenuButtonMatrixInputActions() {
        paneMatrix.getMbMatrixInput().valueProperty().addListener((obs, o, n) -> {
            if (n == paneMatrix.getItemIncInput()) {
                paneMatrix.getGraph().setTypeMatrix(MatrixGraph.TypeMatrix.INCIDENCE);
            }
            if  (n == paneMatrix.getItemAdjacencyInput()) {
                paneMatrix.getGraph().setTypeMatrix(MatrixGraph.TypeMatrix.ADJACENCY);
            }
        });
        paneMatrix.getMbMatrixInput().setValue(paneMatrix.getItemIncInput());
        paneMatrix.getButtonLoadMatrix().setOnAction(value -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Выберите директорию для загрузки матрицы");
            // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showOpenDialog(GraphWorkerApplication.getInstance().getPrimaryStage());
            if (file != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    String ls = System.getProperty("line.separator");
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                        stringBuilder.append(ls);
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    if (MatrixGraph.isMatrix(stringBuilder.toString(), paneMatrix.getGraph().getTypeMatrix())) {
                        paneMatrix.getTaMatrixInput().setText(stringBuilder.toString());
                    } else {
                        MainPane.showAlertMessage(
                                "Ошибка! \nФайл не содержит матрицу или тип матрицы в файле отличается от выбранного!");
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }

    private void buildTextAreaMatrixInputAction() {
        paneMatrix.getTaMatrixInput().textProperty().addListener((obs, o, n) -> {
            if (MatrixGraph.isMatrix(n, paneMatrix.getGraph().getTypeMatrix())) {
                paneMatrix.getLabelIconMatrix().setGraphic(paneMatrix.getViewOkIconMatrix());
                paneMatrix.getTaMatrixInput().setFocusColor(Paint.valueOf("#006FFF"));
            } else {
                paneMatrix.getLabelIconMatrix().setGraphic(paneMatrix.getViewWarningIconMatrix());
                paneMatrix.getTaMatrixInput().setFocusColor(Color.RED);
            }
        });
    }

    private void buildButtonBuildGraphAction() {
        paneMatrix.getButtonBuildGraph().setOnAction(event -> {
            String text = paneMatrix.getTaMatrixInput().getText();
            if (MatrixGraph.isMatrix(text, paneMatrix.getGraph().getTypeMatrix())) {
                // System.out.println(typeMatrix);
                paneMatrix.getGraph().buildGraphFromMatrix(new MatrixGraph(paneMatrix.getGraph(), paneMatrix.getGraph().getTypeMatrix(), text));
            } else
                MainPane.showAlertMessage("Ошибка в вводе матрицы");
        });
    }
}
