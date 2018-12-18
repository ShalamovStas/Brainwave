package com.example.shalamov.brainwave;

import com.example.shalamov.brainwave.utils.FilesUtils;
import com.example.shalamov.brainwave.utils.ImageUtils;
import com.example.shalamov.brainwave.utils.JsonUtils;
import com.example.shalamov.brainwave.utils.Lesson;
import com.example.shalamov.brainwave.utils.LessonsUtils;

import java.util.ArrayList;

/**
 * Created by shala on 07.11.2018.
 */

public class Global {

    private static JsonUtils jsonUtils;
    private static FilesUtils filesUtils;
    private static ArrayList<Lesson> lessonsList;
    private static LessonsUtils lessonsUtils;
    private static ActivityNavigation activityNavigation;
    private static ImageUtils imageUtils;
    private static QuizLogic mQuizLogic;

    public static QuizLogic getmQuizLogic() {
        return mQuizLogic;
    }

    public static void setmQuizLogic(QuizLogic mQuizLogic) {
        Global.mQuizLogic = mQuizLogic;
    }

    public static ImageUtils getImageUtils() {
        return imageUtils;
    }

    public static void setImageUtils(ImageUtils imageUtils) {
        Global.imageUtils = imageUtils;
    }

    public static ActivityNavigation getActivityNavigation() {
        return activityNavigation;
    }

    public static void setActivityNavigation(ActivityNavigation activityNavigation) {
        Global.activityNavigation = activityNavigation;
    }

    public static LessonsUtils getLessonsUtils() {
        return lessonsUtils;
    }

    public static void setLessonsUtils(LessonsUtils lessonsUtils) {
        Global.lessonsUtils = lessonsUtils;
    }

    public static ArrayList getLessonsList() {
        return lessonsList;
    }

    public static void setLessonsList(ArrayList lessonsList) {
        Global.lessonsList = lessonsList;
    }

    public static JsonUtils getJsonUtils() {
        return jsonUtils;
    }

    public static void setJsonUtils(JsonUtils jsonUtils) {
        Global.jsonUtils = jsonUtils;
    }

    public static FilesUtils getFilesUtils() {
        return filesUtils;
    }

    public static void setFilesUtils(FilesUtils filesUtils) {
        Global.filesUtils = filesUtils;
    }
}
