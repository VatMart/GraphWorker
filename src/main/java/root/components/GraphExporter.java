package root.components;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import root.model.Graph;

public class GraphExporter {
	
	private Graph graph;
	
	public GraphExporter(Graph graph) {
		this.graph = graph;
	}
	
	public void exportPNG(File file) {
		if (graph == null || file == null) return;
		//Scene scene = new Scene(graph.getRightPane());
		graph.getGraphPane().shadowOn(false);
		Node node = graph.getGraphPane();
		SnapshotParameters ssp = new SnapshotParameters();
		WritableImage image = node.snapshot(ssp, null);
		try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	    } catch (IOException e) {
	        System.out.println("ERROR EXPORT");
	    }
		graph.getGraphPane().shadowOn(true);
	}
	
	public void exportJPG(File file) {
		if (graph == null || file == null) return;
		//Scene scene = new Scene(graph.getRightPane());
		graph.getGraphPane().shadowOn(false);
		Node node = graph.getGraphPane();
		SnapshotParameters ssp = new SnapshotParameters();
		WritableImage image = node.snapshot(ssp, null);
		try {
	        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "jpg", file);
	    } catch (IOException e) {
	        System.out.println("ERROR EXPORT");
	    }
		graph.getGraphPane().shadowOn(true);
	}
}
