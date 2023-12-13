package com.neftali.springboot.app.service;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public interface IUploadService {
	
	
	public String copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
	
	public void deleteAll();
	
	public void init() throws IOException;

}
