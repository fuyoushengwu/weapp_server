package cn.aijiamuyingfang.client.rest.api.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.client.rest.api.ShopCartControllerApi;
import cn.aijiamuyingfang.client.rest.utils.JsonUtils;
import cn.aijiamuyingfang.vo.ResponseBean;
import cn.aijiamuyingfang.vo.ResponseCode;
import cn.aijiamuyingfang.vo.exception.ShopCartException;
import cn.aijiamuyingfang.vo.shopcart.CreateShopCartRequest;
import cn.aijiamuyingfang.vo.shopcart.PagableShopCartList;
import cn.aijiamuyingfang.vo.shopcart.ShopCart;
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
  private ShopCartControllerApi shopCartControllerApi;

  /**
   * 往用户购物车添加商品
   * 
   * @param username
   * @param requestBean
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ShopCart addShopCart(String username, CreateShopCartRequest requestBean, String accessToken) throws IOException {
    Response<ResponseBean> response = shopCartControllerApi.addShopCart(username, requestBean, accessToken).execute();
    return getShopCartFromResponse(response, "add ShopCartItem  return code is '200',but return data is null");
  }

  /**
   * 异步往用户购物车添加商品
   * 
   * @param username
   * @param requestBean
   * @param accessToken
   * @param callback
   */
  public void addShopCartAsync(String username, CreateShopCartRequest requestBean, String accessToken,
      Callback<ResponseBean> callback) {
    shopCartControllerApi.addShopCart(username, requestBean, accessToken).enqueue(callback);
  }

  /**
   * 分页获取用户购物车中的项目
   * 
   * @param username
   * @param currentPage
   * @param pageSize
   * @param accessToken
   * @return
   * @throws IOException
   */
  public PagableShopCartList getShopCartList(String username, int currentPage, int pageSize, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shopCartControllerApi.getShopCartList(username, currentPage, pageSize, accessToken)
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
      PagableShopCartList getShopCartListResponse = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData),
          PagableShopCartList.class);
      if (null == getShopCartListResponse) {
        throw new ShopCartException("500", "get ShopCart list  return code is '200',but return data is null");
      }
      return getShopCartListResponse;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 全选/全不选用户购物车中的商品
   * 
   * @param username
   * @param checked
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void checkAllShopCart(String username, boolean checked, String accessToken, boolean async) throws IOException {
    if (async) {
      shopCartControllerApi.checkAllShopCart(username, checked, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopCartControllerApi.checkAllShopCart(username, checked, accessToken).execute();
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
   * @param username
   * @param shopCartId
   * @param checked
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void checkShopCart(String username, String shopCartId, boolean checked, String accessToken, boolean async)
      throws IOException {
    if (async) {
      shopCartControllerApi.checkShopCart(username, shopCartId, checked, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopCartControllerApi.checkShopCart(username, shopCartId, checked, accessToken)
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
   * @param username
   * @param shopCartId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteShopCart(String username, String shopCartId, String accessToken, boolean async) throws IOException {
    if (async) {
      shopCartControllerApi.deleteShopCart(username, shopCartId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopCartControllerApi.deleteShopCart(username, shopCartId, accessToken).execute();
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
   * @param username
   * @param shopCartId
   * @param count
   * @param accessToken
   * @return
   * @throws IOException
   */
  public ShopCart updateShopCartCount(String username, String shopCartId, int count, String accessToken)
      throws IOException {
    Response<ResponseBean> response = shopCartControllerApi.updateShopCartCount(username, shopCartId, count, accessToken)
        .execute();
    return getShopCartFromResponse(response, "update ShopCartItem count return code is '200',but return data is null");
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
      ShopCart shopCart = JsonUtils.json2Bean(JsonUtils.map2Json((Map<?, ?>) returnData), ShopCart.class);
      if (null == shopCart) {
        throw new ShopCartException("500", exceptionmsg);
      }
      return shopCart;
    }
    LOGGER.error(responseBean.getMsg());
    throw new ShopCartException(returnCode, responseBean.getMsg());
  }

  /**
   * 异步修改用户购物车中商品数量
   * 
   * @param username
   * @param shopCartId
   * @param count
   * @param accessToken
   * @param callback
   */
  public void updateShopCartCountAsync(String username, String shopCartId, int count, String accessToken,
      Callback<ResponseBean> callback) {
    shopCartControllerApi.updateShopCartCount(username, shopCartId, count, accessToken).enqueue(callback);
  }

  /**
   * 删除购物车中的商品(该方法在废弃商品的时候使用,所以只有Admin才能调用该方法)
   * 
   * @param goodId
   * @param accessToken
   * @param async
   * @throws IOException
   */
  public void deleteGood(String goodId, String accessToken, boolean async) throws IOException {
    if (async) {
      shopCartControllerApi.deleteGood(goodId, accessToken).enqueue(Empty_Callback);
      return;
    }
    Response<ResponseBean> response = shopCartControllerApi.deleteGood(goodId, accessToken).execute();
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
