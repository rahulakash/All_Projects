package wireless.uta.com.airport_assist;

/**
 * Created by Rahul on 5/5/2015.
 */
public enum Status {
    ACTIVE,
    CANCELLED,
    DIVERTED,
    DATA_SOURCE_NEEDED,
    LANDED,
    NOT_OPERATIONAL,
    REDIRECTED,
    SCHEDULED,
    UNKNOWN;

    public static Status get(String code){
        switch(code) {
            case "A": return ACTIVE;
            case "C": return CANCELLED;
            case "D": return DIVERTED;
            case "DN": return DATA_SOURCE_NEEDED;
            case "L": return LANDED;
            case "NO": return NOT_OPERATIONAL;
            case "R": return REDIRECTED;
            case "S": return SCHEDULED;
            case "U": return UNKNOWN;
            default: return null;
        }
    }


}

