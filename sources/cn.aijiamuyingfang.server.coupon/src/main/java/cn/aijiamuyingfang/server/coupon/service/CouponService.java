package cn.aijiamuyingfang.server.coupon.service;

import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.UserVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetGoodVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetShopOrderVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetUserVoucherListResponse;
import cn.aijiamuyingfang.commons.domain.coupon.response.GetVoucherItemListResponse;
import cn.aijiamuyingfang.commons.domain.shoporder.ShopOrderVoucher;
import cn.aijiamuyingfang.server.domain.coupon.db.GoodVoucherRepository;
import cn.aijiamuyingfang.server.domain.coupon.db.UserVoucherRepository;
import cn.aijiamuyingfang.server.domain.coupon.db.VoucherItemRepository;
import cn.aijiamuyingfang.server.domain.goods.db.GoodRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
  private UserVoucherRepository uservoucherRepository;

  @Autowired
  private GoodVoucherRepository goodvoucherRepository;

  @Autowired
  private VoucherItemRepository voucheritemRepository;

  @Autowired
  private GoodRepository goodRepository;

  /**
   * 分页获得用户的兑换券
   * 
   * @param userid
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
   * 获得购买商品时可用的兑换券
   * 
   * @param userid
   * @param goodids
   * @return
   */
  public GetShopOrderVoucherListResponse getUserShopOrderVoucherList(String userid, List<String> goodids) {
    GetShopOrderVoucherListResponse response = new GetShopOrderVoucherListResponse();

    List<UserVoucher> voucherList = uservoucherRepository.findByUserid(userid);
    for (UserVoucher voucher : voucherList) {
      List<String> itemidList = voucher.getGoodVoucher().getVoucheritemIdList();
      for (String itemid : itemidList) {
        VoucherItem item = voucheritemRepository.findOne(itemid);
        if (item != null && goodids.contains(item.getGoodid()) && item.getScore() <= voucher.getScore()) {
          ShopOrderVoucher shoporderVoucher = new ShopOrderVoucher();
          shoporderVoucher.setUserVoucher(voucher);
          shoporderVoucher.setVoucherItem(item);
          shoporderVoucher.setGood(goodRepository.findOne(item.getGoodid()));
          response.addUsefulHolderCart(shoporderVoucher);
          break;
        }
      }
    }
    return response;
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

  /**
   * 创建商品兑换券
   * 
   * @param goodVoucher
   * @return
   */
  public GoodVoucher createGoodVoucher(GoodVoucher goodVoucher) {
    if (goodVoucher != null) {
      goodvoucherRepository.saveAndFlush(goodVoucher);
    }
    return goodVoucher;
  }

  /**
   * 废弃商品兑换券
   * 
   * @param voucherid
   * @return
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
    goodRepository.deprecateGoodVoucher(voucherid);

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
   * 创建兑换选择项
   * 
   * @param voucherItem
   * @return
   */
  public VoucherItem createVoucherItem(VoucherItem voucherItem) {
    if (voucherItem != null) {
      voucheritemRepository.saveAndFlush(voucherItem);
    }
    return voucherItem;
  }

  /**
   * 废弃兑换项
   * 
   * @param itemid
   */
  public void deprecateVoucherItem(String itemid) {
    // 废弃VoucherItem
    VoucherItem voucherItem = voucheritemRepository.findOne(itemid);
    if (null == voucherItem || voucherItem.isDeprecated()) {
      return;
    }
    voucherItem.setDeprecated(true);
    voucheritemRepository.saveAndFlush(voucherItem);

    // GoodVoucher中引用的VoucherItem也删除
    goodvoucherRepository.deprecateVoucherItem(itemid);
  }

}
