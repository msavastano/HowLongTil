package com.mikesavastano.howlongtil;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HolidayCalendar {

    public List<List<String>> holidays = new ArrayList<List<String>>();

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
        holidays.add(Arrays.asList("99", "99", "99", "99", "Memorial Day (US)"));
        holidays.add(Arrays.asList("99", "99", "6", "4", "Independence Day (US)"));
        //Ramadan
        holidays.add(Arrays.asList("1", "2", "8", "99", "Labor Day (US)"));
        holidays.add(Arrays.asList("99", "99", "9", "31", "Halloween (US)"));
        holidays.add(Arrays.asList("99", "99", "10", "11", "Veterans Day (US)"));
        holidays.add(Arrays.asList("4", "5", "10", "99", "Thanksgiving Day (US)"));
        //Hanaka
        //Quanza
        holidays.add(Arrays.asList("99", "99", "11", "25", "Christmas (US)"));
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
        if(date.get("name")=="Easter (US)"){
            return EasterSunday(nyear);
        }else if(date.get("name")=="Good Friday (US)"){
            return GoodFriday(EasterSunday(nyear));
        }else if(date.get("name")=="Memorial Day (US)"){
            return MemorialDay(nyear);
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

    public static void main(String [] args){
        HolidayCalendar h = new HolidayCalendar();
        System.out.println( h.holidays.get(0).get(4) );

        for(List<String> holiday : h.holidays){
            System.out.println(holiday.get(4));
            System.out.println(h.mapMaker(holiday));
            //System.out.println(xx.get(Calendar.DAY_OF_MONTH));
        }
    }

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
        /*int day = easter.get(Calendar.DAY_OF_MONTH);
        int dayminustwo = day - 2;
        switch(dayminustwo){
            case 0:
                gf.set(easter.get(Calendar.YEAR), 2, 31);
            case -1:
                gf.set(easter.get(Calendar.YEAR), 2, 30);
            default:
                gf.set(easter.get(Calendar.YEAR), easter.get(Calendar.MONTH), dayminustwo);
        }*/
        return gf;
    }

    public Calendar MemorialDay(int nyear){
        Calendar pCal = Calendar.getInstance();
        pCal.set(Calendar.YEAR, nyear);
        pCal.set(Calendar.MONTH, 4);
        pCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        pCal.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
        return pCal;
    }
}