package com.mohamed.medhat.sanad.model

import com.mohamed.medhat.sanad.ui.persons_manager_activity.persons.PersonItem

/**
 * A data class representing the response body of the **GET** request that returns the list of the
 * known persons of the blinds.
 *
 * _This is a generated class from a json response._
 */
data class KnownPerson(
    val reminderAbout: String,
    val dateCreated: String,
    val name: String,
    val dateModified: String,
    val id: String,
    val personPictures: List<PersonPicturesItem>
) : PersonItem

/**
 * A data class representing the picture object returned by the **GET** request that returns the list of the
 * known persons of the blinds.
 *
 * _This is a generated class from a json response._
 */
data class PersonPicturesItem(
    val dateCreated: String,
    val uriSegment: String,
    val uri: String
)

