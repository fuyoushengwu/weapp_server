package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.domain.ResponseBean;
import cn.aijiamuyingfang.client.domain.ResponseCode;
import cn.aijiamuyingfang.client.domain.exception.ShopCartException;
import cn.aijiamuyingfang.client.domain.shopcart.ShopCart;
import cn.aijiamuyingfang.client.domain.shopcart.response.GetShopCartListResponse;
import cn.aijiamuyingfang.client.domain.shoporder.request.CreateShopCartRequest;
import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.ShopCartControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
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
   * @param userid
   * @param requestBean
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ShopCart addShopCart(String userid, CreateShopCartRequest requestBean, String accessToken) throws IOException {
    Response<ResponseBean> response = shopcartControllerApi.addShopCart(userid, requestBean, accessToken).execute();
    return getShopCartFromResponse(response, "add shopcart item  return code is '200',but return data is null");
  }

  /**
   * 异步往用户购物车添加商品
   * 
   * @param userid
   * @param requestBean
   * @param accessToken
   * @param callback
   */
  public void addShopCartAsync(String userid, CreateShopCartRequest requestBean, String accessToken,
      Callback<ResponseBean> callback) {
    shopcartControllerApi.addShopCart(userid, requestBean, accessToken).enqueue(callback);
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param userid
   * @param currentpage
   * @param pagesize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public GetShopCartListResponse getShopCartList(String userid, int currentpage, int pagesize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shopcartControllerApi.getShopCartList(userid, currentpage, pagesize, accessToken)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      GetShopCartListResponse getShopCartListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          GetShopCartListResponse.class);
      if (null == getShopCartListResponse) {
        throw new ShopCartException("500", "get shopcart list  return code is '200',but return data is null");
      }
      return getShopCartListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param userid
   * @param ischecked
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void checkAllShopCart(String userid, boolean ischecked, String accessToken, boolean async) throws IOException {
    if (async) {
      shopcartControllerApi.checkAllShopCart(userid, ischecked, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.checkAllShopCart(userid, ischecked, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
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
   * @param userid
   * @param shopcartid
   * @param ischecked
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void checkShopCart(String userid, String shopcartid, boolean ischecked, String accessToken, boolean async)
      throws IOException {
    if (async) {
      shopcartControllerApi.checkShopCart(userid, shopcartid, ischecked, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.checkShopCart(userid, shopcartid, ischecked, accessToken)
        .execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
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
   * @param userid
   * @param shopcartid
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteShopCart(String userid, String shopcartid, String accessToken, boolean async) throws IOException {
    if (async) {
      shopcartControllerApi.deleteShopCart(userid, shopcartid, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.deleteShopCart(userid, shopcartid, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
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
   * @param userid
   * @param shopcartid
   * @param count
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ShopCart updateShopCartCount(String userid, String shopcartid, int count, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shopcartControllerApi.updateShopCartCount(userid, shopcartid, count, accessToken)
        .execute();
    return getShopCartFromResponse(response,
        "update shopcart item  count return code is '200',but return data is null");
  }

  /**
   * 从response中获取ShopCart
   * 
   * @param response
   * @param exceptionmsg
   * @return
   * @throws IOException
   */
  private ShopCart getShopCartFromResponse(Response<ResponseBean> response, String exceptionmsg) throws IOException {
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
      throw new ShopCartException(ResponseCode.RESPONSE_BODY_IS_NULL);
    }
    String returnCode = responseBean.getCode();
    Object returnData = responseBean.getData();
    if ("200".equals(returnCode)) {
      ShopCart shopcart = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), ShopCart.class);
      if (null == shopcart) {
        throw new ShopCartException("500", exceptionmsg);
      }
      return shopcart;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步修改用户购物车中商品数量
   * 
   * @param userid
   * @param shopcartid
   * @param count
   * @param accessToken
   * @param callback
   */
  public void updateShopCartCountAsync(String userid, String shopcartid, int count, String accessToken,
      Callback<ResponseBean> callback) {
    shopcartControllerApi.updateShopCartCount(userid, shopcartid, count, accessToken).enqueue(callback);
  }

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodid
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteGood(String goodid, String accessToken, boolean async) throws IOException {
    if (async) {
      shopcartControllerApi.deleteGood(goodid, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopcartControllerApi.deleteGood(goodid, accessToken).execute();
    ResponseBean responseBean = response.body();
    if (null == responseBean) {
      if (response.errorBody() != null) {
        LOGGER.error(new String(response.errorBody().bytes()));
      }
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
