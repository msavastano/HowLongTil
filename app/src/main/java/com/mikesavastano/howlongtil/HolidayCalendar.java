package com.mikesavastano.howlongtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.ibm.icu.util.*;



public class HolidayCalendar {

    public List<List<String>> holidays = new ArrayList<>();

    public HolidayCalendar() {
        holidays.add(Arrays.asList("99", "99", "1", "0", "New Years Day"));
        holidays.add(Arrays.asList("3", "2", "0", "99", "Martin Luther King Day (US)"));
        holidays.add(Arrays.asList("99", "99", "1", "14", "Valentine's Day"));
        holidays.add(Arrays.asList("3", "2", "1", "99", "President's Day (US)"));
        holidays.add(Arrays.asList("99", "99", "2", "17", "St. Patrick's Day (US)"));
        //Passover
        holidays.add(Arrays.asList("99", "99", "99", "99", "Good Friday (US)"));
        holidays.add(Arrays.asList("99", "99", "99", "99", "Easter (US)"));
        holidays.add(Arrays.asList("99", "99", "3", "15", "Tax Day (US)"));
        holidays.add(Arrays.asList("99", "99", "4", "5", "Cinco de Mayo"));
        holidays.add(Arrays.asList("2", "1", "4", "99", "Mother's Day (US)"));
        holidays.add(Arrays.asList("-1", "2", "4", "99", "Memorial Day (US)"));
        holidays.add(Arrays.asList("99", "99", "6", "4", "Independence Day (US)"));
        holidays.add(Arrays.asList("99", "99", "99", "99", "Eid al-Fitr"));
        //Ramadan
        holidays.add(Arrays.asList("1", "2", "8", "99", "Labor Day (US)"));
        holidays.add(Arrays.asList("99", "99", "9", "31", "Halloween (US)"));
        holidays.add(Arrays.asList("99", "99", "10", "11", "Veterans Day (US)"));
        holidays.add(Arrays.asList("4", "5", "10", "99", "Thanksgiving Day (US)"));
        holidays.add(Arrays.asList("99", "99", "99", "99", "Eid al-Adha"));
        //Hanaka
        holidays.add(Arrays.asList("99", "99", "11", "25", "Christmas (US)"));
        holidays.add(Arrays.asList("99", "99", "11", "26", "Kwanzaa"));
    }

    public Map mapMaker(final List<String> hol){
        Map<String, String> w = new HashMap<String, String>(){{
            put("weekOfMonth", hol.get(0));
            put("dayOfWeek", hol.get(1));
            put("month", hol.get(2));
            put("dayOfMonth", hol.get(3));
            put("name", hol.get(4));
        }};
        return w;
    }

    public Calendar calcHoliday (Map<String, String> date, int nyear) {
        Calendar hol = Calendar.getInstance();
        if(date.get("name")=="Eid al-Adha"){
            return EidAlAdha(nyear);
        }else if(date.get("name")=="Eid al-Fitr"){
            return EidAlFitr(nyear);
        }else if(date.get("name")=="Easter (US)"){
            return EasterSunday(nyear);
        }else if(date.get("name")=="Good Friday (US)"){
            return GoodFriday(EasterSunday(nyear));
        }else if(Integer.valueOf(date.get("dayOfMonth")) == 99){
            Integer week = Integer.valueOf(date.get("weekOfMonth")); //week of month
            Integer day = Integer.valueOf(date.get("dayOfWeek"));  //day of week
            Integer month = Integer.valueOf(date.get("month")); //month
            int dte = getHolDay(week, day, month, nyear);
            hol.set(nyear, month, dte);
        }else{
            Integer dayofmonth = Integer.valueOf(date.get("dayOfMonth"));
            hol.set(nyear, Integer.valueOf(date.get("month")), dayofmonth);
        }
        return hol;
    }

    private int getHolDay(int week, int day, int month, int year){
        Calendar d = Calendar.getInstance();
        d.set(Calendar.YEAR, year);
        d.set(Calendar.MONTH, month);
        d.set(Calendar.DAY_OF_WEEK, day);
        d.set(Calendar.DAY_OF_WEEK_IN_MONTH, week);
        return d.get(Calendar.DAY_OF_MONTH);
    }


    //http://core0.staticworld.net/downloads/idge/imported/article/jvw/1998/01/holidays.java
    public static Calendar EasterSunday(int nYear)
    {
        Calendar easter = Calendar.getInstance();

        int nEasterMonth;
        int nEasterDay;
        // Calculate Easter

        int nA = nYear % 19;
        int nB = nYear / 100;
        int nC = nYear % 100;
        int nD = nB / 4;
        int nE = nB % 4;
        int nF = (nB + 8) / 25;
        int nG = (nB - nF + 1) / 3;
        int nH = (19 * nA + nB - nD - nG + 15) % 30;
        int nI = nC / 4;
        int nK = nC % 4;
        int nL = (32 + 2 * nE + 2 * nI - nH - nK) % 7;
        int nM=  (nA + 11 * nH + 22 * nL) / 451;
        int nP;

        //  [3=March, 4=April]
        nEasterMonth = (nH + nL - 7 * nM + 114) / 31;
        --nEasterMonth;
        nP = (nH + nL - 7 * nM + 114) % 31;

        // Date in Easter Month.
        nEasterDay = nP + 1;

        easter.set(nYear, nEasterMonth, nEasterDay);
        // Populate the date object...
        return easter;
    }

