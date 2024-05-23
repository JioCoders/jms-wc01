// package com.harry.userservice.controller;

// import static org.springframework.http.HttpHeaders.AUTHORIZATION;

// import com.jiocoders.rate.base.ResponseBase;
// import com.jiocoders.rate.entity.Employee;
// import com.jiocoders.rate.model.FileInfo;
// import com.jiocoders.rate.networkmodel.response.ResponseImageList;
// import com.jiocoders.rate.networkmodel.response.ResponseUploadFile;
// import com.jiocoders.rate.repository.AdminRepository;
// import com.jiocoders.rate.security.AuthScope;
// import com.jiocoders.rate.security.JwtTokenProvider;
// import com.jiocoders.rate.services.ImageService;
// import com.jiocoders.rate.utils.ApiConstant;
// import com.jiocoders.rate.utils.StrConstant;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.Resource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import
// org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

// import java.io.IOException;
// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import static com.jiocoders.rate.utils.ApiConstant.BAD_TOKEN_CODE;
// import static com.jiocoders.rate.utils.Common.checkNotNull;

// /**
// * – @CrossOrigin is for configuring allowed origins.
// * – @Controller annotation is used to define a controller.
// * – @GetMapping and @PostMapping annotation is for mapping HTTP GET & POST
// * requests onto specific handler methods:
// * <p>
// * POST /upload: uploadFile()
// * GET /files: getListFiles()
// * GET /files/[filename]: getFile()
// * – We use @Autowired to inject implementation of FilesStorageService bean to
// * local variable.
// */

// // @CrossOrigin("http://localhost:8090")
// @RestController("/image")
// public class ImageController {
// private static String TAG = ImageController.class.getSimpleName();
// private static final Logger logger =
// LoggerFactory.getLogger(ImageController.class);

// @Autowired
// AdminRepository adminRepository;
// @Autowired
// JwtTokenProvider jwtTokenProvider;
// @Autowired
// ImageService storageService;

// @PostMapping(value = ApiConstant.IMAGE_UPLOAD, consumes =
// MediaType.MULTIPART_FORM_DATA_VALUE)
// public ResponseEntity<ResponseUploadFile>
// uploadImage(@RequestHeader(AUTHORIZATION) String token,
// @RequestParam("file") MultipartFile file, @RequestParam("emp_id") String
// empId) throws IOException {
// ResponseUploadFile response = new ResponseUploadFile();
// if (!storageService.imageFile(file.getOriginalFilename())) {
// response.setMessage("Invalid image file extension!");
// response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
// return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
// }
// if (!checkNotNull(token)) {
// response.setMessage(StrConstant.TOKEN_NOT_FOUND);
// response.setStatus(BAD_TOKEN_CODE);
// return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
// }

// // CHECK ONLY ADMIN USER
// String bearer = jwtTokenProvider.resolveToken(token);
// if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
// && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
// response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
// response.setStatus(ApiConstant.FORBIDDEN_CODE);
// return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
// }
// // CHECK VALID USER ID
// UUID userId = UUID.fromString(jwtTokenProvider.getUsername(bearer));
// logger.info("=>UserId=" + userId);
// Employee employee = adminRepository.findById(userId).get();
// if (employee == null) {
// response.setMessage("User not found!");
// response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
// return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
// }
// if (!employee.isActive()) {
// response.setMessage("User is not active to update!");
// response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
// return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
// }
// if (!checkNotNull(empId) || empId.length() < 10) {
// response.setMessage("Please provide valid user id!");
// response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
// return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
// }

// try {
// String newFileName = storageService.save(file, empId);
// employee.setImage(newFileName);
// adminRepository.saveAndFlush(employee);
// String message = "File uploaded successfully!";
// response.setMessage(message);
// response.fileName = newFileName;
// response.setStatus(ApiConstant.SUCCESS_CODE);
// return ResponseEntity.status(HttpStatus.OK).body(response);
// } catch (Exception e) {
// String message = "Could not upload the file: " + file.getOriginalFilename() +
// "!";
// response.setMessage(message);
// response.setStatus(ApiConstant.SUCCESS_CODE);
// return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
// }
// }

// /**
// * Get list of images with url but
// * Currently this api is not working for unknown reason
// * with could not find getPath after getting list of images
// *
// * @param token
// * @return response list
// */
// @GetMapping(ApiConstant.IMAGE_LIST)
// public ResponseEntity<ResponseImageList>
// getListImages(@RequestHeader(AUTHORIZATION) String token) {
// ResponseImageList response = new ResponseImageList();
// if (!checkNotNull(token)) {
// response.setMessage(StrConstant.TOKEN_NOT_FOUND);
// response.setStatus(BAD_TOKEN_CODE);
// return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
// }

// // CHECK ONLY ADMIN USER
// String bearer = jwtTokenProvider.resolveToken(token);
// if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
// && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
// response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
// response.setStatus(ApiConstant.FORBIDDEN_CODE);
// return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
// }
// // CHECK VALID USER ID
// UUID userId = UUID.fromString(jwtTokenProvider.getUsername(bearer));
// logger.info("=>UserId=" + userId);
// Employee employee = adminRepository.findById(userId).get();
// if (employee == null) {
// response.setMessage("User not found!");
// response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
// return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
// }
// if (!employee.isActive()) {
// response.setMessage("User is not active to update!");
// response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
// return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
// }

// try {
// List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
// String filename = path.getFileName().toString();
// String url = MvcUriComponentsBuilder
// .fromMethodName(ImageController.class, "getFile",
// path.getFileName().toString()).build()
// .toString();
// return new FileInfo(filename, url);
// }).collect(Collectors.toList());
// response.imageList.addAll(fileInfos);
// response.setStatus(ApiConstant.SUCCESS_CODE);
// response.setMessage(StrConstant.SUCCESS);
// } catch (Exception e) {
// e.printStackTrace();
// response.setStatus(ApiConstant.SERVER_ERROR_CODE);
// response.setMessage(StrConstant.SERVER_ERROR);
// return
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
// }
// return ResponseEntity.status(HttpStatus.OK).body(response);
// }

// @GetMapping("/files/{filename:.+}")
// @ResponseBody
// public ResponseEntity<Resource> getFile(@PathVariable String filename
// // , HttpServletRequest request
// ) {
// // Try to determine file's content type
// // String contentType = null;
// // try {
// // contentType =
// //
// request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
// // } catch (IOException ex) {
// // logger.info("Could not determine file type.");
// // }
// Resource file = null;
// try {
// file = storageService.load(filename);
// return ResponseEntity.ok()
// .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
// file.getFilename() + "\"")
// .body(file);
// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(file);
// }
// }
// }