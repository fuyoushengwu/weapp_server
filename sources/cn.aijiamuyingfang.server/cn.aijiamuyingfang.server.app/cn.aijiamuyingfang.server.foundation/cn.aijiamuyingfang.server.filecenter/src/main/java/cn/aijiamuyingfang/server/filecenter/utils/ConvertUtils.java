package cn.aijiamuyingfang.server.filecenter.utils;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.server.filecenter.dto.FileInfoDTO;
import cn.aijiamuyingfang.server.filecenter.dto.FileSourceDTO;
import cn.aijiamuyingfang.vo.filecenter.FileInfo;
import cn.aijiamuyingfang.vo.filecenter.FileSource;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertUtils {
  public FileInfo convertFileInfoDTO(FileInfoDTO fileInfoDTO) {
    if (null == fileInfoDTO) {
      return null;
    }
    FileInfo fileInfo = new FileInfo();
    fileInfo.setId(fileInfoDTO.getId());
    fileInfo.setName(fileInfoDTO.getName());
    fileInfo.setImage(fileInfoDTO.getImage());
    fileInfo.setContentType(fileInfoDTO.getContentType());
    fileInfo.setSize(fileInfoDTO.getSize());
    fileInfo.setPath(fileInfoDTO.getPath());
    fileInfo.setUrl(fileInfoDTO.getUrl());
    fileInfo.setSource(convertFileSourceDTO(fileInfoDTO.getSource()));
    fileInfo.setCreateTime(fileInfoDTO.getCreateTime());
    return fileInfo;
  }

  public List<FileInfo> convertFileInfoDTOList(List<FileInfoDTO> fileInfoDTOList) {
    List<FileInfo> fileInfoList = new ArrayList<>();
    if (null == fileInfoDTOList) {
      return fileInfoList;
    }
    for (FileInfoDTO fileInfoDTO : fileInfoDTOList) {
      FileInfo fileInfo = convertFileInfoDTO(fileInfoDTO);
      if (fileInfo != null) {
        fileInfoList.add(fileInfo);
      }
    }
    return fileInfoList;
  }

  public FileInfoDTO convertFileInfo(FileInfo fileInfo) {
    if (null == fileInfo) {
      return null;
    }
    FileInfoDTO fileInfoDTO = new FileInfoDTO();
    fileInfoDTO.setId(fileInfo.getId());
    fileInfoDTO.setName(fileInfo.getName());
    fileInfoDTO.setImage(fileInfo.getImage());
    fileInfoDTO.setContentType(fileInfo.getContentType());
    fileInfoDTO.setSize(fileInfo.getSize());
    fileInfoDTO.setPath(fileInfo.getPath());
    fileInfoDTO.setUrl(fileInfo.getUrl());
    fileInfoDTO.setSource(convertFileSource(fileInfo.getSource()));
    fileInfoDTO.setCreateTime(fileInfo.getCreateTime());
    return fileInfoDTO;
  }

  public List<FileInfoDTO> convertFileInfoList(List<FileInfo> fileInfoList) {
    List<FileInfoDTO> fileInfoDTOList = new ArrayList<>();
    if (null == fileInfoList) {
      return fileInfoDTOList;
    }
    for (FileInfo fileInfo : fileInfoList) {
      FileInfoDTO fileInfoDTO = convertFileInfo(fileInfo);
      if (fileInfoDTO != null) {
        fileInfoDTOList.add(fileInfoDTO);
      }
    }
    return fileInfoDTOList;
  }

  public FileSource convertFileSourceDTO(FileSourceDTO fileSourceDTO) {
    if (null == fileSourceDTO) {
      return FileSource.UNKNOW;
    }
    for (FileSource fileSource : FileSource.values()) {
      if (fileSource.getValue() == fileSourceDTO.getValue()) {
        return fileSource;
      }
    }
    return FileSource.UNKNOW;
  }

  public FileSourceDTO convertFileSource(FileSource fileSource) {
    if (null == fileSource) {
      return FileSourceDTO.UNKNOW;
    }
    for (FileSourceDTO fileSourceDTO : FileSourceDTO.values()) {
      if (fileSource.getValue() == fileSourceDTO.getValue()) {
        return fileSourceDTO;
      }
    }
    return FileSourceDTO.UNKNOW;
  }
}
