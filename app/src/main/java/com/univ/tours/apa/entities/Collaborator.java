package com.univ.tours.apa.entities;

public class Collaborator {
    private static Long collaboratorUid = 1L;

    private Long collaboratorId;

    public Collaborator() {
        super();
        setCollaboratorId(collaboratorUid);
        collaboratorUid++;
    }

    public Long getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(Long collaboratorId) {
        this.collaboratorId = collaboratorId;
    }
}
