package cn.aijiamuyingfang.server.wxservice.service;

import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.TemplateMsg;
import cn.aijiamuyingfang.server.commons.controller.bean.wxservice.TemplateMsgKeyValue;
import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage.Data;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * 预约到货通知
   */
  private static final String PREORDER_TEMPLATE_ID = "hm-Gye_mB5CgFOn2v_jneFILQo8Mb7VkxvSeld_euDk";

  /**
   * 订单自提通知
   */
  private static final String PICKUP_TEMPLATE_ID = "8pupoKmzj3dton53L5VDhKFtlNP4HOPVkTNvxs7TsIQ";

  /**
   * 快递发货通知
   */
  private static final String THIRDSEND_TEMPLATE_ID = "-VmbcooMwz5afl7hBrkQFzVrct0iGcMZ2-MEBDkPStI";

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
   * @param openid
   * @param msgData
   * @throws WxErrorException
   */
  public void sendPreOrderMsg(String openid, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(openid, PREORDER_TEMPLATE_ID, msgData);
  }

  /**
   * 发送订单自提通知
   * 
   * @param openid
   * @param msgData
   * @throws WxErrorException
   */
  public void sendPickupMsg(String openid, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(openid, PICKUP_TEMPLATE_ID, msgData);
  }

  /**
   * 发送快递发货通知
   * 
   * @param openid
   * @param msgData
   * @throws WxErrorException
   */
  public void sendThirdSendMsg(String openid, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(openid, THIRDSEND_TEMPLATE_ID, msgData);
  }

  /**
   * 发送订单配送通知
   * 
   * @param openid
   * @param msgData
   * @throws WxErrorException
   */
  public void sendOwnSendMsg(String openid, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(openid, OWNSEND_TEMPLATE_ID, msgData);
  }

  /**
   * 发送订单超时提醒
   * 
   * @param openid
   * @param msgData
   * @throws WxErrorException
   */
  public void sendOrderOverTimeMsg(String openid, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(openid, OVERTIME_TEMPLATE_ID, msgData);
  }

  /**
   * 发送 订单确认通知
   * 
   * @param openid
   * @param msgData
   * @throws WxErrorException
   */
  public void sendOrderConfirmMsg(String openid, TemplateMsg msgData) throws WxErrorException {
    sendTemplateMsg(openid, ORDERCONFIRM_TEMPLATE_ID, msgData);
  }

  /**
   * 发送模板消息
   * 
   * @param openid
   * @param templateid
   * @param msgData
   * @throws WxErrorException
   */
  private void sendTemplateMsg(String openid, String templateid, TemplateMsg msgData) throws WxErrorException {
    WxMaTemplateMessage message = new WxMaTemplateMessage();
    message.setToUser(openid);
    message.setTemplateId(templateid);
    message.setPage(msgData.getPage());
    message.setFormId(msgData.getFormid());
    for (TemplateMsgKeyValue keyWordData : msgData.getData()) {
      message.addData(new Data(keyWordData.getName(), keyWordData.getValue()));
    }
    msgService.sendTemplateMsg(message);
  }
}
