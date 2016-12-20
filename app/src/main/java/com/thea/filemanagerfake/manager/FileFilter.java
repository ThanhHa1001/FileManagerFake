package com.thea.filemanagerfake.manager;

import android.util.Log;

/**
 * Created by Thea on 10/19/2016.
 */

public class FileFilter {

    public static boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg")
                || filePath.endsWith(".png")
                || filePath.endsWith(".jpeg")
                || filePath.endsWith(".gif"))
        {
            return true;
        }
        return false;
    }

    public static boolean isVideoFile(String filePath) {
        if (filePath.endsWith(".3gp")
                || filePath.endsWith(".mp4")
                || filePath.endsWith(".mkv")
                || filePath.endsWith(".MKV")
                || filePath.endsWith(".webm"))
        {
            return true;
        }
        return false;
    }

    public static boolean isMusicFile(String filePath) {
        if (filePath.endsWith(".flac")
                || filePath.endsWith(".mp3")
                || filePath.endsWith(".mid")
                || filePath.endsWith(".ogg"))
        {
            return true;
        }
        return false;
    }

    public static boolean isDocumentFile(String filePath) {
        if (filePath.endsWith(".doc")
                || filePath.endsWith(".docx")
                || filePath.endsWith(".txt")
                || filePath.endsWith(".ppt")
                || filePath.endsWith(".pptx")
                || filePath.endsWith(".xls")
                || filePath.endsWith(".xlsx")
                || filePath.endsWith(".pdf"))
        {
            return true;
        }
        return false;
    }

    public static boolean isCompressedFile(String filePath) {
        if (filePath.endsWith(".rar")
                || filePath.endsWith(".zip"))
        {
            return true;
        }
        return false;
    }

    public static boolean isFile(String filePath) {
        if (!filePath.substring(filePath.lastIndexOf("/") + 1).startsWith(".")) {
            return true;
        }
        return false;
    }

    public static boolean isDirectory(String filePath) {
        if (!filePath.substring(filePath.lastIndexOf("/") + 1).startsWith(".")) {
            return true;
        }
        return false;
    }
}
