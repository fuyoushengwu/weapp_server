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
import cn.aijiamuyingfang.server.coupon.domain.response.PagableGoodVoucherList;
import cn.aijiamuyingfang.server.coupon.domain.response.PagableUserVoucherList;
import cn.aijiamuyingfang.server.coupon.domain.response.PagableVoucherItemList;
import cn.aijiamuyingfang.server.coupon.dto.GoodVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.UserVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.VoucherItemDTO;
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
  public PagableUserVoucherList getUserVoucherList(String username, int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<UserVoucherDTO> userVoucherPage = uservoucherRepository.findByUsername(username, pageRequest);
    PagableUserVoucherList response = new PagableUserVoucherList();
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
  public UserVoucherDTO getUserVoucher(String voucherId) {
    return uservoucherRepository.findOne(voucherId);
  }

  /**
   * 获得用户的兑换券
   * 
   * @param username
   * @param goodvoucherId
   * @return
   */
  public UserVoucherDTO getUserVoucherForGoodVoucher(String username, String goodvoucherId) {
    return uservoucherRepository.findByUsernameAndGoodVoucher(username, goodvoucherId);
  }

  /**
   * 更新用户兑换券
   * 
   * @param userVoucherList
   */
  public void updateUserVoucher(List<UserVoucherDTO> userVoucherList) {
    if (CollectionUtils.hasContent(userVoucherList)) {
      for (UserVoucherDTO userVoucher : userVoucherList) {
        updateUserVoucher(userVoucher);
      }
    }
  }

  /**
   * 更新用户兑换券
   * 
   * @param userVoucher
   */
  public void updateUserVoucher(UserVoucherDTO userVoucher) {
    if (null == userVoucher) {
      return;
    }
    if (StringUtils.isEmpty(userVoucher.getId())) {
      uservoucherRepository.saveAndFlush(userVoucher);
      return;
    }
    UserVoucherDTO oriUserVoucher = uservoucherRepository.findOne(userVoucher.getId());
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
  public PagableGoodVoucherList getGoodVoucherList(int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<GoodVoucherDTO> goodVoucherPage = goodvoucherRepository.findAll(pageRequest);

    PagableGoodVoucherList response = new PagableGoodVoucherList();
    response.setCurrentPage(goodVoucherPage.getNumber() + 1);
    response.setDataList(goodVoucherPage.getContent());
    response.setTotalpage(goodVoucherPage.getTotalPages());
    return response;
  }

  public GoodVoucherDTO getGoodVoucher(String voucherId) {
    return goodvoucherRepository.findOne(voucherId);
  }

  /**
   * 创建商品兑换券
   * 
   * @param goodVoucher
   * @return
   */
  public GoodVoucherDTO createORUpdateGoodVoucher(GoodVoucherDTO goodVoucher) {
    if (null == goodVoucher) {
      return null;
    }
    if (StringUtils.hasContent(goodVoucher.getId())) {
      GoodVoucherDTO oriGoodVoucher = goodvoucherRepository.findOne(goodVoucher.getId());
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
    GoodVoucherDTO goodVoucher = goodvoucherRepository.findOne(voucherId);
    if (null == goodVoucher || goodVoucher.isDeprecated()) {
      return;
    }
    goodVoucher.setDeprecated(true);
    goodvoucherRepository.saveAndFlush(goodVoucher);

    // 废弃UserVoucher
    UserVoucherDTO userVoucher = uservoucherRepository.findByGoodVoucherId(voucherId);
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
  public PagableVoucherItemList getVoucherItemList(int currentPage, int pageSize) {
    // PageRequest的Page参数是基于0的,但是currentPage是基于1的,所有将currentPage作为参数传递给PgeRequest时需要'-1'
    PageRequest pageRequest = new PageRequest(currentPage - 1, pageSize);
    Page<VoucherItemDTO> voucherItemPage = voucherItemRepository.findAll(pageRequest);
    PagableVoucherItemList response = new PagableVoucherItemList();
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
  public VoucherItemDTO getVoucherItem(String voucherItemId) {
    return voucherItemRepository.findOne(voucherItemId);
  }

  /**
   * 创建兑换选择项
   * 
   * @param voucherItem
   * @return
   */
  public VoucherItemDTO createORUpdateVoucherItem(VoucherItemDTO voucherItem) {
    if (null == voucherItem) {
      return null;
    }
    if (StringUtils.hasContent(voucherItem.getId())) {
      VoucherItemDTO oriVoucherItem = voucherItemRepository.findOne(voucherItem.getId());
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
    VoucherItemDTO voucherItem = voucherItemRepository.findOne(voucherItemId);
    if (null == voucherItem || voucherItem.isDeprecated()) {
      return;
    }
    voucherItem.setDeprecated(true);
    voucherItemRepository.saveAndFlush(voucherItem);

    // GoodVoucher中引用的VoucherItem也删除
    goodvoucherRepository.deprecateVoucherItem(voucherItemId);
  }

}
