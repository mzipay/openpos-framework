package org.jumpmind.jumppos.core.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jumpmind.jumppos.core.model.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicItemSearchScreen extends DefaultScreen {
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public enum ItemRetrievalMode {
        ALL,
        CALLBACK
    };
    
    public enum SearchCategoryStructure {
        FLAT,
        HIERARCHICAL
    };
    // For HIERARCHICAL list:
    // SearchCategoryStructure = HIERARCHICAL
    //  searchCategories = [ {ROOT,{name=top}}, {SUBCATEGORY,{name=level1}}, {SUBCATEGORY,{name=level2}} ]
    //  searchCategoryValues = [ {{name=top_item_1},[{{name=level1_item1},[]}]} ]
   
    // For FLAT list:
    // SearchCategoryStructure = FLAT
    //  searchCategories = [ {CATEGORY1,{name=cat1}}, {CATEGORY2,{name=cat2}}, {CATEGORY3,{name=cat3}} ]
    //  searchCategoryValues = [ {{name=cat1},[{name=item1},{name=item2},...]}, {{name=cat2},[{name=item1},{name=item2},...] ]
    
    public static final int MAX_SEARCH_CATEGORIES = 3;
    
    private ItemRetrievalMode itemRetrievalMode = ItemRetrievalMode.ALL;
    private SearchCategoryStructure searchCategoryStructure = SearchCategoryStructure.HIERARCHICAL;
    private List<SearchCategory> searchCategories = new ArrayList<>();
    private List<SearchCategoryValue> searchCategoryValues = new ArrayList<>();
    private Form searchFieldForm;
    
    public BasicItemSearchScreen() {
        this.setType(ScreenType.BasicItemSearch);
    }
    
    public void addSearchCategory(SearchCategory searchCategory) {
        this.searchCategories.add(searchCategory);
    }

    public void addSearchCategoryValue(SearchCategoryValue searchCategoryValue) {
        this.searchCategoryValues.add(searchCategoryValue);
    }
    
    public List<SearchCategory> getSearchCategories() {
        return Collections.unmodifiableList(this.searchCategories);
    }

    public List<SearchCategoryValue> getSearchCategoryValues() {
        return Collections.unmodifiableList(this.searchCategoryValues);
    }
    
    public ItemRetrievalMode getItemRetrievalMode() {
        return itemRetrievalMode;
    }

    public void setItemRetrievalMode(ItemRetrievalMode itemRetrievalMode) {
        this.itemRetrievalMode = itemRetrievalMode;
    }

    public SearchCategoryStructure getSearchCategoryStructure() {
        return searchCategoryStructure;
    }

    public void setSearchCategoryStructure(SearchCategoryStructure searchCategoryStructure) {
        this.searchCategoryStructure = searchCategoryStructure;
    }

    public Form getSearchFieldForm() {
        return searchFieldForm;
    }

    public void setSearchFieldForm(Form searchFieldForm) {
        this.searchFieldForm = searchFieldForm;
    }

    static public class SearchCategory {
        public static final String ATTR_NAME = "name";
        public enum SearchCategoryType {
            ROOT,
            SUBCATEGORY
        };
        
        private Map<String, Object> attributes = new HashMap<>();
        
        public void setAttribute(String attributeName, Object value) {
            this.attributes.put(attributeName, value);
        }
        
        public Object getAttribute(String attributeName) {
            return this.attributes.get(attributeName);
        }
    }
    
    static public class SearchCategoryValue {
        public static final String ATTR_NAME = "name";
        
        private Map<String, Object> attributes = new HashMap<>();
        private List<SearchCategoryValue> subCategoryValues = new ArrayList<>();
        
        public List<SearchCategoryValue> getSubCategoryValues() {
            return Collections.unmodifiableList(this.subCategoryValues);
        }
        
        public void addSubCategoryValue(SearchCategoryValue searchCategoryValue) {
            this.subCategoryValues.add(searchCategoryValue);
        }
        
        public void setAttribute(String attributeName, Object value) {
            this.attributes.put(attributeName, value);
        }
        
        public Object getAttribute(String attributeName) {
            return this.attributes.get(attributeName);
        }
       
    }
}
