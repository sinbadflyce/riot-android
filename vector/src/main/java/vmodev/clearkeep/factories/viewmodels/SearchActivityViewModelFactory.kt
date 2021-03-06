package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractSearchActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class SearchActivityViewModelFactory @Inject constructor(@Named(IActivity.SEARCH_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractSearchActivityViewModel> {
    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractSearchActivityViewModel::class.java);
    override fun getViewModel(): AbstractSearchActivityViewModel {
        return viewModel;
    }
}