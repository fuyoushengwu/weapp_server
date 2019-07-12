package cn.aijiamuyingfang.server.coupon.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.server.coupon.dto.GoodVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.UserVoucherDTO;
import cn.aijiamuyingfang.server.coupon.dto.VoucherItemDTO;
import cn.aijiamuyingfang.server.coupon.service.CouponService;
import cn.aijiamuyingfang.vo.coupon.GoodVoucher;
import cn.aijiamuyingfang.vo.coupon.UserVoucher;
import cn.aijiamuyingfang.vo.coupon.VoucherItem;

@Service
public class ConvertService {

  @Autowired
  private CouponService couponService;

  public UserVoucher convertUserVoucherDTO(UserVoucherDTO userVoucherDTO) {
    if (null == userVoucherDTO) {
      return null;
    }
    UserVoucher userVoucher = new UserVoucher();
    userVoucher.setId(userVoucherDTO.getId());
    userVoucher.setDeprecated(userVoucherDTO.isDeprecated());
    userVoucher.setUsername(userVoucherDTO.getUsername());
    userVoucher.setGoodVoucher(convertGoodVoucherDTO(userVoucherDTO.getGoodVoucher()));
    userVoucher.setScore(userVoucherDTO.getScore());
    return userVoucher;
  }

  public List<UserVoucher> convertUserVoucherDTOList(List<UserVoucherDTO> userVoucherDTOList) {
    List<UserVoucher> userVoucherList = new ArrayList<>();
    if (null == userVoucherDTOList) {
      return userVoucherList;
    }
    for (UserVoucherDTO userVoucherDTO : userVoucherDTOList) {
      UserVoucher userVoucher = convertUserVoucherDTO(userVoucherDTO);
      if (userVoucher != null) {
        userVoucherList.add(userVoucher);
      }
    }
    return userVoucherList;
  }

  public UserVoucherDTO convertUserVoucher(UserVoucher userVoucher) {
    if (null == userVoucher) {
      return null;
    }
    UserVoucherDTO userVoucherDTO = new UserVoucherDTO();
    userVoucherDTO.setId(userVoucher.getId());
    userVoucherDTO.setDeprecated(userVoucher.isDeprecated());
    userVoucherDTO.setUsername(userVoucher.getUsername());
    userVoucherDTO.setGoodVoucher(convertGoodVoucher(userVoucher.getGoodVoucher()));
    userVoucherDTO.setScore(userVoucher.getScore());
    return userVoucherDTO;
  }

  public List<UserVoucherDTO> convertUserVoucherList(List<UserVoucher> userVoucherList) {
    List<UserVoucherDTO> userVoucherDTOList = new ArrayList<>();
    if (null == userVoucherList) {
      return userVoucherDTOList;
    }
    for (UserVoucher userVoucher : userVoucherList) {
      UserVoucherDTO userVoucherDTO = convertUserVoucher(userVoucher);
      if (userVoucherDTO != null) {
        userVoucherDTOList.add(userVoucherDTO);
      }
    }
    return userVoucherDTOList;
  }

  public GoodVoucher convertGoodVoucherDTO(GoodVoucherDTO goodVoucherDTO) {
    if (null == goodVoucherDTO) {
      return null;
    }
    GoodVoucher goodVoucher = new GoodVoucher();
    goodVoucher.setId(goodVoucherDTO.getId());
    goodVoucher.setDeprecated(goodVoucherDTO.isDeprecated());
    goodVoucher.setName(goodVoucherDTO.getName());
    goodVoucher.setVoucherItemList(couponService.getVoucherItemList(goodVoucherDTO.getVoucherItemIdList()));
    goodVoucher.setDescription(goodVoucherDTO.getDescription());
    goodVoucher.setScore(goodVoucherDTO.getScore());
    return goodVoucher;
  }

  public List<GoodVoucher> convertGoodVoucherDTOList(List<GoodVoucherDTO> goodVoucherDTOList) {
    List<GoodVoucher> goodVoucherList = new ArrayList<>();
    if (null == goodVoucherDTOList) {
      return goodVoucherList;
    }
    for (GoodVoucherDTO goodVoucherDTO : goodVoucherDTOList) {
      GoodVoucher goodVoucher = convertGoodVoucherDTO(goodVoucherDTO);
      if (goodVoucher != null) {
        goodVoucherList.add(goodVoucher);
      }
    }
    return goodVoucherList;
  }

