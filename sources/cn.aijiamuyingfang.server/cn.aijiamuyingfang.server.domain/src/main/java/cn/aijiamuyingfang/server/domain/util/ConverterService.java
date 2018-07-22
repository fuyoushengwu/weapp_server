package cn.aijiamuyingfang.server.domain.util;

import cn.aijiamuyingfang.commons.domain.address.RecieveAddress;
import cn.aijiamuyingfang.commons.domain.address.RecieveAddressRequest;
import cn.aijiamuyingfang.commons.domain.address.StoreAddress;
import cn.aijiamuyingfang.commons.domain.address.StoreAddressRequest;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucher;
import cn.aijiamuyingfang.commons.domain.coupon.GoodVoucherRequest;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItem;
import cn.aijiamuyingfang.commons.domain.coupon.VoucherItemRequest;
import cn.aijiamuyingfang.commons.domain.goods.Classify;
import cn.aijiamuyingfang.commons.domain.goods.ClassifyRequest;
import cn.aijiamuyingfang.commons.domain.goods.Good;
import cn.aijiamuyingfang.commons.domain.goods.GoodRequest;
import cn.aijiamuyingfang.commons.domain.goods.Store;
import cn.aijiamuyingfang.commons.domain.goods.StoreRequest;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItem;
import cn.aijiamuyingfang.commons.domain.shoporder.PreviewOrderItemRequest;
import cn.aijiamuyingfang.commons.domain.user.User;
import cn.aijiamuyingfang.commons.domain.user.UserMessage;
import cn.aijiamuyingfang.commons.domain.user.UserMessageRequest;
import cn.aijiamuyingfang.commons.domain.user.UserRequest;
import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 将请求的RequestBean转换为DomainBean的工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-04 19:45:26
 */
@Service
public class ConverterService {

  public GoodVoucher from(GoodVoucherRequest request) {
    GoodVoucher goodVoucher = new GoodVoucher();
    if (null == request) {
      return goodVoucher;
    }
    goodVoucher.setName(request.getName());
    goodVoucher.setScore(request.getScore());
    goodVoucher.setDescription(request.getDescription());
    if (!CollectionUtils.isEmpty(request.getVoucheritemIdList())) {
      goodVoucher.setVoucheritemIdList(request.getVoucheritemIdList());
    }
    return goodVoucher;
  }

  public VoucherItem from(VoucherItemRequest request) {
    VoucherItem voucherItem = new VoucherItem();
    if (null == request) {
      return voucherItem;
    }
    voucherItem.setName(request.getName());
    voucherItem.setScore(request.getScore());
    voucherItem.setDescription(request.getDescription());
    voucherItem.setGoodid(request.getGoodid());
    return voucherItem;
  }

  public Classify from(ClassifyRequest request) {
    Classify classify = new Classify();
    if (null == request) {
      return classify;
    }
    classify.setName(request.getName());
    return classify;
  }

  public Good from(GoodRequest request) {
    Good good = new Good();
    if (null == request) {
      return good;
    }
    good.setName(request.getName());
    good.setCount(request.getCount());
    good.setSalecount(request.getSalecount());
    good.setPrice(request.getPrice());
    good.setMarketprice(request.getMarketprice());
    good.setPack(request.getPack());
    good.setLevel(request.getLevel());
    good.setBarcode(request.getBarcode());
    good.setScore(request.getScore());
    good.setLifetime(request.getLifetime());
    good.setVoucherId(request.getVoucherId());

    return good;
  }

  public Store from(StoreRequest request) {
    Store store = new Store();
    if (null == request) {
      return store;
    }
    store.setName(request.getName());
    store.setWorkTime(request.getWorkTime());
    store.setStoreAddress(from(request.getStoreaddressRequest()));

    return store;
  }

  public StoreAddress from(StoreAddressRequest request) {
    StoreAddress storeAddress = new StoreAddress();
    if (null == request) {
      return storeAddress;
    }
    storeAddress.setProvince(request.getProvince());
    storeAddress.setCity(request.getCity());
    storeAddress.setCounty(request.getCounty());
    storeAddress.setTown(request.getTown());
    storeAddress.setDetail(request.getDetail());
    storeAddress.setCoordinate(request.getCoordinate());
    storeAddress.setPhone(request.getPhone());
    storeAddress.setContactor(request.getContactor());

    return storeAddress;
  }

  public PreviewOrderItem from(PreviewOrderItemRequest request) {
    PreviewOrderItem item = new PreviewOrderItem();
    if (null == request) {
      return item;
    }
    item.setCount(request.getCount());
    item.setGood(request.getGood());
    item.setShopcartItemId(request.getShopcartItemId());
    return item;
  }

  public User from(UserRequest request) {
    User user = new User();
    if (null == request) {
      return user;
    }
    user.setNickname(request.getNickname());
    user.setAvatar(request.getAvatar());
    user.setPhone(request.getPhone());
    user.setJscode(request.getJscode());
    return user;
  }

  public RecieveAddress from(RecieveAddressRequest request) {
    RecieveAddress address = new RecieveAddress();
    if (null == request) {
      return address;
    }
    address.setProvince(request.getProvince());
    address.setCity(request.getCity());
    address.setCounty(request.getCounty());
    address.setTown(request.getTown());
    address.setDetail(request.getDetail());
    address.setCoordinate(request.getCoordinate());
    address.setUserid(request.getUserid());
    address.setPhone(request.getPhone());
    address.setReciever(request.getReciever());
    return address;
  }

  public UserMessage from(UserMessageRequest request) {
    UserMessage usermessage = new UserMessage();
    if (null == request) {
      return usermessage;
    }
    usermessage.setUserid(request.getUserid());
    usermessage.setType(request.getType());
    usermessage.setTitle(request.getTitle());
    usermessage.setRoundup(request.getRoundup());
    usermessage.setContent(request.getContent());
    usermessage.setCreateTime(request.getCreateTime());
    usermessage.setFinishTime(request.getFinishTime());
    usermessage.setReaded(request.isReaded());
    return usermessage;
  }

}
