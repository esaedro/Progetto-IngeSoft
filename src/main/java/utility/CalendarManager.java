package utility;

import java.time.Month;
import java.util.Calendar;

public class CalendarManager {
    protected Month meseAttuale;
    
    public Month meseDiLavoro() {
        int giornoDelMese = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (giornoDelMese > 15) {
            return meseAttuale;
        } else {
            return meseAttuale.plus(1);
        }
    }
}
