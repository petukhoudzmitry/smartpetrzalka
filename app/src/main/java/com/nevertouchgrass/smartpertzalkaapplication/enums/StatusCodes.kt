package com.nevertouchgrass.smartpertzalkaapplication.enums

enum class StatusCodes(value: Int) {
    OK(200),
    BAD_REQUEST(400);

    companion object {
        fun getStatusCode(value: Int): StatusCodes {
            return if (200 == value) OK else BAD_REQUEST
        }
    }
}