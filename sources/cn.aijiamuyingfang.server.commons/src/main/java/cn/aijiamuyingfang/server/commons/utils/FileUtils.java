package cn.aijiamuyingfang.server.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * [描述]:
 * <p>
 * 文件操作工具类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-28 23:40:49
 */
public final class FileUtils {
  private static final char SYSTEM_SEPARATOR = File.separatorChar;

  private FileUtils() {
  }

  /**
   * 创建文件夹
   * 
   * @param directory
   */
  public static boolean createDirectory(File directory) {
    if (null == directory) {
      return false;
    }
    if (directory.exists()) {
      return true;
    }
    return directory.mkdirs();
  }

  /**
   * 创建文件
   * 
   * @param file
   * @return
   */
  public static boolean createFile(File file) {
    if (null == file) {
      return false;
    }
    if (file.exists()) {
      return true;
    }
    if (createDirectory(file.getParentFile())) {
      try {
        return file.createNewFile();
      } catch (IOException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * 清理文件夹
   * 
   * @param directory
   * @throws IOException
   */
  public static void cleanDirectory(File directory) throws IOException {
    if (!directory.exists()) {
      return;
    }

    if (!directory.isDirectory()) {
      return;
    }

    File[] files = directory.listFiles();
    if (files == null) {
      throw new IOException("Failed to list contents of " + directory);
    }

    IOException exception = null;
    for (File file : files) {
      try {
        forceDelete(file);
      } catch (IOException ioe) {
        exception = ioe;
      }
    }

    if (null != exception) {
      throw exception;
    }
  }

  /**
   * 强制删除文件
   * 
   * @param file
   * @throws IOException
   */
  public static void forceDelete(File file) throws IOException {
    if (file.isDirectory()) {
      deleteDirectory(file);
    } else {
      Files.delete(file.toPath());
    }
  }

  /**
   * . 删除文件夹
   * 
   * @param directory
   * @throws IOException
   */
  public static void deleteDirectory(File directory) throws IOException {
    if (!directory.exists()) {
      return;
    }

    if (!isSymlink(directory)) {
      cleanDirectory(directory);
    }
    Files.delete(directory.toPath());
  }

  /**
   * 是否是符号链接文件
   * 
   * @param file
   * @return
   * @throws IOException
   */
  public static boolean isSymlink(File file) throws IOException {
    if (file == null) {
      throw new NullPointerException("File must not be null");
    }
    if (isSystemWindows()) {
      return false;
    }
    File fileInCanonicalDir = null;
    if (file.getParent() == null) {
      fileInCanonicalDir = file;
    } else {
      File canonicalDir = file.getParentFile().getCanonicalFile();
      fileInCanonicalDir = new File(canonicalDir, file.getName());
    }

    return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
  }

  /**
   * 是否是Windows操作系统
   * 
   * @return
   */
  static boolean isSystemWindows() {
    return SYSTEM_SEPARATOR == '\\';
  }
}
