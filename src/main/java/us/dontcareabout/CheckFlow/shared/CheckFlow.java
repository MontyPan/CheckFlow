package us.dontcareabout.CheckFlow.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import us.dontcareabout.gwt.shared.util.BitFlag;

/**
 * 「檢查流程」跟「檢查流程範本」共用此 class。
 */
public class CheckFlow implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int F_ENABLE = 0;

	private String versionId;
	private String name;
	private ArrayList<CheckPoint> pointList;
	private Date deadline;
	private int templateFlag;

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String id) {
		this.versionId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CheckPoint> getPointList() {
		if (pointList == null) {
			pointList = new ArrayList<CheckPoint>();
		}

		return pointList;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * @return 目前尚未完成的 {@link CheckPoint} 的 index（從 0 開始）。
	 * 	如果全部都完成，會回傳 {@link #getPointList()} 的 size。
	 */
	public int getUnfinishPointIndex() {
		int i = 0;

		for (; i < getPointList().size(); i++) {
			if (!getPointList().get(i).isFinish()) {
				break;
			}
		}

		return i;
	}

	public double getProgress() {
		return 1.0 * getUnfinishPointIndex() / getPointList().size();
	}

	public boolean isEnable() {
		return BitFlag.get(templateFlag, F_ENABLE);
	}

	public void setEnable(boolean enable) {
		templateFlag = BitFlag.set(templateFlag, F_ENABLE, enable);
	}
}
