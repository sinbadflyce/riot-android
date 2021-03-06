package vmodev.clearkeep.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.jakewharton.rxbinding3.viewpager.pageSelections
import im.vector.R
import im.vector.databinding.ActivitySearchBinding
import im.vector.extensions.hideKeyboard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.adapters.SearchViewPagerAdapter
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.fragments.Interfaces.ISearchFragment
import vmodev.clearkeep.fragments.SearchFilesFragment
import vmodev.clearkeep.fragments.SearchMessagesFragment
import vmodev.clearkeep.fragments.SearchPeopleFragment
import vmodev.clearkeep.fragments.SearchRoomsFragment
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchActivityViewModel
import javax.inject.Inject


class SearchActivity : DataBindingDaggerActivity(), IActivity, SearchRoomsFragment.OnFragmentInteractionListener
        , SearchFilesFragment.OnFragmentInteractionListener
        , SearchMessagesFragment.OnFragmentInteractionListener
        , SearchPeopleFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var viewModelFactory: IViewModelFactory<AbstractSearchActivityViewModel>

    private val publishSubjectSearchView: PublishSubject<String> = PublishSubject.create()

    private var disposable: Disposable? = null

    private lateinit var arraySearchFragment: Array<ISearchFragment>

    private var currentSearchFragment: ISearchFragment? = null

    private lateinit var userId: String
    private lateinit var binding: ActivitySearchBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search, dataBinding.getDataBindingComponent())
        userId = intent.getStringExtra(USER_ID)
        arraySearchFragment = arrayOf(SearchRoomsFragment.newInstance(userId), SearchMessagesFragment.newInstance(userId),
                SearchPeopleFragment.newInstance(), SearchFilesFragment.newInstance(userId))
        binding.imgBack.setOnClickListener { v -> finish() }
        val pagerAdapter = SearchViewPagerAdapter(supportFragmentManager, arraySearchFragment)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = pagerAdapter
        val backgroundView = binding.searchView.findViewById(androidx.appcompat.R.id.search_plate) as View
        backgroundView.background = null
        binding.searchView.onActionViewExpanded()
        Handler().postDelayed(Runnable {  binding.searchView.clearFocus() }, 30)
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.isIconified = false

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { s -> publishSubjectSearchView.onNext(s) }
                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
        })
        disposable = binding.viewPager.pageSelections().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            currentSearchFragment?.unSelectedFragment()
            currentSearchFragment = arraySearchFragment[it].selectedFragment(binding.searchView.query.toString())
        }

        binding.layoutSearch.setOnTouchListener { v, event ->
            Handler().postDelayed(Runnable {  binding.searchView.clearFocus() }, 30)
                hideKeyboard()
//            }else{
//                Log.d("","")
//            }
            return@setOnTouchListener true
        }
        binding.lifecycleOwner = this
    }

    override fun getActivity(): FragmentActivity {
        return this
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        publishSubjectSearchView.onComplete()
        disposable?.dispose()
    }


    override fun getSearchViewTextChange(): Observable<String> {
        return publishSubjectSearchView
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed(Runnable {  binding.searchView.clearFocus() }, 30)

    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}
