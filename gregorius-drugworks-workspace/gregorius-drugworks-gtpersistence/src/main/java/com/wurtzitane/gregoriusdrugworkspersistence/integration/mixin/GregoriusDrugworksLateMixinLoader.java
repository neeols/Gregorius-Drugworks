package com.wurtzitane.gregoriusdrugworkspersistence.integration.mixin;

import java.util.Collections;
import java.util.List;

import zone.rong.mixinbooter.ILateMixinLoader;

/**
 * Registers GDW client mixins explicitly for MixinBooter runtime discovery.
 *
 * @author wurtzitane
 */
public final class GregoriusDrugworksLateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.gregoriusdrugworkspersistence.json");
    }
}
