package cn.aijiamuyingfang.server.coupon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.coupon.db.GoodVoucherRepository;
import cn.aijiamuyingfang.server.coupon.db.UserVoucherRepository;
import cn.aijiamuyingfang.server.coupon.db.VoucherItemRepository;
import cn.aijiamuyingfang.server.coupon.domain.GoodVoucher;
import cn.aijiamuyingfang.server.coupon.domain.UserVoucher;
import cn.aijiamuyingfang.server.coupon.domain.VoucherItem;
import cn.aijiamuyingfang.server.coupon.domain.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.server.coupon.domain.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.server.coupon.domain.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.server.exception.CouponException;
import cn.aijiamuyingfang.server.feign.GoodClient;

/**
 * [描述]:
 * <p>
 * 优惠券模块服务层
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-02 01:32:49
 */
@Service
public class CouponService {

  @Autowired
  private GoodClient goodClient;

  @Autowired
  private UserVoucherRepository uservoucherRepository;

  @Autowired
  private GoodVoucherRepository goodvoucherRepository;

  @Autowired
  private VoucherItemRepository voucherItemRepository;

  /**
   * 分页获得用户的兑换券
   * 
   * @param username
   *          用户id
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetUserVoucherListResponse getUserVoucherList(String username, int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<UserVoucher> userVoucherPage = uservoucherRepository.findByUsername(username, pageRequest);
    GetUserVoucherListResponse response = new GetUserVoucherListResponse();
    response.setCurrentPage(userVoucherPage.getNumber() + 1);
    response.setDataList(userVoucherPage.getContent());
    response.setTotalpage(userVoucherPage.getTotalPages());
    return response;
  }

  /**
   * 获得用户的兑换券
   * 
   * @param voucherId
   * @return
   */
  public UserVoucher getUserVoucher(String voucherId) {
    return uservoucherRepository.findOne(voucherId);
  }

  /**
   * 获得用户的兑换券
   * 
   * @param username
   * @param goodvoucherId
   * @return
   */
  public UserVoucher getUserVoucherForGoodVoucher(String username, String goodvoucherId) {
    return uservoucherRepository.findByUsernameAndGoodVoucher(username, goodvoucherId);
  }

  /**
   * 更新用户兑换券
   * 
   * @param userVoucherList
   */
  public void updateUserVoucher(List<UserVoucher> userVoucherList) {
    if (CollectionUtils.hasContent(userVoucherList)) {
      for (UserVoucher userVoucher : userVoucherList) {
        updateUserVoucher(userVoucher);
      }
    }
  }

  /**
   * 更新用户兑换券
   * 
   * @param userVoucher
   */
  public void updateUserVoucher(UserVoucher userVoucher) {
    if (null == userVoucher) {
      return;
    }
    if (StringUtils.isEmpty(userVoucher.getId())) {
      uservoucherRepository.saveAndFlush(userVoucher);
      return;
    }
    UserVoucher oriUserVoucher = uservoucherRepository.findOne(userVoucher.getId());
    if (null == oriUserVoucher) {
      throw new CouponException("404", "UserVoucher[" + userVoucher.getId() + "] not exist");
    }
    oriUserVoucher.update(userVoucher);
    uservoucherRepository.saveAndFlush(oriUserVoucher);
  }

  /**
   * 分页获取商品兑换项
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetGoodVoucherListResponse getGoodVoucherList(int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<GoodVoucher> goodVoucherPage = goodvoucherRepository.findAll(pageRequest);

    GetGoodVoucherListResponse response = new GetGoodVoucherListResponse();
    response.setCurrentPage(goodVoucherPage.getNumber() + 1);
    response.setDataList(goodVoucherPage.getContent());
    response.setTotalpage(goodVoucherPage.getTotalPages());
    return response;
  }

  public GoodVoucher getGoodVoucher(String voucherId) {
    return goodvoucherRepository.findOne(voucherId);
  }

  /**
   * 创建商品兑换券
   * 
   * @param goodVoucher
   * @return
   */
  public GoodVoucher createORUpdateGoodVoucher(GoodVoucher goodVoucher) {
    if (null == goodVoucher) {
      return null;
    }
    if (StringUtils.hasContent(goodVoucher.getId())) {
      GoodVoucher oriGoodVoucher = goodvoucherRepository.findOne(goodVoucher.getId());
      if (oriGoodVoucher != null) {
        oriGoodVoucher.update(goodVoucher);
        return goodvoucherRepository.saveAndFlush(oriGoodVoucher);
      }
    }
    return goodvoucherRepository.saveAndFlush(goodVoucher);
  }

  /**
   * 废弃商品兑换券
   * 
   * @param voucherId
   */
  public void deprecateGoodVoucher(String voucherId) {
    // 废弃GoodVoucher
    GoodVoucher goodVoucher = goodvoucherRepository.findOne(voucherId);
    if (null == goodVoucher || goodVoucher.isDeprecated()) {
      return;
    }
    goodVoucher.setDeprecated(true);
    goodvoucherRepository.saveAndFlush(goodVoucher);

    // 废弃UserVoucher
    UserVoucher userVoucher = uservoucherRepository.findByGoodVoucherId(voucherId);
    if (null == userVoucher || userVoucher.isDeprecated()) {
      return;
    }
    userVoucher.setDeprecated(true);
    uservoucherRepository.saveAndFlush(userVoucher);

    // 消除Good对GoodVoucherId的引用
    goodClient.deprecateGoodVoucher(voucherId);
  }

  /**
   * 分页获取可选的兑换方式
   * 
   * @param currentPage
   * @param pageSize
   * @return
   */
  public GetVoucherItemListResponse getVoucherItemList(int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<VoucherItem> voucherItemPage = voucherItemRepository.findAll(pageRequest);
    GetVoucherItemListResponse response = new GetVoucherItemListResponse();
    response.setCurrentPage(voucherItemPage.getNumber() + 1);
    response.setDataList(voucherItemPage.getContent());
    response.setTotalpage(voucherItemPage.getTotalPages());
    return response;
  }

  /**
   * 获取兑换方式
   * 
   * @param voucherItemId
   * @return
   */
  public VoucherItem getVoucherItem(String voucherItemId) {
    return voucherItemRepository.findOne(voucherItemId);
  }

  /**
   * 创建兑换选择项
   * 
   * @param voucherItem
   * @return
   */
  public VoucherItem createORUpdateVoucherItem(VoucherItem voucherItem) {
    if (null == voucherItem) {
      return null;
    }
    if (StringUtils.hasContent(voucherItem.getId())) {
      VoucherItem oriVoucherItem = voucherItemRepository.findOne(voucherItem.getId());
      if (oriVoucherItem != null) {
        oriVoucherItem.update(voucherItem);
        return voucherItemRepository.saveAndFlush(oriVoucherItem);
      }
    }
    return voucherItemRepository.saveAndFlush(voucherItem);
  }

  /**
   * 废弃兑换项
   * 
   * @param voucherItemId
   */
  public void deprecateVoucherItem(String voucherItemId) {
    // 废弃VoucherItem
    VoucherItem voucherItem = voucherItemRepository.findOne(voucherItemId);
    if (null == voucherItem || voucherItem.isDeprecated()) {
      return;
    }
    voucherItem.setDeprecated(true);
    voucherItemRepository.saveAndFlush(voucherItem);

    // GoodVoucher中引用的VoucherItem也删除
    goodvoucherRepository.deprecateVoucherItem(voucherItemId);
  }

}
