import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;


import javax.swing.*;

public class GraphicPanel extends JPanel {
    public GraphicPanel(Table table) {
        Graph graph = new SingleGraph("Tutorial", false, true);
        var sman=new SpriteManager(graph);
//        graph.addEdge("AB", "A", "B");
//        Node a = graph.getNode("A");
//        a.setAttribute("xy", 1, 1);
//        Node b = graph.getNode("B");
//        b.setAttribute("xy", -1, -1);

        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = (ViewPanel)viewer.addDefaultView(false);   // false indicates "no JFrame".
        this.add(view);
    }
}
