package DSA;

import Database.AccountDAO.RestaurantInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HashTable {


    private static volatile int dataVersion = 0;
    private final Map<String, List<RestaurantInfo>> byArea = new HashMap<>();
    private final Map<String, String> areaNames = new java.util.TreeMap<>();
    
    public static void markStale() {
        dataVersion++;
    }

    
    private int builtVersion = -1;     

    public boolean isStale() {
        return builtVersion != dataVersion;
    }


    public void rebuild(List<RestaurantInfo> restaurants) {
        areaNames.clear();
        builtVersion = dataVersion;
        byArea.clear();
        for (RestaurantInfo r : restaurants) {
            String key = key(r.area);
            if (key.isEmpty()) continue;           
            byArea.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
            areaNames.putIfAbsent(key, r.area.trim());
        }
    }


    public List<RestaurantInfo> get(String area) {
        List<RestaurantInfo> found = byArea.get(key(area));
        return found == null ? Collections.<RestaurantInfo>emptyList() : Collections.unmodifiableList(found);
    }
    public List<String> areas() {
        return new ArrayList<>(areaNames.values());
    }
    public boolean hasArea(String area) {
        return byArea.containsKey(key(area));
    }

    public int areaCount() {
        return byArea.size();
    }

    private static String key(String area) {
        return area == null ? "" : area.trim().toLowerCase(Locale.ROOT);
    }
}