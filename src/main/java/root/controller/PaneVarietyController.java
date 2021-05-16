package root.controller;

import javafx.collections.MapChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import root.GraphWorkerApplication;
import root.view.LeftPane;
import root.view.ShowMatrixWindow;
import root.model.Edge;
import root.model.MatrixGraph;
import root.model.Vertex;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PaneVarietyController implements ActionableComponents {

    private LeftPane.PaneVariety paneVariety;

    public PaneVarietyController(LeftPane.PaneVariety paneVariety) {
        this.paneVariety = paneVariety;
    }

    @Override
    public void buildActionsComponents() {
        buildSpinnerQuanityAction();
        buildTextAreaEdgesAction();
        buildMenuButtonMatrixsActions();
        buildTextAreaOutputMatrixActions();
    }

    private void buildSpinnerQuanityAction() {
        paneVariety.getSpinnerQuanityVertexs().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                newValue = 0;
                paneVariety.getGraph().buildVertexes(newValue);
                paneVariety.getSpinnerQuanityVertexs().getValueFactory().setValue(0);
                return;
            }
            if (oldValue != null && !oldValue.equals(newValue)) {
                if (newValue > 50 || newValue < 0) {
                    paneVariety.getSpinnerQuanityVertexs().getValueFactory().setValue(oldValue);
                    return;
                }
                paneVariety.getGraph().buildVertexes(newValue);
            }
        });

        paneVariety.getGraph().getVertexes().addListener((MapChangeListener<Integer, Vertex>) change -> {
            if (change.wasAdded()) {
                paneVariety.getSpinnerQuanityVertexs().getValueFactory().setValue(paneVariety.getGraph().getVertexes().size());
            }
            if (change.wasRemoved()) {
                paneVariety.getSpinnerQuanityVertexs().getValueFactory().setValue(paneVariety.getGraph().getVertexes().size());
            }
        });
    }

    private void buildTextAreaEdgesAction() {
        MapChangeListener<Integer, Vertex> onVertexsRemoved = (ch) -> {
            if (ch.wasRemoved()) {
                rewriteEdgesInTextAreaEdges(ch.getValueRemoved().getNumber());
            }
        };
        MapChangeListener<String, Edge> onEdgeAdd = (ch) -> {
            if (ch.wasAdded()) {
                rewriteTextAreaEdges(ch.getValueAdded().getVertexFirst().getNumber(),
                        ch.getValueAdded().getVertexSecond().getNumber());
            }
        };
        MapChangeListener<String, Edge> onEdgeRemoved = (ch) -> {
            if (ch.wasRemoved()) {
                deleteFromTextAreaEdges(ch.getValueRemoved().getVertexFirst().getNumber(),
                        ch.getValueRemoved().getVertexSecond().getNumber());
            }
        };
        paneVariety.getGraph().getVertexes().addListener(onVertexsRemoved);
        paneVariety.getGraph().getEdges().addListener(onEdgeAdd);
        paneVariety.getGraph().getEdges().addListener(onEdgeRemoved);

        paneVariety.getTaEdges().textProperty().addListener((observable, oldValue, newValue) -> {
            if (( paneVariety.getGraph().isHasCorrectFormatEdges(newValue) || newValue.equals(""))
                    &&  paneVariety.getGraph().isVertexesContains(newValue)) {
                paneVariety.getGraph().getEdges().removeListener(onEdgeAdd);
                paneVariety.getGraph().buildEdges(newValue);
                paneVariety.getGraph().getEdges().addListener(onEdgeAdd);
                paneVariety.getTaEdges().setStyle(paneVariety.getDefaulStyleTextAreaEdges());
                paneVariety.getLabelIconEdge().setGraphic(paneVariety.getViewOkIconEdge());
                paneVariety.getLabelWarningNoVertexs().setVisible(false);
                paneVariety.getTaEdges().setFocusColor(Paint.valueOf("#006FFF"));
            } else {
                if ( paneVariety.getGraph().isHasCorrectFormatEdges(newValue)
                        && ! paneVariety.getGraph().isVertexesContains(newValue)) {
                    paneVariety.getLabelWarningNoVertexs().setText("Введенной грани нет в графе");
                    paneVariety.getLabelWarningNoVertexs().setVisible(true);
                    paneVariety.getTaEdges().setFocusColor(Color.RED);
                } else {
                    if (! paneVariety.getGraph().isHasCorrectFormatEdges(newValue)
                            && paneVariety.getGraph().isVertexesContains(newValue)) {
                        paneVariety.getLabelWarningNoVertexs().setText("Ошибка в формате ввода!");
                        paneVariety.getLabelWarningNoVertexs().setVisible(true);
                        paneVariety.getTaEdges().setFocusColor(Color.RED);
                    }
                    // labelWarningNoVertexs.setVisible(false);
                }
                // taEdges.setStyle(
                // "-fx-background-color:#ff0000; -fx-text-fill: #ff0000;
                // -fx-highlight-text-fill: #ff9999; -fx-highlight-fill: #ff0000");
                // -fx-text-fill: #00ff00;
                paneVariety.getLabelIconEdge().setGraphic(paneVariety.getViewWarningIconEdge());

            }
        });
        paneVariety.getButtonToComplete().setOnAction(value ->  paneVariety.getGraph().toCompleteGraph());
    }

    private void buildMenuButtonMatrixsActions() {
        paneVariety.getMbMatrixs().valueProperty().addListener((obs, o, n) -> {
            if (n == paneVariety.getItemInc()) {
                paneVariety.getGraph().matrix.typeMatrix = MatrixGraph.TypeMatrix.INCIDENCE;
                paneVariety.getTaOutputMatrix().setText(paneVariety.getGraph().matrix.buildMatrix());
                paneVariety.getListView().getItems().setAll(paneVariety.getGraph().matrix.getListVertexsPosition());
            }
            if  (n == paneVariety.getItemAdjacency()) {
                paneVariety.getGraph().matrix.typeMatrix = MatrixGraph.TypeMatrix.ADJACENCY;
                paneVariety.getTaOutputMatrix().setText(paneVariety.getGraph().matrix.buildMatrix());
                paneVariety.getListView().getItems().setAll(paneVariety.getGraph().matrix.getListVertexsPosition());
            }
        });
        paneVariety.getMbMatrixs().setValue(paneVariety.getItemInc());
    }

    private void buildTextAreaOutputMatrixActions() {
        MapChangeListener<Integer, Vertex> vertexsListner = change -> {
            paneVariety.getTaOutputMatrix().setText( paneVariety.getGraph().matrix.buildMatrix());
            paneVariety.getListView().getItems().setAll( paneVariety.getGraph().matrix.getListVertexsPosition());
        };
        paneVariety.getGraph().getVertexes().addListener(vertexsListner);
        MapChangeListener<String, Edge> edgesListener = change -> {
            paneVariety.getTaOutputMatrix().setText( paneVariety.getGraph().matrix.buildMatrix());
            paneVariety.getListView().getItems().setAll( paneVariety.getGraph().matrix.getListVertexsPosition());
        };
        paneVariety.getGraph().getEdges().addListener(edgesListener);
        paneVariety.getGraph().getOrientationProperty().addListener((obs, o, n) -> {
            paneVariety.getTaOutputMatrix().setText( paneVariety.getGraph().matrix.buildMatrix());
            paneVariety.getListView().getItems().setAll( paneVariety.getGraph().matrix.getListVertexsPosition());
        });
        paneVariety.getButtonShowMatrix().setOnAction(value -> {
            ShowMatrixWindow.getInstance( paneVariety.getGraph().matrix).show();
            ShowMatrixWindow.getInstance( paneVariety.getGraph().matrix).showMatrix();
        });
        paneVariety.getButtonSaveMatrix().setOnAction(value -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            Date dateNow = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd_MM");
            fileChooser.setInitialFileName("GraphMatrix" + formatForDateNow.format(dateNow));
            fileChooser.setTitle("Выберите директорию");
            // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(GraphWorkerApplication.getInstance().getPrimaryStage());
            if (file != null) {
                try {
                    PrintWriter writer;
                    writer = new PrintWriter(file);
                    writer.println( paneVariety.getGraph().matrix.buildMatrix());
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(LeftPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void rewriteEdgesInTextAreaEdges(int numberVertex) {
        // ArrayList<String> regexs = new ArrayList<String>();
        String oldStroke = paneVariety.getTaEdges().getText();
        // for (Map.Entry<String, root.model.Edge> entry : graph.getEdges().entrySet()) {
        // if (entry.getValue().getVertexFirst().getNumber() == numberVertex ||
        // entry.getValue().getVertexSecond().getNumber() == numberVertex) {
        // int vertex1 = entry.getValue().getVertexFirst().getNumber();
        // int vertex2 = entry.getValue().getVertexSecond().getNumber();
        String regex1 = "\n*\\s*" + numberVertex + "((\n+)|(\\s+))\\d+\n*\\s*([;,])";
        String regex2 = "\n*\\s*\\d+((\n+)|(\\s+))" + numberVertex + "\n*\\s*([;,])";
        String newStroke = Pattern.compile(regex1).matcher(oldStroke).replaceAll("");
        newStroke = Pattern.compile(regex2).matcher(newStroke).replaceAll("");
        // regexs.add(newStroke);
        // textAreaEdges.setText(Pattern.compile(regex).matcher(oldStroke).replaceAll(newVertex1+"
        // "+newVertex2));
        // Pattern.compile("((\n*\\s*\n*\\d{1,2}\n*\\s+\n*\\d{1,2}\n*\\s*\n*(,|;){1}\n*\\s*\n*)+)");
        // }
        // }
        paneVariety.getTaEdges().setText(newStroke);
    }

    private void rewriteTextAreaEdges(int numberVertex1, int numberVertex2) {
        // System.out.println("WORK!");
        String newEdge = numberVertex1 + " " + numberVertex2 + ";";
        String oldStroke = paneVariety.getTaEdges().getText();
        if (Pattern.compile("\\s*").matcher(oldStroke).matches()) {
            paneVariety.getTaEdges().setText(paneVariety.getTaEdges().getText() + newEdge);
            return;
        }
        if (oldStroke.lastIndexOf('\n') != -1) {
            if (oldStroke.substring(oldStroke.lastIndexOf('\n')).length() >= 28) {
                newEdge = "\n" + newEdge;
            } else {
                newEdge = " " + newEdge;
            }
            paneVariety.getTaEdges().setText(paneVariety.getTaEdges().getText() + newEdge);
            return;
        }
        if (oldStroke.length() >= 28) {
            newEdge = "\n" + newEdge;
        } else {
            newEdge = " " + newEdge;
        }
        paneVariety.getTaEdges().setText(paneVariety.getTaEdges().getText() + newEdge);
    }

    private void deleteFromTextAreaEdges(int oldVertex1, int oldVertex2) {
        String regex = "\n*\\s*" + oldVertex1 + "\n*((\n+)|(\\s+))\n*" + oldVertex2 + "\n*\\s*([;,])";
        String oldStroke = paneVariety.getTaEdges().getText();
        paneVariety.getTaEdges().setText(Pattern.compile(regex).matcher(oldStroke).replaceAll(""));
        // Pattern.compile("((\n*\\s*\n*\\d{1,2}\n*\\s+\n*\\d{1,2}\n*\\s*\n*(,|;){1}\n*\\s*\n*)+)");
    }

}
