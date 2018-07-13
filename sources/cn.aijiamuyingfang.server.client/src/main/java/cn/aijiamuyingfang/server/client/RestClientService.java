package cn.aijiamuyingfang.server.client;

import org.springframework.stereotype.Service;

/**
 * [描述]:
 * <p>
 * 为客户端提供调用服务端服务的包装类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-07-01 14:49:30
 */
@Service
public class RestClientService {
  // private static final Logger LOGGER = LogManager.getLogger(RestClientService.class);
  //
  // private static final Callback<ResponseBean<Void>> Empty_Callback = new
  // Callback<ResponseBean<Void>>() {
  //
  // @Override
  // public void onResponse(Call<ResponseBean<Void>> call, Response<ResponseBean<Void>> response) {
  // LOGGER.info("onResponse:" + response.message());
  // }
  //
  // @Override
  // public void onFailure(Call<ResponseBean<Void>> call, Throwable t) {
  // LOGGER.error(t.getMessage(), t);
  // }
  //
  // };
  //
  // /**
  // * 时间的表达式
  // */
  // private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
  //
  // @HttpService
  // private TemplateMsgControllerApi templatemsgApi;
  //
  // @HttpService
  // private WXSessionControllerApi wxsessionApi;
  //
  // @HttpService
  // private ShopOrderControllerApi shoporderApi;
  //
  // @HttpService
  // private ShopCartControllerApi shopcartApi;
  //

  //

  // public void updatePreOrder(@Body Good good) {
  // shoporderApi.updatePreOrder(good).enqueue(Empty_Callback);
  // }
  //
  // public ResponseBean<WXSession> jscode2Session(String jscode) throws IOException {
  // Response<ResponseBean<WXSession>> response = wxsessionApi.jscode2Session(jscode).execute();
  // return response.body();
  // }
  //
  // /**
  // * 删除购物车中的商品
  // *
  // * @param goodid
  // */
  // public void deleteShopCartGood(String goodid) {
  // shopcartApi.deleteGood(goodid).enqueue(Empty_Callback);
  // }

}
