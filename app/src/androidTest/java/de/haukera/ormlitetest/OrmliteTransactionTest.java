package de.haukera.ormlitetest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.haukera.ormlitetest.tables.ChildTable;
import de.haukera.ormlitetest.tables.ParentTable;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class OrmliteTransactionTest {
    private static final int NUM_ITERATIONS = 10;

    @Test
    public void testTransactions() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(appContext);

        generateData(dbHelper);

        final Dao<ParentTable, Long> dao = dbHelper.getParentDao();

        ExecutorService serialExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            final int finalI = i;
            serialExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    makeDatabaseCall(dao, finalI, 0);
                }
            });
        }
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            makeDatabaseCall(dao, i, 0);
        }
        serialExecutor.shutdown();
        serialExecutor.awaitTermination(1000000000000L, TimeUnit.DAYS);
    }

    private void makeDatabaseCall(final Dao<ParentTable, Long> parentDao, final int iteration, final int iterationDepth) {
        try {
            parentDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    List<ParentTable> parents = parentDao.queryForAll();
                    for (ParentTable parent : parents) {
                        for (ChildTable child : parent.getChildren()) {
                            Log.d(iterationDepth + ":" + iteration + ":OrmliteTest@" + Thread.currentThread().getName(), child.getDummyField());
                        }
                    }
                    if (Math.random() > 0.5) {
                        throw new Exception("Test Exception");
                    } else {
                        // Make sure it terminates in a deterministic way
                        if (iterationDepth < 10) {
                            makeDatabaseCall(parentDao, iteration, iterationDepth + 1);
                        }
                    }
                    return null;
                }
            });
        } catch (Exception ignored) {
            // Catch test Exception
        }
    }

    private void generateData(DatabaseHelper dbHelper) throws SQLException {
        Dao<ParentTable, Long> parentDao = dbHelper.getParentDao();
        Dao<ChildTable, Long> childDao = dbHelper.getChildDao();

        if (parentDao.countOf() == 0) {
            for (int i = 0; i < 100; i++) {
                ParentTable newParentEntry = new ParentTable();
                parentDao.create(newParentEntry);
                for (int j = 0; j < 100; j++) {
                    ChildTable newChildEntry = new ChildTable();
                    newChildEntry.setDummyField("A dummy field " + i);
                    newChildEntry.setParent(newParentEntry);
                    childDao.create(newChildEntry);
                }
            }
        }
    }
}
