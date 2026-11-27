package DSA;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Weighted, undirected graph of locations (areas) joined by roads, with the
 * distance of each road in km. Dijkstra's algorithm finds the shortest route
 * between two locations; it uses our own DSA.PriorityQueue (min-heap).
 */
public class GraphDeliveryRoutes {

    public static final double AVG_SPEED_KMH = 25.0;

    public static final class Road {
        public final String a, b;
        public final double km;

        Road(String a, String b, double km) {
            this.a = a;
            this.b = b;
            this.km = km;
        }
    }

    public static final class Route {
        public final List<String> path;
        public final double km;

        Route(List<String> path, double km) {
            this.path = path;
            this.km = km;
        }

        public int estimatedMinutes() {
            return (int) Math.ceil(km / AVG_SPEED_KMH * 60.0);
        }

        @Override
        public String toString() {
            return String.join("  \u2192  ", path);
        }
    }

    // adjacency list: location -> (neighbour -> distance in km)
    private final Map<String, Map<String, Double>> adjacency = new TreeMap<>();
    private final Set<String> hubs = new TreeSet<>();

    public boolean addLocation(String name, boolean hub) {
        if (name == null || name.trim().isEmpty() || adjacency.containsKey(name)) {
            return false;
        }
        adjacency.put(name, new TreeMap<>());
        if (hub) {
            hubs.add(name);
        }
        return true;
    }

    public boolean hasLocation(String name) {
        return adjacency.containsKey(name);
    }

    public boolean isHub(String name) {
        return hubs.contains(name);
    }

    public boolean removeLocation(String name) {
        Map<String, Double> neighbours = adjacency.remove(name);
        if (neighbours == null) {
            return false;
        }
        for (String other : neighbours.keySet()) {
            adjacency.get(other).remove(name);
        }
        hubs.remove(name);
        return true;
    }

    public boolean renameLocation(String oldName, String newName) {
        if (!adjacency.containsKey(oldName) || newName == null || newName.trim().isEmpty()
                || adjacency.containsKey(newName)) {
            return false;
        }
        Map<String, Double> neighbours = adjacency.remove(oldName);
        adjacency.put(newName, neighbours);
        for (String other : neighbours.keySet()) {
            Map<String, Double> back = adjacency.get(other);
            back.put(newName, back.remove(oldName));
        }
        if (hubs.remove(oldName)) {
            hubs.add(newName);
        }
        return true;
    }

    public List<String> locations() {
        return new ArrayList<>(adjacency.keySet());
    }

    public List<String> hubs() {
        return new ArrayList<>(hubs);
    }

    public boolean addRoad(String a, String b, double km) {
        if (a == null || b == null || a.equals(b) || km <= 0
                || !adjacency.containsKey(a) || !adjacency.containsKey(b)) {
            return false;
        }
        adjacency.get(a).put(b, km);
        adjacency.get(b).put(a, km);
        return true;
    }

    public boolean removeRoad(String a, String b) {
        if (!adjacency.containsKey(a) || !adjacency.containsKey(b) || adjacency.get(a).remove(b) == null) {
            return false;
        }
        adjacency.get(b).remove(a);
        return true;
    }

