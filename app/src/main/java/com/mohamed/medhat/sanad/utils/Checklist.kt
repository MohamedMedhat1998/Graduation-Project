package com.mohamed.medhat.sanad.utils

/**
 * Checklist pattern, source: https://medium.com/@mohamed.medhat0298/checklist-pattern-ec473bb39ee8
 */
class Checklist(private val onComplete: () -> Unit) {

    private val checklistItems = mutableMapOf<String, Boolean>()

    fun register(item: String) {
        checklistItems[item] = false
    }

    fun check(item: String) {
        checklistItems[item] = true
        isComplete()
    }

    private fun isComplete() {
        var completed = true
        checklistItems.entries.forEach {
            if (!it.value) {
                completed = false
                return@forEach
            }
        }
        if (completed) {
            onComplete.invoke()
        }
    }
}