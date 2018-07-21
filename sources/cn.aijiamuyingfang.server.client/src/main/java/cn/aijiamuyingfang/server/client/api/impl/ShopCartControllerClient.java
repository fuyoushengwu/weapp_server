package cn.aijiamuyingfang.server.client.api.impl;

import cn.aijiamuyingfang.server.client.annotation.HttpService;
import cn.aijiamuyingfang.server.client.api.ShopCartControllerApi;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseBean;
import cn.aijiamuyingfang.server.commons.controller.bean.ResponseCode;
import cn.aijiamuyingfang.server.commons.utils.JsonUtils;
import cn.aijiamuyingfang.server.domain.exception.ShopCartException;
import cn.aijiamuyingfang.server.domain.shopcart.AddShopCartItemRequest;
import cn.aijiamuyingfang.server.domain.shopcart.GetShopCartItemListResponse;
import cn.aijiamuyingfang.server.domain.shopcart.ShopCartItem;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * 客户端调用 ShopCartController的服务
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-10 19:27:07
 */
@Service
@SuppressWarnings("rawtypes")
public class ShopCartControllerClient {
  private static final Logger LOGGER = LogManager.getLogger(ShopCartControllerClient.class);

  private static final Callback<ResponseBean> Empty_Callback = new Callback<ResponseBean>() {

    @Override
    public void onResponse(Call<ResponseBean> call, Response<ResponseBean> response) {
      LOGGER.info("onResponse:" + response.message());
    }

    @Override
    public void onFailure(Call<ResponseBean> call, Throwable t) {
      LOGGER.error(t.getMessage(), t);
    }
  };

  @HttpService
  private ShopCartControllerApi shopcartControllerApi;

  /**
   * 往用户购物车添加商品
   * 
   * @param token
   * @param userid
   * @param requestBean
   * @return
   * @throws IOException
   */
  public ShopCartItem addShopCartItem(String token, String userid, AddShopCartItemRequest requestBean)
      throws IOException {
    Response<ResponseBean> response = shopcartControllerApi.addShopCartItem(token, userid, requestBean).execute();
    return getShopCartItemFromResponse(response, "add shopcart item  return code is '200',but return data is null");
  }

  /**
   * 异步往用户购物车添加商品
   * 
   * @param token
   * @param userid
   * @param requestBean
   * @param callback
   */
  public void addShopCartItemAsync(String token, String userid, AddShopCartItemRequest requestBean,
      Callback<ResponseBean> callback) {
    shopcartControllerApi.addShopCartItem(token, userid, requestBean).enqueue(callback);
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param token
   * @param userid
   * @param currentpage
   * @param pagesize
   * @return
   * @throws IOException
   */
  public GetShopCartItemListResponse getShopCartItemList(String token, String userid, int currentpage, int pagesize)
      throws IOException {
    Response<ResponseBean> response = shopcartControllerApi.getShopCartItemList(token, userid, currentpage, pagesize)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetShopCartItemListResponse getShopCartItemListResponse = JsonUtils
          .json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), GetShopCartItemListResponse.class);
      if (null == getShopCartItemListResponse) {
        throw new ShopCartException("500", "get shopcart list  return code is '200',but return data is null");
      }
      return getShopCartItemListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param token
   * @param userid
   * @param ischecked
   * @param async
   * @return
   * @throws IOException
   */
  public void checkAllShopCartItem(String token, String userid, boolean ischecked, boolean async) throws IOException {
    if (async) {
      shopcartControllerApi.checkAllShopCartItem(token, userid, ischecked).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.checkAllShopCartItem(token, userid, ischecked).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 选中用户购物车下的某一项
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @param ischecked
   * @param async
   * @return
   * @throws IOException
   */
  public void checkShopCartItem(String token, String userid, String shopcartid, boolean ischecked, boolean async)
      throws IOException {
    if (async) {
      shopcartControllerApi.checkShopCartItem(token, userid, shopcartid, ischecked).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.checkShopCartItem(token, userid, shopcartid, ischecked)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 删除用户购物车中的某项
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @param async
   * @return
   * @throws IOException
   */
  public void deleteShopCartItem(String token, String userid, String shopcartid, boolean async) throws IOException {
    if (async) {
      shopcartControllerApi.deleteShopCartItem(token, userid, shopcartid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.deleteShopCartItem(token, userid, shopcartid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 修改用户购物车中商品数量
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @param count
   * @return
   * @throws IOException
   */
  public ShopCartItem updateShopCartItemCount(String token, String userid, String shopcartid, int count)
      throws IOException {
    Response<ResponseBean> response = shopcartControllerApi.updateShopCartItemCount(token, userid, shopcartid, count)
        .execute();
    return getShopCartItemFromResponse(response,
        "update shopcart item  count return code is '200',but return data is null");
  }

  /**
   * 从response中获取ShopCartItem
   * 
   * @param response
   * @param exceptionmsg
   * @return
   */
  private ShopCartItem getShopCartItemFromResponse(Response<ResponseBean> response, String exceptionmsg) {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      ShopCartItem shopcartitem = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), ShopCartItem.class);
      if (null == shopcartitem) {
        throw new ShopCartException("500", exceptionmsg);
      }
      return shopcartitem;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步修改用户购物车中商品数量
   * 
   * @param token
   * @param userid
   * @param shopcartid
   * @param count
   * @param callback
   */
  public void updateShopCartItemCountAsync(String token, String userid, String shopcartid, int count,
      Callback<ResponseBean> callback) {
    shopcartControllerApi.updateShopCartItemCount(token, userid, shopcartid, count).enqueue(callback);
  }

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param token
   * @param goodid
   * @param async
   * @return
   * @throws IOException
   */
  public void deleteGood(String token, String goodid, boolean async) throws IOException {
    if (async) {
      shopcartControllerApi.deleteGood(token, goodid).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.deleteGood(token, goodid).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    if ("200".equals(returnCode)) {
      return;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }
}
