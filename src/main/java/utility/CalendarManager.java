package utility;

import java.time.Month;
import java.util.Calendar;

public class CalendarManager {
    protected static Month meseAttuale = Month.of(Calendar.getInstance().get(Calendar.MONTH) + 1);

    public static Month meseDiLavoro(int offset) {
        int giornoDelMese = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (giornoDelMese > 15 && offset > 0) {
            return meseAttuale.plus(offset);
        } else {
            return meseAttuale.plus(offset-1);
        }
    }
}
