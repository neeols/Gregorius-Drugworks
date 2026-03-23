package com.wurtzitane.gregoriusdrugworkspersistence.trip;

/**
 * Marker interface for items that should NOT trigger the trip system
 * from RightClickItem immediately.
 *
 * These items defer trip handling until their use sequence finishes.
 */
public interface ITripUseDeferredItem {
}