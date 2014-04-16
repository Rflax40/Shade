package shade.src.resource;

import java.util.ArrayList;

public abstract class ResourceBased {

    public static final ArrayList<ResourceBased> resourceBasedPool = new ArrayList<>();
    protected final Resource resource;

    protected ResourceBased(Resource res) {
        resource = res;
        resourceBasedPool.add(this);
    }

    public void unload() {
        System.out.println("Resource Removed: " + resource);
        resourceBasedPool.remove(this);
        resource.invalidate();
    }

    public static void unloadResources() {
        while (!resourceBasedPool.isEmpty()) {
            ResourceBased based = resourceBasedPool.get(0);
            based.unload();
        }
    }

    public static void unloadResources(Class type) {
        for (int i = 0; i < resourceBasedPool.size(); i++) {
            ResourceBased based = resourceBasedPool.get(i);
            if (type.isInstance(based)) {
                based.unload();
            }
        }
    }

    protected static ResourceBased getResourceBased(Resource resource, Factory factory) {
        for (ResourceBased based : resourceBasedPool) {
            if (based.getClass().equals(factory.getClassType())) {
                if (based.resource.equals(resource)) {
                    return based;
                }
            }
        }
        ResourceBased newBased = factory.construct(resource);
        return newBased;
    }

    protected static interface Factory {

        public ResourceBased construct(Resource resource);

        public Class getClassType();
    }
}
