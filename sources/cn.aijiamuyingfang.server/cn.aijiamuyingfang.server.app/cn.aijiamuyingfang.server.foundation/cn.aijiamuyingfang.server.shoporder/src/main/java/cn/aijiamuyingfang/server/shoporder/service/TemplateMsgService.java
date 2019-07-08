package cn.aijiamuyingfang.server.shoporder.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.commons.utils.CollectionUtils;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import cn.aijiamuyingfang.server.feign.StoreClient;
import cn.aijiamuyingfang.server.feign.TemplateMsgClient;
import cn.aijiamuyingfang.server.feign.UserClient;
import cn.aijiamuyingfang.server.feign.domain.good.Good;
import cn.aijiamuyingfang.server.feign.domain.message.TemplateMsg;
import cn.aijiamuyingfang.server.feign.domain.message.TemplateMsgKeyValue;
import cn.aijiamuyingfang.server.feign.domain.store.StoreAddress;
import cn.aijiamuyingfang.server.feign.domain.user.RecieveAddress;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderDTO;
import cn.aijiamuyingfang.server.shoporder.dto.ShopOrderItemDTO;

/**
 * [描述]:
 * <p>
 * 客户端调用TemplateMsgController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-12 02:30:00
 */
@Service
public class TemplateMsgService {
  /**
   * 时间的表达式
   */
  private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

  @Autowired
  private TemplateMsgClient templateMsgClient;

  @Autowired
  private StoreClient storeClient;

  @Autowired
  private UserClient userClient;

