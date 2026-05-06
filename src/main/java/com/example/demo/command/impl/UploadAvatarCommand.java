package com.example.demo.command.impl;

import com.example.demo.command.Command;
import com.example.demo.command.PageConstants;
import com.example.demo.command.AttributeConstants;
import com.example.demo.entity.User;
import com.example.demo.exception.DataException;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class UploadAvatarCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(UploadAvatarCommand.class);
    private static final String UPLOAD_DIR = "uploads/avatars";

    @Override
    public String execute(HttpServletRequest request) {
        try {
            String currentUser = (String) request.getSession().getAttribute(AttributeConstants.USER_ATTR);
            if (currentUser == null) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Пожалуйста, сначала войдите в систему");
                return PageConstants.INDEX_PAGE;
            }

            Part filePart = request.getPart("avatar");
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Файл не выбран");
                return PageConstants.EDIT_PROFILE_PAGE;
            }

            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Разрешены только файлы изображений");
                return PageConstants.EDIT_PROFILE_PAGE;
            }

            if (filePart.getSize() > 5 * 1024 * 1024) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Размер файла не должен превышать 5 МБ");
                return PageConstants.EDIT_PROFILE_PAGE;
            }

            String appPath = request.getServletContext().getRealPath("");
            String uploadPath = appPath + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID().toString() + "_" + getFileName(filePart);
            String filePath = uploadPath + File.separator + fileName;

            filePart.write(filePath);

            UserService userService = UserServiceImpl.getInstance();
            Optional<User> userOpt = userService.findByLogin(currentUser);
            if (userOpt.isPresent()) {
                String dbPath = UPLOAD_DIR + "/" + fileName;
                userService.updateAvatar(userOpt.get().getId(), dbPath);

                User updatedUser = userService.findByLogin(currentUser).get();
                request.getSession().setAttribute("userAvatar", updatedUser.getAvatarPath());

                request.setAttribute(AttributeConstants.MESSAGE_ATTR, "Фото профиля успешно обновлено!");
            }

        } catch (IOException | ServletException | DataException e) {
            LOGGER.error("Error uploading avatar", e);
            request.setAttribute(AttributeConstants.ERROR_MESSAGE_ATTR, "Не удалось обновить фото профиля");
        }

        return PageConstants.EDIT_PROFILE_PAGE;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf("=") + 2, token.length() - 1);
                return fileName.replace("\\", "/").substring(fileName.lastIndexOf("/") + 1);
            }
        }
        return "unknown.jpg";
    }
}