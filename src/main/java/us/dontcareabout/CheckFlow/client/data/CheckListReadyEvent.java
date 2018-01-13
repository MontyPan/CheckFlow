package us.dontcareabout.CheckFlow.client.data;

import java.util.ArrayList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.CheckFlow.client.data.CheckListReadyEvent.CheckFlowListHandler;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class CheckListReadyEvent extends GwtEvent< CheckFlowListHandler> {
	public static final Type< CheckFlowListHandler> TYPE = new Type< CheckFlowListHandler>();
	public final ArrayList<CheckFlow> data;

	public CheckListReadyEvent(ArrayList<CheckFlow> result) {
		data = result;
	}

	@Override
	public Type< CheckFlowListHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CheckFlowListHandler handler) {
		handler.onCheckFlowReady(this);
	}

	public interface CheckFlowListHandler extends EventHandler{
		public void onCheckFlowReady(CheckListReadyEvent event);
	}
}