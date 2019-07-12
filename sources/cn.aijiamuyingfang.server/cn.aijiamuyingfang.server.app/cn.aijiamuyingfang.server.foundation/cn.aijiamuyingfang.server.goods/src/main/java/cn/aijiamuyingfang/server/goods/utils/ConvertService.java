package cn.aijiamuyingfang.server.goods.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aijiamuyingfang.server.feign.CouponClient;
import cn.aijiamuyingfang.server.goods.dto.ClassifyDTO;
import cn.aijiamuyingfang.server.goods.dto.GoodDTO;
import cn.aijiamuyingfang.vo.classify.Classify;
import cn.aijiamuyingfang.vo.goods.Good;
import cn.aijiamuyingfang.vo.utils.StringUtils;

@Service
public class ConvertService {
  @Autowired
  private CouponClient couponClient;

  public Good convertGoodDTO(GoodDTO goodDTO) {
    if (null == goodDTO) {
      return null;
    }
    Good good = new Good();
    good.setId(goodDTO.getId());
    good.setCoverImg(ConvertUtils.convertImageSourceDTO(goodDTO.getCoverImg()));
    good.setDeprecated(goodDTO.isDeprecated());
    good.setLifetime(ConvertUtils.convertShelfLifeDTO(goodDTO.getLifetime()));
    good.setName(goodDTO.getName());
    good.setCount(goodDTO.getCount());
    good.setSalecount(goodDTO.getSalecount());
    good.setPrice(goodDTO.getPrice());
    good.setMarketPrice(goodDTO.getMarketPrice());
    good.setPack(goodDTO.getPack());
    good.setLevel(goodDTO.getLevel());
    good.setBarcode(goodDTO.getBarcode());
    good.setScore(goodDTO.getScore());
    if (StringUtils.hasContent(goodDTO.getVoucherId())) {
      good.setGoodVoucher(couponClient.getGoodVoucher(goodDTO.getVoucherId()).getData());
    }
    return good;
  }

  public List<Good> convertGoodDTOList(List<GoodDTO> goodDTOList) {
    List<Good> goodList = new ArrayList<>();
    if (null == goodDTOList) {
      return goodList;
    }
    for (GoodDTO goodDTO : goodDTOList) {
      Good good = convertGoodDTO(goodDTO);
      if (good != null) {
        goodList.add(good);
      }
    }
    return goodList;
  }

  public GoodDTO convertGood(Good good) {
    if (null == good) {
      return null;
    }
    GoodDTO goodDTO = new GoodDTO();
    goodDTO.setId(good.getId());
    goodDTO.setCoverImg(ConvertUtils.convertImageSource(good.getCoverImg()));
    goodDTO.setDeprecated(good.isDeprecated());
    goodDTO.setLifetime(ConvertUtils.convertShelfLife(good.getLifetime()));
    goodDTO.setName(good.getName());
    goodDTO.setCount(good.getCount());
    goodDTO.setSalecount(good.getSalecount());
    goodDTO.setPrice(good.getPrice());
    goodDTO.setMarketPrice(good.getMarketPrice());
    goodDTO.setPack(good.getPack());
    goodDTO.setLevel(good.getLevel());
    goodDTO.setBarcode(good.getBarcode());
    goodDTO.setScore(good.getScore());
    if (good.getGoodVoucher() != null) {
      goodDTO.setVoucherId(good.getGoodVoucher().getId());
    }
    return goodDTO;
  }

  public List<GoodDTO> convertGoodList(List<Good> goodList) {
    List<GoodDTO> goodDTOList = new ArrayList<>();
    if (null == goodList) {
      return goodDTOList;
    }
    for (Good good : goodList) {
      GoodDTO goodDTO = convertGood(good);
      if (good != null) {
        goodDTOList.add(goodDTO);
      }
    }
    return goodDTOList;
  }

  public Classify convertClassifyDTO(ClassifyDTO classifyDTO) {
    if (null == classifyDTO) {
      return null;
    }
    Classify classify = new Classify();
    classify.setId(classifyDTO.getId());
    classify.setName(classifyDTO.getName());
    classify.setLevel(classifyDTO.getLevel());
    classify.setCoverImg(ConvertUtils.convertImageSourceDTO(classifyDTO.getCoverImg()));
    classify.setSubClassifyList(convertClassifyDTOList(classifyDTO.getSubClassifyList()));
    classify.setGoodList(convertGoodDTOList(classifyDTO.getGoodList()));
    return classify;
  }

  public List<Classify> convertClassifyDTOList(List<ClassifyDTO> classifyDTOList) {
    List<Classify> classifyList = new ArrayList<>();
    if (null == classifyDTOList) {
      return classifyList;
    }
    for (ClassifyDTO classifyDTO : classifyDTOList) {
      Classify classify = convertClassifyDTO(classifyDTO);
      if (classify != null) {
        classifyList.add(classify);
      }
    }
    return classifyList;
  }

  public ClassifyDTO convertClassify(Classify classify) {
    if (null == classify) {
      return null;
    }
    ClassifyDTO classifyDTO = new ClassifyDTO();
    classifyDTO.setId(classify.getId());
    classifyDTO.setName(classify.getName());
    classifyDTO.setLevel(classify.getLevel());
    classifyDTO.setCoverImg(ConvertUtils.convertImageSource(classify.getCoverImg()));
    classifyDTO.setSubClassifyList(convertClassifyList(classify.getSubClassifyList()));
    classifyDTO.setGoodList(convertGoodList(classify.getGoodList()));
    return classifyDTO;
  }

  public List<ClassifyDTO> convertClassifyList(List<Classify> classifyList) {
    List<ClassifyDTO> classifyDTOList = new ArrayList<>();
    if (null == classifyList) {
      return classifyDTOList;
    }
    for (Classify classify : classifyList) {
      ClassifyDTO classifyDTO = convertClassify(classify);
      if (classifyDTO != null) {
        classifyDTOList.add(classifyDTO);
      }
    }
    return classifyDTOList;
  }
}
