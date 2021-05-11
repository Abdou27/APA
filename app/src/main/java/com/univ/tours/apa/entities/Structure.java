package com.univ.tours.apa.entities;

public class Structure {
    private static Long uid = 1L;

    private Long id;
    private String name;
    private String discipline;
    private String pathologyList;

    public Structure() {
        setId(uid);
        uid++;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getPathologyList() {
        return pathologyList;
    }

    public void setPathologyList(String pathologyList) {
        this.pathologyList = pathologyList;
    }
}
