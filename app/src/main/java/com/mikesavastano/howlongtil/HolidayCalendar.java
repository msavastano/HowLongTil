package com.mikesavastano.howlongtil;

import java.util.Calendar;



public class HolidayCalendar {

    public static Calendar NewYears (int nYear)  {
        // January 1st
        Calendar newYears = Calendar.getInstance();
        newYears.set(nYear, Calendar.JANUARY, 1);
        return newYears;
    }

    public Calendar MartinLutherKing (int nYear) {
        // Third Monday in January
        Calendar mlkDay = Calendar.getInstance();
        mlkDay.set(nYear, Calendar.JANUARY, 1);
        int day = mlkDay.get(Calendar.DAY_OF_WEEK);
        switch(day){
            case 1:
                mlkDay.set(nYear, Calendar.JANUARY, 16);
            case 2:
                mlkDay.set(nYear, Calendar.JANUARY, 15);
            case 3:
                mlkDay.set(nYear, Calendar.JANUARY, 21);
            case 4:
                mlkDay.set(nYear, Calendar.JANUARY, 20);
            case 5:
                mlkDay.set(nYear, Calendar.JANUARY, 19);
            case 6:
                mlkDay.set(nYear, Calendar.JANUARY, 18);
            default:
                mlkDay.set(nYear, Calendar.JANUARY, 17);

            return mlkDay;
        }
    }

    public static Calendar EasterSunday(int nYear)
    {


        Calendar easter = Calendar.getInstance();


        int nEasterMonth;
        int nEasterDay;

        // Calculate Easter

        if (nYear < 1900)
        {
            // if year is in java format put it into standard
            // format for the calculation
            nYear += 1900;
        }
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

        // Uncorrect for our earlier correction.
        nYear -= 1900;

        easter.set(nYear, nEasterMonth, nEasterDay);
        // Populate the date object...
        return easter;
    }




}
