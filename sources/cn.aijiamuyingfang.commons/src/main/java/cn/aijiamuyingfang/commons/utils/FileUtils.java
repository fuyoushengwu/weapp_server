package cn.aijiamuyingfang.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import lombok.experimental.UtilityClass;

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
@UtilityClass
public class FileUtils {
  private static final char SYSTEM_SEPARATOR = File.separatorChar;

  /**
   * @param directory
   *          要创建的文件夹
   * @return 是否创建成功
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
   *          要创建的文件
   * @return 是否创建成功
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
   *          要清理的文件夹
   * @throws IOException
   *           抛出异常,清理失败
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
   * 
   * @param file
   *          要删除的文件
   * @throws IOException
   *           抛出异常,删除失败
   */
  public static void forceDelete(File file) throws IOException {
    if (file.isDirectory()) {
      deleteDirectory(file);
    } else {
      Files.delete(file.toPath());
    }
  }

  /**
   * 
   * @param directory
   *          要删除的文件夹
   * @throws IOException
   *           抛出异常,删除失败
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
   * 
   * @param file
   *          要判断的文件
   * @return 是否是符号链接文件
   * @throws IOException
   *           抛出异常,判断失败
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
   * 
   * @return 是否是Windows操作系统
   */
  static boolean isSystemWindows() {
    return SYSTEM_SEPARATOR == '\\';
  }
}