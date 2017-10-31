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

	public double getProgress() {
		int count = 0;
		int total = getPointList().size();

		for (CheckPoint cp : getPointList()) {
			if (cp.isFinish()) { count++; }
		}

		return total == 0 ? 0 : count * 1.0 / total;
	}

	public boolean isEnable() {
		return BitFlag.get(templateFlag, F_ENABLE);
	}

	public void setEnable(boolean enable) {
		templateFlag = BitFlag.set(templateFlag, F_ENABLE, enable);
	}
}
