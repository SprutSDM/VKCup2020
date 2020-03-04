package ru.zakoulov.vkcupa

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.zakoulov.vkcupa.data.DocumentRepository
import ru.zakoulov.vkcupa.data.source.vk.VkDocumentsDataSource
import ru.zakoulov.vkcupa.data.source.vk.mappers.DocumentMapper

class App : Application() {

    lateinit var documentRepository: DocumentRepository

    val tokenExpired = MutableLiveData<Int>()

    private var countOfTokenExpiration: Int = 0

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
        documentRepository = DocumentRepository(VkDocumentsDataSource(DocumentMapper()))
        if (VK.isLoggedIn()) {
            documentRepository.loadDocuments(trackProgress = true)
        }
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            tokenExpired.value = ++countOfTokenExpiration
        }
    }
}
