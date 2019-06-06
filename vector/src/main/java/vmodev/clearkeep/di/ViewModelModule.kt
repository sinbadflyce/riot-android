package vmodev.clearkeep.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.view.View
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vmodev.clearkeep.viewmodelproviderfactories.ClearKeepViewModelProviderFactory
import vmodev.clearkeep.viewmodels.*
import vmodev.clearkeep.viewmodels.interfaces.*

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AbstractUserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomViewModel::class)
    abstract fun bindRoomViewModel(roomViewModel: RoomViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractHomeScreenActivityViewModel::class)
    abstract fun bindHomeScreenActivityViewModel(viewModel: HomeScreenActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractDirectMessageFragmentViewModel::class)
    abstract fun bindDirectMessageFragmentViewModel(viewModel: DirectMessageFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractHomeScreenFragmentViewModel::class)
    abstract fun bindHomeScreenFragmentViewModel(viewModel: HomeScreenFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractRoomFragmentViewModel::class)
    abstract fun bindRoomFragmentViewModel(viewModel: RoomFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractFavouritesFragmentViewModel::class)
    abstract fun bindFavouritesFragmentViewModel(viewModel: FavouritesFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractContactFragmentViewModel::class)
    abstract fun bindContactFragmentViewModel(viewModel: ContactFragmentViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractProfileSettingsActivityViewModel::class)
    abstract fun bindProfileSettingsActivityViewModel(viewModel: ProfileSettingsActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractEditProfileActivityViewModel::class)
    abstract fun bindEditProfileActivityViewModel(viewModel: EditProfileActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractCallSettingActivityViewModel::class)
    abstract fun bindAbstractCallSettingActivityViewModel(view: CallSettingsActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractNotificationSettingsActivityViewModel::class)
    abstract fun bindAbstractNotificationSettingsActivityViewModel(viewModel: NotificationSettingsActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractMessageListActivityViewModel::class)
    abstract fun bindMessageListActivityViewModel(viewModel: MessageListActivityViewModel): ViewModel;

    @Binds
    @IntoMap
    @ViewModelKey(AbstractSplashActivityViewModel::class)
    abstract fun bindAbstractSplashActivityModule(viewModel: SplashActivityViewModel): ViewModel;

    @Binds
    abstract fun bindViewModelFactory(clearKeepViewModelProviderFactory: ClearKeepViewModelProviderFactory): ViewModelProvider.Factory;
}