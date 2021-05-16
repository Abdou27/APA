package com.univ.tours.apa.database;

import androidx.room.TypeConverter;

import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Converters {
    @TypeConverter
    public static LocalDate fromLocalDate(String value) {
        return LocalDate.parse(value);
    }

    @TypeConverter
    public static String localDateToString(LocalDate date) {
        return date.toString();
    }

    @TypeConverter
    public static LocalDateTime fromLocalDateTime(String value) {
        return LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String localDateTimeToString(LocalDateTime datetime) {
        return datetime.toString();
    }

    @TypeConverter
    public static User fromUser(Long value) {
        return MainActivity.db.userDao().findById(value);
    }

    @TypeConverter
    public static Long userToLong(User user) {
        if (user != null)
            return user.getId();
        else
            return null;
    }

    @TypeConverter
    public static Activity fromActivity(Long value) {
        return MainActivity.db.activityDao().findById(value);
    }

    @TypeConverter
    public static Long activityToLong(Activity activity) {
        return activity.getId();
    }

    @TypeConverter
    public static Course fromCourse(Long value) {
        return MainActivity.db.courseDao().findById(value);
    }

    @TypeConverter
    public static Long courseToLong(Course course) {
        return course.getId();
    }

    @TypeConverter
    public static Session fromSession(Long value) {
        return MainActivity.db.sessionDao().findById(value);
    }

    @TypeConverter
    public static Long sessionToLong(Session session) {
        return session.getId();
    }

    @TypeConverter
    public static Structure fromStructure(Long value) {
        return MainActivity.db.structureDao().findById(value);
    }

    @TypeConverter
    public static Long structureToLong(Structure structure) {
        if (structure != null)
            return structure.getId();
        else
            return null;
    }
}
