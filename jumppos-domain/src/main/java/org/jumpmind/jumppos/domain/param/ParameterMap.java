package org.jumpmind.jumppos.domain.param;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents a complete collection of parameters organized by category,
 * subcategory, and name.
 * 
 * @author hanes
 * 
 */
public class ParameterMap {

    SortedMap<String, SortedMap<String, SortedMap<String, Parameter>>> params = new TreeMap<String, SortedMap<String, SortedMap<String, Parameter>>>();

    public ParameterMap() {

    }

    public SortedMap<String, SortedMap<String, Parameter>> getParameter(String category) {
        return params.get(category);
    }

    public SortedMap<String, Parameter> getParameter(String category, String subcategory) {
        if (getParameter(category) != null) {
            return getParameter(category).get(subcategory);
        } else {
            return null;
        }
    }

    public Parameter getParameter(String category, String subcategory, String name) {
        if (getParameter(category, subcategory) != null) {
            return getParameter(category, subcategory).get(name);
        } else {
            return null;
        }
    }

    public void put(Parameter p) {
        SortedMap<String, SortedMap<String, Parameter>> subcategories = getParameter(p.getCategory());

        if (subcategories == null) {
            subcategories = new TreeMap<String, SortedMap<String, Parameter>>();
            params.put(p.getCategory(), subcategories);
        }

        SortedMap<String, Parameter> names = subcategories.get(p.getSubcategory());
        if (names == null) {
            names = new TreeMap<String, Parameter>();
            subcategories.put(p.getSubcategory(), names);
        }

        names.put(p.getName(), p);
    }

    public Set<String> getCategories() {
        return params.keySet();
    }

    public Set<String> getSubcategories(String category) {
        SortedMap<String, SortedMap<String, Parameter>> tempSub = params.get(category);
        return tempSub == null ? null : tempSub.keySet();

    }

    public Set<String> getNames(String category, String subcategory) {
        SortedMap<String, SortedMap<String, Parameter>> tempSub = params.get(category);
        if (tempSub == null) {
            return null;
        }
        SortedMap<String, Parameter> tempName = tempSub.get(subcategory);
        return tempName == null ? null : tempName.keySet();
    }

    public int getCategoriesCount() {
        return params.size();
    }

    public int getSubcategoriesCount(String category) {
        Set<String> temp = getSubcategories(category);
        return temp == null ? null : temp.size();

    }

    public int getNamesCount(String category, String subcategory) {
        Set<String> temp = getNames(category, subcategory);
        return temp == null ? null : temp.size();
    }
}
