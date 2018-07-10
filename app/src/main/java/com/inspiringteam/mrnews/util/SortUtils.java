package com.inspiringteam.mrnews.util;

import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.inspiringteam.mrnews.data.models.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SortUtils {
    public static int hashCode(String string) {
        return Hashing.murmur3_32()
                .newHasher()
                .putString(string, Charsets.UTF_8).hash().asInt();
    }

    private static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    /** Transform ISO 8601 string to Calendar. */
    private static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }

    private static long toTimestamp(Calendar calendar){
        return calendar.getTimeInMillis();
    }

    public static List<News> orderNewsByNewest(List<News> news){
        if(news != null && news.size() > 1){
            Collections.sort(news, new Comparator<News>() {
                @Override
                public int compare(News o1, News o2) {
                    try {
                        if (toTimestamp(toCalendar(o1.getPublishedAt()))
                                < toTimestamp(toCalendar(o2.getPublishedAt()))){
                            return -1;
                        } else if (toTimestamp(toCalendar(o1.getPublishedAt()))
                                > toTimestamp(toCalendar(o2.getPublishedAt()))) {
                            return 1;
                        } return  0;
                    } catch (ParseException e) {
                        Log.d(SortUtils.class.getName(), "Parse exception thrown");
                    }
                    return 0;
                }
            });
            Collections.reverse(news);
        }
        return news;
    }
}
