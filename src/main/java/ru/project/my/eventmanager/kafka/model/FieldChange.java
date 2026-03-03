package ru.project.my.eventmanager.kafka.model;

public class FieldChange<T> {
    private T oldField;
    private T newField;

    public FieldChange(T oldField, T newField) {
        this.oldField = oldField;
        this.newField = newField;
    }

    public T getOldField() {
        return oldField;
    }
    public void setOldField(T oldField) {
        this.oldField = oldField;
    }

    public T getNewField() {
        return newField;
    }
    public void setNewField(T newField) {
        this.newField = newField;
    }

    @Override
    public String toString() {
        return "FieldChange{" +
                "oldField=" + oldField +
                ", newField=" + newField +
                '}';
    }
}
