package com.intothevortex.tardis;

import com.intothevortex.entity.ControlHitboxEntity;
import com.intothevortex.dimension.TardisDimensionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import java.util.List;

public class CoordinateDriftEvent implements FlightEventType {
    private final int dx;
    private final int dz;
    private final int weight;

    public CoordinateDriftEvent(int dx, int dz, int weight) {
        this.dx = dx;
        this.dz = dz;
        this.weight = weight;
    }

    @Override public boolean eligible(FlightEventContext context) { return context.data().travelState() == TardisTravelState.FLIGHT; }
    @Override public int weight(FlightEventContext context) { return weight; }
    @Override public int durationTicks(FlightEventContext context) { return 120; }
    @Override public boolean requiresControl() { return true; }
    @Override public TardisData start(FlightEventContext context, String controlId) { return context.data().withFlightEvent("", controlId, durationTicks(context), 0, 0, false, ""); }
    @Override public TardisData tick(FlightEventContext context) { return context.data(); }
    @Override public TardisData onControl(FlightEventContext context, String controlId) { return controlId.equals(context.data().activeEventControl()) ? context.data().clearFlightEvent() : context.data(); }
    @Override public TardisData timeout(FlightEventContext context) {
        if (context.data().activeEventConsequenceApplied()) return context.data().clearFlightEvent();
        TardisTravelDestination destination = context.requestedDestination();
        long x = (long) destination.position().getX() + dx;
        long z = (long) destination.position().getZ() + dz;
        int safeX = (int) Math.clamp(x, -29_999_999L, 29_999_999L);
        int safeZ = (int) Math.clamp(z, -29_999_999L, 29_999_999L);
        TardisData shifted = context.data().withRequestedDestination(new TardisTravelDestination(destination.dimension(), new net.minecraft.core.BlockPos(safeX, destination.position().getY(), safeZ), destination.yaw())).withFlightEvent(context.data().activeFlightEvent(), context.data().activeEventControl(), 0, context.data().activeEventElapsed(), context.data().activeEventStep(), true, context.data().activeEventData());
        return shifted.clearFlightEvent();
    }

    public static String chooseControl(ServerLevel level) {
        List<ControlHitboxEntity> controls = level.getEntitiesOfClass(ControlHitboxEntity.class, new AABB(-64, -64, -64, 64, 256, 64), entity -> entity.definition() != null);
        return controls.stream().map(ControlHitboxEntity::controlId).sorted().findFirst().orElse("");
    }
}
