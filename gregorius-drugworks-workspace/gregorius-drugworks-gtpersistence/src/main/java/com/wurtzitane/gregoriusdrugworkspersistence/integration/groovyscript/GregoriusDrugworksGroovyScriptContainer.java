package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;

public final class GregoriusDrugworksGroovyScriptContainer extends GroovyPropertyContainer {

    public static final GroovyPillsSection pills = new GroovyPillsSection();
    public static final GroovyVapesSection vapes = new GroovyVapesSection();
    public static final GroovyPayloadsSection payloads = new GroovyPayloadsSection();
    public static final GroovyPayloadSourcesSection payloadSources = new GroovyPayloadSourcesSection();
    public static final GroovyTriggerBundlesSection triggerBundles = new GroovyTriggerBundlesSection();
    public static final GroovyVisualProfilesSection visualProfiles = new GroovyVisualProfilesSection();
    public static final GroovyTripsSection trips = new GroovyTripsSection();
    public static final GroovyApplicatorsSection applicators = new GroovyApplicatorsSection();

    public GregoriusDrugworksGroovyScriptContainer() {
        addProperty(pills);
        addProperty(vapes);
        addProperty(payloads);
        addProperty(payloadSources);
        addProperty(triggerBundles);
        addProperty(visualProfiles);
        addProperty(trips);
        addProperty(applicators);
    }
}
