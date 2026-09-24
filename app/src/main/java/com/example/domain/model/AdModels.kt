package com.example.domain.model

enum class AdStatus(val label: String) {
    DRAFT("Draft"),
    PENDING_PAYMENT("Pending Payment"),
    PAYMENT_RECEIVED("Payment Received"),
    PENDING_APPROVAL("Pending Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    EXPIRED("Expired"),
    CANCELLED("Cancelled");

    companion object {
        fun fromString(value: String): AdStatus =
            entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
                ?: PENDING_APPROVAL
    }
}

data class AdvertisementPackage(
    val id: String,
    val name: String,
    val priceLkr: Int,
    val durationDays: Int,
    val features: List<String>,
    val isPopular: Boolean = false
) {
    val formattedPrice: String
        get() = "LKR %,d".format(priceLkr)
}

data class Advertisement(
    val id: String,
    val title: String,
    val description: String,
    val organizationName: String,
    val imageUrl: String? = null,
    val websiteUrl: String? = null, // Optional official website URL per product spec!
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val location: String,
    val category: String,
    val packageId: String,
    val packageName: String = "Standard Package",
    val status: AdStatus = AdStatus.PENDING_APPROVAL,
    val createdAt: Long = System.currentTimeMillis(),
    val validUntil: Long? = null,
    val userId: String? = null
)

data class PaymentOrder(
    val id: String,
    val adId: String,
    val packageId: String,
    val amountLkr: Int,
    val currency: String = "LKR",
    val status: String = "INITIALIZED", // Future payment state
    val createdAt: Long = System.currentTimeMillis()
)
