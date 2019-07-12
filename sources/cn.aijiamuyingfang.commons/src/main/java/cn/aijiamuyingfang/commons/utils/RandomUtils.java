package cn.aijiamuyingfang.commons.utils;

import java.security.SecureRandom;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomUtils {

  private static final SecureRandom RANDOM = new SecureRandom();

  public static int nextInt(final int startInclusive, final int endExclusive) {
    Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
    Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

    if (startInclusive == endExclusive) {
      return startInclusive;
    }
    return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
  }
}
