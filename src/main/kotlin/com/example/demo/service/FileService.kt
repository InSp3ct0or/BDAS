package com.example.demo.service

import com.example.demo.entity.BinaryContent
import com.example.demo.repository.FileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class FileService(@Autowired private val fileRepository: FileRepository) {

    // Загружаем файл в базу данных
    fun uploadFile(file: MultipartFile, uploadedBy: String): Long {
        val fileData = BinaryContent(
            fileName = file.originalFilename ?: "unknown",
            fileType = file.contentType ?: "unknown",
            fileExtension = file.originalFilename?.substringAfterLast('.') ?: "unknown",
            fileData = file.bytes,
            uploadedBy = uploadedBy,
            uploadDate = java.util.Date(),
            modifyDate = java.util.Date()
        )

        val savedFile = fileRepository.save(fileData)
        return savedFile.contentId
    }

    // Получаем бинарные данные файла по ID
    fun getFileData(fileId: Long): ByteArray {
        val file = fileRepository.findById(fileId).orElseThrow { RuntimeException("File not found") }
        return file.fileData
    }
}