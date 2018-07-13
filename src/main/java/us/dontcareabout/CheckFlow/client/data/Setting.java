package us.dontcareabout.CheckFlow.client.data;

import com.google.gwt.user.client.Window.Location;

public class Setting {
	/**
	 * 目前只有兩種 style（以後大概還是只有兩種 Zzz）：flow 跟 group。
	 *
	 * @return true：flow style。false：group style。
	 */
	public static boolean style() {
		return "flow".equalsIgnoreCase(Location.getParameter("style"));
	}
}
