package com.mohamed.medhat.sanad.ui.persons_manager_activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity that is responsible for displaying the list of the people recognized by the blind.
 * It is also responsible for adding new people that the blind knows.
 */
class PersonsManagerActivity : BaseActivity(), PersonsManagerView {

    @Inject
    lateinit var personsManagerPresenter: PersonsManagerPresenter

    val personsManagerViewModel: PersonsManagerViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.personsManagerViewModel())
            .get(PersonsManagerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_persons_manager)
        activityComponent.inject(this)
        personsManagerPresenter.setView(this)
        personsManagerPresenter.setViewModel(personsManagerViewModel)
        personsManagerPresenter.start(savedInstanceState)
    }
}