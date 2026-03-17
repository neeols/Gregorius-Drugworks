package com.wurtzitane.gregoriusdrugworkspersistence.integration.groovyscript;

import com.cleanroommc.groovyscript.compat.mods.ModPropertyContainer;

public final class GregoriusDrugworksGroovyScriptContainer extends ModPropertyContainer {

    public static final GroovyPillsSection pills = new GroovyPillsSection();
    public static final GroovyVapesSection vapes = new GroovyVapesSection();
    public static final GroovyPayloadsSection payloads = new GroovyPayloadsSection();
    public static final GroovyPayloadSourcesSection payloadSources = new GroovyPayloadSourcesSection();
    public static final GroovyTriggerBundlesSection triggerBundles = new GroovyTriggerBundlesSection();
    public static final GroovyVisualProfilesSection visualProfiles = new GroovyVisualProfilesSection();
    public static final GroovyTripsSection trips = new GroovyTripsSection();
    public static final GroovyApplicatorsSection applicators = new GroovyApplicatorsSection();

    public GregoriusDrugworksGroovyScriptContainer() {
        addRegistry(pills);
        addRegistry(vapes);
        addRegistry(payloads);
        addRegistry(payloadSources);
        addRegistry(triggerBundles);
        addRegistry(visualProfiles);
        addRegistry(trips);
        addRegistry(applicators);
    }
}
