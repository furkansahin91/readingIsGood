package com.getir.entity;

import org.hibernate.envers.RevisionListener;

public class MyRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionInfo revision = (RevisionInfo) revisionEntity;
        revision.fixTimezone();
    }
}