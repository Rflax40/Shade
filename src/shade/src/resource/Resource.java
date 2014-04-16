package shade.src.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class Resource {

    private static final ArrayList<Resource> resourcePool = new ArrayList<Resource>();
    public final URL handle;
    private boolean valid;

    private Resource(String location) {
        handle = Thread.currentThread().getContextClassLoader().getResource(location);
        if (handle != null) {
            valid = true;
        }
    }

    public void invalidate() {
        valid = false;
        resourcePool.remove(this);
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

    public String toString() {
        return handle.toString();
    }

    public InputStream getAsStream() {
        try {
            return handle.openStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public OutputStream getAsOutput() {
        try {
            return handle.openConnection().getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
}
