package com.checkpoint.andela.mytracker.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.checkpoint.andela.mytracker.model.TrackerModel;

import org.junit.Before;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by suadahaji.
 */
public class DBManagerTest extends AndroidTestCase {


    void deleteDb(){
        mContext.deleteDatabase(Constants.DATABASE_NAME);
    }


    @Before
    public void setUp() throws Exception {
    }

    public long insertMovement() {
        TrackerDbHelper trackerDbHelper = new TrackerDbHelper(mContext);
        ContentValues testValues = TestHelper.createMovementValues();
        long recordId = trackerDbHelper.getDB().insert(Constants.TABLE_NAME, null, testValues);
        assertTrue(recordId !=-1);
        return recordId;
    }

    public void testRecordTable() {
        long id = insertMovement();
        assertFalse("Error: Location has not been inserted correctly", id == -1L);

    }

    public void testListAll() {
        deleteDb();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        TrackerDbHelper trackerDbHelper = new TrackerDbHelper(mContext);
        DBManager dbManager = new DBManager(trackerDbHelper);
        TrackerModel trackerModel = new TrackerModel();
        trackerModel.setTracker_date(simpleDateFormat.format(date));
        trackerModel.setLocation("Kindaruma Rd, Nairobi");
        trackerModel.setDuration(230313);
        trackerModel.setCoordinates("-1.2974303:36.7889626");
        dbManager.insertDataIntoDatabase(trackerModel);
        dbManager.listByID(1);
        assertEquals("Kindaruma Rd, Nairobi", trackerModel.getLocation());
        assertEquals("-1.2974303:36.7889626", trackerModel.getCoordinates());

    }


}