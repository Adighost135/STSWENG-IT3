package ph.edu.dlsu.enlistment;

import java.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.Validate;

record Schedule(Days days, String periodStart, String periodEnd) {

    Schedule{
        Objects.requireNonNull(days);
        Objects.requireNonNull(periodStart);
        Objects.requireNonNull(periodEnd);
        Validate.isTrue(NumberUtils.isDigits(periodStart), "Period must be numeric, was: " + periodStart);
        Validate.isTrue(NumberUtils.isDigits(periodEnd), "Period must be numeric, was: " + periodEnd);
    }

    @Override
    public String toString(){
        // TF H0830, WS H1000
        return days + " " + periodStart + "-" + periodEnd;
    }

    @Override
    public final boolean equals(Object o){
        if(){
            return true;
        }
        return false;
    }
}

enum Days {
    MTH, TF, WS
}

//enum Period{
//    H0830, H1000, H1130, H1300, H1430, H1600
//}

