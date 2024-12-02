package cn.aijiamuyingfang.server.coupon.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import cn.aijiamuyingfang.server.coupon.db.GoodVoucherRepository;
import cn.aijiamuyingfang.server.coupon.db.UserVoucherRepository;
import cn.aijiamuyingfang.server.coupon.db.VoucherItemRepository;
import cn.aijiamuyingfang.server.coupon.dto.GoodVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.UserVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.VoucherItemDTO;
import cn.aijiamuyingfang.server.coupon.utils.ConvertService;
import cn.aijiamuyingfang.server.feign.GoodClient;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import cn.aijiamuyingfang.vo.coupon.PagableGoodVoucherList;
import cn.aijiamuyingfang.vo.coupon.PagableUserVoucherList;
import cn.aijiamuyingfang.vo.coupon.PagableVoucherItemList;
import cn.aijiamuyingfang.vo.coupon.UserVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;
import cn.aijiamuyingfang.vo.exception.CouponException;
import cn.aijiamuyingfang.vo.utils.StringUtils;

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

  @Autowired
  private ConvertService convertService;

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
    Page<UserVoucherDTO> userVoucherDTOPage = uservoucherRepository.findByUsername(username, pageRequest);
    PagableUserVoucherList response = new PagableUserVoucherList();
    response.setCurrentPage(userVoucherDTOPage.getNumber() + 1);
    response.setDataList(convertService.convertUserVoucherDTOList(userVoucherDTOPage.getContent()));
    response.setTotalpage(userVoucherDTOPage.getTotalPages());
    return response;
  }

  /**
   * 获得用户的兑换券
   * 
   * @param voucherId
   * @return
   */
  public UserVoucher getUserVoucher(String voucherId) {
    return convertService.convertUserVoucherDTO(uservoucherRepository.findOne(voucherId));
  }

  /**
   * 获得用户的兑换券
   * 
   * @param username
   * @param goodvoucherId
   * @return
   */
  public UserVoucher getUserVoucherForGoodVoucher(String username, String goodvoucherId) {
    return convertService
        .convertUserVoucherDTO(uservoucherRepository.findByUsernameAndGoodVoucher(username, goodvoucherId));
  }

  /**
   * 更新用户兑换券
   * 
   * @param userVoucherList
   */
  public void updateUserVoucher(List<UserVoucher> userVoucherList) {
    if (CollectionUtils.isNotEmpty(userVoucherList)) {
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
      uservoucherRepository.saveAndFlush(convertService.convertUserVoucher(userVoucher));
      return;
    }
    UserVoucherDTO oriUserVoucherDTO = uservoucherRepository.findOne(userVoucher.getId());
    if (null == oriUserVoucherDTO) {
      throw new CouponException("404", "UserVoucher[" + userVoucher.getId() + "] not exist");
    }
    oriUserVoucherDTO.update(userVoucher);
    uservoucherRepository.saveAndFlush(oriUserVoucherDTO);
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
    Page<GoodVoucherDTO> goodVoucherDTOPage = goodvoucherRepository.findAll(pageRequest);

    PagableGoodVoucherList response = new PagableGoodVoucherList();
    response.setCurrentPage(goodVoucherDTOPage.getNumber() + 1);
    response.setDataList(convertService.convertGoodVoucherDTOList(goodVoucherDTOPage.getContent()));
    response.setTotalpage(goodVoucherDTOPage.getTotalPages());
    return response;
  }

  public GoodVoucher getGoodVoucher(String voucherId) {
    return convertService.convertGoodVoucherDTO(goodvoucherRepository.findOne(voucherId));
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
      GoodVoucherDTO oriGoodVoucherDTO = goodvoucherRepository.findOne(goodVoucher.getId());
      if (oriGoodVoucherDTO != null) {
        oriGoodVoucherDTO.update(goodVoucher);
        return convertService.convertGoodVoucherDTO(goodvoucherRepository.saveAndFlush(oriGoodVoucherDTO));
      }
    }
    return convertService
        .convertGoodVoucherDTO(goodvoucherRepository.saveAndFlush(convertService.convertGoodVoucher(goodVoucher)));
  }

  /**
   * 废弃商品兑换券
   * 
   * @param voucherId
   */
  public void deprecateGoodVoucher(String voucherId) {
    // 废弃GoodVoucher
    GoodVoucherDTO goodVoucherDTO = goodvoucherRepository.findOne(voucherId);
    if (null == goodVoucherDTO || goodVoucherDTO.isDeprecated()) {
      return;
    }
    goodVoucherDTO.setDeprecated(true);
    goodvoucherRepository.saveAndFlush(goodVoucherDTO);

    // 废弃UserVoucher
    UserVoucherDTO userVoucherDTO = uservoucherRepository.findByGoodVoucherId(voucherId);
    if (null == userVoucherDTO || userVoucherDTO.isDeprecated()) {
      return;
    }
    userVoucherDTO.setDeprecated(true);
    uservoucherRepository.saveAndFlush(userVoucherDTO);

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
    Page<VoucherItemDTO> voucherItemDTOPage = voucherItemRepository.findAll(pageRequest);
    PagableVoucherItemList response = new PagableVoucherItemList();
    response.setCurrentPage(voucherItemDTOPage.getNumber() + 1);
    response.setDataList(convertService.convertVoucherItemDTOList(voucherItemDTOPage.getContent()));
    response.setTotalpage(voucherItemDTOPage.getTotalPages());
    return response;
  }

  /**
   * 获取兑换方式
   * 
   * @param voucherItemId
   * @return
   */
  public VoucherItem getVoucherItem(String voucherItemId) {
    return convertService.convertVoucherItemDTO(voucherItemRepository.findOne(voucherItemId));
  }

  public List<VoucherItem> getVoucherItemList(List<String> voucherItemIdList) {
    return convertService.convertVoucherItemDTOList(voucherItemRepository.findAll(voucherItemIdList));
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
      VoucherItemDTO oriVoucherItemDTO = voucherItemRepository.findOne(voucherItem.getId());
      if (oriVoucherItemDTO != null) {
        oriVoucherItemDTO.update(voucherItem);
        return convertService.convertVoucherItemDTO(voucherItemRepository.saveAndFlush(oriVoucherItemDTO));
      }
    }
    return convertService
        .convertVoucherItemDTO(voucherItemRepository.saveAndFlush(convertService.convertVoucherItem(voucherItem)));
  }

  /**
   * 废弃兑换项
   * 
   * @param voucherItemId
   */
  public void deprecateVoucherItem(String voucherItemId) {
    // 废弃VoucherItem
    VoucherItemDTO voucherItemDTO = voucherItemRepository.findOne(voucherItemId);
    if (null == voucherItemDTO || voucherItemDTO.isDeprecated()) {
      return;
    }
    voucherItemDTO.setDeprecated(true);
    voucherItemRepository.saveAndFlush(voucherItemDTO);

    // GoodVoucher中引用的VoucherItem也删除
    goodvoucherRepository.deprecateVoucherItem(voucherItemId);
  }

}
