package cn.aijiamuyingfang.commons.config;

import cn.aijiamuyingfang.commons.utils.RandomUtils;

/**
 * 随机生成一个端口，并只生成一次
 */
public class RandomServerPort {

  private static final int RANDOM_SERVERPORT_START = 0;

  private static final int RANDOM_SERVERPORT_END = 65535;

  private volatile int serverPort;

  public int nextValue(int start) {
    return nextValue(start, RANDOM_SERVERPORT_END);
  }

  public int nextValue(int start, int end) {

    if (serverPort == 0) {
      synchronized (this) {
        if (serverPort == 0) {
          start = Math.max(start, RANDOM_SERVERPORT_START);
          end = Math.min(end, RANDOM_SERVERPORT_END);
          serverPort = RandomUtils.nextInt(start, end);
        }
      }
    }
    return serverPort;
  }
}