    public List<Road> roads() {
        List<Road> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> from : adjacency.entrySet()) {
            for (Map.Entry<String, Double> to : from.getValue().entrySet()) {
                if (from.getKey().compareTo(to.getKey()) < 0) {   // list each road once
                    result.add(new Road(from.getKey(), to.getKey(), to.getValue()));
                }
            }
        }
        return result;
    }

    public int degree(String name) {
        Map<String, Double> neighbours = adjacency.get(name);
        return neighbours == null ? 0 : neighbours.size();
    }

    // Dijkstra's shortest path, O((V + E) log V). Returns null if either
    // location is unknown or there is no way to get from one to the other.
    public Route shortestRoute(String from, String to) {
        if (!adjacency.containsKey(from) || !adjacency.containsKey(to)) {
            return null;
        }
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> done = new HashSet<>();

        // min-heap ordered by distance travelled so far
        PriorityQueue<Map.Entry<String, Double>> queue =
                new PriorityQueue<Map.Entry<String, Double>>((x, y) -> Double.compare(x.getValue(), y.getValue()));

        dist.put(from, 0.0);
        queue.enqueue(new AbstractMap.SimpleEntry<>(from, 0.0));

        while (!queue.isEmpty()) {
            String current = queue.dequeue().getKey();
            if (!done.add(current)) {
                continue;                       // stale heap entry, already settled
            }
            if (current.equals(to)) {
                break;                          // destination settled = shortest found
            }
            for (Map.Entry<String, Double> edge : adjacency.get(current).entrySet()) {
                double candidate = dist.get(current) + edge.getValue();
                if (candidate < dist.getOrDefault(edge.getKey(), Double.POSITIVE_INFINITY)) {
                    dist.put(edge.getKey(), candidate);
                    previous.put(edge.getKey(), current);
                    queue.enqueue(new AbstractMap.SimpleEntry<>(edge.getKey(), candidate));
                }
            }
        }
        if (!dist.containsKey(to)) {
            return null;
        }
        LinkedList<String> path = new LinkedList<>();
        for (String at = to; at != null; at = previous.get(at)) {
            path.addFirst(at);
        }
        return new Route(path, dist.get(to));
    }

    // every location you can reach from start (breadth-first search)
    public Set<String> reachableFrom(String start) {
        Set<String> seen = new LinkedHashSet<>();
        if (!adjacency.containsKey(start)) {
            return seen;
        }
        Deque<String> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start);
        while (!queue.isEmpty()) {
            for (String next : adjacency.get(queue.poll()).keySet()) {
                if (seen.add(next)) {
                    queue.add(next);
                }
            }
        }
        return seen;
    }

    public List<String> unreachableFromHubs() {
        Set<String> covered = new HashSet<>();
        for (String hub : hubs) {
            covered.addAll(reachableFrom(hub));
        }
        List<String> missing = new ArrayList<>();
        for (String location : adjacency.keySet()) {
            if (!covered.contains(location)) {
                missing.add(location);
            }
        }
        return missing;
    }

    public Route nearestHubRoute(String destination) {
        Route best = null;
        for (String hub : hubs) {
            Route route = shortestRoute(hub, destination);
            if (route != null && (best == null || route.km < best.km)) {
                best = route;
            }
        }
        return best;
    }
    // finds which known area an address belongs to, e.g. "PUP Biñan, San Antonio" -> "Binan"
    public String resolveArea(String address) {
        if (address == null) {
            return null;
        }
        String text = java.text.Normalizer.normalize(address, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
        for (String area : adjacency.keySet()) {
            String key = java.text.Normalizer.normalize(area, java.text.Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "").toLowerCase();
            if (text.contains(key)) {
                return area;
            }
        }
        return null;
    }
        // the one shared network: the customer's Tracking screen and the admin's Routes tab both use it
    private static final GraphDeliveryRoutes SHARED = defaultNetwork();

    public static GraphDeliveryRoutes shared() {
        return SHARED;
    }

    // the areas offered at sign-up, joined by roads.
    // The distances are starting values; the admin can edit them in the Routes tab.
    private static GraphDeliveryRoutes defaultNetwork() {
        GraphDeliveryRoutes g = new GraphDeliveryRoutes();
        String[] areas = {"Binan", "Cabuyao", "San Pablo", "San Pedro", "Santa Rosa"};
        for (String area : areas) {
            g.addLocation(area, false);
        }
        g.addRoad("San Pedro", "Binan", 8.0);
        g.addRoad("Binan", "Santa Rosa", 9.0);
        g.addRoad("San Pedro", "Santa Rosa", 20.0);   // longer than going through Binan (17)
        g.addRoad("Santa Rosa", "Cabuyao", 11.0);
        g.addRoad("Binan", "Cabuyao", 22.0);          // longer than going through Santa Rosa (20)
        g.addRoad("Cabuyao", "San Pablo", 33.0);
        g.addRoad("Santa Rosa", "San Pablo", 40.0);
        return g;
    }

    // marks which areas have an active restaurant right now (used by "nearest restaurant")
    public void setHubs(java.util.Collection<String> areaNames) {
        hubs.clear();
        for (String name : areaNames) {
            if (adjacency.containsKey(name)) {
                hubs.add(name);
            }
        }
    }
    // starting network for the admin dashboard (changed in step 8.3)

}