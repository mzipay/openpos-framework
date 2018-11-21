package org.jumpmind.pos.devices.javapos;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import org.jumpmind.pos.util.ContextWrapper;
import org.springframework.context.ApplicationContext;

import jpos.config.JposEntry;
import jpos.config.simple.AbstractRegPopulator;

public class AlternateRegPopulator extends AbstractRegPopulator {

    public AlternateRegPopulator() {
        super(AlternateRegPopulator.class.getName());
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public void save(@SuppressWarnings("rawtypes") Enumeration entries) throws Exception {
    }

    @Override
    public void save(@SuppressWarnings("rawtypes") Enumeration entries, String fileName) throws Exception {
    }

    @Override
    public void load() {
        ApplicationContext context = ContextWrapper.getContext();
        Hashtable<String, JposEntry> entries = this.getJposEntries();
    }

    @Override
    public void load(String fileName) {
        this.load();
    }

    @Override
    public URL getEntriesURL() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

}