    public Calendar GoodFriday(Calendar easter){
        Calendar gf = easter;
        gf.add(Calendar.DAY_OF_YEAR, -2);

        return gf;
    }

    public static void main(String [] args){
        HolidayCalendar h = new HolidayCalendar();
        //System.out.println(h.EidAlFitr(2016));
        System.out.println(EidAlFitr(2018).getTime());
    }

    //http://www.coderanch.com/t/534271/java/java/Gregorian-Hijri-Dates-Converter-JAVA
    static double gmod(double n,double  m) {
        return ((n % m) + m) % m;
    }

    static double[] kuwaiticalendar(boolean adjust, int nyear) {
        Calendar today = Calendar.getInstance();
        int adj=0;
        if(adjust){
            adj=0;
        }else{
            adj=1;
        }

        if (adjust) {
            int adjustmili = 1000 * 60 * 60 * 24 * adj;
            long todaymili = today.getTimeInMillis() + adjustmili;
            today.setTimeInMillis(todaymili);
        }
        double day = today.get(Calendar.DAY_OF_MONTH);
        double  month = today.get(Calendar.MONTH);
        double  year = nyear;

        double  m = month + 1;
        double  y = year;
        if (m < 3) {
            y -= 1;
            m += 12;
        }

        double a = Math.floor(y / 100.);
        double b = 2 - a + Math.floor(a / 4.);

        if (y < 1583)
            b = 0;
        if (y == 1582) {
            if (m > 10)
                b = -10;
            if (m == 10) {
                b = 0;
                if (day > 4)
                    b = -10;
            }
        }

        double jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day
                + b - 1524;

        b = 0;
        if (jd > 2299160) {
            a = Math.floor((jd - 1867216.25) / 36524.25);
            b = 1 + a - Math.floor(a / 4.);
        }
        double bb = jd + b + 1524;
        double cc = Math.floor((bb - 122.1) / 365.25);
        double dd = Math.floor(365.25 * cc);
        double ee = Math.floor((bb - dd) / 30.6001);
        day = (bb - dd) - Math.floor(30.6001 * ee);
        month = ee - 1;
        if (ee > 13) {
            cc += 1;
            month = ee - 13;
        }
        year = cc - 4716;

        double wd = gmod(jd + 1, 7) + 1;

        double iyear = 10631. / 30.;
        double epochastro = 1948084;
        double epochcivil = 1948085;

        double shift1 = 8.01 / 60.;

        double z = jd - epochastro;
        double cyc = Math.floor(z / 10631.);
        z = z - 10631 * cyc;
        double j = Math.floor((z - shift1) / iyear);
        double iy = 30 * cyc + j;
        z = z - Math.floor(j * iyear + shift1);
        double im = Math.floor((z + 28.5001) / 29.5);
        if (im == 13)
            im = 12;
        double id = z - Math.floor(29.5001 * im - 29);

        double[]  myRes = new double[8];

        myRes[0] = day; // calculated day (CE)
        myRes[1] = month - 1; // calculated month (CE)
        myRes[2] = year; // calculated year (CE)
        myRes[3] = jd - 1; // julian day number
        myRes[4] = wd - 1; // weekday number
        myRes[5] = id; // islamic date
        myRes[6] = im - 1; // islamic month
        myRes[7] = iy; // islamic year

        return myRes;
    }
    static Calendar EidAlFitr(int nyear) {

        boolean dayTest = false;
        double[] iDate = kuwaiticalendar(dayTest, nyear);

        IslamicCalendar dtIslamic = new com.ibm.icu.util.IslamicCalendar((int)iDate[7],  9,  1);

        Long milliTime = dtIslamic.getTimeInMillis(); //.getTime()+"  "+Double.toString(iDate[6])+ " "+Double.toString(iDate[5])+" " +Double.toString(iDate[7]);
        Calendar tempc = Calendar.getInstance();
        tempc.setTimeInMillis(milliTime);
        return tempc;
    }

    static Calendar EidAlAdha(int nyear) {

        boolean dayTest = false;
        double[] iDate = kuwaiticalendar(dayTest, nyear);

        IslamicCalendar dtIslamic = new com.ibm.icu.util.IslamicCalendar((int)iDate[7],  11,  10);

        Long milliTime = dtIslamic.getTimeInMillis(); //.getTime()+"  "+Double.toString(iDate[6])+ " "+Double.toString(iDate[5])+" " +Double.toString(iDate[7]);
        Calendar tempc = Calendar.getInstance();
        tempc.setTimeInMillis(milliTime);
        return tempc;
    }
}
