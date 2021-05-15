package com.mohamed.medhat.sanad.ui.persons_manager_activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.gps_errors.KnownPersonAdd
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.persons_manager_activity.persons.PersonItem
import com.mohamed.medhat.sanad.ui.persons_manager_activity.persons.PersonsAdapter
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_BLIND_PROFILE
import com.mohamed.medhat.sanad.utils.SPAN_KNOWN_PERSONS
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_persons_manager.*
import javax.inject.Inject

/**
 * An mvp presenter for the [PersonsManagerActivity].
 */
class PersonsManagerPresenter @Inject constructor() :
    AdvancedPresenter<PersonsManagerView, PersonsManagerViewModel>() {

    private lateinit var personsManagerView: PersonsManagerView
    private lateinit var personsManagerViewModel: PersonsManagerViewModel
    private lateinit var activity: PersonsManagerActivity
    lateinit var blindMiniProfile: BlindMiniProfile
    private lateinit var personsAdapter: PersonsAdapter
    private lateinit var personsRecyclerView: RecyclerView

    override fun start(savedInstanceState: Bundle?) {
        initPersonsRecyclerView()
        if (savedInstanceState == null) {
            personsManagerViewModel.loadKnownPersons(blindMiniProfile)
        }
        personsManagerViewModel.knownPersons.observe(activity) {
            val addItem = KnownPersonAdd()
            val newList = mutableListOf<PersonItem>()
            newList.addAll(it)
            newList.add(addItem)
            personsAdapter.update(newList)
            if (it.isEmpty()) {
                personsManagerView.displayEmptyListHint()
            } else {
                personsManagerView.hideEmptyListHint()
            }
        }
        personsManagerViewModel.shouldReLogin.observe(activity) {
            if (it) {
                personsManagerView.startActivityAsRoot(LoginActivity::class.java)
            }
        }
        personsManagerViewModel.state.observe(activity) {
            val errorViewer =
                TextErrorViewer(personsManagerViewModel.appError, activity.tv_persons_manager_error)
            personsManagerView.setAppErrorViewer(errorViewer)
            handleLoadingState(personsManagerView, it)
        }
    }

    override fun setView(view: PersonsManagerView) {
        personsManagerView = view
        activity = view as PersonsManagerActivity
        blindMiniProfile =
            activity.intent.extras?.getSerializable(FRAGMENT_FEATURES_BLIND_PROFILE) as BlindMiniProfile
    }

    override fun setViewModel(viewModel: PersonsManagerViewModel) {
        personsManagerViewModel = viewModel
    }

    /**
     * Performs the initial setup for the personsRecyclerView and personsAdapter objects.
     */
    private fun initPersonsRecyclerView() {
        personsRecyclerView = activity.rv_persons_manager_known_persons
        personsAdapter = PersonsAdapter()
        personsRecyclerView.layoutManager = GridLayoutManager(activity, SPAN_KNOWN_PERSONS)
        personsRecyclerView.adapter = personsAdapter
    }
}