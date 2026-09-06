package com.intothevortex.client.monitor;

import net.minecraft.client.gui.screens.Screen;
import com.intothevortex.network.MonitorStatePayload;

@FunctionalInterface
public interface MonitorPageFactory {
    Screen create(TardisMonitorScreen parent, MonitorStatePayload state);
}
