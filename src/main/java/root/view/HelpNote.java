package root.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import root.GraphWorkerApplication;

public class HelpNote extends StackPane {

    private Label lHelpNote;
    private MainPane parent;

    public static final String DEFAULT_TEXT = "Используйте мышь и панель инструментов, чтобы добавлять вершины и грани";

    public HelpNote(MainPane parent) {
        super();
        this.parent = parent;
        initiate();
        initiateLabel();
    }

    private void initiate() {
        setPadding(new Insets(4, 5, 4, 5));
        setStyle("-fx-background-color: #000000; -fx-background-radius: 4px;");
        setOpacity(0.6);
        // helpNote.setToCentre();
        setLayoutX(363.5 - getBoundsInParent().getWidth() / 2);
    }

    private void initiateLabel() {
        lHelpNote = new Label(DEFAULT_TEXT);
        lHelpNote.setTextFill(Paint.valueOf("white"));
        getChildren().add(lHelpNote);
        setLayoutX(363.5 - lHelpNote.getText().toCharArray().length * 3.2);
        setLayoutY(5);
    }

    public void setToCentre() {
        double deskCentreX = GraphWorkerApplication.getInstance().getPrimaryStage().getScene().getWidth() / 2
                - (parent.getTbHideLeftPane().isSelected() ? (0) : 125);
        setLayoutX((deskCentreX - (lHelpNote.getText().toCharArray().length * 3.2)));
        setLayoutY(5);
    }

    public void setNote(String note) {
        lHelpNote.setText(note);
        setToCentre();
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }
}