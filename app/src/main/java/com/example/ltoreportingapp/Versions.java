package com.example.ltoreportingapp;

public class Versions {

    private String codeName, version, description;

    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Versions(String codeName, String version, String aplilevel, String description) {
        this.codeName = codeName;
        this.version = version;
        this.description = description;
        this.expandable = false;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return  "Versions{" + "codeName='" + codeName + ", Answer='" + version + ", description='" + description +'\'' + '}';
    }
}
