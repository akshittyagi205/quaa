package com.quanutrition.app.selectiondialogs;

public class MultipleSelectionModel {
    String id,label;
    boolean selected;

    public MultipleSelectionModel(String id, String label, boolean selected) {
        this.id = id;
        this.label = label;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
