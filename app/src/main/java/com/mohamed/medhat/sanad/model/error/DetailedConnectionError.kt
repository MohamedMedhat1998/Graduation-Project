package com.mohamed.medhat.sanad.model.error

/**
 * A data class used to represent the detailed error from a network connection.
 */
data class DetailedConnectionError(
	val id: String,
	val statusCode: Int,
	val title: String,
	val description: String,
	val errorDate: String,
	val errors: Map<String, List<String>>
) : AppError
