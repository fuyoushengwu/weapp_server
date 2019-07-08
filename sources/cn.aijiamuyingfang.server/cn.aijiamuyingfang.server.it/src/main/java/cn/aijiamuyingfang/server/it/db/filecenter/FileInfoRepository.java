package cn.aijiamuyingfang.server.it.db.filecenter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.dto.filecenter.FileInfoDTO;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfoDTO, String> {
}
