package com.wurtzitane.gregoriusdrugworks.common.trip.registry;

import com.wurtzitane.gregoriusdrugworks.common.trip.model.AntidoteDefinition;
import com.wurtzitane.gregoriusdrugworks.common.trip.model.TripDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class TripRegistry {

    private final Map<String, TripDefinition> tripsByItemId = new HashMap<String, TripDefinition>();
    private final Map<Integer, TripDefinition> tripsByHash = new HashMap<Integer, TripDefinition>();
    private final Map<String, AntidoteDefinition> antidotesByItemId = new HashMap<String, AntidoteDefinition>();
    private final Map<String, Set<String>> antidoteMap = new HashMap<String, Set<String>>();

    public void clear() {
        tripsByItemId.clear();
        tripsByHash.clear();
        antidotesByItemId.clear();
        antidoteMap.clear();
    }

    public void registerTrip(TripDefinition definition) {
        String itemId = definition.getItemId();
        tripsByItemId.put(itemId, definition);
        tripsByHash.put(hashId(itemId), definition);
    }

    public void registerAntidote(AntidoteDefinition definition) {
        antidotesByItemId.put(definition.getItemId(), definition);
    }

    public void allowAntidoteForTrip(String antidoteItemId, String tripItemId) {
        Set<String> allowed = antidoteMap.get(antidoteItemId);
        if (allowed == null) {
            allowed = new HashSet<String>();
            antidoteMap.put(antidoteItemId, allowed);
        }
        allowed.add(tripItemId);
    }

    public TripDefinition getTrip(String itemId) {
        return tripsByItemId.get(itemId);
    }

    public TripDefinition getTripByHash(long hash) {
        return tripsByHash.get(Integer.valueOf((int) hash));
    }

    public AntidoteDefinition getAntidote(String itemId) {
        return antidotesByItemId.get(itemId);
    }

    public boolean canAntidoteCancel(String antidoteItemId, long activeTripHash) {
        TripDefinition active = getTripByHash(activeTripHash);
        if (active == null) {
            return false;
        }

        Set<String> allowed = antidoteMap.get(antidoteItemId);
        if (allowed == null || allowed.isEmpty()) {
            return false;
        }

        return allowed.contains(active.getItemId());
    }

    public Set<String> getAllowedTripsForAntidote(String antidoteItemId) {
        Set<String> allowed = antidoteMap.get(antidoteItemId);
        if (allowed == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(allowed);
    }

    public static int hashId(String value) {
        int hash = 0;
        for (int i = 0; i < value.length(); i++) {
            hash = ((hash << 5) - hash) + value.charAt(i);
        }
        return hash;
    }
}