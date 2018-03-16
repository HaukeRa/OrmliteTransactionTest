package de.haukera.ormlitetest.tables;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Hauke on 16.03.2018.
 */

public class BaseTable {
    @DatabaseField(generatedId = true)
    private long id;

    public long getId() {
        return id;
    }
}
