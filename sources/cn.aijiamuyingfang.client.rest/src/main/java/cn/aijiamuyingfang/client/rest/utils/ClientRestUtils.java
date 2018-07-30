package cn.aijiamuyingfang.client.rest.utils;

import static cn.aijiamuyingfang.client.rest.ClientRestConstants.DEFAULT_BASE_URL;
import static cn.aijiamuyingfang.client.rest.ClientRestConstants.DEFAULT_CONNECT_TIMEOUT;
import static cn.aijiamuyingfang.client.rest.ClientRestConstants.DEFAULT_HOST_NAME;
import static cn.aijiamuyingfang.client.rest.ClientRestConstants.DEFAULT_READ_TIMEOUT;
import static cn.aijiamuyingfang.client.rest.ClientRestConstants.DEFAULT_WRITE_TIMEOUT;

import cn.aijiamuyingfang.client.rest.converter.EnumRetrofitConverterFactory;
import cn.aijiamuyingfang.commons.utils.StringUtils;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * [描述]:
 * <p>
 * 工具类
 * </p>
 * 
 * @version 1.0.0
 * @author shiweideyouxiang@sina.cn
 * @date 2018-07-25 05:29:26
 */
public class ClientRestUtils {
  /**
   * 创建OkHttpClient.Builder实例(支持HTTPS(使用默认的证书))
   * 
   * @param hostname
   * @param connecttimeout
   * @param readtimeout
   * @param writetimeout
   * @return
   */
  public static OkHttpClient.Builder getOkHttpClientBuilder(String hostname, int connecttimeout, int readtimeout,
      int writetimeout) {
    if (StringUtils.isEmpty(hostname)) {
      hostname = DEFAULT_HOST_NAME;
    }
    String localhostname = hostname;
    if (connecttimeout < 0) {
      connecttimeout = DEFAULT_CONNECT_TIMEOUT;
    }
    if (readtimeout < 0) {
      readtimeout = DEFAULT_READ_TIMEOUT;
    }
    if (writetimeout < 0) {
      writetimeout = DEFAULT_WRITE_TIMEOUT;
    }
    OkHttpClient.Builder httpclientBuilder = new OkHttpClient.Builder(); // 支持https,添加证书指纹,验证域名
    httpclientBuilder.certificatePinner(
        new CertificatePinner.Builder().add(hostname, "sha256/ws7jjHqFe0uRilFM2Rmby01kpiLy7LFiQLhQz4ntLWk=")
            .add(hostname, "sha256/jzqM6/58ozsPRvxUzg0hzjM+GcfwhTbU/G0TCDvL7hU=")
            .add(hostname, "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=").build());
    httpclientBuilder.hostnameVerifier((str, session) -> localhostname.equals(str));
    httpclientBuilder.connectTimeout(connecttimeout, TimeUnit.SECONDS);
    httpclientBuilder.readTimeout(readtimeout, TimeUnit.SECONDS);
    httpclientBuilder.writeTimeout(writetimeout, TimeUnit.SECONDS);
    return httpclientBuilder;
  }

  /**
   * 创建 Retrofit.Builder实例(默认添加Converter:ScalarsConverter,GsonConverter,EnumRetrofitConverter)
   * 
   * @param baseurl
   * @return
   */
  public static Retrofit.Builder getRetrofitBuilder(String baseurl) {
    if (StringUtils.isEmpty(baseurl)) {
      baseurl = DEFAULT_BASE_URL;
    }
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Date.class, new TypeAdapter<Date>() {
      private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      @Override
      public void write(JsonWriter out, Date value) throws IOException {
        if (value != null) {
          out.value(dateFormatter.format(value));
        } else {
          out.value((String) null);
        }
      }

      @Override
      public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        String time = in.nextString();
        try {
          return dateFormatter.parse(time);
        } catch (ParseException e) {
          return null;
        }
      }

    });

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
    retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create());
    retrofitBuilder.addConverterFactory(GsonConverterFactory.create(builder.create()));
    retrofitBuilder.addConverterFactory(new EnumRetrofitConverterFactory());
    retrofitBuilder.baseUrl(baseurl);
    return retrofitBuilder;
  }

}
