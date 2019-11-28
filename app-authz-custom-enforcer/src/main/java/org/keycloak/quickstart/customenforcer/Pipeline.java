package org.keycloak.quickstart.customenforcer;

public class Pipeline {

    private final String name;
    private String namespace;
    private Object resources;

    public Pipeline(String name,String namespace,Object resources) {
        this.name = name;
        this.namespace=namespace;
        this.resources=resources;
    }

    public String getNamespace() {
        return namespace;
    }

    public Object getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }
}
