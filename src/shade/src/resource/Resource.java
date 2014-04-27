package shade.src.resource;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Resource {

    private static final ArrayList<Resource> resourcePool = new ArrayList<>();
    public final URL handle;
    private boolean valid;

    private Resource(URL location) {
        handle = location;
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

    public OutputStream getAsOutputStream() {
        if (handle.getProtocol().equals("file"))
            try {
                return new FileOutputStream(handle.getPath().replaceAll("%20", " "));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        try {
            return handle.openConnection().getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Resource getResource(String location) {
        return getResource(new Resource(Thread.currentThread().getContextClassLoader().getResource(location)));
    }

    public static Resource getResource(File file) {
        URL url;
        try {
            url = new URL("file:///" + file.getAbsolutePath().replaceAll(" ", "%20"));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return getResource(new Resource(url));
    }

    public static void invalidateAll() {
        while(resourcePool.size() > 0)
            resourcePool.get(0).invalidate();
    }

    private static Resource getResource(Resource newRes) {
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
