package cn.aijiamuyingfang.server.it.user.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.user.User;

/**
 * [描述]:
 * <p>
 * User数据仓库
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2018-06-25 21:14:03
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

  /**
   * 据openid获取用户(因为逻辑上User和openid是一一对应的)
   * 
   * @param openid
   * @return
   */
  User findByOpenid(String openid);

  /**
   * 获取不同权限用户的Id
   * 
   * @param authority
   * @return
   */
  @Query(value = "select user_id from user_authority_list where authority_list=:type", nativeQuery = true)
  List<String> findUsersByAuthority(@Param("type") int authority);

}
