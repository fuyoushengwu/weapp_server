package cn.aijiamuyingfang.server.dnspod.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.client.rest.annotation.HttpService;
import cn.aijiamuyingfang.server.dnspod.api.DNSPodApi;
import cn.aijiamuyingfang.server.dnspod.domain.CreateRecordRequest;
import cn.aijiamuyingfang.server.dnspod.domain.DomainRequest;
import cn.aijiamuyingfang.server.dnspod.domain.RecordResponse;
import cn.aijiamuyingfang.server.dnspod.domain.SubDomainRecordListResponse;
import cn.aijiamuyingfang.server.dnspod.domain.UpdateRecordRequest;
import retrofit2.Response;

/**
 * [描述]:
 * <p>
 * dns pod操作客戶端
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 06:44:50
 */
@Service
public class DNSPodClient {

  /**
   * 请求公网IP的URL
   */
  private static final String GET_NET_IP_URL = "http://20019.ip138.com/ic.asp";

  @HttpService
  private DNSPodApi dnspodApi;

  public void updateDNSPod(String domain, String tokenId, String tokenValue)
      throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    String[] domainArrary = domain.split("\\.");
    String rootDomain = domainArrary[domainArrary.length - 2] + '.' + domainArrary[domainArrary.length - 1];
    String prefixDomain = domain.replace(rootDomain, "");
    if (prefixDomain.endsWith(".")) {
      prefixDomain = prefixDomain.substring(0, prefixDomain.length() - 1);
    }

    DomainRequest request = new DomainRequest(tokenId, tokenValue, rootDomain);
    SubDomainRecordListResponse subDomainListResponse = dnspodApi.getSubDomainRecod(request.toPartMap()).execute()
        .body();
    SubDomainRecordListResponse.Record equalRecord = null;
    SubDomainRecordListResponse.Record wildRecord = null;
    if (subDomainListResponse != null && CollectionUtils.isNotEmpty(subDomainListResponse.getRecords())) {
      for (SubDomainRecordListResponse.Record record : subDomainListResponse.getRecords()) {
        if (prefixDomain.equals(record.getSubDomain())) {
          equalRecord = record;
        }
        if ("*".equals(record.getSubDomain())) {
          wildRecord = record;
        }
      }
    }
    String actualIp = getPublicIP();
    String recordIp = null;
    String recordId = null;
    String recordLine = null;
    if (equalRecord != null) {
      recordIp = equalRecord.getValue();
      recordId = equalRecord.getId();
      recordLine = equalRecord.getArea();
    } else if (wildRecord != null) {
      recordIp = wildRecord.getValue();
      recordId = wildRecord.getId();
      recordLine = wildRecord.getArea();
    } else {
      CreateRecordRequest createRecordRequest = new CreateRecordRequest(tokenId, tokenValue, rootDomain, actualIp);
      Response<RecordResponse> response=dnspodApi.createRecord(createRecordRequest.toPartMap()).execute();
      System.out.println(response.isSuccessful());
      return;
    }
    if (!actualIp.equals(recordIp)) {
      UpdateRecordRequest updateRecordRequest = new UpdateRecordRequest(tokenId, tokenValue, rootDomain, prefixDomain,
          recordId, recordLine);
      dnspodApi.updateDynamicDNS(updateRecordRequest.toPartMap()).execute();
    }
  }

  public String getPublicIP() throws IOException {
    StringBuffer content = new StringBuffer();
    URL url = new URL(GET_NET_IP_URL);
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    String read = "";
    while ((read = in.readLine()) != null) {
      content.append(read);
    }
    String contentStr = content.toString();
    return contentStr.split("\\[")[1].split("\\]")[0];
  }
}
