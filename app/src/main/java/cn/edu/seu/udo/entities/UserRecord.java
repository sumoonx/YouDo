package cn.edu.seu.udo.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.seu.udo.service.CountResult;
import cn.edu.seu.udo.utils.UserUtil;


public class UserRecord {
	
	private int _id;
	private int id;
	private long time;
	private CountResult data;
	private float rank;
	private double score;
	private int upload;
	
	public UserRecord(int id,long time,CountResult data,float rank,double score,int b){
		this._id = 0;
		this.id = id;
		this.time = time;
		this.data = data;
		this.rank = rank;
		this.score = score;
		this.upload = b;
	}

	public void set_id(int _id){
		this._id = _id;
	}
	
	public int get_id(){
		return _id;
	}
	
	public int getId() {
		return id;
	}

	public long getTime() {
		return time;
	}

	public String getRecordTimeString(){
//		 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm",Locale.SIMPLIFIED_CHINESE);
		 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.SIMPLIFIED_CHINESE);
	     Date date= new Date(time);
	            return sdf.format(date);
	}
	
	public CountResult getData() {
		return data;
	}

	public float getRank() {
		return rank;
	}

	public double getScore() {
		return score;
	}
	
	public void setRank(float rank){
		this.rank = rank;
	}
	
	public void setUpload(int b){
		this.upload = b;
	}
	
	public int getUpload(){
		return upload;
	}
	
	public void upload(int i,int size) {
		final int index = i;
		final int count =size;
		if (UserUtil.hasAccount()) {
//			final UserRecord record = this;
//			// 上传到网络
//			String url = "http://api.learningjun.site/upload";
//			final double score = this.getScore();
//			final float totalTime = data.getTotalTime() / 1000f;
//			final String detail = data.getDetail();
//			final long timestamp =  data.getRecordTime()/1000;
//			StringRequest stringRequest = new StringRequest(Method.POST, url,
//					new Response.Listener<String>() {
//						@Override
//						public void onResponse(String response) {
//							try {
//								LogUtil.i("ljj_upload", response + "/ id:" + record.get_id());
//								JSONObject json = new JSONObject(response);
//								UserDBHelper.INSTANCE.updateUserRecord(record.get_id(),(float)json.getDouble("rank"), 1);
//								if(index==count-1){
//									MainActivity.getInstance().getHandler().sendEmptyMessage(3);
//								}
//							} catch (Exception e) {
//								//LogUtil.e("json error", e.getMessage());
//							}
//						}
//					}, new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							//LogUtil.e("response error", error.getMessage());
//						}
//					}) {
//				@Override
//				protected Map<String, String> getParams() {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("user_id", record.getId() + "");
//					params.put("score", score + "");
//					params.put("duration", totalTime + "");
//					params.put("detail", detail);
//					params.put("timestamp", timestamp+"");
//					return params;
//				}
//			};
//			NetworkRequestUtil.getInstance().addToRequestQueue(stringRequest);
		}
	}
}
