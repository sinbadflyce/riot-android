package vmodev.clearkeep.viewmodels.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vmodev.clearkeep.viewmodelobjects.DeviceSettings
import vmodev.clearkeep.viewmodelobjects.Resource

abstract class AbstractCallSettingActivityViewModel : ViewModel() {
    abstract fun getDeviceSettingsResult(): LiveData<Resource<DeviceSettings>>;
    abstract fun setIdForDeviceSettingsResult(id: String);
    abstract fun getChangeDeviceSettingsResult(): LiveData<Resource<DeviceSettings>>;
    abstract fun setChangeDeviceSettingValue(id: String, value: Byte);

    data class ChangeDeviceSettingsObject(val id: String, val value: Byte);
}