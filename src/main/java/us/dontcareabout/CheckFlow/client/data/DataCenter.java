package us.dontcareabout.CheckFlow.client.data;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.CheckFlow.client.RpcService;
import us.dontcareabout.CheckFlow.client.RpcServiceAsync;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();
	private final static RpcServiceAsync rpc = GWT.create(RpcService.class);
}
