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
  private VoucherItemRepository voucheritemRepository;

  /**
   * 分页获得用户的兑换券
   * 
   * @param userid
   *          用户id
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetUserVoucherListResponse getUserVoucherList(String userid, int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
    Page<UserVoucher> userVoucherPage = uservoucherRepository.findByUserid(userid, pageRequest);
    GetUserVoucherListResponse response = new GetUserVoucherListResponse();
    response.setCurrentpage(userVoucherPage.getNumber() + 1);
    response.setDataList(userVoucherPage.getContent());
    response.setTotalpage(userVoucherPage.getTotalPages());
    return response;
  }

  /**
   * 获得用户的兑换券
   * 
   * @param voucherid
   * @return
   */
  public UserVoucher getUserVoucher(String voucherid) {
    return uservoucherRepository.findOne(voucherid);
  }

  /**
   * 获得用户的兑换券
   * 
   * @param userid
   * @param goodvoucherId
   * @return
   */
  public UserVoucher getUserVoucherForGoodVoucher(String userid, String goodvoucherId) {
    return uservoucherRepository.findByUseridAndGoodVoucher(userid, goodvoucherId);
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
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetGoodVoucherListResponse getGoodVoucherList(int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
    Page<GoodVoucher> goodVoucherPage = goodvoucherRepository.findAll(pageRequest);

    GetGoodVoucherListResponse response = new GetGoodVoucherListResponse();
    response.setCurrentpage(goodVoucherPage.getNumber() + 1);
    response.setDataList(goodVoucherPage.getContent());
    response.setTotalpage(goodVoucherPage.getTotalPages());
    return response;
  }

  public GoodVoucher getGoodVoucher(String voucherid) {
    return goodvoucherRepository.findOne(voucherid);
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
   * @param voucherid
   */
  public void deprecateGoodVoucher(String voucherid) {
    // 废弃GoodVoucher
    GoodVoucher goodVoucher = goodvoucherRepository.findOne(voucherid);
    if (null == goodVoucher || goodVoucher.isDeprecated()) {
      return;
    }
    goodVoucher.setDeprecated(true);
    goodvoucherRepository.saveAndFlush(goodVoucher);

    // 废弃UserVoucher
    UserVoucher userVoucher = uservoucherRepository.findByGoodVoucherId(voucherid);
    if (null == userVoucher || userVoucher.isDeprecated()) {
      return;
    }
    userVoucher.setDeprecated(true);
    uservoucherRepository.saveAndFlush(userVoucher);

    // 消除Good对GoodVoucherId的引用
    goodClient.deprecateGoodVoucher(voucherid);
  }

  /**
   * 分页获取可选的兑换方式
   * 
   * @param currentpage
   * @param pagesize
   * @return
   */
  public GetVoucherItemListResponse getVoucherItemList(int currentpage, int pagesize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentpage - 1, pagesize);
    Page<VoucherItem> voucheritemPage = voucheritemRepository.findAll(pageRequest);
    GetVoucherItemListResponse response = new GetVoucherItemListResponse();
    response.setCurrentpage(voucheritemPage.getNumber() + 1);
    response.setDataList(voucheritemPage.getContent());
    response.setTotalpage(voucheritemPage.getTotalPages());
    return response;
  }

  /**
   * 获取兑换方式
   * 
   * @param voucheritemId
   * @return
   */
  public VoucherItem getVoucherItem(String voucheritemId) {
    return voucheritemRepository.findOne(voucheritemId);
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
      VoucherItem oriVoucherItem = voucheritemRepository.findOne(voucherItem.getId());
      if (oriVoucherItem != null) {
        oriVoucherItem.update(voucherItem);
        return voucheritemRepository.saveAndFlush(oriVoucherItem);
      }
    }
    return voucheritemRepository.saveAndFlush(voucherItem);
  }

  /**
   * 废弃兑换项
   * 
   * @param voucheritemId
   */
  public void deprecateVoucherItem(String voucheritemId) {
    // 废弃VoucherItem
    VoucherItem voucherItem = voucheritemRepository.findOne(voucheritemId);
    if (null == voucherItem || voucherItem.isDeprecated()) {
      return;
    }
    voucherItem.setDeprecated(true);
    voucheritemRepository.saveAndFlush(voucherItem);

    // GoodVoucher中引用的VoucherItem也删除
    goodvoucherRepository.deprecateVoucherItem(voucheritemId);
  }

}
