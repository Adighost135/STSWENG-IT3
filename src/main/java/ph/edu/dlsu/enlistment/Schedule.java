package ph.edu.dlsu.enlistment;

import java.util.*;

record Schedule(Days days, Period period) {

    Schedule{
        Objects.requireNonNull(days);
        Objects.requireNonNull(period);
    }

    @Override
    public String toString(){
        // TF H0830, WS H1000
        return days + " " + period;
    }
}

enum Days {
    MTH, TF, WS
}

enum Period{
    H0830, H1000, H1130, H1300, H1430, H1600
}



