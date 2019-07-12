package cn.aijiamuyingfang.server.it.coupon.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.api.impl.CouponControllerClient;
import cn.aijiamuyingfang.server.it.AbstractTestAction;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.goods.Good;

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

  @Autowired
  protected CouponControllerClient couponControllerClient;

  public VoucherItem getVoucherItemOneForGoodOne() throws IOException {
    if (null == voucherItemOne) {
      VoucherItem voucherItemRequest = new VoucherItem();
      voucherItemRequest.setName("voucher item 1");
      voucherItemRequest.setDescription("voucher item 1");
      voucherItemRequest.setScore(120);
      voucherItemRequest.setGoodId(getGoodOne().getId());
      this.voucherItemOne = couponControllerClient.createVoucherItem(voucherItemRequest, getAdminAccessToken());
    }
    return voucherItemOne;
  }

  public VoucherItem getVoucherItemTwoForGoodTwo() throws IOException {
    if (null == voucherItemTwo) {
      VoucherItem voucherItemRequest = new VoucherItem();
      voucherItemRequest.setName("voucher item 2");
      voucherItemRequest.setDescription("voucher item 2");
      voucherItemRequest.setScore(120);
      voucherItemRequest.setGoodId(getGoodTwo().getId());
      this.voucherItemTwo = couponControllerClient.createVoucherItem(voucherItemRequest, getAdminAccessToken());
    }
    return voucherItemTwo;
  }

  public GoodVoucher getGoodVoucherOneWithVoucherItemOne() throws IOException {
    if (null == goodVoucherOne) {
      GoodVoucher goodVoucherRequest = new GoodVoucher();
      goodVoucherRequest.setName("good voucher 1");
      goodVoucherRequest.setDescription("good voucher 1");
      goodVoucherRequest.setScore(15);
      goodVoucherRequest.addVoucherItem(getVoucherItemOneForGoodOne());
      this.goodVoucherOne = couponControllerClient.createGoodVoucher(goodVoucherRequest, getAdminAccessToken());
    }
    return goodVoucherOne;
  }

  public GoodVoucher getGoodVoucherTwoWithVoucherItemTwo() throws IOException {
    if (null == goodVoucherTwo) {
      GoodVoucher goodVoucherRequest = new GoodVoucher();
      goodVoucherRequest.setName("good voucher 2");
      goodVoucherRequest.setDescription("good voucher 2");
      goodVoucherRequest.setScore(15);
      goodVoucherRequest.addVoucherItem(getVoucherItemTwoForGoodTwo());
      this.goodVoucherTwo = couponControllerClient.createGoodVoucher(goodVoucherRequest, getAdminAccessToken());
    }
    return goodVoucherTwo;
  }

  public Good applyGoodVoucherOneForGoodOne() throws IOException {
    Good goodRequest = new Good();
    GoodVoucher goodVoucher = new GoodVoucher();
    goodVoucher.setId(getGoodVoucherOneWithVoucherItemOne().getId());
    goodRequest.setGoodVoucher(goodVoucher);
    return goodControllerClient.updateGood(getGoodOne().getId(), goodRequest, getAdminAccessToken());
  }

  public Good applyGoodVoucherTwoForGoodTwo() throws IOException {
    Good goodRequest = new Good();
    GoodVoucher goodVoucher = new GoodVoucher();
    goodVoucher.setId(getGoodVoucherTwoWithVoucherItemTwo().getId());
    goodRequest.setGoodVoucher(goodVoucher);
    return goodControllerClient.updateGood(getGoodTwo().getId(), goodRequest, getAdminAccessToken());
  }
}