  public GoodVoucherDTO convertGoodVoucher(GoodVoucher goodVoucher) {
    if (null == goodVoucher) {
      return null;
    }
    GoodVoucherDTO goodVoucherDTO = new GoodVoucherDTO();
    goodVoucherDTO.setId(goodVoucher.getId());
    goodVoucherDTO.setDeprecated(goodVoucher.isDeprecated());
    goodVoucherDTO.setName(goodVoucher.getName());
    if (CollectionUtils.hasContent(goodVoucher.getVoucherItemList())) {
      List<String> voucherItemIdList = new ArrayList<>();
      for (VoucherItem voucherItem : goodVoucher.getVoucherItemList()) {
        voucherItemIdList.add(voucherItem.getId());
      }
      goodVoucherDTO.setVoucherItemIdList(voucherItemIdList);
    }
    goodVoucherDTO.setDescription(goodVoucher.getDescription());
    goodVoucherDTO.setScore(goodVoucher.getScore());
    return goodVoucherDTO;
  }

  public List<GoodVoucherDTO> convertGoodVoucherList(List<GoodVoucher> goodVoucherList) {
    List<GoodVoucherDTO> goodVoucherDTOList = new ArrayList<>();
    if (null == goodVoucherList) {
      return goodVoucherDTOList;
    }
    for (GoodVoucher goodVoucher : goodVoucherList) {
      GoodVoucherDTO goodVoucherDTO = convertGoodVoucher(goodVoucher);
      if (goodVoucherDTO != null) {
        goodVoucherDTOList.add(goodVoucherDTO);
      }
    }
    return goodVoucherDTOList;
  }

  public VoucherItem convertVoucherItemDTO(VoucherItemDTO voucherItemDTO) {
    if (null == voucherItemDTO) {
      return null;
    }
    VoucherItem voucherItem = new VoucherItem();
    voucherItem.setId(voucherItemDTO.getId());
    voucherItem.setDeprecated(voucherItemDTO.isDeprecated());
    voucherItem.setName(voucherItemDTO.getName());
    voucherItem.setDescription(voucherItemDTO.getDescription());
    voucherItem.setGoodId(voucherItemDTO.getGoodId());
    voucherItem.setScore(voucherItemDTO.getScore());
    return voucherItem;
  }

  public List<VoucherItem> convertVoucherItemDTOList(List<VoucherItemDTO> voucherItemDTOList) {
    List<VoucherItem> voucherItemList = new ArrayList<>();
    if (null == voucherItemDTOList) {
      return voucherItemList;
    }
    for (VoucherItemDTO voucherItemDTO : voucherItemDTOList) {
      VoucherItem voucherItem = convertVoucherItemDTO(voucherItemDTO);
      if (voucherItem != null) {
        voucherItemList.add(voucherItem);
      }
    }
    return voucherItemList;
  }

  public VoucherItemDTO convertVoucherItem(VoucherItem voucherItem) {
    if (null == voucherItem) {
      return null;
    }
    VoucherItemDTO voucherItemDTO = new VoucherItemDTO();
    voucherItemDTO.setId(voucherItem.getId());
    voucherItemDTO.setDeprecated(voucherItem.isDeprecated());
    voucherItemDTO.setName(voucherItem.getName());
    voucherItemDTO.setDescription(voucherItem.getDescription());
    voucherItemDTO.setGoodId(voucherItem.getGoodId());
    voucherItemDTO.setScore(voucherItem.getScore());
    return voucherItemDTO;
  }

  public List<VoucherItemDTO> convertVoucherItemList(List<VoucherItem> voucherItemList) {
    List<VoucherItemDTO> voucherItemDTOList = new ArrayList<>();
    if (null == voucherItemList) {
      return voucherItemDTOList;
    }
    for (VoucherItem voucherItem : voucherItemList) {
      VoucherItemDTO voucherItemDTO = convertVoucherItem(voucherItem);
      if (voucherItemDTO != null) {
        voucherItemDTOList.add(voucherItemDTO);
      }
    }
    return voucherItemDTOList;
  }
}
