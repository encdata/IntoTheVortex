package com.intothevortex.client.monitor;

import com.intothevortex.network.MonitorStatePayload;

public final class ClientMonitorState {
    private static MonitorStatePayload state;

    private ClientMonitorState() {
    }

    public static void accept(MonitorStatePayload value) {
        state = value;
    }

    public static MonitorStatePayload get() {
        return state;
    }

    public static void clear() {
        state = null;
    }
}
