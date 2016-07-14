package cn.edu.seu.udo.model.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.edu.seu.udo.R;
import cn.edu.seu.udo.model.entities.Greeting;
import cn.edu.seu.udo.model.entities.Time;

/**
 * Author: Jeremy Xu on 2016/7/2 21:41
 * E-mail: jeremy_xm@163.com
 */
public class GreetingCache {

    private static final String DB_NAME = "greeting.db";
    private static final String TABLE_NAME = "greeting_table";

    private static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME +
                                            "(_id integer primary key," +
                                            "hour integer, minute integer, greeting text)";

    private static final String DELETE_TABLE = "delete from " + TABLE_NAME + " where _id=?";

    private SQLiteDatabase database;

    @Inject
    public GreetingCache(Context context) {
        String path = context.getFilesDir().getPath() + DB_NAME;
        database = SQLiteDatabase.openOrCreateDatabase(path, null);
        createTable();
    }

    private void createTable() {
        database.execSQL(CREATE_TABLE);
    }

    public void add(Greeting greeting) {
        String greeting_sql = "insert into " + TABLE_NAME +
                "(_id, hour, minute, greeting) values(" +
                "'" + greeting.getId() + "'" +
                "'" + greeting.getTime().getHour() + "'" +
                "'" + greeting.getTime().getMinute() + "'" +
                "'" + greeting.getContent() + "')";
        database.execSQL(greeting_sql);
    }

    public List<Greeting> getGreetings(long id) {
        Cursor cursor = database.rawQuery("SELECT ID, hour, minute, greeting FROM " + TABLE_NAME +
                " where ID=?", new String[]{"" + id});
        cursor.moveToFirst();
        List<Greeting> greetings = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int hour = cursor.getInt(1);
            int minute = cursor.getInt(2);
            String text = cursor.getString(3);
            Greeting greeting = new Greeting(id, R.drawable.invoker, "my name", text, new Time(hour, minute));
            greetings.add(greeting);
        }
        return greetings;
    }

    public void clear() {
        database.execSQL(DELETE_TABLE);
    }
}
