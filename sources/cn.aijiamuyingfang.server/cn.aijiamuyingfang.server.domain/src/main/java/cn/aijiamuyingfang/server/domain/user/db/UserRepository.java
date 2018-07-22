package cn.aijiamuyingfang.server.domain.user.db;

import cn.aijiamuyingfang.commons.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
   * 获取管理员用户的Id
   * 
   * @return
   */
  @Query(value = "select user_id from user_authority_list where authority_list=0", nativeQuery = true)
  String findAdminUserId();

}
