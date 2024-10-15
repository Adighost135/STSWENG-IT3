package ph.edu.dlsu.enlistment;

import java.util.*;

record Schedule(Days days, int timeStart, int timeEnd) {

    Schedule{
        Objects.requireNonNull(days);
        Objects.requireNonNull(timeStart);
        Objects.requireNonNull(timeEnd);
        if(timeStart % 100 != 0 && timeStart % 100 != 30) throw new IllegalArgumentException(
                "Period does not start at the top or bottom of each hour, was" + timeStart);

        if(timeEnd % 100 != 0 && timeEnd % 100 != 30) throw new IllegalArgumentException(
                "Period does not end at the top or bottom of each hour, was" + timeEnd );

        if(timeStart >= timeEnd) throw new IllegalArgumentException(
            "Start of the period should not be the same or exceed the end of the period");

        if(timeStart < 830 || timeStart > 1730) throw new IllegalArgumentException(
            "Start of the period should not be outside the bounds of 0830 to 1730");

        if(timeEnd < 830 || timeEnd > 1730) throw new IllegalArgumentException(
            "End of the period should not be outside the bounds of 0830 to 1730");
    }

    public boolean isOverlap(Schedule schedule){
        int thisStart = this.timeStart;
        int thisEnd = this.timeEnd;
        int otherStart = schedule.timeStart;
        int otherEnd = schedule.timeEnd;
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

    @Override
    public String toString(){
        // TF H0830, WS H1000
        return days + ": " + timeStart + "-" + timeEnd;
    }

    @Override
    public final boolean equals(Object o){
        if(this == o)return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule that = (Schedule) o;
        if (this.days == that.days){
            if(this.timeStart != that.timeStart) return false;
            return this.timeEnd == that.timeEnd;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (int) days.hashCode() * timeStart * timeEnd;
    }
}

enum Days {
    MTH, TF, WS
}

//enum Period{
//    H0830, H1000, H1130, H1300, H1430, H1600
//}

