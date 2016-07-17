package cn.edu.seu.udo.service;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.edu.seu.udo.UdoApplication;
import cn.edu.seu.udo.utils.AppInfoUtil;


public class CountResult implements Serializable {

	private static final long serialVersionUID = -58204006271621953L;

	private static Map<String, Double> qMap;
	static {
		qMap = new HashMap<String, Double>();
		qMap.put("ËøÆÁ", 0.0);
		qMap.put("Udo", 0.0);
		qMap.put(AppInfoUtil.getLauncherLabel(), 0.0);
		// qMap.put("å¾®ä¿¡", 1.0);
		// qMap.put("QQ", 1.0);
	}

	private ArrayList<TimeAxisItem> timeAxis;
	private Map<String, Long> appTimeMap;
	private ArrayList<TimeAxisItem> appTimeList;
	private float rank;
	private double score;
	private long recordTime;
	private long totalTime;
	private boolean isStored = false;

	public CountResult(ArrayList<TimeAxisItem> result) {
		recordTime = System.currentTimeMillis();
		rank = 0;
		score = 0;
		totalTime = 0;
		appTimeMap = new HashMap<String, Long>();
		timeAxis = new ArrayList<TimeAxisItem>();
		appTimeList = new ArrayList<TimeAxisItem>();
		timeAxis.addAll(result);
		countAppTime();
		computeScore();
	}

	private void countAppTime() {
		for (int i = 0; i < timeAxis.size(); ++i) {
			TimeAxisItem item = timeAxis.get(i);
			String pkgName = item.getPkgName();
			long time = item.getTime();
			totalTime += time;
			if (appTimeMap.containsKey(pkgName)) {
				Long old = appTimeMap.get(pkgName);
				appTimeMap.put(pkgName, old + time);
			} else {
				appTimeMap.put(pkgName, time);
			}
		}
		for (Map.Entry<String, Long> entry : appTimeMap.entrySet()) {
			appTimeList.add(new TimeAxisItem(entry.getKey(), entry.getValue()));
		}
	}

	public ArrayList<TimeAxisItem> getSortTiemArrayList() {

		Collections.sort(appTimeList, new Comparator<TimeAxisItem>() {

			@Override
			public int compare(TimeAxisItem obj1, TimeAxisItem obj2) {

				if (obj2.getTime().compareTo(obj1.getTime()) > 0) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		return appTimeList;
	}

	private void computeScore() {

		float q1 = 0;
		float q2 = 0;

		float minute = totalTime / 60000f;
		if (minute <= 30)
			q2 = minute * 0.6f / 30;
		else if (minute <= 120)
			q2 = 0.6f + (minute - 30) * 0.4f / 90;
		else if (minute <= 240)
			q2 = 1.0f + (minute - 120) * 0.15f / 120;
		else
			q2 = 1.15f;

		for (Map.Entry<String, Long> entry : appTimeMap.entrySet()) {
			String pkgName = entry.getKey();
//			if (qMap.containsKey(lable) || MatchingUtil.matchString(lable) || MyApp.whiteSet.contains(lable))
//				q1 += entry.getValue() * 1.0f / totalTime;
			if (UdoApplication.whiteSet.contains(pkgName))
				q1 += entry.getValue() * 1.0f / totalTime;
		}
		score = q1 * q2 * 100;
	}

	public ArrayList<TimeAxisItem> getTimeAxis() {
		return timeAxis;
	}

	public Map<String, Long> getAppTimeMap() {
		return appTimeMap;
	}

	public ArrayList<TimeAxisItem> getAppTimeList() {
		return appTimeList;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public double getScore() {
		return score;
	}

	public long getTotalTime() {
		return totalTime;
	}
	
	public String getDetail() {
		StringBuilder sb = new StringBuilder();
		int size = appTimeList.size();
		for (int i = 0; i < size; ++i) {
			sb.append(appTimeList.get(i).getPkgName());
			sb.append(':');
			sb.append(appTimeList.get(i).getTime() / 1000f + " ");
		}
		return sb.toString();
	}

	public long getRecordTime() {
		return recordTime;
	}

	public void setStored(boolean b) {
		this.isStored = b;
	}

	public boolean getStored() {
		return this.isStored;
	}

	public static long getDate(String date){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.SIMPLIFIED_CHINESE);   
		Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}      
		return dt.getTime();
	}
	
	public static ArrayList<TimeAxisItem> getDetail(String detail){
		ArrayList<TimeAxisItem> list = new ArrayList<TimeAxisItem>();
		String[] ss = detail.split(" |:");
		if(ss.length<2)
			return list;
		for(int i=0;i<ss.length;){
			String pkgName = ss[i++];
			long time = (long) (Float.valueOf(ss[i++]) *1000);
			list.add(new TimeAxisItem(pkgName, time));
		}
		list.trimToSize();
		return list;
	}
}
