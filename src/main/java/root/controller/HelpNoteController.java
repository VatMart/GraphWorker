package root.controller;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import root.view.HelpNote;

public class HelpNoteController implements ActionableComponents {
    private HelpNote helpNote;

    public HelpNoteController(HelpNote helpNote) {
        this.helpNote = helpNote;
    }

    @Override
    public void buildActionsComponents() {
        helpNote.setOnMouseEntered(value -> {
            helpNote.setOpacity(0.8);
        });
        helpNote.setOnMouseExited(value -> {
            helpNote.setOpacity(0.6);
        });
        ContextMenu menu = new ContextMenu();
        MenuItem hide = new MenuItem("Спрятать");
        hide.setOnAction(value -> {
            helpNote.hide();
        });
        menu.getItems().add(hide);
        helpNote.setOnContextMenuRequested(value -> {
            menu.show(helpNote, value.getScreenX(), value.getScreenY());
        });
    }
}
