package vmodev.clearkeep.factories.viewmodels

import androidx.lifecycle.ViewModelProvider
import vmodev.clearkeep.activities.interfaces.IActivity
import vmodev.clearkeep.factories.viewmodels.interfaces.IViewModelFactory
import vmodev.clearkeep.viewmodels.interfaces.AbstractRoomSettingsActivityViewModel
import javax.inject.Inject
import javax.inject.Named

class RoomSettingsActivityViewModelFactory @Inject constructor(@Named(IActivity.ROOM_SETTINGS_ACTIVITY) activity: IActivity, factory: ViewModelProvider.Factory) : IViewModelFactory<AbstractRoomSettingsActivityViewModel> {

    private val viewModel = ViewModelProvider(activity.getActivity(), factory).get(AbstractRoomSettingsActivityViewModel::class.java);

    override fun getViewModel(): AbstractRoomSettingsActivityViewModel {
        return viewModel;
    }
}