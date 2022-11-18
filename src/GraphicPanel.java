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
import java.awt.*;

public class GraphicPanel extends JPanel {
    public GraphicPanel(Table table) {
        Graph graph = new SingleGraph("Result", false, true);
        var sman=new SpriteManager(graph);
        for (var node_name:table.nodes) {
            graph.addNode(node_name);
            var node=graph.getNode(node_name);
            node.setAttribute("ui.style", "shape:circle;fill-color: yellow;size: 13px;");
            node.setAttribute("ui.label", node_name);


        }
        for (int i=0;i<table.routes.size(); i++){
            var line=table.routes.get(i);
            var edge=graph.addEdge(line, Character.toString(line.charAt(0)), Character.toString(line.charAt(1)));
            edge.setAttribute("ui.label", table.C.get(i).toString());
        }
        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        ViewPanel view = (ViewPanel)viewer.addDefaultView(false);
        view.setPreferredSize(new Dimension(300, 300));
        this.add(view);
    }
}
