/**
 *
 */
package de.haukera.ormlitetest;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    public static void main(String[] args) throws Exception {
        File rawDir = findRawDir(new File("."));
        if (rawDir != null) {
            writeConfigFile(new File(rawDir, "ormlite_config.txt"));
        }
    }
}
