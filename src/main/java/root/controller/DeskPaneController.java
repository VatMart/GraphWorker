package root.controller;

import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import root.GraphWorkerApplication;
import root.view.CanvasSettingWindow;
import root.view.DeskPane;
import root.model.Vertex;

import java.awt.*;
import java.util.Map;

public class DeskPaneController implements ActionableComponents {
    private DeskPane deskPane;

    public DeskPaneController(DeskPane deskPane) {
        this.deskPane = deskPane;
    }

    private double x = 0;
    private double y = 0;
    private double xG = 0;
    private double yG = 0;
    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = .1d;
    private boolean choosing;

    @Override
    public void buildActionsComponents() {
        buildActionsGraphPane();

        deskPane.setOnMousePressed(ev -> {
            x = ev.getX();
            y = ev.getY();
            if (ev.isMiddleButtonDown() || ev.isSecondaryButtonDown()) {
                xG = deskPane.getGraphPane().getLayoutX();
                yG = deskPane.getGraphPane().getLayoutY();
            }
            if (deskPane.getGraph().isMultipleChoosingOn()) {
                for (Map.Entry<Integer, Vertex> entry : deskPane.getGraph().getVertexes().entrySet()) {
                    if (entry.getValue().getVertexCircle().localToScene(entry.getValue().getVertexCircle().getBoundsInLocal()).contains(deskPane.localToScene(new Point2D(x,y)))) return;
                }
                choosing = true;
                //rectangle.chooserOn();
            }
        });
        deskPane.setOnMouseDragged(ev -> {
            if (!choosing  &&  (ev.isMiddleButtonDown() || ev.isSecondaryButtonDown())) {
                Point mouse = MouseInfo.getPointerInfo().getLocation();
                Point2D local = deskPane.screenToLocal(mouse.x, mouse.y);
                if (local.getX() > x) {
                    deskPane.getGraphPane().setLayoutX(xG - (x - local.getX()));
                } else
                    deskPane.getGraphPane().setLayoutX(xG + (local.getX() - x));
                if (local.getY() > y) {
                    deskPane.getGraphPane().setLayoutY(yG - (y - local.getY()));
                } else
                    deskPane.getGraphPane().setLayoutY(yG + (local.getY() - y));
                return;
            }
            if (choosing && ev.isPrimaryButtonDown()) {
                resizet(x, y, ev.getX(), ev.getY());
            }
        });
        deskPane.setOnMouseReleased(event -> {
            chooserOff();
            choosing = false;
        });
        // getChildren().add(group);
        deskPane.setOnScroll(event -> {
            double delta = 1.2;
            double scale = deskPane.getGraphPane().getScaleY(); // currently we only use Y, same value is used for X
            double oldScale = scale;
            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;
            scale = clamp(scale, MIN_SCALE, MAX_SCALE);
            double f = (scale / oldScale) - 1;
            double dx = (event.getX()
                    - (deskPane.getGraphPane().getBoundsInParent().getWidth() / 2 + deskPane.getGraphPane().getBoundsInParent().getMinX()));
            double dy = (event.getY()
                    - (deskPane.getGraphPane().getBoundsInParent().getHeight() / 2 + deskPane.getGraphPane().getBoundsInParent().getMinY()));
            deskPane.getGraphPane().setScaleX(scale);
            deskPane.getGraphPane().setScaleY(scale);
            deskPane.getMainPane().getToolBar().getMbScale().setText(Integer.toString((int) (deskPane.getGraphPane().getScaleX() * 100)) + "%");
            deskPane.getGraphPane().setLayoutX((deskPane.getGraphPane().getLayoutX() - f * dx));
            deskPane.getGraphPane().setLayoutY((deskPane.getGraphPane().getLayoutY() - f * dy));
            event.consume();
        });
        deskPane.getCentreButton().setOnAction(ev -> {
            centreGraphPane();
            deskPane.getGraphPane().setScaleX(1.);
            deskPane.getGraphPane().setScaleY(1.);
            deskPane.getMainPane().getToolBar().getMbScale().setText("100%");
        });
        choosing();
    }

