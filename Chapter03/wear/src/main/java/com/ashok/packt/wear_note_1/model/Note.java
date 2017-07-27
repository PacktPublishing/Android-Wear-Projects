package com.ashok.packt.wear_note_1.model;

import io.realm.RealmObject;

/**
 * Created by ashok.kumar on 15/02/17.
 */

public class Note extends RealmObject {

    private String notes = "";
    private String id = "";

    public Note() {

    }

    public Note(String id, String notes) {
        this.id = id;
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
