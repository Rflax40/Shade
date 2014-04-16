package shade.src.resource;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Resource {

    private static final ArrayList<Resource> resourcePool = new ArrayList<Resource>();
    public final URL handle;
    private boolean valid;
    protected Resource(String location) {
        handle = Thread.currentThread().getContextClassLoader().getResource(location);
        if (handle != null) {
            invalidate();
        }
    }

    public void invalidate() {
        valid = false;
    }

    public static Resource getResource(String location) {
        Resource newRes = new Resource(location);
        for (Resource r : resourcePool) {
            if (r.equals(newRes)) {
                return r;
            }
        }
        if (!newRes.isValid()) {
            return null;
        }
        resourcePool.add(newRes);
        return newRes;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Resource)) {
            return false;
        }
        Resource other = (Resource) o;
        try {
            return other.handle.toURI().equals(handle.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public InputStream getAsStream() {
        try {
            return handle.openStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String fileName() {
        return handle.getFile();
    }
}
