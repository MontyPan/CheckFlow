package us.dontcareabout.CheckFlow.client.data;

import java.util.ArrayList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.CheckFlow.client.data.CheckFlowReadyEvent.CheckFlowReadyHandler;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class CheckFlowReadyEvent extends GwtEvent< CheckFlowReadyHandler> {
	public static final Type< CheckFlowReadyHandler> TYPE = new Type< CheckFlowReadyHandler>();
	public final ArrayList<CheckFlow> data;

	public CheckFlowReadyEvent(ArrayList<CheckFlow> result) {
		data = result;
	}

	@Override
	public Type< CheckFlowReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CheckFlowReadyHandler handler) {
		handler.onCheckFlowReady(this);
	}

	public interface CheckFlowReadyHandler extends EventHandler{
		public void onCheckFlowReady(CheckFlowReadyEvent event);
	}
}