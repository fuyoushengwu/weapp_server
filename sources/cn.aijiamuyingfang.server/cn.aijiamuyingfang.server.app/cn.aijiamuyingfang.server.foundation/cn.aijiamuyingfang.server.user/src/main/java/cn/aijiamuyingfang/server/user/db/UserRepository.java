package cn.aijiamuyingfang.server.user.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.user.domain.User;

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
   * 获取不同权限用户的Id
   * 
   * @param authority
   * @return
   */
  @Query(value = "select user_username from user_authority_list where authority_list=:type", nativeQuery = true)
  List<String> findUsersByAuthority(@Param("type") int authority);

}
