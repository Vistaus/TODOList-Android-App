package com.koller.lukas.todolist.Todolist;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Lukas on 22.08.2016.
 */
public class Alarm {

    public long id;
    public long time;
    public long lastChange_timeStamp;

    public boolean repeating = false;
    public int repeatMode; //0: daily; 1: weekly; 2: monthly; 3: certain days of the week; 4: custom intervall;

    public boolean[] certain_days = new boolean[7] ;

    public long custom_intervall;
    public int numberPicker1_value;
    public int numberPicker2_value;

    public Alarm(long id, long time) {
        this.id = id;
        this.time = time;

        this.lastChange_timeStamp = System.currentTimeMillis();
    }

    public void setTime(long id, long time){
        this.id = id;
        this.time = time;

        this.lastChange_timeStamp = System.currentTimeMillis();
    }

    public void setRepeating(int repeatMode) {
        repeating = true;
        this.repeatMode = repeatMode;

        this.lastChange_timeStamp = System.currentTimeMillis();
    }

    public void unRepeat() {
        repeating = false;
        repeatMode = 0;
        certain_days = null;
        custom_intervall = 0;

        this.lastChange_timeStamp = System.currentTimeMillis();
    }

    public void restoreCertainDays(String s) {
        String[] strings = s.split(";");
        if (strings.length == 7) {
            for (int i = 0; i < certain_days.length; i++) {
                certain_days[i] = Boolean.valueOf(strings[i]);
            }
        }
    }

    public String getCertainDaysString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < certain_days.length; i++) {
            sb.append(certain_days[i]);
            sb.append(";");
        }
        return sb.toString();
    }

    public long nextAlarmTime() {
        //only works if an alarm just fired
        if (!repeating) {
            return 0;
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        if (repeatMode == 0 || repeatMode == 1 || repeatMode == 2) {
            int field = Calendar.DATE;
            switch (repeatMode) {
                case 1:
                    field = Calendar.WEEK_OF_YEAR;
                    break;
                case 2:
                    field = Calendar.MONTH;
                    break;
            }
            calendar.add(field, 1);

        } else if (repeatMode == 3) {
            boolean nextDayFound = false;
            int daysUntilNextAlarm = 1;
            int day = getDayOfTheWeekIndex();
            if (day == 6) {
                day = 0;
            } else {
                day++;
            }
            while (!nextDayFound) {
                if (certain_days[day]) {
                    nextDayFound = true;
                } else {
                    if (day == 6) {
                        day = 0;
                    } else {
                        day++;
                    }
                    daysUntilNextAlarm++;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, daysUntilNextAlarm);

        } else if (repeatMode == 4) {
            calendar.add(Calendar.MILLISECOND, (int) custom_intervall);
        }

        return calendar.getTimeInMillis();
    }

    public int getDayOfTheWeekIndex() {
        int day = Calendar.getInstance(TimeZone.getDefault()).get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            default:
                return 6;
        }
    }

    public boolean equals(Alarm alarm){
        return this.id == alarm.id ||
                this.time == alarm.time ||
                this.repeating == alarm.repeating ||
                this.repeatMode == alarm.repeatMode ||
                equalsCertainDays(alarm.certain_days) ||
                this.custom_intervall == alarm.custom_intervall;
    }

    public boolean equalsCertainDays(boolean[] certain_days){
        for (int i = 0; i < this.certain_days.length; i++){
            if(this.certain_days[i] != certain_days[i]){
                return false;
            }
        }
        return true;
    }
}
