package us.dontcareabout.CheckFlow.client.data;

import java.util.ArrayList;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.CheckFlow.client.data.TemplateReadyEvent.TemplateReadyHandler;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class TemplateReadyEvent extends GwtEvent< TemplateReadyHandler> {
	public static final Type< TemplateReadyHandler> TYPE = new Type< TemplateReadyHandler>();
	public final ArrayList<CheckFlow> data;

	public TemplateReadyEvent(ArrayList<CheckFlow> result) {
		data = result;
	}

	@Override
	public Type< TemplateReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(TemplateReadyHandler handler) {
		handler.onTemplateReady(this);
	}

	public interface TemplateReadyHandler extends EventHandler{
		public void onTemplateReady(TemplateReadyEvent event);
	}
}