package us.dontcareabout.CheckFlow.client;

import java.util.Date;

//Refactory 搬到 GF
@SuppressWarnings("deprecation")
public class DateUtil {
	/**
	 * @return 開始與結束時間間隔幾天
	 */
	public static int daysBetween(Date start, Date end) {
		return (int)((stripTime(start).getTime() - stripTime(end).getTime()) / 86400000);
	}

	/**
	 * @return 去除時間的新 {@link Date} instance
	 */
	//沿用 GWT 的 CalendarUtil，只是不改變傳入值
	public static Date stripTime(Date date) {
		Date result = new Date(resetMilliseconds(date.getTime()));
		result.setHours(0);
		result.setMinutes(0);
		result.setSeconds(0);
		return result;
	}

	//沿用 GWT 的 CalendarUtil
	private static long resetMilliseconds(long msec) {
		int offset = (int) (msec % 1000);
		// Normalize if time is before epoch
		if (offset < 0) { offset += 1000; }
		return msec - offset;
	}
}
