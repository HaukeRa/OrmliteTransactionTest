package de.haukera.ormlitetest.tables;

import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hauke on 16.03.2018.
 */
@DatabaseTable
public class ParentTable extends BaseTable {
    @ForeignCollectionField
    private Collection<ChildTable> children;

    public List<ChildTable> getChildren() {
        return new ArrayList<>(children);
    }
}
