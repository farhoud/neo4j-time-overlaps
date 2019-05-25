package interval;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

/**
 * This is an interval how you can create a simple user-defined function for Neo4j.
 */
public class IntervalOverlaps
{
    @UserFunction
    @Description("interval.join(['s1','s2',...], delimiter) - join the given strings with the given delimiter.")
    public boolean overlaps(
            @Name("start1") String start1,
            @Name("end1") String end1,
            @Name("start2") String start2,
            @Name("end2") String end2 ) throws ParseException {
        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            return false;
        }
        Date startDate1 = this.parseDate(start1);
        Date endDate1 = this.parseDate(end1);
        Date startDate2 = this.parseDate(start2);
        Date endDate2 = this.parseDate(end2);
        return this.overlap(startDate1,endDate1,startDate2, endDate2);
    }

    private Date parseDate(String input) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return format.parse(input);
    }



    private boolean overlap(Date start1, Date end1, Date start2, Date end2){
        return start1.getTime() <= end2.getTime() && start2.getTime() <= end1.getTime();
    }


}