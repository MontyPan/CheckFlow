package us.dontcareabout.CheckFlow.client.data;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

import us.dontcareabout.CheckFlow.client.RpcService;
import us.dontcareabout.CheckFlow.client.RpcServiceAsync;
import us.dontcareabout.CheckFlow.client.data.CheckListReadyEvent.CheckFlowListHandler;
import us.dontcareabout.CheckFlow.client.data.DelCheckListEndEvent.DelCheckListEndHandler;
import us.dontcareabout.CheckFlow.client.data.SaveCheckListEndEvent.SaveCheckListEndHandler;
import us.dontcareabout.CheckFlow.client.data.SaveTemplateEndEvent.SaveTemplateEndHandler;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();
	private final static RpcServiceAsync rpc = GWT.create(RpcService.class);

	public static void wantCheckFlows() {
		rpc.getChecklists(new AsyncCallback<ArrayList<CheckFlow>>() {
			@Override
			public void onSuccess(ArrayList<CheckFlow> result) {
				eventBus.fireEvent(new CheckListReadyEvent(result));
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static void wantTemplates() {
		rpc.getTemplates(new AsyncCallback<ArrayList<CheckFlow>>() {
			@Override
			public void onSuccess(ArrayList<CheckFlow> result) {
				eventBus.fireEvent(new TemplateReadyEvent(result));
				templates = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private static ArrayList<CheckFlow> templates;
	public static ArrayList<CheckFlow> getTemplates() {
		if (templates == null || templates.size() == 0) {
			templates.add(defaultTemplate());
		}
		return templates;
	}

	public static HandlerRegistration addCheckFlowReady(CheckFlowListHandler handler) {
		return eventBus.addHandler(CheckListReadyEvent.TYPE, handler);
	}

	public static void saveCheckList(CheckFlow checkList) {
		rpc.saveCheckList(checkList, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new SaveCheckListEndEvent());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static HandlerRegistration addSaveCheckListEnd(SaveCheckListEndHandler handler) {
		return eventBus.addHandler(SaveCheckListEndEvent.TYPE, handler);
	}

	public static void saveTemplate(CheckFlow template) {
		rpc.saveTemplate(template, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new SaveTemplateEndEvent());
				wantTemplates();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static HandlerRegistration addSaveTemplateEnd(SaveTemplateEndHandler handler) {
		return eventBus.addHandler(SaveTemplateEndEvent.TYPE, handler);
	}

	public static void delCheckList(CheckFlow checkList) {
		rpc.delCheckList(checkList, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				eventBus.fireEvent(new DelCheckListEndEvent());
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static HandlerRegistration addDelCheckListEnd(DelCheckListEndHandler handler) {
		return eventBus.addHandler(DelCheckListEndEvent.TYPE, handler);
	}

	private static CheckFlow defaultTemplate() {
		final String[] CP = {
			"這是範例與撰寫說明",
			"----------------",
			"行首沒有空格的是 Check Point",
			"Point 可以沒有 Item",
			"其他注意事項："
		};
		final String[][] ITEM = {
			{},
			{},
			{"Check Item 前面需要四個空格", "每個 point 可以有多個 item", "例如這個 point 就有三個 item"},
			{},
			{"程式不會理會空白行", "每次新增專案時會使用當下的範本", "也就是說範本編輯不溯及既往"},
		};

		CheckFlow result = new CheckFlow();

		for (int i = 0; i < CP.length; i++) {
			CheckPoint cp = new CheckPoint();
			cp.setName(CP[i]);
			result.getPointList().add(cp);

			for (int j = 0; j < ITEM[i].length; j++) {
				CheckPoint item = new CheckPoint();
				item.setName(ITEM[i][j]);
				cp.getItemList().add(item);
			}
		}

		return result;
	}
}
