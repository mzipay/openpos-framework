package org.jumpmind.pos.ops.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jumpmind.pos.persist.DBSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn(value = { "OpsModule" })
public class UnitStatusRepository {

    @Autowired
    @Qualifier("opsSession")
    @Lazy
    DBSession dbSession;
    
    
    public List<UnitStatusModel> findUnitStatus(String unitType, String unitId) {
        Map<String, Object> params = new HashMap<>();
        params.put("unitType", unitType);
        if (!"*".equals(unitId)) {
            params.put("unitId", unitId);
        }
        List<UnitStatusModel> list = dbSession.findByFields(UnitStatusModel.class, params);        
        return getLatest(list);
    }
    
    public void saveUnitStatus(UnitStatusModel unitStatus) {
        dbSession.save(unitStatus);
    }
    
    protected static List<UnitStatusModel> getLatest(List<UnitStatusModel> list) {
        list = list.stream().sorted((o1, o2) -> -o1.getCreateTime().compareTo(o2.getCreateTime())).collect(Collectors.toList());
        Iterator<UnitStatusModel> i = list.iterator();
        Set<String> ids = new HashSet<>();
        while (i.hasNext()) {
            UnitStatusModel unitStatus = i.next();
            if (ids.contains(unitStatus.getUnitId())) {
                i.remove();
            } else {
                ids.add(unitStatus.getUnitId());
            }            
        }
        return list;        
    }
}
