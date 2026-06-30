package com.greet.servlet;

import com.greet.model.Greeting;
import com.greet.model.User;
import com.greet.service.GreetingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.springframework.web.context.support.WebApplicationContextUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
@MultipartConfig
public class GreetingServlet extends HttpServlet {
    private GreetingService greetingService;
    private static final String UPLOAD_DIR = "C:/greeting-app-uploads/";

    @Override
    public void init() throws ServletException {
        // Retrieve Spring beans from Context
        var context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.greetingService = context.getBean(GreetingService.class);

        // Ensure upload directory exists
        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path =
                request.getRequestURI().substring(request.getContextPath().length());
        if (path.equals("/greetings")) {
            List<Greeting> greetingsList = greetingService.getAllGreetings();
            request.setAttribute("greetings", greetingsList);
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } else if (path.equals("/greetings/new")) {
            request.setAttribute("title", "Create Greeting");
            request.setAttribute("actionUrl", "greetings/new");
            request.setAttribute("greeting", new Greeting());
            request.getRequestDispatcher("/WEB-INF/views/greeting-form.jsp").forward(request, response);

        } else if (path.equals("/greetings/edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Greeting existing = greetingService.getGreetingById(id);
            if (existing != null) {
                request.setAttribute("title", "Edit Greeting");
                request.setAttribute("actionUrl", "greetings/edit?id=" + id);
                request.setAttribute("greeting", existing);
                request.getRequestDispatcher("/WEB-INF/views/greeting-form.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/greetings");
            }

        } else if (path.equals("/greetings/delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            greetingService.deleteGreeting(id);
            response.sendRedirect(request.getContextPath() + "/greetings");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response)
            throws ServletException, IOException {
        String path =
                request.getRequestURI().substring(request.getContextPath().length());
        User currentUser = (User)
                request.getSession().getAttribute("currentUser");
        if (path.equals("/greetings/new")) {
            String message = request.getParameter("message");
            Part filePart = request.getPart("imageFile");
            String imagePath = null;
            if (filePart != null && filePart.getSize() > 0) {
                imagePath = saveUploadedFile(filePart);
            }
            Greeting newGreet = new Greeting();
            newGreet.setMessage(message);
            newGreet.setImagePath(imagePath);
            newGreet.setCreatedById(currentUser.getId());
            greetingService.createGreeting(newGreet);
            response.sendRedirect(request.getContextPath() + "/greetings");
        } else if (path.equals("/greetings/edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            String message = request.getParameter("message");
            String existingImagePath = request.getParameter("existingImagePath");

            Part filePart = request.getPart("imageFile");
            String imagePath = existingImagePath; // Fallback to old path if no

            if (filePart != null && filePart.getSize() > 0) {
                imagePath = saveUploadedFile(filePart);
                // Optionally delete the old file from disk
                if (existingImagePath != null) {
                    deleteFile(existingImagePath);
                }
            }
            Greeting updatedGreet = new Greeting();
            updatedGreet.setId(id);
            updatedGreet.setMessage(message);
            updatedGreet.setImagePath(imagePath);
            greetingService.updateGreeting(updatedGreet);
            response.sendRedirect(request.getContextPath() + "/greetings");
        }
    }
    private String saveUploadedFile(Part part) throws IOException {
        String submittedFileName =

        Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String fileExt = "";
        int extIndex = submittedFileName.lastIndexOf('.');
        if (extIndex > 0) {
            fileExt = submittedFileName.substring(extIndex);
        }

        String uniqueName = UUID.randomUUID().toString() + fileExt;
        String fullPath = UPLOAD_DIR + uniqueName;
        part.write(fullPath);
        return "/uploads/" + uniqueName;
    }
    private void deleteFile(String relativePath) {
        String fileName = relativePath.replace("/uploads/", "");
        File file = new File(UPLOAD_DIR + fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}





