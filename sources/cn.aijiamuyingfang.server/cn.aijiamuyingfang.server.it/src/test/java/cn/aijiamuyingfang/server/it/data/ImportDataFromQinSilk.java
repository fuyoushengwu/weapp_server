package cn.aijiamuyingfang.server.it.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import cn.aijiamuyingfang.client.domain.goods.ShelfLife;
import cn.aijiamuyingfang.client.rest.api.impl.ClassifyControllerClient;
import cn.aijiamuyingfang.client.rest.api.impl.GoodControllerClient;
import cn.aijiamuyingfang.server.it.ITApplication;
import cn.aijiamuyingfang.server.it.data.qinsilk.Classify;
import cn.aijiamuyingfang.server.it.data.qinsilk.Good;
import cn.aijiamuyingfang.server.it.data.qinsilk.GoodImage;
import cn.aijiamuyingfang.server.it.data.qinsilk.ResponseBean;
import cn.aijiamuyingfang.server.it.goods.controller.GoodsTestActions;

/**
 * [描述]:
 * <p>
 * 从秦丝导入数据
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-30 19:52:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = ITApplication.class)
public class ImportDataFromQinSilk {
  private static List<Classify> qinSilkClassifyList = new ArrayList<>();
  {
    qinSilkClassifyList.add(new Classify("美赞臣", 1649858L));
    qinSilkClassifyList.add(new Classify("雀巢", 1649861L));
    qinSilkClassifyList.add(new Classify("完达山", 1649882L));
    qinSilkClassifyList.add(new Classify("雅培", 1649884L));
    qinSilkClassifyList.add(new Classify("圣元", 1652416L));
    qinSilkClassifyList.add(new Classify("蒙牛", 1655814L));
    // qinSilkClassifyList.add(new Classify("奶瓶", 1662190L));
    // qinSilkClassifyList.add(new Classify("成人尿不湿", 1662191L));
    // qinSilkClassifyList.add(new Classify("拉拉裤", 1662192L));
    // qinSilkClassifyList.add(new Classify("纸尿片", 1662193L));
    // qinSilkClassifyList.add(new Classify("纸尿裤", 1662194L));
    // qinSilkClassifyList.add(new Classify("辅食", 1661862L));
    // qinSilkClassifyList.add(new Classify("营养强化剂", 1661861L));
    // qinSilkClassifyList.add(new Classify("千山牧雪", 1655813L));
  }

  private static List<String> classifyList = new ArrayList<>();
  static {
    classifyList.add("2c8a82cd6b844430016b84996fdc0007");// 美赞臣
    classifyList.add("2c8a82cd6b844430016b849ff825000d");// 雀巢
    classifyList.add("2c8a82cd6b844430016b849b1e82000a");// 完达山
    classifyList.add("2c8a82cd6b844430016b8494450e0003");// 雅培
    classifyList.add("2c8a82c96b815357016b8440215f0001");// 圣元
    classifyList.add("2c8a82cd6b844430016b849a792e0009");// 蒙牛
    // classifyList.add("");
    // classifyList.add("");
    // classifyList.add("");
    // classifyList.add("");
    // classifyList.add("");
    // classifyList.add("");
    // classifyList.add("");
    // classifyList.add("");
  }

  @Autowired
  private QinSilkControllerClient qinSilkControllerClient;

  @Autowired
  private ClassifyControllerClient classifyControllerClient;

  @Autowired
  private GoodControllerClient goodControllerClient;

  @Autowired
  private GoodsTestActions testActions;

  // @Test
  public void importDataFromQinSilk() throws IOException, URISyntaxException {
    for (int i = 0; i < qinSilkClassifyList.size(); i++) {
      Classify c = qinSilkClassifyList.get(i);
      ResponseBean<Good> responseBean = qinSilkControllerClient.getGoodList(c.getId());

      for (int j = 12; j < responseBean.getRows().size(); j++) {
        Good good = responseBean.getRows().get(j);
        good.setGoodImages(qinSilkControllerClient.getGoodImages(good.getId()));

        File coverImageFile = downloadImage(good.getImgUrl());

        List<File> detailImageFiles = new ArrayList<>();
        for (GoodImage goodImage : good.getGoodImages()) {
          File detailImageFile = downloadImage(goodImage.getUrl());
          detailImageFiles.add(detailImageFile);
        }
        cn.aijiamuyingfang.client.domain.goods.Good goodRequest = new cn.aijiamuyingfang.client.domain.goods.Good();
        goodRequest.setBarcode(good.getGoodsSn());
        goodRequest.setCount(1000);
        goodRequest.setLevel(good.getSpecs());
        ShelfLife shelfLife = new ShelfLife();
        shelfLife.setStart("2019-06-30");
        shelfLife.setEnd("2021-06-30");
        goodRequest.setLifetime(shelfLife);
        goodRequest.setMarketPrice(1);
        goodRequest.setName(good.getName());
        goodRequest.setPack(good.getSpecs());
        goodRequest.setPrice(1);
        goodRequest.setSalecount(0);
        goodRequest.setScore(0);
        cn.aijiamuyingfang.client.domain.goods.Good newGood = goodControllerClient.createGood(coverImageFile,
            detailImageFiles, goodRequest, testActions.getAdminAccessToken());
        classifyControllerClient.addClassifyGood(classifyList.get(i), newGood.getId(),
            testActions.getAdminAccessToken(), false);
      }
    }
  }

  private File downloadImage(String source) throws IOException {
    File tmpFile = File.createTempFile("weapp-image-", ".jpg");
    InputStream input = new URL(source).openStream();
    FileOutputStream output = new FileOutputStream(tmpFile);
    IOUtils.copy(input, output);
    IOUtils.closeQuietly(input);
    IOUtils.closeQuietly(output);
    return tmpFile;
  }
}
