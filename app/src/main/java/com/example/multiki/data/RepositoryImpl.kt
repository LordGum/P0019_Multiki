package com.example.multiki.data

import android.app.Application
import com.example.multiki.domain.Animation
import com.example.multiki.domain.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    application: Application
): Repository {

    private val animDao = AppDatabase.getInstance(application).animationDao()

    override suspend fun addAnim(animation: Animation) {
        animDao.addAnim(animation.mapToDb())
    }

    override suspend fun deleteAnim(createAt: Long) {
        animDao.deleteAnim(createAt)
    }

    override fun getAnimList(): Flow<List<Animation>> = animDao.getAnimList().map {
        it.map { dbModel -> dbModel.mapToEntity() }
    }

    override suspend fun getAnimInfo(createAt: Long): Animation {
        return animDao.getAnimInfo(createAt).mapToEntity()
    }

    override suspend fun allDelete() {
        animDao.allDelete()
    }

    fun getFileName(createAt: Long): String = "anim_$createAt"
}