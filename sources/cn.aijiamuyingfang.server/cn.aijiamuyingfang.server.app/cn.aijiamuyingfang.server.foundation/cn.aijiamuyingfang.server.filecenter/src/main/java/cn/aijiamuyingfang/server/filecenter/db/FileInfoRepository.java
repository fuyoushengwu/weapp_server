package cn.aijiamuyingfang.server.filecenter.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.filecenter.domain.FileInfo;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {
}
