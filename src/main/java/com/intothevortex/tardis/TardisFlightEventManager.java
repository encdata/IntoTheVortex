package com.intothevortex.tardis;

import com.intothevortex.dimension.TardisDimensionManager;
import com.intothevortex.entity.ControlHitboxEntity;
import com.intothevortex.interior.ControlUseContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TardisFlightEventManager {
    private static final int MIN_FLIGHT_BEFORE_EVENT = 40;
    private static final int EVENT_COOLDOWN = 100;
    private static final int MAX_EVENTS_PER_FLIGHT = 3;
    private TardisFlightEventManager() {}

    public static void initialize() {
        FlightEventRegistry.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("intothevortex", "drift_x_positive"), new CoordinateDriftEvent(32, 0, 10));
        FlightEventRegistry.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("intothevortex", "drift_x_negative"), new CoordinateDriftEvent(-32, 0, 10));
        FlightEventRegistry.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("intothevortex", "drift_z_positive"), new CoordinateDriftEvent(0, 32, 10));
        FlightEventRegistry.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("intothevortex", "drift_z_negative"), new CoordinateDriftEvent(0, -32, 10));
        FlightEventRegistry.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("intothevortex", "dimensional_shear"), new CoordinateDriftEvent(48, 48, 4) {
            @Override public boolean eligible(FlightEventContext context) { return super.eligible(context) && !context.data().travelSourceDimension().equals(context.data().travelDestinationDimension()); }
        });
    }

    public static TardisData tick(MinecraftServer server, TardisData data) {
        if (data.travelState() != TardisTravelState.FLIGHT) return data.activeFlightEvent().isEmpty() ? data : data.clearFlightEvent();
        ServerLevel level = TardisDimensionManager.ensureLoaded(server, data.id());
        if (level == null) return data;
        FlightEventType active = data.activeFlightEvent().isEmpty() ? null : FlightEventRegistry.get(data.activeFlightEvent());
        if (!data.activeFlightEvent().isEmpty() && active == null) return data.clearFlightEvent();
        if (active != null) {
            TardisData next = active.tick(new FlightEventContext(server, level, data));
            if (next.activeEventRemaining() > 0) next = next.withFlightEvent(next.activeFlightEvent(), next.activeEventControl(), next.activeEventRemaining() - 1, next.activeEventElapsed() + 1, next.activeEventStep(), next.activeEventConsequenceApplied(), next.activeEventData());
            if (next.activeEventRemaining() <= 0) next = active.timeout(new FlightEventContext(server, level, next));
            highlight(level, next);
            return next;
        }
        if (data.autopilot()) return data;
        if (data.flightTicks() < MIN_FLIGHT_BEFORE_EVENT || data.flightTicks() % EVENT_COOLDOWN != 0 || data.targetFlightTicks() - data.flightTicks() < 140) return data;
        List<FlightEventType> eligible = new ArrayList<>();
        int totalWeight = 0;
        for (FlightEventType event : FlightEventRegistry.entries().values()) if (event.eligible(new FlightEventContext(server, level, data)) && event.weight(new FlightEventContext(server, level, data)) > 0) { eligible.add(event); totalWeight += event.weight(new FlightEventContext(server, level, data)); }
        if (eligible.isEmpty()) return data;
        int selection = Math.floorMod(data.id().hashCode() + data.flightTicks() / EVENT_COOLDOWN, totalWeight);
        FlightEventType selected = eligible.get(eligible.size() - 1);
        for (FlightEventType event : eligible) { selection -= event.weight(new FlightEventContext(server, level, data)); if (selection < 0) { selected = event; break; } }
        String control = selected.requiresControl() ? chooseControl(level) : "";
        if (selected.requiresControl() && control.isEmpty()) return data;
        String eventId = "";
        for (var entry : FlightEventRegistry.entries().entrySet()) if (entry.getValue() == selected) { eventId = entry.getKey().toString(); break; }
        TardisData started = selected.start(new FlightEventContext(server, level, data), control);
        return started.withFlightEvent(eventId, control, selected.durationTicks(new FlightEventContext(server, level, data)), 0, 0, false, "");
    }

    public static void onControl(ControlUseContext control) {
        TardisData current = TardisManager.get(control.level().getServer(), TardisDimensionManager.id(control.level().dimension()));
        if (current == null || current.activeFlightEvent().isEmpty()) return;
        FlightEventType event = FlightEventRegistry.get(current.activeFlightEvent());
        if (event == null) return;
        TardisData updated = event.onControl(new FlightEventContext(control.level().getServer(), control.level(), current), control.definition().id());
        if (updated != current) TardisManager.save(control.level().getServer(), updated);
    }

    private static String chooseControl(ServerLevel level) {
        return level.getEntitiesOfClass(ControlHitboxEntity.class, new AABB(-64, -64, -64, 64, 256, 64), entity -> entity.definition() != null).stream().map(ControlHitboxEntity::controlId).sorted().findFirst().orElse("");
    }

    private static void highlight(ServerLevel level, TardisData data) {
        if (data.activeEventControl().isEmpty() || data.flightTicks() % 10 != 0) return;
        level.getEntitiesOfClass(ControlHitboxEntity.class, new AABB(-64, -64, -64, 64, 256, 64), entity -> entity.controlId().equals(data.activeEventControl())).forEach(entity -> level.sendParticles(ParticleTypes.END_ROD, entity.getX(), entity.getY() + 0.5D, entity.getZ(), 2, 0.12D, 0.12D, 0.12D, 0.01D));
    }
}
