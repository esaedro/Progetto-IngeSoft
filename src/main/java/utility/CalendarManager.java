package utility;

import java.time.Month;
import java.time.Year;
import java.util.AbstractMap;
import java.util.Calendar;

public class CalendarManager {

    protected static Month meseAttuale = Month.of(Calendar.getInstance().get(Calendar.MONTH) + 1);

    public static Month meseDiLavoro(int offset) {
        int giornoDelMese = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (giornoDelMese > 15 && offset > 0) {
            return meseAttuale.plus(offset);
        } else {
            return meseAttuale.plus(offset - 1);
        }
    }

    public static Year annoCorrente() {
        return Year.of(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static AbstractMap.SimpleImmutableEntry<Calendar, Calendar> getInizioFineMeseVisite() {
        Calendar calInizioMese = Calendar.getInstance();
        calInizioMese.add(Calendar.MONTH, 1);
        calInizioMese.set(Calendar.DAY_OF_MONTH, 1);
        calInizioMese.set(Calendar.HOUR_OF_DAY, 0);
        calInizioMese.set(Calendar.MINUTE, 0);
        calInizioMese.set(Calendar.SECOND, 0);
        calInizioMese.set(Calendar.MILLISECOND, 0);

        Calendar calFineMese = Calendar.getInstance();
        calFineMese.add(Calendar.MONTH, 1);
        calFineMese.set(Calendar.DAY_OF_MONTH, calFineMese.getMaximum(Calendar.DAY_OF_MONTH));
        calFineMese.set(Calendar.HOUR_OF_DAY, 23);
        calFineMese.set(Calendar.MINUTE, 59);
        calFineMese.set(Calendar.SECOND, 59);
        calFineMese.set(Calendar.MILLISECOND, 999);

        return new AbstractMap.SimpleImmutableEntry<>(calInizioMese, calFineMese);
    }
}
