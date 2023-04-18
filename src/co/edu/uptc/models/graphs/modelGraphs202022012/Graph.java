package co.edu.uptc.models.graphs.modelGraphs202022012;



import co.edu.uptc.models.graphs.modelGraphs202022012.UtilGraphs;
import co.edu.uptc.pojos.MapElement;
import co.edu.uptc.pojos.MapRoute;
import co.edu.uptc.views.maps.MapPointGraph;
import co.edu.uptc.views.maps.MapRouteGraph;

import java.io.FileNotFoundException;
import java.util.*;


public class Graph{

    private List<Node> elements;
    private List<Edge> edges;
    private UtilGraphs utilGraphs;

    // TODO: 16/04/2023 Donde dejar el Orientation routes

    public Graph() {
        elements = new ArrayList<>();
        edges = new ArrayList<>();
        utilGraphs = new UtilGraphs();
    }

    public void addNode(Node node){
        elements.add(node);
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public void deletePoint(MapElement mapElement){
        for (int i = 0; i < elements.size(); i++) {
            if(elements.get(i).getMapElement().getIdElement() == mapElement.getIdElement()){
                if(elements.get(i).isConnected()){
                    elements.remove(elements.get(i));
                }
            }
        }
    }

    public void saveData(Graph graph) {
        Persistence.getInstance().saveData(graph);
    }

    public Graph loadData(){
        return Persistence.getInstance().loadData();
    }
    public void calculateDistance(MapRouteGraph mapRoute){
        utilGraphs.calculateDistance(mapRoute.getPoint1(),mapRoute.getPoint2());
    }

    public Set<MapElement> calculateFastestRout(MapPointGraph point1, MapPointGraph point2){
        Set<MapElement> mapElements = new HashSet<>();
        return mapElements;

    }

    public List<Node> getNodes() {
        return elements;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setNodes(List<Node> elements) {
        this.elements = elements;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "Nodo: " + elements.toString() + " Edge: " + edges.toString();
    }
}
