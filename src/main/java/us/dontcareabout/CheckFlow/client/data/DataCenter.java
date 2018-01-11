package us.dontcareabout.CheckFlow.client.data;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

import us.dontcareabout.CheckFlow.client.RpcService;
import us.dontcareabout.CheckFlow.client.RpcServiceAsync;
import us.dontcareabout.CheckFlow.client.data.CheckFlowReadyEvent.CheckFlowReadyHandler;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();
	private final static RpcServiceAsync rpc = GWT.create(RpcService.class);

	public static void wantCheckFlows() {
		rpc.getChecklists(new AsyncCallback<ArrayList<CheckFlow>>() {
			@Override
			public void onSuccess(ArrayList<CheckFlow> result) {
				eventBus.fireEvent(new CheckFlowReadyEvent(result));
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
		return templates;
	}

	public static HandlerRegistration addCheckFlowReady(CheckFlowReadyHandler handler) {
		return eventBus.addHandler(CheckFlowReadyEvent.TYPE, handler);
	}
}
