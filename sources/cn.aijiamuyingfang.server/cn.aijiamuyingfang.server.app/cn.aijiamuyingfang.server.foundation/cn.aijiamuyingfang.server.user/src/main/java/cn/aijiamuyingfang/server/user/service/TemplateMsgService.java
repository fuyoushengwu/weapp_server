package cn.aijiamuyingfang.server.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.user.domain.TemplateMsg;
import cn.aijiamuyingfang.server.user.domain.TemplateMsgKeyValue;
import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage.Data;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * [描述]:
 * <p>
 * 模板消息Service
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 15:05:33
 */
@Service
public class TemplateMsgService {
  /**
   * 快递发货通知
   */
  private static final String THIRDSEND_TEMPLATE_ID = "-VmbcooMwz5afl7hBrkQF1SvY48Ta1H8N6GSJeeLGoU";

  /**
   * 预约到货通知
   */
  private static final String PREORDER_TEMPLATE_ID = "hm-Gye_mB5CgFOn2v_jneFILQo8Mb7VkxvSeld_euDk";

  /**
   * 订单自提通知
   */
  private static final String PICKUP_TEMPLATE_ID = "8pupoKmzj3dton53L5VDhKFtlNP4HOPVkTNvxs7TsIQ";

  /**
   * 订单配送通知
   */
  private static final String OWNSEND_TEMPLATE_ID = "RyVXhozayB_YMFV2jHLURo2r1otY5QkRun0R_PhLrFM";

  /**
   * 订单超时提醒
   */
  private static final String OVERTIME_TEMPLATE_ID = "ISxOxAzimQbslKQ6cUFzoIti97b3lO5GSrv9AhENFt0";

  /**
   * 订单确认通知
   */
  private static final String ORDERCONFIRM_TEMPLATE_ID = "pksYUtTAf6x3PefNgkwf3DpOBeRRDteLVziZ8HOZ36E";

  @Autowired
  private WxMaMsgService msgService;

  /**
   * 发送预约到货消息
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  public void sendPreOrderMsg(String username, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(username, PREORDER_TEMPLATE_ID, msgData);
  }

  /**
   * 发送订单自提通知
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  public void sendPickupMsg(String username, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(username, PICKUP_TEMPLATE_ID, msgData);
  }

  /**
   * 发送快递发货通知
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  public void sendThirdSendMsg(String username, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(username, THIRDSEND_TEMPLATE_ID, msgData);
  }

  /**
   * 发送订单配送通知
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  public void sendOwnSendMsg(String username, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(username, OWNSEND_TEMPLATE_ID, msgData);
  }

  /**
   * 发送订单超时提醒
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  public void sendOrderOverTimeMsg(String username, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(username, OVERTIME_TEMPLATE_ID, msgData);
  }

  /**
   * 发送 订单确认通知
   * 
   * @param username
   * @param msgData
   * @throws WxErrorException
   */
  public void sendOrderConfirmMsg(String username, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(username, ORDERCONFIRM_TEMPLATE_ID, msgData);
  }

  /**
   * 发送模板消息
   * 
   * @param username
   * @param templateid
   * @param msgData
   * @throws WxErrorException
   */
  private void sendTemplateMsg(String username, String templateid, TemplateMsg msgData) throws WxErrorException {
    WxMaTemplateMessage message = new WxMaTemplateMessage();
    message.setToUser(username);
    message.setTemplateId(templateid);
    message.setPage(msgData.getPage());
    message.setFormId(msgData.getFormid());
    for (TemplateMsgKeyValue keyWordData : msgData.getData()) {
      message.addData(new Data(keyWordData.getName(), keyWordData.getValue()));
    }
    msgService.sendTemplateMsg(message);
  }
}
