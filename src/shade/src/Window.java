package shade.src;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

    public final String windowTitle;
    private WindowController windowController;

    private boolean full = false;

    public Window(String wt) {
        windowTitle = wt;
    }

    public int getHeight() {
        return Display.getDisplayMode().getHeight();
    }

    public String getRenderContext() {
        return "GL";
    }

    public int getWidth() {
        return Display.getDisplayMode().getWidth();
    }

    public boolean hasFocus() {
        return Display.isActive();
    }

    public void open() throws Exception {
        open(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), full);
    }

    public void open(int w, int h, boolean f) throws Exception {
        resize(w, h, f);
        Display.setTitle(windowTitle);
        Display.create();
        boolean fu = f;
        Display.setFullscreen(fu);
    }

    public void resize(int w, int h, boolean f) throws Exception {
        for (DisplayMode d : Display.getAvailableDisplayModes()) {
            if (d.getWidth() == w && d.getHeight() == h) {
                if (f && !d.isFullscreenCapable()) {
                    continue;
                }
                Display.setDisplayMode(d);
                full = f;
                if (isOpen()) {
                    Display.setFullscreen(f);
                }
                return;
            }
        }
        throw new Exception(String.format("No such display mode: %dx%d %s fullscreen", w, h, "with" + (f? "" : "out")));
    }

    public boolean isOpen() {
        return Display.isCreated();
    }

    public void setWindowController(WindowController w) {
        windowController = w;
    }

    public void update() {
        Display.sync(windowController.fpsCap);
        Display.update();
        if (windowController != null) {
            if (closeRequested()) {
                windowController.onCloseRequest();
            }
        } else if (closeRequested()) {
            close();
        }
    }

    public void close() {
        Display.destroy();
    }

    public boolean closeRequested() {
        return Display.isCloseRequested();
    }
}