package com.intothevortex.tardis;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public final class DoorEvents {
    private static final List<BiConsumer<TardisData, Boolean>> STATE_CHANGED = new CopyOnWriteArrayList<>();
    private DoorEvents() {}
    public static void register(BiConsumer<TardisData, Boolean> callback) { STATE_CHANGED.add(callback); }
    public static void fire(TardisData data, boolean open) { STATE_CHANGED.forEach(callback -> callback.accept(data, open)); }
}
