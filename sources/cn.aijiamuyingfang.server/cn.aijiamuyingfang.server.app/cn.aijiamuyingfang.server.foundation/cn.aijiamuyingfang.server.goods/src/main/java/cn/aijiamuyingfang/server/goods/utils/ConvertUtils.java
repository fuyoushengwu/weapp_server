package cn.aijiamuyingfang.server.goods.utils;

import java.util.ArrayList;
import java.util.List;

import cn.aijiamuyingfang.server.goods.dto.GoodDetailDTO;
import cn.aijiamuyingfang.server.goods.dto.ImageSourceDTO;
import cn.aijiamuyingfang.server.goods.dto.ShelfLifeDTO;
import cn.aijiamuyingfang.server.goods.dto.StoreAddressDTO;
import cn.aijiamuyingfang.server.goods.dto.StoreDTO;
import cn.aijiamuyingfang.server.goods.dto.WorkTimeDTO;
import cn.aijiamuyingfang.vo.ImageSource;
import cn.aijiamuyingfang.vo.goods.GoodDetail;
import cn.aijiamuyingfang.vo.goods.ShelfLife;
import cn.aijiamuyingfang.vo.store.Store;
import cn.aijiamuyingfang.vo.store.StoreAddress;
import cn.aijiamuyingfang.vo.store.WorkTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertUtils {

  public static GoodDetail convertGoodDetailDTO(GoodDetailDTO goodDetailDTO) {
    if (null == goodDetailDTO) {
      return null;
    }
    GoodDetail goodDetail = new GoodDetail();
    goodDetail.setId(goodDetailDTO.getId());
    goodDetail.setLifetime(ConvertUtils.convertShelfLifeDTO(goodDetailDTO.getLifetime()));
    goodDetail.setDetailImgList(ConvertUtils.convertImageSourceDTOList(goodDetailDTO.getDetailImgList()));
    return goodDetail;
  }

  public static GoodDetailDTO convertGoodDetail(GoodDetail goodDetail) {
    if (null == goodDetail) {
      return null;
    }
    GoodDetailDTO goodDetailDTO = new GoodDetailDTO();
    goodDetailDTO.setId(goodDetail.getId());
    goodDetailDTO.setLifetime(ConvertUtils.convertShelfLife(goodDetail.getLifetime()));
    goodDetailDTO.setDetailImgList(ConvertUtils.convertImageSourceList(goodDetail.getDetailImgList()));
    return goodDetailDTO;
  }

  public static Store convertStoreDTO(StoreDTO storeDTO) {
    if (null == storeDTO) {
      return null;
    }
    Store store = new Store();
    store.setId(storeDTO.getId());
    store.setDeprecated(storeDTO.isDeprecated());
    store.setName(storeDTO.getName());
    store.setWorkTime(convertWorkTimeDTO(storeDTO.getWorkTime()));
    store.setCoverImg(convertImageSourceDTO(storeDTO.getCoverImg()));
    store.setDetailImgList(convertImageSourceDTOList(storeDTO.getDetailImgList()));
    store.setStoreAddress(convertStoreAddressDTO(storeDTO.getStoreAddress()));
    return store;
  }

  public static List<Store> convertStoreDTOList(List<StoreDTO> storeDTOList) {
    List<Store> storeList = new ArrayList<>();
    if (null == storeDTOList) {
      return storeList;
    }
    for (StoreDTO storeDTO : storeDTOList) {
      Store store = convertStoreDTO(storeDTO);
      if (store != null) {
        storeList.add(store);
      }
    }
    return storeList;
  }

  public static StoreDTO convertStore(Store store) {
    if (null == store) {
      return null;
    }
    StoreDTO storeDTO = new StoreDTO();
    storeDTO.setId(store.getId());
    storeDTO.setDeprecated(store.isDeprecated());
    storeDTO.setName(store.getName());
    storeDTO.setWorkTime(convertWorkTime(store.getWorkTime()));
    storeDTO.setCoverImg(convertImageSource(store.getCoverImg()));
    storeDTO.setDetailImgList(convertImageSourceList(store.getDetailImgList()));
    storeDTO.setStoreAddress(convertStoreAddress(store.getStoreAddress()));
    return storeDTO;
  }

  public static StoreAddress convertStoreAddressDTO(StoreAddressDTO storeAddressDTO) {
    if (null == storeAddressDTO) {
      return null;
    }
    StoreAddress storeAddress = new StoreAddress();
    storeAddress.setId(storeAddressDTO.getId());
    storeAddress.setDeprecated(storeAddressDTO.isDeprecated());
    storeAddress
        .setProvince(cn.aijiamuyingfang.server.utils.ConvertUtils.convertProvinceDTO(storeAddressDTO.getProvince()));
    storeAddress.setCity(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCityDTO(storeAddressDTO.getCity()));
    storeAddress.setCounty(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCountyDTO(storeAddressDTO.getCounty()));
    storeAddress.setTown(cn.aijiamuyingfang.server.utils.ConvertUtils.convertTownDTO(storeAddressDTO.getTown()));
    storeAddress.setDetail(storeAddressDTO.getDetail());
    storeAddress.setCoordinate(
        cn.aijiamuyingfang.server.utils.ConvertUtils.convertCoordinateDTO(storeAddressDTO.getCoordinate()));
    storeAddress.setPhone(storeAddressDTO.getPhone());
    storeAddress.setContactor(storeAddressDTO.getContactor());
    return storeAddress;
  }

  public static StoreAddressDTO convertStoreAddress(StoreAddress storeAddress) {
    if (null == storeAddress) {
      return null;
    }
    StoreAddressDTO storeAddressDTO = new StoreAddressDTO();
    storeAddressDTO.setId(storeAddress.getId());
    storeAddressDTO.setDeprecated(storeAddress.isDeprecated());
    storeAddressDTO
        .setProvince(cn.aijiamuyingfang.server.utils.ConvertUtils.convertProvince(storeAddress.getProvince()));
    storeAddressDTO.setCity(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCity(storeAddress.getCity()));
    storeAddressDTO.setCounty(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCounty(storeAddress.getCounty()));
    storeAddressDTO.setTown(cn.aijiamuyingfang.server.utils.ConvertUtils.convertTown(storeAddress.getTown()));
    storeAddressDTO.setDetail(storeAddress.getDetail());
    storeAddressDTO
        .setCoordinate(cn.aijiamuyingfang.server.utils.ConvertUtils.convertCoordinate(storeAddress.getCoordinate()));
    storeAddressDTO.setPhone(storeAddress.getPhone());
    storeAddressDTO.setContactor(storeAddress.getContactor());
    return storeAddressDTO;
  }

  public static WorkTime convertWorkTimeDTO(WorkTimeDTO workTimeDTO) {
    if (null == workTimeDTO) {
      return null;
    }
    WorkTime workTime = new WorkTime();
    workTime.setStart(workTimeDTO.getStart());
    workTime.setEnd(workTimeDTO.getEnd());
    return workTime;
  }

  public static WorkTimeDTO convertWorkTime(WorkTime workTime) {
    if (null == workTime) {
      return null;
    }
    WorkTimeDTO workTimeDTO = new WorkTimeDTO();
    workTimeDTO.setStart(workTime.getStart());
    workTimeDTO.setEnd(workTime.getEnd());
    return workTimeDTO;
  }

  public static ImageSource convertImageSourceDTO(ImageSourceDTO imageSourceDTO) {
    if (null == imageSourceDTO) {
      return null;
    }
    ImageSource imageSource = new ImageSource();
    imageSource.setId(imageSourceDTO.getId());
    imageSource.setUrl(imageSourceDTO.getUrl());
    return imageSource;
  }

  public static List<ImageSource> convertImageSourceDTOList(List<ImageSourceDTO> imageSourceDTOList) {
    List<ImageSource> imageSourceList = new ArrayList<>();
    if (null == imageSourceDTOList) {
      return imageSourceList;
    }
    for (ImageSourceDTO imageSourceDTO : imageSourceDTOList) {
      ImageSource imageSource = convertImageSourceDTO(imageSourceDTO);
      if (imageSource != null) {
        imageSourceList.add(imageSource);
      }
    }
    return imageSourceList;
  }

  public static ImageSourceDTO convertImageSource(ImageSource imageSource) {
    if (null == imageSource) {
      return null;
    }
    ImageSourceDTO imageSourceDTO = new ImageSourceDTO();
    imageSourceDTO.setId(imageSource.getId());
    imageSourceDTO.setUrl(imageSource.getUrl());
    return imageSourceDTO;
  }

  public static List<ImageSourceDTO> convertImageSourceList(List<ImageSource> imageSourceList) {
    List<ImageSourceDTO> imageSourceDTOList = new ArrayList<>();
    if (null == imageSourceList) {
      return imageSourceDTOList;
    }
    for (ImageSource imageSource : imageSourceList) {
      ImageSourceDTO imageSourceDTO = convertImageSource(imageSource);
      if (imageSourceDTO != null) {
        imageSourceDTOList.add(imageSourceDTO);
      }
    }
    return imageSourceDTOList;
  }

  public static ShelfLife convertShelfLifeDTO(ShelfLifeDTO shelfLifeDTO) {
    if (null == shelfLifeDTO) {
      return null;
    }
    ShelfLife shelfLife = new ShelfLife();
    shelfLife.setStart(shelfLifeDTO.getStart());
    shelfLife.setEnd(shelfLifeDTO.getEnd());
    return shelfLife;
  }

  public static ShelfLifeDTO convertShelfLife(ShelfLife shelfLife) {
    if (null == shelfLife) {
      return null;
    }
    ShelfLifeDTO shelfLifeDTO = new ShelfLifeDTO();
    shelfLifeDTO.setStart(shelfLife.getStart());
    shelfLifeDTO.setEnd(shelfLife.getEnd());
    return shelfLifeDTO;
  }

}
