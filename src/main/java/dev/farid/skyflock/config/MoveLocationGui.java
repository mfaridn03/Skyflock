package dev.farid.skyflock.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.farid.skyflock.utils.RenderUtils;
import gg.essential.api.utils.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.util.Collections;
import java.util.List;

import static dev.farid.skyflock.Skyflock.printLogs;

public class MoveLocationGui extends GuiScreen {

    public final String SAVE_FILE_PATH = Loader.instance().getConfigDir() + File.separator + "skyflock.json";
    public String name;
    public int x, y;
    private final List<String> texts;
    private GuiScreen lastOpenedGui;
    private boolean dirty = false;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .serializeSpecialFloatingPointValues()
            .create();

    public MoveLocationGui(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.texts = Collections.singletonList("Placeholder");
        load();
    }

    public MoveLocationGui(String name, int x, int y, List<String> texts) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.texts = texts;
        load();
    }

    public void open() {
        this.lastOpenedGui = GuiUtil.getOpenedScreen();
        GuiUtil.open(this);
    }

    private void write() {
        if (!this.dirty)
            return;
        File file = new File(SAVE_FILE_PATH);

        try (FileWriter writer = new FileWriter(file.getPath())) {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created)
                    printLogs(null, "Couldn't create config file [" + this.name + "]", false);
            }

            JsonObject j = new JsonObject();
            j.addProperty("name", this.name);
            j.addProperty("x", this.x);
            j.addProperty("y", this.y);

            String json = gson.toJson(j);
            writer.write(json);
            writer.close();

            this.dirty = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        printLogs(null, "Saved Gui location at " + SAVE_FILE_PATH +  "[" + this.name + "]", false);
    }

    private void load() {
        File file = new File(SAVE_FILE_PATH);
        if (!file.exists()) {
            printLogs(null, "Writing default locations at " + SAVE_FILE_PATH + "[" + this.name + "]", false);
            // Create file and write default data
            write();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            reader.close();

            if (jsonObject != null) {
                this.name = jsonObject.get("name").getAsString();
                this.x = jsonObject.get("x").getAsInt();
                this.y = jsonObject.get("y").getAsInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        printLogs(null, "Loaded Gui location at " + SAVE_FILE_PATH +  "[" + this.name + "]", false);
    }

    public void setLocations(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void onGuiClosed() {
        // save config
        this.dirty = true;
        write();

        if (this.lastOpenedGui != null)
            GuiUtil.open(this.lastOpenedGui);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        // horizontal
        RenderUtils.Render2D.drawLine(0f, (float) this.y, (float) sr.getScaledWidth(), this.y, 1f);
        // vertical
        RenderUtils.Render2D.drawLine((float) this.x, 0f, (float) this.x, sr.getScaledHeight(), 1f);

        int yOffset = 0;
        for (String s : this.texts) {
            fr.drawString(s, this.x + 2, this.y + 3 + yOffset, 0xffffff);
            yOffset += fr.FONT_HEIGHT;
        }
        fr.drawString("x: " + this.x, this.x + 2, this.y + 3 + yOffset, 0xffffff);
        yOffset += fr.FONT_HEIGHT;
        fr.drawString("y: " + this.y, this.x + 2, this.y + 3 + yOffset, 0xffffff);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0)
            setLocations(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0)
            setLocations(mouseX, mouseY);
    }
}
