package com.intothevortex.client.monitor;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public record MonitorPageDefinition(Identifier id, Component title, MonitorPageFactory factory) {
}
