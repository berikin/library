package util;

/**
 * Fecha es una clase que implementa una date del calendario gregoriano
 */
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date {
    //Atributos

    private int day;//1 al 31
    private int month;//1 al 12
    private int year;//>=1582
    private String dayOfWeek;//
    //Constantes (Field Sumary)
    public static final int DAYOFMONTH = 0;
    public static final int MONTH = 1;
    public static final int YEAR = 2;
    //****************************CONSTRUCTORES********************************

    /**
     * Construye una Fecha usando la date actual del sistema
     */
    public Date() {
        java.util.GregorianCalendar today = new java.util.GregorianCalendar();
        day = today.get(Calendar.DAY_OF_MONTH);
        month = today.get(Calendar.MONTH) + 1;
        year = today.get(Calendar.YEAR);
        dayOfWeek = getDayOfWeek(today.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Construye una Fecha con la date pasada en los 3 argumentos. Si la date no
     * es correcta inicializa a cero todas las variables.
     */
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        GregorianCalendar date = new GregorianCalendar(year, month - 1, day);
        this.dayOfWeek = getDayOfWeek(date.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Construye una Fecha con la date pasada como un String con el formato
     * 'dd/mm/aaaa'.
     */
    public Date(String f) {
        String stringDay = f.substring(0, 2);
        String stringMonth = f.substring(3, 5);
        String stringYear = f.substring(6);
        day = Integer.parseInt(stringDay);
        month = Integer.parseInt(stringMonth);
        year = Integer.parseInt(stringYear);
        GregorianCalendar date = new GregorianCalendar(year, month - 1, day);
        this.dayOfWeek = getDayOfWeek(date.get(Calendar.DAY_OF_WEEK));
    }
    //************************ MÉTODOS PRIVADOS ********************************

    private String getDayOfWeek(int d) {
        switch (d) {
            case Calendar.MONDAY:
                return "lunes";
            case Calendar.TUESDAY:
                return "martes";
            case Calendar.WEDNESDAY:
                return "miércoles";
            case Calendar.THURSDAY:
                return "jueves";
            case Calendar.FRIDAY:
                return "viernes";
            case Calendar.SATURDAY:
                return "sabado";
            case Calendar.SUNDAY:
                return "domingo";
        }
        return "ERROR";

    }

    private static boolean isNumber(String n) {
        for (int i = 0; i < n.length(); i++) {
            if (!Character.isDigit(n.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static long difDaysBetweenDates(int d1, int m1, int y1, int d2, int m2, int y2) {
        m1--;
        m2--;
        java.util.GregorianCalendar date1 = new java.util.GregorianCalendar(y1, m1, d1);
        java.util.GregorianCalendar date2 = new java.util.GregorianCalendar(y2, m2, d2);
        long difms = date2.getTimeInMillis() - date1.getTimeInMillis();
        long difd = difms / (1000 * 60 * 60 * 24);
        return difd;
    }

    private boolean correctDay() {
        switch (month) {
            case 2:
                if (isLeap()) {
                    return (day >= 1 && day <= 29);
                } else {
                    return (day >= 1 && day <= 28);
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return (day >= 1 && day <= 30);
            default:
                return (day >= 1 && day <= 31);
        }
    }

    private boolean correctMonth() {
        return (month >= 1) && (month <= 12);
    }

    private boolean correctYear() {
        return (year >= 1582);
    }
    //************************ MÉTODOS PÚBLICOS ********************************
    //************MÉTODOS DE CLASE ************

    /**
     * Diferencia de días entre dos fechas
     *
     * @param f1 date menor
     * @param f2 date mayor
     * @return <ul> <li>entero<0 si f1 es la date mayor</li> <li>entero==0 si
     * las dos fechas son iguales</li> <li>entero>0 si f1 es la date menor</li>
     * </ul>
     *
     */
    public static long difDaysBetweenDates(Date f1, Date f2) {
        int d1, m1, a1, d2, m2, a2;
        d1 = f1.get(Date.DAYOFMONTH);
        m1 = f1.get(Date.MONTH);
        a1 = f1.get(Date.YEAR);
        d2 = f2.get(Date.DAYOFMONTH);
        m2 = f2.get(Date.MONTH);
        a2 = f2.get(Date.YEAR);
        return difDaysBetweenDates(d1, m1, a1, d2, m2, a2);
    }

    /**
     * Nº de años completos desde la date f hasta today. Si la date f es mayor
     * que la date de today, devuelve -1.
     *
     * @param f date cuya edad en años deseamos conocer
     * @return nº de años o -1
     */
    public static int getAge(Date f) {
        Date today = new Date();
        if (today.compareTo(f) < 0) {
            return -1;
        }
        if (today.get(YEAR) == f.get(YEAR)) {
            return 0;
        }
        if (today.get(MONTH) > f.get(MONTH)) {
            return today.get(YEAR) - f.get(YEAR);
        }
        if (today.get(MONTH) == f.get(MONTH) && today.get(DAYOFMONTH) >= f.get(DAYOFMONTH)) {
            return today.get(YEAR) - f.get(YEAR);
        }
        return today.get(YEAR) - f.get(YEAR) - 1;
    }

    /**
     * Dice si el String que se le pasa como parámetro se ajusta al formato
     * 'dd/mm/aaaa' y si además se corresponde con una date válida
     *
     * @param stringDate
     * @return true si el String se ajusta al formato 'dd/mm/aaaa' y si además
     * se corresponde con una date válida
     */
    public static boolean isValidDate(String stringDate) {
        if (stringDate.length() == 10) {
            String stringDay = stringDate.substring(0, 2);
            String separatorOne = stringDate.substring(2, 3);
            String stringMonth = stringDate.substring(3, 5);
            String separatorTwo = stringDate.substring(5, 6);
            String stringYear = stringDate.substring(6);
            if (isNumber(stringDay) && isNumber(stringMonth) && isNumber(stringYear) && separatorOne.equals("/") && separatorTwo.equals("/")) {
                Date f = new Date(Integer.parseInt(stringDay), Integer.parseInt(stringMonth), Integer.parseInt(stringYear));
                if (f.isValid()) {
                    return true;
                }
            }
        }
        return false;
    }

    //************MÉTODOS DE INSTANCIA ********
    /**
     * Cambiar los atributos del objeto Fecha con los valores de los argumentos
     *
     * @param day del 1 al 31 que representa el día del month
     * @param month nº del 1 al 12 que representa el month del year
     * @param year nº de cuatro cifras que representa el year
     */
    private void setDate(int day, int month, int year) {
        int auxD = day, auxM = month, auxA = year;
        this.day = day;
        this.month = month;
        this.year = year;
        if (!this.isValid()) {
            this.day = auxD;
            this.month = auxM;
            this.year = auxA;
        }
        GregorianCalendar date = new GregorianCalendar(year, month - 1, day);
        this.dayOfWeek = getDayOfWeek(date.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * Cambia los atributos day y month con los valores de los parámetros y el
     * atributo year con el valor de year de la date actual. Si el día y/o el
     * month no son correctos asigna a todos los atributos el valor 0.
     *
     * @param day nº del 1 al 31 que representa el día del month
     * @param month nº del 1 al 12 que representa el month del year
     */
    public void setDate(int day, int month) {
        java.util.Calendar today = new java.util.GregorianCalendar();
        int year = today.get(java.util.Calendar.YEAR);
        setDate(day, month, year);
    }

    /**
     * Cambia el atributo day con el valor del parámetro y los atributos year y
     * month con los valores year y month de la date actual. Si el día no es
     * correcto asigna a todos los atributos el valor 0.
     *
     * @param day nº del 1 al 31 que representa el día del month
     */
    public void setDate(int day) {
        java.util.Calendar today = new java.util.GregorianCalendar();
        int month = today.get(java.util.Calendar.MONTH) + 1;
        int year = today.get(java.util.Calendar.YEAR);
        setDate(day, month, year);
    }

    /**
     * Comprueba si el year es o no bisiesto
     *
     * @return true si el year es bisiesto y false en caso contrario
     */
    public boolean isLeap() {
        return ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
    }

    public boolean isValid() {
        return correctDay() && correctMonth() && correctYear();

    }

    public int get(int dataField) {
        switch (dataField) {
            case DAYOFMONTH:
                return day;
            case MONTH:
                return month;
            case YEAR:
                return year;
        }
        return -1;
    }

    public void set(int dataField, int value) {
        switch (dataField) {
            case DAYOFMONTH:
                day = value;
                break;
            case MONTH:
                month = value;
                break;
            case YEAR:
                year = value;
                break;
        }
        GregorianCalendar date = new GregorianCalendar(day, month - 1, year);
        dayOfWeek = getDayOfWeek(date.get(Calendar.DAY_OF_WEEK));
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Nº de años completos desde la date del objeto hasta today. Si la date del
     * objeto es mayor que la date de today, devuelve -1.
     *
     * @return nº de años o -1
     */
    public int getAge() {
        Date today = new Date();
        if (today.compareTo(this) < 0) {
            return -1;
        }
        if (today.get(YEAR) == this.get(YEAR)) {
            return 0;
        }
        if (today.get(YEAR) > this.get(YEAR) && today.get(MONTH) > this.get(MONTH)) {
            return today.get(YEAR) - this.get(YEAR);
        }
        if (today.get(YEAR) > this.get(YEAR) && today.get(MONTH) == this.get(MONTH) && today.get(DAYOFMONTH) >= this.get(DAYOFMONTH)) {
            return today.get(YEAR) - this.get(YEAR);
        }
        return today.get(YEAR) - this.get(YEAR) - 1;

    }

    public String getShortFormat() {
        if (isValid()) {
            String returnString;
            if (day < 10) {
                returnString = "0" + day;
            } else {
                returnString = "" + day;
            }
            if (month < 10) {
                returnString += "/0" + month;
            } else {
                returnString += "/" + month;
            }
            return returnString + "/" + year;
        }
        return null;
    }

    public String getLongFormat() {
        if (isValid()) {
            String[] months = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};
            return dayOfWeek + " " + day + " de " + months[month - 1] + " de " + year;
        }
        return null;
    }

    /**
     * Compara el objeto Fecha al que se le envía el mensaje con el objeto Fecha
     * del argumento.
     *
     * @param date Fecha - la date con la que será comparada
     * @return Devuelve cero si el argumento Fecha es igual que este objeto
     * Fecha; un valor menor que cero si este objeto Fecha es menor que el
     * objeto Fecha del argumento; un valor mayor que cero si el objeto Fecha
     * del argumento es mayor que este objeto Fecha.
     */
    public int compareTo(Date date) {
        if (this.year > date.get(Date.YEAR)) {
            return 1;
        }
        if (this.year < date.get(Date.YEAR)) {
            return -1;
        }
        if (this.month > date.get(Date.MONTH)) {
            return 1;
        }
        if (this.month < date.get(Date.MONTH)) {
            return -1;
        }
        if (this.day > date.get(Date.DAYOFMONTH)) {
            return 1;
        }
        if (this.day < date.get(Date.DAYOFMONTH)) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return day + "/" + month + "/" + year;
    }
}