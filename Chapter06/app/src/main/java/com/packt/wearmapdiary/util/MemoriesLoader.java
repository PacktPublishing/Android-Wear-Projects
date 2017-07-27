package com.packt.wearmapdiary.util;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by ashok.kumar on 12/03/17.
 */

public class MemoriesLoader extends DbCursorLoader {

    private MemoriesDataSource mDataSource;

    public MemoriesLoader(Context context, MemoriesDataSource memoriesDataSource){
        super(context);
        mDataSource = memoriesDataSource;
    }

    @Override
    protected Cursor loadCursor() {
        return mDataSource.allMemoriesCursor();
    }
}