package org.jumpmind.pos.ops.model;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UnitStatusRepositoryTest {

    @Test
    public void testGetLatestUnitStatuses() throws Exception {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<UnitStatusModel> statuses = new ArrayList<>();
        statuses.add(new UnitStatusModel("0001","STORE", "OPEN", "2018-01-10", f.parse("2018-01-01 10:00:00")));
        statuses.add(new UnitStatusModel("0001","STORE", "CLOSED", "2018-01-10", f.parse("2018-01-01 11:00:00")));
        statuses.add(new UnitStatusModel("0001","STORE", "WINNER", "2018-01-10", f.parse("2018-01-01 12:00:00")));
        statuses.add(new UnitStatusModel("0001","STORE", "TOOEARLY", "2018-01-11", f.parse("2018-01-01 09:00:00")));
        
        statuses = UnitStatusRepository.getLatest(statuses);
        assertEquals(1, statuses.size());
        assertEquals("WINNER", statuses.get(0).getUnitStatus());
    }
}
