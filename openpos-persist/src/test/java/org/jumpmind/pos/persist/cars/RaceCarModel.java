package org.jumpmind.pos.persist.cars;

import org.jumpmind.pos.persist.Column;
import org.jumpmind.pos.persist.Table;

@Table(name="race_car")
public class RaceCarModel extends CarModel {

    private static final long serialVersionUID = 1L;
    
    @Column
    private boolean turboCharged;
    
    public void setTurboCharged(boolean turboCharged) {
        this.turboCharged = turboCharged;
    }
    
    public boolean isTurboCharged() {
        return turboCharged;
    }

}
