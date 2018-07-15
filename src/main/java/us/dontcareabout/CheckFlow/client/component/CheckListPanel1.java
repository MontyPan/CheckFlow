package us.dontcareabout.CheckFlow.client.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.CheckFlow.client.DateUtil;
import us.dontcareabout.CheckFlow.client.Palette;
import us.dontcareabout.CheckFlow.client.Resources;
import us.dontcareabout.CheckFlow.shared.CheckFlow;
import us.dontcareabout.CheckFlow.shared.CheckPoint;
import us.dontcareabout.gxt.client.draw.Cursor;
import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LTextSprite;
import us.dontcareabout.gxt.client.draw.Layer;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;

public class CheckListPanel1 extends LayerContainer {
	private static final int ITEM_HEIGHT = 50;
	private static final int H_UNIT = 60;
	private static final int MARGIN = 5;
	private static final ImageResource arrow = Resources.instance.arrowDown();

	private CheckFlow checkList;

	public void setData(CheckFlow cf) {
		this.checkList = cf;
		refresh();
	}

	private void refresh() {
		this.clear();
		int height = 0;

		List<CheckPoint> cpList = checkList.getPointList();
		CheckPoint cp;
		CheckPointLayer cpLayer;

		for (int i = 0; i < cpList.size(); i++) {
			cp = cpList.get(i);

			if (cp.isFinish()) {
				cpLayer = new FinishLayer();
				cpLayer.resize(0, H_UNIT);
			} else {
				cpLayer = new UnfinishLayer();
				cpLayer.resize(
					0,
					95 + (int)(Math.ceil(cp.getItemList().size() / 2.0) + 1)
					* (ITEM_HEIGHT + MARGIN)
				);
			}

			cpLayer.setLX(MARGIN);
			cpLayer.setLY(height + MARGIN);
			cpLayer.setData(cpList.get(i));
			this.addLayer(cpLayer);
			height += cpLayer.getHeight() + MARGIN;

			if (i < cpList.size() - 1) {
				ArrowLayer arrowLayer = new ArrowLayer();
				arrowLayer.setLY(height + MARGIN);
				this.addLayer(arrowLayer);
				height += arrow.getHeight() + MARGIN;
			}
		}

		//防止 Component.onAttach() 時沒有大小而被（莫名？）給了 500px 的問題
		//重點是上述問題還不是每次都會發生...... ＝＝"
		setHeight(height);
		//確保一定會觸發各 member 的 onResize()
		onResize(getOffsetWidth(), height);
	}

	@Override
	protected void onResize(int width, int height) {
		for (Layer layer : getLayers()) {
			if (layer instanceof CheckPointLayer) {
				LayerSprite ls = (LayerSprite)layer;
				ls.resize(width - MARGIN * 2, ls.getHeight());
			}

			if (layer instanceof ArrowLayer) {
				LayerSprite ls = (LayerSprite)layer;
				ls.setLX((width - arrow.getWidth()) / 2);
			}
		}

		super.onResize(width, height);
	}

	class CheckPointLayer extends LayerSprite {
		private CheckPoint data;
		private LTextSprite text = new LTextSprite();
		private LTextSprite reciprocal = new LTextSprite();

		public CheckPointLayer() {
			add(text);
			add(reciprocal);
		}

		public void setData(CheckPoint checkPoint) {
			this.data = checkPoint;
			getText().setText(checkPoint.getName());

			if (checkPoint.isFinish()) {
				reciprocal.setText("");
			} else {
				if (checkPoint.getDeadline() == null) {
					getReciprocal().setText("？");
				} else {
					int diff = DateUtil.daysBetween(checkPoint.getDeadline(), new Date());
					String reciprocalHeader = diff < 0 ? "逾 " : "剩 ";
					getReciprocal().setText(reciprocalHeader + Math.abs(diff) + " 天");
				}
			}

			adjustMember();
		}

		@Override
		protected void adjustMember() {
			//沒有字就不用調了
			if (Strings.isNullOrEmpty(getReciprocal().getText())) { return; }

			getReciprocal().setLX(
				getWidth() -
				(getReciprocal().getText().length() + 2) *	//有兩個中文字補兩個 unit
				getReciprocal().getFontSize() / 2
				- 10	//右邊的 margin
			);
		}

		public LTextSprite getText() {
			return text;
		}

		public LTextSprite getReciprocal() {
			return reciprocal;
		}

