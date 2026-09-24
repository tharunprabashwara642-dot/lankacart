package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.AdvertisementEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.SavedJobEntity
import com.example.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs_cache ORDER BY isFeatured DESC, cachedAt DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs_cache WHERE isFeatured = 1 ORDER BY cachedAt DESC LIMIT 10")
    fun getFeaturedJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs_cache ORDER BY cachedAt DESC LIMIT :limit")
    fun getLatestJobs(limit: Int): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs_cache WHERE categoryId = :categoryId ORDER BY cachedAt DESC")
    fun getJobsByCategory(categoryId: String): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs_cache WHERE id = :id LIMIT 1")
    fun getJobById(id: String): Flow<JobEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobEntity>)

    @Query("SELECT COUNT(*) FROM jobs_cache")
    suspend fun getJobCount(): Int

    @Query("DELETE FROM jobs_cache")
    suspend fun clearAll()
}

@Dao
interface SavedJobDao {
    @Query("SELECT jobId FROM saved_jobs ORDER BY savedAt DESC")
    fun getSavedJobIds(): Flow<List<String>>

    // High performance direct SQLite join query
    @Query("SELECT jobs_cache.* FROM jobs_cache INNER JOIN saved_jobs ON jobs_cache.id = saved_jobs.jobId ORDER BY saved_jobs.savedAt DESC")
    fun getSavedJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM saved_jobs ORDER BY savedAt DESC")
    fun getAllSavedJobs(): Flow<List<SavedJobEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_jobs WHERE jobId = :jobId)")
    suspend fun isJobSaved(jobId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(savedJob: SavedJobEntity)

    @Query("DELETE FROM saved_jobs WHERE jobId = :jobId")
    suspend fun removeSavedJob(jobId: String)
}

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getCurrentProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profiles")
    suspend fun clearProfile()
}

@Dao
interface AdvertisementDao {
    @Query("SELECT * FROM advertisements WHERE status = 'APPROVED' ORDER BY createdAt DESC")
    fun getApprovedAdvertisements(): Flow<List<AdvertisementEntity>>

    @Query("SELECT * FROM advertisements WHERE id = :id LIMIT 1")
    fun getAdvertisementById(id: String): Flow<AdvertisementEntity?>

    @Query("SELECT * FROM advertisements WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserAdvertisements(userId: String): Flow<List<AdvertisementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(ad: AdvertisementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ads: List<AdvertisementEntity>)

    @Query("SELECT COUNT(*) FROM advertisements")
    suspend fun getCount(): Int
}