    public static double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0)
            return min;
        if (Double.compare(value, max) > 0)
            return max;
        return value;
    }

    public void centreGraphPane() {
        double deskCentreX = GraphWorkerApplication.getInstance().getPrimaryStage().getWidth() / 2
                - (deskPane.getMainPane().getTbHideLeftPane().isSelected() ? (0) : 125);
        deskPane.getGraphPane().setLayoutX((deskCentreX - deskPane.getGraphPane().getBoundsInLocal().getWidth() / 2));
        deskPane.getGraphPane().setLayoutY(15);
    }
    private void buildActionsGraphPane() {
        deskPane.getMainPane().setOnKeyPressed(val -> {
            if (val.getCode() == KeyCode.CONTROL) {
                deskPane.getGraph().multipleChoosingOn(true);
            }
        });
        deskPane.getMainPane().setOnKeyReleased(val -> {
            if (val.getCode() == KeyCode.CONTROL) {
                deskPane.getGraph().multipleChoosingOn(false);
            }
            if (val.getCode() == KeyCode.DELETE) {
                deskPane.getGraph().deleteChoosesElements();
            }
            if (val.getCode() == KeyCode.A) {
                deskPane.getGraph().choseAllElements();
            }
        });
        //root.components.MultipleMovingGraphElements multipleMovingGraphElements = new root.components.MultipleMovingGraphElements(graph);
        deskPane.getGraphPane().setOnMouseMoved(event -> {
            deskPane.getMainPane().getToolBar().getTextXMouth().setText(Integer.toString((int) event.getX()));
            deskPane.getMainPane().getToolBar().getTextYMouth().setText(Integer.toString((int) event.getY()));
        });
        deskPane.getGraphPane().getTransparencyGraphPane().setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                deskPane.getGraph().choseObject(null);
                deskPane.getVertexContextMenu().hide();
                event.consume();
            }
        });
        deskPane.setVertexContextMenu(new ContextMenu());
        deskPane.getGraphPane().getTransparencyGraphPane().setOnContextMenuRequested(event -> deskPane.getVertexContextMenu().show(deskPane.getGraphPane().getTransparencyGraphPane(), event.getScreenX(), event.getScreenY()));

        javafx.scene.control.MenuItem addVertexItem = new javafx.scene.control.MenuItem("Добавить вершину");
        addVertexItem.setOnAction(event -> {
            Point2D screenPoint = new Point2D(deskPane.getVertexContextMenu().getX(), deskPane.getVertexContextMenu().getY());
            Point2D local = deskPane.getGraphPane().screenToLocal(screenPoint.getX(), screenPoint.getY());
            deskPane.getGraph().addVertex(deskPane.getGraph().solveNumberOfVertex(), local.getX(), local.getY());
        });
        deskPane.getGraph().getGraphPane().widthProperty().addListener((obs, o, n) -> {
            centreGraphPane();
        });
        deskPane.getGraph().getGraphPane().heightProperty().addListener((obs, o, n) -> {
            centreGraphPane();
        });
        deskPane.widthProperty().addListener((obs, o, n) -> {
            // centreGraphPane();
            deskPane.getHelpNote().setToCentre();
        });
        deskPane.heightProperty().addListener((obs, o, n) -> {
            // centreGraphPane();
            deskPane.getHelpNote().setToCentre();
        });

        javafx.scene.control.MenuItem canvasSettingItem = new MenuItem("Настройка холста");
        canvasSettingItem.setOnAction(value -> {
            CanvasSettingWindow.getInstance(deskPane.getGraph()).show();
        });
        deskPane.getVertexContextMenu().getItems().addAll(addVertexItem, canvasSettingItem);
    }

    public void resizet(double oldX, double oldY, double newX, double newY) {
        if (newX < x) deskPane.getRectangle().setX(newX); else deskPane.getRectangle().setX(oldX);
        if (newY < y) deskPane.getRectangle().setY(newY); else deskPane.getRectangle().setY(oldY);
        deskPane.getRectangle().setWidth(Math.abs(oldX - newX));
        deskPane.getRectangle().setHeight(Math.abs(oldY - newY));
        if (!deskPane.getRectangle().isVisible()) deskPane.getRectangle().setVisible(true);
        choosing();
    }

    private void choosing() {
        deskPane.getGraph().getVertexes().forEach((num, vertex) -> {
            if (deskPane.getRectangle().localToScene(deskPane.getRectangle().getBoundsInLocal()).intersects(vertex.getVertexCircle().localToScene(new Point2D(vertex.getVertexCircle().getCenterX(), vertex.getVertexCircle().getCenterY())).getX(), vertex.getVertexCircle().localToScene(new Point2D(vertex.getVertexCircle().getCenterX(), vertex.getVertexCircle().getCenterY())).getY(), 1, 1)) {
                if (!deskPane.getRectangle().getChoosingList().contains(vertex)) {
                    deskPane.getGraph().addChoseElement(vertex);
                    deskPane.getRectangle().getChoosingList().add(vertex);
                }
            } else {
                if (deskPane.getRectangle().getChoosingList().contains(vertex)) {
                    deskPane.getGraph().removeChoseElement(vertex);
                    deskPane.getRectangle().getChoosingList().remove(vertex);
                }
            }
        });
        deskPane.getGraph().getEdges().forEach((name, edge) -> {
            if (deskPane.getRectangle().localToScene(deskPane.getRectangle().getBoundsInLocal()).intersects(edge.getEdgeLine().localToScene(new Point2D(edge.getEdgeLine().getEdgeStartPoint().getX(), edge.getEdgeLine().getEdgeStartPoint().getY())).getX(), edge.getEdgeLine().localToScene(new Point2D(edge.getEdgeLine().getEdgeStartPoint().getX(), edge.getEdgeLine().getEdgeStartPoint().getY())).getY(), 1,1) &&
                    deskPane.getRectangle().localToScene(deskPane.getRectangle().getBoundsInLocal()).intersects(edge.getEdgeLine().localToScene(new Point2D(edge.getEdgeLine().getEdgeEndPoint().getX(), edge.getEdgeLine().getEdgeEndPoint().getY())).getX(), edge.getEdgeLine().localToScene(new Point2D(edge.getEdgeLine().getEdgeEndPoint().getX(), edge.getEdgeLine().getEdgeEndPoint().getY())).getY(), 1, 1)) {
                if (!deskPane.getRectangle().getChoosingList().contains(edge)) {
                    deskPane.getGraph().addChoseElement(edge);
                    deskPane.getRectangle().getChoosingList().add(edge);
                }
            } else {
                if (deskPane.getRectangle().getChoosingList().contains(edge)) {
                    deskPane.getGraph().removeChoseElement(edge);
                    deskPane.getRectangle().getChoosingList().remove(edge);
                }
            }
        });
    }

    public void chooserOff() {
        deskPane.getRectangle().getChoosingList().clear();
        deskPane.getRectangle().setVisible(false);
    }

    public void chooserOn() {
        deskPane.getRectangle().getChoosingList().addAll(deskPane.getGraph().getChosenGraphElements());
    }


}
