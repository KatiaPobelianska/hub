package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.model.SystemInfo;
import photo.hub.repository.SystemInfoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SystemInfoService {
    private final SystemInfoRepository systemInfoRepository;

    @Autowired
    public SystemInfoService(SystemInfoRepository systemInfoRepository) {
        this.systemInfoRepository = systemInfoRepository;
    }

    public SystemInfo getById(long id) {
        return systemInfoRepository.findById(id).orElseThrow();
    }

    public SystemInfo getByTitle(String title) {
        return systemInfoRepository.findByTitle(title).orElseThrow();
    }

    public Optional<SystemInfo> getOptionalByTitle(String title) {
        return systemInfoRepository.findByTitle(title);
    }

    public List<SystemInfo> getAll() {
        return systemInfoRepository.findAll();
    }
    public SystemInfo save(SystemInfo info) {
        return systemInfoRepository.save(info);
    }

    public SystemInfo edit(SystemInfo info, long id) {
        SystemInfo infoToSave = getById(id);
        infoToSave.setText(info.getText());
        return save(infoToSave);
    }

    public void delete(long id) {
        systemInfoRepository.deleteById(id);
    }
}
