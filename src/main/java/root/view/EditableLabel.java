package root.view;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class EditableLabel extends Label {
	private TextField tf = new TextField();
	/***
	 * backup is used to cancel when press ESC...
	 */
	public String backup = "";

	public EditableLabel() {
		this("");
	}

	public EditableLabel(String str) {
		super(str);
		this.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				tf.setText(backup = this.getText());
				this.setGraphic(tf);
				this.setText("");
				tf.requestFocus();
			}
		});
		tf.focusedProperty().addListener((prop, o, n) -> {
			if (!n) {
				toLabel();
			}
		});
		tf.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				toLabel();
			} else if (e.getCode().equals(KeyCode.ESCAPE)) {
				tf.setText(backup);
				toLabel();
			}
		});
	}

	public TextField getTf() {
		return tf;
	}


	public void toLabel() {
		this.setGraphic(null);
		this.setText(tf.getText());
	}
}