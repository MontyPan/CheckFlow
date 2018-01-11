package us.dontcareabout.CheckFlow.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import us.dontcareabout.CheckFlow.client.RpcService;
import us.dontcareabout.CheckFlow.shared.CheckFlow;

public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {
	private static final long serialVersionUID = 1L;
	private static final Setting SETTING = new Setting();
	private static final Gson gson = new Gson();

	private final File TEMP_DIR;
	private final File DATA_DIR;

	public RpcServiceImpl() {
		File workspace = SETTING.workspace();
		//template / checklist 會確保 workspace 存在

		TEMP_DIR = new File(workspace, "template");
		DATA_DIR = new File(workspace, "checklist");

		if (!TEMP_DIR.exists()) { TEMP_DIR.mkdirs(); }
		if (!DATA_DIR.exists()) { DATA_DIR.mkdirs(); }
	}

	@Override
	public ArrayList<CheckFlow> getTemplates() {
		return loadAll(TEMP_DIR);
	}

	@Override
	public ArrayList<CheckFlow> getChecklists() {
		return loadAll(DATA_DIR);
	}

	@Override
	public void saveCheckList(CheckFlow checkFlow) {
		save(checkFlow, new File(DATA_DIR, checkFlow.getName()));
	}

	private CheckFlow load(File file) {
		try {
			return gson.fromJson(new FileReader(file), CheckFlow.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private ArrayList<CheckFlow> loadAll(File dir) {
		ArrayList<CheckFlow> result = new ArrayList<>();

		for (File file : dir.listFiles()) {
			CheckFlow cf = load(file);
			if (cf != null) {
				result.add(cf);
			}
		}

		return result;
	}

	// 懶得抽出去了 XDDDD
	private void save(CheckFlow cf, File file) {
		try {
			Files.write(file.toPath(), gson.toJson(cf).getBytes(StandardCharsets.UTF_8),
					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
