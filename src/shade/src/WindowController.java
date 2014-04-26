package shade.src;

import shade.src.resource.Resource;
import shade.src.resource.ResourceBased;

public abstract class WindowController {

    public Window window;
    /**
     * In milliseconds
     */
    public int updateInterval;
    /**
     * -1 for no cap
     */
    public int fpsCap = -1;
    public boolean renderAfterUpdate;
    private volatile boolean running;

    public WindowController(String t) {
        this(new Window(t));
    }

    public WindowController(Window iw) {
        window = iw;
        window.setWindowController(this);
    }

    public void onCloseRequest() {
        requestClose();
    }

    public void requestClose() {
        running = false;
    }

    public void start() {
        try {
            preStartup();
            running = true;
            long lr = System.currentTimeMillis();
            long lu = lr;
            while (isRunning()) {
                boolean updated = false;
                if (shouldUpdate(lu)) {
                    int d = (int) (System.currentTimeMillis() - lu);
                    lu = System.currentTimeMillis();
                    update(d);
                    updated = true;
                }
                if ((updated && renderAfterUpdate) || shouldRender(lr)) {
                    int d = (int) (System.currentTimeMillis() - lr);
                    lr = System.currentTimeMillis();
                    render(d);
                }
                window.update();
            }
        } catch (Exception e) {
            handleError(e);
        } finally {
            shutdown();
        }
    }

    public void handleError(Exception e) {
        e.printStackTrace();
    }

    public boolean isRunning() {
        return running;
    }

    public void preStartup() throws Exception {
        window.open();
    }

    public abstract void render(int delta);

    public boolean shouldRender(long last) {
        if (fpsCap < 1)
            return true;
        double millis = 1000D / fpsCap;
        return System.currentTimeMillis() - last >= millis;
    }

    public boolean shouldUpdate(long last) {
        if (last == 0) {
            last = System.currentTimeMillis();
        }
        long l = System.currentTimeMillis();
        int interval = (int) (l - last);
        return interval >= updateInterval;
    }

    public void shutdown() {
        ResourceBased.unloadResources();
        Resource.invalidateAll();
        window.close();
    }

    public abstract void update(int delta);
}