package ph.edu.dlsu.enlistment;

import java.util.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.Validate;

record Schedule(Days days, String periodStart, String periodEnd) {

    Schedule{
        Objects.requireNonNull(days);
        Objects.requireNonNull(periodStart);
        Objects.requireNonNull(periodEnd);
        Validate.isTrue(NumberUtils.isDigits(periodStart), "Period Start must be numeric, was: " + periodStart);
        Validate.isTrue(NumberUtils.isDigits(periodEnd), "Period End must be numeric, was: " + periodEnd);
        if(Integer.parseInt(periodStart) % 100 != 0 && Integer.parseInt(periodStart) % 100 != 30) throw new IllegalArgumentException(
                "Period does not start at the top or bottom of each hour, was" + periodStart);

        if(Integer.parseInt(periodEnd) % 100 != 0 && Integer.parseInt(periodEnd) % 100 != 30) throw new IllegalArgumentException(
                "Period does not end at the top or bottom of each hour, was" + periodEnd );

        if(Integer.parseInt(periodStart) >= Integer.parseInt(periodEnd)) throw new IllegalArgumentException(
            "Start of the period should not be the same or exceed the end of the period");

        if(Integer.parseInt(periodStart) < 830 || Integer.parseInt(periodStart) > 1730) throw new IllegalArgumentException(
            "Start of the period should not be outside the bounds of 0830 to 1730");

        if(Integer.parseInt(periodEnd) < 830 || Integer.parseInt(periodEnd) > 1730) throw new IllegalArgumentException(
            "End of the period should not be outside the bounds of 0830 to 1730");
    }

    @Override
    public String toString(){
        // TF H0830, WS H1000
        return days + " " + periodStart + "-" + periodEnd;
    }

    @Override
    public final boolean equals(Object o){
        if(this == o)return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        int thisStart = Integer.parseInt(this.periodStart);
        int thisEnd = Integer.parseInt(this.periodEnd);
        int otherStart = Integer.parseInt(schedule.periodStart);
        int otherEnd = Integer.parseInt(schedule.periodEnd);
        if (this.days == schedule.days){
            if(thisStart < otherEnd && thisEnd > otherStart){
                return true;
            }
            if(!(thisStart > otherEnd || otherStart > thisEnd)){
                return true;
            }
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

