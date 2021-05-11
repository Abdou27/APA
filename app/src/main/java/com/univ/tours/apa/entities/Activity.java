package com.univ.tours.apa.entities;

public class Activity {
    private static Long uid = 1L;

    private Long id;
    private String title;
    private String description;
    private Structure structure;

    public Activity() {
        setId(uid);
        uid++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
