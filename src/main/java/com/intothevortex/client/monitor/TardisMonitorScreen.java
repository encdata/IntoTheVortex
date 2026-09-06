package com.intothevortex.client.monitor;

import com.intothevortex.network.MonitorStatePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import com.intothevortex.exterior.ExteriorDefinition;
import com.intothevortex.exterior.ExteriorRegistry;
import com.intothevortex.interior.InteriorRegistry;
import com.intothevortex.network.MonitorSelectionPayload;
import com.intothevortex.client.render.TardisExteriorRenderState;
import com.intothevortex.entity.ModEntityTypes;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import java.util.ArrayList;
import java.util.List;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class TardisMonitorScreen extends Screen {
    private enum Page { MAIN, STATUS, EXTERIOR, INTERIOR, FLIGHT, SECURITY, SETTINGS }

    private MonitorStatePayload state;
    private final net.minecraft.core.BlockPos monitorPos;
    private MonitorLayout layout;
    private Page page = Page.MAIN;
    private int exteriorIndex;
    private int interiorIndex;
    private int previewTicks;

    public TardisMonitorScreen(MonitorStatePayload state) {
        this(state, net.minecraft.core.BlockPos.ZERO);
    }

    public TardisMonitorScreen(MonitorStatePayload state, net.minecraft.core.BlockPos monitorPos) {
        super(Component.literal("Into The Vortex"));
        this.state = state;
        this.monitorPos = monitorPos;
    }

    public net.minecraft.core.BlockPos monitorPos() {
        return monitorPos;
    }

    @Override
    protected void init() {
        layout = new MonitorLayout(width, height);
        exteriorIndex = selectedExteriorIndex();
        interiorIndex = selectedInteriorIndex();
        rebuildButtons();
    }

    private void rebuildButtons() {
        clearWidgets();
        addPageContent();
        if (page != Page.MAIN) {
            addRenderableWidget(Button.builder(Component.literal("Back"), button -> {
                page = Page.MAIN;
                rebuildButtons();
            }).bounds(layout.x(MonitorAnchor.TOP_LEFT, 48, 8), layout.y(MonitorAnchor.TOP_LEFT, 20, 8), 48, 20).build());
        }
        if (page == Page.MAIN) {
            addPageButton("Status", Page.STATUS, 0, 0);
            addPageButton("Exterior", Page.EXTERIOR, 1, 0);
            addPageButton("Interior", Page.INTERIOR, 0, 1);
            addPageButton("Flight", Page.FLIGHT, 1, 1);
            addPageButton("Security", Page.SECURITY, 0, 2);
            addPageButton("Settings", Page.SETTINGS, 1, 2);
            int row = 3;
            int column = 0;
            for (MonitorPageDefinition definition : MonitorPageRegistry.definitions()) {
                addAddonPageButton(definition, column, row);
                column++;
                if (column == 2) {
                    column = 0;
                    row++;
                }
            }
        }
        if (page == Page.EXTERIOR || page == Page.INTERIOR) addSelectionControls();
        addRenderableWidget(Button.builder(Component.literal("Close"), button -> Minecraft.getInstance().setScreen(null)).bounds(layout.x(MonitorAnchor.BOTTOM_RIGHT, 52, 8), layout.y(MonitorAnchor.BOTTOM_RIGHT, 20, 8), 52, 20).build());
    }

    private void addSelectionControls() {
        int y = layout.y() + layout.height() - 52;
        addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            if (page == Page.EXTERIOR) exteriorIndex = Math.floorMod(exteriorIndex - 1, exteriorOptions().size());
            else interiorIndex = Math.floorMod(interiorIndex - 1, interiorOptions().size());
            rebuildButtons();
        }).bounds(layout.x() + 16, y, 24, 20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            if (page == Page.EXTERIOR) exteriorIndex = (exteriorIndex + 1) % exteriorOptions().size();
            else interiorIndex = (interiorIndex + 1) % interiorOptions().size();
            rebuildButtons();
        }).bounds(layout.x() + 44, y, 24, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Apply"), button -> {
            Identifier selected = page == Page.EXTERIOR ? exteriorOptions().get(exteriorIndex).id() : interiorOptions().get(interiorIndex);
            ClientPlayNetworking.send(new MonitorSelectionPayload(state.tardisId(), monitorPos, page == Page.EXTERIOR ? "exterior" : "interior", selected.toString()));
        }).bounds(layout.x() + 76, y, 62, 20).build());
    }

    @Override
    public void tick() {
        previewTicks = (previewTicks + 1) % 80;
        super.tick();
    }

    private void addPageContent() {
        int x = layout.x() + 16;
        int y = layout.y() + 30;
        switch (page) {
            case MAIN -> {
                addTextLine(x, y, "TARDIS " + state.tardisId());
                addTextLine(x, y + 14, "Power: " + (state.powered() ? "ON" : "OFF"));
                addTextLine(x, y + 28, "Fuel: " + Math.round(state.fuel()) + " / " + Math.round(state.maxFuel()));
            }
            case STATUS -> {
                addTextLine(x, y, "State: " + state.travelState());
                addTextLine(x, y + 14, "Condition: " + state.flightCondition());
                addTextLine(x, y + 28, "Power: " + (state.powered() ? "ON" : "OFF"));
                addTextLine(x, y + 42, "Fuel: " + Math.round(state.fuel()) + " / " + Math.round(state.maxFuel()));
                addTextLine(x, y + 56, "Throttle: " + state.throttle());
                addTextLine(x, y + 70, "Handbrake: " + (state.handbrake() ? "ENGAGED" : "RELEASED"));
            }
            case EXTERIOR -> {
                addTextLine(x, y, "Exterior selection");
                addTextLine(x, y + 14, exteriorOptions().get(exteriorIndex).id().toString());
                addTextLine(x, y + 30, "Current: " + state.exterior());
            }
            case INTERIOR -> {
                addTextLine(x, y, "Interior selection");
                addTextLine(x, y + 14, interiorOptions().get(interiorIndex).toString());
                addTextLine(x, y + 30, "Current: " + state.interior());
            }
            case FLIGHT -> {
                addTextLine(x, y, "Current: " + state.dimension());
                addTextLine(x, y + 14, "Position: " + state.position().toShortString());
                addTextLine(x, y + 28, "Destination: " + state.destinationDimension());
                addTextLine(x, y + 42, "Target: " + state.destinationPosition().toShortString());
                addTextLine(x, y + 56, "Progress: " + state.progress() + "%");
                addTextLine(x, y + 70, "Event: " + (state.eventId().isEmpty() ? "None" : state.eventId()));
            }
            case SECURITY -> addTextLine(x, y, "Security: " + (state.locked() ? "Locked" : "Unlocked"));
            case SETTINGS -> addTextLine(x, y, "Settings: Registry-driven monitor");
        }
    }

    private void addTextLine(int x, int y, String text) {
        addRenderableWidget(new StringWidget(x, y, layout.width() - 32, 10, Component.literal(text), font));
    }

    private List<ExteriorDefinition> exteriorOptions() {
        return new ArrayList<>(ExteriorRegistry.values());
    }

    private List<Identifier> interiorOptions() {
        return new ArrayList<>(InteriorRegistry.registered());
    }

    private int selectedExteriorIndex() {
        List<ExteriorDefinition> options = exteriorOptions();
        for (int index = 0; index < options.size(); index++) if (options.get(index).id().toString().equals(state.exterior())) return index;
        return 0;
    }

    private int selectedInteriorIndex() {
        List<Identifier> options = interiorOptions();
        for (int index = 0; index < options.size(); index++) if (options.get(index).toString().equals(state.interior())) return index;
        return 0;
    }

    private void addPageButton(String label, Page target, int column, int row) {
        int buttonWidth = 120;
        int x = layout.x(MonitorAnchor.CENTER, buttonWidth, 0) - 64 + column * 128;
        int y = layout.y(MonitorAnchor.CENTER, 20, -34) + row * 28;
        addRenderableWidget(Button.builder(Component.literal(label), button -> {
            page = target;
            rebuildButtons();
        }).bounds(x, y, buttonWidth, 20).build());
    }

    private void addAddonPageButton(MonitorPageDefinition definition, int column, int row) {
        int buttonWidth = 120;
        int x = layout.x(MonitorAnchor.CENTER, buttonWidth, 0) - 64 + column * 128;
        int y = layout.y(MonitorAnchor.CENTER, 20, -34) + row * 28;
        addRenderableWidget(Button.builder(definition.title(), button -> Minecraft.getInstance().setScreen(definition.factory().create(this, state))).bounds(x, y, buttonWidth, 20).build());
    }

    public void openPage(Identifier id) {
        MonitorPageDefinition definition = MonitorPageRegistry.definitions().stream().filter(value -> value.id().equals(id)).findFirst().orElse(null);
        if (definition != null) Minecraft.getInstance().setScreen(definition.factory().create(this, state));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, 0, width, height, 0xB0000000);
        graphics.fill(layout.x(), layout.y(), layout.x() + layout.width(), layout.y() + layout.height(), 0xFF101820);
        graphics.fill(layout.x() + 3, layout.y() + 3, layout.x() + layout.width() - 3, layout.y() + layout.height() - 3, 0xFF182630);
        if (page == Page.EXTERIOR || page == Page.INTERIOR) {
            graphics.fill(layout.x() + 10, layout.y() + 25, layout.x() + layout.width() - 122, layout.y() + layout.height() - 12, 0xFF111B22);
            graphics.fill(layout.x() + layout.width() - 114, layout.y() + 25, layout.x() + layout.width() - 10, layout.y() + layout.height() - 12, 0xFF0B1117);
        }
        graphics.centeredText(font, title, width / 2, layout.y() + 10, 0xE0F5FF);
        graphics.nextStratum();
        if (page == Page.EXTERIOR || page == Page.INTERIOR) renderPreview(graphics, delta);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    private void renderPreview(GuiGraphicsExtractor graphics, float delta) {
        int previewX = layout.x() + layout.width() - 112;
        int previewY = layout.y() + 28;
        int previewSize = 96;
        graphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, 0xFF0B1117);
        TardisExteriorRenderState preview = new TardisExteriorRenderState();
        preview.entityType = ModEntityTypes.TARDIS_EXTERIOR;
        preview.exterior = page == Page.EXTERIOR ? exteriorOptions().get(exteriorIndex).id().toString() : state.exterior();
        preview.interiorPreview = page == Page.INTERIOR;
        preview.interiorPreviewId = page == Page.INTERIOR ? interiorOptions().get(interiorIndex).toString() : state.interior();
        preview.doorOpen = previewTicks >= 40;
        float phase = (previewTicks % 40) / 40.0F;
        preview.doorProgress = preview.doorOpen ? phase : 1.0F - phase;
        preview.powered = state.powered();
        preview.travelState = "LANDED";
        preview.travelOpacity = 1.0F;
        preview.yaw = 180.0F + (previewTicks + delta) * 0.8F;
        preview.boundingBoxWidth = 1.5F;
        preview.boundingBoxHeight = 3.1F;
        preview.x = 0.0D;
        preview.y = 0.0D;
        preview.z = 0.0D;
        preview.eyeHeight = 1.6F;
        preview.distanceToCameraSq = 0.0D;
        preview.isInvisible = false;
        preview.isDiscrete = false;
        preview.shadowRadius = 0.0F;
        preview.lightCoords = 15728880;
        graphics.entity(preview, 24.0F, new Vector3f(previewX + 48.0F, previewY + 82.0F, 0.0F), new Quaternionf(), new Quaternionf(), previewX, previewY, previewX + previewSize, previewY + previewSize);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
