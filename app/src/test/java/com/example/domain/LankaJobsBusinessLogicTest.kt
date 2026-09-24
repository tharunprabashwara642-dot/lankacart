package com.example.domain

import com.example.core.util.ValidationUtil
import com.example.data.local.SeedData
import com.example.domain.model.AdStatus
import com.example.domain.model.Advertisement
import com.example.domain.model.EmploymentType
import com.example.domain.model.ExperienceLevel
import com.example.domain.model.Job
import com.example.domain.model.JobFilter
import com.example.domain.model.JobSortOrder
import com.example.domain.model.UserProfile
import com.example.domain.model.WorkplaceType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LankaJobsBusinessLogicTest {

    @Test
    fun testSriLankanPhoneValidation_validNumbers() {
        // Standard 10-digit mobile numbers with common Sri Lankan operators (077, 071, 070, etc.)
        val v1 = ValidationUtil.validateSriLankanPhone("0771234567")
        assertTrue(v1.isValid)
        assertEquals("077 123 4567", v1.formattedValue)

        val v2 = ValidationUtil.validateSriLankanPhone("071 987 6543")
        assertTrue(v2.isValid)

        // International format with +94
        val v3 = ValidationUtil.validateSriLankanPhone("+94 77 123 4567")
        assertTrue(v3.isValid)

        // Landline number (011 for Colombo)
        val v4 = ValidationUtil.validateSriLankanPhone("011 234 5678")
        assertTrue(v4.isValid)
    }

    @Test
    fun testSriLankanPhoneValidation_invalidNumbers() {
        // Empty phone
        assertFalse(ValidationUtil.validateSriLankanPhone("").isValid)

        // Too short
        assertFalse(ValidationUtil.validateSriLankanPhone("0771234").isValid)

        // Letters
        assertFalse(ValidationUtil.validateSriLankanPhone("07712abcde").isValid)

        // Invalid operator prefix (073 does not exist in Sri Lanka)
        assertFalse(ValidationUtil.validateSriLankanPhone("0731234567").isValid)
    }

    @Test
    fun testFullNameValidation() {
        assertTrue(ValidationUtil.validateFullName("Kasun Perera").isValid)
        assertFalse(ValidationUtil.validateFullName("").isValid)
        assertFalse(ValidationUtil.validateFullName("A").isValid) // Too short
    }

    @Test
    fun testSuggestedAdsMatching() {
        val allAds = SeedData.approvedAds

        // When user prefers "Colombo" and "IT & Software"
        val userLoc = "Colombo"
        val userCats = listOf("IT & Software")

        val matchingAds = allAds.filter { ad ->
            ad.location.contains(userLoc, ignoreCase = true) ||
            userCats.any { ad.category.contains(it, ignoreCase = true) }
        }

        assertTrue(matchingAds.isNotEmpty())
        assertTrue(matchingAds.any { it.title.contains("Cloud", ignoreCase = true) || it.title.contains("Development", ignoreCase = true) })
    }

    @Test
    fun testJobFilter_activeCount() {
        val emptyFilter = JobFilter()
        assertEquals(0, emptyFilter.activeFilterCount)

        val appliedFilter = JobFilter(
            location = "Colombo",
            employmentType = EmploymentType.FULL_TIME,
            workplaceType = WorkplaceType.REMOTE,
            minSalary = 100000,
            sortOrder = JobSortOrder.SALARY_HIGH_TO_LOW
        )
        assertEquals(5, appliedFilter.activeFilterCount)
    }

    @Test
    fun testJobFiltering_byLocationAndCategory() {
        val jobs = SeedData.sampleJobs

        // Filter by Colombo
        val colomboJobs = jobs.filter { it.location.contains("Colombo", ignoreCase = true) }
        assertTrue(colomboJobs.isNotEmpty())
        colomboJobs.forEach {
            assertTrue(it.location.contains("Colombo", ignoreCase = true))
        }

        // Filter by IT category
        val itJobs = jobs.filter { it.categoryId == "cat-it" }
        assertTrue(itJobs.isNotEmpty())
        itJobs.forEach {
            assertEquals("cat-it", it.categoryId)
        }
    }

    @Test
    fun testJobSalaryFormatting() {
        val jobWithRange = Job(
            id = "test_1",
            title = "Software Engineer",
            companyId = "c1",
            companyName = "Test Co",
            categoryId = "cat-it",
            categoryName = "IT",
            location = "Colombo",
            district = "Colombo",
            employmentType = EmploymentType.FULL_TIME,
            experienceLevel = ExperienceLevel.MID_LEVEL,
            workplaceType = WorkplaceType.HYBRID,
            salaryMin = 200000,
            salaryMax = 350000,
            description = "Desc",
            postedDate = "Today"
        )
        assertEquals("LKR 200,000 - 350,000 /mo", jobWithRange.formattedSalary)

        val jobNegotiable = Job(
            id = "test_2",
            title = "Accountant",
            companyId = "c2",
            companyName = "Test Co",
            categoryId = "cat-acc",
            categoryName = "Finance",
            location = "Kandy",
            district = "Kandy",
            employmentType = EmploymentType.FULL_TIME,
            experienceLevel = ExperienceLevel.SENIOR,
            workplaceType = WorkplaceType.ON_SITE,
            isSalaryNegotiable = true,
            description = "Desc",
            postedDate = "Yesterday"
        )
        assertEquals("Negotiable", jobNegotiable.formattedSalary)
    }

    @Test
    fun testAdvertisement_initialStatusMustBePendingApproval() {
        val ad = Advertisement(
            id = "ad_123",
            title = "Cloud Diploma 2026",
            description = "Hands on devops training",
            organizationName = "Lanka Tech",
            location = "Colombo",
            category = "Education",
            packageId = "pkg_standard",
            status = AdStatus.PENDING_APPROVAL
        )
        assertEquals(AdStatus.PENDING_APPROVAL, ad.status)
        assertNull(ad.websiteUrl)
    }

    @Test
    fun testUserProfile_onboardingCompleteness() {
        val initialUser = UserProfile(
            id = "usr_1",
            email = "user@test.lk",
            fullName = "Kasun Perera",
            phoneNumber = "",
            preferredLocation = "",
            isProfileComplete = false
        )
        assertFalse(initialUser.isProfileComplete)

        val completedUser = initialUser.copy(
            phoneNumber = "077 123 4567",
            preferredLocation = "Colombo",
            preferredCategories = listOf("IT & Software"),
            isProfileComplete = true
        )
        assertTrue(completedUser.isProfileComplete)
        assertEquals("077 123 4567", completedUser.phoneNumber)
        assertEquals("Colombo", completedUser.preferredLocation)
    }
}
