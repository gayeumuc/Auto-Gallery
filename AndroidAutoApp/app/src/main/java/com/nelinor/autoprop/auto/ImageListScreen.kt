package com.nelinor.autoprop.auto

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.ItemList
import androidx.car.app.model.ListTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import com.nelinor.autoprop.SharedImageState
import com.nelinor.autoprop.repository.MediaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Car screen that shows gallery images as a scrollable list.
 * Tapping an image updates [SharedImageState] and pushes [ImageDetailScreen].
 */
class ImageListScreen(carContext: CarContext) : Screen(carContext) {

    private val repository = MediaRepository(carContext)

    private var isLoading = true
    private var imageItems: List<Pair<Long, String>> = emptyList()

    init {
        loadImages()
    }

    private fun loadImages() {
        isLoading = true
        lifecycleScope.launch {
            val images = withContext(Dispatchers.IO) { repository.getImages() }
            imageItems = images.map { it.id to it.displayName }
            isLoading = false
            invalidate()
        }
    }

    override fun onGetTemplate(): Template {
        if (isLoading) {
            return ListTemplate.Builder()
                .setTitle("Gallery")
                .setHeaderAction(Action.BACK)
                .setLoading(true)
                .build()
        }

        if (imageItems.isEmpty()) {
            return ListTemplate.Builder()
                .setTitle("Gallery")
                .setHeaderAction(Action.BACK)
                .setSingleList(
                    ItemList.Builder()
                        .setNoItemsMessage("No images found on device.")
                        .build()
                )
                .build()
        }

        val listBuilder = ItemList.Builder()
        imageItems.take(6).forEach { (id, name) ->
            listBuilder.addItem(
                Row.Builder()
                    .setTitle(name)
                    .addText("Tap to view on car screen")
                    .setOnClickListener { onImageTapped(id) }
                    .build()
            )
        }

        return ListTemplate.Builder()
            .setTitle("Gallery (${imageItems.size} photos)")
            .setHeaderAction(Action.BACK)
            .setSingleList(listBuilder.build())
            .build()
    }

    private fun onImageTapped(id: Long) {
        lifecycleScope.launch {
            val item = withContext(Dispatchers.IO) { repository.getImageById(id) }
            if (item != null) {
                SharedImageState.selectImage(
                    id = item.id,
                    uri = item.uri,
                    name = item.displayName
                )
                screenManager.push(ImageDetailScreen(carContext))
            }
        }
    }
}
