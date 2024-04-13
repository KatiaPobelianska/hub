package photo.hub.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import photo.hub.model.SystemInfo;
import photo.hub.service.SystemInfoService;

@Component
public class SystemInfoValidator implements Validator {
    private final SystemInfoService systemInfoService;

    @Autowired
    public SystemInfoValidator(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SystemInfo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SystemInfo systemInfo = (SystemInfo) target;
        if (systemInfoService.getOptionalByTitle(systemInfo.getTitle()).isPresent()) {
            errors.rejectValue("title", "", "this title already in use");
        }
    }
}