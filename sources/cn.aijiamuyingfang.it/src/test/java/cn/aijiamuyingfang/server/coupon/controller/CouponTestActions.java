package cn.aijiamuyingfang.server.coupon.controller;

import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.server.client.AbstractTestAction;
import java.io.IOException;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 测试动作集
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-11 04:44:16
 */
@Service
public class CouponTestActions extends AbstractTestAction {

  public String voucheritemoneId;

  public String voucheritemtwoId;

  public String goodvoucheroneId;

  public String goodvouchertwoId;

  public VoucherItem createVoucherItemOneForGoodOne() throws IOException {
    VoucherItem voucheritemRequest = new VoucherItem();
    voucheritemRequest.setName("voucher item 1");
    voucheritemRequest.setDescription("voucher item 1");
    voucheritemRequest.setScore(120);
    voucheritemRequest.setGoodid(goodoneId);
    VoucherItem voucherItem = couponControllerClient.createVoucherItem(ADMIN_USER_TOKEN, voucheritemRequest);
    if (voucherItem != null) {
      voucheritemoneId = voucherItem.getId();
    }
    return voucherItem;
  }

  public VoucherItem createVoucherItemTwoForGoodTwo() throws IOException {
    VoucherItem voucheritemRequest = new VoucherItem();
    voucheritemRequest.setName("voucher item 2");
    voucheritemRequest.setDescription("voucher item 2");
    voucheritemRequest.setScore(120);
    voucheritemRequest.setGoodid(goodtwoId);
    VoucherItem voucherItem = couponControllerClient.createVoucherItem(ADMIN_USER_TOKEN, voucheritemRequest);
    if (voucherItem != null) {
      voucheritemtwoId = voucherItem.getId();
    }
    return voucherItem;
  }

  public GoodVoucher createGoodVoucherOneWithVoucherItemOne() throws IOException {
    GoodVoucher goodVoucherRequest = new GoodVoucher();
    goodVoucherRequest.setName("good voucher 1");
    goodVoucherRequest.setDescription("good voucher 1");
    goodVoucherRequest.setScore(15);
    goodVoucherRequest.addVoucheritemId(voucheritemoneId);
    GoodVoucher goodvoucher = couponControllerClient.createGoodVoucher(ADMIN_USER_TOKEN, goodVoucherRequest);
    if (goodvoucher != null) {
      goodvoucheroneId = goodvoucher.getId();
    }
    return goodvoucher;
  }

  public GoodVoucher createGoodVoucherTwoWithVoucherItemTwo() throws IOException {
    GoodVoucher goodVoucherRequest = new GoodVoucher();
    goodVoucherRequest.setName("good voucher 2");
    goodVoucherRequest.setDescription("good voucher 2");
    goodVoucherRequest.setScore(15);
    goodVoucherRequest.addVoucheritemId(voucheritemtwoId);
    GoodVoucher goodvoucher = couponControllerClient.createGoodVoucher(ADMIN_USER_TOKEN, goodVoucherRequest);
    if (goodvoucher != null) {
      goodvouchertwoId = goodvoucher.getId();
    }
    return goodvoucher;
  }

  public Good applyGoodVoucherOneForGoodOne() throws IOException {
    Good goodRequest = new Good();
    goodRequest.setVoucherId(goodvoucheroneId);
    return goodControllerClient.updateGood(ADMIN_USER_TOKEN, goodoneId, goodRequest);
  }

  public Good applyGoodVoucherTwoForGoodTwo() throws IOException {
    Good goodRequest = new Good();
    goodRequest.setVoucherId(goodvouchertwoId);
    return goodControllerClient.updateGood(ADMIN_USER_TOKEN, goodtwoId, goodRequest);
  }

  @Override
  public void clearData() {
    super.clearData();
    voucheritemoneId = null;
    goodvoucheroneId = null;
  }
}
