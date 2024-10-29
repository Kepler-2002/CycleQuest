package com.cyclequest.data.sync.adapters

import com.cyclequest.core.base.BaseEntity
import com.cyclequest.service.backend.BackendService
import javax.inject.Inject

class DefaultSyncAdapter<T : BaseEntity> @Inject constructor(
    private val backendService: BackendService,
    private val entityPath: String,
    private val entityClass: Class<T>
) : SyncAdapter<T> {
    
    override suspend fun getAll(): List<T> =
        backendService.request { 
            backendService.getApi().getAll(entityPath, entityClass)
        }

    override suspend fun getById(id: String): T? =
        backendService.request {
            backendService.getApi().getById(entityPath, id, entityClass)
        }

    override suspend fun getByIds(ids: List<String>): List<T> =
        backendService.request {
            backendService.getApi().getByIds(entityPath, ids, entityClass)
        }

    override suspend fun update(entities: List<T>): List<T> =
        backendService.request {
            backendService.getApi().batchUpdate(entityPath, entities, entityClass)
        }

    override suspend fun updateOne(entity: T): T =
        backendService.request {
            backendService.getApi().update(entityPath, entity, entityClass)
        }

    override suspend fun delete(ids: List<String>) {
        backendService.request {
            backendService.getApi().batchDelete(entityPath, ids)
        }
    }

    override suspend fun deleteOne(id: String) {
        backendService.request {
            backendService.getApi().delete(entityPath, id)
        }
    }

    companion object {
        fun <T : BaseEntity> create(
            backendService: BackendService,
            entityClass: Class<T>
        ): DefaultSyncAdapter<T> {
            val entityPath = entityClass.simpleName.removeSuffix("Entity").lowercase() + "s"
            return DefaultSyncAdapter(backendService, entityPath, entityClass)
        }
    }
}
