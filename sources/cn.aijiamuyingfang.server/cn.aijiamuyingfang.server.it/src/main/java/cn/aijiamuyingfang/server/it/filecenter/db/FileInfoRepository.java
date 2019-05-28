package cn.aijiamuyingfang.server.it.filecenter.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.aijiamuyingfang.server.it.filecenter.FileInfo;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {
}
