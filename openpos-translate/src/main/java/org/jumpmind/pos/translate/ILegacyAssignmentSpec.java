package org.jumpmind.pos.translate;

public interface ILegacyAssignmentSpec {
   String getBeanSpecName();
   
   String getPropertyValue(String propertyName);
}
