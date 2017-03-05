package diabai.uw.tacoma.edu.hobbyfinder.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import diabai.uw.tacoma.edu.hobbyfinder.R;
import diabai.uw.tacoma.edu.hobbyfinder.user.User;

/**
 * Created by edgards on 3/5/17.
 */

public class UserDB {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "User.db";
    private static final String USER_TABLE = "User";

    private UserDBHelper mUserDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public UserDB(Context context) {
        mUserDBHelper = new UserDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mUserDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     *
     * @param id
     * @param email
     * @param gender
     * @param hometown
     * @return true or false
     */
    public boolean insertUser(String id, String email, String gender, String hometown, String hobbies) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("email", email);
        contentValues.put("gender", gender);
        contentValues.put("hometown", hometown);
        contentValues.put("hobbies", hobbies);

        long rowId = mSQLiteDatabase.insert("User", null, contentValues);
        return rowId != -1;
    }

    /**
     * Delete all the data from the USER_TABLE
     */
    public void deleteUser() {
        mSQLiteDatabase.delete(USER_TABLE, null, null);
    }


    /**
     * Returns the list of courses from the local User table.
     *
     * @return list
     */
    public List<User> getUser() {

        String[] columns = {
                "id", "name", "email", "gender", "hometown", "hobbies"
        };

        Cursor c = mSQLiteDatabase.query(
                USER_TABLE,  // The table to query
                columns,    // The columns to return
                null,       // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,       // don't group the rows
                null,       // don't filter by row groups
                null        // The sort order
        );
        c.moveToFirst();
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(0);
            String name = c.getString(1);
            String email = c.getString(2);
            String gender = c.getString(3);
            String hometown = c.getString(4);
            String hobbies = c.getString(5);
            User user = new User(id, name, email, gender, hometown);
            list.add(user);
            c.moveToNext();
        }

        return list;
    }

    class UserDBHelper extends SQLiteOpenHelper {

        private final String CREATE_USER_SQL;

        private final String DROP_USER_SQL;

        public UserDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_USER_SQL = context.getString(R.string.CREATE_USER_SQL);
            DROP_USER_SQL = context.getString(R.string.DROP_USER_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_USER_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_USER_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}
