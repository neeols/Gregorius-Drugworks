package com.wurtzitane.gregoriusdrugworks.common.payload;

import java.util.Set;

public interface IPayloadCarrier {

    Set<PayloadCategory> supportedPayloadCategories();

    boolean canAcceptPayload(PayloadDefinition payload);

}