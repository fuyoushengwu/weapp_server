package cn.aijiamuyingfang.client.oauth2.utils;

public abstract class Assert {

  /**
   * Assert that the given String is not empty; that is, it must not be {@code null} and not the
   * empty String.
   * 
   * <pre class="code">
   * Assert.hasLength(name, "Name must not be empty");
   * </pre>
   * 
   * @param text
   *          the String to check
   * @param message
   *          the exception message to use if the assertion fails
   * @see StringUtils#hasLength
   * @throws IllegalArgumentException
   *           if the text is empty
   */
  public static void hasLength(String text, String message) {
    if (!StringUtils.hasLength(text)) {
      throw new IllegalArgumentException(message);
    }
  }

}
