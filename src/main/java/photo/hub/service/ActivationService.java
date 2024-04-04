package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.model.Activation;
import photo.hub.repository.ActivationRepository;

@Service
public class ActivationService {
    private final ActivationRepository activationRepository;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public ActivationService(ActivationRepository activationRepository, PersonDetailsService personDetailsService) {
        this.activationRepository = activationRepository;
        this.personDetailsService = personDetailsService;
    }


    public String generateCode(String username) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int size = (int) (Math.random() * 6 + 10);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * alphabet.length());
            stringBuilder.append(alphabet.charAt(index));
        }
        Activation activation = new Activation(stringBuilder.toString(), username);
        activationRepository.save(activation);
        return stringBuilder.toString();
    }
    public void activate(String key){
        Activation activation = activationRepository.findByKey(key).orElseThrow();
        personDetailsService.activate(activation.getUsername());
    }

}