  /**
   * 发送预定单所有商品已到货的消息(需要用户确认预订单是否发货)
   * 
   * @param username
   * @param order
   * @param updatedGood
   */
  public void sendPreOrderMsg(String username, ShopOrderDTO order, Good updatedGood) {
    if (null == order || null == updatedGood) {
      return;
    }
    if (StringUtils.isEmpty(username)) {
      return;
    }

    String keyword1Value = updatedGood.getName();
    String keyword2Value = updatedGood.getPrice() + "";
    String keyword3Value = "您预订单中的商品已全部到货,点击该消息确认是否发货";
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    String keyword4Value = dateFormat.format(order.getCreateTime());

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value);
    templateMsgClient.sendPreOrderMsg(username, message);
  }

  /**
   * 发送用户自取订单消息(订单已经准备好,可以自取)
   * 
   * @param username
   * @param order
   */
  public void sendPickupMsg(String username, ShopOrderDTO order) {
    if (null == order) {
      return;
    }
    if (StringUtils.isEmpty(username)) {
      return;
    }

    String keyword1Value = order.getOrderNo();

    StringBuilder contentSB = new StringBuilder();
    List<ShopOrderItemDTO> shoporderitemList = order.getOrderItemList();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItemDTO item : shoporderitemList) {
        contentSB.append(item.getGoodName()).append(" ");
        contentSB.append(item.getCount()).append(item.getGoodPack()).append("\n");
      }
    }

    String goodsNameStr = contentSB.toString();
    String keyword2Value = goodsNameStr.substring(0, goodsNameStr.length() - 1);

    String keyword3Value = "爱家母婴坊";
    StoreAddress storeAddress = storeClient.getStoreAddressByAddressId(order.getPickupStoreAddressId()).getData();
    String keyword4Value = storeAddress.getDetail();

    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    String keyword5Value = dateFormat.format(order.getPickupTime());
    String keyword6Value = "亲您的商品已经准备完毕,请准时取货;如有疑问:请联系13852827612,13815740142";

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value,
        keyword5Value, keyword6Value);
    templateMsgClient.sendPickupMsg(username, message);

  }

  /**
   * 发送快递订单消息(订单已发货)
   * 
   * @param username
   * @param order
   */
  public void sendThirdSendMsg(String username, ShopOrderDTO order) {
    if (null == order) {
      return;
    }
    if (StringUtils.isEmpty(username)) {
      return;
    }
    String keyword1Value = order.getOrderNo();

    StringBuilder contentSB = new StringBuilder();
    List<ShopOrderItemDTO> shoporderitemList = order.getOrderItemList();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItemDTO item : shoporderitemList) {
        contentSB.append(item.getGoodName()).append(" ");
        contentSB.append(item.getCount()).append(item.getGoodPack()).append("\n");
      }
    }
    String goodsNameStr = contentSB.toString();
    String keyword2Value = goodsNameStr.substring(0, goodsNameStr.length() - 1);

    String keyword3Value = order.getThirdsendCompany();
    String keyword4Value = order.getThirdsendNo();

    RecieveAddress recieveAddress = userClient.getRecieveAddress(order.getUsername(), order.getRecieveAddressId())
        .getData();
    String keyword5Value = recieveAddress.getDetail();

    List<String> operatorList = order.getOperator();
    String keyword6Value = operatorList.get(operatorList.size() - 1);
    String keyword7Value = "您的商品已发货,请注意查收;如有疑问:请联系13852827612,13815740142";

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value,
        keyword5Value, keyword6Value, keyword7Value);
    templateMsgClient.sendThirdSendMsg(username, message);
  }

  /**
   * 发送送货上门订单消息(订单已发货)
   * 
   * @param username
   * @param order
   */
  public void sendOwnSendMsg(String username, ShopOrderDTO order) {
    if (null == order) {
      return;
    }
    if (StringUtils.isEmpty(username)) {
      return;
    }

    String keyword1Value = order.getOrderNo();
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
    String keyword2Value = dateFormat.format(order.getCreateTime());
    RecieveAddress recieveAddress = userClient.getRecieveAddress(order.getUsername(), order.getRecieveAddressId())
        .getData();
    String keyword3Value = recieveAddress.getDetail();
    String keyword4Value = order.getThirdsendNo();

    StringBuilder contentSB = new StringBuilder();
    List<ShopOrderItemDTO> shoporderitemList = order.getOrderItemList();
    if (!CollectionUtils.isEmpty(shoporderitemList)) {
      for (ShopOrderItemDTO item : shoporderitemList) {
        contentSB.append(item.getGoodName()).append(" ");
        contentSB.append(item.getCount()).append(item.getGoodPack()).append("\n");
      }
    }
    String contentStr = contentSB.toString();
    String keyword5Value = contentStr.substring(0, contentStr.length() - 1);

    String keyword6Value = "您的商品已经准备派送,请保持手机畅通";
    String keyword7Value = order.getTotalPrice() + "";

    TemplateMsg message = createTemplateMsg(order, keyword1Value, keyword2Value, keyword3Value, keyword4Value,
        keyword5Value, keyword6Value, keyword7Value);
    templateMsgClient.sendOwnSendMsg(username, message);
  }

  /**
   * 发送订单超时消息
   * 
   * @param username
   * @param msgData
   */
  public void sendOrderOverTimeMsg(String username, TemplateMsg msgData) {
    templateMsgClient.sendOrderOverTimeMsg(username, msgData);
  }

  /**
   * 发送订单确认消息
   * 
   * @param username
   * @param msgData
   */
  public void sendOrderConfirmMsg(String username, TemplateMsg msgData) {
    templateMsgClient.sendOrderConfirmMsg(username, msgData);
  }

  /**
   * 根据订单创建模板消息类
   *
   * @param shoporder
   * @param messagedata
   */
  private TemplateMsg createTemplateMsg(ShopOrderDTO shoporder, String... messagedata) {
    TemplateMsg message = new TemplateMsg();
    message.setPage("/pages/order_detail?shop_order_id=" + shoporder.getId());
    message.setFormid(shoporder.getFormid());

    List<TemplateMsgKeyValue> messagedataList = new ArrayList<>();
    int keyworkIndex = 1;
    for (String item : messagedata) {
      TemplateMsgKeyValue value = new TemplateMsgKeyValue("keyword" + keyworkIndex, item);
      messagedataList.add(value);
      keyworkIndex++;
    }

    message.setData(messagedataList);
    return message;
  }
}
