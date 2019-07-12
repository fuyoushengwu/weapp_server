package cn.aijiamuyingfang.server.utils;

import cn.aijiamuyingfang.server.dto.address.CityDTO;
import cn.aijiamuyingfang.server.dto.address.CoordinateDTO;
import cn.aijiamuyingfang.server.dto.address.CountyDTO;
import cn.aijiamuyingfang.server.dto.address.ProvinceDTO;
import cn.aijiamuyingfang.server.dto.address.TownDTO;
import cn.aijiamuyingfang.vo.address.City;
import cn.aijiamuyingfang.vo.address.Coordinate;
import cn.aijiamuyingfang.vo.address.County;
import cn.aijiamuyingfang.vo.address.Province;
import cn.aijiamuyingfang.vo.address.Town;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConvertUtils {

  public static Province convertProvinceDTO(ProvinceDTO provinceDTO) {
    if (null == provinceDTO) {
      return null;
    }
    Province province = new Province();
    province.setCode(provinceDTO.getCode());
    province.setName(provinceDTO.getName());
    return province;
  }

  public static ProvinceDTO convertProvince(Province province) {
    if (null == province) {
      return null;
    }
    ProvinceDTO provinceDTO = new ProvinceDTO();
    provinceDTO.setCode(province.getCode());
    provinceDTO.setName(province.getName());
    return provinceDTO;
  }

  public static City convertCityDTO(CityDTO cityDTO) {
    if (null == cityDTO) {
      return null;
    }
    City city = new City();
    city.setCode(cityDTO.getCode());
    city.setName(cityDTO.getName());
    return city;
  }

  public static CityDTO convertCity(City city) {
    if (null == city) {
      return null;
    }
    CityDTO cityDTO = new CityDTO();
    cityDTO.setCode(city.getCode());
    cityDTO.setName(city.getName());
    return cityDTO;
  }

  public static County convertCountyDTO(CountyDTO countyDTO) {
    if (null == countyDTO) {
      return null;
    }
    County county = new County();
    county.setCode(countyDTO.getCode());
    county.setName(countyDTO.getName());
    return county;
  }

  public static CountyDTO convertCounty(County county) {
    if (null == county) {
      return null;
    }
    CountyDTO countyDTO = new CountyDTO();
    countyDTO.setCode(county.getCode());
    countyDTO.setName(county.getName());
    return countyDTO;
  }

  public static Town convertTownDTO(TownDTO townDTO) {
    if (null == townDTO) {
      return null;
    }
    Town town = new Town();
    town.setCode(townDTO.getCode());
    town.setName(townDTO.getName());
    return town;
  }

  public static TownDTO convertTown(Town town) {
    if (null == town) {
      return null;
    }
    TownDTO townDTO = new TownDTO();
    townDTO.setCode(town.getCode());
    townDTO.setName(town.getName());
    return townDTO;
  }

  public static Coordinate convertCoordinateDTO(CoordinateDTO coordinateDTO) {
    if (null == coordinateDTO) {
      return null;
    }
    Coordinate coordinate = new Coordinate();
    coordinate.setLatitude(coordinateDTO.getLatitude());
    coordinate.setLongitude(coordinateDTO.getLongitude());
    return coordinate;
  }

  public static CoordinateDTO convertCoordinate(Coordinate coordinate) {
    if (null == coordinate) {
      return null;
    }
    CoordinateDTO coordinateDTO = new CoordinateDTO();
    coordinateDTO.setLatitude(coordinate.getLatitude());
    coordinateDTO.setLongitude(coordinate.getLongitude());
    return coordinateDTO;
  }

}