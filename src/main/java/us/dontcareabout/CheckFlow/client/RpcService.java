package us.dontcareabout.CheckFlow.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import us.dontcareabout.CheckFlow.shared.CheckFlow;

@RemoteServiceRelativePath("RPC")
public interface RpcService extends RemoteService{
	ArrayList<CheckFlow> getTemplates();
	ArrayList<CheckFlow> getChecklists();

	void saveCheckList(CheckFlow checkFlow);
	void saveTemplate(CheckFlow template);
}
