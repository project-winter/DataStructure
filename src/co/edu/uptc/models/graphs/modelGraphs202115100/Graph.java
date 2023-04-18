package co.edu.uptc.models.graphs.modelGraphs202115100;

import co.edu.uptc.pojos.MapElement;
import co.edu.uptc.pojos.MapRoute;
import co.edu.uptc.views.maps.types.ElementType;

import java.util.*;

import static java.lang.Math.*;

public class Graph {
    private Map<Integer, MapElement> elements;
    private Map<Integer, MapElement> resultElements;
    private int count = 0;
    public static final int SPEED = 0;
    public static final int DISTANCE = 1;
    public static final double RADIUS = 6371;

    public Graph() {
        elements = new HashMap<>();
        resultElements = new HashMap<>();
    }

    public void addElement(MapElement element) {
        element.setIdElement(count++);
        elements.put(element.getIdElement(), element);
    }

    public Set<MapElement> getElements() {
        return new HashSet<>(elements.values());
    }

    public void setElements(Map<Integer, MapElement> elements) {
        this.elements = elements;
    }

    public void setResultElements(Map<Integer, MapElement> resultElements) {
        this.resultElements = resultElements;
    }

    public int getCount() {
        return count;
    }

    public Double getDistanceBetweenPoints(MapElement point1, MapElement point2) {
        double lat1Rad = toRadians(point1.getGeoPosition().getLatitude());
        double lat2Rad = toRadians(point2.getGeoPosition().getLatitude());

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = toRadians(point2.getGeoPosition().getLongitude()) - toRadians(point1.getGeoPosition().getLongitude());

        double a = sin(deltaLat / 2) * sin(deltaLat / 2) + cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2) * sin(deltaLon / 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return RADIUS * c;
    }

    public void deleteElement(int id) {
        elements.remove(id);
    }

    public void calculateShortestRoute(int idPoint1, int idPoint2, int attributeToCompare) {
        Map<MapElement, Double> temporalValues = new HashMap<>();
        for (MapElement point : elements.values().stream().filter(element -> element.getElementType() == ElementType.POINT).toList()) {
            temporalValues.put(point, Double.MAX_VALUE);
        }
        Map<MapElement, Double> finalValues = new HashMap<>(temporalValues);
        temporalValues.put(getElement(idPoint1), 0.0);
        dijkstra(temporalValues, finalValues, attributeToCompare);
        getShortestRoute(finalValues, idPoint2, attributeToCompare);
    }

    private void getShortestRoute(Map<MapElement, Double> finalValues, int idPoint2, int attributeToCompare) {
        for (MapRoute element : getChildren(getElement(idPoint2))) {
            MapElement definitivePoint = (getElement(idPoint2) == element.getPoint1()) ? element.getPoint2() : element.getPoint1();
            double value = getValueOfAttribute(element, attributeToCompare);
            if (finalValues.get(definitivePoint) == (finalValues.get(getElement(idPoint2)) - value)) {//Pendiente - El set deberia contener rutas y puntos?
                resultElements.put(element.getPoint1().getIdElement(), element.getPoint1());
                resultElements.put(element.getPoint2().getIdElement(), element.getPoint2());
                resultElements.put(searchElementByRoute(element).getIdElement(), searchElementByRoute(element));
                getShortestRoute(finalValues, element.getPoint1().getIdElement(), attributeToCompare);
            } else {
                resultElements.put(getElement(idPoint2).getIdElement(), getElement(idPoint2));
            }
        }
    }

    private double getValueOfAttribute(MapRoute point, int attributeToCompare) {
        double value = getDistanceBetweenPoints(point.getPoint1(), point.getPoint2());
        return switch (attributeToCompare) {
            case SPEED ->//Pendiente - Usar el tiempo y la distancia para hallar la velocidad
                    point.getSpeedRoute();
            case DISTANCE -> value;
            default -> 0;
        };
    }

    private void dijkstra(Map<MapElement, Double> temporalValues, Map<MapElement, Double> finalValues, int attributeToCompare) {
        MapElement minPoint = getMinPoint(new HashMap<>(temporalValues), new HashMap<>(finalValues));
        finalValues.put(minPoint, temporalValues.get(minPoint));
        setTemporalValues(minPoint, getChildren(minPoint), temporalValues, attributeToCompare);
        if (finalValues.containsValue(Double.MAX_VALUE)) {
            dijkstra(temporalValues, finalValues, attributeToCompare);
        }
    }

    private MapElement getMinPoint(Map<MapElement, Double> temporalValues, Map<MapElement, Double> finalValues) {
        MapElement minKey = null;
        Double minValue = null;
        for (Map.Entry<MapElement, Double> entry : temporalValues.entrySet()) {
            if (minValue == null || entry.getValue() < minValue) {
                minValue = entry.getValue();
                minKey = entry.getKey();
            }
        }
        if (finalValues.get(minKey) != Double.MAX_VALUE) {
            temporalValues.remove(minKey);
            return getMinPoint(temporalValues, finalValues);
        }
        return minKey;
    }

    private void setTemporalValues(MapElement minPoint, List<MapRoute> children, Map<MapElement, Double> temporalValues, int attributeToCompare) {
        for (MapRoute child : children) {
            MapElement point = child.getPoint1().equals(minPoint) ? child.getPoint2() : child.getPoint1();
            double temporalValue = temporalValues.get(minPoint) + getValueOfAttribute(child, attributeToCompare);
            if (temporalValues.get(point) == Double.MAX_VALUE) {
                temporalValues.put(point, temporalValue);
            } else {
                temporalValues.put(point, Math.min(temporalValues.get(point), temporalValue));
            }
        }
    }

    private List<MapRoute> getChildren(MapElement actual) {
        List<MapRoute> children = new ArrayList<>();
        for (MapElement route : elements.values()) {
            if (route.getElementType() == ElementType.ROUTE) {
                if (route.getMapRoute().getPoint1().equals(actual) || route.getMapRoute().getPoint2().equals(actual)) {
                    children.add(route.getMapRoute());
                }
            }
        }
        return children;
    }

    private MapElement searchElementByRoute(MapRoute child) {
        for (MapElement element : elements.values()) {
            if (element.getElementType() == ElementType.ROUTE) {
                if (element.getMapRoute().equals(child)) {
                    return element;
                }
            }
        }
        return null;
    }

    public MapElement getElement(int id) {
        for (MapElement element : elements.values()) {
            if (element.getIdElement() == id) {
                return element;
            }
        }
        return null;
    }

    public Map<Integer, MapElement> getResultElements() {
        return resultElements;
    }
}

