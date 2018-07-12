package us.dontcareabout.CheckFlow.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 「檢查點」跟「檢查細項」共用這個 class。
 * 「檢查細項」不在乎 {@link #deadline} 跟 {@link #itemList} 的值。
 */
public class CheckPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private Date deadline;
	private boolean finish;
	private ArrayList<CheckPoint> itemList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public ArrayList<CheckPoint> getItemList() {
		if (itemList == null) {
			itemList = new ArrayList<CheckPoint>();
		}

		return itemList;
	}

	public double getProgress() {
		//這不知道該不該算當初殘留 finish 延伸的問題
		//總之先用這個 WA 處理 finish / progress 的問題
		if (getItemList().size() == 0) {
			return isFinish() ? 1 : 0;
		}

		int count = 0;
		int total = getItemList().size();

		for (CheckPoint cp : getItemList()) {
			if (cp.isFinish()) { count++; }
		}

		return total == 0 ? 0 : count * 1.0 / total;
	}
}
