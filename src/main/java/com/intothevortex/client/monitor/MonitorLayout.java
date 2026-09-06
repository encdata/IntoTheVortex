package com.intothevortex.client.monitor;

public final class MonitorLayout {
    private final int left;
    private final int top;
    private final int width;
    private final int height;

    public MonitorLayout(int screenWidth, int screenHeight) {
        width = Math.min(480, screenWidth - 24);
        height = Math.min(280, screenHeight - 24);
        left = (screenWidth - width) / 2;
        top = (screenHeight - height) / 2;
    }

    public int x() {
        return left;
    }

    public int y() {
        return top;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int x(MonitorAnchor anchor, int widgetWidth, int offset) {
        return switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> left + offset;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> left + (width - widgetWidth) / 2 + offset;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> left + width - widgetWidth - offset;
        };
    }

    public int y(MonitorAnchor anchor, int widgetHeight, int offset) {
        return switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> top + offset;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> top + (height - widgetHeight) / 2 + offset;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> top + height - widgetHeight - offset;
        };
    }
}
