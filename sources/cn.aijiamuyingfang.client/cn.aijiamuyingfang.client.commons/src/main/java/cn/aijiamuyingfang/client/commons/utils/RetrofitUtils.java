package cn.aijiamuyingfang.client.commons.utils;

import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_BASE_URL;
import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_CONNECT_TIMEOUT;
import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_READ_TIMEOUT;
import static cn.aijiamuyingfang.client.commons.constant.ClientRestConstants.DEFAULT_WRITE_TIMEOUT;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import cn.aijiamuyingfang.client.commons.domain.EnumRetrofitConverterFactory;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@UtilityClass
public class RetrofitUtils {

  /**
   * 创建OkHttpClient.Builder实例(支持HTTPS(使用默认的证书))
   * 
   * @param hostname
   * @param connecttimeout
   * @param readtimeout
   * @param writetimeout
   * @return
   */
  public static OkHttpClient.Builder getOkHttpClientBuilder(int connecttimeout, int readtimeout,
      int writetimeout) {
    if (connecttimeout < 0) {
      connecttimeout = DEFAULT_CONNECT_TIMEOUT;
    }
    if (readtimeout < 0) {
      readtimeout = DEFAULT_READ_TIMEOUT;
    }
    if (writetimeout < 0) {
      writetimeout = DEFAULT_WRITE_TIMEOUT;
    }
    OkHttpClient.Builder httpclientBuilder =trustAllSSLClient();
    httpclientBuilder.connectTimeout(connecttimeout, TimeUnit.SECONDS);
    httpclientBuilder.readTimeout(readtimeout, TimeUnit.SECONDS);
    httpclientBuilder.writeTimeout(writetimeout, TimeUnit.SECONDS);
    return httpclientBuilder;
  }

  private static final SSLContext trustAllSslContext;

  private static final SSLSocketFactory trustAllSslSocketFactory;

  private static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return new java.security.cert.X509Certificate[] {};
    }
  } };
  
  static {
    try {
      trustAllSslContext = SSLContext.getInstance("SSL");
      trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * OkHttpClient信任所有的SSL证书
   */
  public static OkHttpClient.Builder trustAllSSLClient( ) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
    builder.hostnameVerifier((str, session) -> true);
    return builder;
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
