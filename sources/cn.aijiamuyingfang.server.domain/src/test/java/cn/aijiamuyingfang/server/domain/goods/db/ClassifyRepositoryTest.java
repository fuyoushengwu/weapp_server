package cn.aijiamuyingfang.server.domain.goods.db;

import cn.aijiamuyingfang.commons.annotation.TestDescription;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * [描述]:
 * <p>
 * ClassifyRepository测试
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-05 13:43:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassifyRepositoryTest {
  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private ClassifyRepository classifyRepository;

  @Before
  public void before() throws IOException {
    storeRepository.deleteAll();
    classifyRepository.deleteAll();
  }

  @After
  public void after() {
    storeRepository.deleteAll();
    classifyRepository.deleteAll();
  }

  @Test
  @TestDescription(description = "测试删除门店所属的条目")
  public void testDeleteClassify() {
    Classify classify = new Classify();
    classify.setName("classify");
    classify.setLevel(1);
    classifyRepository.saveAndFlush(classify);
    Assert.assertTrue(StringUtils.hasContent(classify.getId()));

    Store store = new Store();
    store.setName("store");
    store.addClassify(classify);
    storeRepository.saveAndFlush(store);
    Assert.assertTrue(StringUtils.hasContent(store.getId()));
    Assert.assertEquals(1, store.getClassifyList().size());
    Assert.assertEquals(classify.getId(), store.getClassifyList().get(0).getId());

    store.getClassifyList().remove(classify);
    storeRepository.saveAndFlush(store);
    classifyRepository.delete(classify.getId());

    Assert.assertEquals(0, store.getClassifyList().size());
  }
}
