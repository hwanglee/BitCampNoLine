package classes;

public class DOB {
    private int day; //Give Possible options (count for leap years)
    private int month; //Always twelve options that an be represented in words
    private int year; // Current year Range | minus 35 and 18 (inclusive)


    /**
     * Days - could be checked in two ways.  (1) But creating a list and then selecting.
     * 	or (2) by entering a number and then checking if it's valid.
     * Month - will be selected by a list - it will always be right
     * Year - will always be right - provide the options from a list
     * @param day
     * @param month
     * @param year
     */
    public DOB(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }


    /*SETTERS*/
    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String toString() {
        return this.month + "/" + this.day + "/" + this.year;
    }

}//End Class