		public CheckPoint getData() {
			return data;
		}
	}

	class UnfinishLayer extends CheckPointLayer {
		private ArrayList<CheckItemLayer> itemLayers = new ArrayList<>();
		private TextButton finish = new TextButton("完成");

		public UnfinishLayer() {
			setBgColor(Palette.AMBER[1]);
			setBgOpacity(0.9);

			getText().setLX(12);
			getText().setLY(8);
			getText().setFontSize(60);

			getReciprocal().setLX(600);
			getReciprocal().setLY(15);
			getReciprocal().setFontSize(40);
			getReciprocal().setCursor(Cursor.POINTER);

			finish.setTextColor(RGB.WHITE);
			finish.setBgRadius(10);
			finish.setMargin(5);
			finish.addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					if (finish.getBgColor() != Palette.RED[1]) { return; }

					getData().setFinish(true);
					refresh();
				}
			});
			add(finish);
		}

		@Override
		public void setData(CheckPoint checkPoint) {
			super.setData(checkPoint);

			finish.setBgColor(
				checkPoint.getItemList().size() == 0 ? Palette.RED[1] : Palette.PURPLE[1]
			);

			//清空 CheckItemLayer 們
			for (CheckItemLayer layer : itemLayers) {
				remove(layer);
			}

			itemLayers.clear();
			////

			for (int i = 0; i < checkPoint.getItemList().size(); i++) {
				CheckItemLayer cil = new CheckItemLayer(checkPoint.getItemList().get(i));
				cil.setLZIndex(100);
				add(cil);
				itemLayers.add(cil);
				//還沒有真正掛到 DrawComponent 上去，所以補作
				cil.deploy(CheckListPanel1.this);
			}

			adjustMember();
		}

		@Override
		protected void adjustMember() {
			super.adjustMember();
			double w = (getWidth() - 40) / 2; //左右各留 15、中間留 10

			for (int i = 0; i < itemLayers.size(); i++) {
				CheckItemLayer cil = itemLayers.get(i);
				cil.resize(w, ITEM_HEIGHT);
				cil.setLX(i % 2 == 0 ? 15 : w + 25);
				cil.setLY(i / 2 * (ITEM_HEIGHT + MARGIN) + 90);
			}

			finish.resize(w, ITEM_HEIGHT);
			finish.setLX(
				(getWidth() - w) / 2
			);
			finish.setLY(
				(int)(Math.ceil(itemLayers.size() / 2.0)) * (ITEM_HEIGHT + MARGIN) + 90
			);
		}

		private void itemChange() {
			boolean flag = true;

			for (CheckPoint cp : getData().getItemList()) {
				if (!cp.isFinish()) {
					flag = false;
					break;
				}
			}

			finish.setBgColor(flag ? Palette.RED[1] : Palette.PURPLE[1]);
		}

		class CheckItemLayer extends LayerSprite {
			private CheckPoint data;
			private LTextSprite text = new LTextSprite();

			public CheckItemLayer(CheckPoint checkItem) {
				data = checkItem;

				setCursor(Cursor.POINTER);
				setBgRadius(10);

				addSpriteSelectionHandler(new SpriteSelectionHandler() {
					@Override
					public void onSpriteSelect(SpriteSelectionEvent event) {
						data.setFinish(!data.isFinish());
						refresh();
						itemChange();
					}
				});

				text.setFontSize(20);
				text.setLX(10);
				text.setLY(11);
				text.setText(data.getName());
				text.setFill(RGB.WHITE);
				add(text);

				refresh();
			}

			public LTextSprite getText() {
				return text;
			}

			private void refresh() {
				setBgColor(data.isFinish() ? Palette.BLUE : Palette.RED[1]);
			}
		}
	}

	class FinishLayer extends CheckPointLayer {
		public FinishLayer() {
			setBgColor(Palette.BLUE);

			getText().setLX(10);
			getText().setLY(10);
			getText().setFontSize(30);
			getText().setFill(RGB.WHITE);

			getReciprocal().setLY(15);
			getReciprocal().setFontSize(20);
			getReciprocal().setFill(RGB.WHITE);
		}
	}

	//用 LayerSprite 比較方便... XD
	class ArrowLayer extends LayerSprite {
		public ArrowLayer() {
			add(new Arrow());
		}

		class Arrow extends LImageSprite {
			public Arrow() {
				setResource(arrow);
			}
		}
	}
}