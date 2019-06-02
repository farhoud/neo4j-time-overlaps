package interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.time.LocalDateTime;


import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

/**
 * This is an interval how you can create a simple user-defined function for Neo4j.
 */
public class IntervalOverlaps {
    @UserFunction
    @Description("interval.join(['s1','s2',...], delimiter) - join the given strings with the given delimiter.")
    public boolean overlaps(
            @Name("start1") LocalDateTime start1,
            @Name("end1") LocalDateTime end1,
            @Name("start2") LocalDateTime start2,
            @Name("end2") LocalDateTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        return this.overlap(start1, end1, start2, end2);
    }


    private boolean overlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() < end2.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()
                && start2.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() < end1.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }

    @UserFunction
    @Description("interval.join(['s1','s2',...], delimiter) - join the given strings with the given delimiter.")
    public Long intersectInMinutes(
            @Name("start1") LocalDateTime start1,
            @Name("end1") LocalDateTime end1,
            @Name("start2") LocalDateTime start2,
            @Name("end2") LocalDateTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return null;
        }
        return this.intersectMinutes(start1, end1, start2, end2);
    }


    private Long intersectMinutes(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        long epocmilliStart1 =  start1.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        long epocmilliEnd1 =  end1.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        long epocmilliStart2 =  start2.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        long epocmilliEnd2 =  end2.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
        if(this.overlap(start1,end1,start2,end2)){
            if (epocmilliStart1<=epocmilliStart2 && epocmilliEnd1>=epocmilliEnd2){
                return (epocmilliEnd2 - epocmilliStart2) / 60 / 1000;
            }
            if (epocmilliStart1>=epocmilliStart2 && epocmilliEnd1<=epocmilliEnd2){
                return (epocmilliEnd1 - epocmilliStart1) / 60/1000;
            }
            if (epocmilliStart1<=epocmilliStart2){
                return (epocmilliEnd1 -epocmilliStart2) / 60 / 1000;
            }
            return (epocmilliEnd2 -epocmilliStart1) / 60/1000;
        }
        return 0L;
    }


}