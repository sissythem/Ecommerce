package gr.di.ecommerce.airbnb.controllers;

import gr.di.ecommerce.airbnb.entities.Images;
import gr.di.ecommerce.airbnb.services.ImagesService;
import gr.di.ecommerce.airbnb.utils.Constants;
import gr.di.ecommerce.airbnb.utils.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/images/")
public class ImagesController {

    private static String className = ImagesController.class.getSimpleName();

    @Autowired
    private ImagesService imagesService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void create(Images entity) {
        imagesService.createImage(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void edit(@PathVariable("id") Integer id, Images entity) {
        imagesService.updateImage(entity);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String remove(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") String id) {
        if (KeyHolder.checkToken(token, className)) {
            imagesService.removeImage(Integer.parseInt(id));
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "deleteimg/profile/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deleteUserImg(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            try {
                imagesService.deleteUserProfileImage(id);
                return token;
            } catch (IOException e) {
                e.printStackTrace();
                return Constants.NOT;
            }
        }
        return Constants.NOT;
    }

    @RequestMapping(value = "deleteimg/residence/{id}/{resId}/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String deleteResidenceImage(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                     @PathVariable("id") Integer id, @PathVariable("resId") Integer resId,
                                                     @PathVariable("name") String name) {
        if (KeyHolder.checkToken(token, className)) {
            try {
                imagesService.deleteResidenceImage(id, resId, name);
                return token;
            } catch (IOException e) {
                e.printStackTrace();
                return Constants.NOT;
            }
        }
        return Constants.NOT;
    }

    @RequestMapping(value = "profilepic/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadUserImage(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id,
                                                @RequestParam("picture") MultipartFile file) {
        if (KeyHolder.checkToken(token, className)) {
            try {
                return imagesService.uploadUserImage(id, file);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @RequestMapping(value = "residence/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadResidenceImage(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id,
                                                     @RequestParam("picture")MultipartFile file) {
        if (KeyHolder.checkToken(token, className)) {
            try {
                return imagesService.uploadResidenceImage(id, file);
            } catch (IOException e) {
                e.printStackTrace();
                return Constants.NOT;
            }
        }
        return Constants.NOT;
    }

    @RequestMapping(value = "img/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Response getUserImage(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("name") String name) {
        if (KeyHolder.checkToken(token, className)) {
            return imagesService.getUserImage(name);
        }
        return null;
    }

    @RequestMapping(value = "residence/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Images> getResidencePhotos(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                         @PathVariable("id") Integer residenceId) {
        if (KeyHolder.checkToken(token, className)) {
            return imagesService.getResidencePhotos(residenceId);
        }
        return null;
    }

}
