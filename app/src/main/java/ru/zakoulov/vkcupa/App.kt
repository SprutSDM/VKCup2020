package ru.zakoulov.vkcupa

import android.app.Application
import ru.zakoulov.vkcupa.data.DocumentRepository
import ru.zakoulov.vkcupa.data.source.vk.VkDocumentsDataSource
import ru.zakoulov.vkcupa.data.source.vk.mappers.DocumentMapper

class App : Application() {

    lateinit var documentRepository: DocumentRepository

    override fun onCreate() {
        super.onCreate()
        documentRepository = DocumentRepository(VkDocumentsDataSource(DocumentMapper()))
        documentRepository.loadDocuments()
    }
}
