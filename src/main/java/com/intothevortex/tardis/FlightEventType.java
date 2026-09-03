package com.intothevortex.tardis;

public interface FlightEventType {
    boolean eligible(FlightEventContext context);
    int weight(FlightEventContext context);
    int durationTicks(FlightEventContext context);
    boolean requiresControl();
    TardisData start(FlightEventContext context, String controlId);
    TardisData tick(FlightEventContext context);
    TardisData onControl(FlightEventContext context, String controlId);
    TardisData timeout(FlightEventContext context);
}
