package cn.edu.seu.udo.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cn.edu.seu.udo.UdoApplication;
import cn.edu.seu.udo.entities.UserRecord;
import cn.edu.seu.udo.service.CountResult;
import cn.edu.seu.udo.utils.UserUtil;


public class UserDBHelper extends SQLiteOpenHelper {

    /* 数据库名 */
    private final static String DATABASE_NAME = "user_record.db";
    private static final int DATABASE_VERSION = 1;

    /* 表名 */
    private final static String TABLE_NAME = "user_record";

    /* 表中的字段 */
    private final static String TABLE_ID = "id";
    private final static String TABLE_UNIQUE = "idTime";
    private final static String TABLE_TIME = "time";
    private final static String TABLE_DATA = "data";
    private final static String TABLE_RANK = "rank";
    private final static String TABLE_SCORE = "score";
    private final static String TABLE_UPLOAD = "upload";

    /* 创建表的sql语句 */
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + TABLE_UNIQUE + "  VARCHAR UNIQUE,"
            + TABLE_ID + " INTEGER," + TABLE_TIME + " INTERGER," + TABLE_DATA + " BLOB,"
            + TABLE_RANK + " REAL," + TABLE_SCORE + " REAL," + TABLE_UPLOAD + " INTERGER )";

    public static UserDBHelper INSTANCE = new UserDBHelper(UdoApplication.getUdoApplication());

    private UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
    
    public synchronized long add(int userId, long time, CountResult data, float rank, double score,
            int upload) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_ID, userId);
        cv.put(TABLE_TIME, time);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream objOut;
        try {
            objOut = new ObjectOutputStream(bytes);
            objOut.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cv.put(TABLE_DATA, bytes.toByteArray());
        cv.put(TABLE_RANK, rank);
        cv.put(TABLE_SCORE, score);
        cv.put(TABLE_UPLOAD, upload);
        cv.put(TABLE_UNIQUE, userId + "" + time);
        /* 插入数据 */
        return db.insert(TABLE_NAME, null, cv);
    }

    public synchronized ArrayList<UserRecord> query(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserRecord> list = new ArrayList<UserRecord>();

        Cursor cursor =
                db.query(TABLE_NAME,
                        new String[] {TABLE_ID, TABLE_TIME, TABLE_DATA, TABLE_RANK, TABLE_SCORE,
                                TABLE_UPLOAD, "_id"},
                        "id=?", new String[] {"" + userId}, null, null, null);
        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            long time = cursor.getLong(1);
            byte[] bytes = cursor.getBlob(2);
            int rank = cursor.getInt(3);
            double score = cursor.getDouble(4);
            int upload = cursor.getInt(5);
            int _id = cursor.getInt(6);

            CountResult data = null;
            try {
                ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
                ObjectInputStream objInput = new ObjectInputStream(byteInput);
                data = (CountResult) objInput.readObject();
                byteInput.close();
                objInput.close();
            } catch (Exception e) {
                // TODO 自动生成�? catch �?
                e.printStackTrace();
            }
            UserRecord record = new UserRecord(id, time, data, rank, score, upload);
            record.set_id(_id);
            list.add(record);
        }
        cursor.close();
        return list;
    }

    public synchronized ArrayList<UserRecord> query(int userId, int number) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserRecord> list = new ArrayList<UserRecord>();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {TABLE_ID, TABLE_TIME, TABLE_DATA, TABLE_RANK, TABLE_SCORE,
                        TABLE_UPLOAD, "_id"},
                "id=?", new String[] {"" + userId}, null, null, TABLE_TIME + " DESC");
        int n = 0;
        while (n < number && cursor.moveToNext()) {
            ++n;
            int id = cursor.getInt(0);
            long time = cursor.getLong(1);
            byte[] bytes = cursor.getBlob(2);
            float rank = cursor.getFloat(3);
            double score = cursor.getDouble(4);
            int upload = cursor.getInt(5);
            int _id = cursor.getInt(6);

            CountResult data = null;
            try {
                ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
                ObjectInputStream objInput = new ObjectInputStream(byteInput);
                data = (CountResult) objInput.readObject();
                byteInput.close();
                objInput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserRecord record = new UserRecord(id, time, data, rank, score, upload);
            record.set_id(_id);
            list.add(record);
        }
        cursor.close();
        return list;
    }

    public synchronized ArrayList<UserRecord> queryUnuploaded(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UserRecord> list = new ArrayList<UserRecord>();

        Cursor cursor = db.query(TABLE_NAME,
                new String[] {TABLE_ID, TABLE_TIME, TABLE_DATA, TABLE_RANK, TABLE_SCORE,
                        TABLE_UPLOAD, "_id"},
                "id=? and upload=?", new String[] {"" + userId, "0"}, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            long time = cursor.getLong(1);
            byte[] bytes = cursor.getBlob(2);
            int rank = cursor.getInt(3);
            double score = cursor.getDouble(4);
            int upload = cursor.getInt(5);
            int _id = cursor.getInt(6);

            CountResult data = null;
            try {
                ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
                ObjectInputStream objInput = new ObjectInputStream(byteInput);
                data = (CountResult) objInput.readObject();
                byteInput.close();
                objInput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserRecord record = new UserRecord(id, time, data, rank, score, upload);
            record.set_id(_id);
            list.add(record);
        }
        cursor.close();
        return list;
    }

    // 将未上传的数据上传
    public void checkUpload() {
        ArrayList<UserRecord> records =
                UserDBHelper.INSTANCE.queryUnuploaded(Integer.valueOf(UserUtil.getUserId()));
        int size = records.size();
        for (int i = 0; i < size; ++i) {
            records.get(i).upload(i, size);
        }
    }

    // 修改排名和上传信息
    public int updateUserRecord(int _id, float rank, int upload) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rank", rank);
        cv.put("upload", upload);
        String[] args = {_id + ""};
        return db.update(TABLE_NAME, cv, "_id=?", args);
    }

    // 登陆后修改id
    public int updateUserId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        String[] args = {"0"};
        return db.update(TABLE_NAME, cv, "id=?", args);
    }

    public void downloadUserRecord() {
        if (UserUtil.hasAccount()) {
//            String url = "http://api.learningjun.site/synchro";
//            final String id = UserUtil.getAccountId();
//            StringRequest stringRequest =
//                    new StringRequest(Method.POST, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject data = new JSONObject(response);
//                                JSONArray user_data = data.getJSONArray("user_data");
//                                for (int i = 0; i < user_data.length(); ++i) {
//                                    JSONObject record = user_data.getJSONObject(i);
//                                    // LogUtil.i("ljj_download", record.toString());
//                                    String id = UserUtil.getAccountId();
//                                    long time = CountResult.getDate(record.getString("timestamp"));
//                                    CountResult result = new CountResult(
//                                            CountResult.getDetail(record.getString("detail")));
//                                    float rank = (float) record.getDouble("rank");
//                                    double score = record.getDouble("score");
//                                    // LogUtil.i("ljj_doanload", "id:"+id+" timestamp:"+time+"
//                                    // rank:"+rank+" score"+score);
//                                    UserDBHelper.INSTANCE.add(Integer.valueOf(id), time, result,
//                                            rank, score, 1);
//                                }
//                                MainActivity.getInstance().getHandler().sendEmptyMessage(3);
//                            } catch (Exception e) {
//                                // LogUtil.e("ljj", e.getMessage());
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // LogUtil.e("response error", error.getMessage());
//                        }
//                    }) {
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("user_id", id);
//                            return params;
//                        }
//                    };
//            NetworkRequestUtil.getInstance().addToRequestQueue(stringRequest);
        }
    }
}
