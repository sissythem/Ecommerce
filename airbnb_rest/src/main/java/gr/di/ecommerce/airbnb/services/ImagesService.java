package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Images;
import gr.di.ecommerce.airbnb.entities.Users;
import gr.di.ecommerce.airbnb.repositories.ImagesRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ImagesService {

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ResidencesService residencesService;

    @Value("${images.folder}")
    private String imagesPath;

    public void createImage(Images images) {
        imagesRepository.save(images);
    }

    public void updateImage(Images images) {
        imagesRepository.save(images);
    }

    public void removeImage(Integer id) {
        imagesRepository.delete(id.toString());
    }

    public void removeImage(Images images) {
        imagesRepository.delete(images);
    }

    public void deleteUserProfileImage(Integer userId) throws IOException {
        Users user = userService.getUser(userId);
        if(user != null) {
            userService.deleteUserPhoto(user.getId());
            String profilePic = user.getPhoto();
            Path path = Paths.get(imagesPath, profilePic);
            if(Files.exists(path)) {
                Files.delete(path);
            }
        }
    }

    public void deleteResidenceImage(Integer id, Integer residenceId, String name) throws IOException {
        Path path = Paths.get(imagesPath, name);
        if(Files.exists(path)) {
            Files.delete(path);
        }
        removeImage(id);
    }

    public String uploadUserImage(Integer id, MultipartFile fileDetail) throws IOException {
        userService.deleteUserPhoto(id);
        File newFile = File.createTempFile("img", ".jpg", new File(imagesPath));
        saveToFile(fileDetail, newFile);
        userService.updateUserPhoto(newFile.getName(), id);
        return newFile.getName();
    }

    public String uploadResidenceImage(Integer residenceId, MultipartFile fileDetail) throws IOException {
        File newFile = File.createTempFile("img", ".jpg", new File(imagesPath));
        saveToFile(fileDetail, newFile);
        Images images = new Images();
        images.setName(newFile.getName());
        images.setResidenceId(residencesService.getResidence(residenceId));
        createImage(images);
        return newFile.getName();
    }

    public Response getUserImage(String name) {
        Path path = Paths.get(imagesPath, name);
        if(!Files.exists(path)) {
            Logger.getLogger(ImagesService.class).error("Image not found");
        }
        String mt = new MimetypesFileTypeMap().getContentType(path.toFile());
        return Response.ok(path.toFile(), mt).build();
    }

    public List<Images> getResidencePhotos(Integer residenceId) {
        return imagesRepository.findAllImagesByResidenceId(residenceId);
    }

    private void saveToFile(MultipartFile file, File target) throws IOException {
        java.nio.file.Path path = target.toPath();
        File convFile = new File(file.getOriginalFilename());
        Files.copy(convFile.toPath(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
}
