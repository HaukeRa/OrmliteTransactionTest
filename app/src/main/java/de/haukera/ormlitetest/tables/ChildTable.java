package de.haukera.ormlitetest.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hauke on 16.03.2018.
 */
@DatabaseTable
public class ChildTable extends BaseTable {
    @DatabaseField
    private String dummyField;

    @DatabaseField(foreign = true)
    private ParentTable parent;

    public String getDummyField() {
        return dummyField;
    }

    public void setDummyField(String dummyField) {
        this.dummyField = dummyField;
    }

    public void setParent(ParentTable parent) {
        this.parent = parent;
    }
}
