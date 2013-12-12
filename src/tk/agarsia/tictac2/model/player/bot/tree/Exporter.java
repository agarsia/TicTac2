package tk.agarsia.tictac2.model.player.bot.tree;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Exporter {

    private static PrintWriter output;
    private static int boardDim;
    
    public static void setBoardDim(int boardDim){
    	Exporter.boardDim = boardDim;
    }

    public static void doExport(TreeBuilder graph, String filename) throws FileNotFoundException {
        ArrayList<Node> nodes = graph.getNodes();
        ArrayList<Edge> edges = graph.getEdges();

        output = new PrintWriter(filename);

        output.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">");
        output.println("\t<key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d5\"/>");
        output.println("\t<key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>");
        output.println("\t<key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>");
        output.println("\t<key for=\"edge\" id=\"d10\" yfiles.type=\"edgegraphics\"/>");
        output.println("<graph>");

        for (Node node : nodes) {
            node(node.getID(), node.showBoard(), node.wonOrLost(), node.iWon(), node.getExtraInfo());
        }
        for (Edge edge : edges) {
            edge(edge.getID(), edge.getSource().getID(), edge.getTarget()); //generating hash-code just here, not needed before. but yED needs it to read the graphml-file
        }

        output.println("</graph>");
        output.println("</graphml>");
        output.close();

    }

    private static void node(String ID, String value, boolean wonOrLost, boolean iWon, String extraInfo) { //winState: 0 = no winner, 1 = i won, 2 =  i lost
        output.println("<node id=" + '"' + ID + '"' + ">" +
                "<data key=\"d5\"><![CDATA[[" + ID + "]\n" + extraInfo + "]]></data>" +
                "<data key=\"d6\">" +
                "<y:ShapeNode><y:Geometry height=\"" + (18 * boardDim) + "\" width=\"" + (14 * boardDim) + "\"/><y:Fill color=\"" + (!wonOrLost ? "#FFCC00" : (iWon ? "#00CCFF" : "#FF0000")) + "\" transparent=\"false\"/>" +
                "<y:BorderStyle color=\"#FFFFFF\" type=\"line\" width=\"0.5\"/>" +
                "<y:NodeLabel textColor=\"#000000\">" + value + extraInfo + "</y:NodeLabel>" + //"\n" + details + "\n" + wonOrLost + "\n" + iWon +
                "<y:Shape type=\"roundrectangle\"/></y:ShapeNode></data></node>");
    }

    private static void edge(String ID, String sourceID, Node target) {
        output.println("<edge id=" + '"' + ID + '"' + " source=" + '"' + sourceID + '"' + " target=" + '"' + target.getID() + '"' + ">" +
                "<data key=\"d9\"><![CDATA[Edge ID: " + ID + "]]></data>" +
                "<data key=\"d10\"><y:BezierEdge>" +
                "<y:LineStyle color=\""               
				+ (!target.wonOrLost() ? 
						"#666666\" type=\"line\" width=\"1.0"  // target is neither win or loose node, default
					  : (target.iWon() ? 
						"#00CCFF\" type=\"line\" width=\"2.0"  // target is win node
					  : "#FF0000\" type=\"line\" width=\"2.0"  // target is loose node
								 ))
                + "\"/>" +
                "<y:Arrows source=\"none\" target=\"standard\"/>" +
                "</y:BezierEdge></data></edge>");
        output.println("");
    }
    
   
    
}