package com.fruitscale.ananas

import org.apache.commons.lang3.text.WordUtils

/**
 * Created by wouter on 18-4-17.

 * Contact class that contains details about a contact
 *
 * @param username The Matrix.org username for this contact
 * @param email The email for this contact
 * @param phoneNumber The phone number for this contact
 * @param fullName The full name for this user
 */
class Contact(val username: String, val email: String? = null, val phoneNumber: String? = null, val fullName: String? = null) {
    val bestName: String
        get() {
            if (fullName != null) {
                return WordUtils.capitalize(fullName)
            } else {
                return username
            }
        }
}
